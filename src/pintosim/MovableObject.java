package pintosim;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public abstract class MovableObject {
    protected final Point initialLocation;
    protected Point currentLocation;
    private List<LocationChangeListener> listeners = new ArrayList<LocationChangeListener>();
    
    public MovableObject(Point loc) {
        this(loc.x, loc.y);
    }
    
    public MovableObject(int x, int y) {
        initialLocation = currentLocation = new Point(x, y);
    }

    public void move(Point loc) {
        move(loc.x, loc.y);
    }
    
    public void move(int x, int y) {
        currentLocation.x = x;
        currentLocation.y = y;
        notifyLocationChangeListeners();
    }
    
    public int getX() {
        return currentLocation.x;
    }
    
    public int getY() {
        return currentLocation.y;
    }
    
    private void notifyLocationChangeListeners() {
        for (LocationChangeListener listener : listeners) {
            listener.updateLocation(this);
        }
    }
    
    public void addLocationChangeListeners(LocationChangeListener listener) {
        listeners.add(listener);
    }
}
