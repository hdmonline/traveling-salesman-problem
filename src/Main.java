/**
 * Main.java
 * @author Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * The main entry point for CSE6140 final project
 */
public class Main {

    private static String filename;
    private static int algo;
    private static int cutoffTime;
    private static int seed;

    public static void main(String[] args) {
        // Parse arguments
        parseArguments(args);

        // Entry point for different algorithms
        switch (algo) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
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
            System.err.println("Usage: -inst <filename> -algo [BnB | Approx | LS1 | LS2] -time <cutoff_in_seconds> [-seed <random_seed>]");
            System.exit(1);
        }

        // Iterate through the arguments
        int i = 0;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            // -inst
            if (arg.equals("-inst")) {
                if (i < args.length) {
                    filename = args[i++];
                } else {
                    System.err.println("-inst requires a filename");
                    System.exit(1);
                }
            }

            // -algo
            if (arg.equals("-algo")) {
                if (i < args.length) {
                    String algoName = args[i++];
                    switch (algoName) {
                        case "BnB":
                            algo = 1;
                            break;
                        case "Approx":
                            algo = 2;
                            break;
                        case "LS1":
                            algo = 3;
                            break;
                        case "LS2":
                            algo = 4;
                            break;
                        default:
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
                    seed = Integer.parseInt(args[i++]);
                } else {
                    System.err.println("-time requires a integer");
                    System.exit(1);
                }
            }
        }
    }
}
