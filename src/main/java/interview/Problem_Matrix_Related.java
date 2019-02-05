package interview;

import java.util.ArrayList;
import java.util.List;

public class Problem_Matrix_Related {

}

class L54_Spiral_Matrix {
    public List<Integer> L54_spiralOrder(int[][] matrix) {
        List<Integer> ret = new ArrayList<>();

        if(matrix.length == 0 || matrix[0].length == 0)
            return ret;

        int rowBegin = 0;
        int rowEnd = matrix.length - 1;
        int colBegin = 0;
        int colEnd = matrix[0].length - 1;

        while(rowBegin <= rowEnd && colBegin <= colEnd) {
            for(int i = colBegin; i <= colEnd; i++)
                ret.add(matrix[rowBegin][i]);
            if(++rowBegin > rowEnd)
                break;

            for(int i = rowBegin; i <= rowEnd; i++)
                ret.add(matrix[i][colEnd]);
            if(--colEnd < colBegin)
                break;

            for(int i = colEnd; i >= colBegin; i--)
                ret.add(matrix[rowEnd][i]);
            rowEnd--;

            for(int i = rowEnd; i >= rowBegin; i--){
                ret.add(matrix[i][colBegin]);
            }
            colBegin++;
        }

        return ret;
    }
}


class L48_Rotate_Image {

    public void L48_rotate(int[][] matrix) {
        //transpose the matrix
        int tmp;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = i + 1; j < matrix.length; j++) {
                tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        //if clockwise, then reverse left to right
        //if anticlockwise, then reverse up to down
        for(int i = 0; i < matrix.length; i++) {
            int left = 0;
            int right = matrix.length - 1;
            while(left < right) {
                tmp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = tmp;
                left++;
                right--;
            }
        }
    }

    public void L48_rotate_v2(int[][] matrix) {

        rotateMatrix(matrix, 0, matrix.length - 1);
    }

    private void rotateMatrix(int[][] matrix, int begin, int end) {
        if(begin >= end)
            return;

        int cacheValue, x, y, tmp;
        for(int i = begin; i < end; i++){
            x = begin;
            y = i;
            cacheValue = matrix[x][y];
            for(int counter = 0; counter < 4; counter++){
                cacheValue ^= matrix[y][begin + end - x];
                matrix[y][begin + end - x] ^= cacheValue;
                cacheValue ^= matrix[y][begin + end - x];
                tmp = x;
                x = y;
                y = begin + end - tmp;
            }
        }

        rotateMatrix(matrix, begin+1, end-1);
    }
}


/**
 * 1. 对于如何减少数据的读取次数， v3， 减少了内存的读取次数，每次访问一个邻居节点，只需要计算3个节点
 *    但是需要额外的存储空间
 *
 * 2. In-Place如何实现少读取？ v2 对每个节点遍历时，使用方向数组便利邻居节点，计算邻居的存活情况，
 *    同时对于节点的中间状态存储，由于只存在0和1，那么我们可以用第二个bit来作为最终值的存储，默认都为0，只有最终为1的，
 *    根据邻居的存活情况，决定当前节点的取值，如果需要设定为存活，即设置为1，则将当前值进行或操作 |= 0x10;
 *    遍历完矩阵之后，再最后遍历一次，将所有矩阵节点的值进行右移1位操作；
 *
 *
 * 扩展1：
 * 假如矩阵是一个循环矩阵，怎么处理，例如3*3的矩阵，a[0][0]的左边是a[0][2], 上边是a[2][0]
 * 对索引进行取模操作
 *
 *
 * 扩展2：
 * 我们可以只记录存活节点的信息，存入一个live的list中，这里active代表着存活节点，或者存活节点的邻居。
 * 每次只计算这个list中节点和其邻居的情况。进一步优化的话，我们可以用一个active的list，只记录上次更
 * 新的节点，或者该节点的邻居。等计算完这个列表后，将产生更新的节点和它的邻居们存入一个新列表中，再用这
 * 个新列表里节点的值来更新矩阵。下一轮时，就计算这个新列表，再产生一个新列表。
 *
 * 扩展3：
 * 多核机器上的优化，在扩展2的基础上，将矩阵进行切割，例如切成4块，每个线程处理子块，主线程处理4个字块
 * 边缘部分的节点，最终生成一个需要修改状态的节点的list，用这个list生成新的矩阵
 *
 * 扩展4：
 * 多机上的优化，讲扩展3的多线程改成分割矩阵后分发到不同的机器分别计算，然后再将每个机器上生成的哪些几点
 * 需要改变的list收集起来到主机，同时主机也要处理边缘节点的计算
 */

class L289_Game_of_Life {
    public void L289_gameOfLife_v3(int[][] board) {
        if(board.length == 0 || (board.length == 1 && board[0].length == 0))
            return;

        int[] lookupBuffer = new int[512];
        for(int i = 0; i < 512; i++) {
            int bitCounter = Integer.bitCount(i);
            if(bitCounter == 3 || (bitCounter == 4 && (i & 16) == 16))
                lookupBuffer[i] = 1;
        }

        int[][] buffer = new int[board.length][board[0].length];
        for(int i = 0; i < board.length; i++) {
            int env = (i-1 >= 0 && board[i-1][0] == 1 ? 4:0)
                    + (board[i][0] == 1 ? 2:0)
                    + (i+1 < board.length && board[i+1][0] == 1 ? 1:0);
            for(int j = 0; j < board[i].length; j++) {
                env = (env % 64) * 8 + (i-1 >= 0 && j+1 < board[i].length && board[i-1][j+1] == 1 ? 4:0)
                        + (j+1 < board[i].length && board[i][j+1] == 1 ? 2:0)
                        + (i+1 < board.length && j+1 < board[i].length && board[i+1][j+1] == 1 ? 1:0);
                buffer[i][j] = lookupBuffer[env];
            }
        }
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = buffer[i][j];
            }
        }
    }

    public void L289_gameOfLife_v2(int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                int neighbours = getNeighbours(board, i, j);
                if(board[i][j] == 1 && (neighbours == 2 || neighbours == 3)) {
                    board[i][j] |= 2;
                } else if(board[i][j] == 0 && neighbours == 3) {
                    board[i][j] |= 2;
                }
            }
        }
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = board[i][j] >> 1;
            }
        }
    }

    private int getNeighbours(int[][]board, int p, int q) {
        int counter = 0;
        for(int i = Math.max(p-1, 0); i <= Math.min(p+1, board.length-1); i++){
            for(int j = Math.max(q-1, 0); j <= Math.min(q+1, board[0].length-1); j++){
                if(i==p && j==q)
                    continue;
                if((board[i][j] & 1) == 1)
                    counter++;
            }
        }
        return counter;
    }

    public void L289_gameOfLife(int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                int liveCounter = 0;
                if(i-1 >= 0 && j-1 >= 0 && (board[i-1][j-1] == 1 || board[i-1][j-1] == 2))
                    liveCounter++;
                if(i-1 >= 0 && (board[i-1][j] == 1 || board[i-1][j] == 2))
                    liveCounter++;
                if(i-1 >= 0 && j+1 < board[i].length && (board[i-1][j+1] == 1 || board[i-1][j+1] == 2))
                    liveCounter++;
                if(j-1 >= 0 && (board[i][j-1] == 1 || board[i][j-1] == 2))
                    liveCounter++;
                if(j+1 < board[i].length && (board[i][j+1] == 1 || board[i][j+1] == 2))
                    liveCounter++;
                if(i+1 < board.length && j-1 >= 0 && (board[i+1][j-1] == 1 || board[i+1][j-1] == 2))
                    liveCounter++;
                if(i+1 < board.length && (board[i+1][j] == 1 || board[i+1][j] == 2))
                    liveCounter++;
                if(i+1 < board.length && j+1 < board[i].length && (board[i+1][j+1] == 1 || board[i+1][j+1] == 2))
                    liveCounter++;

                if(board[i][j] == 1 && (liveCounter < 2 || liveCounter > 3)) {
                    board[i][j] = 2;
                } else if (board[i][j] == 0 && liveCounter == 3) {
                    board[i][j] = 3;
                }
            }
        }
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = board[i][j]%2;
            }
        }
    }

}



class L794_Valid_Tic_Tac_Toe {
    int[][] winners = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    boolean isWin(char[] array, char ch) {
        for(int i = 0; i < winners.length; ++i) {
            if(array[winners[i][0]] == ch
                    && array[winners[i][1]] == ch
                    && array[winners[i][2]] == ch) {
                return true;
            }
        }
        return false;
    }

    public boolean validTicTacToe(String[] board) {
        StringBuilder sb = new StringBuilder();
        for(String str : board) {
            sb.append(str);
        }
        char[] array = sb.toString().toCharArray();
        int[] nums = countNumbers(array);
        if(nums[0] != nums[1] && nums[0] != nums[1] + 1) {
            return false;
        }

        boolean xWin = isWin(array, 'X');
        boolean oWin = isWin(array, 'O');
        if(xWin == true && oWin == true) {
            return false;
        }
        if(xWin == true && nums[0] == nums[1]) {
            return false;
        }
        if(oWin == true && nums[0] == nums[1] + 1) {
            return false;
        }

        return true;
    }

    int[] countNumbers(char[] array) {
        int[] nums = new int[2];
        for(int i = 0; i < array.length; ++i) {
            if(array[i] == 'X') {
                nums[0] += 1;
            } else if(array[i] == 'O') {
                nums[1] += 1;
            }
        }
        return nums;
    }
}



class L348_Design_Tic_Tac_Toe {
    int[] rows = null;
    int[] cols = null;
    int diagonal = 0;
    int antidiagonal = 0;
    int size = 0;

    /** Initialize your data structure here. */
    public L348_Design_Tic_Tac_Toe(int n) {
        this.rows = new int[n];
        this.cols = new int[n];
        this.size = n;
    }

    /** Player {player} makes a move at ({row}, {col}).
     @param row The row of the board.
     @param col The column of the board.
     @param player The player, can be either 1 or 2.
     @return The current winning condition, can be either:
     0: No one wins.
     1: Player 1 wins.
     2: Player 2 wins. */
    public int move(int row, int col, int player) {
        int step = player == 1 ? 1 : -1;
        rows[row] += step;
        cols[col] += step;

        if(row == col) {
            diagonal += step;
        }
        if(row + col == size - 1) {
            antidiagonal += step;
        }

        if(Math.abs(rows[row]) == size
                || Math.abs(cols[col]) == size
                || Math.abs(diagonal)  == size
                || Math.abs(antidiagonal) == size) {
            return player;
        }
        return 0;
    }
}




