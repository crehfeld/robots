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
                JOptionPane.showMessageDialog(frame,
                        "Work in progress",
                        "Location",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // retrieve an item
        JButton retrieve = new JButton("Retrieve an item");
        retrieve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(frame,
                        "Work in progress",
                        "Retrieve",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // get status
        JButton status = new JButton("Check Status");
        status.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(frame,
                        "Work in progress",
                        "Status",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // cancel a command
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(frame,
                        "Work in progress",
                        "Cancel",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // call help desk
        JButton help = new JButton("Call help desk");
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(frame,
                        "Work in progress",
                        "Help Desk",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add all the buttons to the panel
        panel.add(location);
        panel.add(retrieve);
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

    // if the user selects or deselects an item
    public void itemStateChanged(ItemEvent e) {

    }
}