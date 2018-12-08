package interview;

import java.util.*;

public class Algorithm_union_find {
    //stub

}

class L261_Graph_Valid_Tree {
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

