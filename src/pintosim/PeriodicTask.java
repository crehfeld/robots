/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pintosim;

import java.util.*;

/**
 *
 * @author abais
 */
public class PeriodicTask extends TimerTask {

    private PintoManager _pintoManager;

    public PeriodicTask(PintoManager pintoManager) {
        this._pintoManager = pintoManager;
    }

    @Override
    public void run() {
        try {
           _pintoManager.doWork(); 
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
    }
}
