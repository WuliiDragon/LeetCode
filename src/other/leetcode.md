1. Two Sum
> 描述：给定一个数组`num[]`和一个数字`target`，若数组中有两个数字相加等于target在，则输出两个数字的索引。

最优解我是没有，但是我浏览了大多答案套路以及固定了，由于语言不同效率不同所以C语言肯定是最屌的。总结下思路：

创建一个Map hash， key存放数字，value存放索引，开始循环遍历num[]，用target减去num[i]得出answer,若hash存在key为answer的话就找到了答案了，返回num[i]和answer，若不存在，则hash.set(num[i],i)。


2.Add Two Numbers
描述：给定两个链表，两个链表长度不定且不为0，每个节点的数字域为0-9。求两个列表对应位置的和。

```java
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
    }
}

```

例子：
```
Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8
```

我认为有个点：
两个链表长度不同时：应当将短那一个延长，添0。
最后一位运算完成后：判断是否进位。

以下是我给出的解：

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //头
        ListNode res = new ListNode(0);、
        //游标
        ListNode vernier  = res;
        //是否进位？ 1进，0不进。
        int flag = 0;
        do {
            int sum = l1.val + l2.val + flag;
            flag = sum > 9 ? 1 : 0;
            vernier.next = new ListNode(sum % 10);
            vernier =  vernier.next;
            
            
            //判断是否有不足的，不足则添0
            if (l1.next == null && l2.next != null) {
                    l1.next = new ListNode(0);
            }
            if (l2.next == null && l1.next != null) {
                    l2.next = new ListNode(0);
            }
            if ((l1.next == null) && (l2.next == null)) {
                break;
            }
            l1 = l1.next;
            l2 = l2.next;
        } while (true);

        //最后一位是否进位
        if (flag == 1) {
            vernier.next = new ListNode(1);
        }
        res = res.next;
        return res;
    }
}

```


第三题：最长不重复子字符串，
从给出的字符串中，寻找最长子字符串要求是：子字符串中没有重复字符。
 
思路：刚开始做这道题目卡了很长的时间，思路不对导致卡了很久。看了一些解题方法，大多是：用start表示最长子字符首字符的索引，end表示正在操作的字符索引，用256大小的状态数组表示ASCII码表，默认全部置为0，不为0就存上一次出现的索引，每次查询end在若存在相应ASCII字码表的字符，判断这个字母是没出现过或者已经出现过而且出现在当前最长列的最左边：更行max。否则就更新最长子字符串的start为当前end，当前end字符判断完毕，更新end在状态数组中的状态，

```java
class Solution {
  public int lengthOfLongestSubstring(String s) {
        int[] m = new int[256];
        Arrays.fill(m, 0);
        //max 最长不重复子字符，left 当前最长不重复子字符的坐左边字符的在s中的位置
        int max = 0, start = 0;
        for (int end = 0; end < s.length(); ++end) {
            //挨个字母判断
            //判断这个字母是否出现过或者已经出现过而且出现在当前最长列的最左边
            if (m[s.charAt(end)] == 0 || m[s.charAt(end)] < start) {
                //max判断当前的不重复的字符和之前最长不重复子字符的长度
                max = Math.max(max, end - start + 1);
            } else {
                //若是出现过得话记下这个位置，作为新的最长子字符串的开始
                start = m[s.charAt(end)];
            }
            //记下这个字符最后一次出现的位置
            m[s.charAt(end)] = end + 1;
        }
        return max;
}
}

```


第四题：有序数组的中值Median of Two Sorted Arrays

给定两个已经排好序的数组，求两个数组

对我来说最好理解的方法就是：
用两个游标指示当前两个数组所取得元素，比较大小，将较小的元素放入第三个数组，到了mid时，停止遍历。
按奇偶分开讨论。

这种写法的时间复杂度是O(m+n)

远远没达到题目所说的要求O(log(m+n))。但是好理解

```java

class Solution {
    
    //最low的写法
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length == 0) {
            return midArr(nums2);
        }
        if (nums2.length == 0) {
            return midArr(nums1);
        }


        int totalSize = nums1.length + nums2.length;
        int mid = totalSize / 2;

        int cursor1 = 0, cursor2 = 0;

        int temp1, temp2;

        int[] num3 = new int[totalSize];

        for (int i = 0; i < totalSize; i++) {
            if (cursor1 + cursor2 > mid) {
                break;
            }
            temp1 = cursor1 < nums1.length ? nums1[cursor1] : Integer.MAX_VALUE;
            temp2 = cursor2 < nums2.length ? nums2[cursor2] : Integer.MAX_VALUE;
            if (temp1 < temp2) {
                num3[i] = temp1;
                cursor1 += 1;
            } else {
                num3[i] = temp2;
                cursor2 += 1;
            }
        }


        double res;
        if (totalSize % 2 == 0) {
            res = (num3[mid - 1] + num3[mid]) * 0.5f;
        } else {
            res = num3[mid ];
        }
        return res;
    }

    private static double midArr(int[] arr) {


        int mid = arr.length/2;
        double res;
        if (arr.length % 2 == 0) {
            res = (arr[mid - 1] + arr[mid]) * 0.5f;
        } else {
            res = arr[mid];
        }

        return res;
    }


}


```


第五题：最长子回文字符串

最朴素的算法：遍历子串；判断每个子串是否为回文；然后标记
以下是Java代码
```java


public class T_5 {

    public static void main(String[] args) {
        String target = "slvafhpfjpbqbpcuwxuexavyrtymfydcnvvbvdoitsvumbsvoayefsnusoqmlvatmfzgwlhxtkhdnlmqmyjztlytoxontggyytcezredlrrimcbkyzkrdeshpyyuolsasyyvxfjyjzqksyxtlenaujqcogpqmrbwqbiaweacvkcdxyecairvvhngzdaujypapbhctaoxnjmwhqdzsvpyixyrozyaldmcyizilrmmmvnjbyhlwvpqhnnbausoyoglvogmkrkzppvexiovlxtmustooahwviluumftwnzfbxxrvijjyfybvfnwpjjgdudnyjwoxavlyiarjydlkywmgjqeelrohrqjeflmdyzkqnbqnpaewjdfmdyoazlznzthiuorocncwjrocfpzvkcmxdopisxtatzcpquxyxrdptgxlhlrnwgvee";
        System.out.print(longestPalindrome(target));
    }


    public static String longestPalindrome(String s) {

        int len = s.length();
        //标记最长
        int start = 0, end = 0, max = 0;
        for (int i = 0; i < len; i++) {
            int j = i;
            while (j >= 0) {
                String t = s.substring(j, i + 1);
                if (Palindrome(0, t.length() - 1, t.length(), t) && (i - j > max)) {
                    max = i - j;
                    start = j;
                    end = i;
                }
                j--;
            }
        }
        return s.substring(start, end + 1);
    }

    public static boolean Palindrome(int low, int height, int length, String s) {
        if (length == 0 || length == 1) {
            return true;
        }

        if (s.charAt(low) != s.charAt(height)) {
            return false;
        }
        return Palindrome(low + 1, height - 1, length - 2, s);

    }
}
```

很不幸超时，只能在寻求其他办法


7. Reverse Integer

数字倒序：

输入：123
输出： 321

输入 :210
输出：12


题目不难，每次取x的个位，将返回值左移一位（乘以10）再加上个位的值


```java

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


```

8. String to Integer (atoi)
字符串转数字

难点：非法处理，符号处理，边界处理。

```java

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



```

9. Palindrome Number

判断给定的数字是否是回文数字？
任何负数都不是

我的做法直接倒序判断是否相等

```java

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
```


11. Container With Most Water
能乘最多的水


```java

public class T_11 {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 4, 3};
        System.out.print(maxArea(arr));


    }

    public static int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0, right = height.length - 1;
        int width = 0;

        while (left < right) {
            width = right - left;
            maxArea = Math.max(maxArea, Math.min(height[left], height[right]) * width);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

}


```

15. 3Sum
给定一个数组 求这个数组中的三个数 a b c  使得 a + b + c = 0 
求出所有结果的集合返回一个set
每个组合中数字按大小排序
给定数组 S = [-1, 0, 1, 2, -1, -4],

返回 set:
[
  [-1, 0, 1],
  [-1, -1, 2]
]
我的想法很简单
1. 对给定数组排序
2. 用递归求出所有可能的组合
3. 对每个组合进行求和


但是提交后会有错误.......  玄学
这种方法会超时


```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dragon on 2018/2/2.
 */
public class T_15 {
    public static void main(String[] args) {
        int[] arr = {1, 0, -1};
        System.out.println(new T_15().threeSum(arr));
    }

    static ArrayList<Integer> item = new ArrayList<Integer>();
    static HashMap<String, Boolean> map = new HashMap<String, Boolean>();
    static ArrayList<List<Integer>> result = new ArrayList<List<Integer>>();

    public List<List<Integer>> threeSum(int[] nums) {
        if (nums.length == 0) {
            return result;
        }
        int other = 3;
        Arrays.sort(nums);
        fun(nums, 0, other);
        return result;
    }

    static void fun(int[] arr, int index, int other) {
        if (other == 1) {
            for (int i = index; i < arr.length; i++) {
                item.add(arr[i]);
                int sum = 0;
                for (int j = 0; j < item.size(); j++) {
                    sum += item.get(j);
                }
                if (sum == 0) {
                    if (!map.containsKey(item.toString())) {
                        result.add((ArrayList) item.clone());
                        map.put(item.toString(), true);
                    }
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

```


仔细分析上面的算法可以改进， 哪里改进呢?
仔细想想以上算法其实是有很多徒劳的步骤 
没有必要遍历所有的组合
为什么呢？
对于这个数组  [-1, 0, 1, 2, -1, -4]
我们试着将它排序后 [-4, -1, -1, 0, 1, 2]

上面的算法时间复杂度是(O^3)  其实可以降到（O^2）
第三层循环可以用一个map来替代 效率提高

用两个下标 i , j 来标识前两个数字 
第三个数字在map里找

还有就是当 i + j 大于0时 j后面的全部都不用判断了


以下是算法
```java
public class T_15 {
    public static void main(String[] args) {
        int[] arr = {1, 2, -2, -1};
        System.out.println(new T_15().threeSum(arr));
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
```


16. 3Sum Closest
在15题的基础上修改而来，换汤不换药,很简单写出代码

但是超时

 ```java
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
 public class T_16 {
     public static void main(String[] args) {
         int[] arr = {85, 17, 99, 58, 76, -80, 68, -38, -74, -75, -88, -31, 17, -2, -40, -57, 29, -84, -16, -31, -80, -39, 58, -33, 67, 11, 38, 2, -31, -48, -29, 28, -11, -52, 86, -86, 59, 45, 68, 15, -17, 56, 34, 8, 29, 69, -93, 84, -76, -98, 85, 59, 40, -93, -47, -9, 100, 51, 55, 31, -47, -9, -63, -9, -94, 32, 21, 88, 60, 36, -54, 2, 42, 86, -44, -81, -82, -29, -48, 49, 77, -19, 3, 26, -53, 35, 39, 92, -56, 77, -59, 56, -13, 18, -56, -70, 81, 31, -28, -13, -51, 19, 86, 36, 20, 7, -2, -52, -14, -10, -70, 3, -34, 100, 90, 75, -27, -62, -37, -19, 42, 68, -56, -94, 22, -6, 49, -74, 76, -11, -18, -71, -46, 23, 62, -72, 35, 82, 92, 27, -10, -38, -9, 7, -18, -83, -37, 48, -18, 98, -80, 16, 6, -72, -4, 45, -99, 39, 27, -24, 31, -48, 26, 16, 32, -56, -14, 94, -36, 86, 30, -21, 45, -68, -74, 50, -65, 39, -25, 67, 1, -36, 61, -2, 60, 71, -16};
         System.out.println(new T_16().threeSumClosest(arr, 67));
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
```