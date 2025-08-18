package yee.pltision.game.client.resource;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.PackedVertexBuffer;
import yee.pltision.glfmhelper.globject.ShaderProgram;
import yee.pltision.glfmhelper.globject.VertexProperties;
import yee.pltision.glfmhelper.shape.*;

import java.nio.FloatBuffer;

public interface Shapes {

    ShapeRecord PUAMILA= textureChar(2f,Shaders.TEXTURE_SHADER);
    ShapeRecord TURF=turf(Shaders.TEXTURE_SHADER);

    static ShapeRecord textureChar(float size, ShaderProgram shaderProgram){
        ShapeBuilder<FloatBuffer> builder = new ShapeBuilder<FloatBuffer>()
                .join(D2Shapes.square(new Vector3f(0,size/2,0),size,UvGetters.full()), PosUvVertexConsumer.posUvRgba());
        FloatBuffer buffer = builder.applyToBuffer(MemoryUtil::memAllocFloat);
        buffer.flip();
//        builder.countVertex();
        PackedVertexBuffer packedVertexBuffer=PackedVertexBuffer.create(VertexProperties.posUvRgba(),buffer);
        MemoryUtil.memFree(buffer);
        return new ShapeRecord(packedVertexBuffer, GL11.GL_QUADS, 0, builder.countVertex(), shaderProgram);
    }

    static ShapeRecord turf(ShaderProgram shaderProgram){
        ShapeBuilder<FloatBuffer> builder = new ShapeBuilder<FloatBuffer>()
                .join(D2Shapes.squareFromZero(1,UvGetters.full()), PosUvVertexConsumer.posUvRgba());
        FloatBuffer buffer = builder.applyToBuffer(MemoryUtil::memAllocFloat);
        buffer.flip();
        PackedVertexBuffer packedVertexBuffer=PackedVertexBuffer.create(VertexProperties.posUvRgba(),buffer);
        MemoryUtil.memFree(buffer);
        return new ShapeRecord(packedVertexBuffer, GL11.GL_QUADS, 0, builder.countVertex(), shaderProgram);
    }
}