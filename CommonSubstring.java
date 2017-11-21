//public class CommonSubstring {
//    public static int longestCommonSubstring(String s1, String s2) {
//        for (int i = 0; i < s1.length; i++) {
//            for (int j = 0; j < s2.length; j++) {
//                
//            }
//        }
//        return longestCommonSubstring(s1, s2, 0, 0);
//    }
//    
//    private static int longestCommonSubstring(String s1, String s2, int i, int j) {
//        if (i == s1.length() || j == s2.length()) return 0;
//        int max = 0;
//        if (s1.charAt(i) == s2.charAt(j)) max = longestCommonSubstring(s1, s2, i+1, j+1) + 1;
//        max = Math.max(max, longestCommonSubstring(s1, s2, i+1, j));
//        max = Math.max(max, longestCommonSubstring(s1, s2, i, j+1));
//        return max;
//    }
//    
//    public static void main(String[] args) {
//        System.out.println(longestCommonSubstring("abcdefghij", "abcd"));
//        System.out.println(longestCommonSubstring("abcdefghij", "defg"));
//        System.out.println(longestCommonSubstring("abcdefghij", "bbcdd"));
//        System.out.println(longestCommonSubstring("abccdefg", "abcdefg"));
//    }
//}
//
//// "ABC"
//// "BCD"