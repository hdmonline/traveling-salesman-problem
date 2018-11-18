/**
 * Point.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * A Point class for representing points in the dataset
 */

public class Point {
    private static final double R = 6378.388;

    private int id;
    private double xLatitude;
    private double yLongitude;

    /**
     * Constructor
     *
     * @param id Point id
     * @param x The x coordinate or latitude
     * @param y The y coordinate or longitude
     */
    public Point(int id, double x, double y) {
        this.id = id;
        this.xLatitude = x;
        this.yLongitude = y;
    }

    public int getId() {
        return id;
    }

    public double getxLatitude() {
        return xLatitude;
    }

    public double getyLongitude() {
        return yLongitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setxLatitude(double xLatitude) {
        this.xLatitude = xLatitude;
    }

    public void setyLongitude(double yLongitude) {
        this.yLongitude = yLongitude;
    }

    /**
     * Calculate the Euclidean distance between 2 points
     *
     * @param p1 Point 1
     * @param p2 Point 2
     * @return Distance between 2 points
     */
    public static int calEucDist(Point p1, Point p2) {
        return (int) Math.round(Math.sqrt(Math.pow(p1.getxLatitude()-p2.getxLatitude(), 2) + Math.pow(p1.getyLongitude()-p2.getyLongitude(), 2)));
    }

    /**
     * Calculate the geo distance between 2 points
     * TODO: This is not correct for some reason
     *
     * @param p1 Point 1
     * @param p2 Point 2
     * @return Distance between 2 points
     */
    public static int calGeoDist(Point p1, Point p2) {
//        return (int) Math.floor(R * Math.acos( Math.sin(p1.getxLatitude()) * Math.sin(p2.getxLatitude()) +
//                Math.cos(p1.getxLatitude()) * Math.cos(p2.getxLatitude()) * Math.cos(Math.abs(p1.getyLongitude() - p2.getyLongitude()))));
        double q1 = Math.cos(p1.getyLongitude() - p2.getyLongitude());
        double q2 = Math.cos(p1.getxLatitude() + p2.getxLatitude());
        double q3 = Math.cos(p1.getxLatitude() + p2.getxLatitude());
        return (int) Math.floor(R * Math.acos( 0.5 * ( (1.0 + q1)*q2 - (1.0-q1)*q3 ) ) + 1.0 );
    }
}
