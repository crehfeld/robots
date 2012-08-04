/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pintosim;


public class EaseInOutCubic implements Tween {
    public double tween(double currentTime, double beginValue, double valueChangeAmount, double duration) {
        if ((currentTime /= duration / 2) < 1) {
            return valueChangeAmount / 2 * currentTime * currentTime * currentTime + beginValue;
        }

        return valueChangeAmount / 2 * ((currentTime -= 2) * currentTime * currentTime + 2) + beginValue;
    }

}
