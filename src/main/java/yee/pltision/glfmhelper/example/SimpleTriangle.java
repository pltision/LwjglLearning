package yee.pltision.glfmhelper.example;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.*;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glDrawArrays;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static yee.pltision.glfmhelper.globject.VertexProperties.*;

public class SimpleTriangle {

    PackedVertexBuffer packedVertexBuffer;
    ShaderProgram program;
    Texture texture;

    int matrixUniform;
    int textureUniform;

    public void init() throws IOException{
        {
            VertexProperties properties=new VertexProperties(pos(),uv(),rgba());
            FloatBuffer buf = MemoryUtil.memAllocFloat(properties.floatSizeOf(3));

            for(int i=0;i<3;i++){
                buf.put((float) Math.sin(i * 2 / 3d * Math.PI)).put((float) Math.cos(i * 2 / 3d * Math.PI)).put(0);
                buf.put((1+(float) Math.sin(i * 2 / 3d * Math.PI))/2).put(-(1+(float) Math.cos(i * 2 / 3d * Math.PI))/2);
                buf.put(1).put(1).put(1).put(1);
            }
            buf.flip();

            packedVertexBuffer = PackedVertexBuffer.create(properties,buf);

            MemoryUtil.memFree(buf);

        }

        program=ShaderProgram.create().linkAndDelete(
                Shader.create(GL_VERTEX_SHADER).readResource("maze/texture.vs.glsl"),
                Shader.create(GL_FRAGMENT_SHADER).readResource("maze/texture.fs.glsl")
        );
        matrixUniform = glGetUniformLocation(program.getShaderProgram(), "transform");

        texture=Texture.read("测试图片.png");

        textureUniform = glGetUniformLocation(program.getShaderProgram(), "textureSampler");
        glUniform1i(textureUniform, 0);


    }

    Matrix4f matrix4f=new Matrix4f();
    float[] buffer=new float[16];

    public void render(){
        program.use();
        packedVertexBuffer.bind();
        texture.bind();
        glUniformMatrix4fv(matrixUniform,false, matrix4f.get(buffer));
        glDrawArrays(GL_TRIANGLES, 0, 3);
        matrix4f.rotateZ(0.001f);
//        System.out.println(matrix4f);
    }

    public void delete(){
        packedVertexBuffer.delete();
        program.delete();
        texture.delete();
    }


}
