package yee.pltision.maze;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.VertexPackage;
import yee.pltision.glfmhelper.globject.VertexProperties;

import java.nio.FloatBuffer;

import static yee.pltision.glfmhelper.globject.VertexProperties.*;

public class BasePieceRender implements PieceRenderer {
    VertexPackage outside;

    public VertexPackage init(Vector3f start,Vector3f end){

    }
    @Override
    public void render(RenderContext context) {

    }
}