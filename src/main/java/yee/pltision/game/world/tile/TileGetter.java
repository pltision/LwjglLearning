package yee.pltision.game.world.tile;

@FunctionalInterface
public interface TileGetter {

    TileState getTile(int x, int y);

}
