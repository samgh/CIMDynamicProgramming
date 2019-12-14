"""
 * Title: 0-1 Knapsack
 * Author: Sam Gavis-Hughson
 * Date: 08/01/2017
 * 
 * Given a list of items with values and weights, as well as a max weight, 
 * find the maximum value you can generate from items, where the sum of the 
 * weights is less than or equal to the max.
 * 
 * eg.
 * items = {(w:1, v:6), (w:2, v:10), (w:3, v:12)}
 * maxWeight = 5
 * knapsack(items, maxWeight) = 22
 * 
 * Execution: python knapsack.py
"""
import unittest

from collections import defaultdict
from typing import List


class Item:
    def __init__(self, weight: int, value: int):
        self.weight = weight
        self.value = value


def brute_force_knapsack(items: List[Item], W: int):
    return brute_force_knapsack_helper(items, W, 0)


def brute_force_knapsack_helper(items: List[Item], W: int, i: int):
    # If we've gone through all the items, return
    if i == len(items):
        return 0
    # If the item is too big to fill the remaining space, skip it
    if W - items[i].weight < 0:
        return brute_force_knapsack_helper(items, W, i+1)

    # Find the maximum of including and not including the current item
    return max(brute_force_knapsack_helper(items, W - items[i].weight, i+1) + items[i].value,
               brute_force_knapsack_helper(items, W, i+1))


def top_down_knapsack(items: List[Item], W: int):
    dp = dict()
    for i in range(len(items)):
        for j in range(W+1):
            dp[(i, j)] = -1
    return top_down_knapsack_helper(items, W, 0, dp)


def top_down_knapsack_helper(items: List[Item], W: int, i: int, dp: dict):
    if i == len(items):
        return 0
    if dp[(i, W)] == -1:
        dp[(i, W)] = top_down_knapsack_helper(items, W, i+1, dp)
        if W - items[i].weight >= 0:
            include = top_down_knapsack_helper(items, W-items[i].weight, i+1, dp) + items[i].value
            dp[(i, W)] = max(dp[(i, W)], include)
    return dp[(i, W)]


def bottom_up_knapsack(items: List[Item], W: int):
    if len(items) == 0 or W == 0:
        return 0

    # Initialize base cache
    dp = defaultdict(int)

    # For each item and weight, compute the max value of the items up to 
    # that item that doesn't go over W weight
    for i in range(1, len(items) + 1):
        for j in range(W+1):
            if items[i-1].weight > j:
                dp[(i, j)] = dp[(i-1, j)]
            else:
                include = dp[(i-1, j-items[i-1].weight)] + items[i-1].value
                dp[(i, j)] = max(dp[(i-1, j)], include)
    return dp[(len(items), W)]


class TestKnapsack(unittest.TestCase):
    """Unit test for knapsack."""

    def test_brute_force(self):
        self.assertEqual(brute_force_knapsack([], 0), 0)

        items = [Item(4, 5), Item(1, 8), Item(2, 4), Item(3, 0), Item(2, 5), Item(2, 3)]
        self.assertEqual(brute_force_knapsack(items, 3), 13)

        items = [Item(4, 5), Item(1, 8), Item(2, 4), Item(3, 0), Item(2, 5), Item(2, 3)]
        self.assertEqual(brute_force_knapsack(items, 8), 20)

    def test_top_down(self):
        self.assertEqual(top_down_knapsack([], 0), 0)

        items = [Item(4, 5), Item(1, 8), Item(2, 4), Item(3, 0), Item(2, 5), Item(2, 3)]
        self.assertEqual(top_down_knapsack(items, 3), 13)

        items = [Item(4, 5), Item(1, 8), Item(2, 4), Item(3, 0), Item(2, 5), Item(2, 3)]
        self.assertEqual(top_down_knapsack(items, 8), 20)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_knapsack([], 0), 0)

        items = [Item(4, 5), Item(1, 8), Item(2, 4), Item(3, 0), Item(2, 5), Item(2, 3)]
        self.assertEqual(bottom_up_knapsack(items, 3), 13)

        items = [Item(4, 5), Item(1, 8), Item(2, 4), Item(3, 0), Item(2, 5), Item(2, 3)]
        self.assertEqual(bottom_up_knapsack(items, 8), 20)


if __name__ == '__main__':
    unittest.main()

