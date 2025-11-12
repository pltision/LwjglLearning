package yee.pltision.game.client.level;

import org.joml.Vector2i;
import yee.pltision.game.world.tile.Level;
import yee.pltision.game.world.tile.TileMapping;
import yee.pltision.game.world.tile.TileState;

public interface ClientLevel extends Level {

    default boolean mayChunkChanged(Vector2i chunkPos){
        return mayChunkChanged(chunkPos.x,chunkPos.y);
    }

    default boolean mayChunkChanged(int chunkX,int chunkY){
        return false;
    }

    default void setChanged(Vector2i chunkPos){
        setChanged(chunkPos.x,chunkPos.y);
    }

    default void setChanged(int chunkX,int chunkY){
        //Unmodifiable
    }

    default void unsetChanged(Vector2i chunkPos){
        unsetChanged(chunkPos.x,chunkPos.y);
    }

    default void unsetChanged(int chunkX, int chunkY){
        //Unmodifiable
    }


    @FunctionalInterface
    interface TileFunction {
        int getTile(int x, int y);
    }

    // Function, YES!
    static ClientLevel functionGrid(TileMapping mapping, TileFunction function){
        return new ClientLevel() {
            @Override
            public TileState mapping(int id) {
                return mapping.get(id);
            }

            @Override
            public int getTileRaw(int x, int y) {
                return function.getTile(x, y);
            }
        };
    }

    default boolean isValid(int chunkX,int chunkY){
        return true;
    }


}
