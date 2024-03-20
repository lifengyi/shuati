package interview;

import java.util.*;

public class Algorithm_UnionFind {
    //stub

}

class L261_Graph_Valid_Tree {
    public boolean validTree_v2(int n, int[][]edges) {
        // validate the count of points and edges
        if(n != edges.length + 1) {
            return false;
        }

        // check if all nodes are connected
        UnionFindV2 graph = new UnionFindV2(n);
        for(int[] edge : edges) {
            graph.connect(edge[0], edge[1]);
        }

        // should be only 1 graph
        return graph.count == 1;
    }

    class UnionFindV2 {
        public int[] fathers;
        public int count;

        public UnionFindV2(int n) {
            fathers = new int[n];
            count = n;
            for(int i = 0; i < n; ++i) {
                fathers[i] = i;
            }
        }

        public int findRoot(int a) {
            if(a != fathers[a]) {
                fathers[a] = findRoot(fathers[a]);
                return fathers[a];
            }

            return a;
        }

        public void connect(int a, int b) {
            int rootA = findRoot(a);
            int rootB = findRoot(b);
            if(rootA != rootB) {
                fathers[rootA] = rootB;// everything should be build on top or Root
                count--;
            }
        }
    }

    public boolean validTree(int n, int[][] edges) {
        if(n < 2) {
            return true;
        }
        if(edges == null || edges.length == 0){
            return false;
        }

        int[] flag = new int[n];
        UnionFind unionFind = new UnionFind(n);
        for(int[] edge : edges) {
            int p = edge[0], q = edge[1];
            if(unionFind.isConnected(p, q)) {
                return false;
            } else {
                unionFind.union(p, q);
            }
        }

        return unionFind.getCount() == 1 ? true : false;
    }

    class UnionFind {
        int[] father;
        int count;

        public UnionFind(int n) {
            father = new int[n];
            for(int i = 0; i < n; ++i) {
                father[i] = i;
            }
            count = n;
        }

        public int getCount() {
            return count;
        }

        public boolean isConnected(int p, int q) {
            return find(p) == find(q);
        }

        public int find(int index) {
            while(father[index] != index) {
                father[index] = father[father[index]];
                index = father[index];
            }
            return index;
        }

        public void union (int p, int q) {
            int pRoot = find(p);
            int qRoot = find(q);
            if(pRoot == qRoot)
                return;
            father[qRoot] = pRoot;
            count--;
        }
    }
}

class L200_Number_of_Islands {
    private int revert2Dto1D(int x, int y, int col) {
        return x * col + y;
    }

    public int numIslands(char[][] grid) {
        if(grid == null || grid.length == 0 || grid[0].length == 0)
            return 0;

        int row = grid.length, col = grid[0].length, count = 0;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        UnionFind unionFind = new UnionFind(row * col);
        for(int i = 0; i < row; ++i) {
            for(int j = 0; j < col; ++j) {
                if(grid[i][j] == '1') {
                    count++;
                    for(int k = 0; k < 4; ++k) {
                        int nx = i + dx[k];
                        int ny = j + dy[k];
                        if(nx >= 0 && nx < row && ny >= 0 && ny < col) {
                            int current = revert2Dto1D(i, j, col);
                            int neighbour = revert2Dto1D(nx, ny, col);
                            if(grid[nx][ny] == '1' && !unionFind.isConnected(current, neighbour)) {
                                count--;
                                unionFind.union(current, neighbour);
                            }
                        }
                    }
                }
            }
        }

        return count;
    }

    class UnionFind{
        public int[] father;

        public UnionFind(int n) {
            father = new int[n];
            for(int i = 0; i < n; ++i) {
                father[i] = i;
            }
        }

        public int find(int index) {
            while(father[index] != index) {
                father[index] = father[father[index]];
                index = father[index];
            }

            return index;
        }

        public boolean isConnected(int p, int q) {
            return find(p) == find(q);
        }

        public void union(int p, int q) {
            int pRoot = find(p);
            int qRoot = find(q);
            if(pRoot == qRoot)
                return;

            father[qRoot] = pRoot;
        }
    }
}

class L305_Number_of_Islands_II {
    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        List<Integer> ret = new ArrayList<>();
        if(m == 0 || n == 0 || positions.length == 0 || positions[0].length < 2) {
            return ret;
        }

        UnionFind unionFind = new UnionFind(m * n);
        Set<Integer> islands = new HashSet<>();
        int count = 0;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        for(int[] position : positions) {
            int current = convert(position[0], position[1], n);
            if(!islands.contains(current)) {
                islands.add(current);
                count++;

                for(int i = 0; i < 4; ++i){
                    int nx = position[0] + dx[i];
                    int ny = position[1] + dy[i];
                    int neighbour = convert(nx, ny, n);
                    if(nx >= 0 && nx < m && ny >= 0 && ny < n
                            && islands.contains(neighbour)) {
                        if(!unionFind.isConnected(current, neighbour)) {
                            count--;
                            unionFind.union(current, neighbour);
                        }
                    }
                }
            }

            ret.add(count);
        }

        return ret;
    }

    private int convert(int x, int y, int n) {
        return x * n + y;
    }

    class UnionFind {
        int[] father;

        public UnionFind(int n) {
            father = new int[n];
            for(int i = 0; i < n; ++i) {
                father[i] = i;
            }
        }

        public boolean isConnected(int p, int q) {
            return find(p) == find(q);
        }

        public int find(int index) {
            while(father[index] != index) {
                father[index] = father[father[index]];
                index = father[index];
            }
            return index;
        }

        public void union(int p, int q) {
            int pRoot = find(p);
            int qRoot = find(q);
            if(pRoot == qRoot)
                return;
            father[qRoot] = pRoot;
        }
    }
}


class L323_Number_Of_Connected_Components_in_an_Undirected_Graph {
    class UnionFind {
        int[] father;
        int count;

        public UnionFind(int n) {
            father = new int[n];
            for(int i = 0; i < n; ++i) {
                father[i] = i;
            }
            count = n;
        }

        public int find(int n) {
            while(n != father[n]) {
                father[n] = father[father[n]];
                n = father[n];
            }

            return n;
        }

        public void union(int p, int q) {
            int pRoot = find(p);
            int qRoot = find(q);
            if(pRoot != qRoot) {
                father[qRoot] = pRoot;
                count--;
            }
        }

        public int getCount() {
            return count;
        }

    }

    public int countComponents(int n, int[][] edges) {
        UnionFind unionFind = new UnionFind(n);
        for(int[] edge : edges) {
            unionFind.union(edge[0], edge[1]);
        }
        return unionFind.getCount();
    }
}


/**
 * UnionFind的哈希表实现
 */
class L737_Sentence_Similarity_II_ {
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



class L399_Evaluate_Division_vUnionFind_ {
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



class L924_Minimize_Malware_Spread {
    class UnionFind {
        int n = 0;
        int[] father = null;
        int[] size = null;

        public UnionFind(int n) {
            this.n = n;
            this.size = new int[n];
            this.father = new int[n];
            for(int i = 0; i < n; ++i) {
                father[i] = i;
                size[i] = 1;
            }
        }

        public int getSize(int p) {
            return size[findRoot(p)];
        }

        public int findRoot(int p) {
            while(p != father[p]) {
                father[p] = father[father[p]];
                p = father[p];
            }
            return p;
        }

        public void union(int p, int q) {
            int rootp = findRoot(p);
            int rootq = findRoot(q);
            if(rootp == rootq) {
                return;
            }

            if(size[rootp] > size[rootq]) {
                father[rootq] = rootp;
                size[rootp] += size[rootq];
            } else {
                father[rootp] = rootq;
                size[rootq] += size[rootp];
            }
        }
    }

    public int minMalwareSpread(int[][] graph, int[] initial) {
        int n = graph.length;
        UnionFind uf = new UnionFind(n);
        for(int i = 0; i < n; ++i) {
            for(int j = i + 1; j < n; ++j) {
                if(graph[i][j] == 1) {
                    uf.union(i, j);
                }
            }
        }

        int index = -1, scope = Integer.MIN_VALUE;
        for(int i : initial) {
            int size = uf.getSize(i);
            if(size > scope) {
                scope = size;
                index = i;
            } else if(size == scope) {
                index = Math.min(index, i);
            }
        }
        return index;
    }
}


class L547_Friend_Circles {
    class UnionFind {
        private int[] father = null;
        private int count = 0;

        public UnionFind(int n) {
            this.count = n;
            this.father = new int[n];
            for(int i = 0; i < n; ++i) {
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

        public void connect(int p, int q) {
            int rootp = findRoot(p);
            int rootq = findRoot(q);
            if(rootp != rootq) {
                father[rootp] = rootq;
                count--;
            }
        }

        public int getCount() {
            return count;
        }

    }
    public int findCircleNum(int[][] M) {
        int num = M.length;
        UnionFind uf = new UnionFind(num);
        for(int i = 0; i < num; ++i) {
            for(int j = 0; j < num; ++j) {
                if(i != j && M[i][j] == 1) {
                    uf.connect(i, j);
                }
            }
        }
        return uf.getCount();
    }
}



class L150_Evaluate_Reverse_Polish_Notation {
    public int evalRPN(String[] tokens) {
        LinkedList<Long> stack = new LinkedList<>();
        for(String token : tokens) {
            if(isOperator(token)) {
                calculate(token, stack);
            } else {
                stack.push(Long.valueOf(token));
            }
        }
        long res = stack.pop();
        return (int)res;
    }

    void calculate(String operator, LinkedList<Long> stack) {
        long second = stack.pop();
        long first = stack.pop();
        long res = 0;
        if(operator.equals("*")) {
            res = first * second;
        } else if(operator.equals("/")) {
            res = first/second;
        } else if(operator.equals("+")) {
            res = first + second;
        } else {
            res = first - second;
        }
        stack.push(res);
    }

    boolean isOperator(String s) {
        if(s.equals("*") || s.equals("/") || s.equals("+") || s.equals("-")) {
            return true;
        }
        return false;
    }
}


