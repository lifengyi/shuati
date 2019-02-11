package dropbox;

import java.util.*;

/**
 * Let's assume sodas are sold in packages of 1, 2, 6, 12, 24.
 * e.g. if N = 10, you could buy 1 x 10,  6 + 2 + 2, 6 + 1 + 1 + 1 + 1, ....
 *  input : N
 *  output : all possible combinations,
 *  for instance N=10 { {1,1,...,1}, {6,2,2}, {6,1,1,1,1}, {6,2,1,1}, {2,1,..,1}, .... }
 *  (don't include {2,2,6} since we have {6,2,2})
 */

public class findSodaCombination {
    /**
     * 基本上可以看作为多重背包，即 Outbounded Knapsack
     * 但属于无价值背包，所以比较简单
     *
     * 首先分析穷举法，由于是多重背包，即求排列，假设soda售卖的包装
     * 有K种，那么总共能有 k! 个可能解，每个解需要k操作时间来求出，
     * 即总共的时间复杂度是： k * k!
     *
     * 优化：构建dp表，横坐标是 0 -> K, 纵坐标是 0 -> N
     * 求解整个dp表的复杂度是 n * k, 然后基于dp表，总右下角
     * 开始DFS遍历，求解结果，DFS的最坏复杂度也就是 n * k
     *
     */

    public static void main(String[] args) {
        //int[] packages = {1, 2, 6, 12, 64};
        //int n = 10;
        int[] packages = {4, 6};
        int n = 6;

        findSodaCombination proc = new findSodaCombination();
        List<List<Integer>> combinations = proc.findSodaComb(packages, n);
        for(List<Integer> comb : combinations) {
            System.out.println(comb.toString());
        }
    }

    List<List<Integer>> findSodaComb(int[] packages, int n) {
        int row = packages.length;
        int col = n;
        int[][] dp = new int[row + 1][col + 1];
        populate(packages, dp);

        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cache = new ArrayList<>();
        dfs(packages, dp, row, col, cache, result);
        return result;
    }

    void dfs(int[] packages, int[][] matrix, int x, int y, List<Integer> cache, List<List<Integer>> result) {
        //no available combination
        if(matrix[x][y] == 0) {
            return;
        }

        // reach to the most left
        if((y == 0 || x == 0) && cache.size() != 0) {
            result.add(new ArrayList(cache));
            return;
        }

        //pick the current package
        if(y >= packages[x - 1]) {
            cache.add(packages[x - 1]);
            dfs(packages, matrix, x, y - packages[x - 1], cache, result);
            cache.remove(cache.size() - 1);
        }

        //don't pick the current package
        dfs(packages, matrix, x - 1, y, cache, result);
    }


    void populate(int[] packages, int[][] dp) {
        int row = dp.length;
        int col = dp[0].length;

        for(int i = 0; i < row; ++i) {
            dp[i][0] = 1;
        }

        for(int i = 1; i < row; ++i) {
            for(int j = 1; j < col; ++j) {
                if(j >= packages[i - 1]) {
                    dp[i][j] += dp[i][j - packages[i - 1]];
                }
                dp[i][j] += dp[i - 1][j];
            }

            //System.out.println(Arrays.toString(dp[i]));
        }
    }
}
