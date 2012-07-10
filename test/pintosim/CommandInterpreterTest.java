
package pintosim;

import org.junit.*;
import static org.junit.Assert.*;

public class CommandInterpreterTest {
    
    public CommandInterpreterTest() {
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

    /**
     * Test of interpret method, of class CommandInterpreter.
     */
    @Test
    public void testInterpret() {
        System.out.println("interpret");
        CommandParser ci = new CommandParser();
        Command cmd;
        
        
        
        cmd = ci.interpret("system, get me prunes");
        assertEquals(Command.Type.GET_ITEM, cmd.getType());
        assertEquals("prunes", cmd.getItemName());
        
        
        cmd = ci.interpret("system, cancel item prunes");
        assertEquals(Command.Type.CANCEL_GET_ITEM, cmd.getType());
        assertEquals("prunes", cmd.getItemName());
        
        
        cmd = ci.interpret("system, send a message to the help desk");
        assertEquals(Command.Type.HELP_DESK, cmd.getType());
        
        

        cmd = ci.interpret("nevermind. cancel this help desk request");
        assertEquals(Command.Type.CANCEL_HELP_DESK, cmd.getType());
        
        
        
        cmd = ci.interpret("system, note the location of box of apples at 88,11");
        assertEquals(Command.Type.ADD_ITEM, cmd.getType());
        assertEquals("box of apples", cmd.getItemName());
        assertEquals(88, cmd.getX());
        assertEquals(11, cmd.getY());
        
        

        cmd = ci.interpret("system, what is the status of the item remote control");
        assertEquals(Command.Type.GET_ITEM_STATUS, cmd.getType());
        assertEquals("remote control", cmd.getItemName());

    }
    

}
