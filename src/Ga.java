/**
 * Ga.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Solving TSP problem using Genetic Algorithm
 */


public class Ga {

    private static int MAXX_GEN;

    private int scale;
    private int numCity;
    private int[][] matrix;
    private int bestT;
    private int bestDist;
    private int[] bestTour;

    private int[][] prevPopulation;
    private int[][] newPopulation;
}
