package yee.pltision.game.client;

import yee.pltision.game.world.entity.Entity;
import yee.pltision.glfmhelper.MatrixStack;

public interface EntityRenderer<E extends Entity> {
    void render(MatrixStack matrixStack);

    default void update(E entity){}
}
