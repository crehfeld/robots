
package pintosim;


public class EaseLinear implements Tween {
    public double tween(double currentTime, double beginValue, double valueChangeAmount, double duration) {
        return valueChangeAmount * currentTime / duration + beginValue;
    }
}
