package yee.pltision.game.client.level.tile;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import yee.pltision.game.Camera;
import yee.pltision.game.client.EntityRenderer;
import yee.pltision.game.client.level.ClientLevel;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.client.resource.Shapes;
import yee.pltision.game.world.entity.PuamilaEntity;
import yee.pltision.game.world.tile.WorldAssets;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.glfmhelper.UniformHelper;
import yee.pltision.math.LerpFactor;
import yee.pltision.math.Mth;

public class RenderingWorld  {
    public WorldAssets assets;
    public RenderingTileManger tileManger;

    public PuamilaEntity entity=new PuamilaEntity();
    public EntityRenderer<?> renderer= entity.createRender();

    public Vector3d center=new Vector3d();
    public Vector3f relatived =new Vector3f();
    public int turfX,turfY;

    public Quaternionf zRot=new Quaternionf();
    public Quaternionf cameraRot=new Quaternionf();

    public float rotateCloddown =0;

    public Camera camera=new Camera();

    public ClientLevel level;

    public TileChunksRenderer tileChunksRenderer;

    public static final float ROTATE_CLOD_DOWN_TIME=1/0.25f;
    public void update(float passedTime){
        camera.update(passedTime);
        updateCenter(entity.getPosition());
        tileChunksRenderer.update(entity.getPosition());
        rotateCloddown-=passedTime*ROTATE_CLOD_DOWN_TIME;
    }

    public RenderingWorld(){
        assets=new WorldAssets();

        tileManger=new RenderingTileManger();
        tileManger.build(assets.TILE_MAPPING);

        int[] tiles ={
                assets.GRASS.id(),
                assets.GRASS.id(),
                assets.GRASS.id(),
                assets.GRASS.id(),

                assets.DIRT.id(),
                assets.DIRT.id(),
                assets.DIRT.id(),
                assets.DIRT.id(),

        };

        level = ClientLevel.functionGrid(assets.TILE_MAPPING,
                (x, y)->tiles[(int) Mth.qSqrt(x*x+y*y)% tiles.length]
        );

        tileChunksRenderer=new TileChunksRenderer(level,tileManger);

    }


    //  ———————— 渲染 ————————

    public void render(){
        renderEntity(renderer,entity.getPosition(),camera);
        tileChunksRenderer.tileChunksRenderGpu.render(camera, entity.getPosition(),tileManger);
        renderTurfs(camera.getStartMatrixStack().push(m->m.translate(relatived)),turfX,turfY);

    }

    public void renderEntity(EntityRenderer<?> renderer,Vector3d entityPos,Camera camera){
        renderer.render(camera.getStartMatrixStack().push(m->m.translate(getRelativePos(center,entityPos))),camera);
    }

    public Vector3f getRelativePos(Vector3d origin,Vector3d entityPos){
        return new Vector3f(
                (float) (entityPos.x-origin.x),
                (float) (entityPos.y-origin.y),
                (float) (entityPos.z-origin.z)
        );
    }


    public void renderTurfs(MatrixStack matrixStack,int x,int y){

        int radius=15;
        Matrix4f upMatrix=matrixStack.push();

        Vector3f move=new Vector3f();
        Matrix4f matrix=new Matrix4f();

        Shapes.TURF.initRender();

        for(int i=-radius+x;i<radius+x;i++){
            for(int j=-radius+y;j<radius+y;j++){
                move.set(i,j,0);
                matrix.set(upMatrix)/*.scale(0.2f)*/.translate(move);
                level.getTile(i,j).tile().getRendering().texture().bind();
                UniformHelper.matrix4f(matrix, Shaders.TEXTURE_SHADER_MATRIX);
                Shapes.TURF.render();
            }
        }

        matrixStack.pop();
    }

    //  ———————— 控制 ————————


    public void updateCenter(Vector3d center){
        this.center.set(center);
        relatived.set(center).negate();
        turfX=(int)center.x;
        turfY=(int)center.y;
    }

    public static final float PRE_ROTATE=Mth.QUARTER_PI;

    public void tryTurnLeft(){
        if(rotateCloddown <=0){
            rotateCloddown =1;
            rotate(zRot.rotateZ(-PRE_ROTATE));
        }

    }

    public void tryTurnRight(){
        if(rotateCloddown <=0){
            rotateCloddown =1;
            rotate(zRot.rotateZ(PRE_ROTATE));
        }
    }

    public void rotate(Quaternionf zRot){
        this.zRot=zRot;
        cameraRot.identity().div(zRot);
        camera.zRot.applyLeap(LerpFactor.INVERT_QUARTIC,cameraRot);
    }



}