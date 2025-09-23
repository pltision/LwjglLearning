package yee.pltision.glfmhelper.globject;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.demo.util.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader implements GLObject{
    int shader;

    public Shader(int shader){
        this.shader =shader;
    }

    public static Shader create(int type){
        return new Shader(glCreateShader(type));
    }

    public int getShader() {
        return shader;
    }

    @Override
    public void delete() {
        glDeleteShader(shader);
    }

    public static void bindShader(int shader,String source){
        glShaderSource(shader,source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetShaderInfoLog(shader));
        }
    }

    public static void readFromResourceAndBindShader(int shader, String resource) throws IOException {
        ByteBuffer source = IOUtils.ioResourceToByteBuffer(resource, 81920);
//        byte[] test=new byte[source.remaining()];
//        source.get(test);
//        System.out.println(new String(test));

        PointerBuffer strings = BufferUtils.createPointerBuffer(1);
        IntBuffer lengths = BufferUtils.createIntBuffer(1);

        strings.put(0, source);
        lengths.put(0, source.remaining());

        glShaderSource(shader, strings, lengths);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new ShaderCompileException(shader,glGetShaderInfoLog(shader));
        }
    }

    public Shader readResource(String resource) throws IOException{
        readFromResourceAndBindShader(getShader(),resource);
        return this;
    }

    public Shader compile(String source){
        bindShader(getShader(),source);
        return this;
    }
}
