package yee.pltision.maze.shape;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import yee.pltision.glfmhelper.globject.VertexPackage;
import yee.pltision.glfmhelper.globject.VertexProperties;

import java.nio.FloatBuffer;
import java.util.function.Function;

import static yee.pltision.glfmhelper.globject.VertexProperties.*;

public interface Shapes {
    /**
     * 根据传入的向量创建长方体
     * @param from  长方体的一个角
     * @param to    长方体的对角
     * @param uvGetter  根据顶点序号获取uv
     * @return  一个长方体顶点包
     */
    static VertexPackage cuboid(Vector3f from, Vector3f to, Function<Integer, Vector2f> uvGetter) {
        VertexProperties properties = new VertexProperties(pos(), uv(), rgba());
        FloatBuffer buf = MemoryUtil.memAllocFloat(properties.floatSizeOf(4 * 6));

        Vector3f[] vertices = createCuboidVertex(from, to);

        // 定义6个面的顶点索引（每个面4个顶点，按顺时针顺序）
        int[][] faces = {
                {0, 1, 2, 3}, // 前面
                {5, 4, 7, 6}, // 后面
                {4, 0, 3, 7}, // 左面
                {1, 5, 6, 2}, // 右面
                {3, 2, 6, 7}, // 上面
                {4, 5, 1, 0}  // 下面
        };

        // 为每个面添加顶点数据
        for (int face = 0; face < 6; face++) {
            for (int vertex = 0; vertex < 4; vertex++) {
                int vertexIndex = faces[face][vertex];
                Vector3f pos = vertices[vertexIndex];

                // 添加位置坐标 (x, y, z)
                buf.put(pos.x).put(pos.y).put(pos.z);

                // 添加UV坐标 (u, v)
                Vector2f uv = uvGetter.apply(face * 4 + vertex);
                buf.put(uv.x).put(uv.y);

                // 添加颜色值 (r, g, b, a) - 这里设为白色不透明
                buf.put(1.0f).put(1.0f).put(1.0f).put(1.0f);
            }
        }

        buf.flip();
        return VertexPackage.createAndFree(properties, buf);
    }

    @NotNull
    static Vector3f[] createCuboidVertex(Vector3f from, Vector3f to) {
        float minX = from.x;
        float minY = from.y;
        float minZ = from.z;
        float maxX = to.x;
        float maxY = to.y;
        float maxZ = to.z;

        // 定义长方体的8个顶点
        return new Vector3f[]{
                new Vector3f(minX, minY, minZ), // 0
                new Vector3f(maxX, minY, minZ), // 1
                new Vector3f(maxX, maxY, minZ), // 2
                new Vector3f(minX, maxY, minZ), // 3
                new Vector3f(minX, minY, maxZ), // 4
                new Vector3f(maxX, minY, maxZ), // 5
                new Vector3f(maxX, maxY, maxZ), // 6
                new Vector3f(minX, maxY, maxZ)  // 7
        };
    }


}