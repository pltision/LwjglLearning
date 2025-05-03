package yee.pltision.glfmhelper.globject;

import static org.lwjgl.opengl.GL11.*;

public class Texture implements GLObject {
    int texture;
    int type;

    public int getTexture() {
        return texture;
    }

    public int getType() {
        return type;
    }

    public Texture(int texture,int type){
        this.texture =texture;
        this.type =type;
    }

    public static Texture create(int type){
        return new Texture(glGenTextures(),type);
    }
    @Override
    public void delete() {
        glDeleteTextures(texture);
    }
    public void bind(){
        glBindTexture(type,texture);
    }
}