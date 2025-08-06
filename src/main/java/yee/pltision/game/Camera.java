package yee.pltision.game;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.glfmhelper.Mth;


public class Camera {

    public float xRot= Mth.toRadians(60);
    public float zRot;
    public Vector3f pos=new Vector3f(0,0,0);

    public float scale=0.2f;

    public MatrixStack getStartMatrixStack(){
//        Vector3f relative=new Vector3f(0,0,0);

        Quaternionf cameraRotate=new Quaternionf().rotateX(xRot).rotateZ(zRot);

        MatrixStack matrixStack=new MatrixStack();
        matrixStack.peek().scale(scale).rotate(cameraRotate).translate(pos);

        return matrixStack;
    }
}