/**
 * Bnb.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * A Bnb class with distance matrix
 */

import java.util.PriorityQueue;
import java.util.Stack;

public class Bnb {
    private int numVertices; // Number of vertices
    private int[][] distMat; // Distance matrix
    public static int[][] reducedMat;

//    private PriorityQueue<IntegerPair> pq = new PriorityQueue<IntegerPair>();
    private Stack<Node> stackBnb = new Stack<>();
    private int size;

    // Constructor
    public Bnb(String name, int numVertices, int[][] distMat) {
        this.numVertices = numVertices;
        this.distMat = distMat;
    }

}
