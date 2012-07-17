package pintosim;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PintoManager {

    public enum TaskStatus {

        REGISTER, QUEUED, STARTED, COMPLETE
    }
    // members
    private int _uniquePintoId;
    private int _maxAvailablePintos;
    private int _availablePintos;
    private List<Pinto> _pintos;
    private EnviornmentMap _map;
    private Map<String, TaskStatus> _taskStatusMap;
    private Map<String, Command> itemNameCommandMap = new HashMap<String, Command>();

    // member functions
    public PintoManager(EnviornmentMap map) {
        _uniquePintoId = 1;
        _maxAvailablePintos = 5;
        _availablePintos = _maxAvailablePintos;
        _pintos = new ArrayList<Pinto>();
        _taskStatusMap = new ConcurrentHashMap<String, TaskStatus>();
        _map = map;
    }

    public void setMaxAvailablePintos(int maxAvailablePintos) {
        _maxAvailablePintos = maxAvailablePintos;
        _availablePintos = maxAvailablePintos;
    }

    public void updateAvailablePintos() {
        if (!_pintos.isEmpty()) {
            Iterator<Pinto> it = _pintos.iterator();
            while (it.hasNext()) {
                Pinto pinto = it.next();
                if (pinto.getStatus() == PINTO_STATUS.IDLE) {
                    _availablePintos++;
                }
            }
        }
    }

    public void doWork() {
        Iterator it = _taskStatusMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TaskStatus> pairs = (Map.Entry) it.next();
            String itemName = pairs.getKey();
            if (pairs.getValue() == TaskStatus.QUEUED) {
                Pinto pinto = getIdlePinto();
                if (pinto != null) {
                    Command cmd = itemNameCommandMap.get(itemName);
                    setTaskStatus(itemName, TaskStatus.STARTED);
                    pinto.getItem(cmd);
                }
            }
        }
    }

    public Pinto getPintoWorkingOn(String item) {
        // All Pinto have been put to work, get first IDLE pinto
        if (_pintos.isEmpty()) {
            return null;
        }

        Pinto pinto = null;
        Iterator<Pinto> it = _pintos.iterator();
        while (it.hasNext()) {
            pinto = it.next();
            String itemName = pinto.getItemWorkingOn();
            if (item.equalsIgnoreCase(itemName)) {
                break;
            }
        }
        return (pinto);
    }

    public Pinto getIdlePinto() {
        if (_availablePintos == 0) {
            return null;
        }
        Pinto pinto = null;
        if (_uniquePintoId <= _maxAvailablePintos) {
            // Unused Pintos Are Available
            pinto = new Pinto(_uniquePintoId, _map, this);
            _uniquePintoId++;
            _availablePintos--;
            _pintos.add(pinto);
        } else {
            // All Pinto have been put to work, get first IDLE pinto
            if (!_pintos.isEmpty()) {
                Iterator<Pinto> it = _pintos.iterator();
                while (it.hasNext()) {
                    pinto = it.next();
                    if (pinto.getStatus() == PINTO_STATUS.IDLE) {
                        pinto.setStatus(PINTO_STATUS.BUSY);
                        break;
                    }
                }
                _availablePintos--;
            }
        }
        return pinto;
    }

    public boolean canImmediatelyFulfillATask() {
        return _availablePintos > 0;
    }

    public boolean taskExistsFor(Item item) {
        TaskStatus status = getTaskStatus(item.getName().toLowerCase());
        if (status == null || status == TaskStatus.COMPLETE) {
            return false;
        }
        return true;
    }

    public TaskStatus getTaskStatus(String itemName) {
        return _taskStatusMap.get(itemName.toLowerCase());
    }

    public void setTaskStatus(String itemName, TaskStatus status) {
        if (itemName == null) {
            String e = "No itemName provided to set status ";
            throw new NullPointerException(e);
        }
        switch (status) {
            case COMPLETE:
                updateAvailablePintos();
                _taskStatusMap.remove(itemName);
                break;
            default:
                _taskStatusMap.put(itemName, status);
                break;
        }
    }

    public void addCommand(Command cmd) {

        String itemName = cmd.getItemName().toLowerCase();
        if (itemName == null) {
            String e = "No item associated with command ";
            throw new NullPointerException(e);
        }

        TaskStatus status = getTaskStatus(itemName);
        if (status == null) {
            setTaskStatus(itemName, TaskStatus.REGISTER);
            itemNameCommandMap.put(itemName, cmd);
        }
        switch (cmd.getType()) {
            case GET_ITEM:
                switch (getTaskStatus(itemName)) {
                    case STARTED:
                        Pinto pinto = getPintoWorkingOn(itemName);
                        if (pinto == null) {
                            String e = "No Pinto out fetching "
                                    + itemName.toLowerCase();
                            throw new NullPointerException(e);
                        }
                        String msg = "Pinto "
                                + pinto.getName()
                                + "is already out fetching item "
                                + itemName;

                        cmd.onDisplay(msg);
                        break;
                    case QUEUED:
                        msg = "identical get_item "
                                + itemName
                                + " request is in the queue "
                                + " and waiting for the next available pinto.";
                        cmd.onDisplay(msg);
                        break;
                    case REGISTER:
                        setTaskStatus(itemName, TaskStatus.QUEUED);
                        break;
                    default:
                        throw new UnsupportedOperationException("unknown task status type");
                }
                break;
            case CANCEL_GET_ITEM:
                switch (status) {
                    case QUEUED:
                        setTaskStatus(itemName, TaskStatus.COMPLETE);
                        break;
                    case STARTED:
                        Pinto pinto = getPintoWorkingOn(itemName);
                        if (pinto == null) {
                            String e = "No Pinto out fetching " + itemName.toLowerCase();
                            throw new NullPointerException(e);
                        }
                        pinto.cancelItem(cmd);
                        setTaskStatus(itemName, TaskStatus.COMPLETE);
                        break;
                    default:
                        throw new UnsupportedOperationException("unexpected task state");
                }
                break;
            default:
                throw new UnsupportedOperationException("unknown command type");
        }
    }
}
