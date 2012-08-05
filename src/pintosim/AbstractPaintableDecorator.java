
package pintosim;

import java.awt.Graphics;
import java.awt.Point;


public abstract class AbstractPaintableDecorator implements Paintable {
    private Paintable paintable;
    
    public AbstractPaintableDecorator(Paintable paintable) {
        this.paintable = paintable;
    }
    
    final public void paint(Graphics g) {
        Point offset = getRelativeOffset();
        g.translate(offset.x, offset.y);
        paintable.paint(g);
        g.translate(-offset.x, -offset.y);
    }

    public int zIndex() {
        return paintable.zIndex();
    }
    
    protected abstract Point getRelativeOffset();
}
