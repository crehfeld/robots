package pintosim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

enum PINTO_STATUS {

    IDLE,
    BUSY
}

public class Pinto extends MovableObject {

    private PintoManager _pintoManager;
    private Point _myCurrentLocation;
    private PINTO_STATUS _status;
    private List<Point> _path;
    private Command command;
    
    
    private Point itemLocation;
    private Point personLocation;
    private Point dockingStationLocation;
    private Point currentHeading;
    private List<Point> currentPath;
    private PathFinder pathFinder;
    private Item itemBeingCarried;
    private EnviornmentMap enviornmentMap;
            

    
    
    private enum PintoState {
        NAVIGATING_TO_ITEM
      , NAVIGATING_TO_PERSON
      , NAVIGATING_TO_DOCKING_STATION
      , IDLE
    }
    
    private PintoState state = PintoState.IDLE;

    public PINTO_STATUS getStatus() {
        return this._status;
    }

    public Pinto(EnviornmentMap map, PintoManager pintoManager, Point initialLocation, PathFinder pathFinder) {
        super(initialLocation);

        enviornmentMap = map;
        this._pintoManager = pintoManager;
        this._status = PINTO_STATUS.BUSY;
        this._path = null;
        this._myCurrentLocation = new Point(0, 0);
        this.pathFinder = pathFinder;
    }

    public String getItemWorkingOn() {
        return command.getItemName();
    }

    public void setStatus(PINTO_STATUS status) {
        _status = status;
    }
    
    

    public void getItem(Command cmd) {
        command = cmd;


        Point ds = enviornmentMap.getPintoDockingStationLocation();
        Point elderly = enviornmentMap.getPersonLocation();
        Point item = enviornmentMap.getItemLocation(command.getItemName());

        setDestinations(item, elderly, ds);
        _path = new ArrayList<Point>();

        




    }
    
    public void setDestinations(Point itemLocation, Point personLocation, Point dockingStationLocation) {
        if (state != PintoState.IDLE) {
            throw new IllegalStateException("cant set new destinations while pinto is navigating");
        }
        
        this.itemLocation = itemLocation;
        this.personLocation = personLocation;
        this.dockingStationLocation = dockingStationLocation;
        transitionToNewState(PintoState.NAVIGATING_TO_ITEM);
    }
    
    public void moveALittleBit() {
        Point nextLocation = null;

        
        switch (state) {
            case IDLE:
                //throw new IllegalStateException("nothing to do");
                
                break;
                
            case NAVIGATING_TO_ITEM:
                nextLocation = generateNextValidStepTowardsHeading();
                move(nextLocation);
                if (currentHeadingReached()) {
                    transitionToNewState(PintoState.NAVIGATING_TO_PERSON);
                    pickupItem();
                }
                break;
                
            case NAVIGATING_TO_PERSON:
                nextLocation = generateNextValidStepTowardsHeading();
                move(nextLocation);
                if (currentLocationAdjacentToHeadingLocation()) {
                    transitionToNewState(PintoState.NAVIGATING_TO_DOCKING_STATION);
                    dropItem();
                }
                break;
                
            case NAVIGATING_TO_DOCKING_STATION:
                nextLocation = generateNextValidStepTowardsHeading();
                _path.add(nextLocation);
                move(nextLocation);
                // docking station blocks movement
                if (currentHeadingReached() || currentLocationAdjacentToHeadingLocation()) {
                    transitionToNewState(PintoState.IDLE);
                    command.onComplete(_path);
                    _pintoManager.setTaskStatus(command.getItemName(), PintoManager.TaskStatus.COMPLETE);
                    setStatus(PINTO_STATUS.IDLE);
                }
                break;
        }
        //dump();
    }
    
    private void transitionToNewState(PintoState newState) {
        state = newState;
        
        
        switch (newState) {
            case IDLE:
                currentHeading = null;
                currentPath = null;
                break;
                
            case NAVIGATING_TO_ITEM:
                currentHeading = itemLocation;
                currentPath = getPathTowardsHeading();
                break;
                
            case NAVIGATING_TO_PERSON:
                currentHeading = personLocation;
                currentPath = getPathTowardsHeading();
                break;
                
            case NAVIGATING_TO_DOCKING_STATION:
                currentHeading = dockingStationLocation;
                currentPath = getPathTowardsHeading();
                break;
                
        }
        

    }
    
    private List<Point> getPathTowardsHeading() {
        // we always remove the first location in the path because it 
        // represents the starting location of the path, and we dont
        // want to move to our own location
        List<Point> path = pathFinder.getPath(currentLocation, currentHeading);
        path.remove(0);
        return path;
    }
    
    
    
    
    private void dump() {
        System.out.printf(
            "state %s, (%d,%d)\n",
                state, currentLocation.x, currentLocation.y
        );
        System.out.println(enviornmentMap.asciiPrint());
    }
    
    
    private boolean currentHeadingReached() {
        return currentPath.size() == 0;
    }
    
    // top, down, left, right only. no diagonal
    private static boolean isAdjacentTo(Point loc1, Point loc2) {
        int horizontalDistance = Math.abs(loc1.x - loc2.x);
        int verticalDistance = Math.abs(loc1.y - loc2.y);
        return 1 == horizontalDistance + verticalDistance;
    }
    
    private boolean currentLocationAdjacentToHeadingLocation() {
        return isAdjacentTo(currentLocation, currentHeading);
    }
    
    private Point generateNextValidStepTowardsHeading___() {
        Point loc = currentPath.remove(0);
        if (!enviornmentMap.isLocationWalkable(loc.x, loc.y)) {
            // randomly decide if we should calc new path or just sit still. 
            //if we dont do this, we get deadlock too often in narrow corridors 
            // because both keep calculating the same alternate path and collide again
            if (Math.random() > 0.5) {
                currentPath = pathFinder.getPath(currentLocation, currentHeading);
                
                loc = currentPath.remove(0);
            } else {
                loc = currentLocation;
            }

        }

        return loc;
    }
    
    private Point generateNextValidStepTowardsHeading() {
        
        if (!currentPath.isEmpty() && enviornmentMap.isLocationWalkable(currentPath.get(0))) {
            return currentPath.remove(0);
        }


        // randomly decide if we should calc new path or just sit still. 
        //if we dont do this, we get deadlock too often in narrow corridors 
        // because both keep calculating the same alternate path and collide again
        if (Math.random() > 0.5) {
            currentPath = getPathTowardsHeading();
            if (!currentPath.isEmpty() && enviornmentMap.isLocationWalkable(currentPath.get(0))) {
                return currentPath.remove(0);
            }
        }

        return currentLocation;

    }
    
    
    
    

    
    public boolean isIdle() {
        return state == PintoState.IDLE;
    }
    
    

    public void cancelItem(Command cmd) {
        command.onCancel(_myCurrentLocation);
        DijkstraPathFinder itemToDsPathFinder = new DijkstraPathFinder(enviornmentMap);
        Point ds = enviornmentMap.getPintoDockingStationLocation();
        Point item = _myCurrentLocation;
        try {
            itemToDsPathFinder.computePathsFrom(item);
            List<Point> path = itemToDsPathFinder.getShortestPathTo(ds);

            _path.addAll(path);
            command.onComplete(_path);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
    }
    
    public boolean isCarryingAnItem() {
        return itemBeingCarried != null;
    }
    
    private void pickupItem() {
        itemBeingCarried = enviornmentMap.getItemByName(command.getItemName());
    }
    
    private void dropItem() {
        itemBeingCarried = null;
    }
    
    
    public void abortItemRetrieval() {
        if (isCarryingAnItem()) {
            dropItem();
        }
        
        if (state != PintoState.IDLE && state != PintoState.NAVIGATING_TO_DOCKING_STATION) {
            transitionToNewState(PintoState.NAVIGATING_TO_DOCKING_STATION);
        }
    }
    
    
    public void move(Point loc) {
        super.move(loc);
        //the reason you dont see the item animated while its being 
        // carried is because the item is painted first, then the pinto carrying it
        // so the pinto is painted over it, hiding it
        if (itemBeingCarried != null) {
            itemBeingCarried.move(loc);
        }
    }
    
    
    
}
