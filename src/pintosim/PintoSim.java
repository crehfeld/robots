package pintosim;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PintoSim {

    public static void main(String[] args) {
        showCommandLineInterface();
        //auto();
    }

    public static void showCommandLineInterface() {
        MapFeatures mf;
        try {
            mf = new ImageMapAnalyzer(new File("maps/10x10-2pintos-1cup.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        EnviornmentMap map = new EnviornmentMap(mf);
        PintoManager pintoManager = new PintoManager(map);

        UserInterface ui = init(map, pintoManager);


        /* PintoManager polls for new queued tasks once every 30secs */
        Timer t = new Timer(true);
        PeriodicTask pt = new PeriodicTask(pintoManager);
        t.scheduleAtFixedRate(pt, 0, 1 * 1 * 1000);

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
        MapFeatures mf;
        try {
            mf = new ImageMapAnalyzer(new File("maps/10x10-2pintos-1cup.png"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        EnviornmentMap map = new EnviornmentMap(mf);
        PintoManager pintoManager = new PintoManager(map);

        UserInterface ui = init(map, pintoManager);


        /* PintoManager polls for new queued tasks once every 30secs */
        Timer t = new Timer(true);
        PeriodicTask pt = new PeriodicTask(pintoManager);
        t.scheduleAtFixedRate(pt, 0, 1 * 1 * 1000);

        Scanner scanner = new Scanner(System.in);
        String input = null;
        System.out.print("Welcome to the 'My Friendly Pintos' assistive Robot system!\n");
        System.out.print("Enter a command, type help, or type quit: ");
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
    public static UserInterface init(EnviornmentMap map,
            PintoManager pintoManager) {
        UserInterface ui = new UserInterface(
                System.out, new CommandParser(), map, pintoManager);

        return ui;
    }
}
