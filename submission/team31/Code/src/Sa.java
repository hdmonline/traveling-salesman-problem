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
    private static int[][] matrix;
    private static int numNodes;
    // Keep track of the bestTour so far
    private static Tour bestTour;
    // The current tour in annealing
    private static Tour  currTour;

    // Annealing parameters
    private static double temperature;
    private static double restartIteration;
    private static final double TIME_CONST = 3.91;
    private static final double FINAL_TEMPERATURE = 1e-5;
    public static final double START_TEMPERATURE = 4;


    // The random number generator
    private static Random random;
    private static double elapsedTime; // Elapsed time
    private static double bestTemperatureTime;
    private static double temperatureTime; // currTime - bestTime + bestTemperatureTIme
    private static long bestTime;
    private static double timeConst;
    private static int restarIterationStart;

    private static final int RESTART_ITERATION_END = 12000;
    private static final int RESTART_ITERATION_START = 400000;

    /**
     * Initializer. Pass in the number of points for Approx.
     *
     * @param numPoints The size of the distance matrix
     */
    public static void init(int numPoints, ArrayList<Integer> initTour) {
        numNodes = numPoints;

        // Random generator with given seed
        random = new Random(Main.getSeed());

        // Calculate distance matrix.
        matrix = FileIo.calDistMat(FileIo.getType(), FileIo.getPoints(), numPoints, "LS2");

        // Initialize bestTour and currTour.
        /* ArrayList<Integer> tourArray = new ArrayList<>(numNodes);
        for (int i = 1; i <= numNodes; i++) {
            tourArray.add(i);
        }
        Collections.shuffle(tourArray, random); */
        bestTour = new Tour(initTour);
        currTour = new Tour(bestTour);

        // Initialize temperature
        temperature = START_TEMPERATURE;

        // Initialize restart iteration
        restartIteration = RESTART_ITERATION_END * 50;
        restarIterationStart = RESTART_ITERATION_END * 50;

        // Initialize time constant
        timeConst = TIME_CONST / (Main.getCutoffTime() * 0.6);

        // Initialize times
        bestTemperatureTime = 0;
        temperatureTime = 0;
        bestTime = System.currentTimeMillis();
    }

    /**
     * The entrance of Sa.
     */
    public static void run() {
        elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;

        int iterationSinceBest = 0;
        while (temperature > FINAL_TEMPERATURE && elapsedTime < Main.getCutoffTime()) {
            // Update the elapsedTime and temperatureTime
            elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;
            temperatureTime = (System.currentTimeMillis() - bestTime) / 1000.0 + bestTemperatureTime;

            // Update temperature
            temperature = START_TEMPERATURE * Math.exp(-timeConst * temperatureTime);

            // Recover the bestTour if it's over the  max allowed iteration
            if (iterationSinceBest > restartIteration) {
                currTour = new Tour(bestTour);
            }

            // Generate new Tour using 2-exchange/4exchange
            Tour adjacentTour = currTour.generateAdjacentTour(1);

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

                    // Update times
                    bestTemperatureTime = temperatureTime;
                    bestTime = System.currentTimeMillis();
                }
            } else {
                double p = random.nextDouble();
                if (p < Math.exp((currTour.getDistance() - adjacentTour.getDistance()) / temperature / currTour.getDistance())) {
                    currTour = new Tour(adjacentTour);
                }
            }

            // Update restart iteration and iteration number
            // restartIteration = restarIterationStart * Math.exp(-timeConst * elapsedTime);
            // linear
            restartIteration = RESTART_ITERATION_START - (RESTART_ITERATION_START - RESTART_ITERATION_END) / Main.getCutoffTime() * elapsedTime;
            iterationSinceBest++;
        }


        // Write solution into .sol file
        FileIo.writeSolution(bestTour.getDistance(), bestTour.getTour());
        System.out.println("Time is up. Exit with the best result so far.");
        System.exit(0);
    }

    public static int[][] getMatrix() {
        return matrix;
    }

    public static int getNumNodes() {
        return numNodes;
    }

    public static Random getRandom() {
        return random;
    }
}
