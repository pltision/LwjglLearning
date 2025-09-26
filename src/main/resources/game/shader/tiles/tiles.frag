#version 330

// 输入：区块位置（世界坐标格子数）和瓦片索引数组
layout (location=0) in ivec2 chunkPos;  // 区块位置（每个区块16x16瓦片）
layout (location=1) in int tiles[16*16];  // 16x16瓦片的索引（对应纹理图集）

// 输出到几何着色器的数据
out VS_OUT {
    vec4 origin;       // 区块左下角的世界坐标（经投影后）
    vec4 unitX;        // x方向一个瓦片的投影后向量
    vec4 unitY;        // y方向一个瓦片的投影后向量
    int tileIndices[16*16];  // 瓦片索引数组
} vs_out;

uniform mat4 projection;  // 投影矩阵


void main() {
    // 计算区块原点（左下角）的世界坐标（转换为浮点数）
    vec2 worldOrigin = vec2(chunkPos) * 16.0;  // 16瓦片/区块
    vs_out.origin = projection * vec4(worldOrigin, 0.0, 1.0);

    // 计算一个瓦片在x/y方向的投影后向量（用于几何着色器生成网格）
    vs_out.unitX = projection * vec4(1.0, 0.0, 0.0, 0.0);  // x方向一个瓦片
    vs_out.unitY = projection * vec4(0.0, 1.0, 0.0, 0.0);  // y方向一个瓦片

    // 传递瓦片索引数组
    vs_out.tileIndices = tiles;

    // 顶点着色器只需输出一个"标记点"（几何着色器以此为基准生成网格）
    gl_Position = vec4(0.0, 0.0, 0.0, 1.0);
}