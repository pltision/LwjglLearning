package yee.pltision.glfmhelper;

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

    /**
     * Constant by which to multiply an angular value in degrees to obtain an
     * angular value in radians.
     */
    public static final float DEGREES_TO_RADIANS = 1 / PI * 180;

    /**
     * Constant by which to multiply an angular value in radians to obtain an
     * angular value in degrees.
     */
    public static final float RADIANS_TO_DEGREES = 1 / DEGREES_TO_RADIANS;

    public static float toRadians(float angdeg) {
        return angdeg * DEGREES_TO_RADIANS;
    }

    public static float toDegrees(float angrad) {
        return angrad * RADIANS_TO_DEGREES;
    }


    public static float lerp(float start, float end, float linear) {
        return start+linear*(end-start);
    }
    public static float lerpSquared(float start, float end, float linear) {
        return start+linear*linear*(end-start);
    }
}