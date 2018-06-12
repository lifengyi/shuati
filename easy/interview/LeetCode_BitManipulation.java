package com.stevenli.interview.easy.interview;

public class LeetCode_BitManipulation {
    /**
     * 260. Single Number III
     *
     * @param nums
     * @return
     */
    public int[] L260_singleNumber(int[] nums) {
        int flag = 0;
        int[] ret = new int[2];
        for(int num : nums) {
            flag = flag ^ num;
        }

        flag = flag & (-flag);
        for(int num : nums){
            if((num & flag) != 0) {
                ret[0] = ret[0] ^ num;
            } else {
                ret[1] = ret[1] ^ num;
            }
        }
        return ret;
    }

    /**
     * 137. Single Number II
     *
     * @param nums
     * @return
     */
    public int L137_singleNumber(int[] nums) {
        int ret = 0, count = 0;
        for(int i = 0; i< 32; i++) {
            count = 0;
            for(int num : nums) {
                if((num & (1 << i)) != 0)
                    count = (count + 1)%3;
            }
            if(count != 0)
                ret |= 1 << i;
        }

        return ret;
    }

    public int L137_singleNumber_v1(int[] nums) {
        int ones = 0, twos = 0;
        for(int num : nums) {
            ones = (ones ^ num) & (~twos);
            twos = (twos ^ num) & (~ones);
        }

        return ones;
    }

    /**
     * 371. Sum of Two Integers
     *
     * @param a
     * @param b
     * @return
     */
    public int L371_getSum(int a, int b) {
        int inc = a & b;
        int valueOfXOR = a ^ b;
        if(inc == 0)
            return valueOfXOR;
        else
            return L371_getSum(valueOfXOR, inc<<1);
    }

    /**
     * 191. Number of 1 Bits
     *
     * @param n
     * @return
     */
    public int L191_hammingWeight(int n) {
        int count = 0, num = n;
        while(num != 0) {
            count++;
            num = num & (num - 1);
        }
        return count;
    }

    /**
     * 190. Reverse Bits
     *
     * @param n
     * @return
     */
    // you need treat n as an unsigned value
    public int L190_reverseBits_v1(int n) {
        int res = 0;
        for(int i = 0; i < 32; ++i) {
            res = (res << 1) + (n & 1);
            n = n >> 1;
        }
        return res;
    }

    public int L190_reverseBits(int n) {
        //reverse 2 bytes
        int target = ((n & 0x0000FFFF) << 16) | ((n & 0xFFFF0000) >>> 16);
        //reverse 1 bytes
        target = ((target & 0x00FF00FF) << 8) | ((target & 0xFF00FF00) >>> 8);
        //reverse  4 bits
        target = ((target & 0x0F0F0F0F) << 4) | ((target & 0xF0F0F0F0) >>> 4);
        //reverse 2 bites
        target = ((target & 0x33333333) << 2) | ((target & 0xCCCCCCCC) >>> 2);
        //revers 1 bitys
        target = ((target & 0x55555555) << 1) | ((target & 0xAAAAAAAA) >>> 1);

        return target;
    }
}
