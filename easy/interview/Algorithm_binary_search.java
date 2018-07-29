package com.stevenli.interview.easy.interview;

import java.util.*;

public class Algorithm_binary_search {

    /**
     * 209 Minimum Size Subarray Sum
     * @param s
     * @param nums
     * @return
     */
    public int L209_minSubArrayLen(int s, int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int[] prefixSum = new int[nums.length + 1];
        for(int i = 0; i < nums.length; ++i) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        int minLen = Integer.MAX_VALUE;
        for(int i = 0; i < nums.length + 1; ++i) {
            int index = binarySearch(i, prefixSum, prefixSum[i] + s);
            if(index != Integer.MAX_VALUE) {
                minLen = Math.min(minLen, index - i);
            }
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    int binarySearch(int start, int[] sum, int value) {
        int end = sum.length, mid = 0, result = Integer.MAX_VALUE;
        while(start < end) {
            mid = start + (end - start)/2;
            if(sum[mid] < value) {
                start = mid + 1;
            } else {
                result = Math.min(result, mid);
                end = mid;
            }
        }

        return result;
    }


    /**
     * Find a number
     */
    public int findNumber() {
        int[] array = {1, 3, 5, 6};
        int target = 1;

        int start = 0, end = array.length, mid = 0;
        while(start < end) {
            mid = start + (end - start)/2;
            if(array[mid] == target) {
                return mid;
            } else if(array[mid] > target) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }

        return -1;
    }


    /**
     * 162. Find Peak Element
     *
     * @param nums
     * @return
     */
    public int L162_findPeakElement(int[] nums) {
        if(nums == null || nums.length == 0)
            return -1;
        if(nums.length == 1 || nums[0] > nums[1])
            return 0;
        if(nums[nums.length - 1] > nums[nums.length - 2])
            return nums.length - 1;

        int start = 1, end = nums.length - 1, mid = 0;
        while(start < end) {
            mid = start + (end - start)/2;
            if(nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1])
                return mid;
            else if(nums[mid] < nums[mid + 1])
                start = mid + 1;
            else
                end = mid;
        }

        return -1;
    }


    /**
     * 69. Sqrt(x)
     *
     * @param x
     * @return
     */
    public int L69_mySqrt(int x) {
        if(x == 0 || x == 1)
            return x;

        int res = -1;
        int left = 1, right = x, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            if(x/middle >= middle) {
                res = Math.max(res, middle);
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return res;
    }

    /**
     * LintCode: Sqrt(x) II
     *
     * @param x
     * @return
     */
    public double LintCode_586_sqrt(double x) {
        // write your code here
        if(x == 0 || x == 1) {
            return x;
        }

        double res = -1;
        double esp = 1e-10;
        double start = 0, end = 0, middle = 0;
        if(x < 1) {
            end = 1;
        }else {
            end = x;
        }

        while(start + esp < end) {
            middle = start + (end - start)/2;
            if(middle <= x/middle ) {
                start = middle + esp;
                res = Math.max(res, middle);
            } else {
                end = middle;
            }
        }

        return res;
    }


    /**
     * 278. First Bad Version
     * @param n
     * @return
     */
    public int L278_firstBadVersion(int n) {
        if(n == 1 && isBadVersion(n))
            return n;

        int firstBadVersion = Integer.MAX_VALUE;
        int left = 1, right = n + 1, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            if(isBadVersion(middle)) {
                firstBadVersion = Math.min(firstBadVersion, middle);
                right = middle;
            } else {
                left = middle + 1;
            }
        }
        return firstBadVersion;
    }

    private boolean isBadVersion(int n) {
        return true;
    }


    /**
     * LintCode: WoodCut
     *
     * @param L
     * @param k
     * @return
     */
    public int LintCode_183_woodCut(int[] L, int k) {
        if(k <= 0) {
            return 0;
        }

        int longestWood =  0;
        for(int wood : L) {
            longestWood = Math.max(longestWood, wood);
        }

        if(k == 1)
            return longestWood;

        int left = 1, right = longestWood, middle = 0;
        int res = 0, numberOfCuts = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            numberOfCuts = cutWood(L, middle);
            if(numberOfCuts >= k) {
                res = Math.max(res, middle);
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return res;
    }

    private int cutWood(int[] L, int len) {
        int count = 0;
        for(int wood : L) {
            count += wood/len;
        }
        return count;
    }


    /**
     * LintCode : Copy books
     *
     * @param pages
     * @param k
     * @return
     */
    public int Lintcode_437_copyBooks(int[] pages, int k) {
        // write your code here
        if(pages == null || pages.length == 0 || k == 0)
            return 0;

        int max = Integer.MIN_VALUE, sum = 0;
        for(int i = 0; i < pages.length; ++i) {
            sum += pages[i];
            max = Math.max(max, pages[i]);
        }

        if(k >= pages.length)
            return max;

        int res = Integer.MAX_VALUE;
        int start = max, end = sum + 1, middle = 0;
        while(start < end) {
            middle = start + (end - start)/2;
            if(check(pages, middle, k)) {
                res = Math.min(res, middle);
                end = middle;
            } else {
                start = middle + 1;
            }
        }
        return res;
    }

    private boolean check(int[] pages, int ave, int k) {
        int copyCount = 0, pageCount = 0;
        for(int page: pages) {
            if(pageCount + page == ave) {
                pageCount = 0;
                copyCount++;
            } else if(pageCount + page > ave) {
                pageCount = page;
                copyCount++;
            } else {
                pageCount += page;
            }
        }
        if(pageCount != 0) {
            copyCount += 1;
        }
        if(copyCount <= k) {
            return true;
        }

        return false;
    }

    /**
     * Maximum Average Subarray II
     */
    public double L644_maxAverage(int[] nums, int k) {
        // write your code here
        if(k == 0) {
            return 0;
        }

        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        for(int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        if(check(nums, k, max)){
            return max;
        }

        double esp = 1e-5;
        double res = Integer.MIN_VALUE;
        double start = min, end = max, middle = 0;
        while(start + esp < end) {
            middle = start + (end - start)/2;
            if(check(nums, k, middle)) {
                res = Math.max(res, middle);
                start = middle + esp;
            } else {
                end = middle;
            }
        }
        return res;
    }

    private boolean check(int[] nums, int k, double ave) {
        double[] sum = new double[nums.length + 1];
        double preMin = Integer.MAX_VALUE;
        for(int i = 1; i < nums.length + 1; ++i) {
            sum[i] = sum[i - 1] + nums[i - 1] - ave;
            if(i >= k) {
                preMin = Math.min(preMin, sum[i - k]);
                if(sum[i] - preMin >= 0){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * LintCode 390. Find Peak Element II
     * O(mlogn) => O(m+n)
     * @param A
     * @return
     */
    public List<Integer> findPeakII(int[][] A) {
        // this is the nlog(n) method
        List<Integer> ret = new ArrayList<>();
        if(A == null || A.length == 0 || A[0].length == 0) {
            return ret;
        }

        //int[] numbers = findPeakII_nlogm(A, 1, A.length - 1, 1, A[0].length - 1);
        int[] numbers = findPeakII_nPlusm(A, 1, A.length - 1, 1, A[0].length - 1);
        for(int number : numbers) {
            ret.add(number);
        }
        return ret;
    }

    // time complexity: O(m * logn)
    private int[] findPeakII_nlogm(int[][] array, int rowStart, int rowEnd, int colStart, int colEnd) {
        while(rowStart < rowEnd) {
            int mid = rowStart + (rowEnd - rowStart)/2;
            int[] ret = findPeakInRow(array, mid, colStart, colEnd);
            if(ret.length == 0 || ret[0] == mid) {
                return ret;
            } else if(ret[0] > mid) {
                rowStart = ret[0];
            } else {
                rowEnd = mid;
            }
        }

        return new int[0];
    }

    // time complexity: O(n + m)
    private int[] findPeakII_nPlusm(int[][] array, int rowStart, int rowEnd, int colStart, int colEnd) {
        while(rowStart < rowEnd && colStart < colEnd) {
            if(rowStart < rowEnd) {
                int mid = rowStart + (rowEnd - rowStart)/2;
                int[] ret = findPeakInRow(array, mid, colStart, colEnd);
                if(ret.length == 0 || ret[0] == mid) {
                    return ret;
                } else if(ret[0] > mid) {
                    rowStart = ret[0];
                } else {
                    rowEnd = mid;
                }
            }
            if(colStart < colEnd) {
                int mid = colStart + (colEnd - colStart)/2;
                int[] ret = findPeakInCol(array, mid, rowStart, rowEnd);
                if(ret.length == 0 || ret[1] == mid) {
                    return ret;
                } else if(ret[1] > mid) {
                    colStart = ret[1];
                } else {
                    colEnd = mid;
                }
            }
        }
        return new int[0];
    }

    private int[] findPeakInCol(int[][] array, int colIndex, int rowStart, int rowEnd) {
        int rowIndex = -1;
        int maxValue = Integer.MIN_VALUE;
        for(int i = rowStart; i < rowEnd; ++i) {
            if(array[i][colIndex] > maxValue) {
                maxValue = array[i][colIndex];
                rowIndex = i;
            }
        }
        int[] ret = new int[2];
        if(array[rowIndex][colIndex] > array[rowIndex][colIndex - 1]
                && array[rowIndex][colIndex] > array[rowIndex][colIndex + 1]) {
            ret[0] = rowIndex;
            ret[1] = colIndex;
        } else if (array[rowIndex][colIndex] < array[rowIndex][colIndex - 1]) {
            ret[0] = rowIndex;
            ret[1] = colIndex - 1;
        } else if(array[rowIndex][colIndex] < array[rowIndex][colIndex + 1]) {
            ret[0] = rowIndex;
            ret[1] = colIndex + 1;
        } else {
            return new int[0];
        }
        return ret;
    }

    private int[] findPeakInRow(int[][] array, int rowIndex, int colStart, int colEnd) {
        int colIndex = -1;
        int maxValue = Integer.MIN_VALUE;
        for(int i = colStart; i < colEnd; ++i) {
            if(array[rowIndex][i] > maxValue) {
                maxValue = array[rowIndex][i];
                colIndex = i;
            }
        }
        int[] index = new int[2];
        if(array[rowIndex][colIndex] > array[rowIndex - 1][colIndex]
                && array[rowIndex][colIndex] > array[rowIndex + 1][colIndex]) {
            index[0] = rowIndex;
            index[1] = colIndex;
        } else if(array[rowIndex][colIndex] < array[rowIndex - 1][colIndex]) {
            index[0] = rowIndex - 1;
            index[1] = colIndex;
        } else if(array[rowIndex][colIndex] < array[rowIndex + 1][colIndex]) {
            index[0] = rowIndex + 1;
            index[1] = colIndex;
        } else {
            return new int[0];
        }

        return index;
    }

    public static void main(String[] args) {
        int[][] array = {
                {1, 2, 3, 4, 5},
                {16,41,23,22,6},
                {15,17,20,21,7},
                {14,18,19,20,8},
                {13,12,11,10,9}
        };

        Algorithm_binary_search processor = new Algorithm_binary_search();
        for(int i : processor.findPeakII(array)) {
            System.out.println(i);
        }
    }


}
