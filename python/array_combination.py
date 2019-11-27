"""
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
 * Execution: python array_combinations.py
"""
from typing import List
import unittest


def brute_force_combos(arr: List[int]):
    """Brute-force solution."""
    return brute_force_combos_helper(arr, 0)


def brute_force_combos_helper(arr: List[int], i: int):
    """Brute-force solution helper."""
    if len(arr) == i:
        return 1
    include = brute_force_combos_helper(arr, i+1)
    exclude = brute_force_combos_helper(arr, i+1)

    return include + exclude


def top_down_combos(arr: List[int]):
    """Top-down solution."""
    dp = [0] * len(arr)
    return top_down_combos_helper(arr, 0, dp)


def top_down_combos_helper(arr: List[int], i: int, dp: List[int]):
    """Top-down solution helper."""
    if len(arr) == i:
        return 1
    if dp[i] == 0:
        include = top_down_combos_helper(arr, i+1, dp)
        exclude = top_down_combos_helper(arr, i+1, dp)
        dp[i] = include + exclude
    return dp[i]


def bottom_up_combos(arr: List[int]):
    """Bottom-up solution."""
    dp = [0] * (len(arr) + 1)
    dp[0] = 1
    for i in range(1, len(dp)):
        dp[i] = dp[i-1] + dp[i-1]
    return dp[len(arr)]


class TestArrayCombinations(unittest.TestCase):
    """Unit test for array_combinations."""

    def test_brute_force(self):
        self.assertEqual(brute_force_combos([]), 1)
        self.assertEqual(brute_force_combos([1]), 2)
        self.assertEqual(brute_force_combos([1, 1]), 4)
        self.assertEqual(brute_force_combos([1, 1, 1, 1, 1]), 32)

    def test_top_down(self):
        self.assertEqual(top_down_combos([]), 1)
        self.assertEqual(top_down_combos([1]), 2)
        self.assertEqual(top_down_combos([1, 1]), 4)
        self.assertEqual(top_down_combos([1, 1, 1, 1, 1]), 32)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_combos([]), 1)
        self.assertEqual(bottom_up_combos([1]), 2)
        self.assertEqual(bottom_up_combos([1, 1]), 4)
        self.assertEqual(bottom_up_combos([1, 1, 1, 1, 1]), 32)


if __name__ == '__main__':
    unittest.main()

