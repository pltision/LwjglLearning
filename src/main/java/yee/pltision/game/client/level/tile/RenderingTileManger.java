package yee.pltision.game.client.level.tile;

import org.jetbrains.annotations.NotNull;
import yee.pltision.game.client.resource.RenderingTile;
import yee.pltision.game.world.tile.TileMapping;
import yee.pltision.game.world.tile.TileState;

import java.util.Arrays;

public class RenderingTileManger {

    public static class Comparing implements Comparable<Comparing>{
        public final RenderingTile rendering;
        public final TileState state;

        public Comparing(TileState state){
            this.state=state;
            this.rendering=state.tile().getRendering();
        }

        @Override
        public int compareTo(@NotNull Comparing o) {
            return rendering.priority()-o.rendering.priority();
        }
    }

    //TileState id 对应的优先级
    public int[] priority;
    //优先级对应的纹理id
    public int[] texture;

    public void build(TileMapping tileMapping){
        Comparing[] tile=new Comparing[tileMapping.size()];
        Arrays.setAll(tile,i->new Comparing(tileMapping.tiles[i]));
        Arrays.sort(tile);
        priority=new int[tileMapping.size()];
        texture=new int[tileMapping.size()];
        for(int i=0;i<tile.length;i++){
            priority[tile[i].state.id()]=i;
            texture[i]=tile[i].rendering.texture().getTexture();
        }
    }

    public int prioritied(int tileIndex){
        return priority[tileIndex];
    }

    public int texture(int priority){
        return texture[priority];
    }



}