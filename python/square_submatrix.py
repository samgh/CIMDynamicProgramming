"""
 * Title: Square submatrix
 * Author: Sam Gavis-Hughson
 * Date: 11/21/17
 *
 * Given a 2D boolean array, find the largest square subarray of true values. 
 * The return value should be the side length of the largest square subarray 
 * subarray.
 *
 * eg.
 * squareSubmatrix([false, true, false, false]) = 2
 *                 [true,  TRUE, TRUE,  true ]
 *                 [false, TRUE, TRUE,  false]
 * (the solution is the submatrix in all caps)
 *
 * Execution: python square_matrix.py
"""
import unittest
from typing import List


def brute_force_square_submatrix(arr: List[List[bool]]):
    """Brute-force solution."""
    max_val = 0
    # Compute recursively for each cell what it is the upper left corner of        
    for i in range(len(arr)):
        for j in range(len(arr[0])):
            max_val = max(max_val, brute_force_square_submatrix_helper(arr, i, j))
    return max_val


def brute_force_square_submatrix_helper(arr: List[List[bool]], i, j):
    """ Recursive helper function """

    # If we get to the bottom or right of the matrix, we can't go any 
    # further
    if i == len(arr) or j == len(arr[0]):
        return 0

    # If the cell is False then it is not part of a valid submatrix
    if arr[i][j] is False:
        return 0

    # Find the size of the right, bottom, and bottom right submatrices and
    # add 1 to the minimum of those 3 to get the result
    return 1 + min(min(brute_force_square_submatrix_helper(arr, i+1, j),
                       brute_force_square_submatrix_helper(arr, i, j+1)),
                   brute_force_square_submatrix_helper(arr, i+1, j+1))


def top_down_square_submatrix(arr: List[List[bool]]):
    # Initialize cache. Don't need to initialize to -1 because the only 
    # cells that will be 0 are ones that are False and we want to skip
    # those ones anyway
    dp = [[0]*(len(arr[0]))]*(len(arr))
    max_val = 0
    for i in range(len(arr)):
        for j in range(len(arr[0])):
            max_val = max(max_val, top_down_square_submatrix_helper(arr, i, j, dp))
    return max_val


def top_down_square_submatrix_helper(arr: List[List[bool]], i: int, j: int, dp: List[List[int]]):
    """ Recursive helper function. """
    if i == len(arr) or j == len(arr[0]):
        return 0
    if arr[i][j] is False:
        return 0

    # If the value is set in the cache return it. Otherwise compute and 
    # save to cache
    if dp[i][j] == 0:
        dp[i][j] = 1 + min(min(top_down_square_submatrix_helper(arr, i+1, j, dp),
                               top_down_square_submatrix_helper(arr, i, j+1, dp)),
                           top_down_square_submatrix_helper(arr, i+1, j+1, dp))
    return dp[i][j]


def bottom_up_square_submatrix(arr: List[List[bool]]):
    """ Bottom-up dynamic solution. """
    max_val = 0

    # Initialize cache
    dp = [[0]*(len(arr[0]))]*(len(arr))
    
    # Iterate over the matrix to compute all values.
    for i in range(len(dp)):
        for j in range(len(dp[0])):
            # If we are in the first row or column then the value is just
            # 1 if that cell is true and 0 otherwise. In other rows and
            # columns, need to look up and to the left.
            if i == 0 or j == 0:
                if arr[i][j]:
                    dp[i][j] = 1
                else:
                    dp[i][j] = 0
            elif arr[i][j]:
                dp[i][j] = min(min(dp[i][j-1], dp[i-1][j]), 
                                dp[i-1][j-1]) + 1
            max_val = max(max_val, dp[i][j])
    return max_val


class TestSquareMatrix(unittest.TestCase):
    """Unit test for square_matrix."""

    def test_brute_force(self):
        self.assertEqual(brute_force_square_submatrix([[True]]), 1)
        self.assertEqual(brute_force_square_submatrix([[False]]), 0)

        self.assertEqual(brute_force_square_submatrix(
            [[True, True, True, False],
             [False, True, True, True],
             [True, True, True, True]]), 2)

        self.assertEqual(brute_force_square_submatrix(
            [[True, True, True, True],
             [False, True, True, True],
             [True, True, True, True]]), 3)

    def test_top_down(self):
        self.assertEqual(top_down_square_submatrix([[True]]), 1)
        self.assertEqual(top_down_square_submatrix([[False]]), 0)

        self.assertEqual(top_down_square_submatrix(
            [[True, True, True, False],
             [False, True, True, True],
             [True, True, True, True]]), 2)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_square_submatrix([[True]]), 1)
        self.assertEqual(bottom_up_square_submatrix([[False]]), 0)

        self.assertEqual(bottom_up_square_submatrix(
            [[True, True, True, True],
             [False, True, True, True],
             [True, True, True, True]]), 3)


if __name__ == '__main__':
    unittest.main()
