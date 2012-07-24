package pintosim;


import java.awt.*;

/**
 * All the shapes in the Gui version of Pintosim
 * @author PlzSendtheCodes team
 */
public interface GuiShape {

    /**
     * Draws the map and updates items when necessary
     * @param g2 the graphics object to draw
     */
    void draw(Graphics2D g2);


    // wip... add more methods to support GuiEnvironmentMap
}
