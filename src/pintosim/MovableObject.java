package pintosim;
import java.util.List;
import java.util.ArrayList;

public class MovableObject {
    private String name;
    private int x, y;
    private EnviornmentMap enviornmentMap;
    
    public MovableObject() {
        this.name = "";
        this.x = -1;
        this.y = -1;
    }
    public MovableObject(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public String getName() {
        return name;
    }
    
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        enviornmentMap.updateLocation(this);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
