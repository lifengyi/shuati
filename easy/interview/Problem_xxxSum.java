package interview;

import java.util.*;

/**
 * Created by fenli on 12/21/16.
 */
public class Problem_xxxSum {

    /*
        有正有负，但是元素唯一，即不存在重复元素
        故可以考虑使用set/map来进行处理
     */
    public static int[] numberList = {-1, 10, 11, 8, 9};

    /*
        有正有负，且元素不唯一，即存在重复元素
        无法考虑使用set/map来进行处理
     */
    public static int[] numberList2 = {-1, 0, -2, 4, 2, -1};
    //target = 1 ?
    //public static int[] numberList2 = {-1, 0, -1, 2}


    //time complexity: O(n)
    public int[] twoSum_hashMap(int[] numbers, int target) {
        Map<Integer,Integer> map = new HashMap<>();
        int[] result = new int[2];

        for(int i=0; i< numbers.length; i++)
        {
            if(map.containsKey(numbers[i])) {
                result[0] = map.get(numbers[i]);
                result[1] = i;
                return result;
            }

            map.put((target-numbers[i]), i);
        }

        return result;
    }

    /*
        time complexity: O(NlogN)
        but the index is wrong, the order is reordered
        this solution is good for "get the right value".
        but not for "get the right index"
    */
    public int [] twoSum_quickSort(int[] numbers, int target) {
        Arrays.sort(numbers);

        int []result = new int[2];
        int i = 0;
        int j = numbers.length - 1;
        while(i < j)
        {
            if(numbers[i] + numbers[j] == target){
                result[0] = i;
                result[1] = j;
                return result;
            } else if (numbers[i] + numbers[j] > target){
                j--;
            } else if (numbers[i] + numbers[j] < target) {
                i++;
            }
        }
        return result;
    }


    // time complexity: O(n^2)
    public int[] twoSum_twoPointers(int[] numbers, int target) {
        int[] result = new int[2];
        for(int i = 0; i < numbers.length - 1; i++) {
            for(int j = i + 1; j < numbers.length; j++) {
                if(numbers[i] + numbers[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }

        return result;
    }

    // 考虑到结果的有序排列，故resort array
    // 带来成本 nlogN的复杂度
    // 带来好处 遍历数组O(n)，基于每个元素，再一次遍历数组，两个指针前后寻找最终的target
    // 考虑：如果对最终结果没有排序的要求
    // 思路：遍历数组O(n), 基于每个元素，对剩下的数组进行hashMap twoSum
    // 总结：twoSum space complexity O(n)  -> hashMap
    //             space complexity O(1) time complexity O(n^2) without re-order
    //             space complexity O(1) time complexity O(nlogN) with re-order
    public List<List<Integer>> threeSum(int[] numbers, int target) {
        List<List<Integer>> result = new ArrayList<>();

        //validation
        if(numbers == null || numbers.length < 3)
            return result;

        //re-order the array: O(nlogN)
        Arrays.sort(numbers);

        for(int i = 0; i < numbers.length - 2; i++)
        {
            // skip the duplicated elements from the head
            if(i != 0 && numbers[i] == numbers[i-1])
                break;

            int j = i + 1;
            int k = numbers.length - 1;
            while (j < k)
            {
                int res = numbers[i] + numbers[j] + numbers[k];
                if( res == target) {
                    List<Integer> tmp = new ArrayList<>();
                    tmp.add(numbers[i]);
                    tmp.add(numbers[j]);
                    tmp.add(numbers[k]);
                    result.add(tmp);
                    j++;
                    k--;
                } else if (res > target) {
                    k--;
                } else if (res < target) {
                    j++;
                }

                //skip duplicated elements
                while(j<k && numbers[j] == numbers[j-1])
                    j++;
                while(j<k && numbers[k] == numbers[k+1])
                    k--;
            }
        }

        return result;
    }


    public static void main(String args[]){
        Problem_xxxSum processor = new Problem_xxxSum();
        int[] result = processor.twoSum_twoPointers(numberList, 17);
        if(result.length > 0) {
            for(int element: result) {
                System.out.print(element + ",");
            }
        }
        System.out.print("\n");

        System.out.println("three sum");
        int[] arr  = {1, 1, 1, 1, 1, 1, 1};
        List<List<Integer>> ret = processor.threeSum(arr, 3);
        for(List<Integer> element : ret) {
            System.out.println(element);
        }
    }

}


class LintCode_L608_TwoSumII_InputArrayIsSorted {

}


/**
 *  设计题，需要考虑和沟通该结构体所涉及的行为，例如会有多少数据被存储？
 *  read heavy or write heavy?
 *
 *  不同的行为将会影响读和写的不同设计
 */
class LintCode_L607_TwoSumIII_DataStructureDesign {
    Map<Integer, Integer> numbers = new HashMap<>();

    public void add(int number) {
        if(numbers.containsKey(number)) {
            numbers.put(number, numbers.get(number) + 1);
        } else {
            numbers.put(number, 1);
        }
    }

    /**
     * @param value: An integer
     * @return: Find if there exists any pair of numbers which sum is equal to the value.
     */
    public boolean find(int value) {
        // write your code here
        for(int number : numbers.keySet()) {
            if(numbers.containsKey(value - number)) {
                if(value - number != number
                        || numbers.get(number) > 1) {
                    return true;
                }
            }
        }

        return false;
    }
}

class L15_3Sum_ {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        if(nums == null || nums.length < 3) {
            return result;
        }

        Arrays.sort(nums);
        int left = 0, right = 0;
        for(int i = 0; i < nums.length; ++i) {
            if(i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            left = i + 1;
            right = nums.length - 1;
            while(left < right) {
                if(nums[i] + nums[left] + nums[right] == 0) {
                    List<Integer> ret = new ArrayList<>();
                    ret.add(nums[i]);
                    ret.add(nums[left++]);
                    ret.add(nums[right--]);
                    result.add(ret);
                    while(left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    while(left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if(nums[i] + nums[left] + nums[right] < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return result;
    }
}

class L16_3SumClosest {
    public int threeSumClosest(int[] nums, int target) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int minDiff = Integer.MAX_VALUE;
        int minResult = 0;
        int left = 0, right = 0;

        Arrays.sort(nums);
        for(int i = 0; i < nums.length; ++i) {
            left = i + 1;
            right = nums.length - 1;
            while(left < right) {
                int sum = nums[left] + nums[right] + nums[i];
                if(sum == target) {
                    return sum;
                } else {
                    int diff = Math.abs(target - sum);
                    if(diff < minDiff) {
                        minDiff = diff;
                        minResult = sum;
                    }
                    if(sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }

        return minResult;
    }
}


class L18_4Sum_ {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();

        if(nums == null || nums.length < 4) {
            return result;
        }

        Arrays.sort(nums);
        int sum = 0, left = 0, right = 0;
        for(int i = 0; i < nums.length; ++i) {
            if(i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            for(int j = i + 1; j < nums.length; ++j) {
                if(j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }

                left = j + 1;
                right = nums.length - 1;
                while(left < right) {
                    sum = nums[i] + nums[j] + nums[left] + nums[right];
                    if(sum == target) {
                        List<Integer> ret = new ArrayList<>();
                        ret.add(nums[i]);
                        ret.add(nums[j]);
                        ret.add(nums[left]);
                        ret.add(nums[right]);
                        result.add(ret);

                        left++;
                        right--;
                        while(left < right && nums[left] == nums[left - 1]) {
                            left++;
                        }
                        while(left < right && nums[right] == nums[right + 1]) {
                            right--;
                        }
                    } else if(sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }

        return result;
    }
}


/**
 *  如果需要去重，就需要打开屏蔽的代码
 */
class L454_4Sum_II_ {
    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        //sortArrays(A, B, C, D);
        Map<Integer, Integer> cache = initHashMap(A, B);

        int result = 0;
        int len = A.length, sum = 0;
        for(int i = 0; i < len; ++i) {
            //if(i > 0 && C[i] == C[i - 1]) {
            //    continue;
            //}
            for(int j = 0; j < len; ++j) {
                //if(j > 0 && D[j] == D[j - 1]) {
                //    continue;
                //}

                sum = C[i] + D[j];
                if(cache.containsKey(sum)) {
                    result += cache.get(sum);
                }
            }
        }
        return result;
    }

    Map<Integer, Integer> initHashMap(int[] A, int[] B) {
        Map<Integer, Integer> result = new HashMap<>();
        int len = A.length, sum = 0;
        for(int i = 0; i < len; ++i) {
            //if(i > 0 && A[i] == A[i - 1]) {
            //    continue;
            //}
            for(int j = 0; j < len; ++j) {
                //if(j > 0 && B[j] == B[j - 1]) {
                //    continue;
                //}

                sum = 0 - A[i] - B[j];
                if(result.containsKey(sum)) {
                    result.put(sum, result.get(sum) + 1);
                } else {
                    result.put(sum, 1);
                }
            }
        }
        return result;
    }

    void sortArrays(int[] A, int[] B, int[] C, int[] D) {
        Arrays.sort(A);
        Arrays.sort(B);
        Arrays.sort(C);
        Arrays.sort(D);
    }
}



