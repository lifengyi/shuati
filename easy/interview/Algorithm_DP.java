package com.stevenli.interview.easy.interview;

import java.util.*;

public class Algorithm_DP {

    /**
     * Use DP to resolve the following 2 problems which
     * have been resolved using monotonous stack
     *
     * 84. Largest Rectangle in Histogram
     * 85. Maximal Rectangle
     */
}

class L62_Unique_Paths {
    public int uniquePaths(int m, int n) {
        if(m == 1 || n == 1) {
            return 1;
        }

        int[][] result = new int[2][m];
        for(int i = 0; i < n; ++i) {
            result[i%2][0] = 1;
        }
        for(int i = 0; i < m; ++i) {
            result[0][i] = 1;
        }
        for(int i = 1; i < n; ++i) {
            for(int j = 1; j < m; ++j) {
                result[i%2][j] = result[i%2][j-1] + result[(i-1)%2][j];
            }
        }

        return result[(n-1)%2][m-1];
    }
}

class L63_Unique_Paths_II {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int row = obstacleGrid.length;
        int col = obstacleGrid[0].length;
        int[][] result = new int[row][col];

        init(obstacleGrid, result, row, col);
        for(int i = 1; i < row; ++i) {
            for(int j = 1; j < col; ++j) {
                if(obstacleGrid[i][j] == 1) {
                    result[i][j] = 0;
                } else {
                    result[i][j] = result[i][j-1] + result[i-1][j];
                }
            }
        }

        return result[row-1][col-1];
    }

    void init(int[][] grid, int[][] result, int row, int col) {
        for(int i = 0; i < row; ++i) {
            if(grid[i][0] == 1) {
                result[i][0] = 0;
            } else {
                result[i][0] =  (i == 0) ? 1 : result[i-1][0];
            }
        }

        for(int i = 0; i < col; ++i) {
            if(grid[0][i] == 1) {
                result[0][i] = 0;
            } else {
                result[0][i] = (i == 0) ? 1 : result[0][i-1];
            }
        }
    }
}

class Unique_Paths_II_Optimized_by_RollingArray {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int row = obstacleGrid.length;
        int col = obstacleGrid[0].length;
        int[][] result = new int[2][col];

        for(int i = 0; i < row; ++i) {
            result[i%2][0] = (obstacleGrid[i][0] == 1) ? 0 : (i == 0) ? 1 : result[(i - 1)%2][0];
            for(int j = 1; j < col; ++j) {
                if(obstacleGrid[i][j] == 1) {
                    result[i%2][j] = 0;
                    continue;
                }

                if(i == 0) {
                    result[i%2][j] = result[i%2][j-1];
                } else {
                    result[i%2][j] = result[i%2][j - 1] + result[(i - 1)%2][j];
                }
            }
        }

        return result[(row - 1)%2][col - 1];
    }
}


class L64_Minimum_Path_Sum {
    public int minPathSum(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        int[][] result = new int[row][col];

        init(grid, result, row, col);

        for(int i = 1; i < row; ++i) {
            for(int j = 1; j < col; ++j) {
                result[i][j] = grid[i][j] + Math.min(result[i][j-1], result[i-1][j]);
            }
        }

        return result[row-1][col-1];
    }

    void init(int[][] grid, int[][] result, int row, int col) {
        for(int i = 0; i < row; ++i) {
            result[i][0] = (i == 0) ? grid[i][0] : grid[i][0] + result[i-1][0];
        }
        for(int i = 0; i < col; ++i) {
            result[0][i] = (i == 0) ? grid[0][i] : grid[0][i] + result[0][i-1];
        }
    }
}


class L64_Minimum_Path_Sum_Optimized_by_RollingArray {
    public int minPathSum(int[][] grid) {
        if(grid == null) {
            return 0;
        }

        int row = grid.length;
        int col = grid[0].length;
        int[][] result = new int[2][col];

        for(int i = 0; i < row; ++i) {
            result[i%2][0] = (i == 0) ? grid[i][0] : grid[i][0] + result[(i - 1)%2][0];
            for(int j = 1; j < col; ++j) {
                if(i == 0) {
                    result[i%2][j] = grid[i][j] + result[i%2][j - 1];
                    continue;
                }

                result[i%2][j] = grid[i][j] + Math.min(result[(i - 1)%2][j], result[i%2][j - 1]);
            }
        }

        return result[(row - 1)%2][col - 1];
    }
}


class L120_Triangle {
    public int minimumTotal(List<List<Integer>> triangle) {
        int row = triangle.size();
        int[][] result = new int[2][row];
        result[0][0] = triangle.get(0).get(0);

        for(int i = 1; i < row; ++i) {
            List<Integer> rowNumbers = triangle.get(i);
            int col = rowNumbers.size();
            for(int j = 0; j < col; ++j) {
                if(j == 0) {
                    result[i%2][j] = result[(i - 1)%2][j] + rowNumbers.get(j);
                } else if(j == col - 1) {
                    result[i%2][j] = result[(i - 1)%2][j - 1] + rowNumbers.get(j);
                } else {
                    result[i%2][j] = Math.min(result[(i - 1)%2][j], result[(i - 1)%2][j - 1]) + rowNumbers.get(j);
                }
            }
        }

        int minTotal = Integer.MAX_VALUE;
        for(int i : result[(row - 1)%2]) {
            minTotal = Math.min(i, minTotal);
        }

        return minTotal;
    }
}


/**
 * 还有双指针解法
 */
class L55_Jump_Games_DP {
    public boolean canJump_v0(int[] nums) {
        if(nums == null || nums.length == 0) {
            return true;
        }
        boolean[] result = new boolean[nums.length];
        result[0] = true;
        for(int i = 0; i < nums.length; ++i) {
            if(result[i] == true) {
                for(int j = i + 1; j <= i + nums[i] && j < nums.length; ++j) {
                    result[j] = true;
                }
            }
        }

        return result[nums.length - 1];
    }
}

/**
 * 还有双指针解法
 */
class L45_Jump_Game_II_DP {
    public int jump(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int[] result = new int[nums.length];
        for(int i = 1; i < nums.length; ++i) {
            result[i] = Integer.MAX_VALUE;
        }

        int maxJump = 0;
        for(int i = 0; i < nums.length; ++i) {
            for(int j = maxJump + 1; j <= i + nums[i] && j < nums.length; ++j) {
                result[j] = Math.min(result[j], result[i] + 1);
            }
            maxJump = Math.max(maxJump, i + nums[i]);
        }

        return result[nums.length - 1];
    }
}


class L70_Climbing_Stairs {
    public int climbStairs(int n) {
        int[] result = new int[2];
        if(n <= 2) {
            return n;
        }
        result[1] = 1;
        result[0] = 2;
        for(int i = 3; i <= n; ++i) {
            result[i%2] = result[(i - 1)%2] + result[(i - 2)%2];
        }

        return result[n%2];
    }
}



class L128_Longest_Consecutive_Sequence {
    public int longestConsecutive(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }
        if(nums.length == 1) {
            return 1;
        }

        Arrays.sort(nums);
        int len = removeDuplicate(nums);
        if(len == 1) {
            return 1;
        }

        int[] dp = new int[2];
        dp[0] = 1;
        int max = dp[0];
        for(int i = 1; i < len; ++i) {
            dp[i%2] = 1;
            if(nums[i - 1] + 1 == nums[i]) {
                dp[i%2] = dp[(i - 1)%2] + 1;
            }
            max = Math.max(max, dp[i%2]);
        }

        return max;
    }

    int removeDuplicate(int[] nums) {
        int end = 0;
        for(int i = 1; i < nums.length; ++i) {
            if(nums[i] == nums[end]) {
                continue;
            }

            end++;
            if(end != i) {
                nums[end] = nums[i];
            }
        }

        return end + 1;
    }
}

class L128_Longest_Consecutive_Sequence_noDP {
    public int longestConsecutive(int[] nums) {
        Set<Integer> cache = new HashSet<>();
        for(int i = 0; i < nums.length; i++) {
            if(!cache.contains(nums[i]))
                cache.add(nums[i]);
        }

        int maxLength = 0;
        for(int number : nums) {
            if(cache.isEmpty())
                break;
            if(cache.contains(number)) {

                int length = 1;
                int successorNumber = number + 1;
                int predecessorNumber = number - 1;
                while(cache.contains(successorNumber)) {
                    cache.remove(successorNumber);
                    length++;
                    successorNumber++;
                }
                while(cache.contains(predecessorNumber)) {
                    cache.remove(predecessorNumber);
                    length++;
                    predecessorNumber--;
                }
                maxLength = Math.max(maxLength, length);
                cache.remove(number);
            }
        }

        return maxLength;
    }
}




/**
 *  LIS 问题
 *
 *  DP解法： O(n^2)
 *  状态转换方程达到了n的平方，无法用滚动数组来优化
 *
 *  还可以：binary search解法: O(nlogn)
 *  还可以：sort + LCS解法：O(nlogn)
 */
class L300_Longest_Increasing_Subsequence {
    public int lengthOfLIS(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int[] result = new int[nums.length];
        for(int i = 0; i < nums.length; ++i) {
            result[i] = 1;
        }

        for(int i = 1; i < nums.length; ++i) {
            for(int j = 0; j < i; ++j) {
                if(nums[j] < nums[i]) {
                    result[i] = Math.max(result[i], result[j] + 1);
                }
            }
        }

        int maxLength = 0;
        for(int ret : result) {
            maxLength = Math.max(maxLength, ret);
        }
        return maxLength;
    }
}

class L674_Longest_Continuous_Increasing_Subsequence {
    public int findLengthOfLCIS(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }
        if(nums.length == 1) {
            return 1;
        }

        int[] dp = new int[2];
        dp[0] = 1;
        int maxLen = dp[0];
        for(int i = 1; i < nums.length; ++i) {
            dp[i%2] = 1;
            if(nums[i] > nums[i - 1]) {
                dp[i%2] = dp[(i - 1)%2] + 1;
            }
            maxLen = Math.max(maxLen, dp[i%2]);
        }

        return maxLen;
    }
}

/**
 *  L128 : Longest Consequtive Sequence
 *         给定一组数字（排序后可变成序列）求子序列
 *         要求：子序列(排序后)是递增的，子序列元素相邻，且数字相差1
 *
 *         dp[i] 取决于排序后序列中前1个元素的状态值
 *         other: 放入set，遍历找最大长度,或者排序再遍历找
 *
 *  L674 : Longest Continuous Increasing Sequence
 *         求解序列中最长子序列
 *         要求：子序列是递增的，子序列元素相邻且递增关系，且子序列序列必须是和原序列一样（即不允许排序）
 *
 *         dp[i] 取决于原序列中前1个元素的状态值
 *         other: 遍历，并维护最大长度
 *
 *  L300 : Longest Increasing Sequence
 *         674 follow up 1: 相邻变不相邻
 *         求解序列中最长子序列
 *         要求：子序列是递增的，子序列元素是递增关系，且子序列必须是和原序列一样
 *
 *         dp[i] 取决于原序列中i之前所有元素中最优的状态值，复杂度为O(n^2)
 *         other: 二分法解决: O(nlogn)
 *                sort + LCS，复杂度为O(n^2)
 *
 *  L398 : Longest Continuous Increasing Sequence II  Matrix
 *         674 follow up 2: 一维数组转为二维矩阵
 *         求解二维矩阵中最长子序列
 *         要求：子序列是递增的，子序列存在于原矩阵中，子序列元素必须是原矩阵中相邻数字
 */



/**
 * 状态转换方程达到了n的平方，无法用滚动数组来优化
 */
class L279_Perfect_Squares {
    public int numSquares(int n) {
        if(n < 2) {
            return n;
        }

        int[] result = new int[n + 1];
        result[1] = 1;
        for(int i = 2; i <= n; ++i) {
            result[i] = i;
            for(int j = 1; j <= (i+1)/2; ++j) {
                if(j * j == i) {
                    result[i] = 1;
                } else {
                    result[i] = Math.min(result[i], result[j] + result[i - j]);
                }
            }
        }

        return result[n];
    }
}


/**
 * 状态转换方程达到了n的平方，无法用滚动数组来优化
 */
class L368_Largest_Divisible_Subset {
    public List<Integer> largestDivisibleSubset(int[] nums) {
        List<Integer> ret = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return ret;
        }
        if(nums.length == 1) {
            ret.add(nums[0]);
            return ret;
        }

        int maxLength = 1, index = 0;
        int[] result = new int[nums.length];
        int[] seq = new int[nums.length];
        for(int i = 0; i < nums.length; ++i) {
            result[i] = 1;
            seq[i] = i;
        }

        Arrays.sort(nums);
        for(int i = 1; i < nums.length; ++i) {
            for(int j = 0; j < i; ++j) {
                if(nums[i]%nums[j] == 0 && result[j] + 1 > result[i]) {
                    result[i] = result[j] + 1;
                    seq[i] = j;
                }
            }
            if(result[i] > maxLength) {
                maxLength = result[i];
                index = i;
            }
        }

        for(int i = index; i >= 0; --i) {
            ret.add(nums[index]);

            if(seq[index] == index) {
                break;
            } else {
                index = seq[index];
            }

        }

        return ret;
    }
}


/**
 * 根据w进行排序后，对h做LIS
 * O(nlogn + n^2)
 * DP中数据转换方程到n平方的时候，就无法用滚动数组优化
 */
class L354_Russian_Doll_Envelopes {
    public int maxEnvelopes(int[][] envelopes) {
        if(envelopes == null || envelopes.length == 0) {
            return 0;
        }
        if(envelopes.length == 1) {
            return 1;
        }

        Comparator<int[]> compl = new Comparator<int[]>(){
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        };

        int[] dp = new int[envelopes.length];
        dp[0] = 1;
        int maxNumber = dp[0];
        Arrays.sort(envelopes, compl);
        for(int i = 1; i < envelopes.length; ++i) {
            dp[i] = 1;
            for(int j = 0; j < i; ++j) {
                if(envelopes[j][0] < envelopes[i][0]
                        && envelopes[j][1] < envelopes[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxNumber = Math.max(maxNumber, dp[i]);
        }

        return maxNumber;
    }
}


class L198_House_Robber {
    public int rob(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }
        if(nums.length == 1) {
            return nums[0];
        }

        int[] dp = new int[2];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for(int i = 2; i < nums.length; ++i) {
            dp[i%2] = Math.max(dp[(i - 1)%2], dp[(i - 2)%2] + nums[i]);
        }

        return dp[(nums.length - 1)%2];
    }
}


class L213_House_Robber_II {
    public int rob(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }
        if(nums.length == 1) {
            return nums[0];
        }
        if(nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }

        return Math.max(rob(nums, 0, nums.length - 1), rob(nums, 1, nums.length));
    }

    int rob(int[] nums, int start, int end) {
        int len = end - start;
        int[] dp = new int[2];
        dp[0] = nums[start];
        dp[1] = Math.max(nums[start + 1], nums[start]);

        for(int i = 2; i < len; ++i) {
            dp[i%2] = Math.max(dp[(i - 1)%2], dp[(i - 2)%2] + nums[start + i]);
        }

        return dp[(len - 1)%2];
    }
}


class L221_Maximal_Square {
    public int maximalSquare(char[][] matrix) {
        if(matrix == null || matrix.length == 0
                || matrix[0] == null || matrix[0].length == 0) {
            return 0;
        }

        int row = matrix.length;
        int col = matrix[0].length;
        int[][] dp = new int[2][col];
        int largestSquare = 0;

        for(int i = 0; i < row; ++i) {
            dp[i%2][0] = (matrix[i][0] == '0') ? 0 : 1;
            largestSquare = Math.max(largestSquare, dp[i%2][0]);

            for(int j = 1; j < col; ++j) {
                if(i == 0) {
                    dp[i%2][j] = (matrix[i][j] == '0') ? 0 : 1;
                    largestSquare = Math.max(largestSquare, dp[i%2][j]);
                    continue;
                }

                if(matrix[i][j] == '0') {
                    dp[i%2][j] = 0;
                } else {
                    dp[i%2][j] = 1 + Math.min(Math.min(dp[(i - 1)%2][j - 1], dp[(i - 1)%2][j]), dp[i%2][j - 1]);
                }
                largestSquare = Math.max(largestSquare, dp[i%2][j]);
            }
        }

        return largestSquare * largestSquare;
    }
}


class L631_Maximal_Square_II {
    public int maxSquare2(int[][] matrix) {
        // write your code here
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int row = matrix.length;
        int col = matrix[0].length;
        int[][] dp = new int[2][col];
        int[][] up = new int[2][col];
        int[][] left = new int[2][col];

        int maxLen = 0;
        for(int i = 0; i < row; ++i) {
            //init dp for first col
            dp[i%2][0] = (matrix[i][0] == 0) ? 0 : 1;
            maxLen = Math.max(maxLen, dp[i%2][0]);

            //init up for first col
            if(i == 0) {
                up[i%2][0] = (matrix[i][0] == 0) ? 1: 0;
            } else {
                up[i%2][0] = (matrix[i][0] == 0) ? up[(i - 1)%2][0] + 1 : 0;
            }

            //init left for first col
            left[i%2][0] = (matrix[i][0] == 0) ? 1 : 0;

            for(int j = 1; j < col; ++j) {
                //init left for the rest cols
                left[i%2][j] = (matrix[i][j] == 0) ? left[i%2][j - 1] + 1 : 0;

                //init up/dp for the rest cols and 1st row
                if(i == 0) {
                    dp[i%2][j] = (matrix[i][j] == 0) ? 0 : 1;
                    maxLen = Math.max(maxLen, dp[i%2][j]);

                    up[i%2][j] = (matrix[i][j] == 0) ? 1: 0;

                    continue;
                }

                //init up/dp for the rest cols and rest rows
                up[i%2][j] = (matrix[i][j] == 0) ? up[(i - 1)%2][j] + 1: 0;
                if(matrix[i][j] == 0) {
                    dp[i%2][j] = 0;
                } else {
                    dp[i%2][j] = 1 + Math.min(dp[(i - 1)%2][j - 1],
                            Math.min(up[(i - 1)%2][j], left[i%2][j - 1]));
                }
                maxLen = Math.max(maxLen, dp[i%2][j]);
            }
        }
        return maxLen * maxLen;
    }
}

class L403_Frog_Jump {
    public boolean canCross(int[] stones) {
        Map<Integer, Map<Integer, Boolean>> map = new HashMap<>();
        for(int stone : stones) {
            map.put(stone, new HashMap<Integer, Boolean>());
        }

        return search(0, 1, stones[stones.length - 1], map);
    }

    boolean search(int pos, int nextStep, int end, Map<Integer, Map<Integer, Boolean>> map) {
        Map<Integer, Boolean> nextSteps = map.get(pos);
        if(nextSteps.containsKey(nextStep)) {
            return nextSteps.get(nextStep);
        }

        int[] dStep = {-1, 0, 1};
        int nextPos = pos + nextStep;
        if(nextPos == end) {
            return true;
        }

        if(nextPos < end && map.containsKey(nextPos)) {
            for(int i = 0; i < 3; ++i) {
                int newNextStep = nextStep + dStep[i];
                if(newNextStep == 0) {
                    continue;
                }

                if(search(pos + nextStep, newNextStep, end, map) == true) {
                    return true;
                }
            }
        }

        nextSteps.put(nextStep, false);
        return false;
    }
}



/**
 * **************************************************************************
 * 以上为：坐标DP，序列DP，以及两种DP如何使用滚动数组来优化
 */

/**
 * **************************************************************************
 * 以下为：记忆化搜索，以及记忆化搜索在：博弈类DP和区间类DP上的使用
 */

/**
 * 记忆化搜索
 */
class LintCode_398_Longest_Continuous_Increasing_Subsequence_II {
    public int longestIncreasingContinuousSubsequenceII(int[][] A) {
        if(A == null || A.length == 0 || A[0].length == 0) {
            return 0;
        }

        int longestSeq = 0;
        int row = A.length, col = A[0].length;
        int[][] dp = new int[row][col];
        int[][] visited = new int[row][col];

        for(int i = 0; i < row; ++i) {
            for(int j = 0; j < col; ++j) {
                longestSeq = Math.max(longestSeq, search(A, i, j, dp,visited));
            }
        }

        return longestSeq;
    }

    int search(int[][]A, int x, int y, int[][]dp, int[][] visited) {
        if(visited[x][y] == 1) {
            return dp[x][y];
        }

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        int len = 1;
        for(int i = 0; i < 4; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(validate(A, nx, ny) && A[x][y] < A[nx][ny]) {
                len = Math.max(len, 1 + search(A, nx, ny, dp, visited));
            }
        }

        dp[x][y] = len;
        visited[x][y] = 1;
        return dp[x][y];
    }

    boolean validate(int[][] A, int x, int y) {
        if(x < 0 || y < 0 || x >= A.length || y >= A[0].length) {
            return false;
        }
        return true;
    }
}



/**
 * ************************ 博弈类DP ******************************
 */

class LintCode_394_Coins_in_a_Line {
    public boolean firstWillWin(int n) {
        // write your code here
        boolean[] dp = new boolean[4];
        if(n == 0){
            return false;
        } else if(n == 1) {
            return true;
        } else if(n == 2) {
            return true;
        } else if(n == 3) {
            return false;
        }

        dp[0] = false;
        dp[1] = true;
        dp[2] = true;
        dp[3] = false;
        for(int i = 4; i <= n; ++i) {
            dp[i%4] = (dp[(i - 2)%4] & dp[(i - 3)%4]) | (dp[(i - 3)%4] & dp[(i - 4)%4]);
        }

        return dp[n%4];
    }
}

class LintCode_394_Coins_in_a_Line_SearchWithMemorization {
    public boolean firstWillWin(int n) {
        int[] dp = new int[n + 1];

        return search(n, dp);
    }

    boolean search(int n, int[] dp) {
        if(dp[n] != 0) {
            return dp[n] == 1 ? true : false;
        }

        if(n == 0) {
            return false;
        } else if(n == 1 || n == 2) {
            return true;
        } else if(n == 3) {
            return false;
        }

        if((search(n - 2, dp) & search(n - 3, dp)) |
                (search(n - 3, dp) & search(n - 4, dp))) {
            dp[n] = 1;
            return true;
        } else {
            dp[n] = 2;
            return false;
        }
    }
}


class LintCode_395_Coins_in_a_Line_II {
    public boolean firstWillWin(int[] values) {
        // write your code here
        if(values == null || values.length == 0) {
            return false;
        } else if(values.length == 1 || values.length == 2) {
            return true;
        } else if(values.length == 3) {
            return values[0] + values[1] > values[2] ? true : false;
        }

        int sum = 0;
        for(int value : values) {
            sum += value;
        }
        int len = values.length;
        int[] dp = new int[len + 1];
        dp[0] = 0;
        dp[1] = values[leftToIndex(len , 1)];
        dp[2] = values[leftToIndex(len, 2)] + values[leftToIndex(len, 2) + 1];
        dp[3] = values[leftToIndex(len, 3)] + values[leftToIndex(len, 3) + 1];

        for(int i = 4; i <= len; ++i) {
            int left = Math.min(dp[i - 2], dp[i - 3])
                    + values[leftToIndex(len, i)];
            int right = Math.min(dp[i - 3], dp[i - 4])
                    + values[leftToIndex(len, i)]
                    + values[leftToIndex(len, i) + 1];
            dp[i] = Math.max(left, right);
        }

        return dp[len] >= sum/2 ? true : false;
    }

    int leftToIndex(int len, int n) {
        return len - n;
    }
}

class LintCode_395_Coins_in_a_Line_II_SearchWithMemorization {
    public boolean firstWillWin(int[] values) {
        if(values == null || values.length == 0) {
            return false;
        } else if (values.length == 1 || values.length == 2) {
            return true;
        } else if(values.length == 3) {
            return values[0] + values[1] >= values[2] ? true : false;
        }

        int total = 0;
        for(int value : values) {
            total += value;
        }

        int[] dp = new int[values.length + 1];
        Arrays.fill(dp, -1);

        int result = search(values, values.length, dp);
        return result >= total/2 ? true : false;
    }

    int search(int[] values, int left, int[] dp) {
        if(dp[left] != -1) {
            return dp[left];
        }
        int len = values.length;
        if(left == 0) {
            return 0;
        } else if(left == 1) {
            return values[leftToIndex(len, 1)];
        } else if(left == 2) {
            return values[leftToIndex(len, 2)] + values[leftToIndex(len, 2) + 1];
        } else if(left == 3) {
            return values[leftToIndex(len, 3)] + values[leftToIndex(len, 3) + 1];
        }

        int leftVal = Math.min(search(values, left - 2, dp), search(values, left - 3, dp))
                + values[leftToIndex(len, left)];
        int rightVal = Math.min(search(values, left - 3, dp), search(values, left - 4, dp))
                + values[leftToIndex(len, left)]
                + values[leftToIndex(len, left) + 1];
        dp[left] = Math.max(leftVal, rightVal);
        return dp[left];
    }

    int leftToIndex(int len, int n) {
        return len - n;
    }
}


/**
 * ************************ 区间类DP ******************************
 */

class LintCode_396_Coins_in_a_Line_III {
    public boolean firstWillWin(int[] values) {
        // write your code here
        if(values == null || values.length == 0) {
            return false;
        } else if(values.length == 1 || values.length == 2) {
            return true;
        }

        int len = values.length;
        int[][] dp = new int[len][len];
        for(int i = 0; i < len; ++i) {
            for(int j = 0; j < len; ++j) {
                dp[i][j] = -1;
            }
        }

        int total = 0;
        for(int i = 0; i < len; ++i) {
            total += values[i];
            dp[i][i] = values[i];
            if(i != len - 1) {
                dp[i][i + 1] = Math.max(values[i], values[i + 1]);
            }
        }

        int result = search(values, 0, len - 1, dp);
        return result >= total/2 ? true : false;
    }

    int search(int[] values, int i, int j, int[][] dp) {
        if(dp[i][j] != -1) {
            return dp[i][j];
        }

        int left = Math.min(search(values, i + 2, j, dp), search(values, i + 1, j - 1, dp))
                + values[i];
        int right = Math.min(search(values, i + 1, j - 1, dp), search(values, i, j - 2, dp))
                + values[j];
        dp[i][j] = Math.max(left, right);
        return dp[i][j];
    }
}


class L877_Stone_Game {
    public boolean stoneGame(int[] piles) {
        if(piles == null || piles.length == 0) {
            return false;
        } else if(piles.length == 1 || piles.length == 2) {
            return true;
        }

        int len = piles.length;
        int[][] dp = new int[len][len];
        int count = 0;

        for(int i = 0; i < len; ++i) {
            for(int j = 0; j < len; ++j) {
                dp[i][j] = -1;
            }
            count += piles[i];
            dp[i][i] = piles[i];
            if(i != len - 1) {
                dp[i][i + 1] = Math.max(piles[i], piles[i + 1]);
            }
        }

        int result = search(piles, 0, len - 1, dp);
        return result > count/2 ? true : false;
    }

    int search(int[] piles, int i, int j, int[][] dp) {
        if(dp[i][j] != -1) {
            return dp[i][j];
        }

        int left = Math.min(search(piles, i + 2, j, dp), search(piles, i + 1, j - 1, dp)) + piles[i];
        int right = Math.min(search(piles, i + 1, j - 1, dp), search(piles, i, j - 2, dp)) + piles[j];
        dp[i][j] = Math.max(left, right);
        return dp[i][j];
    }
}


class L877_Stone_Game_Hacky_Algorithm {
    // 石头的堆数如果是偶数的话，且石头总数为奇数的话
    // 我们可以将总堆数分为奇数堆和偶数堆
    // 必然有一组堆数量相对比较大，先手就可以只取数量大的哪一组的数字
    // 因为先手可以选择选取属于哪一组的石头，
    // 且同时决定了后手不得不选择另一组的石头；
}

class LintCode_476_Stone_Game {
    public int stoneGame(int[] A) {
        // write your code here
        if(A == null || A.length == 0) {
            return 0;
        } else if(A.length == 1) {
            return 0;
        } else if(A.length == 2) {
            return A[0] + A[1];
        }


        int len = A.length;
        int[][] dp = new int[len][len];
        int[] prefixSum = new int[len + 1];
        for(int i = 0; i < len; ++i) {
            for(int j = 0; j < len; ++j) {
                dp[i][j] = -1;
            }
            prefixSum[i + 1] = prefixSum[i] + A[i];
            dp[i][i] = 0;
        }

        int result = search(A, 0, len - 1, prefixSum, dp);
        return result;
    }

    int search(int[] A, int i, int j, int[] prefixSum, int[][] dp){
        if(dp[i][j] != -1) {
            return dp[i][j];
        }

        int sum = prefixSum[j + 1] - prefixSum[i];
        int minStones = Integer.MAX_VALUE;
        for(int k = i; k < j; ++k) {
            minStones = Math.min(minStones, search(A, i, k, prefixSum, dp) +
                    search(A, k + 1, j, prefixSum, dp));
        }

        dp[i][j] = minStones + sum;
        return dp[i][j];
    }
}


/**
 * 与Stone Game I的区别就是，这里头尾可能合成一个堆，
 * 属于循环数组问题，采用复制的方法，double原来的数组
 *
 * 功能函数search和原来一样，主函数多一个循环，
 * 在前半部分的数据内遍历，数据长度的窗口大小固定；
 */
class LintCode_593_Stone_Game_II {
    public int stoneGame2(int[] A) {
        // write your code here
        if(A == null || A.length == 0 || A.length == 1) {
            return 0;
        } else if(A.length == 2) {
            return A[0] + A[1];
        }

        int windowSize = A.length;
        int len = 2 * windowSize;
        int[] newArray = new int[len];
        for(int i = 0; i < len; ++i) {
            newArray[i] = A[i%windowSize];
        }

        int[][] dp = new int[len][len];
        int[] prefixSum = new int[len + 1];
        for(int i = 0; i < len; ++i) {
            for(int j = 0; j < len; ++j) {
                dp[i][j] = -1;
            }
            dp[i][i] = 0;
            prefixSum[i + 1] = prefixSum[i] + newArray[i];
        }

        int minStones = Integer.MAX_VALUE;
        for(int i = 0; i < windowSize; ++i) {
            minStones = Math.min(minStones, search(i, i + windowSize - 1, prefixSum, dp));
        }

        return minStones;
    }

    int search(int i, int j, int[] prefixSum, int[][] dp) {
        if(dp[i][j] != -1) {
            return dp[i][j];
        }

        int minStones = Integer.MAX_VALUE;
        for(int k = i; k < j; ++k) {
            minStones = Math.min(minStones, search(i, k, prefixSum, dp)
                    + search(k + 1, j, prefixSum, dp));
        }

        //add current sum
        minStones += prefixSum[j + 1] - prefixSum[i];

        dp[i][j] = minStones;
        return dp[i][j];
    }
}



class L312_Burst_Balloons {
    public int maxCoins(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        } else if(nums.length == 1) {
            return nums[0];
        }

        int[] array = new int[nums.length + 2];
        array[0] = 1;
        array[array.length - 1] = 1;
        for(int i = 1; i < array.length - 1; ++i) {
            array[i] = nums[i - 1];
        }

        int[][] dp = new int[array.length][array.length];
        for(int i = 0; i < array.length; ++i) {
            for(int j = 0; j < array.length; ++j) {
                dp[i][j] = -1;
            }
            // 这个if没有也ok，因为即使没有初始化这部分
            // search函数中对 [i,i]也能争取处理并生成对应值

            // 这个状态转换函数可以不需要初始化的原因在于：
            // 状态转换函数中不仅依赖了子问题的dp值，
            // 也依赖于原数据集中的元素；
            if(i != 0 && i != array.length - 1) {
                dp[i][i] = array[i] * array[i - 1] * array[i + 1];
            }
        }

        return search(array, 1, array.length - 2, dp);
    }

    int search(int[] array, int i, int j, int[][] dp) {
        if(i > j) {
            return 0;
        }

        if(dp[i][j] != -1) {
            return dp[i][j];
        }

        int maxCoins = Integer.MIN_VALUE;
        for(int k = i; k <= j; ++k) {
            maxCoins = Math.max(maxCoins, array[k] * array[i - 1] * array[j + 1]    //这部分保证了即使没有初始化[i,i]也能正确生成对应的dp值
                    + search(array, i, k - 1, dp)
                    + search(array, k + 1, j, dp));
        }
        dp[i][j] = maxCoins;
        return dp[i][j];
    }
}

/**
 * 1.String.substring的参数用错；
 *
 * 2.记忆化搜索应用在字符串上时，和数组一样，数据源不变，移动相对的索引；
 * 但是错误的使用相对应的子串代入到DFS函数中，而索引确实全局的；
 *
 * 3.当修改了错误2后，简单的做了删除相应的code，以为解决了，犯了经常犯的错误：
 * 找到一个错误，轻易的修改以为能解决，但是带来了新的错误；
 * 应该重新审视整个代码逻辑，查看是否正确合理，而不是简单粗暴的修改了相应的地方就完事；
 */
class L87_Scramble_String {
    public boolean isScramble(String s1, String s2) {
        if(s1 == null || s2 == null || !validate(s1, s2)) {
            return false;
        }

        if(s1.equalsIgnoreCase(s2)) {
            return true;
        }

        int len = s1.length();
        int[][][] dp = new int[len][len][len + 1];

        return checkScramble(s1, s2, 0, 0, len, dp);
    }

    boolean checkScramble(String s1, String s2, int i, int j, int k, int [][][] dp) {
        if(dp[i][j][k] != 0) {
            return dp[i][j][k] == 1 ? true : false;
        }

        String subs1 = s1.substring(i, i + k);
        String subs2 = s2. substring(j, j + k);


        if(!validate(subs1, subs2)) {
            dp[i][j][k] = -1;
            return false;
        }

        if(subs1.equalsIgnoreCase(subs2)) {
            dp[i][j][k] = 1;
            return true;
        }

        boolean result = false;
        for(int len = 1; len < k; ++len) {
            // s1.start vs s2.start && s1.end vs s2.end
            boolean case1 = checkScramble(s1, s2, i, j, len, dp)
                    && checkScramble(s1, s2, i + len, j + len, k - len, dp);
            if(case1 == true) {
                result = true;
                break;
            }

            // s1.start vs s2.end   && s1.end vs s2.start
            boolean case2 = checkScramble(s1, s2, i, j + k - len, len, dp)
                    && checkScramble(s1, s2, i + len, j, k - len, dp);

            if(case2 == true) {
                result = true;
                break;
            }
        }

        dp[i][j][k] = (result == true) ? 1 : -1;
        return result;
    }

    boolean validate(String s1, String s2) {
        if(s1.length() != s2.length()) {
            return false;
        }

        char[] array1 = s1.toCharArray();
        char[] array2 = s2.toCharArray();
        Arrays.sort(array1);
        Arrays.sort(array2);
        for(int i = 0; i < array1.length; ++i) {
            if(array1[i] != array2[i]) {
                return false;
            }
        }

        return true;
    }
}

class L72_Edit_Distance {
    public int minDistance(String word1, String word2) {
        if(word1 == null || word2 == null) {
            return 0;
        }

        int row = word1.length() + 1;
        int col = word2.length() + 1;
        int[][] dp = new int[2][col];

        for(int i = 0; i < row; ++i) {
            dp[i%2][0] = i;
            for(int j = 1; j < col; ++j) {
                if(i == 0) {
                    dp[i%2][j] = j;
                    continue;
                }

                int add = dp[i%2][j - 1] + 1;
                int remove = dp[(i - 1)%2][j] + 1;
                int change = word1.charAt(i - 1) == word2.charAt(j - 1) ? dp[(i - 1)%2][j - 1] : dp[(i - 1)%2][j - 1] + 1;
                dp[i%2][j] = Math.min(add, Math.min(remove, change));
            }
        }

        return dp[(row - 1)%2][col - 1];
    }
}

/**
 *  LCS 问题
 *  给定两个序列：
 *      a1,a2,a3,a4,a5,a6......
 *      b1,b2,b3,b4,b5,b6.......
 *
 *  要求这样的序列使得c同时是这两个序列中的部分（不要求连续），
 *  这个就叫做公共子序列，
 *  然后最长公共子序列自然就是所有的子序列中最长的啦
 *
 */
class LintCode_77_Longest_Common_Subsequence {
    public int longestCommonSubsequence(String A, String B) {
        // write your code here
        if(A == null || B == null) {
            return 0;
        }

        char[] s1 = A.toCharArray();
        char[] s2 = B.toCharArray();
        int row = s1.length + 1;
        int col = s2.length + 1;
        int[][] dp = new int[2][col];

        for(int i = 1; i < row; ++i) {
            for(int j = 1; j < col; ++j) {
                if(s1[i - 1] == s2[j - 1]) {
                    dp[i%2][j] = dp[(i - 1)%2][j - 1] + 1;
                } else {
                    dp[i%2][j] = Math.max(dp[(i - 1)%2][j], dp[i%2][j - 1]);
                }
            }
        }

        return dp[(row - 1)%2][col - 1];
    }
}

class L115_Distinct_Subsequences {
    public int numDistinct_DP1(String s, String t) {
        if(s == null || t == null || (s.length() == 0 && t.length() != 0)) {
            return 0;
        }
        if(t.length() == 0) {
            return 1;
        }

        char[] src = s.toCharArray();
        char[] dst = t.toCharArray();
        int row = src.length + 1;
        int col = dst.length + 1;
        int[][] dp = new int[2][col];

        for(int i = 0; i < row; ++i) {
            dp[i%2][0] = 1;
            for(int j = 1; j < col; ++j) {
                if(i == 0) {
                    dp[0][j] = 0;
                    continue;
                }

                if(src[i - 1] == dst[j - 1]) {
                    dp[i%2][j] = dp[(i - 1)%2][j - 1] + dp[(i - 1)%2][j];
                } else {
                    dp[i%2][j] = dp[(i - 1)%2][j];
                }
            }
        }

        return dp[(row - 1)%2][col - 1];
    }
}

class L115_Distinct_Subsequences_SearchWithMemorization {
    public int numDistinct(String s, String t) {
        if(s == null || t == null || s.length() == 0 && t.length() != 0) {
            return 0;
        } else if (t.length() == 0) {
            return 1;
        }

        char[] src = s.toCharArray();
        char[] dst = t.toCharArray();
        int row = src.length;
        int col = dst.length;
        int[][] dp = new int[row][col];

        for(int i = 0; i < row; ++i) {
            for(int j = 0; j < col; ++j) {
                dp[i][j] = -1;
            }
        }

        return search(src, dst, 0, 0, dp);
    }

    int search(char[] src, char[] dst, int srcIndex, int dstIndex, int[][] dp) {
        if(dstIndex == dst.length) {
            return 1;
        }
        if(srcIndex == src.length) {
            return 0;
        }

        if(dp[srcIndex][dstIndex] != -1) {
            return dp[srcIndex][dstIndex];
        }

        int count = 0;
        for(int i = srcIndex; i < src.length; ++i) {
            if(src[i] == dst[dstIndex]) {
                count += search(src, dst, i + 1, dstIndex + 1, dp);
            }
        }

        dp[srcIndex][dstIndex] = count;
        return dp[srcIndex][dstIndex];
    }
}


/**
 * 还有 DFS BFS的解法
 */
class L97_Interleaving_String {
    public boolean isInterleave(String s1, String s2, String s3) {
        if(s1 == null || s2 == null || s3 == null || s1.length() + s2.length() != s3.length()) {
            return false;
        }

        char[] array1 = s1.toCharArray();
        char[] array2 = s2.toCharArray();
        char[] array3 = s3.toCharArray();
        int row = array1.length + 1;
        int col = array2.length + 1;
        int[][] dp = new int[2][col];

        for(int i = 0; i < row; ++i) {

            dp[i%2][0] = (i == 0) ? 1: (dp[(i - 1)%2][0] == 1 && array1[i - 1] == array3[i - 1]) ? 1 : 0;

            for(int j = 1; j < col; ++j) {
                if(i == 0) {
                    dp[0][j] = (dp[0][j - 1] == 1 && array2[j - 1] == array3[j - 1]) ? 1 : 0;
                    continue;
                }

                if((dp[(i - 1)%2][j] == 1 && array1[i - 1] == array3[i + j - 1])
                        || (dp[i%2][j - 1] == 1 && array2[j - 1] == array3[i + j - 1])) {
                    dp[i%2][j] = 1;
                } else {
                    dp[i%2][j] = 0;
                }
            }
        }


        if(dp[(row - 1)%2][col - 1] == 1) {
            return true;
        }

        return false;
    }
}

class LintCode_92_Backpack {
    public int backPack(int m, int[] A) {
        // write your code here
        if(A == null || A.length == 0) {
            return 0;
        }

        int row = A.length + 1;
        int col = m + 1;
        int[][] dp = new int[2][col];

        for(int i = 0; i < row; ++i) {
            dp[i%2][0] = 1;
            for(int j = 1; j < col; ++j) {
                if(i == 0) {
                    dp[0][j] = 0;
                    continue;
                }

                int pickThis = (j - A[i - 1] >= 0) ? dp[(i - 1)%2][j - A[i - 1]] | dp[(i - 1)%2][j] : 0;
                int skipThis = dp[(i - 1)%2][j];
                dp[i%2][j] = pickThis | skipThis;
            }
        }

        for(int i = m; i >= 0; --i) {
            if(dp[(row - 1)%2][i] == 1) {
                return i;
            }
        }

        return 0;
    }
}

class LintCode_92_BackPack_OptimizedByArray {
    public int backPack(int m, int[] A) {
        if(A == null || A.length == 0 || m == 0) {
            return 0;
        }

        int row = A.length + 1;
        int col = m + 1;
        int[] dp = new int[col];
        dp[0] = 1;

        for(int i = 1; i < row; ++i) {
            int cost = A[i -1];
            for(int j = col - 1; j >= cost; --j) {
                dp[j] = dp[j] | dp[j - cost];
            }
        }

        for(int i = col - 1; i >= 0; --i) {
            if(dp[i] != 0) {
                return i;
            }
        }
        return 0;
    }
}

class LintCode_562_Backpack_IV {
    public int backPackIV(int[] nums, int target) {
        // write your code here
        if(nums == null || nums.length == 0 || target == 0) {
            return 0;
        }

        int row = nums.length + 1;
        int col = target + 1;
        int[] dp = new int[col];
        dp[0] = 1;

        for(int i = 1; i < row; ++i) {
            int cost = nums[i - 1];
            for(int j = cost; j < col; ++j) {
                dp[j] = dp[j] + dp[j - cost];
            }
        }

        return dp[col - 1];
    }
}

class LintCode_563_Backpack_V {
    public int backPackV(int[] nums, int target) {
        // write your code here
        if(nums == null || nums.length == 0 || target == 0) {
            return 0;
        }

        int row = nums.length + 1;
        int col = target + 1;
        int[] dp = new int[col];
        dp[0] = 1;

        for(int i = 1; i < row; ++i) {
            int cost = nums[i - 1];
            for(int j = col - 1; j >= cost; --j) {
                dp[j] = dp[j] + dp[j - cost];
            }
        }

        return dp[col - 1];
    }
}


/**
 *  0/1 Knapsack
 */
class LintCode_125_Backpack_II {
    public int backPackII(int m, int[] A, int[] V) {
        // write your code here
        if(A == null || V == null || A.length == 0 || V.length == 0 || A.length != V.length || m == 0) {
            return 0;
        }

        int row = A.length + 1;
        int col = m + 1;
        int[][] dp = new int[2][col];   // Can be optimized more

        for(int i = 0; i < row; ++i) {
            dp[i%2][0] = 0;
            for(int j = 1; j < col; ++j) {
                if(i == 0) {
                    dp[0][j] = 0;
                    continue;
                }

                int pickThis = (j - A[i - 1] >= 0) ? dp[(i - 1)%2][j - A[i - 1]] + V[i - 1] : 0;
                int skipThis = dp[(i - 1)%2][j];
                dp[i%2][j] = Math.max(pickThis, skipThis);
            }
        }

        int max = 0;
        for(int i = m; i >= 0; --i) {
            max = Math.max(max, dp[(row - 1)%2][i]);
        }
        return max;
    }
}

class LintCode_125_Backpack_II_OptimizedByArray {
    public int backPackII(int m, int[] A, int[] V) {
        // write your code here
        if(A == null || V == null || A.length == 0 || V.length == 0 || A.length != V.length || m == 0) {
            return 0;
        }

        int row = A.length + 1;
        int col = m + 1;
        int[] dp = new int[col];

        for(int i = 1; i < row; ++i) {
            int cost = A[i - 1];
            int price = V[i - 1];
            for(int j = col - 1; j >= cost; --j) {
                dp[j] = Math.max(dp[j], dp[j - cost] + price);
            }
        }

        int max = 0;
        for(int i = 0; i < col; ++i) {
            max = Math.max(max, dp[i]);
        }
        return max;
    }
}

/**
 *  Outbounded Knapsack
 *
 *  如果要求从：背包能装下的情况下，求最高价值
 *  改为： 刚好装满背包的情况下，求最高价值
 *
 *  则初始化从： 第一行第一列都初始化为0，即优化方案中数组全赋值为0；
 *  改为：      第一列为0，第一行除去第一个之外都为无效值 dp[0] = 0, others = -1，
 *             状态转方程中只有当对应值不为-1时才有意义
 *
 *  同理0/1 Knapsack 也是一样的情况；
 *
 *
 *  01背包（0/1 Knapsack）和完全背包（Outbounded Knapsack）
 *  都是带有价值的背包，求解在一定的成本/体积下，求解放入物品价值最优问题
 *
 *  BackPack：    普通，无价值背包，求解一定的成本/体积下，求解可能放入物品个数最多；
 *  BackPack IV： 普通，无价值背包，求解一定的成本/体积下，求解填满背包，最多有几种可能，可多次提取一个物品；
 *  BackPack V：  普通，无价值背包，求解一定的陈本/体积下，求解填满背包，最多有几种可能，一个物品只能提取一次；
 */
class LintCode_440_Backpack_III {
    public int backPackIII(int[] A, int[] V, int m) {
        // write your code here
        if(A == null || V == null || A.length != V.length || m == 0) {
            return 0;
        }

        int row = A.length + 1;
        int col = m + 1;
        int[] dp = new int[col];

        for(int i = 1; i < row; ++i) {
            int cost = A[i - 1];
            int price = V[i - 1];
            for(int j = cost; j < col; ++j) {
                dp[j] = Math.max(dp[j], dp[j - cost] + price);
            }
        }

        int ret = 0;
        for(int i = 1; i < col; ++i) {
            ret = Math.max(ret, dp[i]);
        }
        return ret;
    }
}


/**
 *  初始状态： dp[i][j][k]， 前i个数字取出j个组成和为k的方案个数
 *  转换方程： dp[i][j][k] = dp[i-1][j-1][k - A[i-1]] + dp[i-1][j][k]
 *                             选                    +  不选
 *  可见虽然是三维，但是在第一位维上都依赖于上一次状态，故可以优化掉第一维，
 *  dp[j][k] = dp[j-1][k - A[i-1]] + dp[j][k]
 *
 *  初始化： 第0行第0列上的头一个元素dp[0][0]初始化为1，其他都初始化为0
 */
class LintCode_89_K_Sum {
    public int kSum(int[] A, int k, int target) {
        // write your code here
        if(A == null || A.length == 0 || k <= 0 || target <= 0) {
            return 0;
        }

        int[][] dp  = new int[k + 1][target + 1];
        dp[0][0] = 1;

        for(int num = 1; num < A.length + 1; ++num) {
            int cost = A[num - 1];
            for(int i = k; i >= 1; --i) {
                for(int j = target; j >= cost; --j) {
                    dp[i][j] = dp[i][j] + dp[i - 1][j - cost];
                }
            }
        }

        return dp[k][target];
    }
}


class L887_Super_Egg_Drop {
    int superEggDrop(int K, int N) {
        int[] dp = new int[K+1];
        int step = 0;
        while(dp[K] < N) {
            for(int i = K; i > 0; --i) {
                dp[i] += dp[i - 1] + 1;
            }
            step++;
        }

        return step;
    }
}


