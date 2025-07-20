package yee.pltision.glfmhelper;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public interface UniformHelper {
    float[] matrix4f=new float[16];
    static void matrix4f(Matrix4f matrix,int location){
        glUniformMatrix4fv(location, false, matrix.get(matrix4f));
    }

}