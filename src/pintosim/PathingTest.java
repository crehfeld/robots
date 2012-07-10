
package pintosim;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * this prints an interpretation of an image map
 * e means elderly
 * x means obstruction
 * d is docking station
 * p is pinto - but we dont currently(and may never) use their location, we use the docking station 
 * O is traveled path 
 * 
 * 
 * notices the O trail goes from the 'd' to the 'e' locations
 * 
 * 
 * coordinate system is 0,0 is top left
 */
public class PathingTest {
    public static void main (String[] args) {
        MapFeatures mapFeatures;

        try {
            mapFeatures = new MapFeatures(new File("maps/10x10-2pintos-1cup.png"));
        } catch (Exception e) {
            e.printStackTrace();//could be IO, or bad map file format
            return;
        }
        
        
        System.out.print(mapFeatures);
        
        Point from = mapFeatures.getPintoDockingStationLocation();
        Point to   = mapFeatures.getPersonLocation();
        
        System.out.printf("from %s\n", from);
        System.out.printf("to   %s\n", to);
        
        
        
        EnviornmentMap map = new EnviornmentMap(mapFeatures);
        PathFinder pf = new PathFinder(map);
        pf.computePathsFrom(from);
        List<Point> path = pf.getShortestPathTo(to);
        
        
        
        System.out.print(pf.printPath(path));


    }
}
