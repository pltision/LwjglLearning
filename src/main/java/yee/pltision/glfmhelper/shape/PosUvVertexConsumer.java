package yee.pltision.glfmhelper.shape;

import java.nio.FloatBuffer;

public interface PosUvVertexConsumer<Buffer> extends VertexConsumer<Buffer,PosUvVertex>{

    static PosUvVertexConsumer<FloatBuffer> posUvRgba(){
        return new PosUvVertexConsumer<>() {
            @Override
            public int size() {
                return 3+2+4;
            }

            @Override
            public void apply(FloatBuffer buffer,PosUvVertex vertex) {
                buffer.put(vertex.pos().x).put(vertex.pos().y).put(vertex.pos().z);
                buffer.put(vertex.uv().x).put(vertex.uv().y);
                buffer.put(1).put(1).put(1).put(1);
            }

        };
    }
}
