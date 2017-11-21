/*
 * Title: Target Sum
 * Author: Sam Gavis-Hughson
 * Date: 11/19/2017
 * 
 * Given an array of integers, nums and a target value T, find the number of 
 * ways that you can add and subtract the values in nums to add up to T.
 * 
 * eg.
 * nums = {1, 2, 3, 4}
 * target = 0
 * 
 * 1 - 2 - 3 + 4
 * -1 + 2 + 3 - 4
 * 
 * targetSum(nums, target) = 2
 * 
 * Execution: javac TargetSum.java && java TargetSum
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TargetSum {
    
    // Brute force solution
    public static int bruteForceTargetSum(int[] nums, int T) {
        return bruteForceTargetSum(nums, T, 0, 0);
    }
    
    // Overloaded recursive function
    private static int bruteForceTargetSum(int[] nums, int T, int i, int sum) {
        // When we've gone through every item, see if we've reached our target sum
        if (i == nums.length) {
            return sum == T ? 1 : 0;
        }
        
        // Combine the number of possibilites by adding and subtracting the
        // current value
        return bruteForceTargetSum(nums, T, i+1, sum + nums[i]) 
            + bruteForceTargetSum(nums, T, i+1, sum - nums[i]);
    }
    
    // Top-down dynamic solution. Array-based caching
    public static int topDownTargetSumArray(int[] nums, int T) {
        int numsSum = 0;
        for (int num : nums) numsSum += num;
        int[][]dp = new int[nums.length][2 * numsSum + 1];
        
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                dp[i][j] = -1;
            }
        }
        
        return topDownTargetSumArray(nums, T, 0, 0, dp);
    }
    
    // Overloaded recursive method
    private static int topDownTargetSumArray(int[] nums, int T, int i, int sum, int[][] dp) {
        if (i == nums.length) return sum == T ? 1 : 0;
        int sumIndex = dp[0].length / 2 + sum;
        if (dp[i][sumIndex] == -1) {
            dp[i][sumIndex] = topDownTargetSumArray(nums, T, i+1, sum + nums[i], dp)
                + topDownTargetSumArray(nums, T, i+1, sum - nums[i], dp);
        }
        return dp[i][sumIndex];
    }
    
    // Top-down dynamic programming solution. HashMap-based caching
    public static int topDownTargetSumHashMap(int[] nums, int T) {
        // Map: i -> sum -> value
        Map<Integer, Map<Integer, Integer>> cache = new HashMap<Integer, Map<Integer, Integer>>();
        return topDownTargetSumHashMap(nums, T, 0, 0, cache);
    }
    
    // Overloaded recursive function
    private static int topDownTargetSumHashMap(int[] nums, int T, int i, int sum, Map<Integer, Map<Integer, Integer>> cache) {
        if (i == nums.length) {
            return sum == T ? 1 : 0;
        }
        
        // Check the cache and return value if we get a hit
        if (!cache.containsKey(i)) cache.put(i, new HashMap<Integer, Integer>());
        Integer cached = cache.get(i).get(sum);
        if (cached != null) return cached;
        
        // If we didn't hit in the cache, compute the value and store to cache
        int toReturn = topDownTargetSumHashMap(nums, T, i+1, sum + nums[i], cache) + 
            topDownTargetSumHashMap(nums, T, i+1, sum - nums[i], cache);
        cache.get(i).put(sum, toReturn);
        return toReturn;
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpTargetSum(int[] nums, int T) {
        if (nums.length == 0) return T == 0 ? 1 : 0;
        int numsSum = 0;
        
        // Our cache has to range from -sum(nums) to sum(nums), so we offset
        // everything by sum
        for (int num : nums) numsSum += num;
        int[][] dp = new int[nums.length + 1][2 * numsSum + 1];
        dp[0][numsSum] = 1;
        
        // Iterate over the previous row and update the current row
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                if (j - nums[i-1] >= 0) 
                    dp[i][j] += dp[i-1][j - nums[i-1]];
                if (j + nums[i-1] < dp[0].length)
                    dp[i][j] += dp[i-1][j + nums[i-1]];
            }
        }
        
        return dp[nums.length][numsSum + T];
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(new int[]{}, 1, 0)).run();
        (new TestCase(new int[]{1, 1, 1, 1, 1}, 3, 5)).run();
        (new TestCase(new int[]{1, 1, 1}, 1, 3)).run();
        (new TestCase(new int[]{1, 2, 3, 4}, 0, 2)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private int[] nums;
        private int target;
        private int output;
        
        private TestCase(int[] nums, int target, int output) {
            this.nums = nums;
            this.target = target;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceTargetSum(nums, target) == output:
                "bruteForceTargetSum failed for nums = " + Arrays.toString(nums) + ", target = " + target;
            assert topDownTargetSumArray(nums, target) == output:
                "topDownTargetSumArray failed for nums = " + Arrays.toString(nums) + ", target = " + target;
            assert topDownTargetSumHashMap(nums, target) == output:
                "topDownTargetSumHashMap failed for nums = " + Arrays.toString(nums) + ", target = " + target;
            assert bottomUpTargetSum(nums, target) == output:
                "bottomUpTargetSum failed for nums = " + Arrays.toString(nums) + ", target = " + target;
           }
    }
}