package com.stevenli.interview.easy.interview;

public class Problem_Presum_VS_Bit {
}

class L307_Range_Sum_Query_Mutable_ {
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

    public L307_Range_Sum_Query_Mutable_(int[] nums) {
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

class L304_Range_Sum_Query_2D_Immutable {
    private int[][] preSum = null;

    public L304_Range_Sum_Query_2D_Immutable(int[][] matrix) {
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }

        int row = matrix.length;
        int col = matrix[0].length;
        preSum = new int[row][col + 1];
        for(int i = 0; i < row; ++i) {
            for(int j = 0; j < col; ++j) {
                preSum[i][j + 1] = preSum[i][j] + matrix[i][j];
            }
        }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        if(preSum == null) {
            return 0;
        }

        int sum = 0;
        for(int i = row1; i <= row2; ++i) {
            sum += preSum[i][col2 + 1] - preSum[i][col1];
        }
        return sum;
    }
}


class L308_Range_Sum_Query_2D_Mutable {
    class BIT {
        private int[] bit;
        public BIT(int[] nums) {
            bit = new int[nums.length + 1];
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
            int sum = 0;
            for(int i = index; i > 0; i -= lowbit(i)) {
                sum += bit[i];
            }
            return sum;
        }
    }

    private int[][] copy = null;
    private BIT[] bitArray = null;

    public L308_Range_Sum_Query_2D_Mutable(int[][] matrix) {
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }

        int row = matrix.length;
        int col = matrix[0].length;

        bitArray = new BIT[row];
        copy = new int[row][col];
        for(int i = 0; i < row; ++i) {
            bitArray[i] = new BIT(matrix[i]);
            copy[i] = matrix[i].clone();
        }
    }

    public void update(int row, int col, int val) {
        if(bitArray == null || copy == null) {
            return;
        }

        int diff = val - copy[row][col];
        bitArray[row].add(col + 1, diff);
        copy[row][col] = val;
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        if(bitArray == null || copy == null) {
            return 0;
        }

        int sum = 0;
        for(int i = row1; i <= row2; ++i) {
            sum += bitArray[i].query(col2 + 1) - bitArray[i].query(col1);
        }
        return sum;
    }
}
