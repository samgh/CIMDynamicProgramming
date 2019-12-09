"""
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
 * Execution: python floor_tiling.py
"""
import unittest
from typing import List


def brute_force_floor_tiling(n: int):
    """Brute-force solution."""

    # Brute force solution. We can either place one tile vertically or two 
    # tiles horizontally. Respectively these will cover width 1 and width 2.
    if n == 0 or n == 1:
        return 1
    return brute_force_floor_tiling(n-1) + brute_force_floor_tiling(n-2)


def top_down_floor_tiling(n: int):
    """Top-down dynamic solution."""
    dp = [0] * (n + 1)
    return top_down_floor_tiling_helper(n, dp)


def top_down_floor_tiling_helper(n: int, dp: List[int]):
    if n == 0 or n == 1:
        return 1
    if dp[n] == 0:
        dp[n] = top_down_floor_tiling_helper(n-1, dp) + top_down_floor_tiling_helper(n-2, dp)
    return dp[n]


def bottom_up_floor_tiling(n: int):
    dp = [0] * (n+1)
    dp[0] = 1
    dp[1] = 1
    for i in range(2, len(dp)):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]


def bottom_up_floor_tiling_space_optimized(n: int):
    n0, n1 = 1, 1
    for i in range(2, n+1):
        temp = n0 + n1
        n0, n1 = n1, temp
    return n1


class TestFloorTiling(unittest.TestCase):
    """Unit test for floor tiling."""

    def test_brute_force(self):
        self.assertEqual(brute_force_floor_tiling(1), 1)
        self.assertEqual(brute_force_floor_tiling(3), 3)
        self.assertEqual(brute_force_floor_tiling(5), 8)
        self.assertEqual(brute_force_floor_tiling(10), 89)

    def test_top_down(self):
        self.assertEqual(top_down_floor_tiling(1), 1)
        self.assertEqual(top_down_floor_tiling(3), 3)
        self.assertEqual(top_down_floor_tiling(5), 8)
        self.assertEqual(top_down_floor_tiling(10), 89)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_floor_tiling(1), 1)
        self.assertEqual(bottom_up_floor_tiling(3), 3)
        self.assertEqual(bottom_up_floor_tiling(5), 8)
        self.assertEqual(bottom_up_floor_tiling(10), 89)

    def test_bottom_up_optimized(self):
        self.assertEqual(bottom_up_floor_tiling_space_optimized(1), 1)
        self.assertEqual(bottom_up_floor_tiling_space_optimized(3), 3)
        self.assertEqual(bottom_up_floor_tiling_space_optimized(5), 8)
        self.assertEqual(bottom_up_floor_tiling_space_optimized(10), 89)


if __name__ == '__main__':
    unittest.main()
    
