package yee.pltision.glfmhelper.shape;

public interface VertexConsumer<Buffer,T> {
    int size();
    void apply(Buffer buffer,T vertex);
}