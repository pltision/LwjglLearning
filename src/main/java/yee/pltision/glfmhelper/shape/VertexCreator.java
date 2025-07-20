package yee.pltision.glfmhelper.shape;

import java.util.List;

@FunctionalInterface
public interface VertexCreator<T> {
    List<T> create();
}