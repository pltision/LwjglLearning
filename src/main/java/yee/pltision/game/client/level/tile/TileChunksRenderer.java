package yee.pltision.game.client.level.tile;

import org.joml.Vector2i;
import org.joml.Vector3d;
import yee.pltision.game.client.level.ClientLevel;
import yee.pltision.game.world.tile.Level;
import yee.pltision.utils.foreach.D2Foreach;


public class TileChunksRenderer {


    int viewRadius =1;


    Vector2i playerPos=new Vector2i();

    ClientLevel level;

    TileChunksRenderGpu tileChunksRenderGpu =new TileChunksRenderGpu(viewRadius +2);

    RenderingTileManger renderingTileManger;

    public TileChunksRenderer(ClientLevel level, RenderingTileManger renderingTileManger) {
        this.level = level;
        this.renderingTileManger = renderingTileManger;
    }

    public void update(Vector3d freshPos){
        Vector2i freshPosChunk=Level.getChunkPos(freshPos);

        D2Foreach.foreachSquare(freshPosChunk.x,freshPosChunk.y, viewRadius+1,(x, y)->{
            // 区块有效 & (需要更新 | !曾经上传) & 可以更新
            if (level.isValid(x,y) && (level.mayChunkChanged(x,y)||!tileChunksRenderGpu.uploadBefore(x,y)) && tileChunksRenderGpu.testCanUpdate(x,y)){
//                System.out.println("updateJob "+x+","+y);
                level.setChanged(x,y);
                int[] tiles=level.getChunkRaw(x,y);
                int[] data= tileChunksRenderGpu.getTextureIndexData(x,y);
                if(data!=null) {
                    fillData(data, tiles);
                    tileChunksRenderGpu.setUploadJob(x,y,true);
                }
            }
        });

        if(Level.chunkAxisDistance(freshPosChunk,playerPos)>viewRadius){
            playerPos.set(freshPosChunk);
            tileChunksRenderGpu.setPos(freshPosChunk);
        }
    }

    public void fillData(int[] data,int[] chunk){
        int dataIndex=0;
        for (int k : chunk) {
            for (int j = 0; j < TileChunksRenderGpu.SHAPE_VERTEX_COUNT; j++) {
                data[dataIndex++] = renderingTileManger.prioritied(k);
            }
        }
    }




}