
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 //    static ArrayList<Integer> item = new ArrayList<Integer>();
 //    static HashMap<String, Boolean> map = new HashMap<String, Boolean>();
 //    static ArrayList<List<Integer>> result = new ArrayList<List<Integer>>();
 //
 //    public List<List<Integer>> threeSum(int[] nums) {
 //        if (nums.length == 0) {
 //            return result;
 //        }
 //        int other = 3;
 //        Arrays.sort(nums);
 //        fun(nums, 0, other);
 //        return result;
 //    }
 //
 //    static void fun(int[] arr, int index, int other) {
 //        if (other == 1) {
 //            for (int i = index; i < arr.length; i++) {
 //                item.add(arr[i]);
 //                int sum = 0;
 //                for (int j = 0; j < item.size(); j++) {
 //                    sum += item.get(j);
 //                }
 //                if (sum == 0) {
 //                    if (!map.containsKey(item.toString())) {
 //                        result.add((ArrayList) item.clone());
 //                        map.put(item.toString(), true);
 //                    }
 //                }
 //                item.remove((Object) arr[i]);
 //            }
 //
 //        } else if (other > 1) {
 //            for (int i = index; i <= arr.length - other; i++) {
 //                item.add(arr[i]);
 //                fun(arr, i + 1, other - 1);
 //                item.remove((Object) arr[i]);
 //            }
 //        } else {
 //            return;
 //        }
 //    }
 }
 * @author dragon
 * @date 2018/2/2
 */
public class T_16 {
    public static void main(String[] args) {
        int[] arr = {1, 2, -2, -1};
        System.out.println(new T_16().threeSum(arr));
    }

    private List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(nums.length);
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < nums.length - 1; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                int two = nums[i] + nums[j];
                if (nums[j] + two > 0) {
                    break;
                }
                boolean has = map.containsKey(-two);
                if (has && map.get(-two) != j) {
                    List<Integer> item = new ArrayList<Integer>();
                    item.add(nums[i]);
                    item.add(nums[j]);
                    item.add(-two);
                    System.out.print(item);
                    res.add(item);
                }
            }
        }
        return res;
    }
}

