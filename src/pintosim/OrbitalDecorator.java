
package pintosim;

import java.awt.Graphics;


public class OrbitalDecorator implements Paintable {
    private Paintable paintable;
    private int radius;
    private long startTime;
    private double frequency;
    public OrbitalDecorator(Paintable paintable, int radius, double frequency) {
        this.paintable = paintable;
        startTime = System.currentTimeMillis();
        this.radius = radius;
        this.frequency = frequency;
    }
    
    public void paint(Graphics g) {
        int xShift = (int) Math.round(radius * Math.cos(elapsedTime() / frequency));
        int yShift = (int) Math.round(radius * Math.sin(elapsedTime() / frequency));
        g.translate(xShift, yShift);
        paintable.paint(g);
        g.translate(-xShift, -yShift);
    }

    public int zIndex() {
        return paintable.zIndex();
    }
    
    private long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

}
