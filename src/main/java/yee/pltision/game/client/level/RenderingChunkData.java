package yee.pltision.game.client.level;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RenderingChunkData {
    public static final int DATA_SIZE=16;
    public static final int BUFFER_SIZE=DATA_SIZE*DATA_SIZE;

    public AtomicBoolean writing=new AtomicBoolean(false);
    public AtomicReference<int[]> textures=new AtomicReference<>(new int[BUFFER_SIZE]);


}