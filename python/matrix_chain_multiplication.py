"""
 * Title: Matrix Chain Multiplication
 * Author: Sam Gavis-Hughson
 * Date: 12/8/2017
 * 
 * Given a set of matrices, determine the minimum number of operations required
 * to multiply the matrices. In other words, determine the most efficient way
 * to group the matrices and multiply them.
 * 
 * For more detailed explanations:
 * * https://en.wikipedia.org/wiki/Matrix_multiplication
 * * https://en.wikipedia.org/wiki/Matrix_chain_multiplication
 * 
 * eg.
 * Matrix dimensions:
 * A = [40 x 20]
 * B = [20 x 30]
 * C = [30 x 10]
 * D = [10 x 30]
 * 
 * matrixChainMultiplication({A, B, C, D}) = 26000
 * AxBxCxD
 * (Ax(BxC))xD
 * 6000 + (Ax[20x10])xD
 * 6000 + 8000 + [40x10]xD
 * 6000 + 8000 + 12000
 * 26000
 * 
 * Execution: python matrix_chain_multiplication.py
"""
import unittest
from typing import List


class Matrix:
    def __init__(self, rows: int, cols: int):
        self.rows = rows
        self.cols = cols

    def __str__(self):
        return f"[{self.rows}x{self.cols}]"


def brute_force_mcm(arr: List[Matrix]):
    # Brute force solution. We will consider every possible grouping of 
    # matrices by recursively dividing into smaller and smaller sets until we
    # have only 1 or 2 matrices in our set. This is similar to the EggDrop 
    # problem
    return brute_force_mcm_helper(arr, 0, len(arr) - 1)


def brute_force_mcm_helper(arr: List[Matrix], i: int, j: int):
    # Helper method for brute-force

    # When we have a single matrix, we don't have to do any operations.
    if i == j:
        return 0

    min_val = float("inf")

    # Try splitting our list of matrices at each possible point and see
    # which results in the least total operations
    for k in range(i, j):
        # The number of operations is the number of operations to compute
        # the left and right matrix plus the number of operations to
        # multiply the two resulting matrices together
        ops = brute_force_mcm_helper(arr, i, k) + brute_force_mcm_helper(arr, k+1, j) + arr[i].rows * arr[k].cols * arr[j].cols
        min_val = min(min_val, ops)

    return min_val


def top_down_mcm(arr: List[Matrix]):
    # Top-down dynamic solution
    dp = dict()
    for i in range(len(arr)):
        for j in range(len(arr)):
            dp[(i, j)] = 0
    return top_down_mcm_helper(arr, 0, len(arr) - 1, dp)


def top_down_mcm_helper(arr: List[Matrix], i: int, j: int, dp: dict):
    if i == j:
        return 0

    if dp[(i, j)] == 0:
        min_val = float("inf")
        for k in range(i, j):
            ops = top_down_mcm_helper(arr, i, k, dp) + top_down_mcm_helper(arr, k+1, j, dp) + arr[i].rows * arr[k].cols * arr[j].cols
            min_val = min(min_val, ops)
        dp[(i, j)] = min_val

    return dp[(i, j)]


def bottom_up_mcm(arr: List[Matrix]):
    # Bottom-up dynamic solution.
    dp = dict()
    for i in range(len(arr)):
        for j in range(len(arr)):
            dp[(i, j)] = 0

    #  Unlike the previous problems, the previous subproblem is not based on
    #  the absolute values of i and j, but rather the difference. Our
    #  smallest subproblems are when j = i+1. We need to compute every value
    #  where this is true before we can compute for j = i+2. Because of this
    #  we iterate over an increasing gap between i and j, rather than the
    #  absolute values of i and j.
    for gap in range(1, len(arr)):
        i = 0
        while gap + i < len(arr):
            j = i + gap
            min_val = float("inf")
            for k in range(i, j):
                ops = dp[(i, k)] + dp[(k+1, j)] + arr[i].rows * arr[k].cols * arr[j].cols
                min_val = min(min_val, ops)
            dp[(i, i+gap)] = min_val
            i += 1
    return dp[(0, len(arr) - 1)]


class TestMatrixChainMultiplication(unittest.TestCase):
    """Unit test for matrix_chain_multiplication."""

    def test_brute_force(self):
        matrices = [Matrix(40, 20)]
        self.assertEqual(brute_force_mcm(matrices), 0)

        matrices = [Matrix(10, 20), Matrix(20, 30)]
        self.assertEqual(brute_force_mcm(matrices), 6000)

        matrices = [Matrix(40, 20), Matrix(20, 30), Matrix(30, 10), Matrix(10, 30)]
        self.assertEqual(brute_force_mcm(matrices), 26000)

        matrices = [Matrix(10, 20), Matrix(20, 30), Matrix(30, 40), Matrix(40, 30)]
        self.assertEqual(brute_force_mcm(matrices), 30000)

    def test_top_down(self):
        matrices = [Matrix(40, 20)]
        self.assertEqual(top_down_mcm(matrices), 0)

        matrices = [Matrix(10, 20), Matrix(20, 30)]
        self.assertEqual(top_down_mcm(matrices), 6000)

        matrices = [Matrix(40, 20), Matrix(20, 30), Matrix(30, 10), Matrix(10, 30)]
        self.assertEqual(top_down_mcm(matrices), 26000)

        matrices = [Matrix(10, 20), Matrix(20, 30), Matrix(30, 40), Matrix(40, 30)]
        self.assertEqual(top_down_mcm(matrices), 30000)

    def test_bottom_up(self):
        matrices = [Matrix(40, 20)]
        self.assertEqual(bottom_up_mcm(matrices), 0)

        matrices = [Matrix(10, 20), Matrix(20, 30)]
        self.assertEqual(bottom_up_mcm(matrices), 6000)

        matrices = [Matrix(40, 20), Matrix(20, 30), Matrix(30, 10), Matrix(10, 30)]
        self.assertEqual(bottom_up_mcm(matrices), 26000)

        matrices = [Matrix(10, 20), Matrix(20, 30), Matrix(30, 40), Matrix(40, 30)]
        self.assertEqual(bottom_up_mcm(matrices), 30000)


if __name__ == '__main__':
    unittest.main()
