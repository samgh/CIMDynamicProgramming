/*
 * Title: Array Combinations
 * Author: Sam Gavis-Hughson
 * Date: 11/19/2017
 * 
 * Given an array of integers, write a function to compute the total number of 
 * combinations of integers.
 * 
 * eg. 
 * combos({1, 2, 3, 4, 5}) = 32
 * 
 * Execution: javac ArrayCombinations.java && java ArrayCombinations
 */

import java.util.Arrays;

public class ArrayCombinations {
    
    // Brute force solution
    public static int bruteForceCombos(int[] arr) {
        return bruteForceCombos(arr, 0);
    }
    
    // Overloaded private method
    private static int bruteForceCombos(int[] arr, int i) {
        if (i == arr.length) return 1;
        int include = bruteForceCombos(arr, i+1);
        int exclude = bruteForceCombos(arr, i+1);
        
        return include + exclude;
    }
    
    // Top-down dynamic solution
    public static int topDownCombos(int[] arr) {
        int[] dp = new int[arr.length];
        return topDownCombos(arr, 0, dp);
    }
    
    // Overloaded private method
    private static int topDownCombos(int[] arr, int i, int[] dp) {
        if (i == arr.length) return 1;
        if (dp[i] == 0) {
            int include = topDownCombos(arr, i+1, dp);
            int exclude = topDownCombos(arr, i+1, dp);
            dp[i] = include + exclude;
        }
        return dp[i];
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpCombos(int[] arr) {
        int[] dp = new int[arr.length + 1];
        dp[0] = 1;
        for (int i = 1; i < dp.length; i++) {
            dp[i] = dp[i-1] + dp[i-1];
        }
        
        return dp[arr.length];
    }
    
     // Sample testcases
    public static void main(String[] args) {
        (new TestCase(new int[]{}, 1)).run();
        (new TestCase(new int[]{1}, 2)).run();
        (new TestCase(new int[]{1, 1}, 4)).run();
        (new TestCase(new int[]{1, 1, 1, 1, 1}, 32)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private int[] input;
        private int output;
        
        private TestCase(int[] input, int output) {
            this.input = input;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceCombos(input) == output:
                "bruteForceCombos failed for input = " + Arrays.toString(input);
            assert topDownCombos(input) == output:
                "topDownCombos failed for input = " + Arrays.toString(input);
            assert bottomUpCombos(input) == output:
                "bottomUpCombos failed for input = " + Arrays.toString(input);
        }
    }
}