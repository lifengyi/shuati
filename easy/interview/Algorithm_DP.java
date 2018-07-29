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




