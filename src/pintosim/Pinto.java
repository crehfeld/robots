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

    private PintoManager pintoManager;
    private List<Point> path;
    private Command command;
    private Point itemLocation;
    private Point personLocation;
    private Point dockingStationLocation;
    private Point currentHeading;
    private List<Point> currentPath;
    private PathFinder pathFinder;
    private Item itemBeingCarried;
    private EnviornmentMap EnvironmentMap;
    
    
    
    private enum PintoState {
        NAVIGATING_TO_ITEM
      , NAVIGATING_TO_PERSON
      , NAVIGATING_TO_DOCKING_STATION
      , IDLE
    }
    
    private PintoState state = PintoState.IDLE;

    public Pinto(Point initialLocation, EnviornmentMap map, PintoManager pintoManager, PathFinder pathFinder) {
        super(initialLocation);
        EnvironmentMap = map;
        this.pintoManager = pintoManager;
        this.pathFinder = pathFinder;
    }


    public void getItem(Command cmd) {
        command = cmd;
        Point ds = EnvironmentMap.getPintoDockingStationLocation();
        Point elderly = EnvironmentMap.getPersonLocation();
        Point item = EnvironmentMap.getItemLocation(command.getItemName());

        setDestinations(item, elderly, ds);
        path = new ArrayList<Point>();
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
            
            case NAVIGATING_TO_ITEM:
                nextLocation = generateNextValidStepTowardsHeading();
                path.add(nextLocation);
                move(nextLocation);
                if (currentHeadingReached()) {
                    transitionToNewState(PintoState.NAVIGATING_TO_PERSON);
                    pickupItem();
                }
                break;
                
            case NAVIGATING_TO_PERSON:
                nextLocation = generateNextValidStepTowardsHeading();
                path.add(nextLocation);
                move(nextLocation);
                // the person blocks movement onto the tile
                if (currentLocationAdjacentToHeadingLocation()) {
                    transitionToNewState(PintoState.NAVIGATING_TO_DOCKING_STATION);
                    command.onComplete(path);
                    dropItem();
                }
                break;
                
            case NAVIGATING_TO_DOCKING_STATION:
                nextLocation = generateNextValidStepTowardsHeading();
                path.add(nextLocation);
                move(nextLocation);
                // the docking station blocks movement onto the tile
                if (currentHeadingReached() || currentLocationAdjacentToHeadingLocation()) {
                    transitionToNewState(PintoState.IDLE);
                }
                break;
        }
    }
    
    private void transitionToNewState(PintoState newState) {
        state = newState;
        
        switch (newState) {
            case IDLE:
                currentHeading = null;
                currentPath = null;
                pintoManager.setTaskStatus(command.getItemName(), PintoManager.TaskStatus.COMPLETE);
                break;
                
            case NAVIGATING_TO_ITEM:
                currentHeading = itemLocation;
                currentPath = getPathTowardsHeading();
                break;
                
            case NAVIGATING_TO_PERSON:
                currentHeading = personLocation;
                currentPath = getPathTowardsHeading();
                pintoManager.setTaskStatus(command.getItemName(), PintoManager.TaskStatus.ITEM_BEING_CARRIED);
                break;
                
            case NAVIGATING_TO_DOCKING_STATION:
                currentHeading = dockingStationLocation;
                currentPath = getPathTowardsHeading();
                pintoManager.setTaskStatus(command.getItemName(), PintoManager.TaskStatus.STARTED);
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

    private Point generateNextValidStepTowardsHeading() {
        if (!currentPath.isEmpty() && EnvironmentMap.isLocationWalkable(currentPath.get(0))) {
            return currentPath.remove(0);
        }
        
        //we only reach here if we cant move
        if (!shouldSitStill()) {
            currentPath = getPathTowardsHeading();
            if (!currentPath.isEmpty() && EnvironmentMap.isLocationWalkable(currentPath.get(0))) {
                return currentPath.remove(0);
            }
        }

        // stay put, for now
        return currentLocation;
    }
    
    public boolean isIdle() {
        return state == PintoState.IDLE;
    }
    
    
    // randomly decide if we should calc new path or just sit still. 
    //if we dont do this, we get deadlock too often in narrow corridors 
    // because both keep calculating the same alternate path and collide again
    private boolean shouldSitStill() {
        return Math.random() > 0.8;
    }
    
    public boolean isCarryingAnItem() {
        return itemBeingCarried != null;
    }
    
    private void pickupItem() {
        itemBeingCarried = EnvironmentMap.getItemByName(command.getItemName());
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
        
        command.onCancel(currentLocation);
    }
    
    public void move(Point loc) {
        super.move(loc);

        if (itemBeingCarried != null) {
            itemBeingCarried.move(loc);
        }
    }
    

}
