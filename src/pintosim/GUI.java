package pintosim;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * Provides a graphical front end to PintoSim.
 * @author PlzSendTheCodes team
 */
public class GUI implements ActionListener, FocusListener {

    // GUI objects
    private JFrame frame = new JFrame("PintoSim");

    // Backend objects
    private CommandParser interpreter;
    private EnviornmentMap map;
    private PintoManager pintoManager;
    private Command potentialGetItemCancelationCommand;

    // Item on the map
    private String name;
    private int x;
    private int y;

    /**
     * Constructs a GUI object.
     */
    public GUI() {

        /* Menu Bar */
        JMenuBar menu = new JMenuBar();
        JMenu aboutMenu = new JMenu("About");
        frame.setJMenuBar(menu);
        menu.add(aboutMenu);

        /* Add a panel for location */
        JPanel locationPanel = new JPanel(new FlowLayout());
        locationPanel.setBorder(BorderFactory.createTitledBorder("Note Location"));
        locationPanel.setPreferredSize(new Dimension(350, 130));
        JLabel xLabel = new JLabel("X: ");
        JLabel yLabel = new JLabel("Y: ");

        final JTextField xLoc = new JTextField("", 10);
        // Get the x location
        xLoc.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                // Listen for input
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
                try {
                    x = Integer.parseInt(xLoc.getText());
                    x = Math.abs(x);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame, "Invalid input for x");
                    xLoc.setText("");
                }
            }
        });

        final JTextField yLoc = new JTextField("", 10);
        // Get the y location
        yLoc.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
               // Listen for input
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
                try {
                    y = Integer.parseInt(yLoc.getText());
                    y = Math.abs(y);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame, "Invalid input for y");
                    yLoc.setText("");
                }
            }
        });

        JLabel itemLabel = new JLabel("Name: ");

        final JTextField itemName = new JTextField("", 22);
        itemName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                // Listen for input
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                name = itemName.getText();

                // Clear all text fields after user is done entering
                xLoc.setText("");
                yLoc.setText("");
                itemName.setText("");
            }
        });

        locationPanel.add(xLabel, FlowLayout.LEFT);
        locationPanel.add(xLoc);
        locationPanel.add(yLabel);
        locationPanel.add(yLoc);
        locationPanel.add(itemLabel, BorderLayout.PAGE_START);
        locationPanel.add(itemName, BorderLayout.EAST);

        /* Location button  */
        JButton locationButton = new JButton("Add item");
        locationPanel.add(locationButton);
        locationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(frame, name + " has been noted at " +
                        x + " " + y + "!");
            }
        });

        // About menu
        JMenuItem aboutTeam = new JMenuItem("About PintoSim");
        aboutMenu.add(aboutTeam);
        aboutTeam.addActionListener(this);

        // General frame stuff
        frame.add(locationPanel);
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
    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        // Do nothing.
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        // Is overriden above where needed anyways.
    }
}
