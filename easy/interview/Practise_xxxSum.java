package com.stevenli.interview.easy.interview;

import java.util.*;

/**
 * Created by fenli on 12/21/16.
 */
public class Practise_xxxSum {

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
        Practise_xxxSum processor = new Practise_xxxSum();
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
