package yee.pltision.glfmhelper.globject;

import org.intellij.lang.annotations.MagicConstant;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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

    public void data(FloatBuffer buf, VertexProperties properties, @MagicConstant(intValues = {GL_STREAM_DRAW,GL_DYNAMIC_DRAW,GL_STATIC_DRAW}) int usage){
        bind();
        glBufferData(GL_ARRAY_BUFFER,buf,usage);
    }

    public void data(IntBuffer buf, VertexProperties properties, @MagicConstant(intValues = {GL_STREAM_DRAW,GL_DYNAMIC_DRAW,GL_STATIC_DRAW}) int usage){
        bind();
        glBufferData(GL_ARRAY_BUFFER,buf,usage);
    }

    public void data(int size, VertexProperties properties, @MagicConstant(intValues = {GL_STREAM_DRAW,GL_DYNAMIC_DRAW,GL_STATIC_DRAW}) int usage){
        bind();
        glBufferData(GL_ARRAY_BUFFER,size,usage);
    }

    public void subData(FloatBuffer buf,int offset){
        bind();
        glBufferSubData(GL_ARRAY_BUFFER,offset,buf);
    }
    public void subData(IntBuffer buf,int offset){
        bind();
        glBufferSubData(GL_ARRAY_BUFFER,offset,buf);
    }
    public void subData(int[] buf,int offset){
        bind();
        glBufferSubData(GL_ARRAY_BUFFER,offset,buf);
    }
}
