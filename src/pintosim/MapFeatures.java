package pintosim;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * This class analyzes an image, inspecting each pixel to extract map features.
 * Each pixel represents a "tile", so the image dimensions measured in pixels
 * dictate the map size in tiles.
 *
 * <p> Colors and meaning:
 *
 * <br> White pixels indicate that no obstructions exist at this location. RGB
 * values: (255,255,255)
 *
 * <br> Black pixels indicate that obstructions DO exist at this location(walls,
 * a couch etc...). RGB values: (0,0,0)<br>
 *
 * Blue pixels indicates the pinto docking station. RGB values: (0,0,255)<br>
 *
 * Red pixels indicate the person. RGB values: (255,0,0)<br>
 *
 * Yellow pixels indicates a pinto. RGB values: (255,255,0)<br>
 *
 * <p> <b>The colors in the image must match the above rgb values exactly, and
 * must be fully opaque(no alpha transparency).</b>
 */
public class MapFeatures {

    private int width, height;
    private Point pintoDockingStation;
    private BufferedImage img;
    private List<Point> pintos = new ArrayList<Point>();
    private Point person;
    private Point dockingStation;
    private boolean[][] freeSpaces;
    public static final Color PINTO_DOCKING_STATION_COLOR = Color.BLUE;
    public static final Color PINTO_COLOR = Color.YELLOW;
    public static final Color PERSON_COLOR = Color.RED;
    public static final Color OBSTRUCTION_COLOR = Color.BLACK;
    public static final Color NO_OBSTRUCTION_COLOR = Color.WHITE;

    /**
     * Constructs the map using a {@link File} which is an image file
     *
     * @param mapFile An image file File whose dimensions and pixel colors
     * describe the house features
     */
    public MapFeatures(File mapFile) throws IOException {
        this(ImageIO.read(mapFile));
    }

    /**
     * Constructs the map using a {@link java.awt.BufferedImage}
     *
     * @param img A BufferedImage whose dimensions and pixel colors describe the
     * house features
     */
    public MapFeatures(BufferedImage img) {
        this.img = img;
        analyzeImage();
    }

    /**
     * This method analyzes an image, inspecting the color of each pixel to
     * extract map features.
     *
     * @param img A BufferedImage whose dimensions and pixel colors describe the
     * house features
     * @throws RuntimeException if unknown colors, or more than 1 person or more than 1 docking station is detected
     */
    private void analyzeImage() {
        width = img.getWidth();
        height = img.getHeight();
        freeSpaces = new boolean[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(img.getRGB(x, y));
                Point point = new Point(x, y);
                
                if (PINTO_DOCKING_STATION_COLOR.equals(pixelColor)) {
                    if (dockingStation != null) {
                        throw new RuntimeException(String.format(
                            "duplicate docking station at x=%d,y=%d, only 1 is supported"
                          , x
                          , y
                        ));
                    }
                    dockingStation = point;
                } else if (PINTO_COLOR.equals(pixelColor)) {
                    pintos.add(point);
                } else if (PERSON_COLOR.equals(pixelColor)) {
                    if (person != null) {
                        throw new RuntimeException(String.format(
                            "duplicate person at x=%d,y=%d, only 1 is supported"
                          , x
                          , y
                        ));
                    }
                    person = point;
                } else if (OBSTRUCTION_COLOR.equals(pixelColor)) {
                    freeSpaces[x][y] = false;
                } else if (NO_OBSTRUCTION_COLOR.equals(pixelColor)) {
                    freeSpaces[x][y] = true;
                } else {
                        throw new RuntimeException(String.format(
                            "unknown color  x=%d,y=%d, rgb=%d"
                          , x
                          , y
                          , img.getRGB(x, y)
                        ));
                }
            }
        }
    }

    public List<Point> getPintoLocations() {
        return pintos;
    }

    public Point getPersonLocation() {
        return person;
    }

    public Point getPintoDockingStationLocation() {
        return dockingStation;
    }

    /**
     * Provides a 2d array, in matrix[x][y] format where true values represent a
     * location without obstructions, and false represents an obstruction. Only
     * explicit obstructions are included in this - existance of docking
     * station, pintos, people etc... aren't reflected as obstructions in this
     * matrix.
     *
     * @return a matrix representing non obstructed locations
     */
    public boolean[][] getFreeSpaceMatrix() {
        return freeSpaces;
    }
    
    /**
     * 
     *
     * @return a matrix representing non obstructed locations
     */
    public boolean isLocationFreeSpace(int x, int y) {
        return freeSpaces[x][y];
    }

    /**
     *
     * @return width of map
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return height of map
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a string representation of the map. <p> 'd' means docking
     * station.<br> 'p' means pinto<br> 'e' means person/elderly<br> 'x' means
     * obstruction<br> '.' means no obstruction<br>
     *
     *
     * @return A string representation
     */
    public String toString() {
        String buf = String.format("Map info: width=%d height=%d\n", width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(img.getRGB(x, y));
                Point point = new Point(x, y);
                if (PINTO_DOCKING_STATION_COLOR.equals(pixelColor)) {
                    buf += "d";
                } else if (PINTO_COLOR.equals(pixelColor)) {
                    buf += "p";
                } else if (PERSON_COLOR.equals(pixelColor)) {
                    buf += "e";
                } else if (OBSTRUCTION_COLOR.equals(pixelColor)) {
                    buf += "x";
                } else if (NO_OBSTRUCTION_COLOR.equals(pixelColor)) {
                    buf += ".";
                }
            }
            buf += "\n";
        }

        return buf;
    }
}
