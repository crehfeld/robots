

package pintosim;

import java.awt.Point;
import java.util.List;


/**
 * This class is used to conceptually provide "callback functions"
 * 
 */

public abstract class GetItemCallback {
    public void onComplete(List<Point> path) {

    }

    public void onCancel(Point location, String itemName) {

    }

}
