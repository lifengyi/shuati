package interview;

import java.util.*;

public class Algorithm_BinarySearch {
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

    public static void main(String[] args) {
        int[][] array = {
                {1, 2, 3, 4, 5},
                {16,41,23,22,6},
                {15,17,20,21,7},
                {14,18,19,20,8},
                {13,12,11,10,9}
        };

        Algorithm_BinarySearch processor = new Algorithm_BinarySearch();
        //for(int i : processor.findPeakII(array)) {
        //    System.out.println(i);
        //}
    }


}


/**
 * 还有双指针解法
 */
class L209_Minimum_Size_Subarray_Sum_BinarySearch {
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
}


class L162_Find_Peak_Element {
    public int findPeakElement(int[] nums) {
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
}

class L278_First_Bad_Version {
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
}


class LintCode_183_Cut_Wood {
    public int woodCut(int[] L, int k) {
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

}


class LintCode_437_Copy_Books {
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
}

class L644_Maximum_Average_Subarray_II {
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
}


/**
 * Sqrt X : 求平方根下界
 *
 * Sqrt x variant : 求平方根上界
 *      tips:
 *      1. 需要排除head的特殊情况： 0， 1，2
 *      2. tail应该不需要排除，以为最后一个值是解决方案，所以不用排除，也不用+1操作；
 *      3. 代码中时候用long来定义left， middle，right等
 */
class L69_Sqrt_X {
    public int mySqrt(int x) {
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
     * 提前判断head、tail，使用左闭合区间，无重复值求下界
     * 每次middle小于等于target，移动left时，保存结果值
     *
     */
    public int mySqrt_vLong(int x) {
        if(x == 0 || x == 1)
            return x;

        int res = -1;
        long left = 1, right = x, middle = 0;
        long target = x;
        while(left < right) {
            middle = left + (right -left)/2;
            if(middle * middle <= x) {
                res = (int)middle;
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return res;
    }


    /**
     * 可能存在重复值，取上界，左闭合区间
     * 取重合点即为结果值，可能涉及越界区；
     */
    public void BS_Upperbound() {
        int[] array = {1, 2};
        int num = 0;

        int left = 0, right = array.length, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            int value = array[middle];
            if(value <= num) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        System.out.println("index = " + left);
    }
}



class Sqrt_X_varint {
    int sqrt(int x) {
        if(x == 0 || x == 1 || x == 2)
            return x;

        int ret = -1;
        long target = x, left = 1, right = x, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            if(middle * middle == target) {
                return (int)middle;
            }
            else if(middle * middle < target) {
                left = middle + 1;
            } else {
                ret = (int)middle;
                right = middle;
            }
        }
        return ret;
    }
}



class LintCode_Sqrt_X_II {
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
                start = middle;
                res = Math.max(res, middle);
            } else {
                end = middle;
            }
        }

        return res;
    }
}



/**
 *  O(mlogn) => O(m+n)
 */
class LintCode_390_Find_Peak_Element_II {
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
}


/**
 * binary search: O(nlogn)
 *
 * v1: 自己写二分
 * v2: 库函数二分
 *     tips: Arrays.binarySearch 的返回值的意义，以及fromIndex，toIndex的使用
 *
 */
class L300_Longest_Increasing_Subsequence_BinarySearch {
    public int lengthOfLIS(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }
        if(nums.length == 1) {
            return 1;
        }

        int[] seq = new int[nums.length];
        seq[0] = nums[0];
        int len = 1;
        for(int i = 1; i < nums.length; ++i) {
            if(seq[len - 1] < nums[i]) {
                seq[len++] = nums[i];
                continue;
            } else if(seq[len - 1] == nums[i]) {
                continue;
            }

            if(len == 1) {
                seq[0] = nums[i];
            } else {
                int index = binarySearch(seq, len, nums[i]);
                seq[index] = nums[i];
            }
        }

        return len;
    }

    int binarySearch(int[] seq, int len, int target) {
        int start = 0, end = len, middle = 0;
        int index = Integer.MAX_VALUE;
        while(start < end) {
            middle = start + (end - start)/2;
            if(seq[middle] >= target) {
                end = middle;
                index = Math.min(index, middle);
            } else {
                start = middle + 1;
            }
        }
        return index;
    }

    public int lengthOfLIS_v2(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }
        if(nums.length == 1) {
            return 1;
        }

        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        int len = 1;
        for(int i = 1; i < nums.length; ++i) {
            if(nums[i] > dp[len - 1]) {
                dp[len++] = nums[i];
            } else if(nums[i] < dp[len - 1]) {
                int index = Arrays.binarySearch(dp, 0, len - 1, nums[i]);
                if(index < 0) {
                    index = -(index) - 1;
                    dp[index] = nums[i];
                }
            }
        }
        return len;
    }
}






class L33_Search_in_Rotated_Sorted_Array {
    public int L33_search(int[] nums, int target) {
        int begin = 0;
        int end = nums.length - 1;

        while(begin <= end) {

            int middle = (begin + end)/2;
            if(nums[middle] == target) {
                return middle;
            }

            if(nums[middle] < nums[end]) {
                if(target > nums[middle] && target <= nums[end])
                    begin = middle + 1;
                else
                    end = middle - 1;
            } else {
                if(target >= nums[begin] && target < nums[middle])
                    end = middle -1;
                else
                    begin = middle + 1;
            }

        }

        return -1;
    }
}

/**
 *  本体的原意就不是考二分查找，考察的是观察这题和上题的区别
 *  能否辨别出区别并体现在算法复杂度上的考虑
 *
 *  上题中，首先我们划分两种不同的rotate情况，然后由于肯定
 *  不存在重复数据，所以我们可以通过 target vs middle 以及
 *  target vs start/end 的比较来确定目标数据明确的落在某一
 *  个半边，从而实现 O(logn)的算法复杂度：
 *      f(n) = f(n/2) + O(1)
 *
 *  本例中，首先我们也可以划分不同的rotate的情况，然后在各自分支中
 *  我们要明确由于可能出现重复值，所以我们无法通过比较 target vs middle
 *  以及target vs start/end 来确定目标数据明确的落在某一个半边，
 *  因为start/end的那个随意不得不同时搜索两边
 *      f(n) = 2f(n/2) + O(1)
 *
 *  最坏情况，所有元素之相同，最后找不多数据；
 *
 *  ？？？？
 *
 */
class L81_Search_in_Rotated_Sorted_Array {
    public boolean search(int[] nums, int target) {
        for(int i = 0; i < nums.length; ++i) {
            if(nums[i] == target) {
                return true;
            }
        }
        return false;
    }
}

/**
 * 二分查找注意问题：
 *  1. 是找上界，还是找下界
 *  2. 是否可能移除，左溢出，右溢出
 *
 *  对于target - 1，需要找上界，即找到小于等于target-1的最大索引，防止左溢出；
 *  对于target + 1，需要找下界，即找到大于等于target+1的最小索引，防止右溢出；
 *
 *
 *  换个思路：当我们已知target必然存在的情况下，找target的上界和下界
 *  1. 找上界的时候需要防止右溢出；
 *  2. 找下界的时候需要防止左溢出；
 *
 *  基于上个思路，我们再改一下，如果删除第一个二分查找，即不知道是否target是否存在
 *  直接找target上下界，那么思路会有说明变化？
 *  1. 找上界的时候需要防止左溢出；  （678找5，234找5，只有左溢出）
 *  2. 找下界的时候需要防止右溢出；  （678找5，234找5，只有右溢出）
 *
 *  可以参考和上面第二种思路的比较，可见基本上思路就是，明确求上下界，再明确是否可能溢出，
 *  以及溢出的方向性问题；
 *  该方法套用代码：左闭合区间
 *
 *               求上界：nums[middle] <= target, 保存middle，移动left；否则移动right；
 *                     循环退出后，保存的middle就是目标索引
 *
 *               求下界：nums[middle] < target, 移动left；否则移动right；
 *                     left/right重合导致循环退出后， left/right就是目标索引
 *
 */

class L34_Find_First_and_Last_Position_of_Element_in_Sorted_Array {
    public int[] searchRange(int[] nums, int target) {
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;

        if(nums == null || nums.length == 0) {
            return result;
        }

        int index = Arrays.binarySearch(nums, target);
        if(index < 0) {
            return result;
        }

        int upperBound = findUpperBound(nums, target - 1);
        int lowerBound = findLowerBound(nums, target + 1);
        System.out.println(String.format("u:%d,l:%d", upperBound, lowerBound));
        result[0] = upperBound + 1;
        result[1] = lowerBound - 1;
        return result;
    }

    int findUpperBound(int[] nums, int target) {
        int left = 0, right = nums.length, middle = 0;
        int index = -1;         //must be -1 for find uppper bound，可能左溢出
        while(left < right) {
            middle = left + (right - left)/2;
            if(nums[middle] <= target) {        //注意比较符号是大于等于，以为这是我们要求的目标，上界
                index = middle;
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return index;
    }

    int findLowerBound(int[] nums, int target) {
        int left = 0, right = nums.length, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            if(nums[middle] < target) {     //注意比较符号是小于，因为大于等于是我们要求的目标，下界
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;                    //可能右边溢出
    }

    public int[] searchRange_V2(int[] nums, int target) {
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;

        if(nums == null || nums.length == 0) {
            return result;
        }

        int index = Arrays.binarySearch(nums, target);
        if(index < 0) {
            return result;
        }

        int upperBound = findUpperBound_V2(nums, target);
        int lowerBound = findLowerBound_V2(nums, target);
        System.out.println(String.format("u:%d,l:%d", upperBound, lowerBound));
        result[1] = upperBound;
        result[0] = lowerBound;
        return result;
    }

    int findUpperBound_V2(int[] nums, int target) {
        int left = 0, right = nums.length, middle = 0;
        int index = -1;         //must be -1 for find uppper bound
        while(left < right) {
            middle = left + (right - left)/2;
            if(nums[middle] <= target) {
                index = middle;
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return index;
    }

    int findLowerBound_V2(int[] nums, int target) {
        int left = 0, right = nums.length, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            if(nums[middle] < target) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }


    /**
     * 充分领会二分钟的各种思路：
     *    删除第一个二分搜索，不需要提前判断是否目标值已经存在
     *
     *    直接在搜索上界和下界过程中判断是否找到
     *
     *    找target上界，考虑可能左溢出
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] searchRange_V3(int[] nums, int target) {
        int[] result = new int[2];
        result[0] = -1;
        result[1] = -1;

        if(nums == null || nums.length == 0) {
            return result;
        }

        int upperBound = findUpperBound_V3(nums, target);
        int lowerBound = findLowerBound_V3(nums, target);
        System.out.println(String.format("u:%d,l:%d", upperBound, lowerBound));
        if(upperBound < 0 || lowerBound < 0
                || nums[upperBound] != target || nums[lowerBound] != target) {
            return result;
        }

        result[1] = upperBound;
        result[0] = lowerBound;
        return result;
    }

    int findUpperBound_V3(int[] nums, int target) {
        int left = 0, right = nums.length, middle = 0;
        int index = -1;         //must be -1 for find uppper bound
        while(left < right) {
            middle = left + (right - left)/2;
            if(nums[middle] <= target) {
                index = middle;
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return index;
    }

    int findLowerBound_V3(int[] nums, int target) {
        int left = 0, right = nums.length, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            if(nums[middle] < target) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        if(left == nums.length) {
            left = - left - 1;
        }
        return left;
    }
}