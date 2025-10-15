package yee.pltision.game.client;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import yee.pltision.game.client.level.RenderingWorld;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ClientWindow {

    long window;

    @SuppressWarnings("unused")
    public static void main(String... args) {
        ClientWindow app=new ClientWindow();
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
        window = glfwCreateWindow(1080, 1080, "Hello World!", NULL, NULL);
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

    public static float debugI=0;

    void windowLoop(){

        GL.createCapabilities();

        RenderingWorld renderingWorld=new RenderingWorld();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        //处理相机旋转
        /*glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_Q && action == GLFW_RELEASE )
                renderingWorld.camera.tryTurnLeft();
            if ( key == GLFW_KEY_E && action == GLFW_RELEASE )
                renderingWorld.camera.tryTurnRight();
        });*/

        Vector3f up=new Vector3f(0,0.01f,0);
        Vector3f down=new Vector3f(0,-0.01f,0);
        Vector3f left=new Vector3f(-0.01f,0,0);
        Vector3f right=new Vector3f(0.01f,0,0);
        Vector3f compute=new Vector3f();

        //游戏上一帧计算用了多久
        float passedTime=0; //初始值应该是每帧的时间，但因为我没有限制帧率，所以为0

        while (!glfwWindowShouldClose(window)){
            double startTime=glfwGetTime();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if(glfwGetKey(window,GLFW_KEY_W)==GLFW_PRESS){
                renderingWorld.entity.getPosition().add(up.rotate(renderingWorld.zRot,compute));
            }
            if(glfwGetKey(window,GLFW_KEY_S)==GLFW_PRESS){
                renderingWorld.entity.getPosition().add(down.rotate(renderingWorld.zRot,compute));
            }
            if(glfwGetKey(window,GLFW_KEY_A)==GLFW_PRESS){
                renderingWorld.entity.getPosition().add(left.rotate(renderingWorld.zRot,compute));
            }
            if(glfwGetKey(window,GLFW_KEY_D)==GLFW_PRESS){
                renderingWorld.entity.getPosition().add(right.rotate(renderingWorld.zRot,compute));
            }

            if(glfwGetKey(window,GLFW_KEY_Q)==GLFW_PRESS){
                renderingWorld.tryTurnLeft();
            }
            if(glfwGetKey(window,GLFW_KEY_E)==GLFW_PRESS){
                renderingWorld.tryTurnRight();
            }

            debugI+=0.01f;

            renderingWorld.update(passedTime);
//            renderingWorld.camera.pos.set(renderingWorld.entity.getPosition());

//            renderer.render(camera.getStartMatrixStack());
            renderingWorld.render();
//            renderingWorld.camera.zRot+=0.001f;

            glfwSwapBuffers(window); // swap the color buffers
            glfwPollEvents();

            passedTime= (float) (glfwGetTime()-startTime);
        }
    }

}