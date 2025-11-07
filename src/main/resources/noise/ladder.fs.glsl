#version 330 core

out vec4 FragColor;
in vec2 TexCoord;

uniform float graphScale;
uniform float time;
uniform bool animate;
uniform sampler3D randomTextures[10];

float samplePixels=256;

float smoothstep(float t)
{
    return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
}

float lerp(float a, float b, float t)
{
    return a + t * (b - a);
}

float getRandom(vec3 samplePoint, float scale, int index){
    return texture(randomTextures[index], mod(samplePoint, scale)/samplePixels +(scale+123)*index*vec3(19.3125216761, 49.73215467, 83.1234567)).r;
}

const float dsqrt3 = 1/sqrt(3);
float grad(vec3 scaledPos, vec3 gridPoint, float scale, int index)
{
    vec3 f = fract(scaledPos) - gridPoint;

    // 获取随机向量方向
    float rand = getRandom(floor(scaledPos) + gridPoint, scale, index);
    int choice = int(fract(rand) * 12.0);

    vec3 gradient;

    // 从12个可能的梯度向量中选择一个（三维Perlin噪声标准梯度向量）
    switch(choice) {
        case 0: gradient = vec3(1, 1, 1); break;
        case 1: gradient = vec3(-1, 1, 1); break;
        case 2: gradient = vec3(1, -1, 1); break;
        case 3: gradient = vec3(-1, -1, 1); break;
        case 4: gradient = vec3(1, 1, 1); break;
        case 5: gradient = vec3(-1, 1, 1); break;
        case 6: gradient = vec3(1, -1, -1); break;
        case 7: gradient = vec3(-1, -1, -1); break;
        case 8: gradient = vec3(1, 1, 1); break;
        case 9: gradient = vec3(1, -1, 1); break;
        case 10: gradient = vec3(1, 1, -1); break;
        default: gradient = vec3(1, -1, -1); break;
    }

    // 点积计算梯度贡献
    return dot(f, gradient)*dsqrt3;
}

const int baseScale=4;

float perlinNoise(vec3 uv,float scale, int index)
{
    scale*=baseScale;
    uv*=scale;

    // 网格单元坐标
    vec3 i = floor(uv);
    vec3 f = fract(uv);

    // 平滑曲线
    float u = smoothstep(f.x);
    float v = smoothstep(f.y);
    float w = smoothstep(f.z);

    // 计算8个角落的梯度贡献
    float aaa = grad(uv, vec3(0, 0, 0), scale, index);
    float baa = grad(uv, vec3(1, 0, 0), scale, index);
    float aba = grad(uv, vec3(0, 1, 0), scale, index);
    float bba = grad(uv, vec3(1, 1, 0), scale, index);
    float aab = grad(uv, vec3(0, 0, 1), scale, index);
    float bab = grad(uv, vec3(1, 0, 1), scale, index);
    float abb = grad(uv, vec3(0, 1, 1), scale, index);
    float bbb = grad(uv, vec3(1, 1, 1), scale, index);

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

float clampTo1(float x){
    return (x+1)/2;
}

vec4 pinkAqua(float noise){
    return noise>0?vec4(1,1-noise,1,1):vec4(1+noise,1,1,1);
}

vec4 blackWhite(float noise){
    noise=clampTo1(noise);
    return vec4(noise,noise,noise,1);
}


void main()
{
    vec3 uv = vec3(TexCoord * graphScale, time);

    /**if(animate){
        uv.x += time;
        uv.y += time;
//        uv.z += time * 0.5; // 添加Z轴动画
    }*/

    float noise1 = (perlinNoise(uv,1,0)+perlinNoise(uv,2,1))/2;
    float noiseLadder =noise1*4;
    float i=floor(noiseLadder)/4;
    float f=(1-fract(noiseLadder))/4;
    float noise=i+f;
//    float noise=perlinNoise(uv,0);

    FragColor = blackWhite(noise);

//    FragColor = texture(randomTextures[0],vec3(TexCoord,time));
}