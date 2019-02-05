package poc;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.*;

public class test {

    @Test
    public void test_immutableMap(){
        ImmutableMap<Integer, String> test1 = ImmutableMap.of(
                1, "abc",
                2, "def"
        );
    }

    @Test
    public void test_sb() {
        StringBuilder sb = new StringBuilder();
        sb.append("abcdef\ngh");
        //System.out.println(sb.toString());
        int index = sb.indexOf("\n");
        //System.out.println(sb.indexOf("\n"));
        String str = sb.substring(0, index + 1);
        System.out.println(str);
        System.out.println(str.length());
        sb.delete(0, index + 1);
        System.out.println(sb.toString());
        System.out.println(sb.length());

        Map<Integer, Integer> map = new  HashMap<>();
        map.getOrDefault(1, 0);

    }

    @Test
    public void test_dp1() {
        String s1 = "1abcbc";
        String s2 = "1aggdefg";

        int index = 0, maxLen = Integer.MIN_VALUE;
        int row = s1.length();
        int col = s2.length();
        int[][] dp = new int[2][col + 1];

        for(int i = 1; i <= row; ++i) {
            for(int j = 1; j <= col; ++j) {
                if(s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i%2][j] = dp[(i - 1)%2][j - 1] + 1;
                    if(dp[i%2][j] > maxLen) {
                        index = j;
                        maxLen = dp[i%2][j];
                    }
                } else {
                    dp[i%2][j] = 0;
                }
            }
            System.out.println(Arrays.toString(dp[i%2]));
        }
        System.out.println(s2.substring(index - maxLen, index));
    }



    /*
    def find_highest_minimum_sharpness(board):
    rows, cols = len(board), len(board[0])
    for c in xrange(1, cols):
        for r in xrange(rows):
            left_max = board[r][c - 1]

            if r - 1 >= 0:
                left_max = max(left_max, board[r - 1][c - 1])

            if r + 1 < rows:
                left_max = max(left_max, board[r + 1][c - 1])

            board[r][c] = min(board[r][c], left_max)

    res = board[0][-1]
    for r in xrange(1, rows):
        res = max(res, board[r][-1])

    return res
    */

    //文件很大，可以旋转90度，然后按行读取
    //旋转的问题，可以分块

    // N * N matrix
    // read a row and write to a col:  read : N disk seeks,  write: N * N disk seeks
    // read a col and write to a row:  read : N * N disk seeks, write: N disk seeks
    // if we use k(k < N) to read the sub matrix
    // read : K disk seeks , write: K disk seeks
    // we have # of sub matrix : N * N/ K * K
    // total read: N * N / K  total write: N * N / K



    @Test
    public void test_rotate() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        int n = matrix.length;
        int[][] copy = new int[n][n];
        int k = 2;
        rotate(matrix, copy, k);
    }

    private void rotate(int[][] matrix, int[][] copy, int k) {
        int n = matrix.length;

        int x = 0, xSteps = 0;
        while (x < n) {
            xSteps = Math.min(k, n - x);

            int y = 0, ySteps = 0;
            while (y < n) {
                ySteps =  Math.min(k, n - y);

                System.out.println(String.format("x = %d, y = %d", x, y));
                //rotateMatrix(matrix, x, x + xSteps, y, y + ySteps, copy);

                y += ySteps;
            }
            x += xSteps;
        }
    }

    private void rotateMatrix(int[][] matrix , int xstart, int xend, int ystart, int yend, int[][] copy) {
        for(int i = xstart; i < xend; ++i) {
            for(int j = ystart; j < yend; ++j) {

            }
        }
    }


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
}


class Game_of_Life_large {
    public void gameOfLife() {
        int [] prev = null, cur = null, next = null;
        int[] pointer = null;

        while ((pointer = readLine()) != null) {
            if (cur == null) {
                cur = pointer;
                continue;
            }

            if (next == null)
                next = pointer;

            //First row
            if (prev == null) {
                int[][] tmpBoard = new int[2][];
                tmpBoard[0] = cur.clone();
                tmpBoard[1] = next.clone();
                int[] nextStateBoard = updateBoard(tmpBoard, 0);
                writeLine(nextStateBoard);
            }
            else {
                int[][] tmpBoard = new int[3][];
                tmpBoard[0] = prev.clone();
                tmpBoard[1] = cur.clone();
                tmpBoard[2] = next.clone();
                int[] nextStateBoard = updateBoard(tmpBoard, 1);
                writeLine(nextStateBoard);
            }

            prev = cur;
            cur = next;
            next = null;
        }

        //last row
        int[][] tmpBoard = new int[2][];
        tmpBoard[0] =  prev.clone();
        tmpBoard[1] = cur.clone();
        int[] nextStateBoard = updateBoard(tmpBoard, 2);
        writeLine(nextStateBoard);
    }


    int[] readLine() {
        return new int[10];
    }

    void writeLine(int[] input) {

    }

    int getNeighbours(int[][] board, int i, int j) {

        int[] dx = {-1, -1, -1, 0,  0,  1, 1, 1};
        int[] dy = {-1, 0,  1,  -1, 1, -1, 0, 1};

        return 0;
    }


    private int[] updateBoard(int[][] board, int index) {

        int[] result = new int[board[0].length];
        int row = board.length;
        int col = board[0].length;

        for(int j = 0; j < col; ++j) {
            int neighbours = getNeighbours(board, index, j);
            if(board[index][j] == 1 && (neighbours == 2 ||neighbours == 3)) {
                result[j] = 1;
            } else if (board[index][j] == 0 && neighbours == 3){
                result[j] = 1;
            }
        }
        return result;
    }
}


class L928_Minimiza_Malware_Spread_II {
    public int minMalwareSpread(int[][] graph, int[] initial) {
        Map<Integer, Set<Integer>> graphMap = new HashMap<>();
        populateGraph(graph, graphMap);

        int minScope = Integer.MAX_VALUE, result = -1;
        int count = 0;
        for(int i : initial) {
            count = bfs(graphMap, i, initial);
            if(count < minScope) {
                minScope = count;
                result = i;
            } else if(count == minScope) {
                result = Math.min(result, i);
            }
        }
        return result;
    }

    void populateGraph(int[][] graph, Map<Integer, Set<Integer>> map) {
        int len = graph.length;
        for(int i = 0; i < len; ++i) {
            map.put(i, new HashSet<Integer>());
        }

        for(int i = 0; i < len; ++i) {
            for(int j = i + 1; j < len; ++j) {
                if(graph[i][j] == 1) {
                    map.get(i).add(j);
                    map.get(j).add(i);
                }
            }
        }
    }

    int bfs(Map<Integer, Set<Integer>> graph, int excludeNode, int[] initial) {
        LinkedList<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        visited.add(excludeNode);
        for(int i : initial) {
            if(i == excludeNode) {
                continue;
            }
            queue.offer(i);
            visited.add(i);
        }

        int count = 0;
        while(!queue.isEmpty()){
            int cur = queue.poll();
            count++;
            for(int next : graph.get(cur)) {
                if(visited.contains(next)) {
                    continue;
                }
                queue.offer(next);
                visited.add(next);
            }
        }
        return count;
    }
}



class L811_Subdomain_Visit_Count {
    public List<String> subdomainVisits(String[] cpdomains) {
        List<String> result = new ArrayList<>();
        if(cpdomains == null || cpdomains.length == 0) {
            return result;
        }

        Map<String, Integer> cache = new HashMap<>();
        for(String cpdomain : cpdomains) {
            String[] items = cpdomain.split(" ");
            int count = Integer.parseInt(items[0]);
            String domain = items[1];

            List<String> allSubDomains = getAllSubdomains(domain);
            for(String subdomain : allSubDomains) {
                if(cache.containsKey(subdomain)) {
                    cache.put(subdomain, cache.get(subdomain) + count);
                } else {
                    cache.put(subdomain, count);
                }
            }
        }
        for(String key : cache.keySet()) {
            String pair = cache.get(key) + " " + key;
            result.add(pair);
        }
        return result;
    }

    List<String> getAllSubdomains(String domain) {
        List<String> result = new ArrayList<>();
        result.add(domain);

        int index = -1;
        while((index = domain.indexOf(".")) != -1) {
            domain = domain.substring(index + 1, domain.length());
            result.add(domain);
        }
        return result;
    }
}
