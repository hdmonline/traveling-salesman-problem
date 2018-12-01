import java.util.ArrayList;

/**
 * Sa.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Solving TSP problem using Simulated Annealing Algorithm
 */

public class Sa {
    private static int[][] matrix;
    private static int numNodes;
    private static ArrayList<Integer> bestTour;
    private static int bestDist;

    /**
     * Constructor. Pass in the number of points for Approx.
     *
     * @param numPoints The size of the distance matrix
     */
    public Sa(int numPoints) {
        numNodes = numPoints;

        // Initialize bestDist.
        bestDist = Integer.MAX_VALUE;

        // Initialize bestTour.
        bestTour = new ArrayList<>();

        // Calculate distance matrix.
        matrix = FileIo.calDistMat(FileIo.getType(), FileIo.getPoints(), numPoints, "LS2");
    }

    public static void run() {
        double elapsedTime;

    }
}
