import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dragon
 * @date 2018/1/30
 * <p>
 * 给定数组大小m   求从m中取n个元素的全部组合
 * <p>
 * {1，2，3，4}  4取2： 12，13，14，23，24，34。
 */
public class T_15 {
    public static void main(String[] args) {
        int[] arr = {85, 17, 99, 58, 76, -80, 68, -38, -74, -75, -88, -31, 17, -2, -40, -57, 29, -84, -16, -31, -80, -39, 58, -33, 67, 11, 38, 2, -31, -48, -29, 28, -11, -52, 86, -86, 59, 45, 68, 15, -17, 56, 34, 8, 29, 69, -93, 84, -76, -98, 85, 59, 40, -93, -47, -9, 100, 51, 55, 31, -47, -9, -63, -9, -94, 32, 21, 88, 60, 36, -54, 2, 42, 86, -44, -81, -82, -29, -48, 49, 77, -19, 3, 26, -53, 35, 39, 92, -56, 77, -59, 56, -13, 18, -56, -70, 81, 31, -28, -13, -51, 19, 86, 36, 20, 7, -2, -52, -14, -10, -70, 3, -34, 100, 90, 75, -27, -62, -37, -19, 42, 68, -56, -94, 22, -6, 49, -74, 76, -11, -18, -71, -46, 23, 62, -72, 35, 82, 92, 27, -10, -38, -9, 7, -18, -83, -37, 48, -18, 98, -80, 16, 6, -72, -4, 45, -99, 39, 27, -24, 31, -48, 26, 16, 32, -56, -14, 94, -36, 86, 30, -21, 45, -68, -74, 50, -65, 39, -25, 67, 1, -36, 61, -2, 60, 71, -16};
        System.out.println(new T_15().threeSumClosest(arr, 67));
    }

    static ArrayList<Integer> item = new ArrayList<Integer>();
    static ArrayList<List<Integer>> result = new ArrayList<List<Integer>>();
    static int targets;
    static int minDistance;
    static int SUM;

    public int threeSumClosest(int[] nums, int target) {
        if (nums.length == 0) {
            return 0;
        }
        targets = target;
        int other = 3;
        minDistance = Integer.MAX_VALUE;
        Arrays.sort(nums);

        fun(nums, 0, other);
        return SUM;
    }

    static void fun(int[] arr, int index, int other) {
        if (other == 1) {
            for (int i = index; i < arr.length; i++) {
                item.add(arr[i]);
                int sum = 0;
                for (int j = 0; j < item.size(); j++) {
                    sum += item.get(j);
                }
                int distance = sum - targets;
                if (Math.abs(distance) < minDistance) {
                    minDistance = Math.abs(distance);
                    SUM = sum;
                }

                item.remove((Object) arr[i]);
            }
        } else if (other > 1) {
            for (int i = index; i <= arr.length - other; i++) {
                item.add(arr[i]);
                fun(arr, i + 1, other - 1);
                item.remove((Object) arr[i]);
            }
        } else {
            return;
        }
    }

}
