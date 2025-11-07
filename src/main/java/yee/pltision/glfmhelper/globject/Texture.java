package yee.pltision.glfmhelper.globject;

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.demo.util.IOUtils.ioResourceToByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

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

    public void setFilter(int filterType){
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, filterType);
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, filterType);
    }

    public void setFilter(int min,int mag){
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, min);
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, mag);
    }

    public static Texture read(String resource) throws IOException {
        try(MemoryStack frame = MemoryStack.stackPush()) {
            IntBuffer width = frame.mallocInt(1);
            IntBuffer height = frame.mallocInt(1);
            IntBuffer components = frame.mallocInt(1);
            ByteBuffer data = stbi_load_from_memory(
                    ioResourceToByteBuffer(resource, 1024), width, height, components,
                    4);
            Texture texture=Texture.create(GL_TEXTURE_2D);
            texture.bind();
            texture.setFilter(GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
            stbi_image_free(data);
            return texture;
        }
    }

    public static Texture createEmpty(int width,int height){
        Texture texture = Texture.create(GL_TEXTURE_2D);
        texture.bind();
        texture.emptyImage2D(0,GL_RGBA,width,height,GL_RGBA);
        return texture;
    }

    public void emptyImage2D(int mipmapLevel,int internalFormat, int width,int height,int bufferFormat){
        setFilter(GL_NEAREST);
        glTexImage2D(type, mipmapLevel, internalFormat, width, height, 0, bufferFormat, GL_UNSIGNED_BYTE, 0);
    }



}