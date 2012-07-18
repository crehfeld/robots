package pintosim;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;

/**
 * Provides a graphical front end to PintoSim.
 *
 * @author PlzSendTheCodes team
 */
public class GUI implements ActionListener, ItemListener {

    /**
     * Constructs a GUI object.
     * @param interpreter parses a command
     * @param map the map of the environment
     * @param pintoManager manages Pintos
     */
    public GUI(CommandParser interpreter, EnviornmentMap map, PintoManager pintoManager) {

        this.interpreter = interpreter;
        this.map = map;
        this.pintoManager = pintoManager;

        frame.setJMenuBar(menu);
        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(helpMenu);
        menu.add(aboutMenu);

        // File menu
        JMenuItem note = new JMenuItem("Note a location");
        JMenuItem retrieve = new JMenuItem("Retrieve an item");
        fileMenu.add(note);
        fileMenu.add(retrieve);

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

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("About PintoSim")) {
            JOptionPane.showMessageDialog(frame,
                    "(c) PlzSendTheCodes team\n" +
                            "\t\t\t\t\t\t\t\t 2012",
                    "About PintoSim",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

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