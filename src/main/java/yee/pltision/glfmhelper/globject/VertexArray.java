package yee.pltision.glfmhelper.globject;

import kotlin.Pair;

import java.nio.FloatBuffer;

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

    public void bind(){
        glBindVertexArray(vertexArray);
    }

    public void bindBuffer(VertexBuffer buffer,VertexProperties properties){
        bindBuffers(new Pair<>(buffer,properties));
    }

    /**
     * 将多个顶点缓冲区(VertexBuffer)绑定到当前顶点数组对象，并配置顶点属性
     * 这是OpenGL渲染管线中配置顶点数据的关键步骤
     *
     * @param pairs 顶点缓冲区与顶点属性配置的配对数组
     *              每个配对包含一个要绑定的VertexBuffer和其对应的VertexProperties配置
     */
    @SafeVarargs
    public final void bindBuffers(Pair<VertexBuffer, VertexProperties>... pairs) {
        bind();

        // 顶点属性位置计数器，从0开始依次绑定不同的顶点属性
        int location = 0;

        // 遍历所有顶点缓冲区-属性对
        for (Pair<VertexBuffer, VertexProperties> pair : pairs) {
            // 记录每个元素的字节偏移量
            int byteOffset = 0;

            // 绑定当前顶点缓冲区
            pair.component1().bind();

            for (VertexProperties.Element element : pair.component2().elements) {
                glEnableVertexAttribArray(location);
                if(element.type==GLDataType.INT){
                    glVertexAttribIPointer(location, element.count, element.type.type, pair.component2().size, byteOffset);
                }
                else {
                    glVertexAttribPointer(location, element.count, element.type.type, false, pair.component2().size, byteOffset);
                }
//                glVertexAttribPointer(location, element.count, element.type.type, false, pair.component2().size, byteOffset/*表示当前Element在当前VBO中的偏移量*/);

                byteOffset += element.count * element.type.size;
                location++;
            }
        }
    }

    public static <A,B> Pair<A,B> pair(A a,B b){
        return new Pair<>(a,b);
    }

    public static VertexArray createSimple(VertexProperties properties, FloatBuffer buf){
        VertexBuffer buffer=VertexBuffer.create();
        buffer.data(buf, GL_STATIC_DRAW);
        VertexArray vertexArray=VertexArray.create();
        vertexArray.bindBuffer(buffer,properties);
        return vertexArray;
    }
}