package yee.pltision.glfmhelper;

import org.joml.Matrix4f;

import java.util.Stack;

public class MatrixStack {
    Stack<Matrix4f> matrixStack=new Stack<>();
    public MatrixStack(){
        matrixStack.push(new Matrix4f());
    }

    public Matrix4f push(){
        return matrixStack.push(new Matrix4f(matrixStack.peek()));
    }

    public void pop(){
        matrixStack.pop();
    }

    public Matrix4f peek(){
        return matrixStack.peek();
    }
}