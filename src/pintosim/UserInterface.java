package pintosim;

import java.awt.Point;
import java.io.PrintStream;
import java.util.List;

public class UserInterface {

    private PrintStream out;
    private CommandParser interpreter;
    private EnviornmentMap map;
    private PintoManager pintoManager;
    private Command potentialGetItemCancelationCommand;
    private boolean helpDeskMessageInProgress = false;

    public UserInterface(PrintStream output, CommandParser interpreter, EnviornmentMap map, PintoManager pintoManager) {
        out = output;
        this.interpreter = interpreter;
        this.map = map;
        this.pintoManager = pintoManager;
    }

    public void addInput(String input) {

        dispatchCommand(interpreter.interpret(input));
    }

    public void dispatchCommand(Command cmd) {

        switch (cmd.getType()) {
            case ADD_ITEM:
                performAddItem(cmd);
                break;
            case GET_ITEM:
                performGetItem(cmd);
                break;
            case GET_ITEM_STATUS:
                performGetItemStatus(cmd);
                break;
            case CANCEL_GET_ITEM:
                performCancelGetItem(cmd);
                break;
            case CONFIRMATION:
                performConfirmedCancelGetItem(cmd);
                break;
            case HELP_DESK:
                performHelpDeskRequest();
                break;
            case CANCEL_HELP_DESK:
                performCancelHelpDeskRequest();
                break;
            case SHOW_HELP:
                performDisplayHelp();
                break;
            case SHOW_MAP:
                performDisplayMap();
                break;
            case UNKNOWN:
            default:
                if (helpDeskMessageInProgress) {
                    performHelpDeskMessage();
                    helpDeskMessageInProgress = false;
                } else {
                    performUnknown();
                }
                break;
        }
    }

    public void display(String msg) {
        out.println(msg);
    }

    public void performAddItem(Command cmd) {

        if (!map.withinBoundaries(cmd.getX(), cmd.getY())) {
            display(String.format(
                "I can't add the %s there, the location(%d, %d) is outside of bounds(%d, %d)."
              , cmd.getItemName()
              , cmd.getX()
              , cmd.getY()
              , map.getWidth()
              , map.getHeight()
            ));

            return;
        }

        if (!map.isLocationWalkable(cmd.getX(), cmd.getY())) {
            display(String.format(
                "I can't add the %s there, the location(%d, %d) is occupied by something else."
              , cmd.getItemName()
              , cmd.getX()
              , cmd.getY()
            ));

            return;
        }

        Item item = map.getItemByName(cmd.getItemName());
        if (item != null) {
            display(String.format(
                "Cant add the item %s. It already exists in the system at (%d, %d)."
              , item.getName()
              , item.getX()
              , item.getY()
            ));

            return;
        }


        map.trackItem(new Item(cmd.getItemName(), cmd.getX(), cmd.getY()));

        display(String.format(
            "%s recorded. I can now retrieve it for you anytime you want."
          , cmd.getItemName()
        ));

    }

    private void performHelpDeskRequest() {
        display("Ok. I'm listening closely, and recording. Please type your message. If you want to cancel, you can type 'Nevermind. Cancel this help desk request'");
        helpDeskMessageInProgress = true;
    }

    private void performHelpDeskMessage() {
        display("Your message was sent to the help desk.");
    }

    private void performCancelHelpDeskRequest() {
        if (helpDeskMessageInProgress) {
            display("Ok, your help desk request was canceled.");
            helpDeskMessageInProgress = false;
        } else {
            display("No message was being recorded.");
        }
        
       
    }


    private void performGetItem(final Command cmd) {
        final Item item = map.getItemByName(cmd.getItemName());
        if (item == null) {
            display(String.format(
                "I don't know where %s is. You need to first add the item."
              , cmd.getItemName()
            ));

            return;
        }


        if (pintoManager.taskExistsFor(item)) {
            display(String.format(
                "You already requested that I bring %s to you. You can't request the same item again."
                , item.getName()
            ));

            return;
        }

        if (pintoManager.canImmediatelyFulfillATask()) {
            display(String.format(
                "Ok, I will get %s right now. Please wait a moment.\n"
              , cmd.getItemName()
            ));
        } else {
            display(String.format(
                "All pintos are busy right now, but I will get %s as soon as I can."
              , cmd.getItemName()
            ));
        }


        cmd.setGetItemCallback(new GetItemCallback() {

            public void onComplete(List<Point> path) {

                display(String.format(
                    "Here's your %s."
                  , cmd.getItemName()
                ));
                display("I will show you the map of the house, and then another map with the path that the Pinto took traced on top.");
                String legend = "Map Legend:";
                legend += "\nD='Pinto Docking Station'\nE='An Elderly'\nI='An item'\nX='An obstruction(eg, a wall)'\nO='A location in the pintos path'";
                display(legend);

                // dont stop tracking the item before printing the next map or it wont show up
                display(String.format(
                    "%s"
                  , map.asciiPrint()
                ));
                
                map.stopTrackingItem(item);
                display(String.format(
                    "%s"
                  , map.asciiPrint(path)
                ));

            }

            public void onPintoMove(Point currentLocation) {
            }

            public void onDisplay(String msg) {
                display(msg);
            }

            public void onCancel(Point currentLocation, String itemName) {
                Item item = map.getItemByName(itemName);
                map.stopTrackingItem(item);
                map.trackItem(new Item(cmd.getItemName(),
                        currentLocation.x,
                        currentLocation.y));
                display(String.format(
                        "Item %s dropped at (%d, %d)."
                        + " I can now retrieve it for you from here"
                        + " anytime you want.",
                        cmd.getItemName(),
                        currentLocation.x,
                        currentLocation.y));
            }
        });

        pintoManager.addCommand(cmd);

    }

    private void performGetItemStatus(Command cmd) {
        Item item = map.getItemByName(cmd.getItemName());
        if (item == null) {
            display(String.format(
                "I never knew where %s was, so its not possible that I'm retreiving it for you. You need to first tell me where that item is."
              , cmd.getItemName()
            ));

            return;
        }

        if (!pintoManager.taskExistsFor(item)) {
            display(String.format(
                "You never requested that I retrieve %s for you, so there is no status to report."
                , item.getName()
            ));

            return;
        }

        switch (pintoManager.getTaskStatus(item.getName())) {
            case COMPLETE:
                display(String.format(
                    "The status of your retrival request for %s is 'Complete'. I already delivered it."
                  , item.getName()
                ));
                break;
            case QUEUED:
                display(String.format(
                    "The status of your retrival request for %s is 'Queued'. All my pintos are busy doing other things. They will get it for you soon."
                  , item.getName()
                ));
                break;
            case STARTED:
                display(String.format(
                    "The status of your retrival request for %s is 'Started'. A pinto is working on it as we speak, so you should have it soon."
                  , item.getName()
                ));
                break;
            default:
                throw new UnsupportedOperationException("unknown status");
        }

    }

    /**
     * If the task is currently in progress, we store the command so that we
     * remember the item name, and then prompt the user for confirmation.
     * Assuming they input y or n, the next input will be a confirmation and
     * will trigger performConfirmedCancelGetItem() where we will either use the
     * stored command to cancel it, or we null out the stored command and just
     * let the pinto keep
     *
     * @param cmd
     */
    private void performCancelGetItem(Command cmd) {
        Item item = map.getItemByName(cmd.getItemName());
        if (item == null) {
            display(String.format(
                "I never knew where %s was, so its not possible that I'm retreiving it for you."
              , cmd.getItemName()
            ));

            return;
        }

        if (!pintoManager.taskExistsFor(item)) {
            display(String.format(
                "You never requested that I retrieve %s for you, so there is nothing to cancel."
                , item.getName()
            ));

            return;
        }

        switch (pintoManager.getTaskStatus(item.getName())) {
            case COMPLETE:
                display("It's already complete, nothing to cancel.");
                break;
            case QUEUED:
                display("Ok, it's canceled.");
                pintoManager.addCommand(cmd);
                break;
            case STARTED:
                if (cmd.getType() == Command.Type.CONFIRMATION && cmd.getConfirmation() == true) {
                    display("Ok, I will leave it on the ground.");
                    pintoManager.addCommand(potentialGetItemCancelationCommand);
                } else {
                    display("A pinto is carrying the item now. The item will be left on the ground. Do you want to cancel it? (y/n)");
                    potentialGetItemCancelationCommand = cmd;
                }
                break;
            default:
                throw new UnsupportedOperationException("unknown status");
        }


    }

    private void performConfirmedCancelGetItem(Command cmd) {
        if (potentialGetItemCancelationCommand == null) {
            return;
        }

        if (cmd.getConfirmation() == true) {
            display("Ok, I will leave it on the ground.");
            pintoManager.addCommand(potentialGetItemCancelationCommand);
        } else {
            display("Ok, you will have the item soon.");
        }
        potentialGetItemCancelationCommand = null;
    }


    private void performUnknown() {
        display("I don't understand that command. Type 'help' to see options.");
    }

    private void performDisplayHelp() {
        String s = "Supported Commands:\n";
        s += "You can tell the system where an item is by saying:\n"
                + "   'system, note the location of <item name> at <x>,<y>'\n"
                + "You can request an item be retrieved by saying:\n"
                + "   'system, get me <item name>'\n"
                + "You can check the status of an item retrieval by saying:\n"
                + "   'system, what is the status of the item <item name>'\n"
                + "You can cancel an item retrieval by saying:\n"
                + "   'system, cancel item <item name>'\n"
                + "You can send a message to the help desk by saying:\n"
                + "   'system, send a message to the help desk'\n"
                + "You can send a map of the house by saying:\n"
                + "   'system, show map'\n"
                + "You can exit the program by saying:\n"
                + "   'quit'\n";



        display(s);
    }

    private void performDisplayMap() {
        String legend = "Map Legend:";
        legend += "\nD='Pinto Docking Station'\nE='An Elderly'\nI='An item'\nX='An obstruction(eg, a wall)'\nO='A location in the pintos path'\n";
        legend += map.asciiPrint();
        display(legend);
    }
}
