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
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Pipe {

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

	public static void bindShader(int shader,String path) throws IOException {
		glShaderSource(shader,new String(Files.readAllBytes(Paths.get(path))));
		glCompileShader(shader);

		int[] isSuccess={0};
		glGetShaderiv(shader, GL_COMPILE_STATUS, isSuccess);
		if (isSuccess[0] == 0) {
			throw new RuntimeException(glGetShaderInfoLog(shader));
		}
	}

	private void loop() throws Throwable{
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.125f, 0.25f, 0.0f);

		Matrix4f matrix4f=new Matrix4f();
		float[] matrixBuffer=new float[16];
		float deg=0;

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


		float[] vertexes={
                (float) sin(2*PI/3), (float) cos(2*PI/3),0, 1,1,0,
                (float) sin(2*PI/3*2), (float) cos(2*PI/3*2),0, 0,1,1,
                (float) sin(0), (float) cos(0),0, 1,0,1
		};

		int vbo=glGenBuffers();
		int vao=glGenVertexArrays();

		glBindVertexArray(vao);

		glBindBuffer(GL_ARRAY_BUFFER,vbo);
		glBufferData(GL_ARRAY_BUFFER,vertexes,GL_STATIC_DRAW);

		glVertexAttribPointer(0,3,GL_FLOAT,false,6*4,0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1,3,GL_FLOAT,false,6*4,3*4);
		glEnableVertexAttribArray(1);

		int vertexShader=glCreateShader(GL_VERTEX_SHADER);
		bindShader(vertexShader,"temp/texture.vs.glsl");

		int fragmentShader=glCreateShader(GL_FRAGMENT_SHADER);
		bindShader(fragmentShader,"temp/texture.fs.glsl");

		int shaderProgram=glCreateProgram();
		glAttachShader(shaderProgram,vertexShader);
		glAttachShader(shaderProgram,fragmentShader);
		glLinkProgram(shaderProgram);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer.

			glPushMatrix();

			matrix4f.get(matrixBuffer);
//			glMultMatrixf(matrixBuffer);
			glRotatef(0,0,0,deg);	//不知为何矩阵和旋转都不起作用

//			glBindTexture(GL_TEXTURE_2D,textureHolder);

//			if(deg%20>5) {
				glUseProgram(shaderProgram);
				glBindVertexArray(vao);
				glDrawArrays(GL_TRIANGLES, 0, 3);
				glBindVertexArray(0);
//			}

			glfwSwapBuffers(window); // swap the color buffers

			glPopMatrix();
			matrix4f.rotateY(1f);
			deg+=1;

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}


		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteProgram(shaderProgram);
	}

	public static void main(String[] args) throws Throwable{
		new Pipe().run();
	}


}