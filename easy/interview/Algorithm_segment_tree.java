package com.stevenli.interview.easy.interview;

import java.util.*;

public class Algorithm_segment_tree {

    /**
     * LintCode: Interval Minimum Number
     */
    class SegmentTreeNode {
        int start;
        int end;
        int min;
        SegmentTreeNode left;
        SegmentTreeNode right;

        public SegmentTreeNode(int start, int end, int min) {
            this.start = start;
            this.end = end;
            this.min = min;
            this.left = null;
            this.right = null;
        }
    }

    class Interval {
        int start, end;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private SegmentTreeNode build(int[] A, int start, int end) {
        if(A == null || A.length == 0 || start > end) {
            return null;
        }

        SegmentTreeNode node = new SegmentTreeNode(start, end, 0);
        if(start == end) {
            node.min = A[start];
        } else {
            int mid = start + (end - start)/2;
            node.left = build(A, start, mid);
            node.right = build(A, mid + 1, end);
            node.min = Math.min(node.left.min, node.right.min);
        }

        return node;
    }

    private int query(SegmentTreeNode root, int start, int end) {
        if(root == null || start > end) {
            return 0;
        }

        if(root.start == start && root.end == end) {
            return root.min;
        }

        int leftMin = Integer.MAX_VALUE, rightMin = Integer.MAX_VALUE;
        int mid = root.start + (root.end - root.start)/2;
        if(start <= mid) {
            leftMin = query(root.left, start, Math.min(end, mid));
        }

        if(end > mid) {
            rightMin = query(root.right, Math.max(start, mid + 1), end);
        }

        return Math.min(leftMin, rightMin);
    }

    /**
     * @param A: An integer array
     * @param queries: An query list
     * @return: The result list
     */
    public List<Integer> intervalMinNumber(int[] A, List<Interval> queries) {
        // write your code here
        List<Integer> res = new ArrayList<>();
        if(A == null || A.length == 0 || queries.isEmpty()) {
            return res;
        }

        SegmentTreeNode root = build(A, 0, A.length - 1);
        for(Interval query : queries) {
            res.add(query(root, query.start, query.end));
        }
        return res;
    }
}


class Counter_Of_Smaller_Number {

    class SegmentTreeNode {
        int start;
        int end;
        int min;
        int max;
        SegmentTreeNode left;
        SegmentTreeNode right;

        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.min = 0;
            this.max = 0;
            this.left = null;
            this.right = null;
        }
    }

    private SegmentTreeNode build(int[] A, int start, int end) {
        SegmentTreeNode node = new SegmentTreeNode(start, end);
        if(start == end) {
            node.min = A[start];
            node.max = A[start];
        } else {
            int mid = start + (end - start)/2;
            node.left = build(A, start, mid);
            node.right = build(A, mid + 1, end);
            node.min = Math.min(node.left.min, node.right.min);
            node.max = Math.max(node.left.max, node.right.max);
        }

        return node;
    }

    private int querySmallerNumber(SegmentTreeNode root, int number) {
        int count = 0, left = 0, right = 0;
        if(number > root.max) {
            count = root.end - root.start + 1;
        } else if(number > root.min) {
            left = querySmallerNumber(root.left, number);
            right = querySmallerNumber(root.right, number);
            count = left + right;
        }
        return count;
    }
    /**
     * @param A: An integer array
     * @param queries: The query list
     * @return: The number of element in the array that are smaller that the given integer
     */
    public List<Integer> countOfSmallerNumber(int[] A, int[] queries) {
        // write your code here
        List<Integer> res = new ArrayList<>();
        if(A == null || queries == null) {
            return res;
        }

        if(A.length == 0) {
            for(int query : queries) {
                res.add(0);
            }
            return res;
        }

        SegmentTreeNode root = build(A, 0, A.length - 1);
        for(int query : queries) {
            res.add(querySmallerNumber(root, query));
        }
        return res;
    }
}

/**
 *  O(K) K为元素出现的最大值的上限
 *  排序：  O(K) + O(n)
 *  查找元素： O(logn)
 */

class Counter_Of_Smaller_Number_opt {
    class SegmentTreeNode {
        int start;
        int end;
        int count;
        SegmentTreeNode left;
        SegmentTreeNode right;

        public SegmentTreeNode(int start, int end, int count) {
            this.start = start;
            this.end = end;
            this.count = count;
            left = null;
            right = null;
        }
    }

    private SegmentTreeNode build(int start, int end) {
        SegmentTreeNode node = new SegmentTreeNode(start, end, 0);
        if(start != end) {
            int mid = start + (end - start)/2;
            node.left = build(start, mid);
            node.right = build(mid + 1, end);
        }
        return node;
    }

    private void modify(SegmentTreeNode root, int index) {
        if(root.start == index && root.end == index) {
            root.count += 1;
            return;
        }

        int mid = root.start + (root.end - root.start)/2;
        if(index >= root.start && index <= mid) {
            modify(root.left, index);
        }
        if(index > mid && index <= root.end) {
            modify(root.right, index);
        }

        root.count = root.left.count + root.right.count;
    }

    private int query(SegmentTreeNode root, int start, int end) {
        if(root.start == start && root.end == end) {
            return root.count;
        }

        int leftVal = 0, rightVal = 0;
        int mid = root.start + (root.end - root.start)/2;
        if(start <= mid) {
            //left
            leftVal = query(root.left, start, Math.min(end, mid));
        }
        if(end > mid) {
            rightVal = query(root.right, Math.max(start, mid + 1), end);
        }

        return leftVal + rightVal;
    }

    public ArrayList<Integer> countOfSmallerNumber(int[] A, int[] queries) {
        // write your code here
        int start = 0, end = 10000;
        ArrayList<Integer> res = new ArrayList<>();
        if(A == null || queries == null || queries.length == 0) {
            return res;
        }

        if(A.length == 0) {
            for(int query: queries) {
                res.add(0);
            }
            return res;
        }

        SegmentTreeNode root = build(start, end);
        for(int i = 0; i < A.length; ++i) {
            modify(root, A[i]);
        }

        for(int query : queries) {
            res.add(query(root, start, query - 1));
        }
        return res;
    }
}