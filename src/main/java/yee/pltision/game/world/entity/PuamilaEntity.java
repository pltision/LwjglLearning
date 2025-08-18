package yee.pltision.game.world.entity;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import yee.pltision.game.Camera;
import yee.pltision.game.client.EntityRenderer;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.client.resource.Shapes;
import yee.pltision.game.client.resource.Textures;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.glfmhelper.Mth;
import yee.pltision.glfmhelper.UniformHelper;

public class PuamilaEntity implements Player {

    Vector3f pos=new Vector3f();

    @Override
    public void tick() {

    }

    @Override
    public void discard() {

    }

    @Override
    public @NotNull EntityRenderer<?> createRender() {
        return new EntityRenderer<PuamilaEntity>() {
            @Override
            public void render(MatrixStack matrixStack, Camera camera) {
//                Shaders.TEXTURE_SHADER.use();

                Textures.PUAMILA.bind();
                UniformHelper.matrix4f(matrixStack.push().translate(pos).rotateZ(-camera.zRot).rotateX(Mth.toRadians(90)+camera.xRot), Shaders.TEXTURE_SHADER_MATRIX);
                Shapes.PUAMILA.render();
            }

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