import java.util.Date;
import java.util.Random;

/**
 * Created by dragon on 2017/11/4.
 */
public class Sort {
    private static int size = 6;
    private static int ceiling = 100;

    public static void main(String[] args) {
        int[] num = arrFactor();
        int[] num2 = num.clone();
        long a = System.currentTimeMillis();
        bubbleSort(num);
        System.out.println("冒泡排序消耗 ：" + (System.currentTimeMillis() - a) / 1000.f + "s");

        long b = System.currentTimeMillis();
        insertSort(num2);
        System.out.println("插入排序排序消耗 ：" + (System.currentTimeMillis() - b) / 1000.f + "s");
    }

    private static int[] arrFactor() {
        int num[] = new int[size];
        Random rand = new Random(2324);

        for (int i = 0; i < size; i++) {
            num[i] = rand.nextInt(ceiling);
        }
        return num;

    }


    private static int[] bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }


    //小在前
    private static int[] insertSort(int[] a) {
        int tmp;
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j] < a[j - 1]) {
                    tmp = a[j - 1];
                    a[j - 1] = a[j];
                    a[j] = tmp;
                }
            }
        }
        return a;
    }


}
