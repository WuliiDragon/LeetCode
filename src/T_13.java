import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dragon
 * @date 2018/1/27
 */
public class T_13 {
    public static void main(String[] args) {
        System.out.print(romanToInt("IV"));
    }

    public static int romanToInt(String s) {
        HashMap<String, Integer> one = new HashMap<String, Integer>() {{
            put("I", 1);
            put("X", 10);
            put("C", 100);
            put("M", 1000);
        }};

        HashMap<String, Integer> five = new HashMap<String, Integer>() {{
            put("V", 5);
            put("L", 50);
            put("D", 500);
        }};

        int index = 0, result = 0;

        while (index < s.length()) {
            System.out.println(s.charAt(index));
            String _char = String.valueOf(s.charAt(index));

            if (one.containsKey(_char)) {
                result += one.get(_char);
            } else if (five.containsKey(_char)) {
                result += five.get(_char);
            }
            index++;
        }
        return result;

    }
}
