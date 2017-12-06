/*
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
 * Execution: javac KnightProbability.java && java KnightProbability 
 */
import java.util.Arrays;

public class KnightProbability {
    // Define the 8 possible moves that a knight can make in chess 
    // (http://i.imgur.com/ismF2.png)
    private static int[][] validMoves = {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, 
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
    private int height; 
    private int width;
    
    // Define a basic object to simplify keeping track of the heigh and width
    public KnightProbability(int height, int width) {
        this.height = height;
        this.width = width;
    }
    
    // Is (row, col) on the chessboard or not?
    private boolean isValidSquare(int row, int col) {
        return row >= 0 && row < this.height && col >= 0 && col < this.width;
    }

    // Brute force solution. Find every possible path and if it goes off the
    // board, return 0. Otherwise return 1. As you return, average the values
    // to get the overall average.
    public double bruteForceKnightProbability(int row, int col, int moves) {
        // Base case. If we're on the board, probability is 1. Otherwise 0.
        if (moves == 0) return isValidSquare(row, col) ? 1.0 : 0.0;
        
        // If we fall off the board, stop because we are not allowed to go off
        // the board and then come back on to the board
        if (!isValidSquare(row, col)) return 0.0;
        
        // We can compute the probability by summing the probabilities of each
        // path and dividing by the number of paths.
        double prob = 0.0;
        for (int[] move : validMoves) {
            prob += bruteForceKnightProbability(row + move[0], col + move[1], moves - 1);
        }
        
        return prob / validMoves.length;
    }
    
    // Top-down dynamic solution
    public double topDownKnightProbability(int row, int col, int moves) {
        // Caching based on the coordinates and which move we're on
        double[][][] dp = new double[this.height][this.width][moves + 1];
        return topDownKnightProbability(row, col, moves, dp);
    }
    
    // Overloaded recursive method
    private double topDownKnightProbability(int row, int col, int moves, double[][][] dp) {
        if (moves == 0) return isValidSquare(row, col) ? 1.0 : 0.0;
        if (!isValidSquare(row, col)) return 0.0;
        
        if (dp[row][col][moves] == 0) {
            double prob = 0.0;
            for (int[] move : validMoves) {
                prob += topDownKnightProbability(row + move[0], col + move[1], moves - 1, dp);
            }
            
            dp[row][col][moves] = prob / validMoves.length;
        }
        return dp[row][col][moves];
    }
    
    // Bottom-up dynamic solution. We flip the subproblems to say what is the
    // probability that a knight starting at any valid position will end up
    // at the desired position after a given number of moves. This is the 
    // inverse of the top-down solution
    public double bottomUpKnightProbability(int row, int col, int moves) {
        double[][][] dp = new double[moves + 1][this.height][this.width];
        // If moves is 0, then the probability is always 1 (we're assuming
        // the knight starts on the board)
        for (int i = 0; i < dp[0].length; i++) {
            for (int j = 0; j < dp[0][0].length; j++) {
                dp[0][i][j] = 1.0;
            }
        }
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                for (int k = 0; k < dp[0][0].length; k++) {
                    double prob = 0.0;
                    // Look at all the previous positions where they could
                    // have moved to this cell
                    for (int[] move : validMoves) {
                        if (isValidSquare(j + move[0], k + move[1])) {
                            prob += dp[i-1][j + move[0]][k + move[1]];
                        }
                        dp[i][j][k] = prob / validMoves.length;
                    }
                }
            }
        }
        return dp[moves][row][col];
    }
    
    // Space-optimized bottom-up dynamic solution. We only ever refer to the 
    // immediate previous move, so we can overwrite the old moves.
    public double bottomUpKnightProbabilitySpaceOptimized(int row, int col, int moves) {
        double[][] dp = new double[this.height][this.width];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                dp[i][j] = 1.0;
            }
        }
        for (int i = 0; i < moves; i++) {
            double[][] newDp = new double[dp.length][dp[0].length];
            for (int j = 0; j < dp.length; j++) {
                for (int k = 0; k < dp[0].length; k++) {
                    double prob = 0.0;
                    for (int[] move : validMoves) {
                        if (isValidSquare(j + move[0], k + move[1])) {
                            prob += dp[j + move[0]][k + move[1]];
                        }
                    }
                    newDp[j][k] = prob / validMoves.length;
                }
            }
            dp = newDp;
        }
        return dp[row][col];
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(1, 1, 0, 0, 1, 0.0)).run();
        (new TestCase(2, 3, 0, 0, 2, 0.015625)).run();
        (new TestCase(3, 3, 0, 0, 0, 1.0)).run();
        (new TestCase(3, 3, 0, 0, 1, 0.25)).run();
        (new TestCase(3, 3, 0, 0, 2, 0.0625)).run();
        (new TestCase(3, 3, 0, 0, 5, 0.0009765625)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private int height;
        private int width;
        private int row;
        private int col;
        private int moves;
        private double output;
        
        private TestCase(int height, int width, int row, int col, int moves, double output) {
            this.height = height;
            this.width = width;
            this.row = row;
            this.col = col;
            this.moves = moves;
            this.output = output;
        }
        
        private void run() {
            KnightProbability kp = new KnightProbability(height, width);
            assert kp.bruteForceKnightProbability(row, col, moves) == output:
                "bruteForceKnightProbability failed on " + height + "x" + width 
                + " board starting at " + row + "," + col;
            assert kp.topDownKnightProbability(row, col, moves) == output:
                "topDownKnightProbability failed on " + height + "x" + width 
                + " board starting at " + row + "," + col;
            assert kp.bottomUpKnightProbability(row, col, moves) == output:
                "bottomUpKnightProbability failed on " + height + "x" + width 
                + " board starting at " + row + "," + col;
            assert kp.bottomUpKnightProbabilitySpaceOptimized(row, col, moves) == output:
                "bottomUpKnightProbabilitySpaceOptimized failed on " + height + "x" + width 
                + " board starting at " + row + "," + col;
        }
    }
}