package dropbox;

import java.util.*;

public class sharpnessRelated {

    public static void main(String[] args) {
        int[][] array = {
                {5, 7, 2},
                {7, 5, 8},
                {9, 1, 5}
        };

        sharpnessRelated proc = new sharpnessRelated();

        System.out.println(proc.findHighestMinimumSharpness(array));

        int[][] array2 = {
                { 1,  2,  3,  4,  5,  6,  7},
                { 8,  9, 10, 11, 12, 13, 14},
                {15, 16, 17, 18, 19, 20, 21}
        };

        int row = array2.length;
        int col = array2[0].length;
        int[][] newArray = new int[col][row];


        proc.rotateMatrix(array2, newArray);
        for(int i = 0; i < newArray.length; ++i) {
            System.out.println(Arrays.toString(newArray[i]));
        }
    }



    int[] dx = {-1,  0,  1};
    int[] dy = {-1, -1, -1};


    /**
     * 输入一个 2-D array，比如说是：
     * 4    5    4    0    1    2
     * 5    6    5    0    0    0
     * 0    0    0    0    0    0
     * 1    3    1    2    3    3
     * 3    0    3    0    0    0
     *
     * 从最左一列中的任意一个出发，到达最右一列的任意一个，要求：
     * 1）当前格子要想往右走，只能往右上、右边、右下三个格子移动；
     * 2）一条path中最小的那个值才是这条path的合格value；
     * 3）在所有path中找到合格value最大的那一条，输出它的value
     *
     * dp[i][j] = min(max(dp[i-1][j-1], dp[i][j-1], dp[i+1][j-1]), grid[i][j])
     *
     *
     * 还有一种题法：淹没路径，正好和上述相反
     * 给一个二维矩阵，每个cell的数字代表海拔，只能从左向右走，问在valid的路径中可以淹没整条路径的最小value是多少
     * 比如说
     *   3 1 2 4
     *   5 7 1 8
     *   3 9 2 6
     *
     *  dp[i][j] = Max(min(dp[i-1][j-1], dp[i][j-1], dp[i+1][j-1]), matrix[i][j])
     */
    int findHighestMinimumSharpness(int[][] array) {
        int row = array.length;
        int col = array[0].length;
        int[][] dp = new int[row][col];
        for(int i = 0; i < row; ++i) {
            dp[i][0] = array[i][0];
        }

        for(int i = 0; i < row; ++i) {
            for(int j = 1; j < col; ++j) {
                int prevValue = Integer.MIN_VALUE;
                for(int k = 0; k < 3; ++k) {
                    int nx = i + dx[k];
                    int ny = j + dy[k];
                    if(nx >= 0 && nx < row && ny >= 0 && ny < col) {
                        prevValue = Math.max(prevValue, dp[nx][ny]);
                    }
                }
                dp[i][j] = Math.min(prevValue, array[i][j]);
            }
        }
        int result = 0;
        for(int i = 0; i < row; ++i) {
            result = Math.max(result, dp[i][col - 1]);
        }
        return result;
    }

    public void rotateMatrix(int[][] matrix, int[][] newMatrix) {
        int k = 2;
        List<List<Integer>> coordinates = cutMatrix(matrix.length, matrix[0].length, k);
        for(List<Integer> coordinate : coordinates) {
            rotate(matrix, coordinate, newMatrix);
        }
    }

    List<List<Integer>> cutMatrix(int row, int col, int k) {
        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0; i < row; i += k) {
            for(int j = 0; j < col; j += k) {
                int iEnd = Math.min(i + k, row);
                int jEnd = Math.min(j + k, col);
                List<Integer> element = new ArrayList<>();
                element.add(i);
                element.add(iEnd);
                element.add(j);
                element.add(jEnd);
                result.add(element);
            }
        }

        return result;
    }

    void rotate(int[][] matrix, List<Integer> coordinate, int[][] newMatrix) {
        int xStart = coordinate.get(0);
        int xEnd = coordinate.get(1);
        int yStart = coordinate.get(2);
        int yEnd = coordinate.get(3);
        int row = matrix.length;
        for(int i = xStart; i < xEnd; ++i) {
            for(int j = yStart; j < yEnd; ++j) {
                newMatrix[j][row - i - 1] = matrix[i][j];
            }
        }
    }
}
