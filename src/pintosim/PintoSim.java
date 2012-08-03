package pintosim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PintoSim {
    private static final String IMAGE_MAP_FILENAME = "maps/30x30-lots-of-items2.png";
    private static final String GRAPHICS_PACKAGE_DIR = "graphics/standard/";
    private static final String OUTPUT_RENDERING_FILENAME = "dynamic-output.txt";
    private static final int    OUTPUT_RENDERING_FREQUENCY_MS = 100;
    
    private static final int    CANVAS_RENDERING_FREQUENCY_MS = 10;
    private static final int    GAME_UPDATE_FREQUENCY_MS = 80;
    

    public static void main(String[] args) {
        showCommandLineInterface();
    }

    public static void showCommandLineInterface() {

        UserInterface ui = buildWithAnimPanel();

        Scanner scanner = new Scanner(System.in);
        String input = null;
        System.out.print("Welcome to the 'My Friendly Pintos' assistive Robot system!\n");
        System.out.print("Enter a command, type help, or type quit: ");
        input = scanner.nextLine();


        while (0 != input.trim().indexOf("quit")) {
            ui.addInput(input);
            System.out.print("\n\nEnter a command, type help, or type quit: ");
            input = scanner.nextLine();
        }
    }
    

    /*
     * Initializes the system, wiring up the object dependencies.
     */
    public static UserInterface build() {
        MapFeatures mapFeatures;
        try {
            mapFeatures = new ImageMapAnalyzer(new File(IMAGE_MAP_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        EnvironmentMap map = new EnvironmentMap(mapFeatures);
        PintoManager pintoManager = new PintoManager(map);
        PathFinder pathFinder = new DijkstraPathFinder(map);
        
        //add pintos
        List<Pinto> pintos = new ArrayList<Pinto>();
        for (Point pintoLocation : mapFeatures.getPintoLocations()) {
            Pinto pinto = new Pinto(pintoLocation, map, pintoManager, pathFinder);
            map.trackObject(pinto);
            pintoManager.addPinto(pinto);
        }
        
        //add items, naming them a1, a2, a3....
        List<Item> items = new ArrayList<Item>();
        int itemSerial = 1;
        for (Point itemLocation : mapFeatures.getItemLocations()) {
            String itemName = "a" + itemSerial++;
            Item item = new Item(itemName, itemLocation.x, itemLocation.y);
            map.trackItem(item);
        }
        
        map.trackObject(new Person(mapFeatures.getPersonLocation()));
        
        UserInterface ui = new UserInterface(
            System.out
          , new CommandParser()
          , map
          , pintoManager
        );
        

        //poll the environmentmap fairly rapidly, writing the asccii printed map to an external file
        // so another process can read/poll it 
        renderMapToFile(map, OUTPUT_RENDERING_FILENAME);

        
        
        final PintoManager pm = pintoManager;
        Timer t = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                pm.work();
            }
        };
        t.scheduleAtFixedRate(task, 0, GAME_UPDATE_FREQUENCY_MS);
        

        return ui;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static UserInterface buildWithAnimPanel() {
        MapFeatures mapFeatures;
        try {
            mapFeatures = new ImageMapAnalyzer(new File(IMAGE_MAP_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        EnvironmentMap map = new EnvironmentMap(mapFeatures);
        PintoManager pintoManager = new PintoManager(map);
        PathFinder pathFinder = new DijkstraPathFinder(map);
        
        
        
        
        
        Image pintoGraphic = null;
        Image floorGraphic = null;
        Image wallGraphic = null;
        Image itemGraphic = null;
        Image dockingGraphic = null;
        Image personGraphic = null;
        try {
            pintoGraphic = ImageIO.read(new File(GRAPHICS_PACKAGE_DIR + "pinto.png"));
            floorGraphic = ImageIO.read(new File(GRAPHICS_PACKAGE_DIR + "floor.png"));
            wallGraphic = ImageIO.read(new File(GRAPHICS_PACKAGE_DIR + "wall.png"));
            itemGraphic = ImageIO.read(new File(GRAPHICS_PACKAGE_DIR + "item.png"));
            dockingGraphic = ImageIO.read(new File(GRAPHICS_PACKAGE_DIR + "docking.png"));
            personGraphic = ImageIO.read(new File(GRAPHICS_PACKAGE_DIR + "person.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        final AnimPanel animPanel = new AnimPanel(map.getWidth() *20, map.getHeight()*20, CANVAS_RENDERING_FREQUENCY_MS);
        animPanel.addPaintable(new FramesPerSecond());
        
        
        
        final int tileSize = 20;
        // create sprites for the walls and floor
        boolean[][] matrix = mapFeatures.getFreeSpaceMatrix();
        for (int i=0; i < matrix.length; i++) {
            for (int j=0; j<matrix[i].length; j++) {
                animPanel.addPaintable(new Sprite(
                    matrix[i][j] ? floorGraphic : wallGraphic
                  , new Point(i * tileSize, j * tileSize)
                  , 0
                ));
            }
        }
        
        
        
        
        
        
        final Tween tween = new EaseLinear();
        final Image graphic = itemGraphic;
        map.addItemTrackedListener(new ItemTrackedListener() {
            public void itemAdded(Item item) {
                Point initialLocation = new Point(item.getX() * 20, item.getY() * 20);
                MovingSprite sprite = new MovingSprite(graphic, initialLocation, 5);
                item.addLocationChangeListeners(new TransitionCreator(GAME_UPDATE_FREQUENCY_MS, tileSize, sprite, tween));
                animPanel.addPaintable(sprite);
            }
        });
        
        
        
        //add pintos
        for (Point pintoLocation : mapFeatures.getPintoLocations()) {
            Pinto pinto = new Pinto(pintoLocation, map, pintoManager, pathFinder);
            map.trackObject(pinto);
            pintoManager.addPinto(pinto);
            
            Point initialLocation = new Point(pintoLocation.x * 20, pintoLocation.y * 20);
            
            MovingSprite sprite = new MovingSprite(pintoGraphic, initialLocation, 10);
            pinto.addLocationChangeListeners(new TransitionCreator(GAME_UPDATE_FREQUENCY_MS, tileSize, sprite, tween));
            animPanel.addPaintable(sprite);

        }
        
        
        
        
        
        //add items, naming them a1, a2, a3....
        int itemSerial = 1;
        for (Point itemLocation : mapFeatures.getItemLocations()) {
            String itemName = "a" + itemSerial++;
            Item item = new Item(itemName, itemLocation.x, itemLocation.y);
            map.trackItem(item);
        }
        
        Point initialLocation = mapFeatures.getPintoDockingStationLocation();
        animPanel.addPaintable(new Sprite(dockingGraphic, new Point(initialLocation.x * 20, initialLocation.y * 20), 5));
        initialLocation = mapFeatures.getPersonLocation();
        animPanel.addPaintable(new Sprite(personGraphic, new Point(initialLocation.x * 20, initialLocation.y * 20), 5));

        
        
        
        map.trackObject(new Person(mapFeatures.getPersonLocation()));
        
        UserInterface ui = new UserInterface(
            System.out
          , new CommandParser()
          , map
          , pintoManager
        );
        


        JFrame frame = new JFrame("Anim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = (JPanel) frame.getContentPane();
        panel.add(animPanel);
		frame.pack();
        
        addMouseClickHandler(animPanel, ui, map);

		frame.setVisible(true);


        
        
        final PintoManager pm = pintoManager;
        Timer t = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                pm.work();
            }
        };
        t.scheduleAtFixedRate(task, 0, GAME_UPDATE_FREQUENCY_MS);
        

        return ui;
    }
    
    
    
    
    private static void renderMapToFile(final EnvironmentMap map, String filename) {
        RandomAccessFile tmp = null;
        try {
            tmp = new RandomAccessFile(filename, "rw");
            tmp.setLength(0);//truncate
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final RandomAccessFile file = tmp;
        
        
        Timer t = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    file.seek(0);
                    file.writeChars(map.asciiPrint() );
                } catch (IOException e) {
                    e.printStackTrace();
                    cancel();
                }
            }
        };
        t.scheduleAtFixedRate(task, 0, OUTPUT_RENDERING_FREQUENCY_MS);
    }
    
    

    
    
    
    
    
    private static void addMouseClickHandler(AnimPanel panel, final UserInterface ui, final EnvironmentMap map) {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                // integer division to get back down to tile coords
                int x = evt.getX() / 20;
                int y = evt.getY() / 20;
                for (Item item : map.getItemsAt(x, y)) {
                    String command = "system, get me " + item.getName();
                    ui.addInput(command);
                    break;
                }
            }
        });
    }
}
