
package pintosim;

import java.io.*;
import java.util.*;


/*
 this program rapidly polls a file for changes, reprinting the file contents to stdout only once per file modification.
 * the intention is to run this from a shell, and let the shell scroll each time new output is printed. it should 
 * make an animation effect if the shell window is sized correctly.
 */
public class CommandLineMapAnimator {
    private static final String FILENAME = "dynamic-output.txt";
    private static final int POLL_FREQUENCY_MS = 80;
    
    
    
    public static void main(String[] args) {
        RandomAccessFile tmp = null;
        try {
            tmp = new RandomAccessFile(FILENAME, "r");
        } catch (Exception e) {}
        
        final RandomAccessFile file = tmp;
        
        
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            String previouData = null;
            String currentData = null;
            byte[] bytes = new byte[100000];
            int frame = 0;
            public void run() {
                try {
                    file.seek(0);
                    int bytesRead = file.read(bytes);
                    if (bytesRead == -1) {
                        return;
                    }
                    

                    currentData = new String(bytes, 0, bytesRead);
                    
                    //only print it if its different
                    if (!currentData.equals(previouData)) {
                        System.out.println("Frame#" + frame++);
                        System.out.println(currentData);
                        previouData = currentData;
                    }
                    

                } catch (IOException e) {
                    cancel();
                    e.printStackTrace();
                }
            }
        };
        
        
        
        t.scheduleAtFixedRate(task, 0, POLL_FREQUENCY_MS);
        
        
    }
    

    
    
    
    
    
    
    
    
    
    
}
