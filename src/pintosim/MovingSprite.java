
package pintosim;


import java.awt.Image;
import java.awt.Point;
import java.util.LinkedList;


public class MovingSprite extends Sprite {
    private LinkedList<Transition> transitions = new LinkedList<Transition>();
    private Transition currentTransition;
    
    public LinkedList<Transition> transitionHistoryForDebugging = new LinkedList<Transition>();
    
    public MovingSprite(Image img, Point initialLocation, int zIndex) {
        super(img, initialLocation, zIndex);
    }
    

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    
    protected Point getCurrentLocation() {
        fastForwardToCurrentTransition();
        //we may not have any transitions yet, so return the starting location
        return currentTransition != null ? currentTransition.getCurrentLocation() : location;
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
            currentTransition = transitions.poll();
            transitionHistoryForDebugging.add(currentTransition);
        }
    }
    
    
}
