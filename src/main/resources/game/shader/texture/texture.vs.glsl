#version 330 core

layout (location = 0) in vec3 vertexPos;
layout (location = 1) in vec2 vertexUv;
layout (location = 2) in vec4 vertexColor;

uniform mat4 transform;

out vec4 fragmentPos;
out vec4 fragmentColor;
out vec2 fragmentUV;

void main(){
    fragmentPos=transform*vec4(vertexPos,1);
    fragmentColor=vertexColor;
    fragmentUV=vertexUv;
    gl_Position=fragmentPos;
}