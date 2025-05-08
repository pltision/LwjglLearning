package yee.pltision.glfmhelper.globject;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class VertexProperties {

    public static class Element {
        GLDataType type;
        int count;

        Element(GLDataType type, int count) {
            this.type = type;
            this.count = count;
        }
    }

    private final Element[] elements;
    public final int size;
    public final int floatSize;

    public VertexProperties(Element... elements) {
        this.elements = elements;
        this.size = computeSize(elements);
        this.floatSize = computeFloatSize(elements);
    }

    public int floatSizeOf(int vertexCount){
        if(floatSize<0)
            throw new UnsupportedOperationException("Element are not all are float!");
        return floatSize*vertexCount;
    }

    public int byteSizeOf(int vertexCount) {
        return size*vertexCount;
    }

    public static int computeSize(Element[] elements){
        int sum=0;
        for (Element element : elements)
            sum += element.count * element.type.size;
        return sum;
    }
    public static int computeFloatSize(Element[] elements){
        int sum=0;
        for (Element element : elements) {
            sum += element.count * element.type.size;
            if(element.type!=GLDataType.FLOAT)
                return -1;
        }
        return sum;
    }

    public static Element element(GLDataType type, int count) {
        return new Element(type,count);
    }

    public static Element i() {
        return element(GLDataType.INT, 1);
    }

    public static Element f() {
        return element(GLDataType.FLOAT, 1);
    }

    public static Element d() {
        return element(GLDataType.DOUBLE, 1);
    }

    public static Element ints(int count) {
        return element(GLDataType.FLOAT, count);
    }

    public static Element floats(int count) {
        return element(GLDataType.FLOAT, count);
    }

    public static Element doubles(int count) {
        return element(GLDataType.FLOAT, count);
    }

    public static Element pos() {
        return floats(3);
    }

    public static Element rgb() {
        return floats(3);
    }

    public static Element rgba() {
        return floats(4);
    }

    public static Element matrix3() {
        return floats(3 * 3);
    }

    public static Element matrix4() {
        return floats(4 * 4);
    }

    public static Element uv() {
        return floats(2);
    }

    public void apply() {
        int location = 0;
        int byteIndex = 0;
        for (Element element : elements) {
            glVertexAttribPointer(location, element.count, element.type.type, false, size, byteIndex);
            glEnableVertexAttribArray(location);
            byteIndex += element.count * element.type.size;
            location++;
        }
    }

}