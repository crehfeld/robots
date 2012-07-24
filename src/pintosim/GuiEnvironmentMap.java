package pintosim;
import java.awt.*;
import java.awt.geom.*;

/**
 * Draws the map for the GUI version of PintoSim. Takes
 * care of items addition/removal and updates the map.
 */
public class GuiEnvironmentMap implements GuiShape {

    // The location of the items on the map
    private int x;
    private int y;

    /**
     * Constructs a GuiEnvironmentMap object
     * @see GUI
     */
    public GuiEnvironmentMap() {
        // work in progress
        // have to draw the whole map here that is shown to the user when
        // they run the program for the first time.
    }

    /**
     * Adds items to the map.
     * @param itemName the name of the item
     */
    public void addItem(String itemName) {

    }

    /**
     * Draws the map and updates items when necessary.
     * @see GuiShape
     */
    public void draw(Graphics2D g2) {

        // Draw the docking station
        Rectangle2D.Double dockingStation = new Rectangle2D.Double();
    }
}