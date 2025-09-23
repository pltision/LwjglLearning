package yee.pltision.glfmhelper.globject;

public class ShaderProgramLinkException extends GLException{
    public final String programInfoLog;
    public final int id;
    public ShaderProgramLinkException(int id,String programInfoLog) {
        super("着色器程序 (id = "+id+") 链接失败: "+programInfoLog);
        this.id=id;
        this.programInfoLog=programInfoLog;
    }
}