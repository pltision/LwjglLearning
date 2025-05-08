package yee.pltision.glfmhelper.shape;

import org.joml.Matrix2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.*;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glDrawArrays;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static yee.pltision.glfmhelper.globject.VertexProperties.*;

public class SimpleSquare {

    VertexPackage vertexPackage;
    ShaderProgram program;
    Texture texture;

    int matrixUniform;
    int textureUniform;

    public void init() throws IOException{
        {
            VertexProperties properties=new VertexProperties(pos(),uv(),rgba());
            FloatBuffer buf = MemoryUtil.memAllocFloat(properties.floatSizeOf(3));

            Matrix2f matrix2f=new Matrix2f().rotate((float) (Math.PI/2));
            Vector2f vector2f=new Vector2f(1,1);
            for(int i=0;i<4;i++){
                buf.put(vector2f.x).put(vector2f.y).put(0);
                buf.put(-(vector2f.x+1)/2).put((vector2f.y+1)/2);
                buf.put(1).put(1).put(1).put(1);
                vector2f.mul(matrix2f);
            }
            buf.flip();

            vertexPackage=VertexPackage.create(properties,buf);

            MemoryUtil.memFree(buf);

        }

        program=ShaderProgram.create().linkAndDelete(
                Shader.create(GL_VERTEX_SHADER).readResource("maze/texture.vs.glsl"),
                Shader.create(GL_FRAGMENT_SHADER).readResource("maze/texture.fs.glsl")
        );
        matrixUniform = glGetUniformLocation(program.getShaderProgram(), "transform");

        texture=Texture.read("maze/test_image.png");

        textureUniform = glGetUniformLocation(program.getShaderProgram(), "textureSampler");
        glUniform1i(textureUniform, 0);


    }

    Matrix4f matrix4f=new Matrix4f();
    float[] buffer=new float[16];

    public void render(){
        program.use();
        vertexPackage.bind();
        texture.bind();
        glUniformMatrix4fv(matrixUniform,false, matrix4f.get(buffer));
        glDrawArrays(GL_QUADS, 0, 4);
        matrix4f.rotateZ(0.001f);
//        System.out.println(matrix4f);
    }

    public void delete(){
        vertexPackage.delete();
        program.delete();
        texture.delete();
    }


}
