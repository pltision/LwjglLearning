package yee.pltision.glfmhelper.shape;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.*;
import yee.pltision.maze.RenderContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.demo.util.IOUtils.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class SimpleTriangle {
    VertexStruct struct;
    VertexBuffer buffer;
    ShaderProgram program;
    Texture texture;

    int matrixUniform;
    int textureUniform;

    public void init() throws IOException{
        //  maze/vertex_shader.glsl
        buffer= VertexBuffer.create();
        struct= VertexStruct.create();

        {
            struct.bind();
            FloatBuffer buf = MemoryUtil.memAllocFloat(27);
//            FloatBuffer buf= frame.mallocFloat(100);
            for(int i=0;i<3;i++){
                buf.put((float) Math.sin(i * 2 / 3d * Math.PI)).put((float) Math.cos(i * 2 / 3d * Math.PI)).put(0);
                buf.put((float) Math.sin(i * 2 / 3d * Math.PI)).put((float) Math.cos(i * 2 / 3d * Math.PI));
                buf.put(1).put(1).put(1).put(1);
            }
            buf.flip();
            /*float[] test=new float[27];
            buf.get(test);
            System.out.println(Arrays.toString(test));*/
            /*float[] buf=new float[]{
                    1,0,0, 1,0, 1,1,1,1,
                    0,0,0, 0,0, 1,1,1,1,
                    0,1,0, 0,1, 1,1,1,1,
            };*/
            buffer.bind();
            glBufferData(GL_ARRAY_BUFFER,buf,GL_STATIC_DRAW);
            MemoryUtil.memFree(buf);

            /*glVertexAttribPointer(0,3,GL_FLOAT,false,9*4,0);
            glEnableVertexAttribArray(0);

            glVertexAttribPointer(1,2,GL_FLOAT,false,9*4,3*4);
            glEnableVertexAttribArray(1);

            glVertexAttribPointer(2,4,GL_FLOAT,false,9*4,5*4);
            glEnableVertexAttribArray(2);*/

            struct.stream().pos().uv().rgba().build();
        }

        program=ShaderProgram.create().linkAndDelete(
                Shader.create(GL_VERTEX_SHADER).readResource("maze/vertex_shader.glsl"),
                Shader.create(GL_FRAGMENT_SHADER).readResource("maze/fragment_shader.glsl")
        );
        matrixUniform = glGetUniformLocation(program.getShaderProgram(), "matrix");

        try(MemoryStack frame = MemoryStack.stackPush()) {
            IntBuffer width = frame.mallocInt(1);
            IntBuffer height = frame.mallocInt(1);
            IntBuffer components = frame.mallocInt(1);
            ByteBuffer data = stbi_load_from_memory(
                    ioResourceToByteBuffer("maze/test_image.png", 1024), width, height, components,
                    4);
            texture=Texture.create(GL_TEXTURE_2D);
            texture.bind();
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
            stbi_image_free(data);
        }

        textureUniform = glGetUniformLocation(program.getShaderProgram(), "textureSampler");
        glUniform1i(textureUniform, 0);


    }

    public void render(){
        program.use();
//        buffer.bind();
        struct.bind();
        texture.bind();
        glDrawArrays(GL_TRIANGLES, 0, 3);
//        glUniformMatrix4fv(matrixUniform,false, new float[]{ 1,0,0,0,    0,1,0,0,    0,0,1,0,    0,0,0,1});
//        glBindVertexArray(0);
    }

    public void delete(){
        struct.delete();
        buffer.delete();
    }


}
