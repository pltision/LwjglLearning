package org.example;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexHelper {
    int vertexArray;
    int vertexBuffer;

    public static VertexHelper create(){
        VertexHelper helper=new VertexHelper();

        helper.vertexArray=glGenBuffers();
        helper.vertexBuffer=glGenVertexArrays();

        return helper;
    }
    public void delete(){
        glDeleteVertexArrays(vertexArray);
        glDeleteBuffers(vertexBuffer);
    }

}
