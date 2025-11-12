package yee.pltision.game.client.level.tile;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.game.Camera;
import yee.pltision.game.client.resource.Shaders;
import yee.pltision.game.world.tile.Level;
import yee.pltision.glfmhelper.MatrixStack;
import yee.pltision.glfmhelper.UniformHelper;
import yee.pltision.glfmhelper.globject.VertexArray;
import yee.pltision.glfmhelper.globject.VertexBuffer;
import yee.pltision.glfmhelper.globject.VertexProperties;
import yee.pltision.glfmhelper.shape.UvGetters;
import yee.pltision.utils.cache.SlideCache;

import java.nio.FloatBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

import static org.lwjgl.opengl.GL32.*;
import static yee.pltision.glfmhelper.globject.VertexArray.pair;
import static yee.pltision.glfmhelper.globject.VertexProperties.floats;
import static yee.pltision.glfmhelper.globject.VertexProperties.i;

public class TileChunksRenderGpu {
    public static final int CHUNK_TILE_SIZE =16;
    public static final int SHAPE_VERTEX_COUNT = 4;
    public static final int SHAPE_COUNT = CHUNK_TILE_SIZE * CHUNK_TILE_SIZE;
    public static final int VERTEX_COUNT =SHAPE_COUNT* SHAPE_VERTEX_COUNT;

    public static final VertexProperties chunkGridPosProperties =new VertexProperties(floats(2));
    public static final VertexProperties chunkGridTexCoordProperties=new VertexProperties(floats(2));
    public static final VertexProperties textureIndexProperties=new VertexProperties(i());
    VertexBuffer chunkGridPos;
    VertexBuffer chunkGridTexCoord;

    public static final int TEXTURE_INDEX_BUFFER_SIZE_INT =textureIndexProperties.intSizeOf(VERTEX_COUNT);

    int cacheRadius;
    int viewRadius;

    public void setPos(int x,int y){
        position.set(x,y);
    }

    public void setPos(Vector2i pos){
        position.set(pos);
    }

    public Vector2i getPos(){
        return position;
    }

    /**
     * 延迟设置缓存中心区块的位置，在渲染时更新缓存
     */
    Vector2i position =new Vector2i();

    // ———————— 渲染 ————————

    public void render(Camera camera, Vector3d origin,RenderingTileManger tileManger){

        setTileArrayTexture(tileManger);
        Shaders.TILE_CHUNK_SHADER.use();
        MatrixStack stack=camera.getStartMatrixStack().push( m->m.translate(getRelativePos(position,origin)));

        Vector2i originChunkPos=Level.getChunkPos(origin);

        //更新缓存位置
        setCachePos(position);

        foreachCache((data,x,y)->{
            //更新顶点属性
            if(data.needUpload){
//                System.out.println("updateGPU "+x+","+y);
                data.textureIndex.subData(0,data.textureIndexData);
//                glGetBufferSubData(GL_ARRAY_BUFFER, 0, data.textureIndexData);
                data.needUpload=false;
                data.alive=true;
            }
            //渲染
            if(data.alive && isInView(originChunkPos,new Vector2i(x,y))){
                stack.push( m->m.translate((x-position.x)* Level.CHUNK_SIZE,(y-position.y)* Level.CHUNK_SIZE,0));
                UniformHelper.matrix4f(stack.peek(), Shaders.TILE_CHUNK_SHADER_MATRIX);
                data.vertexArray.bind();
                GL32.glDrawArrays(GL32.GL_QUADS,0,VERTEX_COUNT);
                stack.pop();
            }
            return data;
        });
        glBindVertexArray(0);
        glUseProgram(0);

    }

    public boolean isInView(Vector2i originChunkPos,Vector2i chunkPos){
        return Level.chunkAxisDistance(originChunkPos,chunkPos)<=viewRadius;
    }

    public void setTileArrayTexture(RenderingTileManger tileManger){
        Shaders.TILE_CHUNK_SHADER.use();
        for (int i = 0; i < tileManger.texture.length; i++) {
            glActiveTexture(GL_TEXTURE0+i);
            glBindTexture(GL_TEXTURE_2D,tileManger.texture[i]);
        }
    }

    Vector3f getRelativePos(Vector2i chunkPos,Vector3d origin){
        return new Vector3f((float) (chunkPos.x* Level.CHUNK_SIZE -origin.x),(float) (chunkPos.y* Level.CHUNK_SIZE -origin.y),(float) origin.z);
    }


    // ———————— 初始化 ————————

    public TileChunksRenderGpu(int cacheRadius){
        initChunkGridPos();
        initChunkGridTexCoord();

        this.cacheRadius=cacheRadius;
        this.viewRadius=1;

        cache= createCache(cacheRadius,new Vector2i());


    }

    public void initChunkGridPos(){
        chunkGridPos =VertexBuffer.create();
        {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(chunkGridPosProperties.floatSizeOf(VERTEX_COUNT));
            for(int y = 0; y< CHUNK_TILE_SIZE; y++){
                for (int x = 0; x< CHUNK_TILE_SIZE; x++){
                    applyQuad(buffer,x,y);
                }
            }
            buffer.flip();
            chunkGridPos.data(buffer, GL32.GL_STATIC_DRAW);
            MemoryUtil.memFree(buffer);
        }
    }
    public void initChunkGridTexCoord(){
        chunkGridTexCoord =VertexBuffer.create();
        {
            FloatBuffer buffer = MemoryUtil.memAllocFloat(chunkGridTexCoordProperties.floatSizeOf(VERTEX_COUNT));
            Function<Integer, Vector2f> uvGetter= UvGetters.full();
            for(int i = 0; i< SHAPE_COUNT; i++){
                for (int j = 0; j< SHAPE_VERTEX_COUNT; j++){
                    Vector2f uv=uvGetter.apply(j);
                    buffer.put(uv.x).put(uv.y);
                }
            }
            buffer.flip();
            chunkGridTexCoord.data(buffer, GL32.GL_STATIC_DRAW);
            MemoryUtil.memFree(buffer);
        }
    }

    public void applyQuad(FloatBuffer buf,float x,float y){
        buf.put(x).put(y);
        buf.put(x+1).put(y);
        buf.put(x+1).put(y+1);
        buf.put(x).put(y+1);
    }


    // ———————— 缓存 ————————

    final SlideCache<GpuChunkData> cache;

    SlideCache<GpuChunkData> createCache(int size, Vector2i offset){
        return new SlideCache<>(size,offset,pos->new GpuChunkData(textureIndexProperties, chunkGridPos, chunkGridPosProperties,chunkGridTexCoord, chunkGridTexCoordProperties));
    }

    final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    final Lock readLock = rwLock.readLock();
    final Lock writeLock = rwLock.writeLock();

    public void setCachePos(Vector2i pos){
        if(pos.equals(cache.getCenterPos()))
            return;
        writeLock.lock();
        try {
            for (SlideCache.CacheElement<GpuChunkData> element : cache.updatePosition(pos)) {
                element.element().clear();
            }
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * 区块计算线程检查是否在合法范围内，且没有已计划的任务
     */
    public boolean testCanUpdate(int x,int y){
        readLock.lock();
        try {
            GpuChunkData data=cache.get(x,y);
            return data!=null && !data.needUpload;
        }finally {
            readLock.unlock();
        }
    }

    public boolean uploadBefore(int x,int y){
        readLock.lock();
        try {
            GpuChunkData data=cache.get(x,y);
            return data!=null && data.alive;
        }finally {
            readLock.unlock();
        }
    }

    /**
     * 渲染线程只设置为false，区块计算线程只设置为true
     */
    public void setUploadJob(int x,int y,boolean uploading){
        readLock.lock();
        try {
            GpuChunkData data=cache.get(x,y);
            if(data==null)
                return;
            data.needUpload =uploading;
        }finally {
            readLock.unlock();
        }
    }


    /**
     * 在渲染线程之外获取可能为null（如果渲染线程恰巧调用setPos），渲染线程之内应该使用foreach遍历所有区块
     */
    public int [] getTextureIndexData(int x,int y){
        readLock.lock();
        try {
            GpuChunkData data=cache.get(x,y);
            if(data==null)
                return null;
            return data.textureIndexData;
        }finally {
            readLock.unlock();
        }
    }

    public void foreachCache(SlideCache.CacheConsumer<GpuChunkData> consumer){
        readLock.lock();
        try {
            cache.foreachCache(consumer);
        }finally {
            readLock.unlock();
        }
    }

    public static class GpuChunkData {
        /**
         * 如果区块从未被发送过数据为false，此时区块不会被渲染
         */
        boolean alive=false;
        /**
         * 渲染线程是否需要上传数据，如果为true，区块计算线程会跳过计算任务
         */
        boolean needUpload=false;

        VertexArray vertexArray;
        VertexBuffer textureIndex;
        public int [] textureIndexData;

        public GpuChunkData(VertexProperties textureIndexProperties, VertexBuffer chunkGridPos, VertexProperties chunkGridProperties,VertexBuffer chunkGridTexCoord, VertexProperties chunkGridTexCoordProperties){
            textureIndex=VertexBuffer.create();
//            System.out.println(textureIndexProperties.byteSizeOf(VERTEX_COUNT));
            textureIndex.data(textureIndexProperties.byteSizeOf(VERTEX_COUNT), GL32.GL_DYNAMIC_DRAW);
            vertexArray=VertexArray.create();
            vertexArray.bindBuffers(pair(chunkGridPos,chunkGridProperties),pair(chunkGridTexCoord,chunkGridTexCoordProperties),pair(textureIndex,textureIndexProperties));
            textureIndexData=new int[TEXTURE_INDEX_BUFFER_SIZE_INT];
        }

        /**
         * 当前区块离开缓存范围后清除标记；无用的区域即使被传输一半时清除也是安全的
         */
        void clear(){
            alive=false;
            needUpload=false;
        }

        public void delete(){
            vertexArray.delete();
            textureIndex.delete();
        }

    }

    /**
     * 直接foreach无论如何都是线程安全的，何况是不需要使用x和y
     */
    public void delete(){
        chunkGridPos.delete();
        cache.foreachCache((element, x, y) -> {
            element.delete();
            return element;
        });
    }
}