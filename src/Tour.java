import java.util.ArrayList;

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

    public ArrayList<Integer> getTour() {
        return tour;
    }

    public int getDistance() {
        return distance;
    }
}
