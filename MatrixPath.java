/*
 * Title: Matrix Path
 * Author: Sam Gavis-Hughson
 * Date: 12/1/2017
 * 
 * Given a matrix, find the path from top left to bottom right with the greatest 
 * product by moving only down and right.
 * 
 * eg. 
 * arr = {-1, 2,  3}
 *       { 4, 5, -6}
 *       { 7, 8,  9}
 * 
 * matrixPath(arr) = 1080 (-1 * 4 * 5 * -6 * 9)
 * 
 * Execution: javac MatrixPath.java && java MatrixPath 
 */

public class MatrixPath {
    // Brute force solution. Recursively try every path. We must compute both
    // the maximum (result[0]) and minimum (result[1]) value for each path
    // because if the current value is negative, we want to multiply it by the
    // minimum path. That way any negatives will cancel out.
    public static int bruteForceMaxPath(int[][] arr) {
        int[] result = bruteForceMaxPath(arr, 0, 0);
        return result[0];
    }
    
    // Overloaded recursive method. Return length 2 array where arr[0] is max
    // and arr[1] is min
    private static int[] bruteForceMaxPath(int[][] arr, int i, int j) {
        // If we're out of bounds, return null
        if (i == arr.length || j == arr[0].length) return null;
        
        int[] down = bruteForceMaxPath(arr, i+1, j);
        int[] right = bruteForceMaxPath(arr, i, j+1);
        
        // Bottom-right corner
        if (down == null && right == null) return new int[]{arr[i][j], arr[i][j]};
        
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        // If we can go down, try that path
        if (down != null) {
            min = Math.min(min, Math.min(arr[i][j] * down[0], arr[i][j] * down[1]));
            max = Math.max(max, Math.max(arr[i][j] * down[0], arr[i][j] * down[1]));
        }
        
        // If we can go right, try that path
        if (right != null) {
            min = Math.min(min, Math.min(arr[i][j] * right[0], arr[i][j] * right[1]));
            max = Math.max(max, Math.max(arr[i][j] * right[0], arr[i][j] * right[1]));
        }
        
        return new int[]{max, min};
    }
    
    // Top-down dynamic solution
    public static int topDownMaxPath(int[][] arr) {
        // Third dimension is max/min result
        int[][][] dp = new int[arr.length][arr[0].length][2];
        
        // We will indicate that a value is unset by setting the max to be less
        // than the min. When it's set, they should at least be equal
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                dp[i][j][0] = -1;
            }
        }
        int[] result = topDownMaxPath(arr, 0, 0, dp);
        return result[0];
    }
    
    // Overloaded recursive method
    private static int[] topDownMaxPath(int[][] arr, int i, int j, int[][][] dp) {
        if (i == arr.length || j == arr[0].length) return null;
        
        // Unset if the max is less than the min
        if (dp[i][j][0] < dp[i][j][1]) {
            int[] down = topDownMaxPath(arr, i+1, j, dp);
            int[] right = topDownMaxPath(arr, i, j+1, dp);
            
            if (down == null && right == null) return new int[]{arr[i][j], arr[i][j]};
        
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            
            if (down != null) {
                min = Math.min(min, Math.min(arr[i][j] * down[0], arr[i][j] * down[1]));
                max = Math.max(max, Math.max(arr[i][j] * down[0], arr[i][j] * down[1]));
            }
            
            if (right != null) {
                min = Math.min(min, Math.min(arr[i][j] * right[0], arr[i][j] * right[1]));
                max = Math.max(max, Math.max(arr[i][j] * right[0], arr[i][j] * right[1]));
            }
            
            dp[i][j][0] = max;
            dp[i][j][1] = min;
        }
        
        return new int[]{dp[i][j][0], dp[i][j][1]};
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpMaxPath(int[][] arr) {
        int[][][] dp = new int[arr.length][arr[0].length][2];
        
        // Build up max path starting at arr[0][0]
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                // Base case
                if (i == 0 && j == 0) {
                    dp[i][j][0] = arr[i][j];
                    dp[i][j][1] = arr[i][j];
                    continue;
                } 
                
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE; 
                
                if (i != 0) {
                    min = Math.min(min, Math.min(arr[i][j] * dp[i-1][j][0], arr[i][j] * dp[i-1][j][1]));
                    max = Math.max(max, Math.max(arr[i][j] * dp[i-1][j][0], arr[i][j] * dp[i-1][j][1]));
                }
                
                if (j != 0) {
                    min = Math.min(min, Math.min(arr[i][j] * dp[i][j-1][0], arr[i][j] * dp[i][j-1][1]));
                    max = Math.max(max, Math.max(arr[i][j] * dp[i][j-1][0], arr[i][j] * dp[i][j-1][1]));
                }
                
                dp[i][j][0] = max;
                dp[i][j][1] = min;
            }
        }
        return dp[arr.length - 1][arr[0].length - 1][0];
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(new int[][]{new int[]{1}}, 1)).run();
        (new TestCase(new int[][]{
            new int[]{1, 2, 3},
            new int[]{4, 5, 6},
            new int[]{7, 8, 9}
        }, 2016)).run();
        (new TestCase(new int[][]{
            new int[]{-1, 2, 3},
            new int[]{4, 5, -6},
            new int[]{7, 8, 9}
        }, 1080)).run();
        (new TestCase(new int[][]{
            new int[]{-1, 2,3},
            new int[]{4, 5, 6},
            new int[]{7, 8, 9}
        }, -324)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private int[][] arr;
        private int output;
        
        private TestCase(int[][] arr, int output) {
            this.arr = arr;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceMaxPath(arr) == output:
                "bruteForceMaxPath failed for arr = " + arr;
            assert topDownMaxPath(arr) == output:
                "topDownMaxPath failed for arr = " + arr;
            assert bottomUpMaxPath(arr) == output:
                "bottomUpMaxPath failed for arr = " + arr;
        }
    }
}