package yee.pltision.game.client.level.tile;

import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.VertexBuffer;
import yee.pltision.glfmhelper.globject.VertexProperties;

import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

//import static yee.pltision.game.client.level.tile.GpuChunkData.*;
import static yee.pltision.game.client.level.tile.GpuChunkObjects.GpuChunkData.*;
import static yee.pltision.glfmhelper.globject.VertexProperties.floats;

public class GpuChunkObjects {
    public static final int DATA_SIZE=16;
    public static final int BUFFER_SIZE=DATA_SIZE*DATA_SIZE;

    VertexProperties chunkGridProperties;
    VertexBuffer chunkGrid;

    public void init(){
        chunkGridProperties=new VertexProperties(floats(2));
        chunkGrid=VertexBuffer.create();
        {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(chunkGridProperties.floatSizeOf(DATA_SIZE));
            for(int i = 0; i< DATA_SIZE; i++){
                for (int j = 0; j< DATA_SIZE; j++){
                    applyQuad(buffer,j,i);
                }
            }
            chunkGrid.data(buffer, chunkGridProperties, GL32.GL_DYNAMIC_DRAW);
            MemoryUtil.memFree(buffer);
        }
    }

    public void applyQuad(FloatBuffer buf,float x,float y){
        buf.put(x).put(y);
        buf.put(x+1).put(y);
        buf.put(x+1).put(y+1);
        buf.put(x).put(y+1);
    }

    public class GpuChunkData {
        public AtomicBoolean writing=new AtomicBoolean(false);
        public AtomicReference<int[]> textures=new AtomicReference<>(new int[BUFFER_SIZE]);



    }
}