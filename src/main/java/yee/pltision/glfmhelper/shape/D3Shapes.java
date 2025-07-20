package yee.pltision.glfmhelper.shape;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface D3Shapes {
    /**
     * 根据传入的向量创建长方体
     * @param from  长方体的一个角
     * @param to    长方体的对角
     * @param uvGetter  根据顶点序号获取uv
     * @return  一个包含长方体顶点数据的List<PosUvVertex>
     */
    static List<PosUvVertex> cuboid(Vector3f from, Vector3f to, Function<Integer, Vector2f> uvGetter) {
        List<PosUvVertex> vertices = new ArrayList<>(6*4);

        Vector3f[] vertexArray = createCuboidVertex(from, to);

        // 定义6个面的顶点索引（每个面4个顶点，按顺时针顺序）
        int[][] faces = {
                {0, 1, 2, 3}, // 前面     -x-y +x-y +x+y -x+y
                {1, 5, 6, 2}, // 右面
                {5, 4, 7, 6}, // 后面
                {4, 0, 3, 7}, // 左面
                {3, 2, 6, 7}, // 上面
                {4, 5, 1, 0}  // 下面
        };

        // 为每个面添加顶点数据
        for (int face = 0; face < 6; face++) {
            for (int vertex = 0; vertex < 4; vertex++) {
                int vertexIndex = faces[face][vertex];
                vertices.add(new PosUvVertex(vertexArray[vertexIndex]/*Immutable*/, uvGetter.apply(face * 4 + vertex)));
            }
        }

        return vertices;
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