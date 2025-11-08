package test.chunk;

interface Level {
    Chunk getChunk(int x,int y);

    static Level createLevel(){
        return Chunk::new;
    }

    static Level createWithCache(SlideCache cache){
        return ((x, y) -> cache.level.getChunk(x, y));
    }
}

class Chunk{
    //区块用于测试，只包含坐标
    public int x,y;

    boolean isNew=true;

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Player{
    public int x,y;
}
