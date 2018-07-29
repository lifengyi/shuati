package com.stevenli.interview.easy.interview;

import java.util.*;

public class Algorithm_binary_indexed_tree {

    int[] c;
    int n;

    public Algorithm_binary_indexed_tree(int n) {
        this.n = n;
        this.c = new int[n+1];
    }

    int lowbit(int x) {
        return x & -x;
    }

    void update(int p, int d) {
        for(int i = p; i <= n; i += lowbit(i)) {
            c[i] += d;
        }
    }


    int sum(int p) {
        int ret = 0;
        for(int i = p; i > 0; i -= lowbit(i)) {
            ret += c[i];
        }
        return ret;
    }


    int sum(int s, int e){
        if(s > n || e < 1 || s > e || s < 1 || e > n){
            System.out.println("input error!");
            return -1;
        }
        return sum(e) - sum(s - 1);
    }

    int[] getBIT() {
        return c;
    }

    public static void main(String[] args) {
        int[] numbers = {1,2,3,4,5,6,7,8,9};
        Algorithm_binary_indexed_tree bit = new Algorithm_binary_indexed_tree(numbers.length);
        for (int i=0; i<numbers.length; i++) {
            bit.update(i+1, numbers[i]);
        }

        System.out.println(Arrays.toString(bit.getBIT()));

        System.out.println("1-6的和为："+bit.sum(6));

        bit.update(3, 4);
        System.out.println( "第三个元素 +4后:"+Arrays.toString(bit.getBIT()));

        System.out.println("第三个元素 +4后.2-6的和为："+bit.sum(2,6));
    }

}

class L315_CountOfSmallerNumbersAfterSelf {
    private int lowbit(int x) {
        return x & (-x);
    }

    private void add(int[] bit, int index, int val) {
        for(int i = index; i < bit.length; i += lowbit(i)) {
            bit[i] += val;
        }
    }

    private int query(int[] bit, int index) {
        int ret = 0;
        for(int i = index; i > 0; i -= lowbit(i)) {
            ret += bit[i];
        }
        return ret;
    }

    private void decrete(int[] nums) {
        int[] newNums = nums.clone();
        Arrays.sort(newNums);
        for(int i = 0; i < nums.length; ++i) {
            nums[i] = Arrays.binarySearch(newNums, nums[i]) + 1;
        }
    }

    public List<Integer> countSmaller(int[] nums) {
        List<Integer> ret = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return ret;
        }

        decrete(nums);

        int[] bit = new int[nums.length + 1];
        for(int num : nums) {
            add(bit, num, 1);
        }

        for(int num : nums) {
            ret.add(query(bit, num - 1));
            add(bit, num, -1);
        }

        return ret;
    }
}
