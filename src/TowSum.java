
import java.util.Arrays;

import static java.lang.Math.max;

/**
 * Created by dragon on 2017/10/29.
 */
public class TowSum {

    public static void main(String[] args) {
        System.out.print(lengthOfLongestSubstring("aabcddd"));

    }

    public static int lengthOfLongestSubstring(String s) {
        int[] m = new int[256];
        Arrays.fill(m, 0);
        //max 最长不重复子字符，left 当前最长不重复子字符的坐左边字符的在s中的位置
        int max = 0, start = 0;
        for (int end = 0; end < s.length(); ++end) {
            //挨个字母判断
            //判断这个字母是否出现过或者已经出现过而且出现在当前最长列的最左边
            if (m[s.charAt(end)] == 0 || m[s.charAt(end)] < start) {
                //max判断当前的不重复的字符和之前最长不重复子字符的长度
                max = Math.max(max, end - start + 1);
            } else {
                //若是出现过得话记下这个位置，作为新的最长子字符串的开始
                start = m[s.charAt(end)];
            }
            //记下这个字符最后一次出现的位置
            m[s.charAt(end)] = end + 1;
        }
        return max;


//        int[] m = new int[256];
//        Arrays.fill(m, 0);
//        int max = 0, left = 0;
//        for (int i = 0; i < s.length(); ++i) {
//            //当前字符左边
//            left = max(left, m[s.charAt(i)]);
//            m[s.charAt(i)] = i + 1;
//            max = max(max, i - left + 1);
//            System.out.println("left:" + left + " m[" + m[s.charAt(i)] + "]:" + i + " max:" + max);
//        }
//        return max;
    }

//    public static String[] subs(String str, String heade) {
//        String[] arr = new String[2];
//        char h = heade.charAt(0);
//        char chararr[] = str.toCharArray();
//        for (int i = 0; i < chararr.length; i++) {
//            if (chararr[i] == h) {
//                if (i == 0) {
//                    arr[0] = "";
//                    arr[1] = str.substring(1, str.length());
//                } else {
//                    arr[0] = str.substring(0, i);
//                    arr[1] = str.substring(i, str.length());
////                    return arr;
//                }
//            }
//        }
//        return arr;
////        return null;
//    }
    //    public static int lengthOfLongestSubstring(String s) {
//        if (s.length() == 0) {
//            return 0;
//        }
//        if (s.length() == 1) {
//            return 1;
//        }
//
//        String heade = s.substring(0, 1);
//        String tail = s.substring(1, s.length());
//        int len = 0, pl = 0, ql = 0;
//
//        if (tail.contains(heade)) {
//            len = 1;
//            String tailArr[] = subs(tail, heade);
//            pl = lengthOfLongestSubstring(tailArr[0]);
//            ql = lengthOfLongestSubstring(tailArr[1]);
//
//            return pl > ql ? pl : ql + len;
//        } else {
//            len = lengthOfLongestSubstring(tail);
//            if (len == 1) {
//                len++;
//            }
//            return pl > ql ? pl + 1 : ql + len;
//        }
//    }
}
