/**
 * Bnb.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Solving TSP problem using Branch and Bound algorithm
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Bnb {
    private static int size; // Number of vertices
    private static int[][] matrix; // Distance matrix

    private static Stack<Node> stackBnb = new Stack<>(); // The stack of nodes to keep track of the processed nodes
    private static long bestDist; // Total distance calculated from the best result
    private static ArrayList<Integer> bestTour; // Best tour

    /**
     * Constructor. Pass in the size of the matrix.
     *
     * @param numV
     */
    public Bnb(int numV) {
        size = numV;
        bestDist = Integer.MAX_VALUE;

        // Calculate distance matrix
        matrix = FileIo.calDistMat(FileIo.getType(), FileIo.getPoints(), size, "BnB");
    }

    /**
     * The main function of BnB.
     *
     * @throws IOException
     */
    public static void run() throws IOException {
        // Initial the root node
        Node root = new Node(size, matrix);

        // Elapsed time
        double elapsedTime;

        stackBnb.push(root);
        while (!stackBnb.isEmpty()) {
            // Check if the time is up. Write the best result to the output files
            elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;
            if (elapsedTime > Main.getCutoffTime() && Main.getCutoffTime() > 0) {
                FileIo.writeSolution(bestDist, bestTour);
                System.out.println("Time is up. Exit with the best result so far.");
                System.exit(3);
            }

            Node processedNode = stackBnb.pop();

            // Calculate the solution for 2 x 2 matrix
            if (processedNode.getSize() == 2) {
                processedNode.calSolution();

                // Update bestTour
                if (processedNode.getLowerBound() < bestDist) {
                    bestDist = processedNode.getLowerBound();
                    bestTour = processedNode.getFinalPath();
                    elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;
                    FileIo.updateTraceFile(elapsedTime, bestDist);

                    // Print out updated results
                    if (Main.isVerbose()) {
                        System.out.println("Best so far: " + bestDist + "\t" + "Elapsed time: " + elapsedTime);
                        System.out.println("Best tour so far: " + bestTour);
                    }

                }
            } else if (processedNode.getLowerBound() < bestDist) {
                // Branch the node if it is better than lowerBound
                processedNode.selectBranchPath();

                // Right branch
                Node rightBrach = new Node(processedNode, processedNode.getHighestPenaltyRow(), processedNode.getHighestPenaltyCol());

                // Left branch
                boolean isContinue = processedNode.transToLeftBranch();

                // Stack the node into the stack
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

        FileIo.writeSolution(bestDist, bestTour);
    }

    public static int getSize() {
        return size;
    }
}
