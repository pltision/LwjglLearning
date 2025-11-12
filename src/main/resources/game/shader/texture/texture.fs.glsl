#version 330 core

//in vec4 fragmentPos;
in vec4 fragmentColor;
in vec2 fragmentUV;

uniform sampler2D textureSampler;

out vec4 outColor;

void main(){
    outColor=fragmentColor *texture(textureSampler,fragmentUV);
    if(outColor.a==0) discard;
//    outColor=color;
//    outColor=vec4(1,1,1,1);
}