package yee.pltision.game.world.tile;

@Deprecated
public interface TileGrid {

    TileState getTurf(int x, int y);

    default void setTurf(int x, int y, TileState tile){
        //  啥也不干
    }
}
