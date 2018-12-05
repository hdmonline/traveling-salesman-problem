# Travelling Salesman Problem
This is the final project of CSE6140 in GaTech, 2018 Fall.

The Travaling Salesperson Problem (TSP) arises in numerous applications such as vehicle routing, circuit board drilling, VLSI design, robot control, X-ray crystallography, machine scheduling and computational biology. In this project, we attempted to solve the TSP using different algorithms, evaluating  their theoretical and experimental complexities on both real and random datasets.

## Background
TSP problem can be defined as follows: given the x-y coordinates of N points in the plane, and a cost function c(u,v) defined for every pair of points, find the shoutest simple cycle that visits all points. The cost function is defined as either the Euclidean or Geographic distance between points u and v. 

See [TSPLIB](https://www.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/) for details.
 
## Algorithms
The project solves the TSP by using 4 different algorithms:
* Exact Algorithm using Branch-and-Bound
* MST-Approximation Algorithm
* Genetic Algorithm
* Simulated Annealing

## Running the Code
### Synopsis
```
java -jar tsp.jar -inst <filename> 
                  -algo [BnB|Approx|LS1|LS2] 
                  -time <cutoff_in_seconds> 
                  [-seed <random_seed>] 
                  [-verbose]
```
### Options
**-inst <filename>**  
The filename of the dataset.


**-algo [BnB|Approx|LS1|LS2]**  
The algorithm to use to solve the TSP.


**-time <cutoff_in_seconds>**  
The cut-off time.  

**-seed <random_seed>**  
A random seed.  


**-verbose**  
Updating results in console.  

## Output
### Solution files:  
* File name:  
`<instance>_<method>_<cutoff>[_<random_seed>].sol`
* File format:  
  * Line1: Best solution found
  * Line2: List of vertex IDs of the TSP tour  
  
### Solution trace file:  
* File name:  
`<instance>_<method>_<cutoff>[_<random_seed>].trace`
* File format: each line has two values (comma-separated)    
  * A timestamp in seconds
  * Best found solution at that point in time
