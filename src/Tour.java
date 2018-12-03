import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tour.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Tour class
 */


public class Tour {
    private ArrayList<Integer> tour;
    private int distance;

    /**
     * Constructor
     *
     * @param tour The tour
     */
    @SuppressWarnings("unchecked")
    public Tour(ArrayList<Integer> tour) {
        this.tour = (ArrayList<Integer>) tour.clone();
        calDist();
    }

    public Tour(Tour tour) {
        this.tour = new ArrayList<Integer>(Sa.getNumNodes());
        for (int node : tour.getTour()) {
            this.tour.add(node);
        }
        this.distance = tour.getDistance();
    }

    /**
     * Calculate the distance for {@link #tour}.
     *
     * @return The total distance
     */
    public void calDist() {
        int totalDist = 0;
        for (int i = 0; i < Sa.getNumNodes() - 1; i++) {
            totalDist += Sa.getMatrix()[tour.get(i)-1][tour.get(i+1)-1];
        }
        totalDist += Sa.getMatrix()[tour.get(Sa.getNumNodes()-1)-1][tour.get(0)-1];
        distance = totalDist;
    }

    /**
     * Generate the next adjacent tour using 2-exchange/4-exchange.
     *
     * @param opt 0 for 2-exchange, 1 for 4-exchange
     * @return The adjacent tour
     */
    public Tour generateAdjacentTour(int opt) {
        switch (opt) {
            case 0:
                return generateAdjacentTour2();
            case 1:
                return generateAdjacentTour4();
            default:
                System.err.println("Wrong option for generating new Tour!");
                System.exit(2);
        }
        return null;
    }

    /**
     * Generate the next adjacent tour using 2-exchange.
     *
     * @return The adjacent tour
     */
    public Tour generateAdjacentTour2() {
        // 2-exchange
        int rand1 = Sa.getRandom().nextInt(tour.size());
        int rand2 = Sa.getRandom().nextInt(tour.size());
        while (rand1 == rand2) {
            rand2 = Sa.getRandom().nextInt(tour.size());
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
    }

    /**
     * Generate the next adjacent tour using 4-exchange.
     *
     * @return The adjacent tour
     */
    public Tour generateAdjacentTour4() {
        // 4-exchange
        ArrayList<Integer> rands = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            int tempRand = Sa.getRandom().nextInt(tour.size());
            while (rands.contains(tempRand)) {
                tempRand = Sa.getRandom().nextInt(tour.size());
            }
            rands.add(tempRand);
        }

        // Sort generated random numbers
        Collections.sort(rands);

        // The tour of the new Tour
        ArrayList<Integer> newtour = new ArrayList<>(tour.size());

        /*
         *  ---------------------------------------------------
         *  |   0-1   |   3-4   |   2-3   |   1-2   |   4-N   |
         *  ---------------------------------------------------
         */
        for (int i = 0; i < rands.get(0); i++) {
            newtour.add(tour.get(i));
        }

        for (int i = rands.get(2); i < rands.get(3); i++) {
            newtour.add(tour.get(i));
        }

        for (int i = rands.get(1); i < rands.get(2); i++) {
            newtour.add(tour.get(i));
        }

        for (int i = rands.get(0); i < rands.get(1); i++) {
            newtour.add(tour.get(i));
        }

        for (int i = rands.get(3); i < tour.size(); i++) {
            newtour.add(tour.get(i));
        }

        // Update the distance
        Tour newTour = new Tour(newtour);
        return newTour;
    }

    public ArrayList<Integer> getTour() {
        return tour;
    }

    public int getDistance() {
        return distance;
    }
}
