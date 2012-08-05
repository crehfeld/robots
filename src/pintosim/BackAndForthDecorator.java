
package pintosim;

import java.awt.Graphics;
import java.awt.Point;

public class BackAndForthDecorator extends AbstractPaintableDecorator {
    private int radius;
    private long startTime;
    private double frequency;
    
    public BackAndForthDecorator(Paintable paintable, int radius, double frequency) {
        super(paintable);
        startTime = System.currentTimeMillis();
        this.radius = radius;
        this.frequency = frequency;
    }
    
    protected Point getRelativeOffset() {
        return new Point(
            (int) Math.round(radius * Math.cos(elapsedTime() / frequency))
          , 0
        );
    }
    private long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

}