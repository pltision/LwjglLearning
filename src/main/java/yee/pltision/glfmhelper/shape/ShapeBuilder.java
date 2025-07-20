package yee.pltision.glfmhelper.shape;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * 帮助写入一坨顶点数据的工具类
 */
public class ShapeBuilder<Buffer> {

    record Pair<T, Buffer>(List<T> vertexes, VertexConsumer<Buffer, T> consumer) {
        /**
         * 将顶点数据应用到Buffer
         */
        public void apply(Buffer buffer){
            for(T vertex:vertexes){
                consumer.apply(buffer,vertex);
            }
        }
    }

    /**
     * 存储顶点列表和对应的VertexConsumer
     */
    List<Pair<?,Buffer>> pairs=new LinkedList<>();

    /**
     * 加入一坨顶点
     */
    public <T> ShapeBuilder<Buffer> join(List<T> vertexes, VertexConsumer<Buffer,T> consumer){
        pairs.add(new Pair<>(vertexes,consumer));
        return this;
    }

    /**
     * 计算大小以创建Buffer，将所有顶点数据写入Buffer
     */
    public Buffer applyToBuffer(Function<Integer,Buffer> bufferCreator){
        Buffer buffer=bufferCreator.apply(computeSize());
        for(Pair<?,Buffer> pair:pairs){
            pair.apply(buffer);
        }
        return buffer;
    }

    /**
     * 计算所有顶点数据所需的Buffer大小
     */
    public int computeSize(){
        int size=0;
        for(Pair<?,Buffer> pair:pairs){
            size+=pair.vertexes.size()*pair.consumer.size();
        }
        return size;
    }

    public int countVertex(){
        int size=0;
        for(Pair<?,Buffer> pair:pairs){
            size+=pair.vertexes.size();
        }
        return size;
    }



}