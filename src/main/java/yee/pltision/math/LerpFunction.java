package yee.pltision.math;

@FunctionalInterface
public interface LerpFunction<T> {
    T getLerped(T from,T to,float time,T dist);
}
