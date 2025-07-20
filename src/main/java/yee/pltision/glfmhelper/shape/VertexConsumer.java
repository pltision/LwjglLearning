package yee.pltision.glfmhelper.shape;

import java.nio.FloatBuffer;

public interface VertexConsumer<Buffer,T> {
    int size();
    void apply(Buffer buffer,T vertex);
}