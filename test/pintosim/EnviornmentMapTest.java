/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pintosim;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author apple
 */
@Ignore
public class EnviornmentMapTest {
    
    MapFeatures mf;
    
    public EnviornmentMapTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() throws Exception {
        mf = new MapFeatures(new File("maps/100x100-all-free.png"));
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getObjectsAt method, of class EnviornmentMap.
     */
    @Test
    public void testGetObjectsAt() {
        System.out.println("getObjectsAt");
        int x = 0;
        int y = 0;
        EnviornmentMap instance = null;
        List expResult = null;
        List result = instance.getObjectsAt(x, y);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isLocationWalkable method, of class EnviornmentMap.
     */
    @Test
    public void testIsLocationWalkable() {
        System.out.println("isLocationWalkable");
        int x = 0;
        int y = 0;
        EnviornmentMap instance = null;
        boolean expResult = false;
        boolean result = instance.isLocationWalkable(x, y);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of withinBoundaries method, of class EnviornmentMap.
     */
    @Test
    public void testWithinBoundaries_int_int() {
        System.out.println("withinBoundaries");
        int x = 0;
        int y = 0;
        EnviornmentMap instance = null;
        boolean expResult = false;
        boolean result = instance.withinBoundaries(x, y);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of withinBoundaries method, of class EnviornmentMap.
     */
    @Test
    public void testWithinBoundaries_MovableObject() {
        System.out.println("withinBoundaries");
        MovableObject obj = null;
        EnviornmentMap instance = null;
        boolean expResult = false;
        boolean result = instance.withinBoundaries(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of trackObject method, of class EnviornmentMap.
     */
    @Test
    public void testTrackObject() {
        System.out.println("trackObject");
        MovableObject obj = null;
        EnviornmentMap instance = null;
        instance.trackObject(obj);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of trackItem method, of class EnviornmentMap.
     */
    @Test
    public void testTrackItem() {
        System.out.println("trackItem");
        Item item = null;
        EnviornmentMap instance = null;
        boolean expResult = false;
        boolean result = instance.trackItem(item);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getItemByName method, of class EnviornmentMap.
     */
    @Test
    public void testGetItemByName() {
        System.out.println("getItemByName");
        String name = "";
        EnviornmentMap instance = null;
        Item expResult = null;
        Item result = instance.getItemByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWidth method, of class EnviornmentMap.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        EnviornmentMap instance = null;
        int expResult = 0;
        int result = instance.getWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeight method, of class EnviornmentMap.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        EnviornmentMap instance = null;
        int expResult = 0;
        int result = instance.getHeight();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPathFinderListener method, of class EnviornmentMap.
     */
    @Test
    public void testAddPathFinderListener() {
        System.out.println("addPathFinderListener");
        PathFinder pathFinder = null;
        EnviornmentMap instance = null;
        instance.addPathFinderListener(pathFinder);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWalkabilityMatrix method, of class EnviornmentMap.
     */
    @Test
    public void testGetWalkabilityMatrix() {
        System.out.println("getWalkabilityMatrix");
        EnviornmentMap instance = null;
        boolean[][] expResult = null;
        boolean[][] result = instance.getWalkabilityMatrix();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateLocation method, of class EnviornmentMap.
     */
    @Test
    public void testUpdateLocation() {
        System.out.println("updateLocation");
        MovableObject obj = null;
        EnviornmentMap instance = null;
        instance.updateLocation(obj);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
