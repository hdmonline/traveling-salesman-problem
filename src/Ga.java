/**
 * Ga.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Solving TSP problem using Genetic Algorithm
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Ga {

    private static int scale;
    private static int numPoints;
    private static int[][] matrix;
    private static int bestT;
    private static int bestDist;
    private static ArrayList<Integer> bestTour;

    private static ArrayList<ArrayList<Integer>> prevPopulation;
    private static ArrayList<ArrayList<Integer>> newPopulation;
    private static int[] fitness; // The total distance of the tour. The smaller the better.

    private static double[] probCumulative; // The cumulative probability of each chromosome to be selected
    private static double pc;
    private static double pm;
    private static int t;

    private static Random random;
    private static double elapsedTime;

    public Ga(int scale, int numPoints, double pc, double pm) {
        // Initialize parameters
        this.scale = scale;
        this.numPoints = numPoints;
        this.pc = pc;
        this.pm = pm;

        // Records of the best results so far
        bestDist = Integer.MAX_VALUE;
        bestTour = new ArrayList<>();;
        bestT = 0;
        t = 0;

        // Populations
        newPopulation = new ArrayList<>(scale);
        prevPopulation = new ArrayList<>(scale);

        // Initialize populations
        for (int i = 0; i < scale; i++) {
            newPopulation.add(new ArrayList<>(numPoints));
            prevPopulation.add(new ArrayList<>(numPoints));
        }
        fitness = new int[scale];
        probCumulative = new double[scale];

        // Random generator with given seed
        random = new Random(Main.getSeed());

        // Calculate distance matrix
        matrix = FileIo.calDistMat(FileIo.getType(), FileIo.getPoints(), numPoints, "LS1");
    }

    public static void run() {
        // Initialize population
        initPopulation();

        // Calculate the cumulative probabilities for each chromosome in the previous population
        calProbs();

        // Initialize elapsedTime
        elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;

        // Run until the cutoff time is reached
        while(elapsedTime < Main.getCutoffTime()) {
            // Update results
            updateResult();

            // Selection
            select();

            // Evolution
            evolution();

            updatePopulation();

            // Calculate new fitness for new population
            calFitness();

            // Update the elapsedTime
            elapsedTime = (System.currentTimeMillis() - Main.getStartTime()) / 1000.0;
        }

        FileIo.writeSolution(bestDist, bestTour);
        System.out.println("Time is up. Exit with the best result so far.");
        System.exit(0);
    }

    /**
     * Initialize {@link #prevPopulation}.
     */
    private static void initPopulation() {
        for (int i = 0; i < scale; i++) {
            // Initialize chromosome with index in ascent order
            for (int j = 0; j < numPoints; j++) {
                prevPopulation.get(i).add(j+1);
            }
            Collections.shuffle(prevPopulation.get(i), random);
        }

        // Calculate fitness for each chromosome
        calFitness();
    }

    private static void calFitness() {
        for (int i = 0; i < scale; i++) {
            fitness[i] = evaluate(prevPopulation.get(i));
        }
    }

    /**
     * Calculate the total distance of a chromosome.
     *
     * @param chromosome    The chromosome
     * @return              The total distance
     */
    private static int evaluate(ArrayList<Integer> chromosome) {
        int len = 0;
        for (int i = 1; i < numPoints; i++) {
            len += matrix[chromosome.get(i-1) - 1][chromosome.get(i) - 1];
        }

        len += matrix[chromosome.get(numPoints-1) - 1][chromosome.get(0) - 1];
        return len;
    }

    /**
     * Calculate cumulative probabilities for each chromosome.
     */
    private static void calProbs() {
        double probSum = 0;
        double[] probSelect = new double[scale];

        for (int i = 0; i < scale; i++) {
            probSelect[i] = 10.0 / fitness[i];
            probSum += probSelect[i];
        }

        probCumulative[0] = probSelect[0] / probSum;
        for (int i = 1; i < scale; i++) {
            probCumulative[i] = probSelect[i] / probSum + probCumulative[i-1];
        }
    }

    /**
     * Select the best chromosome in {@link #prevPopulation} and update the best results so far.
     */
    @SuppressWarnings("unchecked")
    private static void updateResult() {
        // Find the chromosome with smallest fitness (best fit)
        t++;
        int bestChromIdx = 0;
        int bestFitness = fitness[0];

        for (int i = 1; i < scale; i++) {
            if (fitness[i] < bestFitness) {
                bestFitness = fitness[i];
                bestChromIdx = i;
            }
        }

        // Update bestDist
        if (bestFitness < bestDist) {
            bestDist = bestFitness;
            bestT = t;
            bestTour = (ArrayList<Integer>) prevPopulation.get(bestChromIdx).clone();

            // Print out updated results
            System.out.println("Best so far: " + bestDist + "\tElapsed time: " + elapsedTime + "\tGeneration: " + t);
            System.out.println("Best tour so far: " + bestTour);

            try {
                FileIo.updateTraceFile(elapsedTime, bestDist);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Put the best chromosome into next generation
        retainChrom(bestChromIdx, 0);
    }

    /**
     * Retain one of the previous chromosome and put it into next generation.
     *
     * @param prev  The position of the previous chromosome in {@link #prevPopulation}
     * @param next  The position of the chromosome in {@link #newPopulation}
     */
    @SuppressWarnings("unchecked")
    private static void retainChrom(int prev, int next) {
        newPopulation.set(next, (ArrayList<Integer>) prevPopulation.get(prev).clone());
    }

    /**
     * Selection. Select each chromosome of the {@link #newPopulation} from the {@link #prevPopulation}.
     */
    private static void select() {
        double rand;
        for (int i = 1; i < scale; i++) {
            // Choose one of the chromosome from prevPopulation by using fitness proportionate selection
            rand = random.nextDouble();
            int j;
            for (j = 0; j < scale; j++) {
                if (rand <= probCumulative[j]) {
                    break;
                }
            }
            retainChrom(j, i);
        }
    }

    /**
     * Crossover.
     *
     * @param idx1  The index of the first parent chromosome
     * @param idx2  The index of the second parent chromosome
     */
    private static void crossover(int idx1, int idx2) {
        ArrayList<Integer> chrom1 = newPopulation.get(idx1);
        ArrayList<Integer> chrom2 = newPopulation.get(idx2);

        ArrayList<Integer> nChrom1 = new ArrayList<>(numPoints);
        ArrayList<Integer> nChrom2 = new ArrayList<>(numPoints);

        int rand1 = random.nextInt(numPoints);
        int rand2 = random.nextInt(numPoints);
        while(rand1 == rand2) {
            rand2 = random.nextInt(numPoints);
        }

        // Make sure that rand1 < rand2
        if (rand1 > rand2) {
            int tmp = rand1;
            rand1 = rand2;
            rand2 = tmp;
        }

        // Move the 3rd segment of chrom2 to nChrom2
        int i, j, k;
        for (i = 0, j = rand2; j < numPoints; i++, j++) {
            nChrom2.add(chrom1.get(j));
        }

        // Put (chrom2 \ chrom1_3) into the rest of nChrom2. After this, nChrom2 is generated.
        for (k = 0, j = i; j < numPoints; k++) {
            if (nChrom2.contains(chrom2.get(k))) {
                continue;
            }
            nChrom2.add(chrom2.get(k));
            j++;
        }

        // Put (chrom1 \ chrom2_1) into nChrom1 (start from the beginning).
        List<Integer> chrom2part1 = chrom2.subList(0, rand1);
        for (i = 0; i < numPoints; i++) {
            if (chrom2part1.contains(chrom1.get(i))) {
                continue;
            }
            nChrom1.add(chrom1.get(i));
        }

        // Put chrom2_1 into the rest of nChrom1
        for (i = 0; i < rand1; i++) {
            nChrom1.add(chrom2.get(i));
        }

        // Put new chromosomes back into population
        newPopulation.set(idx1, nChrom1);
        newPopulation.set(idx2, nChrom2);
    }

    private static void mutation(int chromIdx) {
        ArrayList<Integer> chrom = newPopulation.get(chromIdx);
        int rand1 = random.nextInt(numPoints);
        int rand2 = random.nextInt(numPoints);
        while (rand1 == rand2) {
            rand2 = random.nextInt(numPoints);
        }

        // Swap two points
        int tmp = chrom.get(rand1);
        chrom.set(rand1, chrom.get(rand2));
        chrom.set(rand2, tmp);
    }

    private static void evolution() {
        for (int i = 1; i + 1 < scale; i += 2) {
            double rand = random.nextDouble();
            if (rand < pc) {
                crossover(i, i + 1);
            } else {
                rand = random.nextDouble();
                if (rand < pm) {
                    mutation(i);
                }
                rand = random.nextDouble();
                if (rand < pm) {
                    mutation(i+1);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void updatePopulation() {
        prevPopulation = (ArrayList<ArrayList<Integer>>) newPopulation.clone();
    }
}
