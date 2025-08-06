package yee.pltision.game.client.resource;

import yee.pltision.glfmhelper.globject.Shader;
import yee.pltision.glfmhelper.globject.ShaderProgram;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public interface Shaders {
    ShaderProgram TEXTURE_SHADER = ShaderProgram.create().linkAndDelete(
            readShader(GL_VERTEX_SHADER,"game/shader/texture.vs.glsl"),
            readShader(GL_FRAGMENT_SHADER,"game/shader/texture.fs.glsl")
    );
    int TEXTURE_SHADER_MATRIX=glGetUniformLocation(TEXTURE_SHADER.getShaderProgram(), "transform");

    static Shader readShader(int type,String resource) throws RuntimeException{
        try {
            return Shader.create(type).readResource(resource);
        } catch (IOException e) {
            throw new StaticInitializeException("Failed to load shader \""+resource+"\"",e);
        }
    }
}