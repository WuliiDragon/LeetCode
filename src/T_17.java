import java.util.*;

/**
 * @author dragon
 * @date 2018/2/12
 * 输出9键键盘所有的可能
 * 9键盘：
 * 1 (***)    2（abc） 3 (def)
 * 4 (ghi)    5（jkl） 6 (mno)
 * 7 (pqrs)   8（tuv） 9 (wxyz)
 * <p>
 * 用递归解决
 */
public class T_17 {
    public static void main(String[] args) {
        System.out.print(new T_17().letterCombinations("79").toString());
    }

    public List<String> letterCombinations(String digits) {

        HashMap<Character, ArrayList<String>> map = new HashMap<Character, ArrayList<String>>();
        map.put('2', new ArrayList<String>() {
            {
                add("a");
                add("b");
                add("c");
            }
        });
        map.put('3', new ArrayList<String>() {
            {
                add("d");
                add("e");
                add("f");
            }
        });
        map.put('4', new ArrayList<String>() {
            {
                add("g");
                add("h");
                add("i");
            }
        });
        map.put('5', new ArrayList<String>() {
            {
                add("j");
                add("k");
                add("l");
            }
        });
        map.put('6', new ArrayList<String>() {
            {
                add("m");
                add("n");
                add("o");
            }
        });
        map.put('7', new ArrayList<String>() {
            {
                add("p");
                add("q");
                add("r");
                add("s");
            }
        });
        map.put('8', new ArrayList<String>() {
            {
                add("t");
                add("u");
                add("v");
            }
        });
        map.put('9', new ArrayList<String>() {
            {
                add("w");
                add("x");
                add("y");
                add("z");
            }
        });

        List<String> result = new ArrayList<String>();
        if (digits.length() == 0) {
            return result;
        }

        if (map.containsKey(digits.charAt(digits.length() - 1))) {
            ArrayList list = map.get(digits.charAt(digits.length() - 1));

            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                result.add((String) iterator.next());
            }
        }


        for (int i = digits.length() - 2; i >= 0; i--) {
            char key = digits.charAt(i);
            if (!map.containsKey(key)) {
                System.out.println("非法字符");
                break;
            }
            ArrayList list = map.get(digits.charAt(i));

            ArrayList itemRes = new ArrayList();

            for (int x = 0; x < list.size(); x++) {
                for (int y = 0; y < result.size(); y++) {
                    itemRes.add(list.get(x) + result.get(y));
                }
            }
            result = itemRes;
        }


        return result;
    }

}
