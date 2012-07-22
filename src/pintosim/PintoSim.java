package pintosim;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class PintoSim {
    
    private static final String OUTPUT_RENDERING_FILENAME = "dynamic-output.txt";
    private static final int    OUTPUT_RENDERING_FREQUENCY_MS = 100;

    public static void main(String[] args) {
        //showCommandLineInterface();
        auto();
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
    
    public static void auto() {
        UserInterface ui = build();

        Scanner scanner = new Scanner(System.in);
        String input = null;
        System.out.print("Welcome to the 'My Friendly Pintos' assistive Robot system!\n");
        System.out.print("Enter a command, type help, or type quit: ");
        ui.addInput("system, note the location of a1 at 8,5");
        ui.addInput("system, note the location of a2 at 8,6");
        ui.addInput("system, note the location of a3 at 8,4");
        ui.addInput("system, note the location of prunes at 1,5");
        ui.addInput("system, get me prunes");
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
            mapFeatures = new ImageMapAnalyzer(new File("maps/30x30-test.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        EnviornmentMap map = new EnviornmentMap(mapFeatures);
        PintoManager pintoManager = new PintoManager(map);
        PathFinder pathFinder = new DijkstraPathFinder(map);
        
        List<Pinto> pintos = new ArrayList<Pinto>();
        for (Point pintoLocation : mapFeatures.getPintoLocations()) {
            Pinto pinto = new Pinto(map, pintoManager, pintoLocation, pathFinder);
            map.trackObject(pinto);
            pintoManager.addPinto(pinto);
        }
        
        map.trackObject(new Person(mapFeatures.getPersonLocation()));
        

        
        
        UserInterface ui = new UserInterface(
            System.out
          , new CommandParser()
          , map
          , pintoManager
        );
        
        try {
            renderMapToFile(map, OUTPUT_RENDERING_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
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
    
    
    private static void renderMapToFile(final EnviornmentMap map, String filename) throws IOException {
        final RandomAccessFile file = new RandomAccessFile(filename, "rw");
        file.setLength(0);//truncate
        Timer t = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    file.seek(0);
                    file.writeChars(map.asciiPrint());
                } catch (IOException e) {
                    e.printStackTrace();
                    cancel();
                }
            }
        };
        t.scheduleAtFixedRate(task, 0, OUTPUT_RENDERING_FREQUENCY_MS);
    }
}
