package Pinterest;

import java.util.*;

public class findNKrequency {

    public static void main(String[] args) {
        int[] array = {1,1,1,1,2,2,3,4,5,6,7,8,8,8,9};
        Map<Integer, Integer> freq = new HashMap<>();
        findNKrequency proc = new findNKrequency();
        proc.findFrequency(array, 0, array.length - 1, freq);

        for(int num : freq.keySet()) {
            System.out.println(String.format("key:%d, value = %d", num, freq.get(num)));
        }
    }

    /**
     * find N/K freq
     *
     * Array must be sorted array
     *
     */
    private void findFrequency(int[] array, int start, int end, Map<Integer, Integer> frequency) {
        if(array[start] == array[end]) {
            frequency.put(array[start], frequency.getOrDefault(array[start], 0) + end - start + 1);
        } else {
            int mid = start + (end - start)/2;
            findFrequency(array, start, mid, frequency);
            findFrequency(array, mid + 1, end, frequency);
        }
    }


    /**
     * The follow 3 functions can be used to find majority number.
     *
     * The input array can be a unsorted array and the majority number
     * must be exist.
     *
     * If the majority number may not exist in the array, use v1 and
     * add some minor change.
     *
     */
    // O(n) + O(1)
    public int L169_majorityElement(int[] nums) {
        int ret = 0;
        for(int i = 0; i < 32; i++) {
            int counter = 0;
            for(int j = 0; j < nums.length; j++) {
                if(((1 << i) & nums[j]) == (1 << i)) {
                    counter++;
                }
            }
            if(counter > nums.length/2)
                ret |= 1 << i;
        }
        return ret;
    }

    //O(nlogN) + O(1)
    public int L169_majorityElement_v2(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length/2];
    }

    //O(n) + O(1)
    public int L169_majorityElement_v1(int[] nums) {
        int currentNum = nums[0];
        int currentNumCounter = 1;
        for(int i = 1; i < nums.length; i++) {
            if(currentNumCounter == 0) {
                currentNum = nums[i];
                currentNumCounter = 1;
            } else if (currentNum == nums[i]) {
                currentNumCounter++;
            } else {
                currentNumCounter--;
            }
        }

        //if the majority element is not guranteed, then we need to scan the array again
        //to verify if the result is the majority element
        return currentNum;
    }


}
