package dropbox;

import java.io.*;
import java.util.Arrays;

public class gameOfLife {

    /**
     * If the matrix is 1m * 1m, how will process the file
     *
     * Note: Write file: fallback 1/2/3 lines
     *       Fallback: must include '\n'
     */

    private int lineSize = 0;
    private RandomAccessFile raf = null;

    public static void main(String[] args) {
        String path = "/Users/fenli/Downloads/test/4.txt";
        gameOfLife proc = new gameOfLife();

        try {
            proc.gameOfLife(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameOfLife(String path) throws IOException {
        File file = new File(path);
        raf = new RandomAccessFile(file, "rw");

        int[] prev = null, mid = null, next = null;
        int[] readLine = null;

        boolean firstTime = true;

        while((readLine = readFile()) != null) {
            if(prev == null) {
                prev = readLine;
                continue;
            } else if(mid == null) {
                mid = readLine;
                continue;
            } else {
                next = readLine;
            }

            int[][] tmpBoard = new int[3][];
            tmpBoard[0] = prev;
            tmpBoard[1] = mid;
            tmpBoard[2] = next;

            int[] updatedLine = null;
            if(firstTime == true) {
                updatedLine = process(tmpBoard, 0);
                writeFile(updatedLine, 3);
                firstTime = false;
            }
            updatedLine = process(tmpBoard, 1);
            writeFile(updatedLine, 2);

            prev = mid;
            mid = next;
            next = null;
        }

        int[][] tmpBoard = new int[2][];
        tmpBoard[0] = prev;
        tmpBoard[1] = mid;
        int[] updatedLine = process(tmpBoard, 1);
        writeFile(updatedLine, 1);

        raf.close();
    }

    int[] process(int[][] board, int index) {
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        int row = board.length;
        int col = board[0].length;

        int[] result = new int[col];
        for(int j = 0; j < col; ++j) {
            int count = countNumber(board, index, j);
            if(board[index][j] == 1
                    && (count == 2 || count == 3)) {
                result[j] = 1;
            } else if(count == 3) {
                result[j] = 1;
            }
        }
        return result;
    }

    int countNumber(int[][] board, int x, int y) {
        int[] dx = {-1, -1, -1,  0, 0,  1, 1, 1};
        int[] dy = {-1,  0,  1, -1, 1, -1, 0, 1};

        int row = board.length;
        int col = board[0].length;

        int count = 0;

        for(int i = 0; i < 8; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(nx >= 0 && nx < row && ny >= 0 && ny < col && board[nx][ny] == 1) {
                count++;
            }
        }
        return count;
    }


    int[] readFile() throws IOException {
        String str = raf.readLine();
        if(str == null) {
            return null;
        }

        lineSize = str.length();
        int[] result = new int[lineSize];

        for(int i = 0; i < result.length; ++i) {
            result[i] = str.charAt(i) == '1' ? 1 : 0;
        }
        return result;
    }

    void writeFile(int[] nums, int fallBackLine) throws IOException {
        Long position = raf.getFilePointer();

        // override the previous read line,
        // fall back, including the '\n'
        raf.seek(position - lineSize * fallBackLine - fallBackLine);

        StringBuilder sb = new StringBuilder();
        for(int num : nums) {
            sb.append(num);
        }
        raf.write(sb.toString().getBytes());
        raf.seek(position);
    }
}
