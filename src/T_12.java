/**
 * @author dragon
 * @date 2018/1/19
 * <p>
 * 整形数字转罗马数字
 * 1  2   3   4   5  6    7    8     9      10
 * I  II III  IV  V  VI  VII  VIII   IX     X
 * --------------------------------------------
 * 10    20    40    50   60    70     80     90     100
 * X     XX    XL    L    LX    LXX   LXXX    CX      C
 * --------------------------------------------
 * <p>
 * 思路 ：按位求
 */
public class T_12 {

    public static void main(String[] args) {
        System.out.print(intToRoman(3999));
    }


    public static String intToRoman(int num) {
        String[] one = {"I", "X", "C", "M"};
        String[] five = {"V", "L", "D"};
        String result = "";
        int t, index = 0;
        while (num != 0) {
            t = num % 10;
            num = num / 10;

            String indexRoman = "";
            if (t < 4) {
                while (t != 0) {
                    indexRoman += one[index];
                    t--;
                }
            } else if (t == 4) {
                indexRoman = one[index] + five[index];
            } else if (t >= 5 && t < 9) {
                t -= 5;
                indexRoman += five[index];
                while (t != 0) {
                    indexRoman += one[index];
                    t--;
                }
            } else {
                indexRoman = one[index] + one[index + 1];
            }
            index++;
            result = indexRoman + result;
        }
        return result;
    }
}