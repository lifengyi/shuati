package interview;

import java.util.Arrays;

public class Problem_Partition {
}

class L215_Kth_Largest_Element_in_an_Array {
    public int findKthLargest(int[] nums, int k) {
        if(nums == null || nums.length == 0 || k < 1 || k > nums.length) {
            return 0;
        }

        return findPivot(nums, 0, nums.length - 1, k - 1);
    }

    int findPivot(int[] nums, int start, int end, int target) {
        int left = start, right = end;
        int pivot = nums[start];
        while(left < right) {
            while(left < right && nums[right] < pivot) {
                right--;
            }
            nums[left] = nums[right];
            while(left < right && nums[left] >= pivot) {
                left++;
            }
            nums[right] = nums[left];
        }

        nums[left] = pivot;
        if(target == left) {
            return nums[left];
        } else if (target < left) {
            return findPivot(nums, start, left - 1, target);
        } else {
            return findPivot(nums, left + 1, end, target);
        }
    }
}

class L215_Kth_Largest_Element_in_an_Array_v2 {
    public int kthLargestElement(int k, int[] nums) {
        return findKthLargestElement(nums, 0, nums.length - 1, nums.length - k);
    }

    int findKthLargestElement(int[] nums, int start, int end, int target) {
        int left = start, right = end;
        int pivot = nums[start];
        int i = start;
        while(i <= right) {
            if(nums[i] == pivot) {
                i++;
            } else if(nums[i] < pivot) {
                swap(nums, i++, left++);
            } else {
                swap(nums, i, right--);
            }
        }
        if(target == left) {
            return nums[left];
        } else if(target < left) {
            return findKthLargestElement(nums, start, left - 1, target);
        } else {
            return findKthLargestElement(nums, left + 1, end, target);
        }
    }

    void swap(int[] nums, int index1, int index2) {
        int tmp = nums[index1];
        nums[index2] = nums[index1];
        nums[index1] = nums[index2];
    }
}

/**
 * Given an array of n objects with k different colors
 * (numbered from 1 to k), sort them so that objects of
 * the same color are adjacent, with the colors in the
 * order 1, 2, ... k.
 *
 * Example
 * Given colors=[3, 2, 2, 1, 4], k=4, your code should
 * sort colors in-place to [1, 2, 2, 3, 4].
 *
 * Challenge
 * A rather straight forward solution is a two-pass
 * algorithm using counting sort. That will cost O(k)
 * extra memory. Can you do it without using extra memory?
 */
class LintCode_143_Sort_Colors {
    public void sortColors2(int[] colors, int k) {
        // write your code here
        if(colors == null || colors.length == 0 || k <= 0 || k > colors.length) {
            return;
        }

        sort(colors, 0, colors.length - 1);
    }

    void sort(int[] colors, int start, int end) {
        if(start >= end) {
            return;
        }

        int left = start, right = end, equal = start;
        int pivot = colors[start];
        while(left <= right) {
            if(colors[left] == pivot) {
                left++;
            } else if(colors[left] < pivot) {
                swap(colors, left++, equal++);
            } else {
                swap(colors, left, right--);
            }
        }

        sort(colors, start, equal - 1);
        sort(colors, right + 1, end);
    }

    void swap(int[] colors, int s1, int s2) {
        int tmp = colors[s1];
        colors[s1] = colors[s2];
        colors[s2] = tmp;
    }
}

class LintCode_399_Nuts_and_Bolts_Problem {
    public void sortNutsAndBolts(String[] nuts, String[] bolts, NBComparator compare) {
        // write your code here
        if(nuts == null || bolts == null || nuts.length == 0 || bolts.length != nuts.length) {
            return;
        }

        sortNutsAndBolts(nuts, bolts, 0, nuts.length - 1, compare);
    }

    void sortNutsAndBolts(String[] nuts, String[] bolts, int start, int end, NBComparator compare) {
        if(start >= end) {
            return;
        }

        String nutPivot = nuts[start];
        int pivotIndex = sort(bolts, start, end, nutPivot, compare);
        sort(nuts, start, end, bolts[pivotIndex], compare);

        sortNutsAndBolts(nuts, bolts, start, pivotIndex - 1, compare);
        sortNutsAndBolts(nuts, bolts, pivotIndex + 1, end, compare);
    }

    int sort(String[] array, int start, int end, String pivot, NBComparator compare) {
        int left = start, right = end;
        while(left < right) {
            while(left < right && compare.cmp(array[right], pivot) > 0) {
                right--;
            }
            while(left < right && compare.cmp(array[left], pivot) < 0) {
                left++;
            }
            if(left < right) {
                swap(array, left, right);
            }
        }
        return left;
    }

    void swap(String[] array, int index1, int index2) {
        String tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }
}

class NBComparator {
    public static int cmp(String nut, String bolt) {
        return 0;
    }
}