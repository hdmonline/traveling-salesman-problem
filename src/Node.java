/**
 * Node.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * A matrix class for operations on a matrix
 */

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    private int lowerBound;
    private int size;

    private int[][] matrix;
    private int[] rowIdx;
    private int[] colIdx;

    private int[] rowPenalty;
    private int[] colPenalty;

    private int highestPenaltyRow, highestPenaltyCol;
    private int maxPenalty;

    // Visited sub-paths
    private ArrayList<int[]> shortcutHistory = new ArrayList<>();
    private ArrayList<int[]> pathHistory = new ArrayList<>();

    // Final path.
    private ArrayList<Integer> finalPath = new ArrayList<>();

    /**
     * Constructor
     *
     * @param size The number of vertices
     */
    public Node(int size, int[][] matrix) {
        this.size = size;

        // Clone distance matrix
        // TODO: check if cloning is necessary
        this.matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }

        rowIdx = new int[size];
        colIdx = new int[size];

        // Assign row and column indices
        for (int i = 0; i < size; i++) {
            rowIdx[i] = i;
            colIdx[i] = i;
        }

        rowPenalty = new int[size];
        colPenalty = new int[size];

        // Initialize the penalty arrays to -1
        Arrays.fill(rowPenalty, -1);
        Arrays.fill(colPenalty, -1);

        // Initialize lowerBound
        lowerBound = 0;

        // Reduce the matrix
        reduceRow();
        reduceCol();
    }

    /**
     * Copy the matrix input and remove the one row and one col based on input
     *
     * @param parent The unreduced node
     * @param rmRowIdx      The index of the row needs to be removed
     * @param rmColIdx      The index of the column needs to be removed
     */
    public Node(Node parent, int rmRowIdx, int rmColIdx) {
        this.size = parent.getSize() - 1;
        this.lowerBound = parent.getLowerBound();

        matrix = new int[size][size];
        rowIdx = new int[size];
        colIdx = new int[size];

        rowPenalty = new int[size];
        colPenalty = new int[size];

        // Initialize the penalty arrays to -1
        Arrays.fill(rowPenalty, -1);
        Arrays.fill(colPenalty, -1);

        // Copy the reduced matrix
        int row = 0, col = 0;
        for (int i = 0; i < parent.getSize(); i++) {
            if (i != rmRowIdx) {
                rowIdx[row] = parent.getRowIdx(i);
                col = 0;

                for (int j = 0; j < parent.getSize(); j++) {
                    if (j != rmColIdx) {
                        matrix[row][col] = parent.getMatrix(i, j);
                        col++;
                    }
                }
                row++;
            }
        }

        // Assign column indices
        col = 0;
        for (int i = 0; i < parent.getSize(); i++) {
            if (i != rmColIdx) {
                colIdx[col] = parent.getColIdx(i);
                col++;
            }
        }

        // Copy visited sub-paths
        for (int[] path : parent.getShortcutHistory()) {
            shortcutHistory.add(path.clone());
        }
        for (int[] path : parent.getPathHistory()) {
            pathHistory.add(path.clone());
        }

        // Remove the path by checking possible cycles
        int[] newPath = {parent.getRowIdx(rmRowIdx), parent.getColIdx(rmColIdx)};
        pathHistory.add(newPath);
        removeCycle(parent.getRowIdx(rmRowIdx), parent.getColIdx(rmColIdx));

        // Reduce the matrix
        reduceRow();
        reduceCol();
    }

    /**
     * Find possible path in the reduced matrix to form any cycles and set them to -1 (Inf)
     *
     * @param row The index of the removed row
     * @param col The index of the removed column
     */
    private void removeCycle(int row, int col) {
        /*
         * There are two cases for the selected path:
         * 1. It doesn't connect to any of the previous paths (isolated):
         *      It can only form a cycle with the inverse of itself. Mark the inverse path as -1 (Inf).
         * 2. It connects to one of the previous paths:
         *      Need to find the potential path to form the cycle, then mark that path as -1 (Inf).
         */
        boolean isIsolated = false;
        for (int i = 0; i < size; i++) {
            if (rowIdx[i] == col) {
                for (int j = 0; j < size; j++) {
                    if (colIdx[j] == row) {
                        matrix[i][j] = -1;
                        isIsolated = true;
                    }
                }
            }
        }

        if (isIsolated) {
            int[] newPath = {row, col};
            shortcutHistory.add(newPath);
            return;
        } else {
            // The selected path is connected. Find the potential path by using shortcutHistory
            for (int i = 0; i < shortcutHistory.size(); i++) {
                // Make a short cut if the previous path is connected to the selected path
                int[] temp = shortcutHistory.get(i);
                if (temp[1] == row) {
                    temp[1] = col;
                    shortcutHistory.remove(i);
                    this.removeCycle(temp[0], temp[1]);
                    break;
                } else if (temp[0] == col) {
                    temp[0] = row;
                    shortcutHistory.remove(i);
                    this.removeCycle(temp[0], temp[1]);
                    break;
                }
            }
        }
    }

    /**
     * Calculate the penalty of certain column. The column index is based on {@link #matrix} of current object.
     * Update the penalty in the {@link #colPenalty}
     *
     * @param col   The column index (based on {@link #matrix})
     * @return      The penalty of given column
     */
    private int calColPenalty(int col) {
        // If the column has been calculated, return the result.
        if (colPenalty[col] >= 0) {
            return colPenalty[col];
        }

        int numZero = 0;
        int minPenalty = Integer.MAX_VALUE;

        for (int i = 0; i < size; i++) {
            if (matrix[i][col] == 0) {
                numZero++;
                if (numZero > 1) {
                    minPenalty = 0;
                    break;
                }
            } else if (matrix[i][col] > 0 && matrix[i][col] < minPenalty) {
                minPenalty = matrix[i][col];
            }
        }

        colPenalty[col] = minPenalty;
        return minPenalty;
    }

    /**
     * Calculate the penalty of certain row. The row index is based on {@link #matrix} of current object.
     * Update the penalty in the {@link #rowPenalty}
     *
     * @param row   The row index (based on {@link #matrix})
     * @return      The penalty of given row
     */
    private int calRowPenalty(int row) {
        // If the row has been calculated, return the result.
        if (rowPenalty[row] >= 0) {
            return rowPenalty[row];
        }

        int numZero = 0;
        int minPenalty = Integer.MAX_VALUE;

        for (int i = 0; i < size; i++) {
            if (matrix[row][i] == 0) {
                numZero++;
                if (numZero > 1) {
                    minPenalty = 0;
                    break;
                }
            } else if (matrix[row][i] > 0 && matrix[row][i] < minPenalty) {
                minPenalty = matrix[row][i];
            }
        }

        rowPenalty[row] = minPenalty;
        return minPenalty;
    }

    /**
     * Select a path to be branched at based on row and col penalties.
     */
    public void selectBranchPath() {
        maxPenalty = -1;

        // Calculate total penalty for each zero path and
        // keep track of the index of the path with highest panalty.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == 0) {
                    // Check total penalty
                    if (calRowPenalty(i) + calColPenalty(j) > maxPenalty) {
                        // Update
                        maxPenalty = calRowPenalty(i) + calColPenalty(j);
                        highestPenaltyRow = i;
                        highestPenaltyCol = j;
                    }
                }
            }
        }
    }

    /**
     * Find the non-negtive minimal of a row
     *
     * @param row   Request row
     * @return      The minimal of the row
     */
    private int findRowMin(int row) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (matrix[row][i] >= 0 && matrix[row][i] < min) {
                min = matrix[row][i];
            }
        }
        return min;
    }

    /**
     * Find the non-negtive minimal of a column
     *
     * @param col   Request column
     * @return      The minimal of the column
     */
    private int findColMin(int col) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (matrix[i][col] >= 0 && matrix[i][col] < min) {
                min = matrix[i][col];
            }
        }
        return min;
    }

    /**
     * Reduce all rows of the matrix
     */
    public void reduceRow() {
        for (int i = 0; i < size; i++) {
            int rowMin = findRowMin(i);
            //  Subtract rowMin from every row path
            for (int j = 0; j < size; j++) {
                matrix[i][j] -= rowMin;
            }
            // Add rowMin to lowerBound
            lowerBound += rowMin;
        }
    }

    /**
     * Reduce all columns of the matrix
     */
    public void reduceCol() {
        for (int i = 0; i < size; i++) {
            int colMin = findColMin(i);
            // Subtract colMin from every col path
            for (int j = 0; j < size; j++) {
                matrix[j][i] -= colMin;
            }
            // Add colMin to lowerBound
            lowerBound += colMin;
        }
    }

    /**
     * Calculate the solution when the matrix is 2 x 2.
     */
    public void calSolution() {
        int[] newPath1 = new int[2], newPath2 = new int[2];
        if (matrix[0][0] >= 0 && matrix[0][1] >= 0 && matrix[1][0] >= 0 && matrix[1][1] >= 0) {
            if (matrix[0][1] + matrix[1][0] <= matrix[0][0] + matrix[1][1]) {
                lowerBound += matrix[0][1] + matrix[1][0];
                newPath1[0] = rowIdx[0];
                newPath1[1] = colIdx[1];
                newPath2[0] = rowIdx[1];
                newPath2[1] = colIdx[0];
            } else {
                lowerBound += matrix[0][0] + matrix[1][1];
                newPath1[0] = rowIdx[0];
                newPath1[1] = colIdx[0];
                newPath2[0] = rowIdx[1];
                newPath2[1] = colIdx[1];
            }
        } else if (matrix[0][1] >= 0 && matrix[1][0] >= 0) {
            lowerBound += matrix[0][1] + matrix[1][0];
            newPath1[0] = rowIdx[0];
            newPath1[1] = colIdx[1];
            newPath2[0] = rowIdx[1];
            newPath2[1] = colIdx[0];
        } else if (matrix[0][0] >= 0 && matrix[1][1] >= 0) {
            lowerBound += matrix[0][0] + matrix[1][1];
            newPath1[0] = rowIdx[0];
            newPath1[1] = colIdx[0];
            newPath2[0] = rowIdx[1];
            newPath2[1] = colIdx[1];
        } else {
            System.err.println("No feasible solution can be found. Something is wrong!");
            System.exit(2);
        }
        // Add path to history
        pathHistory.add(newPath1);
        pathHistory.add(newPath2);

        // Calculate the final path
        calFinalPath();
    }

    /**
     * Construct the final path from the {@link #pathHistory}
     */
    private void calFinalPath() {
        // Debug: check if the pathHistroy.size() == size;
        if (pathHistory.size() != Bnb.getSize()) {
            System.err.println("Path history has wrong size!");
            System.exit(2);
        }

        int[] first = pathHistory.get(0);
        finalPath.add(first[0]);
        finalPath.add(first[1]);
        pathHistory.remove(0);
        int tail = first[1];
        while (pathHistory.size() > 1) {
            for (int i = 0; i < pathHistory.size(); i++) {
                int[] temp = pathHistory.get(i);
                if (temp[0] == tail) {
                    tail = temp[1];
                    finalPath.add(tail);
                    pathHistory.remove(i);
                    break;
                }
            }
        }
    }

    public boolean transToLeftBranch() {
        // TODO: don't understand this
        if (size == highestPenaltyCol) {
            System.out.println("Left branch failed!");
            return false;
        }

        lowerBound += maxPenalty;
        matrix[highestPenaltyRow][highestPenaltyCol] = -1;

        // Reduce matrix by only unpdating elements on highest penalty row and column
        for (int i = 0; i < size; i++) {
            matrix[highestPenaltyRow][i] -= calRowPenalty(highestPenaltyRow);
            matrix[i][highestPenaltyCol] -= calColPenalty(highestPenaltyCol);
        }

        return true;
    }


    public int getLowerBound() {
        return lowerBound;
    }

    public int getSize() {
        return size;
    }

    public int getRowIdx(int idx) {
        return rowIdx[idx];
    }

    public int getColIdx(int idx) {
        return colIdx[idx];
    }

    public int getMatrix(int i, int j) {
        return matrix[i][j];
    }

    public ArrayList<int[]> getShortcutHistory() {
        return shortcutHistory;
    }

    public ArrayList<int[]> getPathHistory() {
        return pathHistory;
    }

    public ArrayList<Integer> getFinalPath() {
        return finalPath;
    }

    public int getHighestPenaltyRow() {
        return highestPenaltyRow;
    }

    public int getHighestPenaltyCol() {
        return highestPenaltyCol;
    }
}
