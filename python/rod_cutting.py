"""
 * Title: Rod Cutting
 * Author: Sam Gavis-Hughson
 * Date: 11/30/2017
 * 
 * Given a rod of a certain length and a list of prices for different length
 * rods, determine the best way to cut the rod to get the greatest price. You
 * can assume that you have prices for any length from 1 to the length of the 
 * rod.
 * 
 * eg. 
 * prices = {1, 5, 8, 9, 10}
 * (prices[i] = price for length i+1, ie. prices[0] = price for length 1)
 * 
 * rodCutting(prices, 4) = 10 (5 + 5)
 * rodCutting(prices, 5) = 13 (8 + 5)
 * 
 * Execution: python rod_cutting.py
"""
import unittest
from typing import List


def brute_force_rod_cutting(prices: List[int], length: int):
    if length == 0:
        return 0
    max_value = 0
    for i in range(1, length+1):
        max_value = max(max_value, prices[i-1] + brute_force_rod_cutting(prices, length - i))
    return max_value

def top_down_rod_cutting(prices: List[int], length: int):
    dp = [0] * (length + 1)
    return top_down_rod_cutting_helper(prices, length, dp)

def top_down_rod_cutting_helper(prices: List[int], length: int, dp: List[int]):
    """ Recursive top-down rod cutting. """
    if length == 0:
        return 0
    if dp[length] == 0:
        max_value = 0
        for i in range(1, length + 1):
            max_value = max(max_value, prices[i-1] + top_down_rod_cutting_helper(prices, length - i, dp))
        dp[length] = max_value
    return dp[length]

def bottom_up_rod_cutting(prices: List[int], length: int):
    """Bottom-up dynamic solution."""
    dp = [0] * (length + 1)
    for i in range(1, len(dp)):
        max_value = 0
        for j in range(i):
            max_value = max(max_value, prices[i - j - 1] + dp[j])
        dp[i] = max_value
    return dp[length]


class TestRodCutting(unittest.TestCase):
    """Unit test for rod_cutting."""

    def test_brute_force(self):
        self.assertEqual(brute_force_rod_cutting([], 0), 0)
        self.assertEqual(brute_force_rod_cutting([1, 5, 8, 9, 10, 10], 1), 1)
        self.assertEqual(brute_force_rod_cutting([1, 5, 8, 9, 10, 10], 4), 10)
        self.assertEqual(brute_force_rod_cutting([1, 5, 8, 9, 10, 10], 5), 13)

    def test_top_down(self):
        self.assertEqual(top_down_rod_cutting([], 0), 0)
        self.assertEqual(top_down_rod_cutting([1, 5, 8, 9, 10, 10], 1), 1)
        self.assertEqual(top_down_rod_cutting([1, 5, 8, 9, 10, 10], 4), 10)
        self.assertEqual(top_down_rod_cutting([1, 5, 8, 9, 10, 10], 5), 13)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_rod_cutting([], 0), 0)
        self.assertEqual(bottom_up_rod_cutting([1, 5, 8, 9, 10, 10], 1), 1)
        self.assertEqual(bottom_up_rod_cutting([1, 5, 8, 9, 10, 10], 4), 10)
        self.assertEqual(bottom_up_rod_cutting([1, 5, 8, 9, 10, 10], 5), 13)


if __name__ == '__main__':
    unittest.main()

