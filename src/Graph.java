/**
 * Graph.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * A Graph class with distance matrix
 */
public class Graph {
    // Graph name
    private String name;
    // Number of vertices
    private int numVertices;
    // Distance matrix
    private int[][] distMat;

    // The lower bound of the cost
    private int lowerBound;
    // The upper bound of the cost
    private int upperBound;



    // Constructor
    public Graph(String name, int numVertices, int[][] distMat) {
        this.name = name;
        this.numVertices = numVertices;
        this.distMat = distMat;
    }

}
