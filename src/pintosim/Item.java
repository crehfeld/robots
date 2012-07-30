
package pintosim;

public class Item extends MovableObject {
    private String name;
    
    public Item(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

}