package yee.pltision.glfmhelper.globject;

import static org.lwjgl.opengl.GL11.*;

public enum GLDataType {
    FLOAT(GL_FLOAT,4),
    DOUBLE(GL_DOUBLE,8),

    BYTE(GL_BYTE,1),
    INT(GL_INT,4),
    SHORT(GL_SHORT,2),

    UNSIGNED_BYTE(GL_UNSIGNED_BYTE,1),
    UNSIGNED_INT(GL_UNSIGNED_INT,4),
    UNSIGNED_SHORT(GL_UNSIGNED_SHORT,2),

    BYTE_2(GL_2_BYTES,2),
    BYTE_3(GL_3_BYTES,3),
    BYTE_4(GL_4_BYTES,4),

    ;

    public final int type,size;
    GLDataType(int type, int size){
        this.type=type;
        this.size=size;
    }
}
