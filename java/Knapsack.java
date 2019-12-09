/*
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
 * Execution: javac Knapsack.java && java Knapsack
 */

import java.util.HashMap;
import java.util.Map;

public class Knapsack {
    
    // Public inner class to represent an individual item
    public static class Item {
        int weight;
        int value;
        
        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }
    }
    
    // Brute force solution
    public static int bruteForceKnapsack(Item[] items, int W) {
        return bruteForceKnapsack(items, W, 0);
    }
    
    // Overloaded recursive function
    private static int bruteForceKnapsack(Item[] items, int W, int i) {
        // If we've gone through all the items, return
        if (i == items.length) return 0;
        // If the item is too big to fill the remaining space, skip it
        if (W - items[i].weight < 0) return bruteForceKnapsack(items, W, i+1);
        
        // Find the maximum of including and not including the current item
        return Math.max(bruteForceKnapsack(items, W - items[i].weight, i+1) 
                            + items[i].value,
                        bruteForceKnapsack(items, W, i+1));
    }
    
    // Top-down dynamic solution. Array-based caching
    public static int topDownKnapsackArray(Item[] items, int W) {
        int[][] dp = new int[items.length][W + 1];
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                dp[i][j] = -1;
            }
        }
        return topDownKnapsackArray(items, W, 0, dp);
    }
    
    // Overloaded recursive function
    private static int topDownKnapsackArray(Item[] items, int W, int i, int[][] dp) {
        if (i == items.length) return 0;
        if (dp[i][W] == -1) {
            dp[i][W] = topDownKnapsackArray(items, W, i+1, dp);
            if (W - items[i].weight >= 0) {
                int include = topDownKnapsackArray(items, W-items[i].weight, i+1, dp)
                    + items[i].value;
                dp[i][W] = Math.max(dp[i][W], include);
            }
        }
        return dp[i][W];
    }
    
    // Top down dynamic solution. HashMap-based caching
    public static int topDownKnapsackHashMap(Item[] items, int W) {
        // Map: i -> W -> value
        Map<Integer, Map<Integer, Integer>> dp = 
            new HashMap<Integer, Map<Integer, Integer>>();
        return topDownKnapsackHashMap(items, W, 0, dp);
    }
    
    // Overloaded recursive function
    private static int topDownKnapsackHashMap(Item[] items, int W, int i, 
                                              Map<Integer, Map<Integer, Integer>> dp) {
        if (i == items.length) return 0;
        
        // Check if the value is in the cache
        if (!dp.containsKey(i)) dp.put(i, new HashMap<Integer, Integer>());
        Integer cached = dp.get(i).get(W);
        if (cached != null) return cached;
        
        // If not, compute the item and add it to the cache
        int toReturn;
        if (W - items[i].weight < 0) {
            toReturn = topDownKnapsackHashMap(items, W, i+1, dp);
        } else {
            toReturn = Math.max(topDownKnapsackHashMap(items, W - items[i].weight, i+1, dp) 
                                    + items[i].value,
                                topDownKnapsackHashMap(items, W, i+1, dp));
        }
        dp.get(i).put(W, toReturn);
        return toReturn;
    }
    
    // Bottom-up dynamic solution.
    public static int bottomUpKnapsack(Item[] items, int W) {
        if (items.length == 0 || W == 0) return 0;
        // Initialize cache
        int[][] dp = new int[items.length + 1][W + 1];
        // For each item and weight, compute the max value of the items up to 
        // that item that doesn't go over W weight
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < dp[0].length; j++) {
                if (items[i-1].weight > j) {
                    dp[i][j] = dp[i-1][j];
                } else {
                    int include = dp[i-1][j-items[i-1].weight] + items[i-1].value;
                    dp[i][j] = Math.max(dp[i-1][j], include);
                }
            }
        }
        
        return dp[items.length][W];
    }
    
    // Space-optimized bottom-up dynamic solution
    public static int bottomUpKnapsackSpaceOptimized(Item[] items, int W) {
        int[] dp = new int[W + 1];
        for (Item i : items) {
            int[] newDp = new int[W + 1];
            for (int j = 0; j <= W; j++) {
                if (i.weight > j) newDp[j] = dp[j];
                else newDp[j] = Math.max(dp[j], dp[j - i.weight] + i.value);
            }
            dp = newDp;
        }
        
        return dp[W];
    }
    
    // Sample testcases
    public static void main(String[] args) {
        (new TestCase(new Item[]{}, 0, 0)).run();
        (new TestCase(new Item[]{
            new Item(4, 5),
                new Item(1, 8),
                new Item(2, 4),
                new Item(3, 0),
                new Item(2, 5),
                new Item(2, 3)
        }, 3, 13)).run();
            (new TestCase(new Item[]{
                new Item(4, 5),
                    new Item(1, 8),
                    new Item(2, 4),
                    new Item(3, 0),
                    new Item(2, 5),
                    new Item(2, 3)
            }, 8, 20)).run();
                
                System.out.println("Passed all test cases");
    }
    
    // Class for defining and running test cases
    private static class TestCase {
        private Item[] items;
        private int weight;
        private int output;
        
        private TestCase(Item[] items, int weight, int output) {
            this.items = items;
            this.weight = weight;
            this.output = output;
        }
        
        private void run() {
            assert bruteForceKnapsack(items, weight) == output:
                "naiveKnapsack failed for items = " + itemsString() + ", weight = " + weight;
            assert topDownKnapsackArray(items, weight) == output:
                "topDownKnapsack failed for items = " + itemsString() + ", weight = " + weight;
            assert topDownKnapsackHashMap(items, weight) == output:
                "topDownKnapsackHashMap failed for items = " + itemsString() + ", weight = " + weight;
            assert bottomUpKnapsack(items, weight) == output:
                "bottomUpKnapsack failed for items = " + itemsString() + ", weight = " + weight;
            assert bottomUpKnapsackSpaceOptimized(items, weight) == output:
                "bottomUpKnapsackSpaceOptimized failed for items = " + itemsString() + ", weight = " + weight;
        }
        
        private String itemsString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < items.length; i++) {
                Item item = items[i];
                sb.append("{w:" + item.weight + ",v:" + item.value + "}");
                if (i != items.length - 1) sb.append(",");
            }
            sb.append("]");
            
            return sb.toString();
        }
    }
}