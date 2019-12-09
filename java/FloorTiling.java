/*
 * Title: Floor Tiling
 * Author: Sam Gavis-Hughson
 * Date: 11/28/2017
 * 
 * Given a 2xN floor and 2x1 tiles, determine the number of ways that you can
 * tile the floor.
 * 
 * eg. 
 * floorTiling(3) = 3
 * floorTiling(5) = 8
 * 
 * Execution: javac FloorTiling.java && java FloorTiling 
 */
public class FloorTiling {
    
    // Brute force solution. We can either place one tile vertically or two 
    // tiles horizontally. Respectively these will cover width 1 and width 2.
    public static int bruteForceFloorTiling(int n) {
        if (n == 0 || n == 1) return 1;
        return bruteForceFloorTiling(n-1) + bruteForceFloorTiling(n-2);
    }
    
    // Top-down dynamic solution
    public static int topDownFloorTiling(int n) {
        int[] dp = new int[n + 1];
        return topDownFloorTiling(n, dp);
    }
    
    // Overloaded recursive method
    private static int topDownFloorTiling(int n, int[] dp) {
        if (n == 0 || n == 1) return 1;
        if (dp[n] == 0) {
            dp[n] = topDownFloorTiling(n-1, dp) + topDownFloorTiling(n-2, dp);
        }
        return dp[n];
    }
    
    // Bottom-up dynamic solution 
    public static int bottomUpFloorTiling(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        
        for (int i = 2; i < dp.length; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }
    
    // Space-optimized bottom-up dynamic solution
    public static int bottomUpFloorTilingSpaceOptimized(int n) {
        int n0 = 1;
        int n1 = 1;
        for (int i = 2; i < n + 1; i++) {
            int temp = n0 + n1;
            n0 = n1;
            n1 = temp;
        }
        return n1;
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(1, 1)).run();
        (new TestCase(3, 3)).run();
        (new TestCase(5, 8)).run();
        (new TestCase(10, 89)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private int input;
        private int output;
        
        private TestCase(int input, int output) {
            this.input = input;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceFloorTiling(input) == output:
                "bruteForceFloorTiling failed for input = " + input;
            assert topDownFloorTiling(input) == output:
                "topDownFloorTiling failed for input = " + input;
            assert bottomUpFloorTiling(input) == output:
                "bottomUpFloorTiling failed for input = " + input;
            assert bottomUpFloorTilingSpaceOptimized(input) == output:
                "bottomUpFloorTilingSpaceOptimized failed for input = " + input;
        }
    }
}