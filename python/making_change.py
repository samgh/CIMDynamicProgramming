"""
 * Title: Making change
 * Author: Sam Gavis-Hughson
 * Date: 11/19/2017
 *
 * Given an integer representing a given amount of change, write a function to 
 * compute the minimum number of coins required to make that amount of change. 
 *
 * eg. (assuming American coins: 1, 5, 10, and 25 cents)
 * minCoins(1) = 1 (1)
 * minCoins(6) = 2 (5 + 1)
 * minCoins(49) = 7 (25 + 10 + 10 + 1 + 1 + 1 + 1)
 *
 * Execution: python making_change.py
"""
import unittest
from typing import List

# We will assume that there is always a 1 cent coin available, so there is
# always a valid way to make the amount of change. We will also implement this
# as an object that is instantiated with a set of coin sizes so we don't have
# to pass it into our function every time


class MakingChange:
    def __init__(self, coins: List[int]):
        self._coins = coins

    def brute_force_change(self, n: int):
        if n == 0:
            return 0
        min_coins = float("inf")

        # Try removing each coin from the total and see how many more coins
        # are required
        for coin in self._coins:
            # Skip a coin if it's value is greater than the amount remaining
            if n - coin >= 0:
                curr_min = self.brute_force_change(n-coin)
                min_coins = min(min_coins, curr_min)
        # Our recursive call removes one coin from the amount, so add it back
        return min_coins + 1

    def top_down_change(self, n: int):
        dp = [0] * (n+1)
        return self.top_down_change_helper(n, dp)
        
    def top_down_change_helper(self, n: int, dp: List[int]):
        if n == 0:
            return 0
        if dp[n] == 0:
            min_coins = float("inf")
            # Try each different coin to see which is best
            for coin in self._coins:
                if n - coin >= 0:
                    curr_min = self.top_down_change_helper(n-coin, dp)
                    min_coins = min(min_coins, curr_min)
            dp[n] = min_coins + 1
        return dp[n]

    def bottom_up_change(self, n: int):
        dp = [0] * (n+1)
        for i in range(1, n+1):
            min_coins = float("inf")

            # Try removing each coin from the total and see which requires
            # the fewest additional coins
            for coin in self._coins:
                if i - coin >= 0:
                    min_coins = min(min_coins, dp[i - coin])
            dp[i] = min_coins + 1
        return dp[n]


class TestMakingChange(unittest.TestCase):
    """Unit test for making_change."""

    def test_brute_force(self):
        american_coins = [25, 10, 5, 1]
        random_coins = [10, 6, 1]

        american_making_change = MakingChange(american_coins)
        self.assertEqual(american_making_change.brute_force_change(1), 1)
        self.assertEqual(american_making_change.brute_force_change(6), 2)
        self.assertEqual(american_making_change.brute_force_change(47), 5)

        random_making_change = MakingChange(random_coins)
        self.assertEqual(random_making_change.brute_force_change(1), 1)
        self.assertEqual(random_making_change.brute_force_change(8), 3)
        self.assertEqual(random_making_change.brute_force_change(11), 2)
        self.assertEqual(random_making_change.brute_force_change(12), 2)

    def test_top_down(self):
        american_coins = [25, 10, 5, 1]
        random_coins = [10, 6, 1]

        american_making_change = MakingChange(american_coins)
        self.assertEqual(american_making_change.top_down_change(1), 1)
        self.assertEqual(american_making_change.top_down_change(6), 2)
        self.assertEqual(american_making_change.top_down_change(47), 5)

        random_making_change = MakingChange(random_coins)
        self.assertEqual(random_making_change.top_down_change(1), 1)
        self.assertEqual(random_making_change.top_down_change(8), 3)
        self.assertEqual(random_making_change.top_down_change(11), 2)
        self.assertEqual(random_making_change.top_down_change(12), 2)

    def test_bottom_up(self):
        american_coins = [25, 10, 5, 1]
        random_coins = [10, 6, 1]

        american_making_change = MakingChange(american_coins)
        self.assertEqual(american_making_change.bottom_up_change(1), 1)
        self.assertEqual(american_making_change.bottom_up_change(6), 2)
        self.assertEqual(american_making_change.bottom_up_change(47), 5)

        random_making_change = MakingChange(random_coins)
        self.assertEqual(random_making_change.bottom_up_change(1), 1)
        self.assertEqual(random_making_change.bottom_up_change(8), 3)
        self.assertEqual(random_making_change.bottom_up_change(11), 2)
        self.assertEqual(random_making_change.bottom_up_change(12), 2)


if __name__ == '__main__':
    unittest.main()
