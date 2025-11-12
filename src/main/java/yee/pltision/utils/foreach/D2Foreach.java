package yee.pltision.utils.foreach;

public interface D2Foreach {
    interface D2Consumer{
        void accept(int x,int y);
    }

    static void foreachSquare(int x, int y, int radius, D2Consumer consumer){
        for(int i=x-radius,toX=x+radius;i<toX;i++)
            for(int j=y-radius,toY=y+radius;j<toY;j++)
                consumer.accept(i,j);
    }

    static void foreachSquare(int x, int y, D2Consumer consumer){
        for(int i=x-1,toX=x+1;i<toX;i++)
            for(int j=y-1,toY=y+1;j<toY;j++)
                consumer.accept(i,j);
    }
}