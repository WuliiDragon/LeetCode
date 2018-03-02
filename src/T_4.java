import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragon on 2017/11/2.
 */
public class T_4 {

    public static void main(String[] args) {
        int[] nums1 = new int[3];
        nums1[0] = 1;
        nums1[1] = 2;
        nums1[2] = 3;
        int[] nums2 = new int[2];
        nums2[0] = 1;
        nums2[1] = 4;
        System.out.print(findMedianSortedArrays3(nums1, nums2));

    }

    public static double findMedianSortedArrays1(int[] nums1, int[] nums2) {
        int[] num = new int[nums1.length + nums2.length];
        for (int i = 0; i < nums1.length; i++) {
            num[i] = nums1[i];
        }
        for (int i = 0; i < nums2.length; i++) {
            num[nums1.length + i] = nums2[i];
        }
        float ans = 0;
        Arrays.sort(num);
        if (num.length % 2 == 0) {
            ans = (float) ((float) (num[num.length / 2 - 1] + num[num.length / 2]) * 0.5);
        } else {
            ans = num[num.length / 2];
        }
        return ans;
    }


    public static double findMedianSortedArrays2(int[] nums1, int[] nums2) {

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
            res = num3[mid];
        }
        return res;
    }

    private static double midArr(int[] arr) {


        int mid = arr.length / 2;
        double res;
        if (arr.length % 2 == 0) {
            res = (arr[mid - 1] + arr[mid]) * 0.5f;
        } else {
            res = arr[mid];
        }

        return res;
    }


    public static double findMedianSortedArrays3(int[] nums1, int[] nums2) {
        if (nums1.length == 0) {
            return MedofArray(nums2);
        }
        if (nums2.length == 0) {
            return MedofArray(nums1);
        }
        int n = nums1.length;
        int m = nums2.length;
        if (n > m) {
            return findMedianSortedArrays3(nums2, nums1);
        }
        int L1 = 0, L2 = 0, R1 = 0, R2 = 0, c1, c2, lo = 0, hi = 2 * n; //我们目前是虚拟加了'#'所以数组1是2*n+1长度

        //二分
        while (lo <= hi) {
            //c1是二分的结果
            c1 = (lo + hi) / 2;
            c2 = m + n - c1;
            L1 = (c1 == 0) ? Integer.MIN_VALUE : nums1[(c1 - 1) / 2]; //map to original element
            R1 = (c1 == 2 * n) ? Integer.MAX_VALUE : nums1[c1 / 2];
            L2 = (c2 == 0) ? Integer.MIN_VALUE : nums2[(c2 - 1) / 2];
            R2 = (c2 == 2 * m) ? Integer.MAX_VALUE : nums2[c2 / 2];


            if (L1 > R2) {
                hi = c1 - 1;
            } else if (L2 > R1) {
                lo = c1 + 1;
            } else {
                break;
            }
        }
        return (Math.max(L1, L2) + Math.min(R1, R2)) / 2.0;
    }

    static double MedofArray(int[] nums) {
        if (nums.length == 0) {
            return -1;
        }
        return (nums[nums.length / 2] + nums[(nums.length - 1) / 2]) / 2.0;
    }
}
