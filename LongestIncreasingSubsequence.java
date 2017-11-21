import java.util.LinkedList;
import java.util.List;

public class LongestIncreasingSubsequence {
    public static List<Integer> longestIncreasingSubsequence(int[] arr) {
        return longestIncreasingSubsequence(arr, 0, Integer.MIN_VALUE);
    }
    
    private static List<Integer> longestIncreasingSubsequence(int[] arr, int i, int prev) {
        if (i == arr.length) return new LinkedList<Integer>();
        
        if (arr[i] < prev) return longestIncreasingSubsequence(arr, i+1, prev);
        
        List<Integer> include = longestIncreasingSubsequence(arr, i+1, arr[i]);
        List<Integer> exclude = longestIncreasingSubsequence(arr, i+1, prev);
        
        if (exclude.size() > include.size()) return exclude;
        include.add(0, arr[i]);
        return include;
    }
    
    public static int longestIncreasingSubsequenceBottomUp(int[] arr) {
        int[] dp = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            int max = 0;
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) max = Math.max(max, dp[j]);
            }
            dp[i] = max + 1;
        }
        int maxResult = 0;
        for (int i : dp) maxResult = Math.max(maxResult, i);
        return maxResult;
    }
    
    public static void main(String[] args) {
        System.out.println(longestIncreasingSubsequence(new int[]{1, 4, 2, 3, 5}));
        System.out.println(longestIncreasingSubsequenceBottomUp(new int[]{1, 4, 2, 3, 5}));
    }
}