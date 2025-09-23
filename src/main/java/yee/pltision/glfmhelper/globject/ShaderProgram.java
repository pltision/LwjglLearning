package yee.pltision.glfmhelper.globject;

import static org.lwjgl.opengl.GL20.*;
public class ShaderProgram implements GLObject {
    int shaderProgram;

    public ShaderProgram(int shaderProgram){
        this.shaderProgram =shaderProgram;
    }

    public static ShaderProgram create(){
        return new ShaderProgram(glCreateProgram());
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public void delete() {
        glDeleteShader(shaderProgram);
    }

    public void attachShader(int shader){
        glAttachShader(shaderProgram,shader);
    }

    public void attachShader(Shader shader){
        glAttachShader(shaderProgram,shader.getShader());
    }

    public void link(){
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new ShaderProgramLinkException(shaderProgram, glGetProgramInfoLog(shaderProgram));
        }
    }

    public ShaderProgram link(Shader vertexShader,Shader fragmentShader){
        attachShader(vertexShader);
        attachShader(fragmentShader);
        link();
        return this;
    }

//    public static ShaderProgram of(Shader vertexShader,Shader fragmentShader){
//        ShaderProgram shaderProgram=create();
//        shaderProgram.attachShader(vertexShader);
//        shaderProgram.attachShader(fragmentShader);
//        shaderProgram.link();
//        return shaderProgram;
//    }

    public ShaderProgram link(Shader... shaders){
        for(Shader shader:shaders)
            attachShader(shader);
        link();
        return this;
    }

    public ShaderProgram linkAndDelete(Shader... shaders){
        for(Shader shader:shaders)
            attachShader(shader);
        link();
        for(Shader shader:shaders)
            shader.delete();
        return this;
    }

    public void use(){
        glUseProgram(getShaderProgram());
    }
}
