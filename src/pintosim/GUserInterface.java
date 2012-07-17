package pintosim;
import javax.swing.*;
import java.awt.Event;

public class GUserInterface {

    public GUserInterface() {

    }

    public void init() {
        frame.setJMenuBar(menu);

    }

    public void displayFrame() {
    
    }

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JFrame frame = new JFrame("PintoSim");
    private JMenuBar menu = new JMenuBar();
}
