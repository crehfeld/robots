/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pintosim;


public class EaseOutExponential implements Tween {
    public double tween(double currentTime, double beginValue, double valueChangeAmount, double duration) {
        double timeRemaining = duration - currentTime;
        return (timeRemaining < 0.000001) ? 
                beginValue + valueChangeAmount 
                : valueChangeAmount * (-Math.pow(2, -10 * currentTime / duration) + 1f) + beginValue;
    }
}
