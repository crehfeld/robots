package pintosim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Provides a graphical front end to PintoSim.
 *
 * @author PlzSendTheCodes team
 */
public class GUI implements ActionListener, ItemListener {

    // GUI objects
    private JFrame frame = new JFrame("PintoSim");

    // Backend objects
    private CommandParser interpreter;
    private EnviornmentMap map;
    private PintoManager pintoManager;
    private Command potentialGetItemCancelationCommand;

    public static void main(String[] args) {
        new GUI();
    }

    /**
     * Constructs a GUI object.
     */
    public GUI() {

        /*this.interpreter = interpreter;
        this.map = map;
        this.pintoManager = pintoManager;*/

        JMenuBar menu = new JMenuBar();
        JMenu aboutMenu = new JMenu("About");
        frame.setJMenuBar(menu);
        menu.add(aboutMenu);

        // Create a JPanel that flows from left to right for JButtons
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // note the location
        JButton location = new JButton("Note a location");
        location.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String itemName = JOptionPane.showInputDialog(
                        frame, "Enter the name of the item: ");
                if (itemName != null) {
                    JOptionPane.showMessageDialog(frame, "Click on the map where the item is located");
                    // Add a listener to get the coordinates
                }
                else
                    JOptionPane.showMessageDialog(frame, "Item not noted");
            }
        });

        // retrieve an item
        JButton retrieve = new JButton("Retrieve an item");
        retrieve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String itemName = JOptionPane.showInputDialog(frame,
                        "Enter the name of the item that you wish to retrieve: "
                        );
                if (itemName != null) {
                    JOptionPane.showMessageDialog(frame, "Retrieving " + itemName);
                    // tell pinto manager to tell pintos to retrieve the item
                    // wip
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Item will not be retrieved.");
                }
            }
        });

        // get status
        JButton status = new JButton("Check Status");
        status.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String itemName = JOptionPane.showInputDialog(frame,
                        "Enter the name of the item that you wish to check the status on:"
                        );
                if (itemName != null) {
                    JOptionPane.showMessageDialog(frame,
                            "Checking status on " + itemName);
                // tell pinto manager to check status
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Status request canceled");
                }
            }
        });

        // cancel a command
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               String itemName = JOptionPane.showInputDialog(frame,
                        "Enter the name of the item that you wish to cancel work on:"
                        );
                if (itemName != null) {
                    // wip.. check with pinto manager again.
                }
                else
                    JOptionPane.showMessageDialog(frame, "Okay, Pintos will continue with their work.");
            }
        });

        // call help desk
        JButton help = new JButton("Call help desk");
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String query = JOptionPane.showInputDialog(frame,
                        "What is the nature of the problem?");
                if (query != null)
                    JOptionPane.showMessageDialog(frame,
                            "Your query has been sent to the help desk.");
                else
                    JOptionPane.showMessageDialog(frame,
                            "Query canceled.");
            }
        });

        // Add all the buttons to the panel
        panel.add(location);
        panel.add(retrieve);
        panel.add(status);
        panel.add(cancel);
        panel.add(help);
        frame.add(panel);

        // About menu
        JMenuItem aboutTeam = new JMenuItem("About PintoSim");
        aboutMenu.add(aboutTeam);
        aboutTeam.addActionListener(this);

        // General frame stuff
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Manages actions on a object
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("About PintoSim")) {
            JOptionPane.showMessageDialog(frame,
                    "Designed by PlzSendTheCodes team",
                    "About PintoSim",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // wip...
    }

    /**
     * Add support if the user selects or deselects an item.
     * @param e the event
     */
    public void itemStateChanged(ItemEvent e) {

    }

    /**
     * Draws and updates items on the map
     */
    public void draw() {

    }
}