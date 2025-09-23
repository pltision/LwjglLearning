package yee.pltision.glfmhelper.globject;

public class ShaderCompileException extends GLException {
    public final String shaderInfoLog;
    public final int id;

    public ShaderCompileException(int id,String shaderInfoLog){
        super("着色器 (id = "+id+") 编译失败: "+shaderInfoLog);
        this.shaderInfoLog=shaderInfoLog;
        this.id=id;
    }
}