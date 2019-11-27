"""
* Title: Egg Drop
* Author: Sam Gavis-Hughson
* Date: 11/28/2017
*
* Consider dropping eggs off of a building. If you drop an egg from the Xth floor
* and it breaks, then you know for all eggs dropped from floor X or above, they
* will break. Similarly, if you drop an egg from floor X and it doesn't break, no
* egg will break when dropped from floors 1 through X. Given a building with N
* floors and E eggs, find the minimum number of drops needed to determine the
* maximum floor that you can drop an egg from without it breaking.
*
* eg.
*
* eggs = 1
* floors = 10
* eggDrop(eggs, floors) = 10
* If you only have one egg, you need to try every floor from 1 to floors.
*
* eggs = 2
* floors = 10
* eggDrop(eggs, floors) = 4
* Drop the egg from the 4th floor, then the 7th, then the 10th. Each time, if 
* the egg breaks, go to one above the previous test and progress up linearly.
* 
* Execution: python egg_drop.py
"""
import unittest
from typing import List


def brute_force_egg_drop(eggs: int, floors: int):
    """
    Brute force recursive solution. Try dropping an egg from every floor,
    then recursively try the case where it breaks and the case where it
    doesn't to see how many drops they take. floors indicates the range of
    floors that need to be tested, not the total floors. Eg. if the egg broke
    at floor 20 and not at floor 5, then floors = 15
    """
    if floors == 0:
        return 0
    if floors == 1:
        return 1
    # If we only have one egg, we have to go up one at a time through each
    # floor to guarantee that we find the exact floor
    if eggs == 1:
        return floors

    max_drops = float("inf")

    # Try dropping the egg from each height. Compute the worst case for if
    # it breaks and if it doesn't break
    for i in range(1, floors):
        # If the egg breaks, then the floor we're looking for could be any
        # floor less than i
        breaks = brute_force_egg_drop(eggs - 1, i - 1)

        # If the egg doesn't break, we can exclude all the floors below and
        # including the current floor (i)
        doesnt_break = brute_force_egg_drop(eggs, floors - i)
        max_drops = min(max_drops, max(breaks, doesnt_break))
    return max_drops + 1


def top_down_egg_drop(eggs: int, floors: int):
    dp = [[0]*(floors+1)]*(eggs+1)
    return top_down_egg_drop_helper(eggs, floors, dp)


def top_down_egg_drop_helper(eggs: int, floors: int, dp: List[List[int]]):
    if floors == 0:
        return 0
    if floors == 1:
        return 1
    if eggs == 1:
        return floors
    
    if dp[eggs][floors] == 0:
        max_drops = float("inf")
        for i in range(1, floors):
            breaks = top_down_egg_drop_helper(eggs - 1, i - 1, dp)
            doesnt_break = top_down_egg_drop_helper(eggs, floors - i, dp)
            max_drops = min(max_drops, max(breaks, doesnt_break))
        dp[eggs][floors] = max_drops + 1
    return dp[eggs][floors]


def bottom_up_egg_drop(eggs: int, floors: int):
    dp = [[0]*(floors+1)]*(eggs+1)
    for i in range(1, len(dp)):
        for j in range(1, len(dp[0])):
            if j == 1:
                dp[i][j] = 1
            elif i == 1:
                dp[i][j] = j
            else:
                max_drops = float("inf")
                for k in range(1, j+1):
                    breaks = dp[i-1][k-1]
                    doesnt_break = dp[i][j-k]
                    max_drops = min(max_drops, max(breaks, doesnt_break))
                dp[i][j] = max_drops + 1
    return dp[eggs][floors]


class TestEggDrop(unittest.TestCase):
    """Unit test for egg_drop."""

    def test_brute_force(self):
        self.assertEqual(brute_force_egg_drop(1, 1), 1)
        self.assertEqual(brute_force_egg_drop(1, 10), 10)
        self.assertEqual(brute_force_egg_drop(2, 10), 4)
        self.assertEqual(brute_force_egg_drop(2, 20), 6)
        self.assertEqual(brute_force_egg_drop(3, 10), 4)

    def test_top_down(self):
        self.assertEqual(top_down_egg_drop(1, 1), 1)
        self.assertEqual(top_down_egg_drop(1, 10), 10)
        self.assertEqual(top_down_egg_drop(2, 10), 4)
        self.assertEqual(top_down_egg_drop(2, 20), 6)
        self.assertEqual(top_down_egg_drop(3, 10), 4)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_egg_drop(1, 1), 1)
        self.assertEqual(bottom_up_egg_drop(1, 10), 10)
        self.assertEqual(bottom_up_egg_drop(2, 10), 4)
        self.assertEqual(bottom_up_egg_drop(3, 10), 4)


if __name__ == '__main__':
    unittest.main()


#         
#     // Sample testcases
#     public static void main(String[] args) {
#         (new TestCase(1, 1, 1)).run();
#         (new TestCase(1, 10, 10)).run();
#         (new TestCase(2, 10, 4)).run();
#         (new TestCase(2, 20, 6)).run();
#         (new TestCase(3, 10, 4)).run();
#         System.out.println("Passed all test cases");
#     }
#     
#     // Class for defining and running test cases
#     private static class TestCase {
#         private int eggs;
#         private int floors;
#         private int output;
#         
#         private TestCase(int eggs, int floors, int output) {
#             this.eggs = eggs;
#             this.floors = floors;
#             this.output = output;
#         }
#         
#         private void run() {
#             assert bruteForceEggDrop(eggs, floors) == output:
#                 "bruteForceEggDrop failed for eggs = " + eggs + ", floors = " + floors;
#             assert topDownEggDrop(eggs, floors) == output:
#                 "topDownEggDrop failed for eggs = " + eggs + ", floors = " + floors;
#             assert bottomUpEggDrop(eggs, floors) == output:
#                 "bottomUpEggDrop failed for eggs = " + eggs + ", floors = " + floors;
#         }
#     }
# }
