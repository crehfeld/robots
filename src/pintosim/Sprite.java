
package pintosim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;



public class Sprite implements Paintable {
    protected Image graphic;
    protected Point location;
    protected int zIndex;
    
    public Sprite(Image img, Point initialLocation, int zIndex) {
        graphic = img;
        location = initialLocation;
        this.zIndex = zIndex;
    }
    
    public void paint(Graphics g) {
        Point p = getCurrentLocation();
        g.drawImage(graphic, p.x, p.y, null);
    }
    
    protected Point getCurrentLocation() {
        return location;
    }
    
    public int zIndex() {
        return zIndex;
    }
    
}
