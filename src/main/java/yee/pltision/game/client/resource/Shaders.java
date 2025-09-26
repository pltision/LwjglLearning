package yee.pltision.game.client.resource;

import yee.pltision.glfmhelper.globject.Shader;
import yee.pltision.glfmhelper.globject.ShaderProgram;

import java.io.IOException;

import static org.lwjgl.opengl.GL32.*;

public interface Shaders {
    ShaderProgram TEXTURE_SHADER = ShaderProgram.create().linkAndDelete(
            readShader(GL_VERTEX_SHADER, "game/shader/texture/texture.vs.glsl"),
            readShader(GL_FRAGMENT_SHADER, "game/shader/texture/texture.fs.glsl")
    );
    int TEXTURE_SHADER_MATRIX=glGetUniformLocation(TEXTURE_SHADER.getShaderProgram(), "transform");

    ShaderProgram TILES_SHADER = ShaderProgram.create().linkAndDelete(
            readShader(GL_VERTEX_SHADER, "game/shader/tiles/tiles.vert"),
            readShader(GL_FRAGMENT_SHADER, "game/shader/tiles/tiles.frag"),
            readShader(GL_GEOMETRY_SHADER, "game/shader/tiles/tiles.geom")
    );

    static Shader readShader(int type,String resource) throws RuntimeException{
        try {
            return Shader.create(type).readResource(resource);
        } catch (IOException e) {
            throw new StaticInitializeException("Failed to load shader \""+resource+"\"",e);
        }
    }
}