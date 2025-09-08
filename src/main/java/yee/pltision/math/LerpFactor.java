package yee.pltision.math;

@FunctionalInterface
public interface LerpFactor {
    float getValue(float time);

    LerpFactor LINE =(time)->time;
    LerpFactor SQUARED =(time)->time*time;
    LerpFactor INVERT_SQUARE  =(t)->1-(1-t)*(1-t);
    LerpFactor INVERT_CUBE    =(t)->1-(1-t)*(1-t)*(1-t);          //四次方结束要更平滑些
    LerpFactor INVERT_QUARTIC =(t)->1-(1-t)*(1-t)*(1-t)*(1-t);    //开头弹的有点猛但比较平滑，0.25秒或1/3秒比较舒服
    LerpFactor INVERT_QUINTIC    =(t)->1-(1-t)*(1-t)*(1-t)*(1-t)*(1-t);
    LerpFactor INVERT_SEXTIC =(t)->1-(1-t)*(1-t)*(1-t)*(1-t)*(1-t)*(1-t);
    LerpFactor INVERT_SEPTIC =(t)->1-(1-t)*(1-t)*(1-t)*(1-t)*(1-t)*(1-t)*(1-t);
    LerpFactor INVERT_OCTIC =(t)->1-(1-t)*(1-t)*(1-t)*(1-t)*(1-t)*(1-t)*(1-t)*(1-t);

    LerpFactor FADE =(t)->t*t*t*(t*(t*6-15)+10);
    LerpFactor SMOOTHSTEP =(t)-> t*t*(3-2*t);

}
