/*
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
 * Execution: javac MakeChange.java && java MakeChange
 */

// We will assume that there is always a 1¢ coin available, so there is always
// a valid way to make the amount of change. We will also implement this as an
// object that is instantiated with a set of coin sizes so we don't have to 
// pass it into our function every time
public class MakingChange {
    private int[] coins;
    
    // Constructor
    public MakingChange(int[] coins) {
        this.coins = coins;
    }
    
    // Brute force solution
    public int bruteForceChange(int n) {
        if (n == 0) return 0;
        int minCoins = Integer.MAX_VALUE;
        
        // Try removing each coin from the total and see how many more coins
        // are required
        for (int coin : coins) {
            // Skip a coin if it's value is greater than the amount remaining
            if (n - coin >= 0) {
                int currMin = bruteForceChange(n - coin);
                minCoins = Math.min(minCoins, currMin);
            }
        }
        
        // Our recursive call removes one coin from the amount, so add it back
        return minCoins + 1;
    }
    
    // Top down dynamic solution
    public int topDownChange(int n) {
        int[] dp = new int[n + 1];
        return topDownChange(n, dp);
    }
    
    // Overloaded recursive function
    private int topDownChange(int n, int[] dp) {
        if (n == 0) return 0;
        if (dp[n] == 0) {
            int minCoins = Integer.MAX_VALUE;
            
            // Try each different coin to see which is best
            for (int coin : coins) {
                if (n - coin >= 0) {
                    int currMin = topDownChange(n - coin, dp);
                    minCoins = Math.min(minCoins, currMin);
                }
            }
            
            dp[n] = minCoins + 1;
        }
        return dp[n];
    }
    
    // Bottom up dynamic solution
    public int bottomUpChange(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int minCoins = Integer.MAX_VALUE;
            
            // Try removing each coin from the total and see which requires
            // the fewest additional coins
            for (int coin : coins) {
                if (i - coin >= 0) {
                    minCoins = Math.min(minCoins, dp[i - coin]);
                }
            }
            dp[i] = minCoins + 1;
        }
        
        return dp[n];
    }
    
    // Sample testcases
    public static void main(String[] args) {
        int[] americanCoins = new int[]{25, 10, 5, 1};
        int[] randomCoins = new int[]{10, 6, 1};
        
        (new TestCase(americanCoins, 1, 1)).run();
        (new TestCase(americanCoins, 6, 2)).run();
        (new TestCase(americanCoins, 47, 5)).run();
        (new TestCase(randomCoins, 1, 1)).run();
        (new TestCase(randomCoins, 8, 3)).run();
        (new TestCase(randomCoins, 11, 2)).run();
        (new TestCase(randomCoins, 12, 2)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private int[] coins;
        private int input;
        private int output;
        
        private TestCase(int[] coins, int input, int output) {
            this.coins = coins;
            this.input = input;
            this.output = output;
        }
        
        private void run() {
            MakingChange mc = new MakingChange(coins);
            assert mc.bruteForceChange(input) == output:
                "bruteForceChange failed for input = " + input;
            assert mc.topDownChange(input) == output:
                "topDownChange failed for input = " + input;
            assert mc.bottomUpChange(input) == output:
                "bottomUpChange failed for input = " + input;
        }
    }
}