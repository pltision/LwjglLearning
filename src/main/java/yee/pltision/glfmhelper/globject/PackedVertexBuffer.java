package yee.pltision.glfmhelper.globject;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class PackedVertexBuffer implements GLObject {
    int vertexBuffer;
    int vertexArray;

    public int getVertexBuffer() {
        return vertexBuffer;
    }

    public int getVertexArray() {
        return vertexArray;
    }

    public PackedVertexBuffer(int vertexBuffer, int vertexArray){
        this.vertexArray=vertexArray;
        this.vertexBuffer =vertexBuffer;
    }

    public static PackedVertexBuffer create(VertexProperties properties, ByteBuffer buf){
        PackedVertexBuffer vertexes=new PackedVertexBuffer(glGenBuffers(),glGenVertexArrays());
        glBindBuffer(GL_ARRAY_BUFFER,vertexes.vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER,buf,GL_STATIC_DRAW);
        vertexes.apply(properties);
        return vertexes;
    }

    public static PackedVertexBuffer create(VertexProperties properties, FloatBuffer buf){
        PackedVertexBuffer vertexes=new PackedVertexBuffer(glGenBuffers(),glGenVertexArrays());
        glBindBuffer(GL_ARRAY_BUFFER,vertexes.vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER,buf,GL_STATIC_DRAW);
        vertexes.apply(properties);
        return vertexes;
    }

    public static PackedVertexBuffer createAndFree(VertexProperties properties, FloatBuffer buf){
        PackedVertexBuffer packedVertexBuffer =create(properties,buf);
        MemoryUtil.memFree(buf);
        return packedVertexBuffer;
    }

    public void apply(VertexProperties properties){
        bind();
        properties.apply();
    }

    public void delete() {
        glDeleteVertexArrays(vertexArray);
        glDeleteVertexArrays(vertexBuffer);
    }

    public void bind(){
        glBindVertexArray(vertexArray);
    }

}