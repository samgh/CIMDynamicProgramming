"""
 * Title: Fibonacci
 * Author: Sam Gavis-Hughson
 * Date: 11/19/2017
 *
 * Given an integer n, write a function that will return the nth Fibonacci
 * number.
 *
 * eg.
 * fib(0) = 0
 * fib(1) = 1
 * fib(5) = 5
 * fib(10) = 55
 *
 * Execution: python fibonacci.py
"""
import unittest
from typing import List

# We will assume for all solutions that n >= 0 and that int is sufficient
# to hold the result.


def brute_force_fib(n: int):
    """Brute-force solution."""
    if n == 0:
        return 0
    if n == 1:
        return 1
    return brute_force_fib(n-1) + brute_force_fib(n-2)


def top_down_fib(n: int):
    """Top-down dynamic solution."""
    dp = [0] * (n + 1)
    return top_down_fib_helper(n, dp)


def top_down_fib_helper(n: int, dp: List[int]):
    """Top-down dynamic solution helper."""
    if n == 0:
        return 0
    if n == 1:
        return 1
    # If value is not set in cache, compute it.
    if dp[n] == 0:
        dp[n] = top_down_fib_helper(n-1, dp) + top_down_fib_helper(n-2, dp)
    return dp[n]


def bottom_up_fib(n: int):
    """Bottom-up dynamic soluton."""
    if n == 0:
        return 0

    # Initialize cache
    dp = [0] * (n + 1)
    dp[1] = 1

    # Fill cache iteratively
    for i in range(2, n+1):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]


def bottom_up_optimized_fib(n: int):
    """Space-optimized bottom-up dynamic solution."""
    if n == 0:
        return 0
    if n == 1:
        return 1

    n1, n2 = 1, 0
    for i in range(2, n):
        n0 = n1 + n2
        n2 = n1
        n1 = n0
    return n1 + n2


class TestFibonacci(unittest.TestCase):
    """Unit test for fibonacci."""

    def test_brute_force(self):
        self.assertEqual(brute_force_fib(0), 0)
        self.assertEqual(brute_force_fib(1), 1)
        self.assertEqual(brute_force_fib(2), 1)
        self.assertEqual(brute_force_fib(5), 5)
        self.assertEqual(brute_force_fib(10), 55)

    def test_top_down(self):
        self.assertEqual(top_down_fib(0), 0)
        self.assertEqual(top_down_fib(1), 1)
        self.assertEqual(top_down_fib(2), 1)
        self.assertEqual(top_down_fib(5), 5)
        self.assertEqual(top_down_fib(10), 55)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_fib(0), 0)
        self.assertEqual(bottom_up_fib(1), 1)
        self.assertEqual(bottom_up_fib(2), 1)
        self.assertEqual(bottom_up_fib(5), 5)
        self.assertEqual(bottom_up_fib(10), 55)

    def test_bottom_up_optimized(self):
        self.assertEqual(bottom_up_optimized_fib(0), 0)
        self.assertEqual(bottom_up_optimized_fib(1), 1)
        self.assertEqual(bottom_up_optimized_fib(2), 1)
        self.assertEqual(bottom_up_optimized_fib(5), 5)
        self.assertEqual(bottom_up_optimized_fib(10), 55)


if __name__ == '__main__':
    unittest.main()
