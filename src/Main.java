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
    private static long seed;
    private static long startTime;
    private static boolean verbose;

    public static void main(String[] args) throws IOException {
        // Initialize seed
        seed = -1;

        // Parse arguments
        parseArguments(args);

        // Handle input file
        try {
            FileIo.readFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Throw error if no seed is set from user
        if ((algo.equals("LS1") || algo.equals("LS2")) && seed < 0) {
            System.err.println(algo + " needs a seed!");
            System.exit(1);
        }

        // Entry point for different algorithms
        startTime = System.currentTimeMillis();
        switch (algo) {
            case "BnB":
                Bnb.init(FileIo.getNumVertices());
                Bnb.run();
                break;
            case "Approx":
                Approx approx = new Approx(FileIo.getNumVertices());
                approx.run(true);
                break;
            case "LS1":
                int scale = FileIo.getNumVertices() >= 90 ? 20 : 40;
                double pm = FileIo.getNumVertices() >= 90 ? 0.1 : 0.01;
                Ga.init(FileIo.getNumVertices(), scale , 0.8, pm);
                Ga.run();
                break;
            case "LS2":
                Approx approx4Ls2 = new Approx(FileIo.getNumVertices());
                approx4Ls2.run(false);
                Sa.init(FileIo.getNumVertices(), approx4Ls2.getBestTour());
                Sa.run();
                break;
            default:
                break;
        }
    }

    /**
     * Parse the arguments.
     *
     * @param args Input arguments
     */
    private static void parseArguments(String[] args) {

        String arg;

        verbose = false;

        // The number of input arguments can only be 6 or 8
        if (args.length < 6 && args.length >9) {
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
                    seed = Long.parseLong(args[i++]);
                } else {
                    System.err.println("-time requires a integer");
                    System.exit(1);
                }
            }

            // -verbose
            if (arg.equals("-verbose")) {
                verbose = true;
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

    public static long getSeed() {
        return seed;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static boolean isVerbose() {
        return verbose;
    }
}
