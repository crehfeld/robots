
package pintosim;

import java.awt.Point;
import java.util.List;


public interface PathFinder {
    
    public void spaceUnoccupied(int x, int y);
    
    public void spaceOccupied(int x, int y);
    
    public List<Point> getPath(Point from, Point to);
}
