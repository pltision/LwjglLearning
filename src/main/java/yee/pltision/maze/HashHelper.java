package yee.pltision.maze;

public class HashHelper {
    public final long mul;
    public final long xor;

    public long hash(long num){
        return (((num^xor)*mul)>>>16)|((num*mul)<<48);
    }

    public HashHelper(){
        this(1);
    }

    public HashHelper(long seed){
        //����0�����⣬���Ǹ�����
        xor=7080874202222568533L*seed;
        mul=6531603239788631109L*seed;
    }

}