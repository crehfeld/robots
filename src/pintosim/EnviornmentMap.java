package pintosim;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides location services. It knows the physical dimensions and
 * features of the house(as specified by MapFeatures). It also knows the current
 * locations of all objects that it tracks.
 *
 * @author Chris
 */
public class EnviornmentMap implements LocationChangeListener {

    private class MovableObjectList extends ArrayList<MovableObject> {}
    
    // tracked objects indexed by their tile locations, eg list = trackedObjects[x][y]
    // this implies they block the space from other things moving onto the tile, although
    // this class doesnt enforce it, 
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
            for (int y = 0; y < height; y++) {
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
    public synchronized List<MovableObject> getObjectsAt(int x, int y) {
        return new ArrayList<MovableObject>(tileOccupiers[x][y]);
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
    public synchronized List<Item> getItemsAt(int x, int y) {
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item item : trackedItems.values()) {
            if (x == item.getX() && y == item.getY()) {
                items.add(item);
            }
        }
        return items;
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
    public synchronized boolean isLocationWalkable(int x, int y) {
        return tileOccupiers[x][y].size() == 0
            && mapFeatures.isLocationFreeSpace(x, y)
            && !mapFeatures.getPintoDockingStationLocation().equals(new Point(x, y));
    }
    
    public synchronized boolean isLocationWalkable(Point loc) {
        return isLocationWalkable(loc.x, loc.y);
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
        return mapFeatures.getPintoDockingStationLocation();
    }

    public Point getPersonLocation() {
        return mapFeatures.getPersonLocation();
    }

    public Point getItemLocation(String itemName) {
        Item item = getItemByName(itemName);
        return new Point(item.getX(), item.getY());
    }

    public void trackObject(MovableObject obj) {
        if (!trackedObjects.containsKey(obj)) {
            trackedObjects.put(obj, new Point(obj.getX(), obj.getY()));
            tileOccupiers[obj.getX()][obj.getY()].add(obj);
            obj.addLocationChangeListeners(this);
        }
    }

    public void trackItem(Item item) {
        String name = item.getName().toLowerCase();
        if (!trackedItems.containsKey(name)) {
            trackedItems.put(name, item);
            item.addLocationChangeListeners(this);
        }
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
    public synchronized boolean[][] getWalkabilityMatrix() {
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
    public synchronized void updateLocation(MovableObject obj, Point oldLocation, Point newLocation) {
        int oldX = oldLocation.x;
        int oldY = oldLocation.y;
        int newX = newLocation.x;
        int newY = newLocation.y;

        // not everything occupies a tile. eg, items dont "occupy" because they dont prevent other things from moving there
        // check if this object was in the occupier list
        if (tileOccupiers[oldX][oldY].contains(obj)) {
            tileOccupiers[oldX][oldY].remove(obj);
            tileOccupiers[newX][newY].add(obj);
        }
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
            "Map info: width=%d height=%d\n\n%s"
          , width
          , height
          , asciiPrint(new ArrayList<Point>())
        );
    }
    
    
    /**
     * Creates an ascii art like string describing the map. The points in the
     * path will be overlayed on top of the map drawing in an effort to make the
     * path clear.
     * 
     * A Pinto overwrites an item in this ascii rendering, 
     * so this is why you dont see the item while a pinto is carrying it.
     * But, it really is moving with it!
     *
     *
     * @param path An ordered List of Points describing a path taken through the
     * map
     * @return An ascii art like string describing the map
     */
    public synchronized String asciiPrint(List<Point> path) {
        char[][] grid = new char[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = isLocationWalkable(x, y) ? '.' : 'X';
            }
        }
        
        Point dsLoc = mapFeatures.getPintoDockingStationLocation();
        grid[dsLoc.x][dsLoc.y] = 'D';
        
        for (Item item : trackedItems.values()) {
            grid[item.getX()][item.getY()] = 'I';
        }
        
        for (Map.Entry<MovableObject, Point> entry : trackedObjects.entrySet()) {
            Point pt = entry.getValue();
            MovableObject obj = entry.getKey();
            if (obj instanceof Pinto) {
                grid[pt.x][pt.y] = 'P';
            }
            if (obj instanceof Person) {
                grid[pt.x][pt.y] = 'E';
            }
        }
        
        for (Point pt : path) {
            grid[pt.x][pt.y] = 'O';
        }
        
        
        
        
        
        String buf = "";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buf += grid[x][y];
            }
            buf += "\n";
        }

        return buf;
    }
    
    

    
}
