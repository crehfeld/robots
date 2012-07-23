package pintosim;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class PintoSim {
    private static final String IMAGE_MAP_FILENAME = "maps/30x30-test.png";
    private static final String OUTPUT_RENDERING_FILENAME = "dynamic-output.txt";
    private static final int    OUTPUT_RENDERING_FREQUENCY_MS = 100;

    public static void main(String[] args) {
        showCommandLineInterface();
    }

    public static void showCommandLineInterface() {

        UserInterface ui = build();

        Scanner scanner = new Scanner(System.in);
        String input = null;
        System.out.print("Welcome to the 'My Friendly Pintos' assistive Robot system!\n");
        System.out.print("Enter a command, type help, or type quit: ");
        input = scanner.nextLine();


        while (0 != input.trim().indexOf("quit")) {
            ui.addInput(input);
            System.out.print("Enter a command, type help, or type quit: ");
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
        
        EnviornmentMap map = new EnviornmentMap(mapFeatures);
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
        t.scheduleAtFixedRate(task, 0, 20 * 10);
        

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
