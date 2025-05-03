package yee.pltision.glfmhelper.globject;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL30.*;

public class VertexStruct implements GLObject {
    int vertexArray;
    int size=0;

    public VertexStruct(int vertexArray){
        this.vertexArray=vertexArray;
    }

    public static  VertexStruct create(){
        return new VertexStruct(glGenVertexArrays());
    }

    public int getSize() {
        return size;
    }

    public int size(int vertexCount){
        return getSize()*vertexCount;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void delete() {
        glDeleteVertexArrays(vertexArray);
    }

    public int getVertexArray() {
        return vertexArray;
    }

    public void bind(){
        glBindVertexArray(vertexArray);
    }

    public Stream stream(){
        bind();
        return new Stream();
    }

    public class Stream{

        class Data{
            GLDataType type;
            int count;

            Data(GLDataType type, int count){
                this.type=type;
                this.count=count;
            }
        }
        LinkedList<Data> buffer=new LinkedList<>();

        public void next(GLDataType type, int count){
            buffer.add(new Data(type,count));
        }

        public void finalBuild(){
            int location=0;
            int byteIndex=0;
            int sum=0;
            for(Data data:buffer)
                sum += data.count * data.type.size;
            for(Data data:buffer){
                glVertexAttribPointer(location, data.count, data.type.type,false, sum, byteIndex);
                glEnableVertexAttribArray(location);
                byteIndex+=data.count*data.type.size;
                location++;
            }
            setSize(sum);
        }

        public VertexStruct build(){
            finalBuild();
            return VertexStruct.this;
        }

        public Stream i(){
            next(GLDataType.INT,1);
            return this;
        }
        public Stream f(){
            next(GLDataType.FLOAT,1);
            return this;
        }
        public Stream d(){
            next(GLDataType.DOUBLE,1);
            return this;
        }

        public Stream ints(int dim){
            next(GLDataType.FLOAT,dim);
            return this;
        }
        public Stream floats(int dim){
            next(GLDataType.FLOAT,dim);
            return this;
        }
        public Stream doubles(int dim){
            next(GLDataType.FLOAT,dim);
            return this;
        }

        public Stream pos(){
            return floats(3);
        }
        public Stream rgb(){
            return floats(3);
        }
        public Stream rgba(){
            return floats(4);
        }

        public Stream matrix3(){
            return floats(3*3);
        }
        public Stream matrix4(){
            return floats(4*4);
        }

        public Stream uv(){
            return floats(2);
        }

    }
}
