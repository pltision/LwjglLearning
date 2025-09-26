package yee.pltision.glfmhelper.globject;

import org.intellij.lang.annotations.MagicConstant;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
        this.vertexBuffer=vertexBuffer;
        this.vertexArray=vertexArray;
    }

    /**
     * 无初始化
     */
    public PackedVertexBuffer(){
    }

    /**
     * 如果需要重复使用，记得 delete()
     */
    public void init(){
        vertexBuffer=glGenBuffers();
        vertexArray=glGenVertexArrays();
    }

    public void data(FloatBuffer buf,VertexProperties properties,@MagicConstant(intValues = {GL_STREAM_DRAW,GL_DYNAMIC_DRAW,GL_STATIC_DRAW}) int usage){
        bindBuffer();
        glBufferData(GL_ARRAY_BUFFER,buf,usage);
        apply(properties);
    }

    public void data(IntBuffer buf, VertexProperties properties, @MagicConstant(intValues = {GL_STREAM_DRAW,GL_DYNAMIC_DRAW,GL_STATIC_DRAW}) int usage){
        bindBuffer();
        glBufferData(GL_ARRAY_BUFFER,buf,usage);
        apply(properties);
    }

    public void data(int size, VertexProperties properties, @MagicConstant(intValues = {GL_STREAM_DRAW,GL_DYNAMIC_DRAW,GL_STATIC_DRAW}) int usage){
        bindBuffer();
        glBufferData(GL_ARRAY_BUFFER,size,usage);
        apply(properties);
    }

    public void subData(FloatBuffer buf,int offset){
        bindBuffer();
        glBufferSubData(GL_ARRAY_BUFFER,offset,buf);
    }
    public void subData(IntBuffer buf,int offset){
        bindBuffer();
        glBufferSubData(GL_ARRAY_BUFFER,offset,buf);
    }
    public void subData(int[] buf,int offset){
        bindBuffer();
        glBufferSubData(GL_ARRAY_BUFFER,offset,buf);
    }


    public static PackedVertexBuffer create(VertexProperties properties, FloatBuffer buf){
        PackedVertexBuffer vertexes=new PackedVertexBuffer();
        vertexes.init();
        vertexes.data(buf,properties,GL_STATIC_DRAW);
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

    public void bindBuffer(){
        glBindBuffer(GL_ARRAY_BUFFER,vertexBuffer);
    }

    public void bind(){
        glBindVertexArray(vertexArray);
    }

    public void delete() {
        glDeleteVertexArrays(vertexArray);
        glDeleteVertexArrays(vertexBuffer);
    }

    public boolean isNotEmpty(){
        return vertexArray!=0||vertexBuffer!=0;
    }

    public void deleteIfNotEmpty(){
        if(isNotEmpty())
            delete();
    }

}