#version 330

const int chunkSize=16;
const int tileSize = /*256*/1;

in VS_OUT {
    vec4 verts[4];  // 四边形顶点（投影后）
    flat int tileIndices[tileSize];  // 瓦片索引数组
} gs_in;

in vec2 texCoord;
out vec4 color;

uniform sampler2D textures[10];

void main(){
    vec2 pos=texCoord*chunkSize;
    int index=int(pos.x)+int(pos.y)*chunkSize;
    color= texture(textures[gs_in.tileIndices[index]], fract(pos));
}