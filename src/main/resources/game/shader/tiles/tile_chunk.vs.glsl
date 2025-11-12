#version 330 core

layout (location = 0) in vec2 vPos;
layout (location = 1) in vec2 vTexCoord;
layout (location = 2) in int vTextureIndex;

uniform mat4 transform;

out vec2 texCoord;
flat out int textureIndex;

void main(){
    texCoord=vTexCoord;
    textureIndex=vTextureIndex;
    gl_Position=transform*vec4(vPos,0,1);
}