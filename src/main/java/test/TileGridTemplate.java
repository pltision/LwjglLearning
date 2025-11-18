package test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileGridTemplate extends JPanel {
    public static final char[][] grid={
            "## ##".toCharArray(),
            " #   ".toCharArray(),
            "#   #".toCharArray(),
            "### #".toCharArray(),
            "## ##".toCharArray(),
    };

    public static void paint(Graphics g,int x,int y,int size){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                g.setColor(Color.GREEN);
                if(grid[i][j]=='#'){
                    g.fillRect(j*size*2+x,i*size*2+y,size,size);
                }
                if(grid[i][j+1]=='#'){
                    g.fillRect(j*size*2+x+size,i*size*2+y,size,size);
                }
                if(grid[i+1][j]=='#'){
                    g.fillRect(j*size*2+x,i*size*2+y+size,size,size);
                }
                if(grid[i+1][j+1]=='#'){
                    g.fillRect(j*size*2+x+size,i*size*2+y+size,size,size);
                }
                g.setColor(Color.BLACK);
                g.drawRect(j*size*2+x,i*size*2+y,size*2,size*2);
                int toInt=
                        ((grid[i][j]=='#'?1:0)<<3)|
                        ((grid[i][j+1]=='#'?1:0)<<2)|
                        ((grid[i+1][j]=='#'?1:0)<<1)|
                        (grid[i+1][j+1]=='#'?1:0);
                g.drawString(
                        (grid[i][j]=='#'?"1":"0")+
                        (grid[i][j+1]=='#'?"1":"0")+
                        (grid[i+1][j]=='#'?"1":"0")+
                        (grid[i+1][j+1]=='#'?"1":"0")+
                        "    "+toInt,
                        j*size*2+size/4,
                        i*size*2+size/2
                );
            }
        }
    }

    public void paint(Graphics g){
        paint(g,0,0,32);
    }

    public static void main(String[] args) {
//        createPicture();
        window();
    }

    public static void window(){
        JFrame frame=new JFrame();
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TileGridTemplate());
        frame.setVisible(true);
    }
    public static void createPicture(){
        File file=new File("tile_template.png");
        try{
            int size=1024;
            BufferedImage image=new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d=image.createGraphics();
            paint(g2d,0,0,size/8);
            g2d.dispose();
            ImageIO.write(image,"png",file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}