package yee.pltision.maze;

import org.joml.Vector2f;
import org.joml.Vector3f;
import yee.pltision.glfmhelper.globject.VertexPackage;
import yee.pltision.maze.shape.Shapes;
import yee.pltision.maze.shape.UvGetters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;

public class TestCell implements Cell {
    public Collection<Piece> pieces;

    public TestCell(){
        pieces=new LinkedList<>();

        pieces.add(
                () -> new PieceRenderer() {
                    VertexPackage vertexPackage= Shapes.cuboid(new Vector3f(-1,-1,-1),new Vector3f(1,1,1), UvGetters.square(new Vector2f(0,0),new Vector2f(1/4f,1/4f)));
                    @Override
                    public void render(RenderContext context) {
                        vertexPackage.bind();
                        glDrawArrays(GL_QUADS, 0, 4*6);

                    }
                }
        );
    }


    @Override
    public Collection<Piece> pieces() {
        return pieces;
    }
}