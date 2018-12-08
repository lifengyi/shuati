package interview;


import java.util.*;

public class Algorithm_template {

}


/**
 *  LeetCode 208 Implement Trie (Prefix Tree)
 */
class TrieTree {

    class TrieNode {
        char ch;
        TrieNode[] children;
        boolean isWord;
        public TrieNode(char ch) {
            this.children = new TrieNode[26];
            this.ch = ch;
            this.isWord = false;
        }
    }

    TrieNode root;

    public TrieTree() {
        root = new TrieNode('#');
    }

    public void insert(String word) {
        TrieNode node = root;
        char[] array = word.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            if(node.children[array[i] - 'a'] != null) {
                node = node.children[array[i] - 'a'];
            } else {
                node.children[array[i] - 'a'] = new TrieNode(array[i]);
                node = node.children[array[i] - 'a'];
            }
        }
        node.isWord = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        char[] array = word.toCharArray();
        int index = 0;
        for(int i = 0; i < array.length; ++i) {
            index = array[i] - 'a';
            if(node.children[index] == null) {
                return false;
            } else {
                node = node.children[index];
            }
        }
        return node.isWord;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        char[] array = prefix.toCharArray();
        int index = 0;
        for(int i = 0; i < array.length; ++i) {
            index = array[i] - 'a';
            if(node.children[index] == null) {
                return false;
            } else {
                node = node.children[index];
            }
        }
        return true;
    }

}

/**
 *  323. Number of Connected Components in an Undirected Graph
 */
class UnionFind {
    public int countComponents(int n, int[][] edges) {
        if(n == 0 || edges == null) {
            return 0;
        }

        _UnionFind uf = new _UnionFind(n);
        for(int[] edge : edges) {
            if(!uf.isConnected(edge[0], edge[1])) {
                uf.union(edge[0], edge[1]);
            }
        }
        return uf.getCount();
    }

    class _UnionFind {
        private int[] parent;
        private int count;

        public _UnionFind(int n) {
            count = n;
            parent = new int[n];
            for(int i = 0; i < n; ++i) {
                parent[i] = i;
            }
        }

        public int getCount(){
            return count;
        }

        public boolean isConnected(int p, int q) {
            int parentP = find(p);
            int parentQ = find(q);
            return parentP == parentQ;
        }

        public int find(int p) {
            while(p != parent[p]) {
                p = parent[p];
            }
            return p;
        }

        public void union(int p, int q) {
            int indexP = find(p);
            int indexQ = find(q);
            if(indexP != indexQ) {
                parent[indexP] = indexQ;
                count--;
            }
        }
    }
}

/**
 *  注意点：
 *  1. 理解外部传入的索引值的范围是0-n，还是1-n，
 *     该外部索引值范围对BIT内部的update和query操作有决定性意义
 *     所以在对外部数据集做离散化时，需要意识到离散后的数据索引值范围；
 *
 *  2. 理解update/query的意义，当外部索引值传入的时候，需要明确理解
 *     在外部索引值转换成内部索引值之后，
 *     update是更新当前内部索引值的值
 *     query是查询小于等于当前内部索引值的值，如果仅查询小于，需要-1操作
 */
class BinaryIndexedTree {
    public List<Integer> countSmaller(int[] nums) {
        LinkedList<Integer> ret = new LinkedList<>();
        if(nums == null || nums.length == 0) {
            return ret;
        }

        int[] newNums = nums.clone();
        Arrays.sort(newNums);

        int[] indexNums = discretize(nums, newNums);
        int len = indexNums.length;

        BIT bit = new BIT(len);
        for(int i = len - 1; i >= 0; --i) {
            ret.offerFirst(bit.query(indexNums[i]));
            bit.update(indexNums[i]);
        }

        return ret;
    }

    int[] discretize(int[] nums, int[] newNums) {
        int[] ret = new int[nums.length];
        for(int i = 0; i < nums.length; ++i) {
            ret[i] = Arrays.binarySearch(newNums, nums[i]) + 1;
        }
        return ret;
    }

    class BIT {
        public int len;
        public int[] bit;

        public BIT(int n) {
            this.len = n + 1;
            this.bit = new int[n + 1];
        }

        private int lowBit(int n) {
            return n & (-n);
        }

        public void update(int n) {
            for(int i = n; i < len; i += lowBit(i)) {
                bit[i] += 1;
            }
        }

        public int query(int n) {
            int count = 0;
            for(int i = n - 1; i > 0; i -= lowBit(i)) {
                count += bit[i];
            }
            return count;
        }
    }
}


/**
 *  LintCode 207 Interval Sum II
 *
 *  注意折半后：
 *  左边是 [start, middle]
 *  右边是 [middle + 1, end]
 *
 */
class SegmentTree {
    class IntervalTreeNode {
        int start;
        int end;
        int sum;
        IntervalTreeNode left;
        IntervalTreeNode right;
        public IntervalTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.sum = 0;
            this.left = null;
            this.right = null;
        }
    }

    IntervalTreeNode root = null;

    public SegmentTree(int[] A) {
        if(A == null || A.length == 0) {
            return;
        }
        root = build(A, 0, A.length - 1);
    }

    private IntervalTreeNode build(int[] A, int start, int end) {
        if(start > end) {
            return null;
        }
        IntervalTreeNode node = new IntervalTreeNode(start, end);
        if(start == end) {
            node.sum = A[start];
            return node;
        }

        int middle = start + (end - start)/2;
        node.left = build(A, start, middle);
        node.right = build(A, middle + 1, end);
        node.sum += node.left.sum;
        node.sum += node.right.sum;
        return node;
    }

    /*
     * @param start: An integer
     * @param end: An integer
     * @return: The sum from start to end
     */
    public long query(int start, int end) {
        IntervalTreeNode node = root;
        return query(root, start, end);
    }

    private long query(IntervalTreeNode node, int start, int end) {
        if(start > node.end || end < node.start) {
            return 0;
        }
        if(start == node.start && end == node.end) {
            return node.sum;
        }

        int sum = 0;
        int middle = node.start + (node.end - node.start)/2;
        if(start <= middle) {
            sum += query(node.left, start, Math.min(middle, end));
        }
        if(end > middle) {
            sum += query(node.right, Math.max(start, middle + 1), end);
        }
        return sum;
    }

    /*
     * @param index: An integer
     * @param value: An integer
     * @return: nothing
     */
    public void modify(int index, int value) {
        // write your code here
        IntervalTreeNode node = root;
        modify(root, index, value);
    }

    private void modify(IntervalTreeNode node, int index, int value) {
        if(index < node.start || index > node.end) {
            return;
        }
        if(index == node.start && index == node.end) {
            node.sum = value;
            return;
        }

        int middle = node.start + (node.end - node.start)/2;
        if(index <= middle) {
            modify(node.left, index, value);
        } else {
            modify(node.right, index, value);
        }
        node.sum = node.left.sum + node.right.sum;
    }

}

class HeapRelated {

}

class SubSets_DFS {

}

class Permutation {

}



/**
 *  LeetCode 253 Meeting Rooms II
 *
 *  边缘case忘记了一个点的start和一个点的end重合时候的情形
 *  需要定义当相等出现时，优先级如何定义
 *
 *  扫描线问题： 可以遍历点的时候，使用容器存储点，在扫描过程中
 *             做出相应的计算，容器可以是set，map，heap等
 *             容器的选择取决于求解的结果是什么
 */
class SweepLine {
    public class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }

    class Node implements Comparable<Node> {
        int id;
        int val;
        int type;  //0:start, 1:end

        public Node(int id, int val, int type) {
            this.id = id;
            this.val = val;
            this.type = type;
        }

        public int compareTo(Node other) {
            if(this.val == other.val) {
                return other.type - this.type;
            }
            return this.val - other.val;
        }
    }

    public int minMeetingRooms(Interval[] intervals) {
        if(intervals == null || intervals.length == 0) {
            return 0;
        }

        int result = Integer.MIN_VALUE;
        Node[] nodes = init(intervals);
        Arrays.sort(nodes);

        Set<Integer> ids = new HashSet<>();
        for(int i = 0; i < nodes.length; ++i) {
            if(nodes[i].type == 0) {
                ids.add(nodes[i].id);
            } else {
                ids.remove(nodes[i].id);
            }
            result = Math.max(result, ids.size());
        }

        return result;
    }

    Node[] init(Interval[] intervals) {
        Node[] nodes = new Node[intervals.length * 2];
        int index = 0;
        for(int i = 0; i < intervals.length; ++i) {
            nodes[index++] = new Node(i, intervals[i].start, 0);
            nodes[index++] = new Node(i, intervals[i].end, 1);
        }
        return nodes;
    }
}

/**
 *  LintCode 67. Binary Tree Inorder Traversal
 */
class Binary_Tree_Inorder_Traversal {
    public void traverse(TreeNode node) {
        TreeNode cur = node;
        LinkedList<TreeNode> stack = new LinkedList<>();
        while(cur != null || !stack.isEmpty()) {
            if(cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                System.out.print(cur.val + " ");
                cur = cur.right;
            }
        }
    }
}

/**
 *  LintCode 66. Binary Tree Preorder Traversal
 *  option2: 每个节点分别压栈右节点和左节点，直至栈空
 */
class Binary_Tree_Preorder_Traversal {
    public void traverse(TreeNode node) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode cur = node;
        while(cur != null || !stack.isEmpty()) {
            if(cur != null) {
                System.out.print(cur.val + " ");
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                cur = cur.right;
            }
        }
    }
}


/**
 *  145. Binary Tree Postorder Traversal
 *  LintCode 68. Binary Tree Postorder Traversal
 */
class Binary_Tree_Postorder_Traversal {
    public void traverse(TreeNode node) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        stack.push(node);
        TreeNode prev = null;
        while(!stack.isEmpty()) {
            TreeNode cur = stack.peek();
            if(prev == null || prev.right == cur || prev.left == cur) {
                if(cur.left != null) {
                    stack.push(cur.left);
                } else if(cur.right != null) {
                    stack.push(cur.right);
                } else {
                    System.out.print(cur.val + " ");
                    stack.pop();
                }
            } else if(prev == cur.left) {
                if(cur.right != null) {
                    stack.push(cur.right);
                } else {
                    System.out.print(cur.val + " ");
                    stack.pop();
                }
            } else if(prev == cur.right) {
                System.out.print(cur.val + " ");
                stack.pop();
            }

            prev = cur;
        }
    }
}


class quickSelect {

    public int kthLargestElement(int k, int[] nums) {
        return findKthLargestElement(nums, 0, nums.length - 1, nums.length - k);
    }

    int findKthLargestElement(int[] nums, int start, int end, int target) {
        int left = start, right = end;
        int pivot = nums[start];
        int i = start;
        while(i <= right) {
            if(nums[i] == pivot) {
                i++;
            } else if(nums[i] < pivot) {
                swap(nums, i++, left++);
            } else {
                swap(nums, i, right--);
            }
        }
        if(target == left) {
            return nums[left];
        } else if(target < left) {
            return findKthLargestElement(nums, start, left - 1, target);
        } else {
            return findKthLargestElement(nums, left + 1, end, target);
        }
    }

    void swap(int[] nums, int index1, int index2) {
        int tmp = nums[index1];
        nums[index2] = nums[index1];
        nums[index1] = nums[index2];
    }
}


class quickSelect_others {


    /**
     * 外部pivot，左右相向指针实现，不能实现1Pivot3WayPartition
     *
     * 1. 循环内，有4处同时判断 left < right
     * 2. 循环外，对最后一个数据进行判断大于小于等于pivot
     *    解决：a. 头尾相遇，当前该值情况；
     *         b. pivot值超出当前数据集取值范围；
     */
    public int partitionArray(int[] nums, int k) {

        if(nums == null || nums.length == 0) {
            return 0;
        }

        int left = 0, right = nums.length - 1;
        while(left < right) {
            while(left < right && nums[left] < k) {
                left++;
            }
            while(left < right && nums[right] >= k) {
                right--;
            }
            if(left < right) {
                swap(nums, left++, right--);
            }
        }

        if(nums[left] < k) {
            return left + 1;
        }

        return left;
    }


    /**
     * 内部pivot，实现1Pivot2WayPartition 和 1pivot3WayPartition
     */
    public int kthLargestElement (int k, int[] nums) {
        if(nums == null || nums.length == 0 || k == 0 || k > nums.length) {
            return 0;
        }

        int start = 0, end = nums.length - 1;
        int target = nums.length - k;
        int index = -1;

        //这部分判断完全可以放到QuickSelect的逻辑中
        //需要将目标索引值作为参数传入，形成递归调用求解
        while(start <= end) {
            index = quickSelcet(nums, start, end);
            if(index == target) {
                return nums[index];
            } else if(target < index){
                end = index - 1;
            } else {
                start = index + 1;
            }
        }

        return -1;
    }

    int quickSelcet(int[] nums, int start, int end) {
        int pivot = nums[start];
        int left = start, equal = start, right = end;
        while(left < right) {
            while(left < right && nums[right] > pivot) {
                right--;
            }
            nums[left] = nums[right];
            while(left < right && nums[left] <= pivot) {
                if(nums[left] < pivot) {
                    swap(nums, equal++, left++);
                } else {
                    left++;
                }
            }
            nums[right] = nums[left];
        }
        nums[left] = pivot;
        return left;

        /*
        if(k == left) {
            return nums[left];
        } else if (k < left) {
            return findKthElement(nums, start, left - 1, k);
        } else {
            return findKthElement(nums, left + 1, end, k);
        }
        */
    }

    void swap(int[] nums, int index1, int index2) {
        int tmp = nums[index1];
        nums[index2] = nums[index1];
        nums[index1] = nums[index2];
    }
}
