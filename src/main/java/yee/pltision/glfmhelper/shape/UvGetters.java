package yee.pltision.glfmhelper.shape;

import org.joml.Vector2f;

import java.util.function.Function;

public interface UvGetters {

    static Function<Integer,Vector2f> full(){
        return quads(new Vector2f(0,0),new Vector2f(1,1));
    }

    /**
     * 可以循环提供矩形的四个顶点
     */
    static Function<Integer,Vector2f> quads(Vector2f start, Vector2f end){
        return new Function<>() {
            final Vector2f[] coordinates=squareVectorList(start,end);
            @Override
            public Vector2f apply(Integer i) {
                return coordinates[i%4];
            }
        };
    }
    static Vector2f[] squareVectorList(Vector2f start,Vector2f end){
        return new Vector2f[]{
                //真不确定y要不要反过来了
                new Vector2f(start.x,end.y),
                new Vector2f(end.x,end.y),
                new Vector2f(end.x,start.y),
                new Vector2f(start.x,start.y),
        };
    }

    /**
     * Texture shape like this:
     * FRBL
     * UD__
     * Letters are square face, _ is transplanting
     *
     */
    static Function<Integer,Vector2f> cubeFaces(Vector2f start, Vector2f size){
        return new Function<Integer, Vector2f>() {
            final Vector2f[][] coordinates=createCoordinates();

            Vector2f[][] createCoordinates(){
                Vector2f f=new Vector2f(),t=new Vector2f();
                return new Vector2f[][]{
                        squareVectorList(f.set(start),t.set(f).add(size)),
                        squareVectorList(f.set(start).add(size.x,0),t.set(f).add(size)),
                        squareVectorList(f.set(start).add(size.x*2,0),t.set(f).add(size)),
                        squareVectorList(f.set(start).add(size.x*3,0),t.set(f).add(size)),
                        squareVectorList(f.set(start).add(0,size.y),t.set(f).add(size)),
                        squareVectorList(f.set(start).add(size.x,size.y),t.set(f).add(size)),
                };
            }
            @Override
            public Vector2f apply(Integer i) {
                return coordinates[i/4][i%4];
            }
        };
    }

}
