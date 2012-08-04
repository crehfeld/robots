package pintosim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Provides a graphical front end to PintoSim
 * @author PlzSendTheCodes team
 */
public class GUI implements ActionListener {

    // GUI objects
    private JFrame frame = new JFrame("PintoSim");
    private JLabel statusOfCommand = new JLabel("");

    // Backend objects
    private EnvironmentMap map;
    private PintoManager pintoManager;

    // Item on the map
    private int x;
    private int y;
    
    private JTextField xLoc = new JTextField("", 3);
    private JTextField yLoc = new JTextField("", 3);
    private JTextField itemName = new JTextField("", 8);
    /**
     * Constructs a GUI object.
     * @param pintoManager manages pintos
     * @param map          the environment map
     * @param animPanel    the animation panel
     */
    public GUI(PintoManager pintoManager, EnvironmentMap map, AnimPanel animPanel) {

        this.pintoManager = pintoManager;
        this.map = map;
        
        setupMouseListener(animPanel);

        /* Menu Bar */
        JMenuBar menu = new JMenuBar();
        JMenu aboutMenu = new JMenu("About");
        frame.setJMenuBar(menu);
        menu.add(aboutMenu);

        /* add a content panel and a panel for all the commands. */
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(1100, 680));
        statusOfCommand.setText("Pintos are waiting for a command.");
        JPanel commands = new JPanel(new FlowLayout(FlowLayout.LEFT));
        commands.setPreferredSize(new Dimension(1100, 161));

        /* Add a panel for location */
        JPanel locationPanel = new JPanel(new FlowLayout());
        locationPanel.setBorder(BorderFactory.createTitledBorder(" Note Location "));
        locationPanel.setPreferredSize(new Dimension(200, 130));

        // Labels and fields
        JLabel xLabel = new JLabel("X: ");
        JLabel yLabel = new JLabel("Y: ");

        JLabel itemLabel = new JLabel("Name: ");


        itemName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (itemName.getText().equals("")) {
                    display("No name entered!");
                } else {
                    try {
                        x = Math.abs(Integer.parseInt(xLoc.getText()));
                        y = Math.abs(Integer.parseInt(yLoc.getText()));
                    } catch (NumberFormatException ex) {
                        statusOfCommand.setText("Error: (" + xLoc.getText()
                                + "," + yLoc.getText() + ") is not a valid location.");
                        xLoc.setText("");
                        yLoc.setText("");
                        itemName.setText("");
                        return;
                    }
                    performAddItem(new Command(Command.Type.ADD_ITEM, itemName.getText(), x, y));
                }
                // Clear all text fields after user is done entering
                xLoc.setText("");
                yLoc.setText("");
                itemName.setText("");
            }
        });

        // Location button
        JButton locationButton = new JButton("Add item");
        locationButton.setLayout(new FlowLayout());
        locationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (itemName.getText().equals("")) {
                    display("No name entered!");
                } else {
                    try {
                        x = Math.abs(Integer.parseInt(xLoc.getText()));
                        y = Math.abs(Integer.parseInt(yLoc.getText()));
                    } catch (NumberFormatException ex) {
                        statusOfCommand.setText("Error: (" + xLoc.getText()
                                + "," + yLoc.getText() + ") is not a valid location.");
                        xLoc.setText("");
                        yLoc.setText("");
                        itemName.setText("");
                        return;
                    }
                    performAddItem(new Command(Command.Type.ADD_ITEM, itemName.getText(), x, y));
                }
                // Clear all text fields after user is done entering
                xLoc.setText("");
                yLoc.setText("");
                itemName.setText("");
            }
        });

        /* Get item panel */
        JPanel getPanel = new JPanel(new FlowLayout());
        getPanel.setBorder(BorderFactory.createTitledBorder(" Get Item "));
        getPanel.setPreferredSize(new Dimension(200, 130));

        // Labels and fields
        JLabel getItemName = new JLabel("Name: ");
        final JTextField getItemField = new JTextField("", 8);

        getItemField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (getItemField.getText().equals("")) {
                    display("No name entered!");
                } else {
                    performGetItem(new Command(Command.Type.GET_ITEM, getItemField.getText()));
                    // Clear out values
                    getItemField.setText("");
                }
            }
        });

        // Get item button
        JButton getItemButton = new JButton("Get Item");
        JLabel getItemHint = new JLabel("...or click the item");
        getItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (getItemField.getText().equals("")) {
                    display("No name entered!");
                } else {
                    performGetItem(new Command(Command.Type.GET_ITEM, getItemField.getText()));
                    // Clear out values
                    getItemField.setText("");
                }
            }
        });

        /* Get Status Panel */
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder(" Get status "));
        statusPanel.setPreferredSize(new Dimension(200, 130));

        // Labels and Fields
        JLabel statusNameLabel = new JLabel("Name: ");
        JLabel statusHint = new JLabel("...or alt-click the item");
        final JTextField statusNameField = new JTextField("", 8);

        statusNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (statusNameField.getText().equals("")) {
                    display("No name entered!");
                } else {
                    performGetItemStatus(new Command(
                            Command.Type.GET_ITEM_STATUS,
                            statusNameField.getText()));
                    // Clear out fields
                    statusNameField.setText("");
                }
            }
        });

        // status button
        JButton statusButton = new JButton("Get Status of item");
        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (statusNameField.getText().equals("")) {
                    display("No name entered!");
                } else {
                    performGetItemStatus(new Command(
                            Command.Type.GET_ITEM_STATUS,
                            statusNameField.getText()));
                    // Clear out fields
                    statusNameField.setText("");
                }
            }
        });

        /* Cancel item Panel */
        JPanel cancelPanel = new JPanel(new FlowLayout());
        cancelPanel.setBorder(BorderFactory.createTitledBorder(" Cancel Item "));
        cancelPanel.setPreferredSize(new Dimension(200, 130));

        // Labels and Fields
        JLabel cancelNameLabel = new JLabel("Name: ");
        JLabel cancelHint = new JLabel("...or shift-click the item");
        final JTextField cancelNameField = new JTextField("", 8);

        cancelNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (cancelNameField.getText().equals("")) {
                    display("No name entered!");
                } else {
                    performCancelGetItem(new Command(Command.Type.CANCEL_GET_ITEM, cancelNameField.getText()));
                    cancelNameField.setText("");
                }
            }
        });

        // Cancel Button
        JButton cancelButton = new JButton("Cancel Item");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (cancelNameField.getText().equals("")) {
                    statusOfCommand.setText("No name entered!");
                } else {
                    performCancelGetItem(new Command(Command.Type.CANCEL_GET_ITEM, cancelNameField.getText()));
                    cancelNameField.setText("");
                }
            }
        });

        /* Help Desk Panel */
        JPanel helpPanel = new JPanel(new FlowLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder(" Call Help Desk "));
        helpPanel.setPreferredSize(new Dimension(250, 130));
        final JTextArea helpArea = new JTextArea("");
        helpArea.setBorder(BorderFactory.createTitledBorder(""));
        final JScrollPane helpScrollPane = new JScrollPane(helpArea);
        helpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        helpScrollPane.setPreferredSize(new Dimension(50, 30));
        helpArea.setEditable(true);
        helpArea.setPreferredSize(new Dimension(220, 60));
        helpArea.setCaretPosition(helpArea.getDocument().getLength());

        // Help button
        JButton helpButton = new JButton("Send query to Help Desk");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                performHelpDesk(helpArea.getText());
            }
        });

        /* Create a commandStatus panel to notify the user if a command finishes */
        JPanel commandStatus = new JPanel(new FlowLayout());
        //commandStatus.setBorder(BorderFactory.createTitledBorder(" Status of commands "));
        commandStatus.setPreferredSize(new Dimension(1070, 25));
        commandStatus.add(statusOfCommand, BorderLayout.CENTER);

        /* Create a panel to house (pun intended) the house map */
        JPanel mapPanel = new JPanel(new FlowLayout());
        mapPanel.setBorder(BorderFactory.createTitledBorder(" House Map "));
        mapPanel.setPreferredSize(new Dimension(1070, 500));
        mapPanel.add(animPanel, BorderLayout.CENTER);

        // About menu
        JMenuItem aboutTeam = new JMenuItem("About PintoSim");
        aboutMenu.add(aboutTeam);
        aboutTeam.addActionListener(this);

        /* Frame settings */
        // Add all the panels to the frame
        locationPanel.add(xLabel, FlowLayout.LEFT);
        locationPanel.add(xLoc);
        locationPanel.add(yLabel);
        locationPanel.add(yLoc);
        locationPanel.add(itemLabel, BorderLayout.PAGE_START);
        locationPanel.add(itemName, BorderLayout.EAST);
        locationPanel.add(locationButton);
        getPanel.add(getItemName);
        getPanel.add(getItemField);
        getPanel.add(getItemButton);
        getPanel.add(getItemHint);
        statusPanel.add(statusNameLabel);
        statusPanel.add(statusNameField);
        statusPanel.add(statusButton);
        statusPanel.add(statusHint);
        cancelPanel.add(cancelNameLabel);
        cancelPanel.add(cancelNameField);
        cancelPanel.add(cancelButton);
        cancelPanel.add(cancelHint);
        helpPanel.add(helpArea);
        helpPanel.add(helpButton);
        commands.add(locationPanel);
        commands.add(getPanel);
        commands.add(statusPanel);
        commands.add(cancelPanel);
        commands.add(helpPanel);
        commands.add(commandStatus);
        content.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.PAGE_END);
        content.add(commands);
        content.add(mapPanel);
        frame.add(content);
        frame.pack();

        // set defaults and make it visible
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Manages actions on a object
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("About PintoSim")) {
            JOptionPane.showMessageDialog(frame,
                    "Designed by PlzSendTheCodes team",
                    "About PintoSim",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Add more actions and menu items if necessary.
    }

    /**
     * Sets the status of command message.
     * @param str the message to display
     */
    public void display(String str) {
        statusOfCommand.setText(str);
    }

    /**
     * Adds an item to the environment map.
     * @param cmd the addItem command
     */
    private void performAddItem(Command cmd) {
        if (!map.withinBoundaries(cmd.getX(), cmd.getY())) {
            display(String.format(
                    "I can't add the %s there," +
                            " the location(%d, %d) is" +
                            " outside of bounds(%d, %d)."
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
                    "I can't add the %s there," +
                            " the location(%d, %d) is " +
                            "occupied by something else."
                    , cmd.getItemName()
                    , cmd.getX()
                    , cmd.getY()
            ));
            return;
        }

        Item item = map.getItemByName(cmd.getItemName());
        if (item != null) {
            display(String.format(
                    "Cant add the item %s." +
                            " It already exists" +
                            " in the system at (%d, %d)."
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

    /**
     * Gets an item for the user
     * @param cmd the getItem command
     */
    private void performGetItem(final Command cmd) {
        final Item item = map.getItemByName(cmd.getItemName());
        if (item == null) {
            display(String.format(
                    "I don't know where %s is." +
                            " You need to first add the item."
                    , cmd.getItemName()
            ));

            return;
        }
        if (pintoManager.uncompletedTaskExistsFor(item)) {
            display(String.format(
                    "You already requested that I bring %s to you." +
                            " You can't request the same item again yet."
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
                        "\nHere's your %s.\n"
                        , cmd.getItemName()
                ));
            }

            public void onCancel(Point currentLocation, String itemName) {
            }
        });
        pintoManager.addCommand(cmd);
    }

    private void performGetItemStatus(Command cmd) {
        Item item = map.getItemByName(cmd.getItemName());
        if (item == null) {
            display(String.format(
                    "I never knew where %s was, " +
                            "so its not possible that I'm retrieving" +
                            " it for you." +
                            " You need to first tell me where that item is."
                    , cmd.getItemName()
            ));
            return;
        }
        if (!pintoManager.uncompletedTaskExistsFor(item) &&
                !pintoManager.completedTaskExistsFor(item)) {
            display(String.format(
                    "You never requested that I retrieve %s for you," +
                            " so there is no status to report."
                    , item.getName()
            ));
            return;
        }
        switch (pintoManager.getTaskStatus(item)) {
            case COMPLETE:
                display(String.format(
                        "The status of your retrieval request for" +
                                " %s is 'Complete'." +
                                " I already delivered it."
                        , item.getName()
                ));
                break;
            case QUEUED:
                display(String.format(
                        "The status of your retrieval request for" +
                                " %s is 'Queued'. All my pintos are busy doing" +
                                " other things." +
                                " They will get it for you soon."
                        , item.getName()
                ));
                break;
            case STARTED:
            case ITEM_BEING_CARRIED:
                display(String.format(
                        "The status of your retrieval request for" +
                                " %s is 'Started'. A pinto is working" +
                                " on it as we speak, " +
                                "so you should have it soon."
                        , item.getName()
                ));
                break;
            default:
                throw new UnsupportedOperationException("unknown status");
        }
    }

    /**
     * Cancels an item retrieval.
     * @param cmd the cancelItem command
     */
    private void performCancelGetItem(Command cmd) {
        Command potentialGetItemCancelationCommand;
        Item item = map.getItemByName(cmd.getItemName());
        int potentialCancelation = 0;
        if (item == null) {
            display(String.format(
                    "I never knew where %s was, " +
                            "so it's not possible that I'm retrieving it for you."
                    , cmd.getItemName()
            ));
            return;
        }
        if (pintoManager.uncompletedTaskExistsFor(item)) {

            if (pintoManager.isItemBeingCarried(item)) {
                pintoManager.pauseTaskIfRunning(item);
                potentialCancelation = JOptionPane.showConfirmDialog(
                        frame, "A Pinto is already carrying the item." +
                        " Do you want it to leave the item on the ground?",
                        "Item is being carried!", JOptionPane.YES_NO_CANCEL_OPTION);

                if (potentialCancelation == JOptionPane.YES_OPTION) {
                    potentialGetItemCancelationCommand = cmd;
                    pintoManager.addCommand(potentialGetItemCancelationCommand);
                    display(String.format(
                            "Ok, I will leave %s on the ground at (%d, %d)."
                            , item.getName()
                            , item.getX()
                            , item.getY()
                    ));
                } else {
                    display("Ok, you will have the item soon.");
                    pintoManager.unPauseTask(item);
                }
            } else {
                display("Ok, it's canceled.");
                pintoManager.addCommand(cmd);
            }
            return;
        }
        if (pintoManager.completedTaskExistsFor(item)) {
            display(String.format(
                    "I already completed the request for %s," +
                            " so it's too late to cancel."
                    , item.getName()
            ));
            return;
        }
        display(String.format(
                "You never requested that I retrieve %s for you," +
                        " so there is nothing to cancel."
                , item.getName()
        ));
    }

    /**
     * Sends the user's help request to the help desk.
     */
    public void performHelpDesk(final String query) {
        if (query.length() == 0) {
            display("No query entered!");
        } else if (query.length() < 3) {
            display("Your query was too short.");
        } else {
            display("Okay, your message was sent to the Help Desk.");
        }
    }

    private void setupMouseListener(Component component) {
        component.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent evt) {
                // integer division to get back down to tile coords
                int x = evt.getX() / 20;
                int y = evt.getY() / 20;
                
                if (evt.isControlDown()) {
                    //populate location of note location form
                    xLoc.setText("" + x);
                    yLoc.setText("" + y);
                    itemName.requestFocusInWindow();
                    return;
                }
                
                
                for (Item item : map.getItemsAt(x, y)) {
                    if (evt.isShiftDown()) {
                        //cancel item
                        Command cmd = new Command(Command.Type.CANCEL_GET_ITEM, item.getName());
                        performCancelGetItem(cmd);
                    } else if (evt.isAltDown()) {
                        //get item status
                        Command cmd = new Command(Command.Type.GET_ITEM_STATUS, item.getName());
                        performGetItemStatus(cmd);
                    } else {
                        //get item
                        Command cmd = new Command(Command.Type.GET_ITEM, item.getName());
                        performGetItem(cmd);
                    }
                    
                    // important to break, we pick ONE arbitrary item on this tile
                    // keep in mind the animations are always behind the actual
                    // item location by up to 1 tile, so clicking moving items
                    // may not behave perfectly
                    // click ahead of it, if you can guess the immediate next tile
                    break;
                }
            }
        });
    }
    
    
}