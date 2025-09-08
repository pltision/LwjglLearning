package yee.pltision.game.world.entity;

import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import yee.pltision.game.Camera;
import yee.pltision.game.client.EntityRenderer;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.client.resource.Shapes;
import yee.pltision.game.client.resource.Textures;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.glfmhelper.UniformHelper;

public class PuamilaEntity implements Player {

    Vector3d pos=new Vector3d();

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
                UniformHelper.matrix4f(matrixStack.push().rotate(new Quaternionf().div(camera.getZRot())).rotateX(-camera.xRot), Shaders.TEXTURE_SHADER_MATRIX);
                Shapes.PUAMILA.render();
            }

        };
    }

    //Mutable
    @Override
    public Vector3d getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Vector3d pos) {
        this.pos = pos;
    }
}