package test.chunk;

public class Cache {

    int[] cache;
    int radius;
    int size;
    int pos;

    public Cache(int radius) {
        this.radius = radius;
        this.size = radius * 2 + 1;
        this.cache = new int[size];
    }

    public void fill() {
        for (int i = 0; i < size; i++) {
            cache[i]= mapToWorldPos(i);
        }
    }

    public int mapToWorldPos(int i){
        return (Math.floorDiv(pos-i+radius,size))*size+i;
    }

    public int mapToCacheIndex(int absolute){
        return Math.floorMod(absolute,size);
    }

    public void print() {
        System.out.println("pos:\t"+pos);
        System.out.print("cache:\t");
        for (int i = 0; i < size; i++) {
            System.out.print(cache[i]+"\t");
        }
        System.out.println();

        System.out.print("world:\t");
        for (int i = -2; i < 10; i++) {
            if(i>=pos-radius&&i<=pos+radius)
                System.out.print(mapToWorldPos(i)+"\t");
            else System.out.print("-\t");
        }
        System.out.println();

        System.out.print("index:\t");
        for (int i = -2; i < 10; i++) {
            if(i>=pos-radius&&i<=pos+radius)
                System.out.print(mapToCacheIndex(i)+"\t");
            else System.out.print("-\t");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Cache cache = new Cache(2);

        for(int i=0;i<8;i++){
            cache.pos=i;
            cache.fill();
            cache.print();
        }
    }
}