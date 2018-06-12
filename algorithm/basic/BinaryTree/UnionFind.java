package com.stevenli.interview.algorithm.basic.BinaryTree;

/**
 * Weighted Quick-Union LeetCode_Tree With Path Compression
 * 1. LeetCode_Tree
 * 2. Weighted Quick-Union
 * 3. Path compression
 */
public class UnionFind {

    private int[] parent;
    private int[] size;
    private int count;

    public UnionFind(int n) {
        parent =  new int[n];
        size = new int[n];
        for(int i = 1; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int count(){
        return count;
    }

    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }

    public int find(int p) {
        int root = p;
        while(root != parent[root]) {
            //Point the parent to grandpa => compress the path
            parent[root] = parent[parent[root]];
            root = parent[root];
        }

        return root;
    }

    public void union(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == pRoot)
            return;

        //Weighted union
        int pSize = size[pRoot];
        int qSize = size[qRoot];
        if(pSize > qSize) {
            parent[qRoot] = pRoot;
            size[pRoot] += size[qRoot];
        } else {
            parent[pRoot] = qRoot;
            size[qRoot] += size[pRoot];
        }
        count--;
    }
}
