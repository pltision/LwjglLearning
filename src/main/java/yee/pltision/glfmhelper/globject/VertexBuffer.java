package yee.pltision.glfmhelper.globject;

import static org.lwjgl.opengl.GL30.*;

public class VertexBuffer implements GLObject{
    int vertexBuffer;

    public VertexBuffer(int vertexBuffer){
        this.vertexBuffer =vertexBuffer;
    }

    public static  VertexBuffer create(){
        return new VertexBuffer(glGenBuffers());
    }

    @Override
    public void delete() {
        glDeleteBuffers(vertexBuffer);
    }

    public void bind(){
        glBindBuffer(GL_ARRAY_BUFFER,vertexBuffer);
    }

    public int getVertexBuffer() {
        return vertexBuffer;
    }

}
