#version 330

layout (points) in;
in VS_OUT {
    vec4 origin;       // 区块原点位置
    vec4 unitX;        // X方向单位向量（已乘投影矩阵）
    vec4 unitY;        // Y方向单位向量（已乘投影矩阵）
    int tileIndices[16*16];  // 瓦片索引数组（对应uniform纹理数组的索引）
} gs_in[];

layout (triangle_strip, max_vertices = 1536) out;

// 输出到片段着色器：瓦片在纹理中的UV（0-1范围）和纹理数组索引
out vec2 texCoord;
flat out int tileIndex;  // 用flat修饰避免插值（索引是整数，不需要插值）

// 生成四边形（四个顶点）
void emitQuad(vec4 verts[4], int index) {
    tileIndex = index;  // 传递瓦片在uniform数组中的索引

    // 顶点0：左下，UV(0,1)
    texCoord = vec2(0.0, 1.0);
    gl_Position = verts[0];
    EmitVertex();

    // 顶点1：右下，UV(1,1)
    texCoord = vec2(1.0, 1.0);
    gl_Position = verts[1];
    EmitVertex();

    // 顶点2：左上，UV(0,0)
    texCoord = vec2(0.0, 0.0);
    gl_Position = verts[2];
    EmitVertex();
    EndPrimitive();

    // 顶点3：右上，UV(1,0)
    texCoord = vec2(1.0, 0.0);
    gl_Position = verts[3];
    EmitVertex();

    // 复用顶点1和顶点2完成第二个三角形
    texCoord = vec2(1.0, 1.0);
    gl_Position = verts[1];
    EmitVertex();

    texCoord = vec2(0.0, 0.0);
    gl_Position = verts[2];
    EmitVertex();
    EndPrimitive();
}

void main() {
    // 遍历区块内所有瓦片（16x16）
    for(int y = 0; y < 16; y++) {
        for(int x = 0; x < 16; x++) {
            int idx = y * 16 + x;  // 计算一维索引
            int tileId = gs_in[0].tileIndices[idx];  // 获取当前瓦片的纹理数组索引

            vec4 origin=gs_in[0].origin;
            vec4 unitX=gs_in[0].unitX;
            vec4 unitY=gs_in[0].unitY;

            // 计算瓦片四个顶点的位置
            vec4 verts[4];
            verts[0] = origin + unitX * x + unitY * y;         // 左下
            verts[1] = origin + unitX * (x + 1) + unitY * y;   // 右下
            verts[2] = origin + unitX * x + unitY * (y + 1);   // 左上
            verts[3] = origin + unitX * (x + 1) + unitY * (y + 1); // 右上

            emitQuad(verts, tileId);
        }
    }
}
