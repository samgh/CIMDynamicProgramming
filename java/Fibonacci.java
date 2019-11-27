/*
 * Title: Fibonacci
 * Author: Sam Gavis-Hughson
 * Date: 11/19/2017
 * 
 * Given an integer n, write a function that will return the nth Fibonacci number.
 * 
 * eg. 
 * fib(0) = 0
 * fib(1) = 1
 * fib(5) = 5
 * fib(10) = 55
 * 
 * Execution: javac Fibonacci.java && java Fibonacci
 */

// We will assume for all solutions that n >= 0 and that int is sufficient
// to hold the result
public class Fibonacci {
    
    // Brute force solution
    public static int bruteForceFib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return bruteForceFib(n-1) + bruteForceFib(n-2);
    }
    
    // Top-down dynamic solution
    public static int topDownFib(int n) {
        int[] dp = new int[n+1];
        return topDownFib(n, dp);
    }
    
    // Overloaded private method
    private static int topDownFib(int n, int[] dp) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        // If value is not set in cache, compute it
        if (dp[n] == 0) {
            dp[n] = topDownFib(n-1, dp) + topDownFib(n-2, dp);
        }
        return dp[n];
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpFib(int n) {
        if (n == 0) return 0;
        
        // Initialize cache
        int[] dp = new int[n+1];
        dp[1] = 1;
        
        // Fill cache iteratively
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        
        return dp[n];
    }
    
    // Space-optimized bottom-up dynamic solution
    public static int bottomUpFibOptimized(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        int n1 = 1, n2 = 0;
        for (int i = 2; i < n; i++) {
            int n0 = n1 + n2;
            n2 = n1;
            n1 = n0;
        }
        
        return n1 + n2;
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(0, 0)).run();
        (new TestCase(1, 1)).run();
        (new TestCase(2, 1)).run();
        (new TestCase(5, 5)).run();
        (new TestCase(10, 55)).run();
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
            assert bruteForceFib(input) == output:
                "bruteForceFib failed for input = " + input;
            assert topDownFib(input) == output:
                "topDownFib failed for input = " + input;
            assert bottomUpFib(input) == output:
                "bottomUpFib failed for input = " + input;
            assert bottomUpFibOptimized(input) == output:
                "bottomUpFibOptimized failed for input = " + input;
        }
    }
}