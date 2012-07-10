/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pintosim;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author apple
 */
public class MapFeaturesTest {
    
    public BufferedImage img;
    public MapFeatures mapFeatures;
    
    public MapFeaturesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        //make a dummy map
        img = new BufferedImage(10, 11, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MapFeatures.PINTO_DOCKING_STATION_COLOR.getRGB());
        
        img.setRGB(0, 1, MapFeatures.PINTO_COLOR.getRGB());
        img.setRGB(1, 1, MapFeatures.PINTO_COLOR.getRGB());
        
        img.setRGB(0, 2, MapFeatures.PERSON_COLOR.getRGB());
        img.setRGB(0, 3, MapFeatures.OBSTRUCTION_COLOR.getRGB());
        img.setRGB(0, 4, MapFeatures.NO_OBSTRUCTION_COLOR.getRGB());
        
        mapFeatures = new MapFeatures(img);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPintoLocations method, of class MapFeatures.
     */
    @Test
    public void testGetPintoLocations() {

        List<Point> locations = mapFeatures.getPintoLocations();
        assertEquals(new Point(0, 1), locations.get(0));
        assertEquals(new Point(1, 1), locations.get(1));
        assertEquals(2, locations.size());
        
    }

    /**
     * Test of getPersonLocation method, of class MapFeatures.
     */
    @Test
    public void testGetPersonLocation() {
        System.out.println("getPersonLocation");
        Point expResult = new Point(0, 2);
        Point result = mapFeatures.getPersonLocation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPintoDockingStationLocation method, of class MapFeatures.
     */
    @Test
    public void testGetPintoDockingStationLocation() {
        System.out.println("getPintoDockingStationLocation");

        Point expResult = new Point(0, 0);
        Point result = mapFeatures.getPintoDockingStationLocation();
        assertEquals(expResult, result);

    }

    /**
     * Test of getFreeSpaceMatrix method, of class MapFeatures.
     */
    @Test
    public void testGetFreeSpaceMatrix() {
        System.out.println("getFreeSpaceMatrix");

        // there should only be one false entry
        boolean[][] result = mapFeatures.getFreeSpaceMatrix();
        assertEquals(false, result[3][0]);
        
    }

    /**
     * Test of getWidth method, of class MapFeatures.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        assertEquals(10, mapFeatures.getWidth());
    }

    /**
     * Test of getHeight method, of class MapFeatures.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        assertEquals(11, mapFeatures.getHeight());
    }


}
