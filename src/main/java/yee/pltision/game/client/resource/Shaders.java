package yee.pltision.game.client.resource;

import yee.pltision.glfmhelper.globject.Shader;
import yee.pltision.glfmhelper.globject.ShaderProgram;

import java.io.IOException;

import static org.lwjgl.opengl.GL32.*;

public class Shaders {
    public static final ShaderProgram TEXTURE_SHADER = ShaderProgram.create().linkAndDelete(
            readShader(GL_VERTEX_SHADER, "game/shader/texture/texture.vs.glsl"),
            readShader(GL_FRAGMENT_SHADER, "game/shader/texture/texture.fs.glsl")
    );
    public static final int TEXTURE_SHADER_MATRIX=glGetUniformLocation(TEXTURE_SHADER.getShaderProgram(), "transform");

    public static final ShaderProgram TILE_CHUNK_SHADER = ShaderProgram.create().linkAndDelete(
            readShader(GL_VERTEX_SHADER, "game/shader/tiles/tile_chunk.vs.glsl"),
            readShader(GL_FRAGMENT_SHADER, "game/shader/tiles/tile_chunk.fs.glsl")
    );
    public static final int TILE_CHUNK_SHADER_MATRIX =glGetUniformLocation(TILE_CHUNK_SHADER.getShaderProgram(), "transform");
    public static final int TILE_TEXTURE_ARRAY = glGetUniformLocation(TILE_CHUNK_SHADER.getShaderProgram(), "textures");

    static {
        Shaders.TILE_CHUNK_SHADER.use();
        for (int i = 0; i < 16; i++) {
            glUniform1i(Shaders.TILE_TEXTURE_ARRAY+i, i);
        }
    }

    static Shader readShader(int type,String resource) throws RuntimeException{
        try {
            return Shader.create(type).readResource(resource);
        } catch (IOException e) {
            throw new StaticInitializeException("Failed to load shader \""+resource+"\"",e);
        }
    }
}