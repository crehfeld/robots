/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pintosim;

public class Edge {

    public Vertex target;
    public double weight;

    public Edge(Vertex argTarget, double argWeight) {
        target = argTarget;
        weight = argWeight;
    }
}