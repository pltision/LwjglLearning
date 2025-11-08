package test.chunk;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//滑动缓存
class SlideCache<E> {

    static class CacheEntry<E>{
        Vector2i pos;
        @Nullable E element;

        public CacheEntry(int x, int y){
            pos=new Vector2i(x,y);
        }
    }

    @NotNull CacheEntry<E> [] cache;
    int radius;
    int size;
    Vector2i position;

    Level level;

    public SlideCache(int radius, Vector2i position) {
        this.radius = radius;
        this.size = radius * 2 + 1;

        this.position = new Vector2i(position);

        @SuppressWarnings("unchecked")
        CacheEntry<E>[] cache = (CacheEntry<E>[]) new CacheEntry[size * size];
        Arrays.setAll(cache,(i)->
                new CacheEntry<>(mapToWorldPos(i/size),mapToWorldPos(i%size))
        );
        this.cache=cache;

    }

    public @NotNull CacheEntry<E> viewCacheArray(int i, int j) {
        return cache[i*size+j];
    }

    public int getSize() {
        return size;
    }


    public boolean isAlive(CacheEntry<E> entry, int x, int y) {
        return mapToWorldPos(x)==entry.pos.x&&mapToWorldPos(y)==entry.pos.y;
    }

//    public record UpdateResult<E>(List<CacheElement<E>> invalidElements,List<Vector2i> requiringElements){}

    public record CacheElement<E>(E element,Vector2i pos){}


    /*
     * 更新缓存位置
     * @param position 新位置
     * @return 无效元素列表
     */
    public ArrayList<CacheElement<E>> updatePosition(Vector2i position){
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
                int worldX=mapToWorldPos(i);
                int worldY=mapToWorldPos(j);
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

    interface CacheConsumer<E>{
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
    public int mapToWorldPos(int cachePos) {
        return  Math.floorDiv(position.x - cachePos + radius, size) * size + cachePos;
    }

    // 将世界坐标向量映射到缓存索引
    public int mapToCacheIndex(int pos) {
        return Math.floorMod(pos, size);
    }

    public void setPosition(Vector2i pos) {
        this.position.set(pos);
    }

}
