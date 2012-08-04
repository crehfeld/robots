
package pintosim;

import java.awt.Point;


public class TimeBasedTransition implements Transition {
    private Tween tweenStrategy;
    private Point from;
    private Point delta;
    private int duration;
    private long timeStart;
    
    protected int numCallsForDebugging = 0;
    
    public TimeBasedTransition(Point from, Point to, int duration, Tween tweener) {
        this.from = new Point(from);
        this.delta = new Point(to.x - from.x, to.y - from.y);
        this.duration = duration;
        this.tweenStrategy = tweener;
        this.timeStart = System.currentTimeMillis();
    }
    
    public boolean complete() {
        return elapsedTime() >= duration;
    }
    
    private long elapsedTime() {
        return System.currentTimeMillis() - timeStart;
    }
    
    
    public Point getCurrentLocation() {
        numCallsForDebugging++;

        //tween doesnt want values greater than duration
        long elapsed = Math.min(duration, elapsedTime());
        
        int x = (int) tweenStrategy.tween(elapsed, from.x, delta.x, duration);
        int y = (int) tweenStrategy.tween(elapsed, from.y, delta.y, duration);
        return new Point(x, y);
    }
}
