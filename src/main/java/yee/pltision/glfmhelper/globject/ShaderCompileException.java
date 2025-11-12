package yee.pltision.glfmhelper.globject;

public class ShaderCompileException extends GLException {
    public final String shaderInfoLog;
    public final int id;

    public ShaderCompileException(int id,String shaderInfoLog){
        super("着色器编译失败 (id = "+id+") : "+shaderInfoLog);
        this.shaderInfoLog=shaderInfoLog;
        this.id=id;
    }
}