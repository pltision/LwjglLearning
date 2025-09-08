package yee.pltision.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.function.BiFunction;

public class Lerp<T> {

    public interface CopyFunction<T> extends BiFunction<T,T,T> {
        T apply(T dist,T value);
    }

    public LerpFunction<T> lerpFunction;
    public CopyFunction<T> copyFunction;

    public T value;
    public T start;
    public T target;

    LerpFactor leapFactor;
    float time;

    public T getValue() {
        return value;
    }

    //构造

    public Lerp(LerpFunction<T> lerpFunction,CopyFunction<T> copyFunction,T value,T start,T target){
        this.lerpFunction=lerpFunction;
        this.copyFunction =copyFunction;
        this.value=value;
        this.start=start;
        this.target=target;
    }

    @NotNull
    @Contract(" -> new")
    public static Lerp<Quaternionf> create(){
        return create(new Quaternionf());
    }

    @NotNull
    @Contract("_ -> new")
    public static Lerp<Quaternionf> create(Quaternionf value) {
        return new Lerp<>(Quaternionf::nlerp, Quaternionf::set,value,new Quaternionf(),new Quaternionf());
    }


    //调用

    public void update(float passedTime){
        if(leapFactor !=null){
            if(time+passedTime>1){
                copyFunction.apply(value,target);
                clear();
                return;
            }
            time+=passedTime;
            value= lerpFunction.getLerped(start,target,leapFactor.getValue(time),value);

        }
    }

    public void applyLeap(LerpFactor leap, T target){
        this.leapFactor = leap;
        this.target=copyFunction.apply(this.target,target);
        this.start=copyFunction.apply(this.start,value);
        time=0;
    }

    public void clear(){
        time=0;
        leapFactor = null;
    }

}