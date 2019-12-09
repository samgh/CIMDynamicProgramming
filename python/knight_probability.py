"""
 * Title: Knight Probability
 * Author: Sam Gavis-Hughson
 * Date: 12/5/2017
 * 
 * Given an NxM chessboard as well as a starting position, determine the 
 * probability that a knight starting at that position will remain on the 
 * board after x moves.
 * 
 * eg. 
 * height = 3, width = 3
 * startingPosition = (0, 0)
 * moves = 3
 * knightProbability(startingPosition, moves) = 0.0625
 * 
 * Execution: python knight_probability.py
"""
import unittest
from typing import List


class KnightProbability:
    def __init__(self, height: int, width: int):
        # Define the 8 possible moves that a knight can make in chess
        # (http://i.imgur.com/ismF2.png)
        self._valid_moves = [[1, 2], [1, -2], [-1, 2], [-1, -2], [2, 1], [2, -1], [-2, 1], [-2, -1]]
        self._height = height
        self._width = width

    def is_valid_square(self, row: int, col: int):
        """ Is (row, col) on the chessboard or not? """
        return row >= 0 and row < self._height and col >= 0 and col < self._width

    def brute_force_knight_probability(self, row: int, col: int, moves: int):
        # Brute force solution. Find every possible path and if it goes off the
        # board, return 0. Otherwise return 1. As you return, average the values
        # to get the overall average.
        
        # Base case:
        if moves == 0:
            if self.is_valid_square(row, col):
                return 1.0
            else:
                return 0.0
            
        # If we fall off the board, stop because we are not allowed to go off
        # the board and then come back on to the board
        if not self.is_valid_square(row, col):
            return 0.0
        
        # We can compute the probability by summing the probabilities of each
        # path and dividing by the number of paths.
        prob = 0.0
        for move in self._valid_moves:
            prob += self.brute_force_knight_probability(row + move[0], col + move[1], moves - 1)

        return prob / len(self._valid_moves)

    def top_down_knight_probability(self, row: int, col: int, moves: int):
        # Caching based on coordinates and which move we're on
        dp = [[[0.0]*(self._height+1)]*(self._width+1)]*(moves+1)

        return self.top_down_knight_probability_helper(row, col, moves, dp)

    def top_down_knight_probability_helper(self, row: int, col: int, moves: int, dp: List[List[List[int]]]):
        if moves == 0:
            if self.is_valid_square(row, col):
                return 1.0
            else:
                return 0.0
                
        if dp[row][col][moves] == 0:
            prob = 0.0
            for move in self._valid_moves:
                prob += self.top_down_knight_probability_helper(row + move[0], col + move[1], moves - 1, dp)
            dp[row][col][moves] = prob / len(self._valid_moves)
        return dp[row][col][moves]

    def bottom_up_knight_probability(self, row: int, col: int, moves: int):
        # Bottom-up dynamic solution. We flip the subproblems to say what is the
        # probability that a knight starting at any valid position will end up
        # at the desired position after a given number of moves. This is the
        # inverse of the top-down solution
        dp = [[[0.0]*(self._width)]*(self._height)]*(moves+1)

        # If moves is 0, then the probability is always 1 (we're assuming
        # the knight starts on the board)
        for i in range(len(dp[0])):
            for j in range(len(dp[0][0])):
                dp[0][i][j] = 1.0
        for i in range(1, len(dp)):
            for j in range(len(dp[0])):
                for k in range(len(dp[0][0])):
                    prob = 0.0
                    # Look at all the previous positions where they could
                    # have moved to this cell
                    for move in self._valid_moves:
                        if self.is_valid_square(j + move[0], k + move[1]):
                            prob += dp[i-1][j + move[0]][k + move[1]]
                        dp[i][j][k] = prob / len(self._valid_moves)

        return dp[moves][row][col]

    def bottom_up_optimized_knight_probability(self, row: int, col: int, moves: int):
        # Space-optimized bottom-up dynamic solution. We only ever refer to the 
        # immediate previous move, so we can overwrite the old moves.
        dp = [[0]*(self._width)]*(self._height)
        for i in range(len(dp)):
            for j in range(len(dp[0])):
                dp[i][j] = 1.0

        for i in range(moves):
            new_dp = [[0]*(len(dp[0]))]*(len(dp))
            for j in range(len(dp)):
                for k in range(len(dp[0])):
                    prob = 0.0
                    for move in self._valid_moves:
                        if self.is_valid_square(j + move[0], k + move[1]):
                            prob += dp[j + move[0]][k + move[1]]
                    new_dp[j][k] = prob / len(self._valid_moves)
            dp = new_dp
        return dp[row][col]


class TestKnightProbability(unittest.TestCase):
    """Unit test for knight_probability."""

    def test_brute_force(self):
        kp = KnightProbability(1, 1)
        self.assertEqual(kp.brute_force_knight_probability(0, 0, 1), 0.0)
        kp = KnightProbability(3, 3)
        self.assertEqual(kp.brute_force_knight_probability(0, 0, 0), 1.0)
        kp = KnightProbability(3, 3)
        self.assertEqual(kp.brute_force_knight_probability(0, 0, 1), 0.25)

    def test_top_down(self):
        kp = KnightProbability(1, 1)
        self.assertEqual(kp.top_down_knight_probability(0, 0, 1), 0.0)
        kp = KnightProbability(3, 3)
        self.assertEqual(kp.top_down_knight_probability(0, 0, 0), 1.0)
        kp = KnightProbability(3, 3)
        self.assertEqual(kp.top_down_knight_probability(0, 0, 1), 0.25)

    def test_bottom_up(self):
        kp = KnightProbability(1, 1)
        self.assertEqual(kp.bottom_up_knight_probability(0, 0, 1), 0.0)
        kp = KnightProbability(3, 3)
        self.assertEqual(kp.bottom_up_knight_probability(0, 0, 0), 1.0)

    def test_bottom_up_optimized(self):
        kp = KnightProbability(1, 1)
        self.assertEqual(kp.bottom_up_optimized_knight_probability(0, 0, 1), 0.0)
        kp = KnightProbability(3, 3)
        self.assertEqual(kp.bottom_up_optimized_knight_probability(0, 0, 0), 1.0)
        kp = KnightProbability(3, 3)
        self.assertEqual(kp.bottom_up_optimized_knight_probability(0, 0, 1), 0.25)


if __name__ == '__main__':
    unittest.main()
