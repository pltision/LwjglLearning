package yee.pltision.game;

import org.joml.Matrix4f;

import java.util.Stack;

public class MatrixStack {
    public Stack<Matrix4f> stack;

    public MatrixStack() {
        stack = new Stack<>();
    }

    public Matrix4f peek() {
        return stack.peek();
    }
    public void pop() {
        stack.pop();
    }
    public void push() {
        stack.push(new Matrix4f(stack.peek()));
    }

}