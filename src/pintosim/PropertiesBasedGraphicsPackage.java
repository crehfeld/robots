
package pintosim;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;


public class PropertiesBasedGraphicsPackage implements GraphicsPackage {
    private Image item;
    private Image docking;
    private Image person;
    private Image pinto;
    private Image wall;
    private Image floor;
    private BufferedImage map;
    private int tileSize;
    private Map<String, Image> namedExtraImages = new HashMap<String, Image>();
    private Properties props;
    
    public PropertiesBasedGraphicsPackage(Properties props) throws Exception {
        this.props = props;
        tileSize = Integer.parseInt(props.getProperty("TileSizePixels"));
        
        File dir = new File(props.getProperty("GraphicsPackageDir"));
        File extraImagesDir = new File(dir.getPath() + "/items/");
        
        pinto   = load(dir, props.getProperty("PintoGraphic"));
        floor   = load(dir, props.getProperty("FloorGraphic"));
        wall    = load(dir, props.getProperty("WallGraphic"));
        item    = load(dir, props.getProperty("DefaultItemGraphic"));
        docking = load(dir, props.getProperty("DockingGraphic"));
        person  = load(dir, props.getProperty("PersonGraphic"));
        map     = ImageIO.read(new File(props.getProperty("ImageMapFilename")));
        
        File[] files = extraImagesDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".png");
            }
        });

        for (File file : files) {
            namedExtraImages.put(file.getName(), load(extraImagesDir, file.getName()));
        }
    }
    
    private Image load(File dir, String name) throws IOException {
        return ImageIO.read(new File(dir.getPath() + "/" + name));
    }
    
    private Image load(File file) throws IOException {
        return ImageIO.read(file);
    }
    
    public Map<String, Image> getNamedExtraImages() {
        return namedExtraImages;
    }
    
    public Image getDockingImage() {
        return docking;
    }

    public Image getFloorImage() {
        return floor;
    }

    public Image getItemImage() {
        return item;
    }

    public Image getPersonImage() {
        return person;
    }

    public Image getPintoImage() {
        return pinto;
    }

    public Image getWallImage() {
        return wall;
    }
    
    public BufferedImage getMapImage() {
        return map;
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public Point translateCoords(Point tileCoordinates) {
        return new Point(
            tileCoordinates.x * tileSize
          , tileCoordinates.y * tileSize
        );
    }
}
