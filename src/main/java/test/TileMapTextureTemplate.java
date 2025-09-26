package test;

import javax.swing.*;
import java.awt.*;

public class TileMapTextureTemplate {

    int tileSize =16;

    char[][] corners=createCorners();
    char[][] edges=createEdges();

    int zoom=4;

    int edge=64;

    char[][] createCorners(){
        char[] leftTop= """
            012#456789abcdef\
            012#456789abcdef\
            012#456789abcdef\
            ####456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            """.toCharArray();
        char[][] corners=new char[4][];
        corners[0]=leftTop;
        corners[1]=rotateClock(leftTop,tileSize,new char[tileSize*tileSize]);
        corners[2]=rotateClock(corners[1],tileSize,new char[tileSize*tileSize]);
        corners[3]=rotateClock(corners[2],tileSize,new char[tileSize*tileSize]);
        return corners;
    }
    char[][] createEdges(){
        char[] top= """
            0123456789abcdef\
            0123456789abcdef\
            ################\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            0123456789abcdef\
            """.toCharArray();
        char[][] edges=new char[4][];
        edges[0]=top;
        edges[1]=rotateClock(top,tileSize,new char[tileSize*tileSize]);
        edges[2]=rotateClock(edges[1],tileSize,new char[tileSize*tileSize]);
        edges[3]=rotateClock(edges[2],tileSize,new char[tileSize*tileSize]);
        return edges;
    }

    public static char[] rotateClock(char[] map,int size,char[] dist){
        for (int i = 0; i < size*size; i++) {
            int x=i%size;
            int y=i/size;
            dist[ x *size + size-y-1 ]=map[i];
        }
        return dist;
    }

    void paint(Graphics g){
        for(int i=0;i<=0b11111111;i++){
            g.setColor(Color.GRAY);
            g.fillRect((i&0b1111)*(tileSize+2)*zoom+edge,(i>>4)*(tileSize+2)*zoom+edge,tileSize*zoom,tileSize*zoom);
            g.setColor(Color.GREEN);
            paintGrid(g,(i&0b1111)*(tileSize+2)*zoom+edge,(i>>4)*(tileSize+2)*zoom+edge,i,tileSize,zoom,corners,edges);
        }
    }

    static void paintGrid(Graphics g,int x,int y,int neighbor,int tileSize,int zoom,char[][] corners,char[][] edge){
        int neighborIndex=0;
        while(neighbor!=0){
            if((neighbor&1)!=0){
                if((neighborIndex&1)==0){
                    paintGraph(g,x,y,tileSize,zoom,corners[neighborIndex>>1]);
                }else{
                    paintGraph(g,x,y,tileSize,zoom,edge[neighborIndex>>1]);
                }
            }

            neighbor>>>=1;
            neighborIndex++;
        }

    }

    static void paintGraph(Graphics g,int x,int y,int tileSize,int zoom,char[] graph){
        for(int i=0;i<tileSize;i++){
            for(int j=0;j<tileSize;j++){
                if(graph[i+tileSize*j]=='#'){
                    g.fillRect(x+i*zoom,y+j*zoom,zoom,zoom);
                }
            }
        }
    }

    public static void main(String[] args) {
        TileMapTextureTemplate main = new TileMapTextureTemplate();
        JFrame frame=new JFrame(){
            @Override
            public void paint(Graphics g){
                main.paint(g);
            }
        };
        frame.setSize((main.tileSize+2)*main.zoom*16+main.edge*2,(main.tileSize+2)*main.zoom*16+main.edge*2);
        frame.setVisible(true);
    }

    public void printArray(char[] array){
        for(int i=0;i<tileSize;i++){
            for(int j=0;j<tileSize;j++){
                System.out.print(array[i*tileSize+j]);
            }
            System.out.println();
        }
    }
}