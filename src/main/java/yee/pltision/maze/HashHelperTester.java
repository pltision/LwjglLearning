package yee.pltision.maze;

import java.util.concurrent.atomic.AtomicInteger;

public class HashHelperTester extends HashHelper{

    public void randomTester() {
        final int threadCount=10,randomCount=100000000,arrayLength=100,sum=threadCount*randomCount;
        int[][] arrays=new int[threadCount][arrayLength];
        AtomicInteger complete= new AtomicInteger();

        for(int i=0;i<threadCount;i++){
            final int index=i;
            new Thread(()->{
                for(int j=index*randomCount,to=index*randomCount+randomCount;j<to;j++){
                    arrays[index][((int)hash(j)&Integer.MAX_VALUE)%arrayLength]++;
                }
                complete.addAndGet(1);
            }).start();
        }

        while (complete.get() <threadCount)
            Thread.yield();

        for (int i=0;i<arrayLength;i++) {
            int count=0;
            for(int[] array:arrays)
                count+=array[i];
            System.out.println((float) count / sum);
        }

    }

    public void testBoolean(){
//        final int arrayLength=200;
//        int[] array=new int[arrayLength];
        for(int i=0;i<200;i++){
            System.out.print(hash(i)&1);
        }
    }


    public static void main(String[] args) {
//        new HashHelperTester().randomTester();
        new HashHelperTester().testBoolean();
    }


}