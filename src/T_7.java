public class T_7 {

    public static void main (String []args ){
        System.out.print(reverse(213456));


    }

    public static int reverse(int x) {
        long answer = 0;
        while(x != 0) {
            //取 x 的最后一位数字
            int last =  x % 10;

            // answer 向前走一位
            answer = 10 * answer;

            //加上刚取到的 last
            answer = answer + last;

            //去掉 x 最后一位数字
            x /= 10;
        }
        return (answer > Integer.MAX_VALUE || answer < Integer.MIN_VALUE) ? 0 : (int) answer;
    }
}
