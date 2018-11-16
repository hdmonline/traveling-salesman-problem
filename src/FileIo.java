/**
 * FileIo.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Helper function for reading dataset and writing results
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileIo {

    private static final double R = 6378.388;

    public static Graph readFile(String filepath) throws IOException {
        // Get instance name from the file name
        File file = new File(filepath);
        String filename = file.getName();
        String instName = filepath.split("\\.")[0];

        // Open the file and read information about the dataset
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String currLine = "";

        // Get type and number of vertices from the header
        int numInfo = 0;
        String type = "EUR_2D";
        int numVertices = 0;
        while (!currLine.equals("NODE_COORD_SECTION")) {
            currLine = br.readLine();
            String[] tokens = currLine.split(" ");
            if (tokens.length >= 2) {
                if (tokens[0].equals("DIMENSION:")) {
                    numVertices = Integer.parseInt(tokens[1]);
                    numInfo++;
                }

                if (tokens[0].equals("EDGE_WEIGHT_TYPE:")) {
                    type = tokens[1];
                    numInfo++;
                }
            }
        }

        // Check if type and number of vertices have been got from the header
        if (numInfo != 2) {
            System.err.println("Can't get edge weight type or dimension from the file header.");
            System.exit(1);
        }

        // Read x-y coordinates or latitude-longitude
        double[] xLatitude = new double[numVertices], yLongitude = new double[numVertices];
        for (int i = 0; i < numVertices; i++) {
            currLine = br.readLine();
            xLatitude[i] = Double.parseDouble(currLine.split(" ")[1]);
            yLongitude[i] = Double.parseDouble(currLine.split(" ")[2]);
        }

        // Calculate distance matrix
        int[][] distMat = getDistMat(type, xLatitude, yLongitude, numVertices);

        // Return the graph
        return new Graph(instName, numVertices, distMat);
    }

    /**
     * Initialize the distance matrix depending on the edge weight type
     * 
     * @param type Edge weight type
     * @param xLatitude x coordinates or latitudes
     * @param yLongitude y cocrdinates or longitudes
     * @param numVertices Number of vertices
     * @return The disctance matrix
     */
    private static int[][] getDistMat(String type, double[] xLatitude, double[] yLongitude, int numVertices) {
        int[][] res = new int[numVertices][numVertices];

        if (type.equals("EUR_2D")) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j <= i; j++) {
                    res[i][j] = res[j][i] = calcEucDist(xLatitude[i], xLatitude[j], yLongitude[i], yLongitude[j]);
                }
                // res[i][i] = Integer.MAX_VALUE;
            }
        } else if (type.equals("GEO")) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j <= i; j++) {
                    res[i][j] = res[j][i] = calcGeoDist(xLatitude[i], xLatitude[j], yLongitude[i], yLongitude[j]);
                }
                // res[i][i] = Integer.MAX_VALUE;
            }
        }


        return res;
    }

    /**
     * Calculate the Euclidean distance between 2 nodes
     *
     * @param x1 x coordinate of node 1
     * @param x2 x coordinate of node 2
     * @param y1 y coordinate of node 1
     * @param y2 y coordinate of node 2
     * @return Distance between 2 nodes
     */
    private static int calcEucDist(double x1, double x2, double y1, double y2) {
        return (int) Math.round(Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)));
    }

    /**
     * Calculate the geo distance between 2 nodes
     *
     * @param lat1 latitude of node 1
     * @param lat2 latitude of node 2
     * @param long1 longitude of node 1
     * @param long2 longitude of node 2
     * @return Distance between 2 nodes
     */
    private static int calcGeoDist(double lat1, double lat2, double long1, double long2) {
        return (int) Math.round(R * Math.acos( Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(Math.abs(long1 - long2))));
    }
}
