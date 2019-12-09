""""
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
 * Execution: python longest_increasing_subsequence.py
 """

import unittest
from typing import List


def brute_force(arr: List[int]):
    # Brute force solution. Starting at each index, look ahead and try 
    # recursively including each value that is larger than the current value
    max_length = 0

    # Try every starting point for our subsequence.
    for i in range(len(arr)):
        max_length = max(max_length, brute_force_helper(arr, i))
    return max_length


def brute_force_helper(arr: List[int], i: int):
    """Recursive method."""
    if i == len(arr):
        return 0

    max_length = 0
    # Consider every possible next value in the subsequence
    for j in range(i+1, len(arr)):
        if arr[i] < arr[j]:
            max_length = max(max_length, brute_force_helper(arr, j))
    return max_length + 1


def top_down(arr: List[int]):
    dp = [0] * len(arr)

    max_length = 0
    for i in range(len(arr)):
        max_length = max(max_length, top_down_helper(arr, i, dp))
    return max_length


def top_down_helper(arr: List[int], i: int, dp: List[int]):
    if i == len(arr):
        return 0

    if dp[i] == 0:
        max_length = 0
        for j in range(i+1, len(arr)):
            if arr[i] < arr[j]:
                max_length = max(max_length, top_down_helper(arr, j, dp))
        dp[i] = max_length + 1
    return dp[i]


def bottom_up(arr: List[int]):
    dp = [0] * len(arr)
    max_length = 0
    for i in range(len(dp)):
        local_max = 0
        for j in range(i):
            if arr[j] < arr[i]:
                local_max = max(local_max, dp[j])
        dp[i] = local_max + 1
        max_length = max(max_length, dp[i])
    return max_length


class TestLongestIncreasingSubsequence(unittest.TestCase):
    """Unit test for longest_increasing_subsequence."""

    def test_brute_force(self):
        self.assertEqual(brute_force([1]), 1)
        self.assertEqual(brute_force([5, 4, 3, 2, 1]), 1)
        self.assertEqual(brute_force([1, 4, 2, 3, 5]), 4)
        self.assertEqual(brute_force([10, 22, 9, 33, 21, 50, 41, 60]), 5)

    def test_top_down(self):
        self.assertEqual(top_down([1]), 1)
        self.assertEqual(top_down([5, 4, 3, 2, 1]), 1)
        self.assertEqual(top_down([1, 4, 2, 3, 5]), 4)
        self.assertEqual(top_down([10, 22, 9, 33, 21, 50, 41, 60]), 5)

    def test_bottom_up(self):
        self.assertEqual(bottom_up([1]), 1)
        self.assertEqual(bottom_up([5, 4, 3, 2, 1]), 1)
        self.assertEqual(bottom_up([1, 4, 2, 3, 5]), 4)
        self.assertEqual(bottom_up([10, 22, 9, 33, 21, 50, 41, 60]), 5)


if __name__ == '__main__':
    unittest.main()


