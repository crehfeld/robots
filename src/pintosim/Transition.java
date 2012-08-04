
package pintosim;

import java.awt.Point;


public interface Transition {
    public boolean complete();
    public Point getCurrentLocation();
}
