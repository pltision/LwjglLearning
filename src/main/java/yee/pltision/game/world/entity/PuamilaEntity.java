package yee.pltision.game.world.entity;

import org.joml.Vector3f;
import yee.pltision.game.client.EntityRender;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.client.resource.Shapes;
import yee.pltision.glfmhelper.UniformHelper;
import yee.pltision.glfmhelper.globject.ShaderProgram;

public class PuamilaEntity implements Player {

    Vector3f pos;

    @Override
    public void tick() {

    }

    @Override
    public void discard() {

    }

    @Override
    public EntityRender<?> createRender() {
        return (EntityRender<PuamilaEntity>) (entity, facingHelper, matrixStack) -> {
            Shaders.TEXTURE_SHADER.use();
            UniformHelper.matrix4f(matrixStack.peek(), Shaders.TEXTURE_SHADER_MATRIX);
            Shapes.PUAMILA.render();
        };
    }

    //Mutable
    @Override
    public Vector3f getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Vector3f pos) {
        this.pos = pos;
    }
}