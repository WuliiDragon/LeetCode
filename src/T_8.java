/**
 * Created by WLW on 2018/1/3.
 */
public class T_8 {

    public static void main(String[] args) {
        System.out.print(myAtoi("-2147483647"));


    }

    public static int myAtoi(String str) {
        if (str.length() == 0 || str == null) {
            return 0;
        }
        int i = 0, res = 0;
        int sign = 1;
        char arr[] = str.toCharArray();

        //跳过空白字符
        while (arr[i] == ' ') {
            i++;
        }

        if (arr[i] == '-' || arr[i] == '+') {
            sign = (arr[i] == '-') ? -1 : 1;
            i++;
        }
        //题目比较变态  会半路杀出个非法字符 ，按照题目的要求 返回出现非法字符前
        //的转化的数字

        //遍历的停止条件因该是出现非法字符
        while (i < arr.length && arr[i] <= '9' && arr[i] >= '0') {
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && arr[i] > '7')) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            res = res * 10 + arr[i] - '0';
            i++;
        }
        return sign * res;
    }
}
