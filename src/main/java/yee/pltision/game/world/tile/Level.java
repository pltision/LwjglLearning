package yee.pltision.game.world.tile;

public interface Level extends TileGetter {
    int CHUNK_SIZE=16;

    TileState mapping(int id);

    int getTileRaw(int x, int y);

    default TileState getTile(int x, int y) {
        return mapping(getTileRaw(x, y));
    }

    default int[] getChunkRaw(int chunkX, int chunkY) {
        int[] chunk=new int[CHUNK_SIZE*CHUNK_SIZE];
        for(int i=0;i<CHUNK_SIZE;i++){
            for(int j=0;j<CHUNK_SIZE;j++){
                chunk[i*CHUNK_SIZE+j]=getTileRaw(chunkX*CHUNK_SIZE+i,chunkY*CHUNK_SIZE+j);
            }
        }
        return chunk;
    }

    default void setTurf(int x, int y, TileState tile){
        //Unmodifiable
    }




}