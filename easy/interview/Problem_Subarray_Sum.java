package com.stevenli.interview.easy.interview;

import java.util.*;

public class Problem_Subarray_Sum {
}

class LintCode_138_Subarray_Sum {
    public List<Integer> subarraySum(int[] nums) {
        // write your code here
        List<Integer> result = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return result;
        }

        Map<Integer, Integer> cache = new HashMap<>();
        cache.put(0,0);
        int[] preSum = new int[nums.length + 1];
        for(int i = 1; i < preSum.length; ++i) {
            preSum[i] = preSum[i - 1] + nums[i - 1];
            if(!cache.containsKey(preSum[i])) {
                cache.put(preSum[i], i);
            } else {
                result.add(cache.get(preSum[i]));
                result.add(i - 1);
                break;
            }
        }

        return result;
    }
}

class LintCode_838_Subarray_Sum_Equals_K {
    public int subarraySumEqualsK(int[] nums, int k) {
        // write your code here
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int result = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0,1);

        int[] preSum = new int[nums.length + 1];
        for(int i = 1; i < preSum.length; ++i) {
            preSum[i] = nums[i - 1] + preSum[i - 1];
            if(map.containsKey(preSum[i] - k)) {
                result += map.get(preSum[i] - k);
            }

            int temp = map.containsKey(preSum[i]) ? map.get(preSum[i]) + 1 : 1;
            map.put(preSum[i], temp);
        }

        return result;
    }
}



class LintCode_404_Subarray_Sum_II {
    public int subarraySumII(int[] A, int start, int end) {
        // write your code here
        if(A == null || A.length == 0 || start > end) {
            return 0;
        }

        int[] preSum = new int[A.length + 1];
        for(int i = 1; i < preSum.length; ++i) {
            preSum[i] = A[i - 1] + preSum[i - 1];
        }

        int result = 0;
        for(int i = 0; i < preSum.length; ++i) {
            int index1 = binarySearch(preSum, preSum[i] + start);
            if(index1 < 0) {
                index1 = -index1 - 1;
            }
            int index2 = binarySearch(preSum, preSum[i] + end + 1);
            if(index2 < 0) {
                index2 = -index2 - 1;
            }
            result += index2 - index1;
        }
        return result;
    }

    int binarySearch(int[] array, int number) {
        int start = 0, end = array.length, middle = 0;
        int result = 0;
        if(number < array[0]) {
            return -1;
        } else if(number > array[array.length - 1]) {
            return -array.length - 1;
        }

        while(start < end) {
            middle = start + (end - start)/2;
            if(array[middle] >= number) {
                result = middle;
                end = middle;
            } else {
                start = middle + 1;
            }
        }
        return result;
    }
}


class LintCode_405_Submatrix_Sum {
    public int[][] submatrixSum(int[][] matrix) {
        // write your code here
        int[][] result = new int[2][2];

        if(matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }

        int row = matrix.length + 1;
        int col = matrix[0].length + 1;
        int[][] preSum = new int[row][col];
        for(int j = 1; j < col; ++j) {
            for(int i = 1; i < row; ++i) {
                preSum[i][j] = matrix[i - 1][j - 1] + preSum[i - 1][j];
            }
        }

        for(int start = 0; start < row; ++start) {
            for(int end = start + 1; end < row; ++end) {
                if(findMatrix(preSum, start, end, result)) {
                    return result;
                }
            }
        }

        return result;
    }

    boolean findMatrix(int[][] matrix, int start, int end, int[][] result) {
        int col = matrix[0].length;
        int[] colPreSum = new int[col];
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        for(int i = 1; i < col; ++i) {
            colPreSum[i] = colPreSum[i - 1] + getValue(matrix, i, start, end);
            if(!map.containsKey(colPreSum[i])) {
                map.put(colPreSum[i], i);
            } else {
                int[] leftUp = result[0];
                int[] rightDown = result[1];
                leftUp[1] = map.get(colPreSum[i]);
                rightDown[1] = i - 1;
                leftUp[0] = start;
                rightDown[0] = end - 1;
                return true;
            }
        }

        return false;
    }

    int getValue(int[][] array, int col, int rowStart, int rowEnd) {
        return array[rowEnd][col] - array[rowStart][col];
    }
}


class L53_Maximum_Subarray {
    public int maxSubArray(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int maxSum = Integer.MIN_VALUE;
        int sum = 0;
        for(int i = 0; i < nums.length; ++i) {
            sum = Math.max(sum + nums[i], nums[i]);
            maxSum = Math.max(maxSum, sum);

        }
        return maxSum;
    }
}


class LintCode_402_Continuous_subarray_Sum {
    public List<Integer> continuousSubarraySum(int[] A) {
        // write your code here
        List<Integer>  result = new ArrayList<>();
        if(A == null || A.length == 0) {
            return result;
        }

        int maxSum = Integer.MIN_VALUE;
        int maxStart = 0, maxEnd = 0;
        int sum = 0, start = 0, end = 0;
        for(int i = 0; i < A.length; ++i) {
            if(sum + A[i] < A[i]) {
                sum = A[i];
                start = i;
                end = i;
            } else {
                sum = sum + A[i];
                end = i;
            }

            if(maxSum < sum) {
                maxStart = start;
                maxEnd = end;
                maxSum = sum;
            }
        }

        result.add(maxStart);
        result.add(maxEnd);
        return result;
    }
}


class LintCode_403_Continuous_Subarray_Sum {
    public List<Integer> continuousSubarraySumII(int[] A) {
        // write your code here
        List<Integer> result = new ArrayList<>();
        if(A == null || A.length == 0) {
            return result;
        }

        int totalSum = getSum(A);

        List<Integer> maxIndex = new ArrayList<>();
        int maxSubarraySum = getMaxSubarraySum(A, maxIndex);

        List<Integer> minIndex = new ArrayList<>();
        int minSubarraySum = getMinSubarraySum(A, minIndex);

        int minStart = minIndex.get(0);
        int minEnd = minIndex.get(1);
        if((minStart == 0 && minEnd == A.length - 1)
                || maxSubarraySum >= totalSum - minSubarraySum) {
            return maxIndex;
        }

        result.add(minEnd + 1);
        result.add(minStart - 1);
        return result;
    }

    int getSum(int[] A) {
        int sum = 0;
        for(int i = 0; i < A.length; ++i) {
            sum += A[i];
        }
        return sum;
    }

    int getMaxSubarraySum(int[] A, List<Integer> list) {
        int max = Integer.MIN_VALUE;
        list.add(0,0);
        list.add(1,0);
        int dp = 0, start = 0, end = 0;
        for(int i = 0; i < A.length; ++i) {
            if(dp + A[i] < A[i]) {
                dp = A[i];
                start = end = i;
            } else {
                dp += A[i];
                end = i;
            }
            if(dp > max) {
                max = dp;
                list.set(0, start);
                list.set(1, end);
            }
        }
        return max;
    }

    int getMinSubarraySum(int[] A, List<Integer> list) {
        int min = Integer.MAX_VALUE;
        list.add(0,0);
        list.add(1,0);
        int dp = 0, start = 0, end = 0;
        for(int i = 0; i < A.length; ++i) {
            if(dp + A[i] > A[i]) {
                dp = A[i];
                start = end = i;
            } else {
                dp += A[i];
                end = i;
            }
            if(dp < min) {
                min = dp;
                list.set(0, start);
                list.set(1, end);
            }
        }
        return min;
    }
}


class LintCode_944_Maximum_Submatrix {
    public int maxSubmatrix(int[][] matrix) {
        // write your code here
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int row = matrix.length + 1;
        int col = matrix[0].length + 1;
        int[][] preSum = new int[row][col];
        for(int j = 1; j < col; ++j) {
            for(int i = 1; i < row; ++i) {
                preSum[i][j] = preSum[i - 1][j] + matrix[i - 1][j - 1];
            }
        }

        int maxSum = Integer.MIN_VALUE;
        for(int start = 0; start < row; ++start) {
            for(int end = start + 1; end < row; ++end) {
                maxSum = Math.max(maxSum, getMaxSubmatrixSum(preSum, start, end));
            }
        }

        return maxSum;
    }

    int getMaxSubmatrixSum(int[][] preSum, int start, int end) {
        int maxSum = Integer.MIN_VALUE;
        int sum = 0;
        for(int i = 1; i < preSum[start].length; ++i) {
            int curValue = getValue(preSum, i, start, end);
            sum = Math.max(sum + curValue, curValue);
            maxSum = Math.max(maxSum, sum);
        }
        return maxSum;
    }

    int getValue(int[][] preSum, int col, int start, int end) {
        return preSum[end][col] - preSum[start][col];
    }
}


class L523_Continuous_Subarray_Sum {
    public boolean checkSubarraySum(int[] nums, int k) {
        if(nums == null || nums.length == 0 || nums.length < 2) {
            return false;
        }

        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);

        int sum = 0, prev = 0;
        for(int i = 0; i < nums.length; ++i) {
            sum += nums[i];
            if(k != 0) {
                sum = sum%k;
            }
            if(!map.containsKey(sum)) {
                map.put(sum, i);
            } else {
                prev = map.get(sum);
                if(i - prev > 1) {
                    return true;
                }
            }
        }
        return false;
    }
}



class L209_Minimum_Size_Subarray_Sum_ {
    public int minSubArrayLen(int s, int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int minSize = Integer.MAX_VALUE;
        int j = 0, sum = 0;
        for(int i = 0; i < nums.length; ++i) {
            while(j < nums.length && sum + nums[j] < s) {
                sum += nums[j];
                j++;
            }

            if(j == nums.length) {
                break;
            } else {
                minSize = Math.min(minSize, j - i + 1);
                sum -= nums[i];
            }
        }

        return minSize == Integer.MAX_VALUE ? 0 : minSize;
    }
}


class L643_Maximum_Average_Subarray_I {
    public double findMaxAverage(int[] nums, int k) {
        double maxAve = Integer.MIN_VALUE;
        double sum = 0;
        for(int i = 0; i < nums.length; ++i) {
            sum += nums[i];
            if(i >= k - 1) {
                maxAve = Math.max(maxAve, sum/k);
                sum -= nums[i - k + 1];
            }
        }
        return maxAve;
    }
}





