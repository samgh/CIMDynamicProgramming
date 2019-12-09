"""
 * Title: Matrix Path
 * Author: Sam Gavis-Hughson
 * Date: 12/1/2017
 * 
 * Given a matrix, find the path from top left to bottom right with the greatest 
 * product by moving only down and right.
 * 
 * eg. 
 * arr = {-1, 2,  3}
 *       { 4, 5, -6}
 *       { 7, 8,  9}
 * 
 * matrixPath(arr) = 1080 (-1 * 4 * 5 * -6 * 9)
 * 
 * Execution: python matrix_path.py
"""
import unittest
from typing import List


def brute_force_matrix_path(arr: List[List[int]]):
    # Brute force solution. Recursively try every path. We must compute both
    # the maximum (result[0]) and minimum (result[1]) value for each path
    # because if the current value is negative, we want to multiply it by the
    # minimum path. That way any negatives will cancel out.
    result = brute_force_matrix_path_helper(arr, 0, 0)
    return result[0]


def brute_force_matrix_path_helper(arr:List[List[int]], i: int, j: int):
    # Overloaded recursive method. Return length 2 array where arr[0] is max
    # and arr[1] is min

    # If we're out of bounds, return null
    if i == len(arr) or j == len(arr[0]):
        return None
    down = brute_force_matrix_path_helper(arr, i+1, j)
    right = brute_force_matrix_path_helper(arr, i, j+1)

    # Bottom-right corner
    if down is None and right is None:
        return [arr[i][j], arr[i][j]]

    min_val = float("inf")
    max_val = float("-inf")

    # If we can go down, try that path
    if down:
        min_val = min(min_val, min(arr[i][j] * down[0], arr[i][j] * down[1]))
        max_val = max(max_val, max(arr[i][j] * down[0], arr[i][j] * down[1]))

    # If we can go right, try that path
    if right:
        min_val = min(min_val, min(arr[i][j] * right[0], arr[i][j] * right[1]))
        max_val = max(max_val, max(arr[i][j] * right[0], arr[i][j] * right[1]))

    return [max_val, min_val]


def top_down_matrix_path(arr: List[List[int]]):
    # Top-down dynamic solution.
    dp = dict()

    # We will indicate that a value is unset by setting the max to be less
    # than the min. When it's set, they should at least be equal
    for i in range(len(arr)):
        for j in range(len(arr[0])):
            dp[(i, j, 0)] = -1
            dp[(i, j, 1)] = 0
    result = top_down_matrix_path_helper(arr, 0, 0, dp)
    return result[0]


def top_down_matrix_path_helper(arr: List[List[int]], i: int, j: int, dp: dict):
    down = None
    right = None
    if i == len(arr) or j == len(arr[0]):
        return None

    # Unset if the max is less than the min.
    if dp[(i, j, 0)] < dp[(i, j, 1)]:
        down = top_down_matrix_path_helper(arr, i+1, j, dp)
        right = top_down_matrix_path_helper(arr, i, j+1, dp)

    if down is None and right is None:
        return [arr[i][j], arr[i][j]]

    min_val = float("inf")
    max_val = float("-inf")

    if down is not None:
        min_val = min(min_val, min(arr[i][j] * down[0], arr[i][j] * down[1]))
        max_val = max(max_val, max(arr[i][j] * down[0], arr[i][j] * down[1]))

    if right is not None:
        min_val = min(min_val, min(arr[i][j] * right[0], arr[i][j] * right[1]))
        max_val = max(max_val, max(arr[i][j] * right[0], arr[i][j] * right[1]))

    dp[(i, j, 0)] = max_val
    dp[(i, j, 1)] = min_val

    return [dp[(i, j, 0)], dp[(i, j, 1)]]


def bottom_up_matrix_path(arr: List[List[int]]):
    # Bottom-up dynamic solution.
    dp = dict()

    # Build-up max path starting at arr[0][0]
    for i in range(len(arr)):
        for j in range(len(arr[0])):
            # Base case
            if i == 0 and j == 0:
                dp[(i, j, 0)] = arr[i][j]
                dp[(i, j, 1)] = arr[i][j]
                continue

            min_val = float("inf")
            max_val = float("-inf")

            if i != 0:
                min_val = min(min_val, min(arr[i][j] * dp[(i-1, j, 0)], arr[i][j] * dp[(i-1, j, 1)]))
                max_val = max(max_val, max(arr[i][j] * dp[(i-1, j, 0)], arr[i][j] * dp[(i-1, j, 1)]))
            if j != 0:
                min_val = min(min_val, min(arr[i][j] * dp[(i, j-1, 0)], arr[i][j] * dp[(i, j-1, 1)]))
                max_val = max(max_val, max(arr[i][j] * dp[(i, j-1, 0)], arr[i][j] * dp[(i, j-1, 1)]))
            
            dp[(i, j, 0)] = max_val
            dp[(i, j, 1)] = min_val

    return dp[(i, j, 0)]


class TestMatrixPath(unittest.TestCase):
    """Unit test for matrix_path."""

    def test_brute_force(self):
        self.assertEqual(brute_force_matrix_path([[1]]), 1)

        self.assertEqual(brute_force_matrix_path([[1, 2, 3],
                                                  [4, 5, 6],
                                                  [7, 8, 9]]), 2016)

        self.assertEqual(brute_force_matrix_path([[-1, 2, 3],
                                                  [4, 5, -6],
                                                  [7, 8, 9]]), 1080)

        self.assertEqual(brute_force_matrix_path([[-1, 2, 3],
                                                  [4, 5, 6],
                                                  [7, 8, 9]]), -324)

    def test_top_down(self):
        self.assertEqual(top_down_matrix_path([[1]]), 1)

        self.assertEqual(top_down_matrix_path([[1, 2, 3],
                                               [4, 5, 6],
                                               [7, 8, 9]]), 2016)

        self.assertEqual(top_down_matrix_path([[-1, 2, 3],
                                               [4, 5, -6],
                                               [7, 8, 9]]), 1080)

    def test_bottom_up(self):
        self.assertEqual(bottom_up_matrix_path([[1]]), 1)

        self.assertEqual(bottom_up_matrix_path([[1, 2, 3],
                                                [4, 5, 6],
                                                [7, 8, 9]]), 2016)

        self.assertEqual(bottom_up_matrix_path([[-1, 2, 3],
                                                [4, 5, -6],
                                                [7, 8, 9]]), 1080)

        self.assertEqual(bottom_up_matrix_path([[-1, 2, 3],
                                                [4, 5, 6],
                                                [7, 8, 9]]), -324)

if __name__ == '__main__':
    unittest.main()
