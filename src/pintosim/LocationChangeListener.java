
package pintosim;

import java.awt.Point;

public interface LocationChangeListener {
    public void updateLocation(MovableObject subject, Point oldLocation, Point newLocation);
}
