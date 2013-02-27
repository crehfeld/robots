package pintosim;

public class Vertex implements Comparable<Vertex> {

    private String _name;
    private int _X;
    private int _Y;
    private Edge[] adjacencies;
    private double minDistance = Double.POSITIVE_INFINITY;
    private Vertex previous;

    public Vertex(String argName, int x, int y) {
        _name = argName;
        _X = x;
        _Y = y;
    }

    public void set(String argName, int x, int y) {
        setName(argName);
        setX(x);
        setY(y);
    }

    public String toString() {
        return getName();
    }

    public int compareTo(Vertex other) {
        return Double.compare(getMinDistance(), other.getMinDistance());
    }
    
    public int getX() {
        return _X;
    }
    
    public int getY() {
        return _Y;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @param X the _X to set
     */
    public void setX(int X) {
        this._X = X;
    }

    /**
     * @param Y the _Y to set
     */
    public void setY(int Y) {
        this._Y = Y;
    }

    /**
     * @return the adjacencies
     */
    public Edge[] getAdjacencies() {
        return adjacencies;
    }

    /**
     * @param adjacencies the adjacencies to set
     */
    public void setAdjacencies(Edge[] adjacencies) {
        this.adjacencies = adjacencies;
    }

    /**
     * @return the minDistance
     */
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * @param minDistance the minDistance to set
     */
    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * @return the previous
     */
    public Vertex getPrevious() {
        return previous;
    }

    /**
     * @param previous the previous to set
     */
    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }
}