
package pintosim;

import java.awt.Point;



public class TransitionCreator implements LocationChangeListener {
    protected int animationDuration;
    protected int tileSize;
    protected MovementTransitionDecorator movementDecorator;
    protected Tween tweeningStrategy;
    
    public TransitionCreator(int animationDuration, int tileSize, MovementTransitionDecorator movementDecorator, Tween tween) {
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
