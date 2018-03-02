/**
 * Created by dragon on 2017/12/26.
 */
public class T_5 {

    public static void main(String[] args) {
        String target = "slvafhpfjpbqbpcuwxuexavyrtymfydcnvvbvdoitsvumbsvoayefsnusoqmlvatmfzgwlhxtkhdnlmqmyjztlytoxontggyytcezredlrrimcbkyzkrdeshpyyuolsasyyvxfjyjzqksyxtlenaujqcogpqmrbwqbiaweacvkcdxyecairvvhngzdaujypapbhctaoxnjmwhqdzsvpyixyrozyaldmcyizilrmmmvnjbyhlwvpqhnnbausoyoglvogmkrkzppvexiovlxtmustooahwviluumftwnzfbxxrvijjyfybvfnwpjjgdudnyjwoxavlyiarjydlkywmgjqeelrohrqjeflmdyzkqnbqnpaewjdfmdyoazlznzthiuorocncwjrocfpzvkcmxdopisxtatzcpquxyxrdptgxlhlrnwgvee";
        System.out.print(longestPalindrome(target));
    }


    public static String longestPalindrome(String s) {

        int len = s.length();
        //标记最长
        int start = 0, end = 0, max = 0;
        for (int i = 0; i < len; i++) {
            int j = i;
            while (j >= 0) {
                String t = s.substring(j, i + 1);
                if (Palindrome(0, t.length() - 1, t.length(), t) && (i - j > max)) {
                    max = i - j;
                    start = j;
                    end = i;
                }
                j--;
            }
        }
        return s.substring(start, end + 1);
    }

    public static boolean Palindrome(int low, int height, int length, String s) {
        if (length == 0 || length == 1) {
            return true;
        }

        if (s.charAt(low) != s.charAt(height)) {
            return false;
        }
        return Palindrome(low + 1, height - 1, length - 2, s);

    }


//
////        int n = target.length();
//        boolean[][] p = new boolean[n][n];
//        int max = 0;
//        for (int i = 0; i < n; i++) {
//            int j = i;
//            while (j >= 0) {
//
//                if ((target.charAt(i) == target.charAt(j)) && (i - j < 2 || p[j + 1][i - 1])) {
//                    System.out.println("i:" + target.charAt(i) + " j:" + target.charAt(j));
//                    p[j][i] = true;
//                    max = Math.max(max, i - j + 1);
//                }
//                j--;
//            }
//
//        }
//    }

}
