
package pintosim;


import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class parses text strings, and then constructs Command objects of the proper type.
 * Various types of Commands need different data put in them, and this populates them correctly.
 * 
 */

public class CommandParser {
    private static final Pattern getItemPattern            = Pattern.compile("system, get me (.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern cancelGetItemPattern      = Pattern.compile("system, cancel item (.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern helpDeskPattern           = Pattern.compile("system, send a message to the help desk", Pattern.CASE_INSENSITIVE);
    private static final Pattern cancelHelpDeskPattern     = Pattern.compile("nevermind. cancel this help desk request", Pattern.CASE_INSENSITIVE);
    private static final Pattern addItemPattern            = Pattern.compile("system, note the location of (.+) at (\\d+),.*?(\\d+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern statusOfItemPattern       = Pattern.compile("system, what is the status of the item (.+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern yesNoPattern              = Pattern.compile("^\\s*(y|n)", Pattern.CASE_INSENSITIVE);
    private static final Pattern showHelpPattern           = Pattern.compile("^help", Pattern.CASE_INSENSITIVE);
    private static final Pattern showMapPattern            = Pattern.compile("^system, show map", Pattern.CASE_INSENSITIVE);
    
    /**
     * Parses a string of text and creates a Command object from it, populated with any relevant data.
     * 
     * @param cmd a string of user input
     * @return A Command 
     */
    public Command interpret(String cmd) {

        Matcher matcher;
        
        matcher = getItemPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(Command.Type.GET_ITEM, matcher.group(1));
        }
        
        matcher = helpDeskPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(Command.Type.HELP_DESK);
        }
        
        matcher = cancelHelpDeskPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(Command.Type.CANCEL_HELP_DESK);
        }
        
        matcher = addItemPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(
                Command.Type.ADD_ITEM
              , matcher.group(1)
              , Integer.parseInt(matcher.group(2))
              , Integer.parseInt(matcher.group(3))
            );
        }
        
        matcher = statusOfItemPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(
                Command.Type.GET_ITEM_STATUS
              , matcher.group(1)
            );
        }
        
        matcher = cancelGetItemPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(
                Command.Type.CANCEL_GET_ITEM
              , matcher.group(1)
            );
        }
        
        matcher = yesNoPattern.matcher(cmd);
        if (matcher.find()) {
            Command command = new Command(Command.Type.CONFIRMATION);
            boolean isYes = matcher.group(1).equalsIgnoreCase("y");
            command.setConfirmation(isYes);
            return command;
        }
        
        matcher = showHelpPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(Command.Type.SHOW_HELP);
        }
        
        matcher = showMapPattern.matcher(cmd);
        if (matcher.find()) {
            return new Command(Command.Type.SHOW_MAP);
        }
        
        
        return new Command(Command.Type.UNKNOWN);
    }
    
    
}
