
package pintosim;

import java.awt.Point;



public class TCreator implements LocationChangeListener {
    protected int animationDuration;
    protected int tileSize;
    protected MovementDecorator movementDecorator;
    protected Tween tweeningStrategy;
    
    public TCreator(int animationDuration, int tileSize, MovementDecorator movementDecorator, Tween tween) {
        this.animationDuration = animationDuration;
        this.tileSize = tileSize;
        this.movementDecorator = movementDecorator;
        tweeningStrategy = tween;
    }
    
    public void updateLocation(MovableObject obj, Point from, Point to) {
        Point easeFrom = new Point(from.x * tileSize, from.y * tileSize);
        Point easeTo = new Point(to.x * tileSize, to.y * tileSize);
        movementDecorator.addTransition(new TimeBasedTransition(easeFrom, easeTo, animationDuration, tweeningStrategy));
    }

}
