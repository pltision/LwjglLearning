#version 330 core

layout (location = 0) in vec3 vertexPos;
layout (location = 1) in vec3 color;
out vec3 fragmentPos;
out vec4 baseColor;

void main(){
    fragmentPos=vertexPos;
    gl_Position=vec4(vertexPos,1);
    baseColor=vec4(color,1);
}