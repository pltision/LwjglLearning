package yee.pltision.game.client.resource;


import yee.pltision.glfmhelper.globject.Texture;

import java.io.IOException;

public interface Textures {
    Texture GRASS = readTexture("game/texture/草地.png");
    Texture PUAMILA = readTexture("game/texture/puamila.png");
    Texture UV_TEST = readTexture("game/texture/uv_test.png");

    Texture TURF_VOID = readTexture("game/texture/turf/void.png");
    Texture TURF_DIRT = readTexture("game/texture/turf/dirt.png");
    Texture TURF_GRASS = readTexture("game/texture/turf/grass.png");


    static Texture readTexture(String resource) throws RuntimeException{
        try {
            return Texture.read(resource);
        } catch (IOException e) {
            throw new StaticInitializeException("Failed to load texture \""+resource+"\"",e);
        }
    }



}

