package interview;

import java.util.*;

public class interview_Google {

}

class L34_Gas_Station {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int startIndex = 0, total = 0, sum = 0;
        for(int i = 0; i < gas.length; ++i) {
            sum += gas[i] - cost[i];
            if(sum < 0) {
                total += sum;
                sum = 0;
                startIndex = i + 1;
            }
        }
        total += sum;
        return total >= 0 ? startIndex : -1;
    }
    public int canCompleteCircuit_dp(int[] gas, int[] cost) {
        int n = gas.length;
        int[][] dp = new int[2][n];
        int prev = 0;

        for(int i = 1; i <= n; ++i) {
            for(int j = 0; j < n; ++j) {
                prev = (n + j - 1) % n;
                if(dp[(i - 1)%2][prev] < 0) {
                    dp[i%2][j] = -1;
                } else {
                    dp[i%2][j] = dp[(i - 1)%2][prev] + gas[prev] - cost[prev];
                }

                if(i == n && dp[i%2][j] >= 0) {
                    return j;
                }
            }
        }

        return -1;
    }
}

class L162_Find_Peak_Element_Google {
    public int findPeakElement(int[] nums) {
        if(nums == null || nums.length < 2 || nums[0] > nums[1]) {
            return 0;
        } else if(nums[nums.length - 1] > nums[nums.length - 2]) {
            return nums.length - 1;
        }

        int left = 1, right = nums.length - 2;
        int mid = 0;
        while(left < right) {
            mid = left + (right - left)/2;
            if(nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
                return mid;
            } else if (nums[mid] < nums[mid - 1]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}


class L205_Ismorphic_String {
    public boolean isIsomorphic(String s, String t) {
        int[] maps = new int[256];
        int[] mapt = new int[256];
        int cs = 0, ct = 0;
        for(int i = s.length() - 1; i >= 0; --i) {      // 倒序枚举是为了避免第0项是自己转自己导致逻辑出错 "ab"和"aa"
                                                        // 如果map表初始化为-1，则无需如此倒序枚举
            cs = (int)s.charAt(i);
            ct = (int)t.charAt(i);
            if(maps[cs] != mapt[ct]) {
                return false;
            }
            maps[cs] = mapt[ct] = i;
        }
        return true;
    }
}


class L146_LRU_Cache {
    class LRU<K, V> extends LinkedHashMap<K, V> {
        private int capacity = 0;

        public LRU(int capacity) {
            super(capacity * 2, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldestEntry) {
            if(size() > capacity) {
                return true;
            }
            return false;
        }
    }


    private LRU<Integer, Integer> lru = null;

    public L146_LRU_Cache(int capacity) {
        this.lru = new LRU(capacity);
    }

    public int get(int key) {
        return lru.containsKey(key) ? lru.get(key) : -1;
    }

    public void put(int key, int value) {
        lru.put(key, value);
    }
}


class L146_LRU_Cache_v2 {
    class Node {
        public int key;
        public int value;
        public Node prev;
        public Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            prev = null;
            next = null;
        }
    }

    private int capacity = 0;
    private Node head = null;
    private Node tail = null;
    private Map<Integer, Node> map = null;

    public L146_LRU_Cache_v2 (int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity * 2);
        this.head = new Node(0, 0);
        head.next = tail;
        this.tail = new Node(0, 0);
        tail.prev = head;
    }

    public int get(int key) {
        if(!map.containsKey(key)) {
            return -1;
        }

        Node node = map.get(key);
        if(node.next != tail) {
            pullNode(node);
            putAtTail(node);
        }

        return node.value;
    }

    public void put(int key, int value) {
        Node node = null;
        if(!map.containsKey(key)) {
            node = new Node(key, value);
            putAtTail(node);
        } else {
            node = map.get(key);
            node.value = value;
            pullNode(node);
            putAtTail(node);
        }
        map.put(key, node);
        if(map.size() > capacity) {
            deleteEldestNode();
        }
    }

    private void deleteEldestNode() {
        Node firstNode = head.next;
        map.remove(firstNode.key);

        pullNode(firstNode);
        firstNode.prev = null;
        firstNode.next = null;
    }

    private void pullNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void putAtTail(Node node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }
}


class L252_Meeting_Rooms {
    public class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }

    class Node {
        public int id;
        public int type;    //0:start, 1: end
        public int value;
        public Node(int id, int type, int value) {
            this.id = id;
            this.type = type;
            this.value = value;
        }
    }

    public static final int TYPE_START = 0;
    public static final int TYPE_END = 1;

    public boolean canAttendMeetings(Interval[] intervals) {
        PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>(){
            @Override
            public int compare(Node n1, Node n2) {
                if(n1.value == n2.value) {
                    return n2.type - n1.type;
                }
                return n1.value - n2.value;
            }
        });

        for(int i = 0; i < intervals.length; ++i) {
            Node node1 = new Node(i, TYPE_START, intervals[i].start);
            Node node2 = new Node(i, TYPE_END, intervals[i].end);
            queue.offer(node1);
            queue.offer(node2);
        }

        boolean canAttendMeeting = true;
        while(!queue.isEmpty()) {
            Node node = queue.poll();
            if(node.type == TYPE_START) {
                if(canAttendMeeting == false) {
                    return false;
                } else {
                    canAttendMeeting = false;
                }
            } else {
                canAttendMeeting = true;
            }
        }
        return true;
    }

}



class L340_Longest_Substring_with_At_Most_K_Distinct_Characters {
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        int count = 0, maxLen = 0;
        int[] map = new int[256];
        int len = s.length();
        int i = 0, j = 0, index = 0;
        for(; i < len; ++i) {
            while(j < len && validate(map, (int)(s.charAt(j)), count, k)) {
                index = (int)s.charAt(j);
                if(map[index] == 0) {
                    map[index] = 1;
                    count++;
                } else {
                    map[index] += 1;
                }
                maxLen = Math.max(j - i + 1, maxLen);
                j++;
            }
            if(j == len) {
                return maxLen;
            }
            index = (int)s.charAt(i);
            map[index] -= 1;
            if(map[index] == 0) {
                count--;
            }
        }

        return maxLen;
    }

    private boolean validate(int[] map, int index, int count, int k) {
        if(map[index] == 0) {
            return count + 1 > k ? false : true;
        }
        return true;
    }

}


class L395_Longest_Substring_with_At_Least_K_Repeating_Characters {
    public int longestSubstring(String s, int k) {
        int[] map = new int[26];
        for(int i = 0; i < s.length(); ++i) {
            map[s.charAt(i) - 'a'] += 1;
        }

        StringBuilder sb = new StringBuilder(s);
        for(int i = 0; i < s.length(); ++i) {
            if(map[s.charAt(i) - 'a'] < k) {
                sb.setCharAt(i, ',');
            }
        }


        String[] strings = sb.toString().split(",");
        if(strings.length == 1) {
            return strings[0].length();
        }

        int maxLen = 0;
        for(String string : strings) {
            maxLen = Math.max(maxLen, longestSubstring(string, k));
        }
        return maxLen;
    }
}


/**
 * 思路上首先要理解善用timestamp，推此即彼；
 *
 * 问题有点类似于LRU，都是哈希表的使用，快速的访问元素是否存在
 * 不同在于LRU关注最后一次访问，而这里关注的是第一次访问；
 *
 * 增加了需要查询在一定的时间范围内是否存在数据，timestamp成为
 * 最好的突破口
 *
 * disadvantage: 内存会越来越大，且低频词将会永久的存在于内存中
 * 解决：定期清理哈希表
 *
 * 改进：额外维护每个时间点的关键词列表；
 */
class L359_Logger_Rate_Limiter {
    int diff = 10;
    Map<String, Integer> map = null;

    /** Initialize your data structure here. */
    public L359_Logger_Rate_Limiter() {
        map = new HashMap<>();
    }

    /** Returns true if the message should be printed in the given timestamp, otherwise returns false.
     If this method returns false, the message will not be printed.
     The timestamp is in seconds granularity. */
    public boolean shouldPrintMessage(int timestamp, String message) {
        if(!map.containsKey(message)) {
            map.put(message, timestamp);
            return true;
        }

        int oldTimestamp = map.get(message);
        if(timestamp - oldTimestamp >= diff) {
            map.put(message, timestamp);
            return true;
        } else {
            return false;
        }
    }
}

class L359_Logger_Rate_Limiter_v2 {
    class Tuple {
        public int t;
        public String s;
        public Tuple(int t, String s) {
            this.t = t;
            this.s = s;
        }
    }

    Queue<Tuple> queue = null;
    Set<String> set = null;

    public L359_Logger_Rate_Limiter_v2() {
        queue = new LinkedList<Tuple>();
        set = new HashSet<String>();
    }

    public boolean shouldPrintMessage(int timestamp, String message) {
        while(!queue.isEmpty() && queue.peek().t <= timestamp - 10) {
            String s = queue.poll().s;
            set.remove(s);
        }

        if(set.contains(message)) {
            return false;
        }

        queue.offer(new Tuple(timestamp, message));
        set.add(message);
        return true;
    }
}

class L399_Evaluate_Division_vGraph {
    public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {
        Map<String, Map<String, Double>> graph =  new HashMap<>();
        generateGraph(equations, values, graph);

        double[] ret = new double[queries.length];
        for(int i = 0; i < queries.length; ++i) {
            String[] query = queries[i];
            String s1 = query[0];
            String s2 = query[1];
            ret[i] = bfs(graph, s1, s2);
        }
        return ret;
    }

    void generateGraph(String[][] equations, double[] values,
                       Map<String, Map<String, Double>> graph) {
        for(int i = 0; i < equations.length; ++i) {
            String s1 = equations[i][0];
            String s2 = equations[i][1];
            if(!graph.containsKey(s1)) {
                Map<String, Double> nexts = new HashMap<String, Double>();
                nexts.put(s2, values[i]);
                graph.put(s1, nexts);
            } else {
                graph.get(s1).put(s2, values[i]);
            }
            if(!graph.containsKey(s2)) {
                Map<String, Double> nexts = new HashMap<String, Double>();
                nexts.put(s1, 1/values[i]);
                graph.put(s2, nexts);
            } else {
                graph.get(s2).put(s1, 1/values[i]);
            }
        }
    }

    double bfs(Map<String, Map<String, Double>> graph, String s1, String s2) {
        if(!graph.containsKey(s1) || !graph.containsKey(s2)) {
            return -1.0;
        } else if(s1.equals(s2)) {
            return 1.0;
        }

        LinkedList<Tuple> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(new Tuple(s1, 1.0));
        visited.add(s1);

        while(!queue.isEmpty()) {
            Tuple currentTuple = queue.poll();
            String currentStr = currentTuple.s;
            double currentVal = currentTuple.v;

            Map<String, Double> nexts = graph.get(currentStr);
            for(String key : nexts.keySet()) {
                String nextStr = key;
                double nextVal = nexts.get(key);

                //str is already processed.
                if(visited.contains(nextStr)) {
                    continue;
                }

                if(nextStr.equals(s2)) {
                    return currentVal * nextVal;
                }

                //set flag and put it into queue
                visited.add(nextStr);
                queue.offer(new Tuple(nextStr, currentVal * nextVal));
            }
        }

        return -1.0;
    }

    class Tuple {
        double v;
        String s;
        public Tuple(String s, double v) {
            this.s = s;
            this.v = v;
        }
    }
}


/**
 *  利用路径压缩来实现比率的转换，得出所有节点相对于顶点的比例，顶点始终为1
 *  使用哈希表实现并查集
 */
class L399_Evaluate_Division_vUnionFind {
    public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {
        UnionFind uf = new UnionFind();
        for(int i = 0; i < equations.length; ++i) {
            uf.union(equations[i][0], equations[i][1], values[i]);
        }

        double[] ret = new double[queries.length];
        for(int i = 0; i < ret.length; ++i) {
            String root1 = uf.findRoot(queries[i][0]);
            String root2 = uf.findRoot(queries[i][1]);
            if(root1 == null || root2 == null || !root1.equals(root2)) {
                ret[i] = -1.0;
            } else {
                ret[i] = uf.getValue(queries[i][0]) / uf.getValue(queries[i][1]);
            }
        }
        return ret;
    }

    class UnionFind {
        Map<String, Tuple> fathers  = null;

        public UnionFind() {
            fathers = new HashMap<>();
        }

        public double getValue(String s) {
            return fathers.get(s).value;
        }

        public String findRoot(String s) {
            if(!fathers.containsKey(s)) {
                return null;
            }

            double value = 1L;
            String key = s;
            Tuple tuple = fathers.get(key);
            while(!tuple.parent.equals(key)) {
                value *= tuple.value;
                key = tuple.parent;
                tuple = fathers.get(key);
            }
            tuple = fathers.get(s);
            tuple.parent = key;
            tuple.value = value;
            return key;
        }

        public void union(String s, String p, double value) {
            String roots = findRoot(s);
            String rootp = findRoot(p);
            if(roots == null && rootp == null) {
                fathers.put(s, new Tuple(p, value));
                fathers.put(p, new Tuple(p, 1L));
            } else if(roots == null) {
                fathers.put(s, new Tuple(p, value));
            } else if(rootp == null) {
                fathers.put(p, new Tuple(s, 1L/value));
            } else if(!roots.equals(rootp)) {
                Tuple ts = fathers.get(s);
                Tuple tp = fathers.get(p);
                Tuple troots = fathers.get(roots);
                troots.parent = rootp;
                troots.value = value * tp.value / ts.value;
            }
        }
    }

    class Tuple {
        String parent;
        double value;
        public Tuple(String parent, double value) {
            this.parent = parent;
            this.value = value;
        }
    }
}


/**
 *  TIPS:
 *  1. findRoot中的while已经犯错好几次了：while写成if
 *
 */
class L737_Sentence_Similarity_II {
    public boolean areSentencesSimilarTwo(String[] words1, String[] words2, String[][] pairs) {
        if(words1 == null && words2 == null) {
            return true;
        } else if(words1 == null || words2 == null || words1.length != words2.length) {
            return false;
        }

        UnionFind uf = new UnionFind();
        for(String[] pair : pairs) {
            uf.union(pair[0], pair[1]);
        }

        for(int i = 0; i < words1.length; ++i) {
            if(!words1[i].equals(words2[i])
                    && !uf.isConnected(words1[i], words2[i])) {
                return false;
            }
        }
        return true;
    }

    class UnionFind {
        Map<String, String> fathers = null;

        public UnionFind() {
            fathers = new HashMap<>(2048);
        }

        public String findRoot(String s) {
            if(!fathers.containsKey(s)) {
                return null;
            }

            String parent = s;
            while(!parent.equals(fathers.get(parent))) {
                parent = fathers.get(parent);
            }
            fathers.put(s, parent);
            return parent;
        }

        public boolean isConnected(String s1, String s2) {
            String root1 = findRoot(s1);
            String root2 = findRoot(s2);

            if(root1 == null || root2 == null) {
                return false;
            }

            return root1.equals(root2);
        }

        public void union(String s1, String s2) {
            String root1 = findRoot(s1);
            String root2 = findRoot(s2);
            if(root1 == null && root2 == null) {
                fathers.put(s1, s2);
                fathers.put(s2, s2);
            } else if(root1 == null) {
                fathers.put(s1, root2);
            } else if(root2 == null) {
                fathers.put(s2, root1);
            } else if(!root1.equals(root2)){
                fathers.put(root1, root2);
            }
        }
    }
}


class L490_The_Maze {
    public boolean hasPath(int[][] maze, int[] start, int[] destination) {
        if(maze == null || maze.length == 0 || maze[0].length == 0) {
            return false;
        }

        int row = maze.length;
        int col = maze[0].length;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        int[][] visited = new int[row][col];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(getNum(start[0], start[1], col));
        visited[start[0]][start[1]] = 1;

        while(!queue.isEmpty()) {
            int num = queue.poll();
            for(int i = 0; i < 4; ++i) {
                int nx = num/col;
                int ny = num%col;
                while(isSpace(nx + dx[i], ny + dy[i], maze, visited)) {
                    nx += dx[i];
                    ny += dy[i];
                }
                if(visited[nx][ny] == 1) {
                    continue;
                }
                if(nx == destination[0] && ny == destination[1]) {
                    return true;
                }
                visited[nx][ny] = 1;
                queue.offer(getNum(nx, ny, col));
            }
        }
        return false;
    }

    boolean isSpace(int x, int y, int[][] maze, int[][] visited) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length
                && maze[x][y] == 0) {
            return true;
        }
        return false;
    }

    int getNum(int x, int y, int col) {
        return x * col + y;
    }
}


class L505_The_Maze_II {
    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        if(maze == null || maze.length == 0 || maze[0].length == 0) {
            return -1;
        }

        int row = maze.length;
        int col = maze[0].length;
        int minSteps = Integer.MAX_VALUE;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        PriorityQueue<Tuple> queue = new PriorityQueue<>(new Comparator<Tuple>(){
            @Override
            public int compare(Tuple t1, Tuple t2) {
                return t1.steps - t2.steps;
            }
        });

        int[][] length = new int[row][col];
        for(int i = 0; i < row * col; ++i) {
            length[i/col][i%col] = Integer.MAX_VALUE;
        }

        queue.offer(new Tuple(start[0], start[1], 0));
        length[start[0]][start[1]] = 0;

        while(!queue.isEmpty()) {
            Tuple curTuple = queue.poll();

            for(int j = 0; j < 4; ++j) {
                int x = curTuple.x;
                int y = curTuple.y;
                int steps = curTuple.steps;
                while(isSpace(x + dx[j], y + dy[j], maze)) {
                    x += dx[j];
                    y += dy[j];
                    steps++;
                }

                if(length[x][y] <= steps) {
                    continue;
                }

                length[x][y] = steps;
                queue.offer(new Tuple(x, y, steps));
            }
        }

        if(length[destination[0]][destination[1]] == Integer.MAX_VALUE) {
            return -1;
        }
        return length[destination[0]][destination[1]];
    }

    boolean isSpace(int x, int y, int[][] maze) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length
                && maze[x][y] == 0) {
            return true;
        }
        return false;
    }

    class Tuple {
        int x;
        int y;
        int steps;
        public Tuple(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }
    }
}

class L499_The_Maze_III {
    class Tuple implements Comparable<Tuple> {
        int x = 0;
        int y = 0;
        int s = 0;
        String p = "";
        public Tuple(int x, int y , int s, String p) {
            this.x = x;
            this.y = y;
            this.s = s;
            this.p = p;
        }

        public void appendDirection(String dir) {
            this.p = this.p + dir;
        }

        public int compareTo(Tuple other) {
            if(this.s != other.s) {
                return this.s - other.s;
            }
            return this.p.compareTo(other.p);
        }
    }

    String[] dir = {"u", "d", "l", "r"};
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};
    private final String NOT_FOUND = "impossible";

    public String findShortestWay(int[][] maze, int[] ball, int[] hole) {
        if(maze == null || maze.length == 0 || maze[0].length == 0) {
            return NOT_FOUND;
        }

        int row = maze.length;
        int col = maze[0].length;
        PriorityQueue<Tuple> queue = new PriorityQueue<>();
        int[][] visited = new int[row][col];

        queue.offer(new Tuple(ball[0], ball[1], 0, ""));
        while(!queue.isEmpty()) {
            Tuple tuple = queue.poll();
            if(visited[tuple.x][tuple.y] == 1) {
                continue;
            }

            visited[tuple.x][tuple.y] = 1;
            if(tuple.x == hole[0] && tuple.y == hole[1]) {
                return tuple.p;
            }

            for(int i = 0; i < 4; ++i) {
                Tuple stopPoint = walk(tuple, i, maze, hole);
                queue.offer(stopPoint);
            }
        }
        return NOT_FOUND;
    }

    private Tuple walk(Tuple tuple, int idx, int[][] maze, int[] hole) {
        int x = tuple.x;
        int y = tuple.y;
        int steps = tuple.s;
        String path = tuple.p + dir[idx];
        while(isSpace(x + dx[idx], y + dy[idx], maze)) {
            x += dx[idx];
            y += dy[idx];
            steps++;
            if(x == hole[0] && y == hole[1]) {
                return new Tuple(x, y, steps, path);
            }
        }
        return new Tuple(x, y, steps, path);
    }

    private boolean isSpace(int x, int y, int[][] maze) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length
                && maze[x][y] != 1) {
            return true;
        }
        return false;
    }
}


class L271_Encode_and_Decode_Strings {
    // Encodes a list of strings to a single string.
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        String head = null;
        for(String str : strs) {
            head = String.format("%05d", str.length());
            sb.append(head).append(str);
        }
        return sb.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode(String s) {
        List<String> list = new ArrayList<>();
        if(s.length() == 0) {
            return list;
        }

        int length = s.length();
        int blockIndex = 0, blockLen = 0;
        while(blockIndex < length) {
            blockLen = Integer.valueOf(s.substring(blockIndex, blockIndex + 5));
            blockIndex += 5;
            list.add(s.substring(blockIndex, blockIndex + blockLen));
            blockIndex += blockLen;
        }
        return list;
    }
}


class L486_Predict_the_Winner {
    public boolean PredictTheWinner(int[] values) {
        // write your code here
        int n = values.length;
        if(n < 3) {
            return true;
        }

        int[][] dp = new int[n][n];
        int sum = 0;
        for(int i = 0; i < n; ++i) {
            for(int j = 0; j < n; ++j) {
                if(i == j) {
                    dp[i][j] = values[i];
                } else if(i + 1 == j) {
                    dp[i][j] = Math.max(values[i], values[j]);
                } else {
                    dp[i][j] = -1;
                }
            }
            sum += values[i];
        }

        int ret = search(values, 0, n - 1, dp);
        return ret >= sum - ret;
    }

    int search(int[] values, int i, int j, int [][] dp) {
        if(dp[i][j] != -1) {
            return dp[i][j];
        }

        int takeLeft = values[i] + Math.min(search(values, i + 2, j, dp), search(values, i + 1, j - 1, dp));
        int takeRight = values[j] + Math.min(search(values, i + 1, j - 1, dp), search(values, i, j - 2, dp));

        dp[i][j] = Math.max(takeLeft, takeRight);
        return dp[i][j];
    }
}




class L263_Ugly_Number {
    public boolean isUgly(int num) {
        if(num <= 0) {
            return false;
        } else if(num == 1) {
            return true;
        }

        for(int i = 2; i <= 5; ++i) {
            while(num%i == 0) {
                num = num/i;
            }
        }
        return num == 1;
    }
}

/**
 * Hint:
 * 1. An ugly number must be multiplied by either 2, 3,
 *    or 5 from a smaller ugly number.
 * 2. maintain the order of the ugly numbers. Try a similar
 *    approach of merging from three sorted lists: L1, L2, and L3.
 */
class L264_Ugly_Number_II {
    public int nthUglyNumber(int n) {
        int[] uglyNumbers = new int[n];
        uglyNumbers[0] = 1;

        int idx2 = 0, idx3 = 0, idx5 = 0;
        for(int i = 1; i < n; ++i) {
            int num2 = uglyNumbers[idx2] * 2;
            int num3 = uglyNumbers[idx3] * 3;
            int num5 = uglyNumbers[idx5] * 5;
            uglyNumbers[i] = Math.min(num2, Math.min(num3, num5));

            if(uglyNumbers[i] == num2) {
                idx2++;
            }
            if(uglyNumbers[i] == num3) {
                idx3++;
            }
            if(uglyNumbers[i] == num5) {
                idx5++;
            }
        }
        return uglyNumbers[n - 1];
    }
}


/**
 *  NGE I: 求输入数据流的NGE，基准数据为数组且数据不重复；
 *  解决：单调栈处理基准数据，结果存入哈希表，对输入流进行后续处理
 *
 *  NGE II: 求所有基准数据的NGE，且基准数据为循环数组
 *  解决： 单调栈处理基准数据，结果存入对应的输出数组
 *
 *  NGE III: 求当前数字的NGE
 *  解决： 类似于Next Permutaion，单调栈思想，但是不需要开辟单调栈
 *        从后往前便利数据，应该是一个递增序列；
 *        如果不是则替换当前数据 <-> 之前数据集中比这个数据小的第一个数据
 *        交换之后，交换过的小的数据那一项开始，进行倒序排列；
 *
 *        注意最后求解的数据有效性，即使不可以待遇Integer的范围
 */
class L496_Next_Greater_Element_I {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        LinkedList<Integer> stack = new LinkedList<>();
        Map<Integer, Integer> map = new HashMap<>();

        for(int num : nums2) {
            if(stack.isEmpty()) {
                stack.push(num);
            } else {
                while(!stack.isEmpty() && num > stack.peek()) {
                    map.put(stack.poll(), num);
                }
                stack.push(num);
            }
        }
        while(!stack.isEmpty()) {
            map.put(stack.poll(), -1);
        }

        int[] ret = new int[nums1.length];
        for(int i = 0; i < nums1.length; ++i) {
            ret[i] = map.get(nums1[i]);
        }
        return ret;
    }
}


class L503_Next_Greater_Element_II {
    class Tuple {
        int num = 0;
        int index = 0;
        public Tuple(int num, int index) {
            this.num = num;
            this.index = index;
        }
    }

    public int[] nextGreaterElements(int[] nums) {
        LinkedList<Tuple> stack = new LinkedList<>();
        int[] ret = new int[nums.length];

        for(int i = 0; i < nums.length; ++i) {
            if(stack.isEmpty()) {
                stack.push(new Tuple(nums[i], i));
            } else {
                while(!stack.isEmpty() && nums[i] > stack.peek().num) {
                    ret[stack.poll().index] = nums[i];
                }
                stack.push(new Tuple(nums[i], i));
            }
        }

        if(!stack.isEmpty()) {
            for(int i = 0; i < nums.length; ++i) {
                while(!stack.isEmpty() && nums[i] > stack.peek().num
                        && i < stack.peek().index) {
                    ret[stack.poll().index] = nums[i];
                }
            }
        }
        while(!stack.isEmpty()) {
            ret[stack.poll().index] = -1;
        }

        return ret;
    }
}


class L556_Next_Greater_Element_III {
    public int nextGreaterElement(int n) {
        if(n <= 11) {
            return -1;
        }

        char[] array = String.valueOf(n).toCharArray();
        int i = array.length - 2;
        for(; i >= 0; --i) {
            if(array[i] < array[i + 1]) {
                break;
            }
        }
        if(i < 0) {
            return -1;
        }

        int smallest = 0;
        for(int j = i + 1; j < array.length; ++j) {
            if(array[j] > array[i]) {
                smallest = j;
            }
        }

        char ch = array[i];
        array[i] = array[smallest];
        array[smallest] = ch;

        int left = i + 1, right = array.length - 1;
        while(left < right) {
            ch = array[left];
            array[left] = array[right];
            array[right] = ch;
            left++;
            right--;
        }

        Long tmp = Long.valueOf(String.valueOf(array));
        if(tmp > Integer.MAX_VALUE) {
            return -1;
        }
        return Integer.valueOf(String.valueOf(array));
    }
}


class L222_Count_Complete_Tree_Nodes {
    public int countNodes(TreeNode root) {
        if(root == null) {
            return 0;
        }

        int lHigh = 1;
        TreeNode lNode = root.left;
        while(lNode != null) {
            lHigh++;
            lNode = lNode.left;
        }

        int rHigh = 1;
        TreeNode rNode = root.right;
        while(rNode != null) {
            rHigh++;
            rNode = rNode.right;
        }

        if(lHigh == rHigh) {
            return (1 << lHigh) - 1;
        }

        return 1 + countNodes(root.left) + countNodes(root.right);
    }
}

class L205_Isomorphic_Strings {
    public boolean isIsomorphic(String s, String t) {
        int[] m1 = new int[256];
        int[] m2 = new int[256];
        for(int i = 0; i < s.length(); ++i) {
            char sc = s.charAt(i);
            char tc = t.charAt(i);
            if(m1[sc] == 0 && m2[tc] == 0) {
                m1[sc] = i + 1;
                m2[tc] = i + 1;
            } else if(m1[sc] == 0 || m2[tc] == 0 || m1[sc] != m2[tc]) {
                return false;
            }
        }

        return true;
    }
}


/**
 * De Bruijin Sequence
 *
 * Use Hierholzer to get a Eu
 */
class L753_Cracking_the_Safe {
    public String crackSafe(int n, int k) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; ++i){
            sb.append("0");
        }
        Set<String> visited = new HashSet<>();
        visited.add(sb.toString());
        dfs(sb, visited, n, k);
        return sb.toString();
    }

    boolean dfs(StringBuilder sb, Set<String> visited, int n, int k) {
        int totalLength = power(k, n) + n - 1;
        if(sb.length() == totalLength) {
            return true;
        }

        for(int i = 0; i < k; ++i) {
            String c = String.valueOf(i);
            if(validate(sb, visited, c, n)) {
                sb.append(c);
                String surffix = sb.substring(sb.length() - n);
                visited.add(surffix);
                boolean ret = dfs(sb, visited, n, k);
                if(ret == true) {
                    return true;
                }
                visited.remove(surffix);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return false;
    }

    boolean validate(StringBuilder sb, Set<String> visited, String str, int n) {
        String s = sb.substring(sb.length() - n + 1) + str;
        return !visited.contains(s);
    }

    int power(int k , int n) {
        int ret = 1;
        for(int i = 0; i < n; ++i) {
            ret *= k;
        }
        return ret;
    }
}


class L685_Redundant_Connection_II {

    /**
     * 题意中要求删除最后一个无效边（有些情况无效边删除有很多种情况）
     *
     * 1. 查找是否存在重复to节点
     *    如果存在，保留这两个edge作为candidate，并将原数组中第二个无效edge删除
     * 2. 并查集连接所有节点，
     *
     *    如果这个过程中没发现环路， 则可能因为：
     *      a. 本身不存在环路，只是两个非根节点同时执行一个节点
     *      b. 本身存在环路，2个非根节点指向同一个非根节点，在1中删除的正是那个正确的edge
     *
     *      a/b两个情况都返回candidate中的第二个节点
     *
     *    如果发现存在环路，则可能因为:
     *      a. 本身存在环路，2个非根节点指向同一个非根节点，在1中删除的是错误的edge
     *         返回candidate中第一个节点；
     *      b. 本身存在环路，且环路包括了根节点；
     *         返回当前节点，即发现环路时候的节点即可
     *
     */
    public int[] findRedundantDirectedConnection(int[][] edges) {
        List<int[]> redundantParents = new ArrayList<>();
        int[] parent = new int[edges.length + 1];
        for(int[] edge : edges) {
            if(parent[edge[1]] != 0) {
                redundantParents.add(new int[]{parent[edge[1]], edge[1]});
                redundantParents.add(new int[]{edge[0], edge[1]});
                edge[1] = 0;
            } else {
                parent[edge[1]] = edge[0];
            }
        }

        UnionFind uf = new UnionFind(edges.length);
        for(int[] edge : edges) {
            if(uf.isConnected(edge[0], edge[1])) {
                if(redundantParents.size() != 0) {
                    return redundantParents.get(0);
                }

                return edge;
            } else {
                uf.connect(edge[0], edge[1]);
            }
        }

        return redundantParents.get(1);
    }

    class UnionFind {
        int[] father = null;

        public UnionFind(int n) {
            this.father = new int[n + 1];
            for(int i = 1; i <= n; ++i) {
                father[i] = i;
            }
        }

        public int findRoot(int p) {
            while(p != father[p]) {
                father[p] = father[father[p]];
                p = father[p];
            }
            return p;
        }

        public boolean isConnected(int p, int q) {
            return findRoot(p) == findRoot(q);
        }

        public void connect(int p , int q) {
            int rootp = findRoot(p);
            int rootq = findRoot(q);
            if(rootp != rootq) {
                father[rootp] = rootq;
            }
        }
    }
}










