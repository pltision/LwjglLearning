#version 330 core

in vec2 texCoord;
flat in int textureIndex;

uniform sampler2D textures[16];

out vec4 outColor;

void main(){
//    if(((int(texCoord.x*2)^(int(texCoord.y*2)))&1)==0)
//        discard;
    outColor=texture(textures[textureIndex],texCoord);
//    outColor=textureIndex==1?vec4(1,0,0,1):vec4(0,1,0,1);
}