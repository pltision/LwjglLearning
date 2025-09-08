package yee.pltision.math;

public class Mth {
    /**
     * The {@code double} value that is closer than any other to
     * <i>pi</i> (&pi;), the ratio of the circumference of a circle to
     * its diameter.
     */
    public static final float PI = (float) Math.PI;

    /**
     * The {@code double} value that is closer than any other to
     * <i>tau</i> (&tau;), the ratio of the circumference of a circle
     * to its radius.
     *
     * @apiNote
     * The value of <i>pi</i> is one half that of <i>tau</i>; in other
     * words, <i>tau</i> is double <i>pi</i> .
     *
     * @since 19
     */
    public static final float TAU = 2.0f * PI;

    public static final float HALF_PI=PI/2;
    public static final float QUARTER_PI=PI/4;
    public static final float THREE_QUARTER_PI=3*PI/4;

    public static final float RADIANS_TO_DEGREES = 180/PI;

    public static final float DEGREES_TO_RADIANS = 1 / RADIANS_TO_DEGREES;

    public static float toRadians(float angdeg) {
        return angdeg * DEGREES_TO_RADIANS;
    }

    public static float toDegrees(float angrad) {
        return angrad * RADIANS_TO_DEGREES;
    }


    public static float lerp(float start, float end, float linear) {
        return start+linear*(end-start);
    }

    /**
     * 快速平方根倒数算法
     */
    public static float invSqrt(float x)
    {
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);    // get bits for floating value
        i = 0x5f375a86 - (i>>1);            // gives initial guess
        x = Float.intBitsToFloat(i);        // convert bits back to float
        x = x * (1.5f - xhalf*x*x);         // Newton step
        return x;
    }

    /**
     * 用快速平方根倒数算法求平方根
     */
    public static float qSqrt(float x)
    {
        return 1/invSqrt(x);
    }

    /**
     * 从from向to移动dist距离，如果超出则不移动
     */
    public static float closeTo(float to, float from, float distance){
        if(from>to){
            return Math.max(from - distance, to);
        }
        else if(from<to){
            return Math.min(from + distance, to);
        }
        else {
            return to;
        }
    }

    public float getClosest(float target,float a,float b){
        if(Math.abs(a-target)<Math.abs(b-target)){
            return a;
        }
        else {
            return b;
        }
    }



}