#version 330 core

out vec4 FragColor;
in vec2 TexCoord;

float noiseScale=8;
uniform float time;
uniform bool animate;
uniform sampler3D randomTextures[10];


float smoothstep(float t)
{
    return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
}

float lerp(float a, float b, float t)
{
    return a + t * (b - a);
}

float getRandom(vec3 uv,int index){
    return texture(randomTextures[index],fract(uv/noiseScale+vec3(index*1.3125216761,index*1.3125216761,index*1.3125216761))).r;
}

float grad(vec3 p, vec3 gridPoint, int index)
{
    vec3 f = fract(p) - gridPoint;

    // 获取随机向量方向
    float rand = getRandom(floor(p) + gridPoint, index);
    int choice = int(fract(rand) * 12.0);

    vec3 gradient;

    // 从12个可能的梯度向量中选择一个（三维Perlin噪声标准梯度向量）
    switch(choice) {
        case 0: gradient = vec3(1, 1, 0); break;
        case 1: gradient = vec3(-1, 1, 0); break;
        case 2: gradient = vec3(1, -1, 0); break;
        case 3: gradient = vec3(-1, -1, 0); break;
        case 4: gradient = vec3(1, 0, 1); break;
        case 5: gradient = vec3(-1, 0, 1); break;
        case 6: gradient = vec3(1, 0, -1); break;
        case 7: gradient = vec3(-1, 0, -1); break;
        case 8: gradient = vec3(0, 1, 1); break;
        case 9: gradient = vec3(0, -1, 1); break;
        case 10: gradient = vec3(0, 1, -1); break;
        default: gradient = vec3(0, -1, -1); break;
    }

    // 点积计算梯度贡献
    return dot(f, gradient);
}

float perlinNoise(vec3 uv, int index)
{
    // 网格单元坐标
    vec3 i = floor(uv);
    vec3 f = fract(uv);

    // 平滑曲线
    float u = smoothstep(f.x);
    float v = smoothstep(f.y);
    float w = smoothstep(f.z);

    // 计算8个角落的梯度贡献
    float aaa = grad(uv, vec3(0, 0, 0), index);
    float baa = grad(uv, vec3(1, 0, 0), index);
    float aba = grad(uv, vec3(0, 1, 0), index);
    float bba = grad(uv, vec3(1, 1, 0), index);
    float aab = grad(uv, vec3(0, 0, 1), index);
    float bab = grad(uv, vec3(1, 0, 1), index);
    float abb = grad(uv, vec3(0, 1, 1), index);
    float bbb = grad(uv, vec3(1, 1, 1), index);

    // 三维三线性插值
    float x1 = lerp(aaa, baa, u);
    float x2 = lerp(aba, bba, u);
    float x3 = lerp(aab, bab, u);
    float x4 = lerp(abb, bbb, u);
    float y1 = lerp(x1, x2, v);
    float y2 = lerp(x3, x4, v);

    return lerp(y1, y2, w);
}

float sqr(float x){
    return x*x;
}

float cube(float x){
    return x*x*x;
}

void main()
{
    vec3 uv = vec3(TexCoord * noiseScale, time);

    /**if(animate){
        uv.x += time;
        uv.y += time;
//        uv.z += time * 0.5; // 添加Z轴动画
    }*/


//    float noise1 = (perlinNoise(uv,0)+perlinNoise(uv*0.5,1)+perlinNoise(uv*0.25,2))/3;
    vec3 shifted = uv + vec3(perlinNoise(uv,6),perlinNoise(uv,7),perlinNoise(uv,8))*0.2;
    float noise3 = (perlinNoise(shifted,0)+perlinNoise(shifted*0.5,1)+perlinNoise(shifted*0.25,2))/3;
//    float noise=1-max(sqrt(abs(noise1)),sqrt(abs(noise2)));


    float noise1 = (perlinNoise(uv,0)+perlinNoise(uv/2,1)/2+perlinNoise(uv/4,2)/2)/2;
//    float noise2 = (perlinNoise(uv,3)+perlinNoise(uv/2,4)/2+perlinNoise(uv/4,5)/2)/2;
    float noise=1-max(sqrt(abs(noise1)),sqrt(abs(noise2)),sqrt(abs(noise3)));
//    float noise=perlinNoise(uv,0);

    FragColor = vec4(0,sqr(noise),noise, 1);

//    FragColor = texture(randomTextures[0],vec3(TexCoord,time));
}
