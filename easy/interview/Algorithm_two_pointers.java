package com.stevenli.interview.easy.interview;

public class Algorithm_two_pointers {
}

class L209_Minimum_Size_Subarray_Sum {
    public int minSubArrayLen(int s, int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;
        int j = 0, sum = 0, count = 0;
        for(int i = 0; i < nums.length; ++i) {
            while(j < nums.length && sum + nums[j] < s) {
                sum += nums[j];
                j++;
            }

            if(j == nums.length) {
                break;
            }

            minLen = Math.min(minLen, j - i + 1);
            sum -= nums[i];
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }
}
