package interview;

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

class L493_Reverse_Pairs {
    public int reversePairs(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int len = nums.length;
        int[] bit = new int[len + 1];

        long[] copy = init(nums);
        Arrays.sort(copy);

        int index = 0, sum = 0;
        for(int num : nums) {
            index = getIndex(copy, 2L * num);
            if(index < 0) {
                index = -index - 1;
                if(index == 0) {
                    sum += query(bit, len);
                } else if(index > 0 && index < len) {
                    sum += query(bit, len) - query(bit, index);
                }
            } else {
                sum += query(bit, len) - query(bit, index + 1);
            }

            index = getIndex(copy, (long)num);
            update(bit, index + 1, 1);
        }

        return sum;
    }

    int getIndex(long[] nums, long num) {
        return Arrays.binarySearch(nums, num);
    }

    int lowbit(int n) {
        return n & (-n);
    }

    int query(int[] bit, int index) {
        int result = 0;
        for(int i = index; i > 0; i -= lowbit(i)) {
            result += bit[i];
        }
        return result;
    }

    void update(int[] bit, int index, int value) {
        for(int i = index; i < bit.length; i += lowbit(i)) {
            bit[i] += value;
        }
    }

    long[] init(int[] nums) {
        long[] result = new long[nums.length];
        for(int i = 0; i < nums.length; ++i) {
            result[i] = nums[i];
        }
        return result;
    }
}

/**
 *  BIT 注意:
 *  1. 外部索引和内部索引之间的转换差1
 *  2. bit操作需要区别是差值还是绝对值
 *
 *  鉴于以上两个注意点，推荐：
 *  1. BIT的add/query单独实现，使用bit索引值
 *  2. BIT的add传入的为差值，而不是绝对值
 *
 *  外部函数update/query单独实现，调用BIT函数需转换成bit索引值，
 *  同时外部函数update调用BIT::update时需要转成差值，
 *  绝对值操作的注意点：如果保留数据copy，则update需要同时更新copy
 *  即：BIT和外部函数分开独立实现；
 */
class L307_Range_Sum_Query_Mutable {
    class Bit {
        private int[] bit;

        public Bit(int[] nums) {
            this.bit = new int[nums.length + 1];
            for(int i = 0; i < nums.length; ++i) {
                add(i + 1, nums[i]);
            }
        }

        private int lowbit(int n) {
            return n & (-n);
        }

        public void add(int index, int diff) {
            for(int i = index; i < bit.length; i += lowbit(i)) {
                bit[i] += diff;
            }
        }

        public int query(int index) {
            int ret = 0;
            for(int i = index; i > 0; i -= lowbit(i)) {
                ret += bit[i];
            }
            return ret;
        }
    }

    private Bit bit = null;
    private int[] copy = null;

    public L307_Range_Sum_Query_Mutable(int[] nums) {
        bit = new Bit(nums);
        copy = nums.clone();
    }

    public void update(int index, int val) {
        int diff = val - copy[index];
        bit.add(index + 1, diff);
        copy[index] = val;
    }

    public int sumRange(int i, int j) {
        return bit.query(j + 1) - bit.query(i);
    }
}
