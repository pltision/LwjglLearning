package org.example;

import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class HelloWorld {

	// The window handle
	private long window;

	public void run() throws Throwable {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
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
		window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
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

//	static void ARGBToRGBA(int [] array){
//		for(int i=0,to=array.length;i<to;i++){
//			array[i]=(array[i]>>>24)|(array[i]<<8)/*|0xff*/;
//		}
//	}

	private void loop() throws Throwable{
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1f, 1f, 1f, 0.0f);

		Matrix4f matrix4f=new Matrix4f();
		float[] matrixBuffer=new float[16];
		double deg=0;

		BufferedImage image= ImageIO.read(new File("temp/test.png"));
		int[] imageBuffer=new int[image.getWidth()*image.getHeight()];
		image.getRGB(0,0,image.getWidth(),image.getHeight(),imageBuffer,0,image.getWidth());
//		ARGBToRGBA(imageBuffer);

//		int[] imageBuffer=new int[]{
//				0xff000000,0xffff00ff,
//				0xffff00ff,0x00000000,
//		};

		int textureHolder=glGenTextures();
		glBindTexture(GL_TEXTURE_2D,textureHolder);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,image.getWidth(),image.getHeight(),0,GL_RGBA,GL_UNSIGNED_BYTE,imageBuffer);

		glEnable(GL_TEXTURE_2D);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			glPushMatrix();
			matrix4f.get(matrixBuffer);
			glMultMatrixf(matrixBuffer);

			glBindTexture(GL_TEXTURE_2D,textureHolder);

			glBegin(GL_TRIANGLES);

			glColor3f(0,1,1);
			glTexCoord2f(0,0);
			glVertex2d(cos(deg),sin(deg));

			glColor3f(1,1,0);
			glTexCoord2f(0,1);
			glVertex2d(cos(deg+2*PI/3),sin(deg+2*PI/3));

			glColor3f(1,0,1);
			glTexCoord2f(1,0);
			glVertex2d(cos(deg+4*PI/3),sin(deg+4*PI/3));

			glEnd();

			glPopMatrix();

			glfwSwapBuffers(window); // swap the color buffers

			deg+=0.01;
//			matrix4f.rotateY(0.01f);

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	public static void main(String[] args) throws Throwable{
		new HelloWorld().run();
	}

}