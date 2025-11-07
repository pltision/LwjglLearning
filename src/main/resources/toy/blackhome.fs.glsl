#version 330 core

out vec4 FragColor;
in vec2 TexCoord;

uniform float noiseScale;
uniform float time;
uniform bool animate;
uniform sampler2D universe;

const int itCount=1024;   //迭代次数
const int itAdd=2;      //让光实际走的长度增长2倍

const float spaceLength=1.0;
const float lightSpeed=1;

//质点
const vec3 particlePos=vec3(0.5,0.5,0.5);   //质点位置
const float particleQuality=-1;              //质点重量


void main()
{
    vec3 pos=vec3(TexCoord,0);
    vec3 speed=vec3(0,0,lightSpeed);

    for(int i=0;i<itCount;i++)
    {

        vec3 dis=particlePos-pos;
        vec3 force=1.0/dot(dis,dis)*normalize(dis)*particleQuality;
        speed+=force / itCount * itAdd;
        pos+=speed / itCount * itAdd;

        if(pos.z>=1)
                break;
    }

    if(pos.z>=1){
        FragColor=texture(universe,pos.xy);
    }
    else{
        FragColor=vec4(0.25,0.0,0.5,1.0);
    }

}