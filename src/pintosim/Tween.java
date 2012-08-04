
package pintosim;

import java.awt.Point;


public interface Tween {

    /**
     * Scales a value as a function of time, using an algorithm.<br>
     * 
     * For example, if we wanted to scale the opacity of something,
     * say from 100% to 60% over a time period of 2000ms, you could use:<br>
     * beginValue == 100<br>
     * valueChangeAmount == -40<br>
     * duration == 2000<br>
     * <br>
     * Then, you would repeatedly call tween to get the opacity value as 
     * a function of the current time. Time starts at 0. Time units are 
     * arbitrary, its up to the caller to pick a unit convention and be 
     * consistent in supplying values for duration and currentTime.<br>
     * <br>
     * 
     * If a linear algorithm was implementing this interface, some sample 
     * return values would be:<br>
     * 
     * 100 == obj.tween(0, 100, -40, 2000)<br>
     * 90 == obj.tween(500, 100, -40, 2000)<br>
     * 80 == obj.tween(1000, 100, -40, 2000)<br>
     * 60 == obj.tween(2000, 100, -40, 2000)<br>
     * 
     * <br>
     * It is the responsibility of the caller to not call this
     * function with a currentTime that is greater than duration.
     * 
     * <br>This interface is modeled after the conventions detailed in: 
     * http://www.robertpenner.com/easing/penner_chapter7_tweening.pdf
     * 
     * <br> cool demo of common implementated algos<br>
     * http://www.robertpenner.com/easing/easing_demo.html
     * 
     * 
     * @param currentTime The current time, between 0 and duration.
     * @param beginValue The starting value of the value being scaled.
     * @param valueChangeAmount The delta amount of change that will occur over the course of duration time.
     * @param duration How long the scaling takes.
     * @return The scaled value
     */
    public double tween(double currentTime, double beginValue, double valueChangeAmount, double duration);
}
