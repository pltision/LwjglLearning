package yee.pltision.game.world.tile;

import org.joml.Vector2i;
import org.joml.Vector3d;

public interface Level extends TileGetter {
    int CHUNK_SIZE=16;

    TileState mapping(int id);

    int getTileRaw(int x, int y);

    default TileState getTile(int x, int y) {
        return mapping(getTileRaw(x, y));
    }

    default int[] getChunkRaw(int chunkX, int chunkY) {
        int[] chunk=new int[CHUNK_SIZE*CHUNK_SIZE];
        for(int y=0;y<CHUNK_SIZE;y++){
            for(int x=0;x<CHUNK_SIZE;x++){
                chunk[y*CHUNK_SIZE+x]=getTileRaw(chunkX*CHUNK_SIZE+x,chunkY*CHUNK_SIZE+y);
            }
        }
        return chunk;
    }

    default void setTurf(int x, int y, TileState tile){
        //Unmodifiable
    }

    static Vector2i getChunkPos(Vector3d pos){
        return new Vector2i((int)Math.floor(pos.x/CHUNK_SIZE),(int)Math.floor(pos.y/CHUNK_SIZE));
    }

    static int chunkAxisDistance(Vector2i a, Vector2i b){
        return Math.max(Math.abs(a.x-b.x),Math.abs(a.y-b.y));
    }

}