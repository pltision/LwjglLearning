package yee.pltision.game.client;

import org.joml.Vector2i;
import org.lwjgl.opengl.GL15;
import yee.pltision.glfmhelper.globject.PackedVertexBuffer;
import yee.pltision.glfmhelper.globject.VertexProperties;

import java.util.Arrays;

import static yee.pltision.game.client.RenderingChunk.CHUNK_SIZE;
import static yee.pltision.glfmhelper.globject.VertexProperties.ints;

public class TilesRenderer {

    RenderingTileManger tileManger;
    int radius;
    int size;
    int sizeSquared;
    int[] prioritiedBuffer=new int[CHUNK_SIZE*CHUNK_SIZE];

    VertexProperties vertexProperties=new VertexProperties(ints(2),ints(CHUNK_SIZE*CHUNK_SIZE),ints(CHUNK_SIZE*CHUNK_SIZE));
    PackedVertexBuffer vertexes=new PackedVertexBuffer();

    public void init(){
        initPackedVertexBuffer(0);
    }

    public void initShader(){

    }

    public void initPackedVertexBuffer(int radius){
        this.radius=radius;
        size=2*radius+1;
        vertexes.deleteIfNotEmpty();
        vertexes.init();
        vertexes.data(vertexProperties.byteSizeOf(size*size),vertexProperties, GL15.GL_DYNAMIC_DRAW);


    }

    public void updateTileManger(RenderingTileManger tileManger){
        this.tileManger=tileManger;
    }

    public void updateChunk(int x,int y){
        RenderingChunk chunk=new RenderingChunk();

        int[] prioritied=new int[size*size];
        Arrays.setAll(prioritied,i->tileManger.prioritied(chunk.tiles[i]));

        vertexes.subData(prioritied,0);

    }

}

interface ChunkProvider{
    RenderingChunk getChunk(int x,int y);
}

class ChunkCache{
    RenderingChunk [][] cache;
    int radius;
    int size;
    Vector2i centerPos;
    Vector2i absolutePos;

    ChunkProvider provider;

    ChunkCache(int radius,Vector2i absolutePos,ChunkProvider provider){
        this.radius=radius;
        size=2*radius+1;
        cache =new RenderingChunk[size][size];
        this.provider=provider;
        this.absolutePos=absolutePos;
        this.centerPos=new Vector2i();
    }

}

class RenderingChunk{
    public static final int CHUNK_SIZE=16;

    int[] tiles=new int[CHUNK_SIZE*CHUNK_SIZE];


}