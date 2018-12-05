/**
 * Point.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * A Point class for representing points in the dataset
 */

public class Point {
    private static final double R = 6378.388;
    private static final double PI = 3.141592;

    private double xLatitude;
    private double yLongitude;

    /**
     * Constructor
     *
     * @param x The x coordinate or latitude
     * @param y The y coordinate or longitude
     */
    public Point(double x, double y) {
        this.xLatitude = x;
        this.yLongitude = y;
    }


    public double getxLatitude() {
        return xLatitude;
    }

    public double getyLongitude() {
        return yLongitude;
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
     * Calculate the geo distance between 2 points.
     *
     * @param p1 Point 1
     * @param p2 Point 2
     * @return Distance between 2 points
     */
    public static int calGeoDist(Point p1, Point p2) {
        double q1 = Math.cos(deg2Rad(p1.getyLongitude()) - deg2Rad(p2.getyLongitude()));
        double q2 = Math.cos(deg2Rad(p1.getxLatitude()) - deg2Rad(p2.getxLatitude()));
        double q3 = Math.cos(deg2Rad(p1.getxLatitude()) + deg2Rad(p2.getxLatitude()));
        return (int) (R * Math.acos( 0.5 * ( (1.0+q1)*q2 - (1.0-q1)*q3 ) ) + 1.0);
    }

    /**
     * Convert the degree to radians in required way.
     *
     * @param deg   Degree number
     * @return      Radians
     */
    private static double deg2Rad(double deg) {
        int intDeg = (int) deg;
        double fractional = deg - intDeg;
        return Math.PI * (intDeg + 5.0 * fractional / 3.0) / 180.0;
    }
}
