#version 330

const int chunkSize = 16;
const int tileSize = /*256*/1;

// 输入：区块位置（世界坐标格子数）和瓦片索引数组
layout (location=0) in ivec2 chunkPos;  // 区块位置
layout (location=1) in int tiles[tileSize];  // 16x16瓦片的索引

// 输出到几何着色器的数据
out VS_OUT {
    vec4 verts[4];  // 四边形顶点（投影后）
    flat int tileIndices[tileSize];  // 瓦片索引数组
} vs_out;

uniform ivec2 relativePos;  // 相对位置（玩家所在区块）
uniform mat4 projection;  // 投影矩阵

vec4 toVec4Pos(vec2 pos) {
    return projection * vec4(pos, 0.0, 1.0);
}

void main() {
    // 计算区块原点的原点坐标（偏移relativePos减少float精度损失）
    vec2 origin = vec2(chunkPos - relativePos) * chunkSize;  // 16瓦片/区块

    vs_out.verts[0] = projection * toVec4Pos(origin) ;         // 左下
    vs_out.verts[1] = projection * toVec4Pos(origin + vec2(chunkSize, 0.0));   // 右下
    vs_out.verts[2] = projection * toVec4Pos(origin + vec2(0.0, chunkSize));   // 左上
    vs_out.verts[3] = projection * toVec4Pos(origin + vec2(chunkSize, chunkSize)); // 右上

    // 传递瓦片索引数组
    vs_out.tileIndices = tiles;

    // 顶点着色器只需输出一个"标记点"（几何着色器以此为基准生成网格）
    gl_Position = vec4(0.0, 0.0, 0.0, 1.0);
}


