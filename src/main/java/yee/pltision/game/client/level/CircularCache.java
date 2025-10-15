package yee.pltision.game.client.level;

import org.joml.Vector2i;

import java.util.function.BiFunction;

/**
 * 循环缓存区，可用于缓存玩家附近的区块
 * @param <E>
 */
public class CircularCache<E> {
    Object[][] cache;
    int radius;
    int size;
    Vector2i position;  // 中心位置

    public CircularCache(int radius,Vector2i position) {
        this.radius = radius;
        this.size = radius * 2 + 1;
        this.cache = new Object[size][size];
        // 初始化缓存中的向量对象

        this.position = new Vector2i(position);

    }

    @SuppressWarnings("unchecked")
    public E getInCacheArray(int i, int j) {
        return (E) cache[i][j];
    }

    public int getArraySize() {
        return size;
    }

    public void foreach(BiFunction<Vector2i,E,E> function) {
        Vector2i worldPos=new Vector2i();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mapToWorldPos(i, j, worldPos);
                cache[i][j]=function.apply(worldPos,getInCacheArray(i,j));
            }
        }
    }

    // 将缓存索引映射到世界坐标向量
    public void mapToWorldPos(int cacheX, int cacheY, Vector2i result) {
        int worldX = (Math.floorDiv(position.x - cacheX + radius, size)) * size + cacheX;
        int worldY = (Math.floorDiv(position.y - cacheY + radius, size)) * size + cacheY;
        result.set(worldX, worldY);
    }

    // 将世界坐标向量映射到缓存索引
    public void mapToCacheIndex(Vector2i worldPos, Vector2i result) {
        result.x = Math.floorMod(worldPos.x, size);
        result.y = Math.floorMod(worldPos.y, size);
    }

    public E get(Vector2i worldPos) {
        Vector2i cachePos=new Vector2i();
        mapToCacheIndex(worldPos,cachePos);
        return getInCacheArray(cachePos.x,cachePos.y);
    }

    public void setPosition(Vector2i pos) {
        this.position.set(pos);
    }

    public void setPosition(int x, int y) {
        this.position.set(x, y);
    }
}