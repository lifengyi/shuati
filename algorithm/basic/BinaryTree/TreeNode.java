package com.stevenli.interview.algorithm.basic.BinaryTree;

public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;
    public Tag tag;

    public TreeNode(int value) {
        val = value;
        left = null;
    }
}



class Res {
    public int value;
}

enum Tag {left, right}




