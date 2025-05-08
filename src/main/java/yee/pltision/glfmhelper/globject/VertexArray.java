package yee.pltision.glfmhelper.globject;

import static org.lwjgl.opengl.GL30.*;

public class VertexArray implements GLObject {
    int vertexArray;

    public VertexArray(int vertexArray){
        this.vertexArray=vertexArray;
    }

    public static VertexArray create(){
        return new VertexArray(glGenVertexArrays());
    }

    @Override
    public void delete() {
        glDeleteVertexArrays(vertexArray);
    }

    public int getVertexArray() {
        return vertexArray;
    }


}
