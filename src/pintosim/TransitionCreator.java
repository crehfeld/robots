
package pintosim;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;


public class TransitionCreator implements LocationChangeListener {
    protected int animationDuration;
    protected int tileSize;
    protected MovingSprite sprite;
    protected Tween tweeningStrategy;
    
    public TransitionCreator(int animationDuration, int tileSize, MovingSprite sprite, Tween tween) {
        this.animationDuration = animationDuration;
        this.tileSize = tileSize;
        this.sprite = sprite;
        tweeningStrategy = tween;
    }
    
    public void updateLocation(MovableObject obj, Point from, Point to) {
        Point easeFrom = new Point(from.x * tileSize, from.y * tileSize);
        Point easeTo = new Point(to.x * tileSize, to.y * tileSize);
        sprite.addTransition(new TimeBasedTransition(easeFrom, easeTo, animationDuration, tweeningStrategy));
    }

}
