
package pintosim;

import java.awt.Graphics;
import java.awt.Point;


public class OrbitalDecorator extends AbstractPaintableDecorator {
    private int radius;
    private long startTime;
    private double frequency;
    
    public OrbitalDecorator(Paintable paintable, int radius, double frequency) {
        super(paintable);
        startTime = System.currentTimeMillis();
        this.radius = radius;
        this.frequency = frequency;
    }

    protected Point getRelativeOffset() {
        return new Point(
            (int) Math.round(radius * Math.cos(elapsedTime() / frequency))
          , (int) Math.round(radius * Math.sin(elapsedTime() / frequency))
        );
    }
    
    private long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

}
