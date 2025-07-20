package yee.pltision.glfmhelper.shape;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface D2Shapes {

    /**
     * @param squareUvGetter {@link UvGetters#quads}, {@link UvGetters#full}
     */
    static List<PosUvVertex> quad(Function<Integer,Vector2f> squareUvGetter, Vector3f... vertexArray){
        ArrayList<PosUvVertex> vertices=new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            vertices.add(new PosUvVertex(vertexArray[i],squareUvGetter.apply(i)));
        }
        return vertices;
    }

    static List<PosUvVertex> square(Function<Integer,Vector2f> squareUvGetter){
        return quad(squareUvGetter,unitSquare());
    }

    @NotNull
    static Vector3f[] unitSquare() {
        return new Vector3f[]{
                new Vector3f(-1, -1,0), // 0
                new Vector3f(+1, -1,0), // 1
                new Vector3f(+1, +1,0), // 2
                new Vector3f(+1, -1,0), // 3
        };
    }

    @NotNull
    static Vector3f[] createQuadArray(Vector2f from, Vector2f to) {
        float minX = from.x;
        float minY = from.y;
        float maxX = to.x;
        float maxY = to.y;

        return new Vector3f[]{
                new Vector3f(minX, minY,0), // 0
                new Vector3f(maxX, minY,0), // 1
                new Vector3f(maxX, maxY,0), // 2
                new Vector3f(minX, maxY,0), // 3
        };
    }
}