package connect4;

import java.util.Arrays;


/**
 *  1. totalStesp => verify player or verify if the matrix is full
 *  2. height[col] => verify the row
 *  3. checkWinningPoint:
 *     a. check the vertical line: check the contiguous row/row-1/row-2/row-3
 *     b. check the diagonal/horizontal/anti-diagonal line
 *        Be sure to use while loop to find all the possibilities
 */
public class Connect4 {

    int totalRow = 0;
    int totalCol = 0;
    int steps = 0;         // total steps
    int[] height = null;  // height of the current col
    int[][] matrix = null;

    public Connect4() {
        totalRow = 6;
        totalCol = 7;
        height = new int[totalCol];
        matrix = new int[totalRow][totalCol];
    }

    public boolean canPlay(int col) {
        return height[col] != totalRow;
    }

    public boolean play(int col) {
        if(!canPlay(col)) {
            return false;
        }

        matrix[getx(col)][col] = steps%2 == 0 ? 1 : 2;
        steps++;
        height[col]++;
        return true;
    }

    private int getx(int col) {
        return totalRow - 1 - height[col];
    }

    public int getPlayer() {
        return steps%2 == 0 ? 1 : 2;
    }

    public boolean isWinningPoint(int col) {
        if(steps == totalRow * totalCol || !canPlay(col)) {
            return false;
        }

        int player = getPlayer();

        if(height[col] >= 3
                && matrix[getx(col) + 1][col] == player            // check the vertical line
                && matrix[getx(col) + 2][col] == player
                && matrix[getx(col) + 3][col] == player) {
            return true;
        }

        //diagonal, row, anti-diagonal
        for(int dx = -1; dx <= 1; ++dx) {
            int x = getx(col);
            int y = col;

            int len = 1;
            for(int dy = -1; dy <= 1; dy += 2) {
                while(validate(totalRow, totalCol, x + dx, y + dy)  // find all the possibilities
                        && matrix[x + dx][y + dy] == player) {
                    x += dx * dy;
                    y += dy;
                    len++;
                }
            }

            if(len >= 4) {
                return true;
            }
        }

        return false;
    }

    private boolean validate(int row, int col, int x, int y) {
        if(x >= 0 && x < row && y >= 0 && y < col) {
            return true;
        }
        return false;
    }

    public void show() {
        for(int i = 0; i < totalRow; ++i) {
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    public static void main(String[] args) {
        Connect4 conn4 = new Connect4();

        conn4.show();
        conn4.play(3);
        conn4.play(3);
        conn4.play(2);
        conn4.play(4);
        conn4.play(2);
        conn4.play(2);
        conn4.play(1);
        conn4.play(1);
        conn4.play(1);
        //conn4.play(1);
        System.out.println(" ");
        conn4.show();

        for(int i = 0; i < 7; ++i) {
            if(conn4.isWinningPoint(i)) {
                System.out.println("find a wining point in col " + i);
            }
        }
        //if(conn4.isWinningPoint(1)) {
        //    System.out.println("find a wining point.");
        //}
    }
}


