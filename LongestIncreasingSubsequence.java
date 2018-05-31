/*
 * Title: Longest Increasing Subsequence
 * Author: Sam Gavis-Hughson
 * Date: 12/7/2017
 * 
 * Given an array of integers, find the length of the longest subsequence 
 * (not necessarily consecutive) of increasing values.
 * 
 * eg.
 * 
 * LIS({1, 4, 2, 3, 5}) = 4 ({1, 2, 3, 5})
 * LIS({10, 22, 9, 33, 21, 50, 41, 60}) = 5 ({10, 22, 33, 50, 60})
 * 
 * Execution: javac LongestIncreasingSubsequence.java && java LongestIncreasingSubsequence
 */

import java.util.Arrays;

public class LongestIncreasingSubsequence {
    
    // Brute force solution. Starting at each index, look ahead and try 
    // recursively including each value that is larger than the current value
    public static int bruteForceLIS(int[] arr) {
        int maxLength = 0;
        // Try every starting point for our subsequence
        for (int i = 0; i < arr.length; i++) {
            maxLength = Math.max(maxLength, bruteForceLIS(arr, i));
        }
        return maxLength;
    }
    
    // Overloaded recursive method
    private static int bruteForceLIS(int[] arr, int i) {
        if (i == arr.length) return 0;
        
        int maxLength = 0;
        // Consider every possible next value in the subsequence
        for (int j = i+1; j < arr.length; j++) {
            if (arr[i] < arr[j]) {
                maxLength = Math.max(maxLength, bruteForceLIS(arr, j));
            }
        }
        
        return maxLength + 1;
    }
    
    // Top-down dynamic solution
    public static int topDownLIS(int[] arr) {
        int[] dp = new int[arr.length];
        
        int maxLength = 0;
        for (int i = 0; i < arr.length; i++) {
            maxLength = Math.max(maxLength, topDownLIS(arr, i, dp));
        }
        
        return maxLength;
    }
    
    // Overloaded recursive method
    private static int topDownLIS(int[] arr, int i, int[] dp) {
        if (i == arr.length) return 0;
        
        if (dp[i] == 0) {
            int maxLength = 0;
            for (int j = i+1; j < arr.length; j++) {
                if (arr[i] < arr[j]) {
                    maxLength = Math.max(maxLength, topDownLIS(arr, j, dp));
                }
            }
            dp[i] = maxLength + 1;
        }
        return dp[i];
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpLIS(int[] arr) {
        int[] dp = new int[arr.length];
        
        int maxLength = 0;
        for (int i = 0; i < dp.length; i++) {
            int localMax = 0;
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    localMax = Math.max(localMax, dp[j]);
                }
            }
            dp[i] = localMax + 1;
            maxLength = Math.max(maxLength, dp[i]);
        }
        return maxLength;
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(new int[]{1}, 1)).run();
        (new TestCase(new int[]{5, 4, 3, 2, 1}, 1)).run();
        (new TestCase(new int[]{1, 4, 2, 3, 5}, 4)).run();
        (new TestCase(new int[]{10, 22, 9, 33, 21, 50, 41, 60}, 5)).run();
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
            assert bruteForceLIS(input) == output:
                "bruteForceLIS failed for input = " + Arrays.toString(input);
            assert topDownLIS(input) == output:
                "topDownLIS failed for input = " + Arrays.toString(input);
            assert bottomUpLIS(input) == output:
                "bottomUpLIS failed for input = " + Arrays.toString(input);
        }
    }
}