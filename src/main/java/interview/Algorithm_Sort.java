package interview;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Algorithm_Sort {
    public static void main(String[] args) {

        int[] array = {1,3, 4, 2, 2, 3, 3, 6, 6, 5, 5, 2, 4, 9};

        StableSorting_CountingSort sort1 = new StableSorting_CountingSort();
        int[] ret1 = sort1.sort(array, 10);
        System.out.println(Arrays.toString(ret1));

        StableSorting_BucketSort sort2 = new StableSorting_BucketSort();
        int[] ret2 = sort2.sort(array, 10, 3);
        System.out.println(Arrays.toString(ret2));

        int[] array2 = {34, 23, 14, 12, 72, 2, 0, 11, 21, 99};
        StableSorting_RadixSort_BasedOnDigital sort4 = new StableSorting_RadixSort_BasedOnDigital();
        int[] ret4 = sort4.sort(array2, 100, 5);
        System.out.println(Arrays.toString(ret4));

        int[][] array1 = {
                {1, 2},
                {1, 3},
                {2, 4},
                {4, 9},
                {6, 6},
                {3, 4},
                {3, 8},
                {2, 9},
                {1, 9},
                {5, 9}
        };
        StableSorting_RadixSort sort3 = new StableSorting_RadixSort();
        int[][] ret3 = sort3.sort(array1, 10, 3);
    }
}

class StableSorting_CountingSort {
    public int[] sort(int[] nums, int range) {
        int[] counting = new int[range + 1];
        for(int num : nums) {
            counting[num] += 1;
        }

        for(int i = 1; i < counting.length; ++i) {
            counting[i] += counting[i - 1];
        }

        //int[] order = new int[nums.length];
        int[] ret = new int[nums.length];
        for(int i = nums.length - 1; i >= 0; --i) {
            ret[counting[nums[i]] - 1] = nums[i];
            //order[counting[nums[i]] - 1] = i;
            counting[nums[i]] -= 1;
        }

        //System.out.println(Arrays.toString(order));
        return ret;
    }
}

/**
 *  Number Range :          0 - N
 *  Bucket number:          k
 *  Space for each bucket:  (N + 1)/k
 *  For each number, find the related bucket:  bucketIndex = num/space
 *
 *  Please be sure to create "k+1" buckets!!!
 *
 *  Radix Sort使用 Bucket 实现时，要注意：
 *  1.数据分桶注意：  a) range + 1/k， 得到 sapce
 *                 b) 针对当前key进行分桶，而不是仅仅针对值，
 *                    这是和桶排序的最大区别
 *  2.桶内寻找索引算法需要找到大于目标值的第一个索引，
 *    即二分查找时的上界；
 *
 */
class StableSorting_BucketSort {

    int binarySearch(LinkedList<Integer> list, int num) {
        int left = 0, right = list.size(), middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            int value = list.get(middle);
            if(value <= num) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    void add(LinkedList<Integer> list, int num) {
        //int index = Collections.binarySearch(list, num); //错误算法，该函数当存在相同值时，不保证返回最后一个索引
        int index = binarySearch(list, num);
        if(index == list.size()) {
            list.add(num);
        } else {
            list.add(index, num);
        }
    }

    public int[] sort(int[] nums, int range, int k) {
        LinkedList<Integer>[] bucket = new LinkedList[k + 1];
        for(int i = 0; i <= k; ++i) {
            bucket[i] = new LinkedList<>();
        }

        int space = (range + 1)/k;
        for(int num : nums) {
            add(bucket[num / space], num);
        }

        //populate the output
        int[] ret = new int[nums.length];
        int count = 0;
        for(int i = 0; i <= k; ++i) {
            for(int j = 0; j < bucket[i].size(); ++j) {
                ret[count++] = bucket[i].get(j);
            }
        }

        return ret;
    }
}


class StableSorting_RadixSort {
    public int binarySearch(LinkedList<int[]> list, int[] num, int key) {
        int left = 0, right = list.size(), middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            int[] value = list.get(middle);
            if(value[key] <= num[key]) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    public void add(LinkedList<int[]> list, int[] num, int key) {
        int index = binarySearch(list, num, key);
        if(index == list.size()) {
            list.add(num);
        } else {
            list.add(index, num);
        }
    }

    public int[][] sort(int[][] nums, int range, int k, int key) {
        LinkedList<int[]>[] buckets = new LinkedList[k + 1];
        for(int i = 0; i <= k; ++i) {
            buckets[i] = new LinkedList<>();
        }

        int space = (range + 1)/k;
        for(int[] num : nums) {
            int index = num[key]/space;
            add(buckets[index], num, key);
        }

        int[][] ret = new int[nums.length][2];
        int count = 0;
        for(int i = 0; i <= k; ++i) {
            for(int j = 0; j < buckets[i].size(); ++j) {
                ret[count++] = buckets[i].get(j);
            }
        }
        return ret;
    }

    public int[][] sort(int[][] nums, int range, int k) {
        int[][] ret = nums;
        for(int i = 1; i >= 0; --i) {
            ret = sort(ret, range, k, i);

            for(int j = 0; j < ret.length; ++j) {
                System.out.println(Arrays.toString(ret[j]));
            }
            System.out.println("\n\n");
        }
        return ret;
    }
}


class StableSorting_RadixSort_BasedOnDigital {

    private int get(int num, int exp) {
        int ret = num%(exp * 10);
        return ret/exp;
    }

    private int comp (int src, int dst, int exp) {
        int srcBit = get(src, exp);
        int dstBit = get(dst, exp);
        if(srcBit > dstBit) {
            return 1;
        } else if(srcBit == dstBit) {
            return 0;
        } else {
            return -1;
        }
    }

    public void add(LinkedList<Integer> list, int num, int exp) {
        if(list.isEmpty() || comp(num, list.get(list.size() - 1), exp) >= 0) {
            list.add(num);
            return;
        } else if(comp(num, list.get(0), exp) < 0) {
            list.add(0, num);
            return;
        }


        int left = 0, right = list.size(), middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            if(get(list.get(middle), exp) <= get(num, exp)) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        list.add(left, num);
    }

    public int[] sort(int[] nums, int range, int k, int exp) {
        LinkedList<Integer>[] buckets = new LinkedList[k + 1];
        for(int i = 0; i <= k; ++i) {
            buckets[i] = new LinkedList<>();
        }

        /**
         *  此处代表了针对 "key" 进行取桶
         *
         *  后续比较插入也是根据 "key"查找相关位置
         */
        int space = 10/k;
        for(int num : nums) {
            int index = get(num, exp)/space;
            add(buckets[index], num, exp);
        }


        int[] ret = new int[nums.length];
        int count = 0;
        for(int i = 0; i <= k; ++i) {
            for(int j = 0; j < buckets[i].size(); ++j) {
                ret[count++] = buckets[i].get(j);
            }
        }
        return ret;
    }

    public int[] sort(int[] nums, int range, int k) {
        int[] ret = nums;
        for(int i = 1; range/i != 0; i = i *10) {
            ret = sort(ret, range, k, i);
            System.out.println(Arrays.toString(ret));
        }
        return ret;
    }
}
