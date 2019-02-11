package dropbox;

/**
 *
 */
public class findHighestMinimumSharpness {
    /**
     * Given a 2-d array of "sharpness" values. Find a path from the leftmost column
     * to the rightmost column which has the highest minimum sharpness.
     *
     * Output the highest minimum sharpness. Each move can only move to the top right,
     * right or bottom right grid
     *
     * Example: 3*3 matrix
     *  5 7 2
     *  7 5 8
     *  9 1 5
     *
     *  The path with highest minimum sharpness is 7-->7-->8, because 7 is the highest
     *  minimum value in all the paths.
     *
     *  Idea: Use DP:
     *  dp[r][c] = min(max(dp[r-1][c-1], dp[r][c-1], dp[r+1][c-1]), grid[r][c])
     */

    /**
     * 如果文件很大，1m * 1m，怎么处理？
     * 如果按照原来方法处理，文件read指针会移动频繁，导致效率低下
     *
     * 可以翻转矩阵90度，然后按行读，然后用dp处理；
     * 翻转算法： row * col
     *     position[i][j] => np[j][row - i - 1]
     *
     * 但是如何翻转90度？如果按行读取，然后写到列上，这样write指针又会频繁切换
     *
     * 可以折中，每次翻转一个小的正方形：
     *  N * N matrix
     *  read a row and write to a col:  read : N disk seeks,  write: N * N disk seeks
     *  read a col and write to a row:  read : N * N disk seeks, write: N disk seeks
     *  if we use k(k < N) to read a sub matrix
     *  read : K disk seeks , write: K disk seeks
     *  we have # of sub matrix : N * N/ K * K
     *  total read: N * N / K
     *  total write: N * N / K
     */
}
