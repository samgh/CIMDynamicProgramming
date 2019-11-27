/*
 * Title: Square submatrix
 * Author: Sam Gavis-Hughson
 * Date: 11/21/17
 * 
 * Given a 2D boolean array, find the largest square subarray of true values. 
 * The return value should be the side length of the largest square subarray 
 * subarray.
 * 
 * eg. 
 * squareSubmatrix([false, true, false, false]) = 2
 *                 [true,  TRUE, TRUE,  true ]
 *                 [false, TRUE, TRUE,  false]
 * (the solution is the submatrix in all caps)
 * 
 * Execution: javac SquareSubmatrix.java && java SquareSubmatrix
 */

import java.util.Arrays;

public class SquareSubmatrix {
    // Brute force solution
    public static int bruteForceSquareSubmatrix(boolean[][] arr) {
        int max = 0;
        // Compute recursively for each cell what it is the upper left corner of        
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                max = Math.max(max, bruteForceSquareSubmatrix(arr, i, j));
            }
        }
        
        return max;
    }
    
    // Overloaded recursive function
    private static int bruteForceSquareSubmatrix(boolean[][] arr, int i, int j) {
        // If we get to the bottom or right of the matrix, we can't go any 
        // further
        if (i == arr.length || j == arr[0].length) return 0;
        
        // If the cell is False then it is not part of a valid submatrix
        if (!arr[i][j]) return 0;
        
        // Find the size of the right, bottom, and bottom right submatrices and
        // add 1 to the minimum of those 3 to get the result
        return 1 + Math.min(Math.min(bruteForceSquareSubmatrix(arr, i+1, j), 
                                     bruteForceSquareSubmatrix(arr, i, j+1)),
                            bruteForceSquareSubmatrix(arr, i+1, j+1));
    }
    
    // Top-down dynamic  solution
    public static int topDownSquareSubmatrix(boolean[][] arr) {
        // Initialize cache. Don't need to initialize to -1 because the only 
        // cells that will be 0 are ones that are False and we want to skip
        // those ones anyway
        int[][] dp = new int[arr.length][arr[0].length];
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                max = Math.max(max, topDownSquareSubmatrix(arr, i, j, dp));
            }
        }
        
        return max;
    }
    
    // Overloaded recursive function
    private static int topDownSquareSubmatrix(boolean[][] arr, int i, int j, int[][] dp) {
        if (i == arr.length || j == arr[0].length) return 0;
        if (!arr[i][j]) return 0;
        
        // If the value is set in the cache return it. Otherwise compute and 
        // save to cache
        if (dp[i][j] == 0) {
            dp[i][j] = 1 + Math.min(Math.min(topDownSquareSubmatrix(arr, i+1, j, dp), 
                                             topDownSquareSubmatrix(arr, i, j+1, dp)),
                                    topDownSquareSubmatrix(arr, i+1, j+1, dp));
        }
        return dp[i][j];
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpSquareSubmatrix(boolean[][] arr) {
        int max = 0;
        // Initialize cache
        int[][] dp = new int[arr.length][arr[0].length];
        // Iterate over the matrix to compute all values
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                // If we are in the first row or column then the value is just
                // 1 if that cell is true and 0 otherwise. In other rows and 
                // columns, need to look up and to the left
                if (i == 0 || j == 0) {
                    dp[i][j] = arr[i][j] ? 1 : 0;
                } else if (arr[i][j]) {
                    dp[i][j] = Math.min(Math.min(dp[i][j-1],
                                                 dp[i-1][j]),
                                        dp[i-1][j-1]) + 1;
                }
                max = Math.max(max, dp[i][j]);
            }
        }
        
        return max;
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(new boolean[][]{new boolean[]{true}}, 1)).run();
        (new TestCase(new boolean[][]{new boolean[]{false}}, 0)).run();
        (new TestCase(new boolean[][]{
            new boolean[]{true, true, true, false},
            new boolean[]{false, true, true, true},
            new boolean[]{true, true, true, true}}, 2)).run();
        (new TestCase(new boolean[][]{
            new boolean[]{true, true, true, true},
            new boolean[]{false, true, true, true},
            new boolean[]{true, true, true, true}}, 3)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private boolean[][] input;
        private int output;
        
        private TestCase(boolean[][] input, int output) {
            this.input = input;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceSquareSubmatrix(input) == output:
                "bruteForceSquareSubmatrix failed for input = " + inputString();
            assert topDownSquareSubmatrix(input) == output:
                "topDownSquareSubmatrix failed for input = " + inputString();
            assert bottomUpSquareSubmatrix(input) == output:
                "bottomUpSquareSubmatrix failed for input = " + inputString();
        }
        
        private String inputString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < input.length; i++) {
                sb.append(Arrays.toString(input[i]));
                if (i != input.length - 1) sb.append('\n');
            }
            return sb.toString();
        }
    }
}