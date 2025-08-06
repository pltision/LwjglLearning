package yee.pltision.game.world.grid;

import yee.pltision.game.client.resource.RenderingTurf;
import yee.pltision.game.client.resource.Textures;
import yee.pltision.glfmhelper.globject.Texture;

public enum Turfs implements Turf{
    VOID(Textures.TURF_VOID),
    DIRT(Textures.TURF_DIRT),
    GRASS(Textures.TURF_GRASS),
    ;

    private final RenderingTurf rendering;

    Turfs(Texture texture) {
        this.rendering = new RenderingTurf(texture);
    }

    @Override
    public RenderingTurf getRendering() {
        return rendering;
    }
}
