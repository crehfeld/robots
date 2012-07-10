package pintosim;

public class Vertex implements Comparable<Vertex> {

    private String _name;
    private int _X;
    private int _Y;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;

    public Vertex(String argName, int x, int y) {
        _name = argName;
        _X = x;
        _Y = y;
    }

    public void set(String argName, int x, int y) {
        _name = argName;
        _X = x;
        _Y = y;
    }

    public String toString() {
        return _name;
    }

    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }
    
    public int getX() {
        return _X;
    }
    
    public int getY() {
        return _Y;
    }
}