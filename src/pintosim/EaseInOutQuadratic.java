/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pintosim;


public class EaseInOutQuadratic implements Tween {
    public double tween(double currentTime, double beginValue, double valueChangeAmount, double duration) {
        if ((currentTime /= duration / 2) < 1) {

            return valueChangeAmount / 2 * currentTime * currentTime + beginValue;
        }

        return -valueChangeAmount / 2 * ((--currentTime) * (currentTime - 2) - 1) + beginValue;
    }

}
