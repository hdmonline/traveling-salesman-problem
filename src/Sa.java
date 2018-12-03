/**
 * Sa.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Solving TSP problem using Simulated Annealing Algorithm
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sa {
    private static final int RESTART_ITERARION = 500;
    private static int[][] matrix;
    private static int numNodes;
    // Keep track of the bestTour so far
    private static Tour bestTour;
    // The current tour in annealing
    private static Tour currTour;

    // Annealing parameters
    private static double temperature = 1500;
    private static final double DELTA = 0.99;
    private static final double FINAL_TEMPERATURE = 1e-5;

    // The random number generator
    private static Random random;
    private static double elapsedTime; // Elapsed time

    /**
     * Constructor. Pass in the number of points for Approx.
     *
     * @param numPoints The size of the distance matrix
     */
    public Sa(int numPoints) {
        numNodes = numPoints;

        // Random generator with given seed
        random = new Random(Main.getSeed());

        // Calculate distance matrix.
        matrix = FileIo.calDistMat(FileIo.getType(), FileIo.getPoints(), numPoints, "LS2");

        // Initialize bestTour and currTour.
        ArrayList<Integer> tourArray = new ArrayList<>(numNodes);
        for (int i = 1; i <= numNodes; i++) {
            tourArray.add(i);
        }
        Collections.shuffle(tourArray, random);
        bestTour = new Tour(tourArray);
        currTour = new Tour(bestTour);

    }

    /**
     * The entrance of Sa.
     */
    // TODO: restart
    public static void run() {
        elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;

        int iterationSinceBest = 0;
        while (temperature > FINAL_TEMPERATURE || elapsedTime < Main.getCutoffTime()) {
            Tour adjacentTour = generateAdjacentTour(currTour);

            if (adjacentTour.getDistance() < currTour.getDistance()) {
                currTour = new Tour(adjacentTour);
                if (currTour.getDistance() < bestTour.getDistance()) {
                    bestTour = new Tour(currTour);

                    // Print out updated results
                    if (Main.isVerbose()) {
                        System.out.println("Best so far: " + bestTour.getDistance() + "\tElapsed time: " + elapsedTime);
                        System.out.println("Best tour so far: " + bestTour.getTour());
                    }

                    // Update the trace file
                    try {
                        FileIo.updateTraceFile(elapsedTime, bestTour.getDistance());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Clear the iteration counter
                    iterationSinceBest = 0;
                }
            } else {
                double p = random.nextDouble();
                if (Math.exp((adjacentTour.getDistance() - currTour.getDistance()) / temperature) > p) {
                    currTour = new Tour(adjacentTour);
                }
            }

            // Update temperature and iteration number
            temperature *= DELTA;
            iterationSinceBest++;

            // Update the elapsedTime
            elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;
        }

        // Write solution into .sol file
        FileIo.writeSolution(bestTour.getDistance(), bestTour.getTour());
        System.out.println("Time is up. Exit with the best result so far.");
        System.exit(0);
    }

    /**
     * Generate the next adjacent tour.
     *
     * @param tour The current tour
     * @return The adjacent tour
     */
    public static Tour generateAdjacentTour(Tour tour) {
        // 2-exchange
        int rand1 = random.nextInt(numNodes);
        int rand2 = random.nextInt(numNodes);
        while (rand1 == rand2) {
            rand2 = random.nextInt(numNodes);
        }

        // Make sure that rand1 < rand2
        if (rand1 > rand2) {
            int tmp = rand1;
            rand1 = rand2;
            rand2 = tmp;
        }

        Tour newTour = new Tour(tour);
        List<Integer> subtour = newTour.getTour().subList(rand1, rand2);
        Collections.reverse(subtour);

        // Update the distance
        newTour.calDist();
        return newTour;
        // TODO: 4-exchange

    }

    public static int[][] getMatrix() {
        return matrix;
    }

    public static int getNumNodes() {
        return numNodes;
    }
}
