package yee.pltision.game.client;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import yee.pltision.game.Camera;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.client.resource.Shapes;
import yee.pltision.game.world.entity.PuamilaEntity;
import yee.pltision.game.world.grid.Turf;
import yee.pltision.game.world.grid.TurfGrid;
import yee.pltision.game.world.grid.Turfs;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.glfmhelper.UniformHelper;

public class RenderingWorld  {

    PuamilaEntity entity=new PuamilaEntity();
    EntityRenderer<?> renderer= entity.createRender();

    Camera camera=new Camera();

    public static final Turf[] TURFS={
            Turfs.GRASS,
            Turfs.GRASS,
            Turfs.GRASS,

            Turfs.DIRT,
            Turfs.DIRT,

            Turfs.VOID,
            Turfs.VOID,
            Turfs.VOID,

    };
//    TurfGrid grid= (x, y) -> TURFS[(Math.abs(x)  + Math.abs(y) ) % TURFS.length];
//    TurfGrid grid= (x, y) -> TURFS[(x*x  + y*y ) % TURFS.length];   //它居然无限平铺了，简直神奇
    TurfGrid grid= (x, y) -> {
        int linear=Math.abs(x) + Math.abs(y);
        return TURFS[linear==0?0:( ((x * x + y * y)/TURFS.length % linear) * TURFS.length / linear )];
    };

    public void render(){
        renderTurfs(camera.getStartMatrixStack());
        renderer.render(camera.getStartMatrixStack());
    }
    public void renderTurfs(MatrixStack matrixStack){

        int radius=10;
        Matrix4f upMatrix=matrixStack.push();

        Vector3f move=new Vector3f();
        Matrix4f matrix=new Matrix4f();

        for(int i=-radius;i<radius;i++){
            for(int j=-radius;j<radius;j++){
                move.set(i,j,0);
                matrix.set(upMatrix)/*.scale(0.2f)*/.translate(move);

                grid.getTurf(i,j).getRendering().texture().bind();
                UniformHelper.matrix4f(matrix, Shaders.TEXTURE_SHADER_MATRIX);
                Shapes.TURF.render();
            }
        }

        matrixStack.pop();
    }



}