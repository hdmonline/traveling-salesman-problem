The source code for this project contains 4 main parts:
1. Main class to parse the input arguments and run the corresponding algorithm with parameters.
2. Algorithm classes that processes each algorithm individually.
3. Miscellaneous classes to be used by the algorithms.
4. File I/O class to read the input and write the result to the input/output files.

Running the codes:
java -jar team31.jar -inst <instance_path> -time <cutoff_time> -seed <seed> -algo <BnB|Approx|LS1|LS2>

Note: For BnB, if "-time" option is not set or set to 0, the program will run until finding the exact solution.