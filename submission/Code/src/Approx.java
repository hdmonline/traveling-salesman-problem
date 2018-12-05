/**
 * Approx.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Solving TSP problem using MST-Approx
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Approx {
    private static int[][] matrix; // Distance matrix
    private static int numNodes; // Size of matrix
    private static ArrayList<Integer> bestTour; // Best tour
    private static ArrayList<Integer> currTour; // Tour for each node as root
    private static int bestDist; // Total distance calculated from the best result

    /**
     * Constructor. Pass in the number of points for Approx.
     *
     * @param numPoints The size of the distance matrix
     */
    public Approx(int numPoints) {
        numNodes = numPoints;

        // Initialize bestDist.
        bestDist = Integer.MAX_VALUE;

        // Initialize bestTour and currTour.
        bestTour = new ArrayList<>();
        currTour = new ArrayList<>();

        // Calculate distance matrix.
        matrix = FileIo.calDistMat(FileIo.getType(), FileIo.getPoints(), numPoints, "Approx");
    }

    /**
     * The entrance of Approx
     */
    @SuppressWarnings("unchecked")
    public void run(boolean writeOutput) {
        double elapsedTime; // Elapsed time

        // Iterate through all the node as root.
        for (int i = 0; i < numNodes; i++) {
            currTour.clear();
            int[] parents = prim(i);
            preOrder(i, parents);
            int currDist = calDist();
            if (currDist < bestDist) {
                bestDist = currDist;
                bestTour = (ArrayList<Integer>) currTour.clone();

                elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;

                // Print out updated results
                if (Main.isVerbose() && writeOutput) {
                    System.out.println("Best so far: " + bestDist + "\tElapsed time: " + elapsedTime);
                    System.out.println("Best tour so far: " + bestTour);
                }

                if (writeOutput){
                    try {
                        FileIo.updateTraceFile(elapsedTime, bestDist);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (writeOutput) {
            FileIo.writeSolution(bestDist, bestTour);
        }
    }

    /**
     * Calculate the MST using Prim's algorithm.
     *
     * @param startNode The starting node. (The root of the tree)
     * @return The MST in the form of an array. Each element's index is its parent
     */
    private int[] prim(int startNode) {
        int[] attachmentCost = new int[numNodes]; // The minimum distance from each node to MST set
        int[] visitedNodes = new int[numNodes]; // The order of visited node
        int[] parents = new int[numNodes]; // The closest node from MST set to each node

        // Keep track of the order of attaching nodes.
        int attachingSequence = 0;

        Arrays.fill(parents, startNode);

        for (int i = 0; i < numNodes; i++) {
            // MST set only has startNode
            attachmentCost[i] = matrix[startNode][i];

            // Set all nodes unvisited
            visitedNodes[i] = -1;
        }

        // Put startNode into MST set as the root of the tree
        visitedNodes[startNode] = attachingSequence;

        for (int i = 0; i < numNodes - 1; i++) {
            int minCost = Integer.MAX_VALUE;
            int attachingNode = -1;

            // Find the minimum cost edge and corresponding node connected to MST set
            for (int j = 0; j < numNodes; j++) {
                if (visitedNodes[j] == -1 && attachmentCost[j] < minCost) {
                    minCost = attachmentCost[j];
                    attachingNode = j;
                }
            }

            if (attachingNode != -1) {
                visitedNodes[attachingNode] = ++attachingSequence;

                // Update attachmentCost for each node
                for (int j = 0; j < numNodes; j++) {
                    if (visitedNodes[j] == -1 && matrix[attachingNode][j] >= 0 && matrix[attachingNode][j] < attachmentCost[j]) {
                        attachmentCost[j] = matrix[attachingNode][j];
                        parents[j] = attachingNode;
                    }
                }
            }
        }
        return parents;
    }

    /**
     * A recursive method to pre-order the tree.
     *
     * @param node Given node as a parent
     * @param parents Tree array
     */
    private void preOrder(int node, int[] parents) {
        currTour.add(node + 1);
        Queue<Integer> children = findChildren(node, parents);
        while(!children.isEmpty()) {
            preOrder(children.remove(), parents);
        }
    }

    /**
     * Find the children for given node.
     *
     * @param node Node index
     * @param parents Tree array
     * @return  The children
     */
    private Queue<Integer> findChildren(int node, int[] parents) {
        Queue<Integer> children = new LinkedList<>();
        for (int i = 0; i < parents.length; i++) {
            if (node != i && node == parents[i]) {
                children.add(i);
            }
        }
        return children;
    }

    /**
     * Calculate the distance for {@link #bestTour}.
     *
     * @return The total distance
     */
    private int calDist() {
        int totalDist = 0;
        for (int i = 0; i < numNodes - 1; i++) {
            totalDist += matrix[currTour.get(i)-1][currTour.get(i+1)-1];
        }
        totalDist += matrix[currTour.get(numNodes-1)-1][currTour.get(0)-1];
        return totalDist;
    }

    public ArrayList<Integer> getBestTour() {
        return bestTour;
    }
}
