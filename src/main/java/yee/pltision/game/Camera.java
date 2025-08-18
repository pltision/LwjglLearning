package yee.pltision.game;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.glfmhelper.Mth;


public class Camera {

    public float xRot= Mth.toRadians(60);
    public float zRot;
    public Vector3f pos=new Vector3f(0,0,0);

    public float zRotActually =0;
    public float rotateLinear=0;

    public float scale=0.2f;

    public MatrixStack getStartMatrixStack(){
        Vector3f relative=new Vector3f();
        relative.sub(pos);

        Quaternionf cameraRotate=new Quaternionf().rotateX(xRot).rotateZ(zRot);

        MatrixStack matrixStack=new MatrixStack();
        matrixStack.peek().scale(scale).rotate(cameraRotate).translate(relative);

        return matrixStack;
    }

    public void tryTurnLeft(){
        if(rotateLinear<=0)
            startRotateZTo(zRotActually+Mth.QUARTER_PI);
    }

    public void tryTurnRight(){
        if(rotateLinear<=0)
            startRotateZTo(zRotActually-Mth.QUARTER_PI);
    }

    public void startRotateZTo(float rot){
        this.zRotActually =rot;
        rotateLinear=1f;
    }

    public void update(){
        if(rotateLinear>0){
            rotateLinear-=0.005f;
            zRot=Mth.lerpSquared(zRot, zRotActually,1-rotateLinear);
        }
    }
}