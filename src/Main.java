/**
 * Main.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * The main entry point for CSE6140 final project
 */

import java.io.File;
import java.io.IOException;

public class Main {

    private static String filePath;
    private static String instName;
    private static String algo;
    private static int cutoffTime;
    private static boolean hasSeed;
    private static int seed;
    private static long startTime;
    private static long currTime;

    // TODO: cutoff time
    public static void main(String[] args) throws IOException {
        // Parse arguments
        parseArguments(args);

        // Handle input file
        try {
            FileIo.readFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Entry point for different algorithms
        switch (algo) {
            case "BnB":
                Bnb bnb = new Bnb(FileIo.getNumVertices(), FileIo.getDistMat());
                startTime = System.currentTimeMillis();
                Bnb.run();
                break;
            case "Approx":
                break;
            case "LS1":
                break;
            case "LS2":
                break;
            default:

        }
    }

    /**
     * Parse the arguments.
     *
     * @param args Input arguments
     */
    private static void parseArguments(String[] args) {

        String arg;

        // The number of input arguments can only be 6 or 8
        if (args.length != 6 && args.length != 8) {
            System.err.println("Usage: -inst <filePath> -algo [BnB | Approx | LS1 | LS2] -time <cutoff_in_seconds> [-seed <random_seed>]");
            System.exit(1);
        }

        // Iterate through the arguments
        int i = 0;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            // -inst
            if (arg.equals("-inst")) {
                if (i < args.length) {
                    filePath = args[i++];
                    // Get instance name from the file name
                    File file = new File(filePath);
                    instName = file.getName().split("\\.")[0];
                } else {
                    System.err.println("-inst requires a filePath");
                    System.exit(1);
                }
            }

            // -algo
            if (arg.equals("-algo")) {
                if (i < args.length) {
                    algo = args[i++];
                    if (!algo.equals("BnB") && !algo.equals("Approx") &&
                            !algo.equals("LS1") && !algo.equals("LS2")) {
                        throw new IllegalArgumentException("Not a valid algorithm name: " + args[i-1]);
                    }
                } else {
                    System.err.println("-algo requires a algorithm name");
                    System.exit(1);
                }
            }

            // -time
            if (arg.equals("-time")) {
                if (i < args.length) {
                    cutoffTime = Integer.parseInt(args[i++]);
                } else {
                    System.err.println("-time requires a integer");
                    System.exit(1);
                }
            }

            // -seed
            if (arg.equals("-seed")) {
                if (i < args.length) {
                    hasSeed = true;
                    seed = Integer.parseInt(args[i++]);
                } else {
                    System.err.println("-time requires a integer");
                    System.exit(1);
                }
            }
        }
    }

    public static String getInstName() {
        return instName;
    }

    public static String getAlgo() {
        return algo;
    }

    public static int getCutoffTime() {
        return cutoffTime;
    }

    public static boolean isHasSeed() {
        return hasSeed;
    }

    public static int getSeed() {
        return seed;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static long getCurrTime() {
        return currTime;
    }

    public static void setCurrTime(long currTime) {
        Main.currTime = currTime;
    }
}
