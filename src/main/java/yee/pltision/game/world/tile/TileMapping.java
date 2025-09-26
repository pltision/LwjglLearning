package yee.pltision.game.world.tile;

import java.util.LinkedList;

public class TileMapping {
    public TileState[] tiles;

    public TileMapping(TileState[] tiles) {
        this.tiles = tiles;
    }

    public TileState get(int index){
        return tiles[index];
    }

    public int size(){
        return tiles.length;
    }

    public static class Builder{
        LinkedList<TileState> tiles=new LinkedList<>();

        public TileState add(Tile tile){
            TileState tileState= new TileState(tile,tiles.size());
            this.tiles.add(tileState);
            return tileState;
        }


        TileMapping build(){
            //noinspection ToArrayCallWithZeroLengthArrayArgument
            return new TileMapping(tiles.toArray(new TileState[tiles.size()]));
        }

    }
}