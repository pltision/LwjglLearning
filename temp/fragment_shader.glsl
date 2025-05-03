#version 330 core

in vec3 fragmentPos;
in vec4 baseColor;
out vec4 color;

void main(){
    float x=fragmentPos.x*8;
    float y=fragmentPos.y*8;
    color=vec4(1,sin(x*x+y*y)/2+0.5,1.0f,1.0f)*baseColor;
}