/**
 * Created by dragon on 2017/12/14.
 */
public class KMP {
    public static void main(String[] args) {
        System.out.print(fKMP("“abcda”", "bcda"));
    }

    static int fKMP(String p, String t) {
        int i = 0;
        int j = 0;
        int pL = p.length();
        int tL = t.length();

        int[] next = getNext(t);

        while (i < pL && j < tL) {
            if (j == -1 || p.charAt(i) == t.charAt(j)) {
                i += 1;
                j += 1;
            } else {
                j = next[j];
            }

        }

        if (tL == j) {
            return i - j;
        }
        return -1;
    }

    private static int[] getNext(String t) {
        int next[] = new int[t.length()];
        int p_len = t.length();
        int i = 0;
        int j = -1;
        next[0] = -1;

        while (i < p_len - 1) {
            if (j == -1 || t.charAt(i) == t.charAt(j)) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }

        }
        return next;
    }
}
