/*
package yee.pltision.destroyer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.client.resource.Textures;
import yee.pltision.glfmhelper.globject.PackedVertexBuffer;
import yee.pltision.glfmhelper.globject.ShaderProgram;
import yee.pltision.glfmhelper.globject.Texture;
import yee.pltision.glfmhelper.shape.ShapeRecord;
import yee.pltision.glfmhelper.shape.D3Shapes;
import yee.pltision.glfmhelper.shape.UvGetters;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Destroyer {

    long window;

    public static void main(String[] args) throws IOException {
        Destroyer app=new Destroyer();
        app.init();
        app.windowLoop();
    }

    void init(){
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(1000, 1000, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);


    }
    ShapeRecord renderHelper;
    Matrix4f matrix4f=new Matrix4f();
    float[] buffer=new float[16];
    int matrixUniform;

    void windowLoop(){

        GL.createCapabilities();

        Texture texture= Textures.readTexture("毁灭者模拟器/毁灭者.png");

        {
            ShaderProgram shader;
            PackedVertexBuffer shape;
            shader = Shaders.TEXTURE_SHADER;
            matrixUniform = glGetUniformLocation(shader.getShaderProgram(), "transform");

            float size = 0.05f;
            shape = D3Shapes.cuboid(
                    new Vector3f(-size, -size, -size),
                    new Vector3f(size, size, size),
                    UvGetters.cubeFaces(new Vector2f(0, 0), new Vector2f(16 / 128f, 16 / 128f))
//                UvGetters.allSameSquare(new Vector2f(0,0),new Vector2f(16/128f,16/128f))
            );

            renderHelper = new ShapeRecord(shape, GL_QUADS, 0, 4 * 6, shader);
        }

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        glClearColor(0, 0, 0, 1.0f);

        while (!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//            glUniformMatrix4fv(matrixUniform,false, matrix4f.get(buffer));
            texture.bind();

            FloatBuffer buffer = MemoryUtil.memAllocFloat(4 * 4);
            glUniformMatrix4fv(matrixUniform, false, matrix4f.get(buffer));
            MemoryUtil.memFree(buffer);

            matrix4f.rotateY(0.0002f);
            matrix4f.rotateX(0.0001f);

            glfwSwapBuffers(window); // swap the color buffers
            glfwPollEvents();
        }
    }

}*/
