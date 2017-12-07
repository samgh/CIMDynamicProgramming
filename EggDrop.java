/*
 * Title: Egg Drop
 * Author: Sam Gavis-Hughson
 * Date: 11/28/2017
 * 
 * Consider dropping eggs off of a building. If you drop an egg from the Xth 
 * floor and it breaks, then you know for all eggs dropped from floor X or above,
 * they will break. Similarly, if you drop an egg from floor X and it doesn't 
 * break, no egg will break when dropped from floors 1 through X. Given a building 
 * with N floors and E eggs, find the minimum number of drops needed to determine 
 * the maximum floor that you can drop an egg from without it breaking.
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
 * Execution: javac EggDrop.java && java EggDrop
 */

public class EggDrop {
    
    // Brute force recursive solution. Try dropping an egg from every floor,
    // then recursively try the case where it breaks and the case where it
    // doesn't to see how many drops they take. floors indicates the range of
    // floors that need to be tested, not the total floors. Eg. if the egg broke
    // at floor 20 and not at floor 5, then floors = 15
    public static int bruteForceEggDrop(int eggs, int floors) {
        if (floors == 0) return 0;
        if (floors == 1) return 1;
        // If we only have one egg, we have to go up one at a time through each
        // floor to guarantee that we find the exact floor
        if (eggs == 1) return floors;
        
        int maxDrops = Integer.MAX_VALUE;
        // Try dropping the egg from each height. Compute the worst case for if
        // it breaks and if it doesn't break
        for (int i = 1; i <= floors; i++) {
            // If the egg breaks, then the floor we're looking for could be any
            // floor less than i
            int breaks = bruteForceEggDrop(eggs - 1, i - 1);
            // If the egg doesn't break, we can exclude all the floors below and
            // including the current floor (i)
            int doesntBreak = bruteForceEggDrop(eggs, floors - i);
            maxDrops = Math.min(maxDrops, Math.max(breaks, doesntBreak));                   
        }
        return maxDrops + 1;
    }
    
    // Top-down dynamic solution
    public static int topDownEggDrop(int eggs, int floors) {
        int[][] dp = new int[eggs + 1][floors + 1];
        return topDownEggDrop(eggs, floors, dp);
    }
    
    // Overloaded recursive method
    private static int topDownEggDrop(int eggs, int floors, int[][] dp) {
        if (floors == 0) return 0;
        if (floors == 1) return 1;
        if (eggs == 1) return floors;
        
        if (dp[eggs][floors] == 0) {
            int maxDrops = Integer.MAX_VALUE;
            for (int i = 1; i <= floors; i++) {
                int breaks = bruteForceEggDrop(eggs - 1, i - 1);
                int doesntBreak = bruteForceEggDrop(eggs, floors - i);
                maxDrops = Math.min(maxDrops, Math.max(breaks, doesntBreak));                    
            }
            dp[eggs][floors] = maxDrops + 1;
        }
        return dp[eggs][floors];
    }
    
    // Bottom-up dynamic solution
    public static int bottomUpEggDrop(int eggs, int floors) {
        int[][] dp = new int[eggs + 1][floors + 1];
        
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[0].length; j++) {
                if (j == 1) {
                    dp[i][j] = 1;
                } else if (i == 1) {
                    dp[i][j] = j;
                } else {
                    int maxDrops = Integer.MAX_VALUE;
                    for (int k = 1; k <= j; k++) {
                        int breaks = dp[i-1][k-1];
                        int doesntBreak = dp[i][j-k];
                        maxDrops = Math.min(maxDrops, Math.max(breaks, doesntBreak));
                    }
                    dp[i][j] = maxDrops + 1;
                }
            }
        }
        
        return dp[eggs][floors];
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(1, 1, 1)).run();
        (new TestCase(1, 10, 10)).run();
        (new TestCase(2, 10, 4)).run();
        (new TestCase(2, 20, 6)).run();
        (new TestCase(3, 10, 4)).run();
        System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private int eggs;
        private int floors;
        private int output;
        
        private TestCase(int eggs, int floors, int output) {
            this.eggs = eggs;
            this.floors = floors;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceEggDrop(eggs, floors) == output:
                "bruteForceEggDrop failed for eggs = " + eggs + ", floors = " + floors;
            assert topDownEggDrop(eggs, floors) == output:
                "topDownEggDrop failed for eggs = " + eggs + ", floors = " + floors;
            assert bottomUpEggDrop(eggs, floors) == output:
                "bottomUpEggDrop failed for eggs = " + eggs + ", floors = " + floors;
        }
    }
}