/**
 * Bnb.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * A Bnb class with distance matrix
 */

import java.util.ArrayList;
import java.util.Stack;

public class Bnb {
    private static int numVertices; // Number of vertices
    private static int[][] distMat; // Distance matrix
    public static int[][] reducedMat;

    private static Stack<Node> stackBnb = new Stack<>();
    private static int size;
    private static int bestDist;
    private static ArrayList<Integer> bestTour;

    // Constructor
    public Bnb(int numV, int[][] distMat) {
        numVertices = numV;
        this.distMat = distMat;
    }

    public static void run() {
        bestDist = Integer.MAX_VALUE;

        // Initial the root node
        Node root = new Node(size, distMat);

        stackBnb.push(root);
        while (!stackBnb.isEmpty()) {
            Node processedNode = stackBnb.pop();

            if (processedNode.getSize() == 2) {
                processedNode.calSolution();

                // Update bestTour;
                if (processedNode.getLowerBound() < bestDist) {
                    bestDist = processedNode.getLowerBound();
                    bestTour = processedNode.getFinalPath();

                }
            } else if (processedNode.getLowerBound() < bestDist) {
                processedNode.selectBranchPath();

                // Right branch
                Node rightBrach = new Node(processedNode, processedNode.getHighestPenaltyRow(), processedNode.getHighestPenaltyCol());

                // Left branch
                boolean isContinue = processedNode.transToLeftBranch();

                if (isContinue) {
                    Node leftBranch = processedNode;

                    if (rightBrach.getLowerBound() <= leftBranch.getLowerBound()) {
                        stackBnb.push(leftBranch);
                        stackBnb.push(rightBrach);
                    } else {
                        stackBnb.push(rightBrach);
                        stackBnb.push(leftBranch);
                    }
                } else {
                    stackBnb.push(rightBrach);
                }
            }
        }
    }

    public static int getNumVertices() {
        return numVertices;
    }
}
