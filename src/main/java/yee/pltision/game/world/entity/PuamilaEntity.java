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

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

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
                glActiveTexture(GL_TEXTURE0);
                Textures.PUAMILA.bind();
                Shapes.PUAMILA.initRender();
                UniformHelper.matrix4f(matrixStack.push().rotate(new Quaternionf().div(camera.getZRot())).rotateX(-camera.xRot), Shaders.TEXTURE_SHADER_MATRIX);
                Shapes.PUAMILA.render();
//                glBindTexture(GL_TEXTURE_2D,0);
                matrixStack.pop();
                glBindVertexArray(0);
                glUseProgram(0);
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