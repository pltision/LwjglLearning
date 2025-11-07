#version 330 core

out vec4 FragColor;
in vec2 TexCoord;

uniform float noiseScale;
uniform float time;
uniform bool animate;


uniform sampler2D square;

vec2 texScale =vec2(1,-1);

void main()
{
    vec2 pos=TexCoord;

    if(dot(pos,pos)>=1.0)
    {
        FragColor=vec4(0.0,0.0,0.0,1.0);
        return;
    }

    //映射到双曲空间
    vec2 diskPos=(length(pos)/(1-length(pos)))*normalize(pos);

    FragColor=texture(square,diskPos*texScale);
}