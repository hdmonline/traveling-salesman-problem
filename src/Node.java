/**
 * Node.java
 * @author Group 31: Chong Ye, Dongmin Han, Shan Xiong, Yuanlai Zhou
 * Georgia Institute of Technology, Fall 2018
 *
 * A matrix class for operations on a matrix
 */

import java.util.ArrayList;

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
    }

    /**
     * Copy the matrix input and remove the one row and one col based on input
     *
     * @param processedMat Input matrix
     * @param rmRowIdx The index of the row needs to be removed
     * @param rmColIdx The index of the column needs to be removed
     */
    public Node(Node processedMat, int rmRowIdx, int rmColIdx) {
        this.size = processedMat.getSize() - 1;
        this.lowerBound = processedMat.getLowerBound();

        matrix = new int[size][size];
        rowIdx = new int[size];
        colIdx = new int[size];

        rowPenalty = new int[size];
        colPenalty = new int[size];

        // Copy the reduced matrix
        int row = 0, col = 0;
        for (int i = 0; i < processedMat.getSize(); i++) {
            if (i != rmRowIdx) {
                rowIdx[row] = processedMat.getRowIdx(i);
                col = 0;

                for (int j = 0; j < processedMat.getSize(); j++) {
                    if (j != rmColIdx) {
                        matrix[row][col] = processedMat.getMatrix(i, j);
                        col++;
                    }
                }
            }
            row++;
        }

        // Assign column indices
        col = 0;
        for (int i = 0; i < processedMat.getSize(); i++) {
            if (i != rmColIdx) {
                colIdx[col] = processedMat.getColIdx(i);
                col++;
            }
        }

        // Copy visited sub-paths
        for (int[] path : processedMat.getPathHistory()) {
            pathHistory.add(path.clone());
        }
    }

    private void removeCycle(int row, int col) {
        boolean isFound = false;
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
