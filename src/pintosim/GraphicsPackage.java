
package pintosim;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public interface GraphicsPackage {
    public Map<String, Image> getNamedExtraImages();
    public Image getDockingImage();
    public Image getFloorImage();
    public Image getItemImage();
    public Image getPersonImage();
    public Image getPintoImage();
    public Image getWallImage();
    public BufferedImage getMapImage();
    public int getTileSize();
    public Point translateCoords(Point tileCoordinates);
}
