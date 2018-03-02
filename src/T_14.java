/**
 * @author dragon
 * @date 2018/1/29
 * <p>
 * 官方给了四种解法，最简单的是垂直扫描，水平扫描
 */
public class T_14 {
    public static void main(String[] args) {
        String[] par = new String[4];
        par[0] = "leets";
        par[1] = "leetcode";
        par[2] = "leet";
        par[3] = "leets";
        System.out.print(longestCommonPrefix(par));
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0 || strs.length == 1) {
            return strs[0];
        }
        String pre = strs[0];
        for (int i = 0; i < pre.length(); i++) {
            String t = pre.substring(0, i + 1);
            for (int j = 1; j < strs.length; j++) {
                String item = strs[j];

                //包含
                if (item.indexOf(t) == 0) {
                    continue;
                } else {
                    return pre.substring(0, i);
                }
            }
        }
        return "";
    }
}
