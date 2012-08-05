package pintosim;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class PintoSim {
    private static final String CONFIG_FILENAME = "config.properties";
    private static final int    OUTPUT_RENDERING_FREQUENCY_MS = 100;
    //lol a unicode fail whale...
    private static final String failureMessage = "failure initializing\n\n"
                              + "▄██████████████▄▐█▄▄▄▄█▌\n"
                              + "██████▌▄▌▄▐▐▌███▌▀▀██▀▀\n"
                              + "████▄█▌▄▌▄▐▐▌▀███▄▄█▌\n"
                              + "▄▄▄▄▄██████████████▀\n";
    

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.load(new FileReader(new File(CONFIG_FILENAME)));
            showCommandLineInterface(props);
        } catch (Exception e) {
            System.out.println(failureMessage);
        }
    }

    public static void showCommandLineInterface(Properties props) throws Exception {
        UserInterface ui = build(props);
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
    public static UserInterface build(Properties props) throws Exception {
        final GraphicsPackage graphicsPackage = new PropertiesBasedGraphicsPackage(props);
        final MapFeatures mapFeatures = new ImageMapAnalyzer(graphicsPackage.getMapImage());
        final EnviornmentMap map = new EnviornmentMap(mapFeatures);
        final PintoManager pintoManager = new PintoManager(map);
        final PathFinder pathFinder = new DijkstraPathFinder(map);

        final int tileSize = graphicsPackage.getTileSize();
        final int ANIMATION_DURATION = Integer.parseInt(props.getProperty("GameUpdateFrequencyMS"));
        final int GAME_UPDATE_FREQUENCY_MS = ANIMATION_DURATION;
        
        //add pintos
        List<Pinto> pintos = new ArrayList<Pinto>();
        for (Point pintoLocation : mapFeatures.getPintoLocations()) {
            Pinto pinto = new Pinto(pintoLocation, map, pintoManager, pathFinder);
            map.trackObject(pinto);
            pintoManager.addPinto(pinto);
        }
        
        //add items, naming them a1, a2, a3....

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
        renderMapToFile(map, props.getProperty("AppToConsoleCommunicationFilename"));
        
        final PintoManager pm = pintoManager;
        Timer t = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                pm.work();
            }
        };
        t.scheduleAtFixedRate(task, 0, Integer.parseInt(props.getProperty("AppToConsoleCommunicationFrequencyMS")));
        
        return ui;
    }
    
    


    private static void renderMapToFile(final EnviornmentMap map, String filename) {
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
    
}
