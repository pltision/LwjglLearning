package yee.pltision.game.client;

import org.joml.Matrix4f;
import yee.pltision.game.world.entity.Entity;

import java.util.Stack;

public interface EntityRender<E extends Entity> {
    void render(E entity,FacingHelper facingHelper, Stack<Matrix4f> matrixStack);
}
