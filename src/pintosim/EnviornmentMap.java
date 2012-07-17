package pintosim;


import java.awt.Point;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * This class provides location services. It knows the physical dimensions and
 * features of the house(as specified by MapFeatures). It also knows the current
 * locations of all objects that it tracks.
 *
 * @author Chris
 */
public class EnviornmentMap {

    private class MovableObjectList extends ArrayList<MovableObject> {}
    // tracked objects indexed by their tile locations, eg list = trackedObjects[x][y]
    private MovableObjectList[][] tileOccupiers;
    //width and height of the map definition
    private int width = 0;
    private int height = 0;
    private MapFeatures mapFeatures;
    private List<PathFinder> pathFinders = new ArrayList<PathFinder>();
    /**
     * All tracked objects. We store this separate location because when
     * something moves, it updates its own internal location and then notifies
     * observers that it has changed state. So by the time we receive the
     * update, the old location is lost. I think we may need both locations.
     */
    private Map<MovableObject, Point> trackedObjects = new HashMap<MovableObject, Point>();
    // tracked items indexed by their name
    private Map<String, Item> trackedItems = new HashMap<String, Item>();

    public EnviornmentMap(MapFeatures mapFeatures) {
        this.mapFeatures = mapFeatures;
        width = mapFeatures.getWidth();
        height = mapFeatures.getHeight();
        tileOccupiers = new MovableObjectList[width][height];
        
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                tileOccupiers[x][y] = new MovableObjectList();
            }
        }
    }

    /**
     * Provides a List of all the MovableObjects currently located on a tile.
     * The List will be an empty List for the case of 0 objects. The List is not
     * a live list, so after a movement has occurred, the list may no longer
     * properly represent the objects on the tile. The MovableObjects contained
     * in the list are not clones. <br> <br> Precondition: The coordinates are
     * within the boundaries as determined by {@link #withinBoundaries(int, int) withinBoundaries()}
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     *
     * @return A list of MovableObjects
     */
    public List<MovableObject> getObjectsAt(int x, int y) {
        return new ArrayList<MovableObject>(tileOccupiers[x][y]);
    }

    /**
     * A walkable location is a location that isn't a wall, couch, or other such
     * conceptual house feature. It also may not currently contain a blocking
     * object, like a pinto.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return true if the location is walkable
     */
    public boolean isLocationWalkable(int x, int y) {
        return tileOccupiers[x][y].size() == 0 && mapFeatures.isLocationFreeSpace(x, y);
    }

    /**
     * Determines if the coordinates are within the boundaries of the loaded
     * map.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return true if the location is within bounds
     */
    public boolean withinBoundaries(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Determines if the coordinates of the MovableObject are within the
     * boundaries of the loaded map.
     *
     * @param obj A MovableObject
     * @return true if the location is within bounds
     */
    public boolean withinBoundaries(MovableObject obj) {
        return withinBoundaries(obj.getX(), obj.getY());
    }

    public Point getPintoDockingStationLocation() {
        return this.mapFeatures.getPintoDockingStationLocation();
    }

    public Point getPersonLocation() {
        return this.mapFeatures.getPersonLocation();
    }

    public Point getItemLocation(String itemName) {
        Item item = getItemByName(itemName);
        return new Point(item.getX(), item.getY());
    }
    
    public void print() {
        System.out.print(this.mapFeatures);
    }
    public void trackObject(MovableObject obj) {
        if (!trackedObjects.containsKey(obj)) {
            trackedObjects.put(obj, new Point(obj.getX(), obj.getY()));
        }
        
    }

    public boolean trackItem(Item item) {
        String name = item.getName().toLowerCase();
        if (!trackedItems.containsKey(name)) {
            trackedItems.put(name, item);
            return true;
        }
        return false;
    }
    
    public void stopTrackingItem(Item item) {
        trackedItems.remove(item.getName().toLowerCase());
    }
    

    public Item getItemByName(String name) {
        return trackedItems.get(name.toLowerCase());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // we will then broadcast location changed update messages to it
    public void addPathFinderListener(PathFinder pathFinder) {
        pathFinders.add(pathFinder);
    }



    /**
     * The freeSpace matrix from the composed MapFeatures object only describes
     * where the house has static obstacles/walls. However, the matrix returned
     * by this method also takes into account the current positions of dynamic
     * obstacles, like pintos.
     *
     * @return a matrix where true means the position is walkable/unobstructed
     */
    public boolean[][] getWalkabilityMatrix() {
        boolean[][] freeSpaceMatrix = mapFeatures.getFreeSpaceMatrix();
        boolean[][] walkabilityMatrix = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                walkabilityMatrix[i][j] = freeSpaceMatrix[i][j] && isLocationWalkable(i, j);
            }
        }

        return walkabilityMatrix;
    }

    
    
    // all movable objects should call this method when they move
    // it allows this map to keep up to date on the locations of things.
    // it also notifies all the pathfinders of the change too
    public void updateLocation(MovableObject obj) {
        int newX = obj.getX();
        int newY = obj.getY();

        if (tileOccupiers[newX][newY] == null) {
            throw new RuntimeException("shouldnt be able to move there.");
        }

        Point oldLocation = trackedObjects.get(obj);

        if (oldLocation == null) {
            throw new RuntimeException("trackObject() was never called on this MovableObject ");
        }

        int oldX = oldLocation.x;
        int oldY = oldLocation.y;

        tileOccupiers[oldX][oldY].remove(obj);
        tileOccupiers[newX][newY].add(obj);

        oldLocation.x = newX;
        oldLocation.y = newY;

        boolean oldLocationUnoccupied = tileOccupiers[oldX][oldY].size() == 0;

        for (PathFinder p : pathFinders) {
            p.spaceOccupied(newX, newY);
            if (oldLocationUnoccupied) {
                p.spaceUnoccupied(oldX, oldY);
            }
        }


    }
    
    
    
    /**
     * Creates an ascii art like string describing the map
     * 
     * @return An ascii art like string describing the map 
     */
    public String asciiPrint() {
        return String.format(
            "Map info: width=%d height=%d\n%s"
          , width
          , height
          , asciiPrint(new ArrayList<Point>())
        );
    }
    
    
    /**
     * Creates an ascii art like string describing the map. The points in the
     * path will be overlayed on top of the map drawing in an effort to make the
     * path clear
     *
     *
     * @param path An ordered List of Points describing a path taken through the
     * map
     * @return An ascii art like string describing the map
     */
    public String asciiPrint(List<Point> path) {
        String buf = "";
        Point dockingLoc = getPintoDockingStationLocation();
        Point personLoc = getPersonLocation();
        List<Point> itemLocations = new ArrayList<Point>();
        for (Item item : trackedItems.values()) {
            itemLocations.add(new Point(item.getX(), item.getY()));
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point gridLocation = new Point(x, y);
                if (path.contains(gridLocation)) {
                    buf += "O";
                } else if (dockingLoc.x == x && dockingLoc.y == y) {
                    buf += "D";
                } else if (personLoc.x == x && personLoc.y == y) {
                    buf += "E";
                } else if (itemLocations.contains(gridLocation)) {
                    buf += "I";
                } else {
                    buf += isLocationWalkable(x, y) ? "." : "X";
                }
            }
            buf += "\n";
        }

        return buf;
    }
    
    
    

    
}
