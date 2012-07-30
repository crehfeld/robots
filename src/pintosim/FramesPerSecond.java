
package pintosim;

import java.awt.Graphics;

// show the fps. i just google copy pasted so not sure how accurate this is but its just temporary for debugging/testing
public class FramesPerSecond implements Paintable {
    long nextSecond = System.currentTimeMillis() + 1000;
    int frameInLastSecond = 0;
    int framesInCurrentSecond = 0;
    public void paint(Graphics g) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > nextSecond) {
            nextSecond += 1000;
            frameInLastSecond = framesInCurrentSecond;
            framesInCurrentSecond = 0;
        }
        framesInCurrentSecond++;
        g.drawString(frameInLastSecond + " fps", 10, 90);
    }
    public int zIndex() {return 55;}
}
