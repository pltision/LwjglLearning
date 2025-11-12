package test;

import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;
import yee.pltision.glfmhelper.globject.Shader;
import yee.pltision.glfmhelper.globject.ShaderProgram;
import yee.pltision.glfmhelper.globject.Texture;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GPUNoiseGenerator {
    // 窗口和纹理参数
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;

    private static final int SAVE_WIDTH = 2048;
    private static final int SAVE_HEIGHT = 2048;

    private static final String OUTPUT_FILE = "gpu_perlin_noise.png";

    // 窗口句柄
    private long window;

    // OpenGL资源
    private ShaderProgram shaderProgram;
    private int vao, vbo, ebo;
    private int fbo;

    // 保存用
//    private int saveFbo;
    private Texture saveTexture;

    // 噪声参数
    private float graphScale = 1.0f;
    private float time = 0.0f;
    private boolean animate = false;


    public void run() {
        System.out.println("LWJGL " + Version.getVersion() + " GPU噪声生成器");

        try {
            init();
            loop();

            // 保存噪声纹理
            saveNoiseTexture();

            // 清理资源
            cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 终止GLFW
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void init() {
        // 设置错误回调
        GLFWErrorCallback.createPrint(System.err).set();

        // 初始化GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("无法初始化GLFW");
        }

        // 配置GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // macOS兼容设置
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        // 创建窗口
        window = glfwCreateWindow(WIDTH, HEIGHT, "Make Some Noise!", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("无法创建GLFW窗口");
        }

        // 设置输入回调
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
            if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
                saveNoiseTexture();
                System.out.println("噪声已保存到: " + OUTPUT_FILE);
            }
            if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
                animate = !animate;
                System.out.println("动画 " + (animate ? "开启" : "关闭"));
            }
            if (key == GLFW_KEY_UP && action == GLFW_RELEASE) {
                graphScale /= 2f;
                System.out.println("噪声缩放: " + graphScale);
            }
            if (key == GLFW_KEY_DOWN && action == GLFW_RELEASE) {
                graphScale *= 2f;
                System.out.println("噪声缩放: " + graphScale);
            }
        });

        // 窗口居中
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        // 创建OpenGL上下文
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // 垂直同步
        glfwShowWindow(window);

        // 初始化OpenGL
        GL.createCapabilities();

        // 配置OpenGL
        glClearColor(0, 0, 0, 0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // 初始化资源
        initShaders();
        initQuad();
        initFBO();

        initRandom();
    }

    private void initShaders() {
        // 创建着色器程序
        try {
            shaderProgram=ShaderProgram.create().linkAndDelete(
                    Shader.create(GL_VERTEX_SHADER).readResource("noise/noise.vs.glsl"),
                    Shader.create(GL_FRAGMENT_SHADER).readResource("noise/noise.fs.glsl")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void initQuad() {
        // 全屏四边形顶点数据
        float[] vertices = {
                // 位置          // 纹理坐标
                -1.0f, -1.0f,  0.0f, 0.0f,
                1.0f, -1.0f,  1.0f, 0.0f,
                1.0f,  1.0f,  1.0f, 1.0f,
                -1.0f,  1.0f,  0.0f, 1.0f
        };

        // 索引数据
        int[] indices = {
                0, 1, 2,
                0, 2, 3
        };

        // 创建VAO
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // 创建VBO
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // 创建EBO
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // 配置顶点属性
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // 解绑
        glBindVertexArray(0);
    }

    private void initFBO() {
        // 创建噪声纹理
        saveTexture=Texture.createEmpty(SAVE_WIDTH,SAVE_HEIGHT);

        // 创建FBO
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, saveTexture.getTexture(), 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("FBO初始化失败");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void initRandom(){
        int randomTexturesUniform=glGetUniformLocation(shaderProgram.getShaderProgram(),"randomTextures");
        int texturesLength=10;

        int size= 256;

        // 生成随机噪声纹理
        int[] textures=new int[texturesLength];
        int[] textureBuffer = new int[size*size*size];
        Random random=new Random(330);

//        System.out.println(texturesLength);
        glGenTextures(textures);
        for (int i = 0; i < texturesLength; i++) {
//            System.out.println(textures[i]);
            glBindTexture(GL_TEXTURE_3D, textures[i]);
            fillRandomBuffer(textureBuffer,random);
            glTexImage3D(GL_TEXTURE_3D, 0, GL_RGBA, size, size, size, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer);
            glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_R, GL_REPEAT);
            glUniform1i(randomTexturesUniform+i,textures[i]);
        }


    }

    void fillRandomBuffer(int[] buffer,Random random){
        for(int i=0;i<buffer.length;i++){
            buffer[i]=random.nextInt()|(0xff<<24);
        }
    }

    private void loop() {
        // 渲染循环
        while (!glfwWindowShouldClose(window)) {
            // 计算时间
            if (animate) {
                time += 0.00001f;
            }

//            graphScale*=1.0001f;

            // 清除屏幕
            glClear(GL_COLOR_BUFFER_BIT);

            // 使用着色器程序
            shaderProgram.use();

            // 更新uniform变量
            glUniform1f(glGetUniformLocation(shaderProgram.getShaderProgram(), "graphScale"), graphScale);
            glUniform1f(glGetUniformLocation(shaderProgram.getShaderProgram(), "time"), time);
            glUniform1i(glGetUniformLocation(shaderProgram.getShaderProgram(), "animate"), animate ? 1 : 0);

            // 绘制四边形
            glBindVertexArray(vao);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            // 交换缓冲区
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void saveNoiseTexture() {
        // 绑定FBO并渲染
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glViewport(0, 0, SAVE_WIDTH, SAVE_HEIGHT);
        shaderProgram.use();
        glUniform1f(glGetUniformLocation(shaderProgram.getShaderProgram(), "graphScale"), graphScale);
        glUniform1f(glGetUniformLocation(shaderProgram.getShaderProgram(), "time"), time);
        glUniform1i(glGetUniformLocation(shaderProgram.getShaderProgram(), "animate"), 0);
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        // 读取像素数据
        ByteBuffer pixels = BufferUtils.createByteBuffer(SAVE_WIDTH * SAVE_HEIGHT * 4);
        glReadPixels(0, 0, SAVE_WIDTH, SAVE_HEIGHT, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        // 保存为PNG
        STBImageWrite.stbi_flip_vertically_on_write(true);
        boolean success = STBImageWrite.stbi_write_png(
                OUTPUT_FILE,
                SAVE_WIDTH, SAVE_HEIGHT,
                4,
                pixels,
                SAVE_WIDTH * 4
        );

        if (!success) {
            System.err.println("无法保存噪声纹理");
        }

        // 解绑FBO
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        // 关键修复：将视口重置为窗口原始尺寸（WIDTH x HEIGHT）
        glViewport(0, 0, WIDTH, HEIGHT);
    }

    private void cleanup() {
        // 删除OpenGL资源
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        saveTexture.delete();
        glDeleteFramebuffers(fbo);
        shaderProgram.delete();
    }

    public static void main(String[] args) {
        new GPUNoiseGenerator().run();
    }
}
