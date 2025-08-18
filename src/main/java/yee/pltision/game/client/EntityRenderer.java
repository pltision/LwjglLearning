package yee.pltision.game.client;

import yee.pltision.game.Camera;
import yee.pltision.game.world.entity.Entity;
import yee.pltision.glfmhelper.MatrixStack;

public interface EntityRenderer<E extends Entity> {
    void render(MatrixStack matrixStack, Camera camera);

    default void update(E entity){}
}
