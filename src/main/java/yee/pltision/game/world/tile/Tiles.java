package yee.pltision.game.world.tile;

import yee.pltision.game.client.resource.RenderingTile;
import yee.pltision.game.client.resource.Textures;
import yee.pltision.glfmhelper.globject.Texture;

import static yee.pltision.game.world.tile.Tiles.Priority.*;

public enum Tiles implements Tile {
    VOID(Textures.TILE_VOID,0),
    DIRT(Textures.TILE_DIRT,PRIORITY_DIRT),
    GRASS(Textures.TILE_GRASS,PRIORITY_GRASS),
    ;

    //应该有一个String的id来获取贴图，但id存哪都还不清楚
    public final Texture texture;

    //瓦片的优先级，大的看起来叠在小的上面
    //按理来说这里应该之处理服务端逻辑
    public final int  priority;


    public static class Priority{
        public static final int PRIORITY_DIRT = 100;
        public static final int PRIORITY_STONE = 200;
        public static final int PRIORITY_GRASS = 300;
    }


    Tiles(Texture texture){
        this(texture,PRIORITY_STONE);
    }

    Tiles(Texture texture,int priority) {
        this.texture = texture;
        this.priority=priority;
    }

    @Override
    public RenderingTile getRendering() {
        return new RenderingTile(texture,priority);
    }
}
