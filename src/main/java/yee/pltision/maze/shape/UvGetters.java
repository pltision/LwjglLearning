package yee.pltision.maze.shape;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.util.function.Function;

public interface UvGetters {

    static Function<Integer,Vector2f> square(Vector2f start, Vector2f end){
        return new Function<Integer, Vector2f>() {
            final Vector2f[] coordinates=squareVectorList(start,end);
            @Override
            public Vector2f apply(Integer i) {
                return coordinates[i];
            }
        };
    }
    static Vector2f[] squareVectorList(Vector2f start,Vector2f end){
        return new Vector2f[]{
                new Vector2f(start.x,start.y),
                new Vector2f(end.x,start.y),
                new Vector2f(end.x,end.y),
                new Vector2f(start.x,end.y),
        };
    }
}
