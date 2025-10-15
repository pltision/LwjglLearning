package yee.pltision.game.client.level;

import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;
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

    public void initChunkBuffer(){
        VertexProperties chunkGridProperties=new VertexProperties(floats(2));
        VertexBuffer chunkGrid=VertexBuffer.create();
        {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(chunkGridProperties.floatSizeOf(RenderingChunkData.DATA_SIZE));
            for(int i=0;i<RenderingChunkData.DATA_SIZE;i++){
                for (int j=0;j<RenderingChunkData.DATA_SIZE;j++){
                    applyQuad(buffer,j,i);
                }
            }
            chunkGrid.data(buffer, chunkGridProperties, GL32.GL_DYNAMIC_DRAW);
            MemoryUtil.memFree(buffer);
        }

        VertexProperties quadUvProperties=new VertexProperties(floats(2));
        VertexBuffer quadUv=VertexBuffer.create();
        {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(quadUvProperties.floatSizeOf(4));
            Arrays.stream(UvGetters.squareVectorList()).forEach(coordination->buffer.put(coordination.x).put(coordination.y));
            quadUv.data(buffer, quadUvProperties, GL32.GL_DYNAMIC_DRAW);
            MemoryUtil.memFree(buffer);
        }



    }

    public void applyQuad(FloatBuffer buf,float x,float y){
        buf.put(x).put(y);
        buf.put(x+1).put(y);
        buf.put(x+1).put(y+1);
        buf.put(x).put(y+1);
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
