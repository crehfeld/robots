/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pintosim;

import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class UserInterfaceTest {

    UserInterface ui;
    ByteArrayOutputStream out;

    public UserInterfaceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

    }


    @After
    public void tearDown() {
    }


    @Ignore
    public UserInterface getUI() {
        MapFeatures mf = null;
        out = new ByteArrayOutputStream();

        try {
            mf = new MapFeatures(new File("maps/100x100-all-free.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        EnviornmentMap map = new EnviornmentMap(mf);
        DijkstraPathFinder pathFinder = new DijkstraPathFinder(map);

        PintoManager pintoManager = new PintoManager(map);

        UserInterface ui = new UserInterface(
            new PrintStream(out)
          , new CommandParser()
          , map
          , pintoManager
        );

        return ui;
    }

    /**
     * Test of addInput method, of class UserInterface.
     */
    @Test
    public void testAddInput() {
        System.out.println("addInput");
        String input = "";
        String expectedOutput = "";
        UserInterface ui = null;

        //out.toString() will contain the output from the ui
        // multiple outputs will "build up" though. calling getUI will clear it, so call it after each interaction
        // note that i had to add line endings \r\n to the expected outputs

        ui = getUI();
        input = "system, note the location of tv at 55,66";
        ui.addInput(input);
        assertEquals("tv recorded. I can now retrieve it for you anytime you want.\r\n", out.toString());

        ui = getUI();// get a fresh ui, all cleared out with no traces of the previous test
        input = "system, note the location of tv at 1000,1000";
        ui.addInput(input);
        assertEquals("I can't add the tv there, the location(1000, 1000) is outside of bounds(100, 100).\r\n", out.toString());

        // here we need to test the combined output of 2 commands
        ui = getUI();// get a fresh ui, all cleared out with no traces of the previous test
        input = "system, note the location of tv at 55,66";
        ui.addInput(input);
        input = "system, get me tv";
        ui.addInput(input);
        expectedOutput = "tv recorded. I can now retrieve it for you anytime you want.\r\n"
                       + "Ok, I will get tv right now.\n\r\n";//this had a funky line ending, most wont

        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void statusOFItem() {
        System.out.println("");
        String input = "";
        String expectedOutput = "";
        UserInterface ui = null;

        ui = getUI();// get a fresh ui, all cleared out with no traces of the previous test
        input = "system, note the location of tv at 55,66";
        ui.addInput(input);
        input = "system, get me tv";
        ui.addInput(input);
        input = "system, what is the status of the item tv";
        ui.addInput(input);

        expectedOutput = "tv recorded. I can now retrieve it for you anytime you want.\r\n"
                       + "Ok, I will get tv right now.\n\r\n"
                       + "Status found. The item is in progress.\r\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void cancelGetItemPattern() {
        System.out.println("");
        String input = "";
        String expectedOutput = "";
        UserInterface ui = null;

        //out.toString() will contain the output from the ui
        // multiple outputs will "build up" though. calling getUI will clear it, so call it after each interaction
        // note that i had to add line endings \r\n to the expected outputs

        // here we need to test the combined output of 2 commands
        ui = getUI();// get a fresh ui, all cleared out with no traces of the previous test
        input = "system, note the location of tv at 55,66";
        ui.addInput(input);
        input = "system, get me tv";
        ui.addInput(input);
        input = "system, cancel item tv";
        ui.addInput(input);
        expectedOutput = "tv recorded. I can now retrieve it for you anytime you want.\r\n"
                       + "Ok, I will get tv right now.\n\r\n"
                       + "A pinto is carrying the item now. The item will be left on the ground. Do you want to cancel it? (y/n)\r\n";

        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void checkHelpDesk() {
        System.out.println("");
        String input = "";
        String expectedOutput = "";
        UserInterface ui = null;

        ui = getUI(); // get a fresh ui, all cleared out with no traces of the previous test
        input = "system, I want to send a message to the help desk";
        ui.addInput(input);

        expectedOutput = "tv recorded. I can now retrieve it for you anytime you want.\r\n"
                       + "Ok, I will get tv right now.\n\r\n"
                       + "\r\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void checkCancelHelpDesk() {
        System.out.println("");
        String input = "";
        String expectedOutput = "";
        UserInterface ui = null;

        ui = getUI(); // get a fresh ui, all cleared out with no traces of the previous test
        input = "system, I want to send a message to the help desk";
        ui.addInput(input);
        input = "system, cancel help request";
        ui.addInput(input);
        expectedOutput = "tv recorded. I can now retrieve it for you anytime you want.\r\n"
                       + "Ok, I will get tv right now.\n\r\n"
                       + "\r\n";
        assertEquals(expectedOutput, out.toString());
    }
}