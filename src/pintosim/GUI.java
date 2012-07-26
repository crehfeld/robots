package pintosim;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * Provides a graphical front end to PintoSim.
 *
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

        /* add a content panel and a panel for all the commands. */
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(680, 680));
        JPanel commands = new JPanel(new FlowLayout());
        commands.setPreferredSize(new Dimension(680, 140));

        /* Add a panel for location */
        JPanel locationPanel = new JPanel(new FlowLayout());
        locationPanel.setBorder(BorderFactory.createTitledBorder(" Note Location "));
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
                if (xLoc.getText().equals("")) {
                    x = 0;
                } else {
                    try {
                        x = Integer.parseInt(xLoc.getText());
                        x = Math.abs(x); // ignore negative numbers
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(frame, "Invalid input for x");
                        xLoc.setText("");
                    }
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
                if (yLoc.getText().equals("")) {
                    y = 0;
                } else {
                    try {
                        y = Integer.parseInt(yLoc.getText());
                        y = Math.abs(y); // ignore negative numbers
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(frame, "Invalid input for y");
                        yLoc.setText("");
                    }
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
                if (itemName.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame, "No name entered!");
                    itemName.setText("");
                }
                else {
                    name = itemName.getText();
                }

                // Clear all text fields after user is done entering
                xLoc.setText("");
                yLoc.setText("");
                itemName.setText("");
            }
        });
        /* Location button  */
        JButton locationButton = new JButton("Add item");
        locationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (itemName.getText().equals(""))
                    JOptionPane.showMessageDialog(frame, "No name entered!");
                else {
                    JOptionPane.showMessageDialog(frame, name + " has been noted at " +
                        x + " " + y + "!");
                }
            }
        });

        /* Get item panel */
        JPanel getPanel = new JPanel(new FlowLayout());
        getPanel.setBorder(BorderFactory.createTitledBorder(" Get Item "));
        getPanel.setPreferredSize(new Dimension(350, 130));
        JLabel getItemName = new JLabel("Name: ");

        final JTextField getItemField = new JTextField("", 10);
        getItemName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                // Listen for input
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                if (getItemField.getText().equals("")) {

                }
                else {
                    name = getItemField.getText();
                }
            }
        });

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
        commands.add(locationPanel);
        commands.add(getPanel);
        content.add(commands);
        frame.add(content);
        frame.pack();

        // set defaults and make it visible
        JFrame.setDefaultLookAndFeelDecorated(true);
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
        // Listen for input.
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        // Method is overridden whenever needed.
    }
}