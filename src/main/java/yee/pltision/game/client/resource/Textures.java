package yee.pltision.game.client.resource;


import yee.pltision.glfmhelper.globject.Texture;

import java.io.IOException;

public interface Textures {
    Texture GRASS = readTexture("game/texture/草地.png");

    static Texture readTexture(String resource) throws RuntimeException{
        try {
            return Texture.read(resource);
        } catch (IOException e) {
            throw new StaticInitializeException("Failed to load texture \""+resource+"\"",e);
        }
    }



}

