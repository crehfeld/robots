
package pintosim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.LinkedList;


public class MovementDecorator implements Paintable {
    private LinkedList<Transition> transitions = new LinkedList<Transition>();
    private Transition currentTransition;
    private Point totalMovementDelta = new Point(0, 0);
    
    public LinkedList<Transition> transitionHistoryForDebugging = new LinkedList<Transition>();
    
    private Paintable paintable;
    
    public MovementDecorator(Paintable paintable) {
        this.paintable = paintable;
    }
    

    public synchronized void addTransition(Transition transition) {
        transitions.add(transition);
    }

    
    protected Point getCurrentLocation() {
        fastForwardToCurrentTransition();
        //we may not have any transitions yet, so return the starting location
        Point currentLoc = new Point(totalMovementDelta);
        if (currentTransition != null) {
            Point delta = currentTransition.getCurrentDeltaLocation();
            currentLoc.translate(delta.x, delta.y);
        }
        return currentLoc;
    }

    /**
     * Transitions are stored in the order they were added. its assumed that earlier added transitions 
     * will return true for complete() before later added ones. The reason we advance and possibly skip
     * some transitions is because the system can fall behind time wise, and the animation model we choose states that
     * that in the event rendering falls behind, we should skip frames in order to try to keep up.
     */
    private void fastForwardToCurrentTransition() {
        if (currentTransition == null) {
            if (transitions.isEmpty()) {
                // no transitions yet
                return;
            } else {
                currentTransition = transitions.poll();
                transitionHistoryForDebugging.add(currentTransition);
            }
        }
        
        while (currentTransition.complete() && !transitions.isEmpty()) {
            Point delta = currentTransition.getDeltaLocation();
            totalMovementDelta.translate(delta.x, delta.y);
            currentTransition = transitions.poll();
            transitionHistoryForDebugging.add(currentTransition);
        }
    }
    
    public int zIndex() {
        return paintable.zIndex();
    }
    
    public synchronized void paint(Graphics g) {
        Point p = getCurrentLocation();
        g.translate(p.x, p.y);
        paintable.paint(g);
        g.translate(-p.x, -p.y);
    }
}
