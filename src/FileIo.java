/**
 * FileIo.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * Helper function for reading dataset and writing results
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileIo {


    private static int numVertices;
    private static Point[] points;
    private static String solutionFile;
    private static String traceFile;
    private static int[][] distMat;

    private static PrintWriter traceWriter;

    /**
     * Read the dataset from input file.
     *
     * @param filepath  The input file path.
     * @return          A graph object
     * @throws IOException
     */
    public static void readFile(String filepath) throws IOException {
        // Open the file and read information about the dataset
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String currLine = "";

        // Get type and number of vertices from the header
        int numInfo = 0;
        String type = "EUR_2D";
        // numVertices = 0;
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
        points = new Point[numVertices];
        for (int i = 0; i < numVertices; i++) {
            currLine = br.readLine();
            points[i] = new Point(
                    Integer.parseInt(currLine.split(" ")[0]),
                    Double.parseDouble(currLine.split(" ")[1]),
                    Double.parseDouble(currLine.split(" ")[2]) );
        }

        // Construct output file names
        composeOutputFileNames();

        // Calculate distance matrix
        calDistMat(type, points, numVertices);
    }

    /**
     * Update the trace result file when having a better result.
     * TODO: Try BufferWriter to make it more efficient
     *
     * @param consumedTime  Current time since starting
     * @param bestDist      Best result so far
     */
    public static void updateTraceFile(double consumedTime, int bestDist) throws IOException {
        traceWriter = new PrintWriter(traceFile, "UTF-8");
        String outputLine = String.format("%.2f", consumedTime) + ", " + bestDist;
        traceWriter.println(outputLine);
    }

    public static void closeWriter() {
        traceWriter.close();
    }

    /**
     * Initialize the distance matrix depending on the edge weight type
     * 
     * @param type          Edge weight type
     * @param points        Array of points
     * @param numVertices   Number of vertices
     * @return              The disctance matrix
     */
    public static void calDistMat(String type, Point[] points, int numVertices) {
        distMat = new int[numVertices][numVertices];

        if (type.equals("EUC_2D")) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < i; j++) {
                    distMat[i][j] = distMat[j][i] = Point.calEucDist(points[i], points[j]);
                }
                distMat[i][i] = -1;
            }
        } else if (type.equals("GEO")) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < i; j++) {
                    distMat[i][j] = distMat[j][i] = Point.calGeoDist(points[i], points[j]);
                }
                distMat[i][i] = -1;
            }
        }
    }

    /**
     * Construct output files based on arguments.
     */
    private static void composeOutputFileNames() {
        String output = Main.getInstName() + "_" + Main.getAlgo() + "_" + Main.getCutoffTime();
        if (Main.isHasSeed()) {
            output += "_" + Main.getSeed();
        }

        // Solution file
        solutionFile = output + ".sol";

        // Trace file
        traceFile = output + ".trace";
    }

    public static int getNumVertices() {
        return numVertices;
    }

    public static int[][] getDistMat() {
        return distMat;
    }
}
