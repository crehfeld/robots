package pintosim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

enum PINTO_STATUS {

    IDLE,
    BUSY
}

public class Pinto extends MovableObject {

    private String _itemName;
    private EnviornmentMap _map;
    private PintoManager _pintoManager;
    private Point _myCurrentLocation;
    private PINTO_STATUS _status;
    private List<Point> _path;
    private Command command;

    public PINTO_STATUS getStatus() {
        return this._status;
    }

    public Pinto(int pintoId,
            EnviornmentMap map,
            PintoManager pintoManager) {

        super("Pinto_" + pintoId, 0, 0);
        this._itemName = "";
        this._map = map;
        this._pintoManager = pintoManager;
        this._status = PINTO_STATUS.BUSY;
        this._path = null;
        this._myCurrentLocation = new Point(0, 0);
    }

    public String getItemWorkingOn() {
        return _itemName;
    }

    public void setStatus(PINTO_STATUS status) {
        _status = status;
    }

    public void getItem(Command cmd) {
        command = cmd;
        this._itemName = command.getItemName().toLowerCase();

        Point ds = _map.getPintoDockingStationLocation();
        Point elderly = _map.getPersonLocation();
        Point item = _map.getItemLocation(_itemName);
        if (ds == null || elderly == null || item == null) {
            String e = "No Pinto out fetching " + _itemName.toLowerCase();
            throw new NullPointerException(e);
        }

        PathFinder dsToItemPathFinder = new PathFinder(_map);
        dsToItemPathFinder.computePathsFrom(ds);
        List<Point> path = dsToItemPathFinder.getShortestPathTo(item);
        try {
            _path = new ArrayList<Point>();
            _path.addAll(path);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
        //System.out.print(dsToItemPathFinder.printPath(path));

        PathFinder itemToElderlyPathFinder = new PathFinder(_map);
        itemToElderlyPathFinder.computePathsFrom(item);
        path = itemToElderlyPathFinder.getShortestPathTo(elderly);
        try {
            _path.addAll(path);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
        //System.out.print(itemToElderlyPathFinder.printPath(path));

        PathFinder elderlyToDsPathFinder = new PathFinder(_map);
        elderlyToDsPathFinder.computePathsFrom(elderly);
        path = elderlyToDsPathFinder.getShortestPathTo(ds);
        try {
            _path.addAll(path);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }

        //System.out.print(elderlyToDsPathFinder.printPath(path));
        // _pathFinder.printMap();
        Iterator<Point> it = _path.listIterator();
        while (it.hasNext()) {
            _myCurrentLocation = it.next();
            //command.onPintoMove(_myCurrentLocation);
        }
        command.onComplete(_path);
        _pintoManager.setTaskStatus(_itemName, PintoManager.TaskStatus.COMPLETE);
        setStatus(PINTO_STATUS.IDLE);
    }

    public void cancelItem(Command cmd) {
        command.onCancel(_myCurrentLocation);
        PathFinder itemToDsPathFinder = new PathFinder(_map);
        Point ds = _map.getPintoDockingStationLocation();
        Point item = _myCurrentLocation;
        try {
            itemToDsPathFinder.computePathsFrom(item);
            List<Point> path = itemToDsPathFinder.getShortestPathTo(ds);

            _path.addAll(path);
            command.onComplete(_path);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return;
        }
    }
}
