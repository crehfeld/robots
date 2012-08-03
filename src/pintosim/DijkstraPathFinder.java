
package pintosim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;


public class DijkstraPathFinder implements PathFinder {
    private int width, height;
    private Vertex[][] vertices;
    private static final int UNOBSTRUCTED_EDGE_WEIGHT  = 5;
    private static final int   OBSTRUCTED_EDGE_WEIGHT  = 8000;
    private EnvironmentMap environmentMap;
    
    private boolean[][] freeSpaces;//true means unobstructed
    
    private boolean sourcePathComputed = false;
    
    public DijkstraPathFinder(EnvironmentMap environmentMap) {
        this.environmentMap = environmentMap;
    }
    
    
    
    /**
     * Sets up the vertices and edges
     */
    private void populate() {
        freeSpaces = environmentMap.getWalkabilityMatrix();
        width  = freeSpaces.length;
        height = freeSpaces[0].length;
        vertices = new Vertex[width][height];

        // initialize a vertex for every position in the matrix
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                vertices[x][y] = new Vertex(
                    String.format("vert(%d, %d)", x, y)
                  , x
                  , y
                 );
            }
        }
        
        // set up edges with proper weights between vertices
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                vertices[x][y].adjacencies = makeEdgesForAllVerticesAdjacentTo(x, y);
            }
        }
    }
    
    
    
    /*
     * make edges directed at the verts above, below, left, and right, of the vert at location x,y.
     * like this:
     * 
     *  X  
     * XOX 
     *  X
     * 
     * where O is the vert at x,y, and we makes edges to the verts at the X's
     */
    private Edge[] makeEdgesForAllVerticesAdjacentTo(int x, int y) {
        ArrayList<Edge> edges = new ArrayList<Edge>();

        // directed edge to vert to the left
        if (withinBounds(x - 1, y)) {
            edges.add(makeEdgeToVertexAt(x - 1, y));
        }

        // directed edge to vert to the right
        if (withinBounds(x + 1, y)) {
            edges.add(makeEdgeToVertexAt(x + 1, y));
        }
        
        // directed edge to the vert above
        if (withinBounds(x, y - 1)) {
            edges.add(makeEdgeToVertexAt(x, y - 1));
        }
        
        // directed edge to the vert below
        if (withinBounds(x, y + 1)) {
            edges.add(makeEdgeToVertexAt(x, y + 1));
        }
        
        return edges.toArray(new Edge[]{});
    }
    
    
    private Edge makeEdgeToVertexAt(int x, int y) {
        Vertex edgeTarget = vertices[x][y];
        int edgeWeight = freeSpaces[x][y] ? UNOBSTRUCTED_EDGE_WEIGHT : OBSTRUCTED_EDGE_WEIGHT;
        return new Edge(edgeTarget, edgeWeight);
    }
    
    
    private boolean withinBounds(int x, int y) {
        return x >= 0
            && x < width
            && y >= 0
            && y < height;
    }
   
    /**
     * 
     * these 2 methods could be called by EnvironmentMap when an update occurs
     * but we dont current do it because we just do a complete teardown+setup
     * for each path calculation
     */
    public void spaceUnoccupied(int x, int y) {
        markClearZoneAround(vertices[x][y]);
    }
    
    public void spaceOccupied(int x, int y) {
        markCollisionZoneAround(vertices[x][y]);
    }

    private void markCollisionZoneAround(Vertex danger) {
        for (Edge e : danger.adjacencies) {
            e.weight = OBSTRUCTED_EDGE_WEIGHT;
        }
    }

    private void markClearZoneAround(Vertex clear) {
        for (Edge e : clear.adjacencies) {
            e.weight = UNOBSTRUCTED_EDGE_WEIGHT;
        }
    }
    
   
    public void computePathsFrom(Point from) {
        
        populate();
        
        
        Vertex source = vertices[from.x][from.y];
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.adjacencies) {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);
                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    vertexQueue.add(v);
                }
            }
        }
    }
    
    
    public List<Point> getPath(Point from, Point to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException(String.format(
                "The from and to location cannot be the same(%d, %d)."
              , from.x
              , from.y
            ));
        }
        
        computePathsFrom(from);
        return getShortestPathTo(to);
    }
    
    
    

    
    public List<Point> getShortestPathTo(Point to) {
        Vertex target = vertices[to.x][to.y];
        List<Point> path = new ArrayList<Point>();
        for (Vertex vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(new Point(vertex.getX(), vertex.getY()));
        }
        Collections.reverse(path);
        return path;
    }
    

    
    //makes ascii art like display of the path. 
    public String printPath(List<Point> path) {
        String[][] grid = new String[width][height];
        for (Point p : path) {
            grid[p.x][p.y] = "O";
        }
        
        String buf = "";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buf += grid[x][y] ==  null ? "." : "O";
            }
            
            buf += "\n";
        }
        
        return buf;
    }
}
