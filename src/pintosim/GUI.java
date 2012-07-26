package pintosim;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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
        content.setPreferredSize(new Dimension(1080, 680));
        JPanel commands = new JPanel(new FlowLayout(FlowLayout.LEFT));
        commands.setPreferredSize(new Dimension(1080, 140));

        /* Add a panel for location */
        JPanel locationPanel = new JPanel(new FlowLayout());
        locationPanel.setBorder(BorderFactory.createTitledBorder(" Note Location "));
        locationPanel.setPreferredSize(new Dimension(250, 130));
        JLabel xLabel = new JLabel("X: ");
        JLabel yLabel = new JLabel("Y: ");

        final JTextField xLoc = new JTextField("", 5);
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

        final JTextField yLoc = new JTextField("", 5);
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

        final JTextField itemName = new JTextField("", 10);
        itemName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                // Listen for input
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                    name = itemName.getText();
            }
        });
        // Location button
        JButton locationButton = new JButton("Add item");
        locationButton.setLayout(new FlowLayout());
        locationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (itemName.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame, "No name entered!");
                }
                else {
                    // Work in progress: Actually add the item to the map.
                    JOptionPane.showMessageDialog(frame, name + " has been noted at " +
                            x + " " + y + "!");
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
        JLabel getItemName = new JLabel("Name: ");

        final JTextField getItemField = new JTextField("", 8);
        getItemField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                // Listen for input
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                    name = getItemField.getText();
            }
        });
        // Get item button
        JButton getItemButton = new JButton("Get Item");
        getItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (getItemField.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame, "No name entered!");
                } else {
                    // first check if the item is valid and then,
                    // show this dialog if Pintos are available:
                    // if not, show another dialog. Work in progress still.
                    JOptionPane.showMessageDialog(frame, "Pintos have been " +
                            "dispatched to get " + name + "!");
                    // Clear out values
                    getItemField.setText("");
                }
            }
        });

        /* Get Status Panel */
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder(" Get status "));
        statusPanel.setPreferredSize(new Dimension(200, 130));
        JLabel statusNameLabel = new JLabel("Name: ");

        final JTextField statusNameField = new JTextField("", 8);
        statusNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                // Listen for input
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                name = statusNameField.getText();
            }
        });
        // status button
        JButton statusButton = new JButton("Get Status of item");
        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (statusNameField.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame, "No name entered!");
                }
                else {
                    // Work in progress:
                    // Check status here and then show this dialog:
                    JOptionPane.showMessageDialog(frame, "Getting status of " + name);
                }
            }
        });

        /* Cancel item Panel */
        JPanel cancelPanel = new JPanel(new FlowLayout());
        cancelPanel.setBorder(BorderFactory.createTitledBorder(" Cancel Item "));
        cancelPanel.setPreferredSize(new Dimension(200, 130));

        /* Help Desk Panel */
        JPanel helpPanel = new JPanel(new FlowLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder(" Call Help Desk "));
        helpPanel.setPreferredSize(new Dimension(200, 130));

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
        statusPanel.add(statusNameLabel);
        statusPanel.add(statusNameField);
        statusPanel.add(statusButton);
        commands.add(locationPanel);
        commands.add(getPanel);
        commands.add(statusPanel);
        commands.add(cancelPanel);
        commands.add(helpPanel);
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
        if (e.getActionCommand().equals("About PintoSim")) {
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