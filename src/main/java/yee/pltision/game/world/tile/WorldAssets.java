package yee.pltision.game.world.tile;

public class WorldAssets {
    private TileMapping.Builder builder=new TileMapping.Builder();

    public final TileState VOID = builder.add(Tiles.VOID);
    public final TileState DIRT = builder.add(Tiles.DIRT);
    public final TileState GRASS = builder.add(Tiles.GRASS);

    public TileMapping TILE_MAPPING;

    {
        build();
        builder=null;
    }

    public void build(){
        TILE_MAPPING = builder.build();
    }
}