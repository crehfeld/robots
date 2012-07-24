package pintosim;
import java.awt.*;
import java.awt.geom.*;

/**
 * Draws the map for the GUI version of PintoSim. Takes
 * care of items addition/removal and updates the map.
 */
public class GuiEnvironmentMap implements GuiShape {

    private int x;
    private int y;

    /**
     * Constructs a GuiEnvironmentMap object
     */
    public GuiEnvironmentMap() {
        // wip...
    }

    /**
     * Draws the map and updates items when necessary
     */
    public void draw() {

        // Draw the docking station
        Rectangle2D.Double dockingStation = new Rectangle2D.Double();
    }

    /**
     * Adds items to the map
     * @param itemName the name of the item
     */
    public void addItem(String itemName) {

    }

    /**
     * Draws the item
     */
    public void draw(Graphics2D g2) {

    }
}