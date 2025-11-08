package yee.pltision.game.client.level;

import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.game.client.level.tile.GpuChunkObjects;
import yee.pltision.game.client.level.tile.RenderingTileManger;
import yee.pltision.glfmhelper.globject.VertexBuffer;
import yee.pltision.glfmhelper.globject.VertexProperties;
import yee.pltision.glfmhelper.shape.UvGetters;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static yee.pltision.glfmhelper.globject.VertexProperties.floats;

public class TilesRenderer {

    RenderingTileManger tileManger;
    int radius;
    int size;
    int sizeSquared;
    int[] chunkGridBuffer;


    public void init(){
        initPackedVertexBuffer(0);
    }

    public void initShader(){
        VertexProperties chunkGridProperties=new VertexProperties(floats(2));
        VertexBuffer chunkGrid=VertexBuffer.create();


        chunkGrid.data(IntBuffer.wrap(chunkGridBuffer),chunkGridProperties, GL32.GL_DYNAMIC_DRAW);

    }

    public void initPosBuffer(){


        VertexProperties quadUvProperties=new VertexProperties(floats(2));
        VertexBuffer quadUv=VertexBuffer.create();
        {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(quadUvProperties.floatSizeOf(4));
            Arrays.stream(UvGetters.squareVectorList()).forEach(coordination->buffer.put(coordination.x).put(coordination.y));
            quadUv.data(buffer, quadUvProperties, GL32.GL_DYNAMIC_DRAW);
            MemoryUtil.memFree(buffer);
        }



    }



    public void initPackedVertexBuffer(int radius){
        this.radius=radius;
        size=2*radius+1;





        /*vertexes.deleteIfNotEmpty();
        vertexes.init();
        vertexes.data(vertexProperties.property(0).byteSizeOf(size*size),vertexProperties, GL15.GL_DYNAMIC_DRAW);*/


    }

    public void updateTileManger(RenderingTileManger tileManger){
        this.tileManger=tileManger;
    }

    public void updatePos(int x, int y){


    }

}
