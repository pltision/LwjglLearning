package yee.pltision.glfmhelper.shape;

import org.intellij.lang.annotations.MagicConstant;
import org.lwjgl.opengl.GL11;
import yee.pltision.glfmhelper.globject.PackedVertexBuffer;
import yee.pltision.glfmhelper.globject.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;

/**
 * 表示一个形状记录，用于存储和渲染顶点数据
 * 通过指定的着色器程序和矩阵进行渲染
 */
public class ShapeRecord {
    PackedVertexBuffer vertexBuffer;
    int drawArrayType;
    int drawArrayStart;
    int drawArrayCount;
    ShaderProgram shaderProgram;

    /**
     * @param drawArrayStart OpenGL 常量，如：
     *  {@link GL11#GL_POINTS},
     *  {@link GL11#GL_LINES},
     *  {@link GL11#GL_LINE_LOOP},
     *  {@link GL11#GL_LINE_STRIP},
     *  {@link GL11#GL_TRIANGLES},
     *  {@link GL11#GL_TRIANGLE_STRIP},
     *  {@link GL11#GL_TRIANGLE_FAN},
     *  {@link GL11#GL_QUADS},
     *  {@link GL11#GL_QUAD_STRIP},
     *  {@link GL11#GL_POLYGON}
     */
    public ShapeRecord(
            PackedVertexBuffer vertexBuffer,
            @MagicConstant(intValues = {
                    GL_POINTS,
                    GL_LINES,
                    GL_LINE_LOOP,
                    GL_LINE_STRIP,
                    GL_TRIANGLES,
                    GL_TRIANGLE_STRIP,
                    GL_TRIANGLE_FAN,
                    GL_QUADS, GL_QUAD_STRIP,
                    GL_POLYGON})
            int drawArrayType,
            int drawArrayStart,
            int drawArrayCount,
            ShaderProgram shaderProgram)
    {
        this.vertexBuffer = vertexBuffer;
        this.drawArrayType = drawArrayType;
        this.drawArrayStart = drawArrayStart;
        this.drawArrayCount = drawArrayCount;
        this.shaderProgram = shaderProgram;
    }

    /**
     * 初始化渲染过程，绑定顶点缓冲区并设置着色器程序的矩阵
     */
    public void initRender() {
        vertexBuffer.bind();
        shaderProgram.use();
    }

    /**
     * 执行渲染操作，调用 OpenGL 的绘制函数
     */
    public void render() {
        initRender();
        glDrawArrays(drawArrayType, drawArrayStart, drawArrayCount);
    }

}