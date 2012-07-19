package pintosim;

import javax.swing.*;
import java.awt.event.*;

/**
 * Provides a graphical front end to PintoSim.
 *
 * @author PlzSendTheCodes team
 */
public class GUI implements ActionListener, ItemListener {

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

        frame.setJMenuBar(menu);
        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(helpMenu);
        menu.add(aboutMenu);

        //Buttons
        JButton location = new JButton("Note a Location");
        location.setBounds(0, 0, 30, 15);
        JButton retrieve = new JButton("Retrieve an item");
        retrieve.setLocation(20,0);
        frame.add(location);
        //frame.add(retrieve);

        // Edit menu
        JMenuItem chooseMap = new JMenuItem("Load Map");
        editMenu.add(chooseMap);
        editMenu.addActionListener(this);

        // Help menu
        JMenuItem helpDesk = new JMenuItem("Call Help Desk");
        helpMenu.add(helpDesk);

        // About menu
        JMenuItem aboutTeam = new JMenuItem("About PintoSim");
        aboutMenu.add(aboutTeam);
        aboutTeam.addActionListener(this);

        // General frame stuff
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Manages actions on a object
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("About PintoSim")) {
            JOptionPane.showMessageDialog(frame,
                    "Designed by PlzSendTheCodes team\n",
                    "About PintoSim",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //
    public void itemStateChanged(ItemEvent e) {

    }

    // GUI objects
    private JFrame frame = new JFrame("PintoSim");
    private JMenuBar menu = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu editMenu = new JMenu("Edit");
    private JMenu helpMenu = new JMenu("Help");
    private JMenu aboutMenu = new JMenu("About");

    // Backend objects
    private CommandParser interpreter;
    private EnviornmentMap map;
    private PintoManager pintoManager;
    private Command potentialGetItemCancelationCommand;
}