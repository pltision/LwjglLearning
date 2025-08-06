package yee.pltision.game.world.grid;

public interface TurfGrid {

    Turf getTurf(int x,int y);

    default void setTurf(int x, int y, Turf turf){
        //  啥也不干
    }
}
