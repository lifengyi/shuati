package interview;

import java.util.Arrays;

public class Problem_Inplace_Shift {

}

class L189_Rotate_Array {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int cycles = gcd(n, k);
        for(int i = 0; i < cycles; ++i) {
            for(int j = (i + k)%n; j != i; j = (j + k)%n) {
                swap(nums, j, i);
            }
        }
        return;
    }

    void swap(int[] nums, int i, int j) {
        nums[i] = nums[i] ^ nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }

    int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a%b);
    }
}


/**
 *  TODO: 理解 Wiggle Sort in-place shift的思想
 */

/**
 * Given an unsorted array nums, reorder it in-place such that
 *
 * nums[0] <= nums[1] >= nums[2] <= nums[3]....
 *
 * Example
 * Given nums = [3, 5, 2, 1, 6, 4],
 * one possible answer is [1, 6, 2, 5, 3, 4].
 *
 */
class LintCode_508_Wiggle_Sort {
    public void wiggleSort(int[] nums) {
        // write your code here
        for(int i = 1; i < nums.length; ++i) {
            if(((i%2) == 1 && nums[i] < nums[i - 1])
                    || ((i%2) == 0 && nums[i] > nums[i - 1])) {
                nums[i] = nums[i] ^ nums[i - 1];
                nums[i - 1] = nums[i] ^ nums[i - 1];
                nums[i] = nums[i] ^ nums[i - 1];
            }
        }
    }
}

/**
 * Given an unsorted array nums, reorder it such that
 *
 * nums[0] < nums[1] > nums[2] < nums[3]....
 *
 * Example
 * Given nums = [1, 5, 1, 1, 6, 4],
 * one possible answer is [1, 4, 1, 5, 1, 6].
 *
 * Given nums = [1, 3, 2, 2, 3, 1],
 * one possible answer is [2, 3, 1, 3, 1, 2].

 */
class LintCode_507_Wiggle_Sort_II {
    public void wiggleSort(int[] nums) {
        // write your code here
        if(nums == null || nums.length < 2) {
            return;
        }

        int middleIndex = (nums.length - 1)/2;
        int leftEnd = middleIndex;
        int rightEnd = nums.length - 1;

        int[] array = nums.clone();
        Arrays.sort(array);
        for(int i = 0; i < nums.length; ++i) {
            nums[i] = (i%2 == 1) ? array[rightEnd--] : array[leftEnd--];
        }
    }
}
