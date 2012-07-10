

package pintosim;

import java.awt.Point;
import java.util.List;


/**
 * This class is used as a way to marshal messages through the system. It should
 * be though of as a message. A Command has a Type, and each type has optional
 * data fields that can be placed into a command. It's mostly used as a 
 * key/value store.
 *
 */
public class Command {


    private Type type;
    private int x, y;
    private String itemName;
    private GetItemCallback getItemCallback;
    private boolean confirmation = false;
    private String helpDeskMessage;
    
    
    /**
     * Describes the type of command. 
     * Only one type is allowed at a time.
     */
    public static enum Type {
        GET_ITEM
      , CANCEL_GET_ITEM
      , ADD_ITEM
      , HELP_DESK
      , CANCEL_HELP_DESK
      , GET_ITEM_STATUS
      , UNKNOWN
      , CONFIRMATION
      , SHOW_HELP
      , SHOW_MAP
    }

    
    /**
     * This constructor is well suited for Commands that don't need to 
     * store additional data.
     *
     * @param type The type of command.
     */
    public Command(Command.Type type) {
        this.type = type;
    }
    
    
    /**
     * This constructor is well suited for an GET_ITEM or CANCEL_GET_ITEM
     * command because it accepts a type and itemName.
     *
     * @param type The type of command.
     * @param itemName A name of an Item
     */
    public Command(Command.Type type, String itemName) {
        this(type);
        this.itemName = itemName;
    }
    
    
    /**
     * This constructor is well suited for an ADD_ITEM command because 
     * it accepts a type, itemName, and position.
     * 
     * @param type The type of command.
     * @param itemName A name of an Item 
     * @param x X coordinate of the Item
     * @param y Y coordinate of the Item
     */
    public Command(Command.Type type, String itemName, int x, int y) {
        this(type, itemName);
        this.x = x;
        this.y = y;
    }
    
    
    /**
     * Returns the Type of this Command.
     * 
     * @return The type of command 
     */
    public Type getType() {
        return this.type;
    }
    
    
    /**
     * Returns an itemName, if present
     * 
     * @return An itemName, or null.
     */
    public String getItemName() {
        return itemName.toLowerCase();
    }
    
    
    /**
     * Returns the x coordinate.
     * 
     * @return An integer coordinate.
     */
    public int getX() {
        return x;
    }
    
    
    /**
     * Returns the y coordinate.
     * 
     * @return An integer coordinate.
     */
    public int getY() {
        return y;
    }
    
    
    /**
     * Sets a callback object whose methods may later be called. Useful for 
     * notification of when asynchronous operations complete.
     * 
     * @param callback A GetItemCallback object.
     */
    public void setGetItemCallback(GetItemCallback callback) {
        getItemCallback = callback;
    }
    
    
    /**
     * Invokes the onComplete() method on the composed GetItemCallback
     */
    public void onComplete(List<Point> path) {
        getItemCallback.onComplete(path);
    }
    
    /**
     * Invokes the onDisplay() method on the composed GetItemCallback
     */
    public void onDisplay(String msg) {
        getItemCallback.onDisplay(msg);
    }
    
    /**
     * Invokes the onPintoMove() method on the composed GetItemCallback
     */
    public void onPintoMove(Point pt) {
        getItemCallback.onPintoMove(pt);
    }
    
    public void onCancel(Point pt) {
        getItemCallback.onCancel(pt, this.itemName.toLowerCase());
    }
    /**
     * A user may be prompted to confirm a yes/no question. 
     * This stores the answer. Used for some situations after 
     * a CANCEL_GET_ITEM command was issued.
     * 
     * @return a boolean which is true to signify a "yes". 
     */
    public boolean getConfirmation() {
        return confirmation;
    }

    
    /**
     * A user may be prompted to confirm a yes/no question. 
     * This stores the answer. Used for some situations after 
     * a CANCEL_GET_ITEM command was issued.
     * 
     * @param confirm A boolean where true indicates "yes"
     */
    public void setConfirmation(boolean confirm) {
        this.confirmation = confirm;
    }
    
    /**
     * A user recorded message to be sent to the help desk
     * 
     * @param message A string message
     */
    public void setHelpDeskMessage(String message) {
        helpDeskMessage = message;
    }
    
    /**
     * A user recorded message to be sent to the help desk
     * 
     * @return A string message
     */
    public String getHelpDeskMessage() {
        return helpDeskMessage;
    }
    
    
    /**
     * 
     * @return A string representation of this Command's Type.
     */
    public String toString() {
        return type.toString();
    }
    
}
