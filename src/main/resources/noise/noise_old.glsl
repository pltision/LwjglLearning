#version 330 core
                out vec4 FragColor;
in vec2 TexCoord;
uniform float noiseScale;
uniform float time;
uniform bool animate;

int rand(vec2 co)
{
    return int(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453)%4;
}

float smoothstep(float t)
{
    return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
}

float lerp(float a, float b, float t)
{
    return a + t * (b - a);
}

float grad(int hash, float x, float y)
{
    switch(hash % 4){
        case 0: return x + y;
        case 1: return -x + y;
        case 2: return x - y;
        default: return -x - y;
    }
}

float perlinNoise(vec2 uv)
{
    // 网格单元坐标
    vec2 i = floor(uv);
    vec2 f = fract(uv);
    // 平滑曲线
    float u = smoothstep(f.x);
    float v = smoothstep(f.y);
    // 网格四个角的哈希值
    int hash00 = rand(i);
    int hash10 = rand(i + vec2(1.0, 0.0));
    int hash01 = rand(i + vec2(0.0, 1.0));
    int hash11 = rand(i + vec2(1.0, 1.0));
    // 计算梯度贡献
    float a = grad(hash00, f.x, f.y);
    float b = grad(hash10, f.x - 1.0, f.y);
    float c = grad(hash01, f.x, f.y - 1.0);
    float d = grad(hash11, f.x - 1.0, f.y - 1.0);
    // 双线性插值
    return lerp(lerp(a, b, u), lerp(c, d, u), v);
}

float sqr(float x){
    return x*x;
}

void main()
{
    vec2 uv = TexCoord * noiseScale;

    // 如果动画开启，添加时间偏移
    if(animate){
        uv.x += time * 0.3;
        uv.y += time * 0.2;
    }

    // 计算噪声值
    float noise1 = (perlinNoise(uv)+perlinNoise(uv*0.5+104)/2+perlinNoise(uv*0.25+234)/2)/2;
    float noise2 = (perlinNoise(uv+124)+perlinNoise(uv*0.5+61)/2+perlinNoise(uv*0.25+34)/2)/2;
    float noise=1-max(sqrt(abs(noise1)),sqrt(abs(noise2)));

    // 映射到[0,1]范围并输出为灰度
    //   noise = (noise + 1.0) * 0.5;
    //                   FragColor = noise>0?vec4(1,1-noise,1, 1.0):vec4(1+noise,1,1, 1.0);
    FragColor = vec4(0,sqr(noise),noise, 1.0);
}