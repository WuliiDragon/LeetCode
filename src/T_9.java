/**
 * Created by WLW on 2018/1/5.
 */
public class T_9 {
    public static void main(String[] args) {
        System.out.print(isPalindrome(-2147447412));
    }

    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }

        int rever = 0, xs = x, last = 0;
        while (xs != 0) {
            last = xs % 10;
            rever = rever * 10 + last;
            xs /= 10;
        }
        if (rever == x) {
            return true;
        } else {
            return false;
        }

    }
}
