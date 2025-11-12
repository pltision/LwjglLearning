package yee.pltision.utils.cache;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

//滑动缓存
public class SlideCache<E> {

    static class CacheEntry<E>{
        Vector2i pos;
        E element;

        public CacheEntry(Vector2i pos, E element){
            this.pos=new Vector2i(pos);
            this.element=element;
        }
    }

    CacheEntry<E> [] cache;
    int radius;
    int size;
    Vector2i position;

    /**
     * 构造默认的元素，如果需要默认值不是null可以使用这个构造方法
     */
    public SlideCache(int radius, Vector2i position, Function<Vector2i,E> initElement) {
        this.radius = radius;
        this.size = radius * 2 + 1;

        this.position = new Vector2i(position);

        @SuppressWarnings("unchecked")
        CacheEntry<E>[] cache = (CacheEntry<E>[]) new CacheEntry[size * size];
        Arrays.setAll(cache,(i)-> {
                    Vector2i pos=new Vector2i(mapToWorldPosX(i/size),mapToWorldPosY(i%size));
                    return new CacheEntry<>(pos,initElement.apply(pos));
                }
        );
        this.cache=cache;

    }

    /**
     * 构造可能存储null的缓存
     */
    public SlideCache(int radius, Vector2i position) {
        this(radius,position,(pos)->null);
    }

    public Vector2i getCenterPos(){
        return position;
    }


    CacheEntry<E> viewCacheArray(int i, int j) {
        return cache[i*size+j];
    }

    public int getSize() {
        return size;
    }


    public record UpdateResult<E>(List<CacheElement<E>> invalidElements,List<Vector2i> requiringElements){}

    public record CacheElement<E>(E element,Vector2i pos){}


    /**
     * 更新缓存的中心位置，离开范围的元素不会被删除，需要自行处理
     * @param position 新的中心位置
     * @return 已经离开范围的元素列表
     */
    public List<CacheElement<E>> updatePosition(Vector2i position){
        //计算每个轴移动了多少个位置（超过size就完全移动了）
        int dx=Math.min(Math.abs(this.position.x-position.x),size);
        int dy=Math.min(Math.abs(this.position.y-position.y),size);
        //重合元素
        int sameSpace=(size-dx)*(size-dy);
        //需要更新的元素=总元素-重合元素
        int diffSpace=size*size-sameSpace;

        ArrayList<CacheElement<E>> invalidElements=new ArrayList<>(diffSpace);
//        ArrayList<Vector2i> requiringElements=new ArrayList<>(diffSpace);

        this.position.set(position);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int worldX=mapToWorldPosX(i);
                int worldY=mapToWorldPosY(j);
                CacheEntry<E> entry=viewCacheArray(i,j);

                if(entry.element!=null){
                    //检查无效元素并更新坐标
                    if((entry.pos.x!=worldX||entry.pos.y!=worldY)){
                        invalidElements.add(new CacheElement<>(entry.element,entry.pos));
                        entry.pos.set(worldX,worldY);
                    }
//                    requiringElements.add(new Vector2i(worldX,worldY));
                }
            }
        }
        return invalidElements;
    }

    public interface CacheConsumer<E>{
        E accept(E cacheElement, int x, int y);
    }

    public void foreachCache(CacheConsumer<E> consumer){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                CacheEntry<E> entry=viewCacheArray(i,j);
                entry.element= consumer.accept(entry.element,entry.pos.x,entry.pos.y);
            }
        }
    }

    // 将缓存索引映射到世界坐标向量
    public int mapToWorldPosX(int cachePos) {
        return  Math.floorDiv(position.x - cachePos + radius, size) * size + cachePos;
    }
    public int mapToWorldPosY(int cachePos) {
        return  Math.floorDiv(position.y - cachePos + radius, size) * size + cachePos;
    }

    // 将世界坐标向量映射到缓存索引
    public int mapToCacheIndex(int pos) {
        return Math.floorMod(pos, size);
    }

    // ---- Setter & Getter ----

    public void set(int x, int y, E element) {
        CacheEntry<E> entry=viewCacheArray(mapToCacheIndex(x),mapToCacheIndex(y));
        entry.element=element;
    }

    public void set(Vector2i pos, E element) {
        set(pos.x,pos.y,element);
    }

    public E get(int x, int y) {
        return x>=position.x-radius&&x<=position.x+radius&&y>=position.y-radius&&y<=position.y+radius
                ? viewCacheArray(mapToCacheIndex(x),mapToCacheIndex(y)).element
                : null;
    }

     public E get(Vector2i pos) {
        return get(pos.x,pos.y);
    }


}
