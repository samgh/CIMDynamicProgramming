"""
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
 * Execution: python target_sum.py
"""
import unittest
from collections import defaultdict
from typing import List


def brute_force_target_sum(nums: List[int], T: int):
    return brute_force_target_sum_helper(nums, T, 0, 0)


def brute_force_target_sum_helper(nums: List[int], T: int, i: int, sum_val: int):
    # When we've gone through every item, see if we've reach out target sum 
    if i == len(nums):
        if sum_val == T:
            return 1
        else:
            return 0
    # Combine the number of possibilities by adding and subtracting the
    # current values
    return brute_force_target_sum_helper(nums, T, i+1, sum_val + nums[i]) + \
           brute_force_target_sum_helper(nums, T, i+1, sum_val - nums[i])


def top_down_target_sum(nums: List[int], T: int):
    nums_sum = 0
    for num in nums:
        nums_sum += num
    dp = dict()
    for i in range(len(nums)):
        for j in range(2*nums_sum+1):
            dp[(i, j)] = -1
    return top_down_target_sum_helper(nums, T, 0, 0, nums_sum, dp)


def top_down_target_sum_helper(nums: List[int], T: int, i: int, sum_val: int, nums_sum: int, dp: dict):
    if i == len(nums):
        if sum_val == T:
            return 1
        else:
            return 0
    sum_index = (2 * nums_sum + 1) // 2 + sum_val
    if dp[(i, sum_index)] == -1:
        dp[(i, sum_index)] = top_down_target_sum_helper(nums, T, i+1, sum_val + nums[i], nums_sum, dp) + \
                             top_down_target_sum_helper(nums, T, i+1, sum_val - nums[i], nums_sum, dp)

    return dp[(i, sum_index)]


def bottom_up_target_sum(nums: List[int], T: int):
    if len(nums) == 0:
        if T == 0:
            return 1
        else:
            return 0
    nums_sum = 0

    # Our cache has to range from -sum(nums) to sum(nums), so we offset
    # everything by sum
    for num in nums:
        nums_sum += num

    dp = defaultdict(int)
    dp[(0, nums_sum)] = 1

    # Iterate over the previous row and update the current row
    for i in range(1, len(nums) + 1):
        for j in range(2 * nums_sum + 1):
            if j - nums[i-1] >= 0:
                dp[(i, j)] += dp[(i-1, j-nums[i-1])]
            if j + nums[i-1] < 2 * nums_sum + 1:
                dp[(i, j)] += dp[(i-1, j + nums[i-1])]
    return dp[(len(nums), nums_sum + T)]


class TestTargetSum(unittest.TestCase):
    """Unit test for target_sum."""

    def test_brute_force(self):
        self.assertEqual(brute_force_target_sum([], 1), 0)
        self.assertEqual(brute_force_target_sum([1, 1, 1, 1, 1], 3), 5)
        self.assertEqual(brute_force_target_sum([1, 1, 1], 1), 3)
        self.assertEqual(brute_force_target_sum([1, 2, 3, 4], 0), 2)

    def test_top_down(self):
        self.assertEqual(top_down_target_sum([], 1), 0)
        self.assertEqual(top_down_target_sum([1, 1, 1, 1, 1], 3), 5)
        self.assertEqual(top_down_target_sum([1, 1, 1], 1), 3)
        self.assertEqual(top_down_target_sum([1, 2, 3, 4], 0), 2)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_target_sum([], 1), 0)
        self.assertEqual(bottom_up_target_sum([1, 1, 1, 1, 1], 3), 5)
        self.assertEqual(bottom_up_target_sum([1, 1, 1], 1), 3)
        self.assertEqual(bottom_up_target_sum([1, 2, 3, 4], 0), 2)



if __name__ == '__main__':
    unittest.main()


""" 
    
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
"""
