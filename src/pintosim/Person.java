package pintosim;

public class Person extends Item {
    
    public Person(String name, int x, int y) {
        super(name, x, y);
    }
    
    public int getXCoOrdinate() {
        return super.getX();
    }
    
    public int getYCoOrdinate() {
        return super.getY();
    }
}
