package yee.pltision.game;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.math.Lerp;
import yee.pltision.math.Mth;


public class Camera {

    public Vector2f defaultWindowSize=new Vector2f(1024,1024);
    public Vector2f windowSize=new Vector2f(1024,1024);

    public float xRot= Mth.toRadians(45);
    public Lerp<Quaternionf> zRot=Lerp.create();
    public Vector3f pos=new Vector3f(0,0,0);

    public float scale=0.2f;

    public static final float ROTATE_TIME=1/0.25f;

    public Quaternionf getZRot(){
        return zRot.getValue();
    }

    public void update(float time){
        zRot.update(time*ROTATE_TIME);
    }

    public MatrixStack getStartMatrixStack(){
        Vector3f relative=new Vector3f();
        relative.sub(pos);

        Quaternionf cameraRotate=new Quaternionf().rotateX(xRot).mul(getZRot());

        MatrixStack matrixStack=new MatrixStack();
        setWindowScale(matrixStack.peek()).scale(scale).rotate(cameraRotate).translate(relative);

        return matrixStack;
    }

    public void setCameraSize(float width,float height){
        windowSize.set(width,height);
    }

    public Matrix4f setWindowScale(Matrix4f matrix){
        return matrix.scale(defaultWindowSize.x()/windowSize.x(),defaultWindowSize.y()/windowSize.y(),0);
    }

}