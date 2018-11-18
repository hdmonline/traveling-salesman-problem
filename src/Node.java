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
    int lowerBound;
    int size;

    int[][] matrix;
    int[] rowIdx;
    int[] colIdx;

    int[] rowPenalty;
    int[] colPenalty;

    int highestPenaltyRowIndex, highestPenaltyColIndex;
    int maxPenalty;

    // Visited sub-paths
    ArrayList<int[]> pathHistory = new ArrayList<>();

    /**
     * Constructor
     *
     * @param size The number of vertices
     */
    public Node(int size) {
        this.size = size;

        matrix = new int[size][size];
        rowIdx = new int[size];
        colIdx = new int[size];

        rowPenalty = new int[size];
        colPenalty = new int[size];

        // Initialize the penalty arrays to -1
        Arrays.fill(rowPenalty, -1);
        Arrays.fill(colPenalty, -1);
    }

    /**
     * Copy the matrix input and remove the one row and one col based on input
     *
     * @param unreducedNode The unreduced node
     * @param rmRowIdx      The index of the row needs to be removed
     * @param rmColIdx      The index of the column needs to be removed
     */
    public Node(Node unreducedNode, int rmRowIdx, int rmColIdx) {
        this.size = unreducedNode.getSize() - 1;
        this.lowerBound = unreducedNode.getLowerBound();

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
        for (int i = 0; i < unreducedNode.getSize(); i++) {
            if (i != rmRowIdx) {
                rowIdx[row] = unreducedNode.getRowIdx(i);
                col = 0;

                for (int j = 0; j < unreducedNode.getSize(); j++) {
                    if (j != rmColIdx) {
                        matrix[row][col] = unreducedNode.getMatrix(i, j);
                        col++;
                    }
                }
            }
            row++;
        }

        // Assign column indices
        col = 0;
        for (int i = 0; i < unreducedNode.getSize(); i++) {
            if (i != rmColIdx) {
                colIdx[col] = unreducedNode.getColIdx(i);
                col++;
            }
        }

        // Copy visited sub-paths
        for (int[] path : unreducedNode.getPathHistory()) {
            pathHistory.add(path.clone());
        }

        // Remove the path by checking possible cycles
        removeCycle(unreducedNode.getRowIdx(rmRowIdx), unreducedNode.getColIdx(rmColIdx));
    }

    /**
     * Find possible path in the reduced matrix to form any cycles and set them to -1 (Inf)
     *
     * @param row The index of the removed row
     * @param col The index of the removed column
     */
    private void removeCycle(int row, int col) {
        boolean isFound = false;

        // TODO: check if this can be optimized by only checking until <col> or <row> instead of <size>
        for (int i = 0; i < size; i++) {
            if (rowIdx[i] == col) {
                for (int j = 0; j < size; j++) {
                    if (colIdx[j] == row) {
                        matrix[i][j] = -1;
                        isFound = true;
                    }
                }
            }
        }

        if (isFound) {
            int[] newPath = {row, col};
            pathHistory.add(newPath);
            return;
        } else {
            // TODO: don't understand this part.
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

    public ArrayList<int[]> getPathHistory() {
        return pathHistory;
    }
}
