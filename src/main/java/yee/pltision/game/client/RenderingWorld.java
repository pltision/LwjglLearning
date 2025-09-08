package yee.pltision.game.client;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import yee.pltision.game.Camera;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.client.resource.Shapes;
import yee.pltision.game.world.entity.PuamilaEntity;
import yee.pltision.game.world.grid.Turf;
import yee.pltision.game.world.grid.TurfGrid;
import yee.pltision.game.world.grid.Turfs;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.math.LerpFactor;
import yee.pltision.math.Mth;
import yee.pltision.glfmhelper.UniformHelper;

public class RenderingWorld  {

    PuamilaEntity entity=new PuamilaEntity();
    EntityRenderer<?> renderer= entity.createRender();

    Vector3d center=new Vector3d();
    Vector3f relatived =new Vector3f();
    int turfX,turfY;

    Quaternionf zRot=new Quaternionf();
    Quaternionf cameraRot=new Quaternionf();

    float rotateCloddown =0;

    Camera camera=new Camera();

    public static final Turf[] TURFS={
            Turfs.GRASS,
            Turfs.GRASS,
            Turfs.GRASS,
            Turfs.GRASS,

            Turfs.DIRT,
            Turfs.DIRT,
            Turfs.DIRT,
            Turfs.DIRT,

    };
//    TurfGrid grid= (x, y) -> TURFS[(Math.abs(x)  + Math.abs(y) ) % TURFS.length];
//    TurfGrid grid= (x, y) -> TURFS[(x*x  + y*y ) % TURFS.length];   //它居然无限平铺了，简直神奇
//    TurfGrid grid= (x, y) -> {
//        int linear=Math.abs(x) + Math.abs(y);
//        return TURFS[linear==0?0:( ((x * x + y * y)/TURFS.length % linear) * TURFS.length / linear )];
//    };
    TurfGrid grid= (x, y) -> TURFS[(int) Mth.qSqrt(x*x+y*y)%TURFS.length];


    public static final float ROTATE_CLOD_DOWN_TIME=1/0.25f;
    public void update(float passedTime){
        camera.update(passedTime);
        updateCenter(entity.getPosition());
        rotateCloddown-=passedTime*ROTATE_CLOD_DOWN_TIME;
    }


    //  ———————— 渲染 ————————

    public void render(){
        renderEntity(renderer,entity.getPosition(),camera);
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

        int radius=10;
        Matrix4f upMatrix=matrixStack.push();

        Vector3f move=new Vector3f();
        Matrix4f matrix=new Matrix4f();

        for(int i=-radius+x;i<radius+x;i++){
            for(int j=-radius+y;j<radius+y;j++){
                move.set(i,j,0);
                matrix.set(upMatrix)/*.scale(0.2f)*/.translate(move);

                grid.getTurf(i,j).getRendering().texture().bind();
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