package pintosim;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class PintoManager {

    public enum TaskStatus {
        QUEUED
      , STARTED
      , ITEM_BEING_CARRIED
      , COMPLETE
    }
    
    private class Task {
        Command command;
        TaskStatus status;
        Pinto pinto;
        List<Point> path = new ArrayList<Point>();
        boolean paused = false;
        
        Task(Command cmd) {
            command = cmd;
            status = TaskStatus.QUEUED;
        }
    }
    
    private List<Pinto> pintos = new ArrayList<Pinto>();
    private EnviornmentMap map;
    private Queue<Task> queuedTasks = new ConcurrentLinkedQueue<Task>();
    private List<Task> runningTasks = new CopyOnWriteArrayList<Task>();
    private List<Task> completedTasks = new CopyOnWriteArrayList<Task>();


    public PintoManager(EnviornmentMap map) {
        this.map = map;
    }
    
    public void addPinto(Pinto pinto) {
        pintos.add(pinto);
    }
    
    
    public void work() {
        assignQueuedTasksToIdlePintos();
        for (Task task : runningTasks) {
            if (!task.paused) {
                task.pinto.moveALittleBit();
            }
        }
    }
    
    private Pinto getIdlePinto() {
        for (Pinto pinto : pintos) {
            if (pinto.isIdle()) {
                return pinto;
            }
        }
        
        return null;
    }
    
    public boolean canImmediatelyFulfillATask() {
        return getIdlePinto() != null;
    }

    public TaskStatus getTaskStatus(String itemName) {
        Task task = null;
        task = getRunningTaskFor(itemName);
        if (task == null) {
            task = getQueuedTaskFor(itemName);
            if (task == null) {
                task = getCompletedTaskFor(itemName);
            }
        }
        
        return task == null ? null : task.status;
    }
    
    
    private boolean completedTaskExistsFor(String itemName) {
        return getCompletedTaskFor(itemName) != null;
    }
    
    public boolean completedTaskExistsFor(Item item) {
        return completedTaskExistsFor(item.getName());
    }

    private boolean uncompletedTaskExistsFor(String itemName) {
        return getRunningTaskFor(itemName) != null || getQueuedTaskFor(itemName) != null;
    }
    
    public boolean uncompletedTaskExistsFor(Item item) {
        return uncompletedTaskExistsFor(item.getName());
    }
    
    private boolean isItemBeingCarried(String itemName) {
        Task task = getRunningTaskFor(itemName);
        if (task == null) {
            return false;
        }
        return task.status == TaskStatus.ITEM_BEING_CARRIED;
    }
    
    public boolean isItemBeingCarried(Item item) {
        return isItemBeingCarried(item.getName());
    }
    
    
    
    private void addItemRetrievalCommand(Command cmd) {
        queuedTasks.add(new Task(cmd));
    }
    
    private void assignQueuedTasksToIdlePintos() {
        Iterator<Task> it = queuedTasks.iterator();
        while (it.hasNext()) {
            Pinto pinto = getIdlePinto();
            if (pinto == null) {
                break;
            }
            Task task = it.next();
            task.status = TaskStatus.STARTED;
            task.pinto = pinto;
            //move the task to the running task list
            it.remove();
            runningTasks.add(task);
            pinto.getItem(task.command);
        }
    }
    
    private Task getRunningTaskFor(String itemName) {
        for (Task task : runningTasks) {
            if (task.command.getItemName().equals(itemName)) {
                return task;
            }
        }
        
        return null;
    }
    
    private Task getQueuedTaskFor(String itemName) {
        for (Task task : queuedTasks) {
            if (task.command.getItemName().equals(itemName)) {
                return task;
            }
        }
        
        return null;
    }
    
    private Task getCompletedTaskFor(String itemName) {
        for (Task task : completedTasks) {
            if (task.command.getItemName().equals(itemName)) {
                return task;
            }
        }
        
        return null;
    }
    
    
    public void pauseTaskIfRunning(Item item) {
        Task task = getRunningTaskFor(item.getName());
        if (task == null) {
            return;
        }
        
        task.paused = true;
    }
    
    public void unPauseTask(Item item) {
        Task task = getRunningTaskFor(item.getName());
        if (task == null) {
            return;
        }
        
        task.paused = false;
    }
    

    
    private void cancelTaskFor(String itemName) {
        Task task = null;
        
        task = getQueuedTaskFor(itemName);
        if (task != null) {
            queuedTasks.remove(task);
            completedTasks.add(task);
            task.status = TaskStatus.COMPLETE;
            return;
        }
        
        task = getRunningTaskFor(itemName);
        if (task != null) {
            task.pinto.abortItemRetrieval();
            task.paused = false;//it may have been paused, need to unpause so pinto can go home
            return;
        }
        

        
    }
    
    
    public void addCommand(Command cmd) {
        String itemName = cmd.getItemName();
        if (itemName == null) {
            String e = "No item associated with command ";
            throw new NullPointerException(e);
        }
        
        switch (cmd.getType()) {
            case GET_ITEM:
                addItemRetrievalCommand(cmd);
                break;
            case CANCEL_GET_ITEM:
                cancelTaskFor(itemName);
                break;
            default:
                throw new UnsupportedOperationException("unknown command type");
        }
        
    }
    
    
    public void setTaskStatus(String itemName, TaskStatus status) {
        
        Task task = null;
        task = getRunningTaskFor(itemName);
        task.status = status;
        
        switch (status) {
            case COMPLETE:
                runningTasks.remove(task);
                completedTasks.add(task);
                break;
        } 
    }
    

    
    
    
    
    
    
    
    
    
    
}
