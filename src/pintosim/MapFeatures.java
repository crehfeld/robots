
package pintosim;

import java.awt.Point;
import java.util.List;


/**
 * This is used to describe some basic features of a house and its enviornment.
 * 
 * An implementing class will likely dynamically populate the data from an
 * external source such as a file in a particular format.
 */

public interface MapFeatures {
    
    
    /**
     * Provides a list of Points which represent initial locations of Pinto robots.
     * 
     * @return A list of Point 
     */
    public List<Point> getPintoLocations();
    
    
    /**
     * Provides a Point which represent the initial location of a Person.
     * 
     * @return A Point 
     */
    public Point getPersonLocation();
    
    
    /**
     * Provides a Point which represent the initial location of a Pinto docking station.
     * 
     * @return A Point 
     */
    public Point getPintoDockingStationLocation();
    
    
    /**
     * Provides a list of Points which represent initial locations of Items
     * 
     * @return A list of Point 
     */
    public List<Point> getItemLocations();
    
    
    /**
     * Provides a 2d array, in matrix[x][y] format where true values represent a
     * location without obstructions, and false represents an obstruction. Only
     * explicit obstructions like walls are included in this - existance of docking
     * station, pintos, people etc... aren't reflected as obstructions in this
     * matrix.
     *
     * @return A 2d matrix of boolean
     */
    public boolean[][] getFreeSpaceMatrix();
    
    
    /**
     * Free space is a space that isn't a wall or occupied by some other 
     * unmodifiable part of the house structure.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @precondition withinBoundaries(x, y) == true
     * @return true if the location is unobstructed by walls
     */
    public boolean isLocationFreeSpace(int x, int y);
    
    
    /**
     * Provides the width
     * 
     * @return width of map
     */
    public int getWidth();

    
    /**
     * Provides the height
     * 
     * @return height of map
     */
    public int getHeight();
    
    
    /**
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @return true when the location is within the physical boundaries of the house
     */
    public boolean withinBoundaries(int x, int y);
}
