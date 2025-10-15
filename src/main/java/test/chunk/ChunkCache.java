package test.chunk;

import org.joml.Vector2i;

class ChunkCache {
    Chunk[][] cache;  // 缓存也使用Vector2i存储世界坐标
    int radius;
    int size;
    Vector2i position;  // 位置向量

    Level level;

    public ChunkCache(int radius,Vector2i position,Level level) {
        this.level=level;
        this.radius = radius;
        this.size = radius * 2 + 1;
        this.cache = new Chunk[size][size];
        // 初始化缓存中的向量对象

        this.position = new Vector2i(position);

        fill();
        //第二次填充移除isNew标志（太懒了）
        fill();
    }

    public Chunk viewCacheArray(int i,int j) {
        return cache[i][j];
    }

    public int getSize() {
        return size;
    }

    public void fill() {
        Vector2i worldPos=new Vector2i();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mapToWorldPos(i, j, worldPos);  // 直接将结果存入缓存向量
                if(cache[i][j]==null||cache[i][j].x!=worldPos.x||cache[i][j].y!=worldPos.y){
                    cache[i][j] = level.getChunk(worldPos);
                }
                else {
                    cache[i][j].isNew=false;
                }
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

    public void print() {
        System.out.println("当前位置: " + position);
        System.out.println("缓存内容 (世界坐标向量):");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("(%2d,%2d) ", cache[i][j].x, cache[i][j].y);
            }
            System.out.println();
        }

        System.out.println("\n世界坐标到缓存索引的映射 (局部区域):");
        Vector2i testPos = new Vector2i();
        Vector2i cacheIdx = new Vector2i();

        for (int y = position.y - 2; y <= position.y + 2; y++) {
            for (int x = position.x - 2; x <= position.x + 2; x++) {
                testPos.set(x, y);
                mapToCacheIndex(testPos, cacheIdx);

                boolean inRange = x >= position.x - radius && x <= position.x + radius &&
                        y >= position.y - radius && y <= position.y + radius;

                if (inRange) {
                    System.out.printf("(%2d,%2d) ", cacheIdx.x, cacheIdx.y);
                } else {
                    System.out.print("        ");
                }
            }
            System.out.println();
        }
        System.out.println("----------------------------------------");
    }

    public void setPosition(Vector2i pos) {
        this.position.set(pos);
    }

    public void setPosition(int x, int y) {
        this.position.set(x, y);
    }

    /*public static void main(String[] args) {
        ChunkCache cache = new ChunkCache(2);

        // 测试不同位置
        Vector2i[] positions = {
                new Vector2i(0, 0),
                new Vector2i(1, 1),
                new Vector2i(2, 3),
                new Vector2i(3, 2),
                new Vector2i(5, 5),
                new Vector2i(6, 4)
        };

        for (Vector2i pos : positions) {
            cache.setPosition(pos);
            cache.fill();
            cache.print();
        }
    }*/
}
