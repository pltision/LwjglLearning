#version 330

const int tileSize = /*256*/1;

layout (points) in;
layout (triangle_strip, max_vertices = 4) out;

in VS_OUT {
    vec4 verts[4];  // 四边形顶点（投影后）
    flat int tileIndices[tileSize];  // 瓦片索引数组
} gs_in[1];

// 输出到片段着色器：瓦片在纹理中的UV（0-1范围）和纹理数组索引
out vec2 texCoord;

// 生成四边形（四个顶点）
void emitQuad(vec4 verts[4]) {

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
    emitQuad(gs_in[0].verts);
}
