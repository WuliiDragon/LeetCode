/**
 * Created by WLW on 2018/1/5.
 */
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
