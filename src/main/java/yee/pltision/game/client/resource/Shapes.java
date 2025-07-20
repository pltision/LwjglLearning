package yee.pltision.game.client.resource;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.*;
import yee.pltision.glfmhelper.shape.*;

import java.nio.FloatBuffer;

public interface Shapes {

    ShapeRecord PUAMILA=squareTexture(Shaders.TEXTURE_SHADER);

    static ShapeRecord squareTexture(ShaderProgram shaderProgram){
        ShapeBuilder<FloatBuffer> builder = new ShapeBuilder<FloatBuffer>()
                .join(D2Shapes.square(UvGetters.full()), PosUvVertexConsumer.posUvRgba());
        FloatBuffer buffer = builder.applyToBuffer(MemoryUtil::memAllocFloat);
        buffer.flip();
        builder.countVertex();
        PackedVertexBuffer packedVertexBuffer=PackedVertexBuffer.create(VertexProperties.posUvRgba(),buffer);
        MemoryUtil.memFree(buffer);
        return new ShapeRecord(packedVertexBuffer, GL11.GL_QUADS, 0, builder.countVertex(), shaderProgram);
    }
}