/*
 * Title: Matrix Chain Multiplication
 * Author: Sam Gavis-Hughson
 * Date: 12/8/2017
 * 
 * Given a set of matrices, determine the minimum number of operations required
 * to multiply the matrices. In other words, determine the most efficient way
 * to group the matrices and multiply them.
 * 
 * For more detailed explanations:
 * * https://en.wikipedia.org/wiki/Matrix_multiplication
 * * https://en.wikipedia.org/wiki/Matrix_chain_multiplication
 * 
 * eg.
 * Matrix dimensions:
 * A = [40 x 20]
 * B = [20 x 30]
 * C = [30 x 10]
 * D = [10 x 30]
 * 
 * matrixChainMultiplication({A, B, C, D}) = 26000
 * AxBxCxD
 * (Ax(BxC))xD
 * 6000 + (Ax[20x10])xD
 * 6000 + 8000 + [40x10]xD
 * 6000 + 8000 + 12000
 * 26000
 * 
 * Execution: javac MatrixChainMultiplication.java && java MatrixChainMultiplication
 */
import java.util.Arrays;

public class MatrixChainMultiplication {
    // Simple matrix class to easily represent input
    public static class Matrix {
        int rows;
        int cols;
        
        public Matrix(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }
        
        @Override
        public String toString() {
            return "[" + rows + "x" + cols + "]";
        }
    }
    
    // Brute force solution. We will consider every possible grouping of 
    // matrices by recursively dividing into smaller and smaller sets until we
    // have only 1 or 2 matrices in our set. This is similar to the EggDrop 
    // problem
    public static int bruteForceMCM(Matrix[] arr) {
        return bruteForceMCM(arr, 0, arr.length-1);
    }
    
    // Overloaded private method
    private static int bruteForceMCM(Matrix[] arr, int i, int j) {
        // When we have a single matrix, we don't have to do any operations
        if (i == j) return 0;
        
        int min = Integer.MAX_VALUE;
        // Try splitting our list of matrices at each possible point and see
        // which results in the least total operations
        for (int k = i; k < j; k++) {
            // The number of operations is the number of operations to compute
            // the left and right matrix plus the number of operations to
            // multiply the two resulting matrices together
            int ops = bruteForceMCM(arr, i, k) 
                + bruteForceMCM(arr, k+1, j) 
                + arr[i].rows * arr[k].cols * arr[j].cols;
            min = Math.min(min, ops);
        }
        
        return min;
    }
    
    // Top-down dynamic solution
    public static int topDownMCM(Matrix[] arr) {
        int[][] dp = new int[arr.length][arr.length];
        return topDownMCM(arr, 0, arr.length-1, dp);
    }
    
    // Overloaded recursive method
    private static int topDownMCM(Matrix[] arr, int i, int j, int[][] dp) {
        if (i == j) return 0;
        
        if (dp[i][j] == 0) {
            int min = Integer.MAX_VALUE;
            for (int k = i; k < j; k++) {
                int ops = topDownMCM(arr, i, k, dp)
                    + topDownMCM(arr, k+1, j, dp)
                    + arr[i].rows * arr[k].cols * arr[j].cols;
                min = Math.min(min, ops);
            }
            dp[i][j] = min;
        }
        
        return dp[i][j];
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpMCM(Matrix[] arr) {
        int[][] dp = new int[arr.length][arr.length];
        
        // Unlike the previous problems, the previous subproblem is not based on
        // the absolute values of i and j, but rather the difference. Our 
        // smallest subproblems are when j = i+1. We need to compute every value 
        // where this is true before we can compute for j = i+2. Because of this
        // we iterate over an increasing gap between i and j, rather than the 
        // absolute values of i and j.
        for (int gap = 1; gap < dp.length; gap++) {
            for (int i = 0; i + gap < dp.length; i++) {
                int j = i + gap;
                int min = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int ops = dp[i][k] 
                        + dp[k+1][j] 
                        + arr[i].rows * arr[k].cols * arr[j].cols;
                    min = Math.min(min, ops);
                }
                dp[i][i+gap] = min;
            }
        }
        return dp[0][arr.length - 1];
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(new Matrix[]{
            new Matrix(40, 20)}, 0)).run();
        (new TestCase(new Matrix[]{
            new Matrix(10, 20),
            new Matrix(20, 30)}, 6000)).run();
        (new TestCase(new Matrix[]{
            new Matrix(40, 20), 
            new Matrix(20, 30), 
            new Matrix(30, 10), 
            new Matrix(10, 30)}, 26000)).run();
        (new TestCase(new Matrix[]{
            new Matrix(10, 20),
            new Matrix(20, 30),
            new Matrix(30, 40),
            new Matrix(40, 30)}, 30000)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private Matrix[] input;
        private int output;
        
        private TestCase(Matrix[] input, int output) {
            this.input = input;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceMCM(input) == output:
                "bruteForceMCM failed on " + Arrays.toString(input);
            assert topDownMCM(input) == output:
                "topDownMCM failed on " + Arrays.toString(input);
            assert bottomUpMCM(input) == output:
                "bottomUpMCM failed on " + Arrays.toString(input);
        }
    }
}