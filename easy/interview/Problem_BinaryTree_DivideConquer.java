package interview;

import java.util.*;

public class Problem_BinaryTree_DivideConquer {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(1);
        root.right = new TreeNode(1);

        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(-1);

        root.left.left.right = new TreeNode(2);
        root.left.right.left = new TreeNode(3);
        root.left.right.left.left = new TreeNode(-1);

        LintCode_Binary_Tree_Path_Sum_II proc = new LintCode_Binary_Tree_Path_Sum_II();
        List<List<Integer>> ret = proc.binaryTreePathSum(root, 5);
        System.out.println(ret.toString());
    }
}


class LintCode_L97_Maximum_Depth_of_Binary_Tree {
    public int maxDepth(TreeNode root) {
        if(root == null) {
            return 0;
        }

        return getDepth(root);
    }

    int getDepth(TreeNode root) {
        if(root == null) {
            return 0;
        }

        int leftDepth = getDepth(root.left);
        int rightDepth = getDepth(root.right);

        return Math.max(leftDepth, rightDepth) + 1;
    }
}


class LintCode_L480_Binary_Tree_Paths {
    public List<String> binaryTreePaths(TreeNode root) {
        // write your code here
        List<String> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        result.addAll(getBinaryTreePaths(root));
        return result;
    }

    List<String> getBinaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        if(root.left == null && root.right == null) {
            result.add(root.val + "");
            return result;
        }

        List<String> leftPaths = getBinaryTreePaths(root.left);
        List<String> rightPaths = getBinaryTreePaths(root.right);
        if(leftPaths.size() != 0) {
            for(String path : leftPaths) {
                result.add(root.val + "->" + path);
            }
        }
        if(rightPaths.size() != 0) {
            for(String path : rightPaths) {
                result.add(root.val + "->" + path);
            }
        }

        return result;
    }
}


class LintCode_L94_Binary_Tree_Maximum_Path_Sum {
    int maxSum = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        // write your code here
        if(root == null) {
            return 0;
        }

        getMaxPathSum(root);
        return maxSum;
    }

    int getMaxPathSum(TreeNode node) {
        if(node == null) {
            return 0;
        }

        int left = Math.max(getMaxPathSum(node.left), 0);
        int right = Math.max(getMaxPathSum(node.right), 0);
        maxSum = Math.max(maxSum, node.val + left + right);
        return node.val + Math.max(left, right);
    }
}

class LintCode_L475_Binary_Tree_maximum_Path_Sum_II {
    public int maxPathSum2(TreeNode root) {
        // write your code here
        if(root == null) {
            return 0;
        }

        int left = Math.max(0, maxPathSum2(root.left));
        int right = Math.max(0, maxPathSum2(root.right));
        return root.val + Math.max(left, right);
    }
}



class LintCode_L93_Balanced_Binary_Tree {
    class ResultType {
        public int height;
        public boolean isBalanced;
        public ResultType(int height, boolean isBalanced) {
            this.height = height;
            this.isBalanced = isBalanced;
        }
    }

    public boolean isBalanced(TreeNode root) {
        // write your code here
        if(root == null) {
            return true;
        }

        ResultType result = validate(root);
        return result.isBalanced;
    }

    ResultType validate(TreeNode node) {
        ResultType result = new ResultType(0, false);
        if(node == null) {
            result.isBalanced = true;
            return result;
        }

        ResultType left = validate(node.left);
        ResultType right = validate(node.right);
        if(left.isBalanced && right.isBalanced && Math.abs(left.height - right.height) <= 1) {
            result.isBalanced = true;
        }
        result.height = Math.max(left.height, right.height) + 1;
        return result;
    }
}


class LintCode_L453_Flatten_Binary_Tree_to_Linked_List {
    class ResultType {
        TreeNode head;
        TreeNode tail;
        public ResultType(TreeNode head, TreeNode tail){
            this.head = head;
            this.tail = tail;
        }
    }
    public void flatten(TreeNode root) {
        // write your code here
        if(root == null) {
            return;
        }

        flattenTree(root);
    }

    ResultType flattenTree(TreeNode node) {
        if(node == null) {
            return null;
        }
        ResultType result = new ResultType(node, node);

        ResultType left = flattenTree(node.left);
        ResultType right = flattenTree(node.right);
        if(left != null) {
            result.tail.right = left.head;
            result.tail = left.tail;
            node.left = null;
        }
        if(right != null) {
            result.tail.right = right.head;
            result.tail = right.tail;
        }
        return result;
    }
}


/**
 *  Node A and B must be in the tree
 */
class LintCode_L88_Lowest_Common_Ancestor_of_a_Binary_Tree {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode A, TreeNode B) {
        // write your code here
        if(root == null || root == A || root == B) {
            return root;
        }

        TreeNode left = lowestCommonAncestor(root.left, A, B);
        TreeNode right = lowestCommonAncestor(root.right, A, B);
        if(left != null && right != null) {
            return root;
        } else if(left != null) {
            return left;
        } else if(right != null) {
            return right;
        } else {
            return null;
        }
    }
}


class LintCode_L474_Lowest_Common_Ancestor_of_a_Binary_Tree_II {
    class ParentTreeNode {
        public ParentTreeNode parent, left, right;
    }

    public ParentTreeNode lowestCommonAncestorII(ParentTreeNode root, ParentTreeNode A, ParentTreeNode B) {
        // write your code here
        if(root == null || A == null || B == null) {
            return null;
        }

        List<ParentTreeNode> aTrace = traceBack(root, A);
        List<ParentTreeNode> bTrace = traceBack(root, B);
        int aIndex = aTrace.size() - 1;
        int bIndex = bTrace.size() - 1;
        while(aIndex >= 0 && bIndex >= 0) {
            if(aTrace.get(aIndex) != bTrace.get(bIndex)) {
                break;
            }
            aIndex--;
            bIndex--;
        }

        return aTrace.get(aIndex + 1);
    }

    List<ParentTreeNode> traceBack(ParentTreeNode root, ParentTreeNode node) {
        List<ParentTreeNode> trace = new ArrayList<>();
        ParentTreeNode tmp = node;
        while(node != root) {
            trace.add(node);
            node = node.parent;
        }
        trace.add(root);
        return trace;
    }
}

class LintCode_L578_Lowest_Common_Ancestor_III {
    class ResultType {
        TreeNode node;
        int number;
        public ResultType() {
            node = null;
            number = 0;
        }
    }
    public TreeNode lowestCommonAncestor3(TreeNode root, TreeNode A, TreeNode B) {
        // write your code here
        if(root == null || A == null || B == null) {
            return null;
        }

        ResultType result = lca(root, A, B);
        if(result.number == 2 && result.node != null) {
            return result.node;
        }

        return null;
    }

    ResultType lca(TreeNode node, TreeNode A, TreeNode B) {

        ResultType result = new ResultType();
        if(node == null) {
            return result;
        }

        ResultType left = lca(node.left, A, B);
        ResultType right = lca(node.right, A, B);
        if(left.node != null && right.node != null) {
            result.node = node;
            result.number = 2;
        } else {
            if(left.node != null) {
                result.node = left.node;
                result.number += left.number;
            }
            if(right.node != null) {
                result.node = right.node;
                result.number += right.number;
            }
            if(node == A) {         // A and B may be the same
                result.node = node;
                result.number += 1;
            }
            if(node == B) {         // A and B may be the same
                result.node = node;
                result.number += 1;
            }
        }

        return result;
    }
}


class LintCode_L595_Binary_Tree_Longest_Consecutive_Sequence {
    int max = 0;
    public int longestConsecutive(TreeNode root) {
        // write your code here
        if(root == null) {
            return max;
        }

        searchLongestConsecutive(root);
        return max;
    }

    int searchLongestConsecutive(TreeNode node) {
        if(node == null) {
            return 0;
        }

        int leftLength = searchLongestConsecutive(node.left);
        int rightLength = searchLongestConsecutive(node.right);
        leftLength = (leftLength == 0 || node.val != node.left.val - 1) ? 1 : leftLength + 1;
        rightLength = (rightLength == 0 || node.val != node.right.val - 1) ? 1 : rightLength + 1;

        int result = Math.max(leftLength, rightLength);
        max = Math.max(max, result);
        return result;
    }
}

class LintCode_L614_Binary_Tree_Longest_Consecutive_Sequence_II {
    class ResultType {
        int upLen;
        int downLen;
        public ResultType() {
            this.upLen = 0;
            this.downLen = 0;
        }
    }

    int max = 0;
    public int longestConsecutive2(TreeNode root) {
        // write your code here
        if(root == null) {
            return 0;
        }

        searchLongestConsecutive2(root);
        return max;
    }

    ResultType searchLongestConsecutive2(TreeNode node) {
        if(node == null) {
            return null;
        }

        ResultType result = new ResultType();
        if(node.left == null && node.right == null) {
            result.upLen = 1;
            result.downLen = 1;
            max = Math.max(max, 1);
            return result;
        }

        ResultType leftResult = searchLongestConsecutive2(node.left);
        ResultType rightResult = searchLongestConsecutive2(node.right);
        int leftUpLen = 1, leftDownLen = 1, rightUpLen = 1, rightDownLen = 1;
        if(leftResult != null) {
            leftUpLen = (node.val == node.left.val - 1) ? leftResult.upLen + 1 : 1;
            leftDownLen = (node.val == node.left.val + 1) ? leftResult.downLen + 1 : 1;
            result.upLen = Math.max(result.upLen, leftUpLen);
            result.downLen = Math.max(result.downLen, leftDownLen);
        }
        if(rightResult != null) {
            rightUpLen = (node.val == node.right.val - 1) ? rightResult.upLen + 1 : 1;
            rightDownLen = (node.val == node.right.val + 1) ? rightResult.downLen + 1 : 1;
            result.upLen = Math.max(result.upLen, rightUpLen);
            result.downLen = Math.max(result.downLen, rightDownLen);
        }

        max = Math.max(max, Math.max(leftUpLen + rightDownLen - 1, leftDownLen + rightUpLen - 1));
        return result;
    }
}



class LintCode_L376_Binary_Tree_Path_Sum {
    public List<List<Integer>> binaryTreePathSum(TreeNode root, int target) {
        // write your code here
        List<List<Integer>> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        List<Integer> path = new ArrayList<>();
        path.add(root.val);
        dfs(root, target - root.val, path, result);
        return result;
    }

    void dfs(TreeNode node, int target, List<Integer> path, List<List<Integer>> result) {
        if(node.left == null && node.right == null) {
            if(target == 0) {
                result.add(new ArrayList(path));
            }
            return;
        }

        if(node.left != null) {
            path.add(node.left.val);
            dfs(node.left, target - node.left.val, path, result);
            path.remove(path.size() - 1);
        }
        if(node.right != null) {
            path.add(node.right.val);
            dfs(node.right, target - node.right.val, path, result);
            path.remove(path.size() - 1);
        }
    }
}


/**
 *  写法2:   将临时数据的维护操作放到了dfs函数中的开始和结尾，
 *          原来临时数据集的维护操作都是在进入dfs之前做的，
 *
 *          注意区别就是后者情况下：dfs函数中任何退出的地方都要
 *          记得动态维护数据集，否则就会出错，所以尽量保持只有一个
 *          退出函数的地方
 */
class LintCode_L376_Binary_Tree_Path_Sum_CodeStyle_II {
    public List<List<Integer>> binaryTreePathSum(TreeNode root, int target) {
        // write your code here
        List<List<Integer>> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        List<Integer> path = new ArrayList<>();
        dfs(root, target, path, result);
        return result;
    }

    void dfs(TreeNode node, int target, List<Integer> path, List<List<Integer>> result) {
        if(node == null) {
            return;
        }

        path.add(node.val);
        target -= node.val;

        if(node.left == null && node.right == null) {
            if(target == 0) {
                result.add(new ArrayList(path));
                // Do not return
                // If return, please remove node from path
            }
        }

        dfs(node.left, target, path, result);
        dfs(node.right, target, path, result);

        path.remove(path.size() - 1);
    }
}


/**
 *  层级节点之间sum满足和为K
 */
class LintCode_Binary_Tree_Path_Sum_II {
    public List<List<Integer>> binaryTreePathSum(TreeNode root, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        List<Integer> buffer = new ArrayList<>();
        buffer.add(root.val);
        dfs(root, target, buffer, result);

        return result;
    }

    void dfs(TreeNode node, int target, List<Integer> buffer, List<List<Integer>> result) {

        validate(target, buffer, result);

        if(node.left != null) {
            buffer.add(node.left.val);
            dfs(node.left, target, buffer, result);
            buffer.remove(buffer.size() - 1);
        }
        if(node.right != null) {
            buffer.add(node.right.val);
            dfs(node.right, target, buffer, result);
            buffer.remove(buffer.size() - 1);
        }
    }

    void validate(int target, List<Integer> buffer, List<List<Integer>> result) {
        int size = buffer.size();
        for(int i = size - 1; i >= 0; --i) {
            target -= buffer.get(i);
            if (target == 0) {
                flush(i, size, buffer, result);
            }
        }
    }

    void flush(int start, int end, List<Integer> buffer, List<List<Integer>>result) {
        List<Integer> tmp = new ArrayList<>();
        for(int i = start; i < end; ++i) {
            tmp.add(buffer.get(i));
        }
        result.add(tmp);
    }
}


/**
 *  任意两个节点之间sum为K
 *
 *  以每个节点为新的根节点，查找以当前新的根节点为起始位置
 */
class LintCode_Binary_Tree_Path_Sum_III {
    public List<List<Integer>> binaryTreePathSum(TreeNode root, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        dfs(root, null, target, result);

        return result;
    }

    void dfs(TreeNode node, TreeNode father, int target, List<List<Integer>> result) {

        // Make the current node as a new root node and
        // search if sum equals K. A valid path is from
        // the new root node to any other node (left, right, parent)
        List<Integer> path = new ArrayList<>();
        findSum(node, null, target - node.val, path, result);

        if(node.left != null) {
            dfs(node.left, father, target, result);
        }
        if(node.right != null) {
            dfs(node.right, father, target, result);
        }
    }

    void findSum(TreeNode node, TreeNode comeFrom, int target,
                 List<Integer> path, List<List<Integer>> result) {

        /**
         *  1. Simplify the solution to find the sum equals K
         *     A valid path is from root to any node.
         *
         *  2. Precondition: Should change TreeNode to ParentTreeNode
         *
         *  3. Code:
         *
         *      if(node == null) {
         *          return;
         *      }
         *
         *      path.add(node.val);
         *
         *      if(comeFrom != node.parent) {
         *          findSum(node.parent, false, target - currentNodeValue, path, result);
         *      }
         *
         *      if(comeFrom != node.left) {
         *          findSum(node.left, false, target - currentNodeValue, path, result);
         *      }
         *
         *      if(comeFrom != node.right) {
         *          findSum(node.right, false, target - currentNodeValue, path, result);
         *      }
         *
         *      path.remove(path.size() - 1);
         *
         */
    }
}


class LintCode_L95_Validate_Binary_Search_Tree {
    class ReturnType {
        int max;
        int min;
        boolean isBST;
        public ReturnType(int max, int min, boolean isBST) {
            this.max = max;
            this.min = min;
            this.isBST = isBST;
        }
    }

    public boolean isValidBST(TreeNode root) {
        // write your code here
        if(root == null) {
            return true;
        }

        ReturnType result = validateBST(root);
        return result.isBST;
    }

    ReturnType validateBST(TreeNode node){
        if(node == null) {
            return null;
        }

        ReturnType result = new ReturnType(node.val, node.val, false);
        if(node.left == null && node.right == null) {
            result.isBST = true;
            return result;
        }

        ReturnType left = validateBST(node.left);
        ReturnType right = validateBST(node.right);
        if(left != null && (!left.isBST || node.val <= left.max)) {
            result.isBST = false;
            return result;
        }
        if(right != null && (!right.isBST || node.val >= right.min)) {
            result.isBST = false;
            return result;
        }

        //return true
        if(left != null) {
            result.min = Math.min(result.min, left.min);
        }
        if(right != null) {
            result.max = Math.max(result.max, right.max);
        }
        result.isBST = true;
        return result;
    }
}


class LintCode_L378_Convert_Binary_Search_Tree_to_Doubly_Linked_List {
    public class DoublyListNode {
        int val;
        DoublyListNode next, prev;
        DoublyListNode(int val) {
            this.val = val;
            this.next = this.prev = null;
        }
    }

    class ReturnType {
        DoublyListNode head;
        DoublyListNode tail;
        public ReturnType (DoublyListNode head, DoublyListNode tail) {
            this.head = head;
            this.tail = tail;
        }
    }

    public DoublyListNode bstToDoublyList(TreeNode root) {
        // write your code here
        if(root == null) {
            return null;
        }

        ReturnType result = revert(root);
        return result.head;
    }

    ReturnType revert(TreeNode node) {
        if(node == null) {
            return null;
        }

        DoublyListNode currentNode = new DoublyListNode(node.val);
        ReturnType result = new ReturnType(currentNode, currentNode);
        if(node.left == null && node.right == null) {
            return result;
        }

        ReturnType left = revert(node.left);
        ReturnType right = revert(node.right);
        if(left != null) {
            result.head.prev = left.tail;
            left.tail.next = result.head;
            result.head = left.head;
        }
        if(right != null) {
            result.tail.next = right.head;
            right.head.prev = result.tail;
            result.tail = right.tail;
        }

        return result;
    }
}


/**
 *  二叉搜索树的顶点只是中间值，不能以此作为是否继续遍历下去的依据
 */
class LintCode_L11_Search_Range_in_Binary_Search_Tree {
    public List<Integer> searchRange(TreeNode root, int k1, int k2) {
        // write your code here
        List<Integer> result = new ArrayList<>();
        if(root == null || k1 > k2) {
            return result;
        }

        if(root.val >= k1) {
            List<Integer> left = searchRange(root.left, k1, k2);
            for(int val : left) {
                result.add(val);
            }
        }

        if(root.val >= k1 && root.val <= k2) {
            result.add(root.val);
        }

        if(root.val <= k2) {
            List<Integer> right = searchRange(root.right, k1, k2);
            for(int val : right) {
                result.add(val);
            }
        }

        return result;
    }
}


class LintCode_L245_Subtree {
    public boolean isSubtree(TreeNode T1, TreeNode T2) {
        // write your code here
        if(T1 == null) {
            return false;
        }
        if(T2 == null) {
            return true;
        }

        if(isEqual(T1, T2)) {
            return true;
        }

        if(isSubtree(T1.left, T2)) {
            return true;
        }
        if(isSubtree(T1.right, T2)) {
            return true;
        }

        return false;
    }

    boolean isEqual(TreeNode node1, TreeNode node2) {
        if(node1 == null && node2 == null) {
            return true;
        } else if (node1 == null || node2 == null || node1.val != node2.val) {
            return false;
        }

        return true;
    }
}


class LIntCode_L1360_Symmetric_Tree {
    public boolean isSymmetric(TreeNode root) {
        if(root == null) {
            return true;
        }

        return compare(root.left, root.right);
    }

    boolean compare(TreeNode node1, TreeNode node2) {
        if(node1 == null && node2 == null) {
            return true;
        } else if(node1 == null || node2 == null || node1.val != node2.val) {
            return false;
        }

        boolean ret1 = compare(node1.left, node2.right);
        boolean ret2 = compare(node1.right, node2.left);
        if(ret1 && ret2) {
            return true;
        }

        return false;
    }
}


class LintCode_L73_Construct_binary_Tree_from_Preorder_and_Inorder_Traversal {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // write your code here
        if(preorder == null || inorder == null
                || preorder.length == 0 || preorder.length != inorder.length) {
            return null;
        }

        return build(inorder, 0, inorder.length - 1, preorder, 0);
    }

    TreeNode build(int[] inorder, int start, int end, int[] preorder, int rootIndex) {
        if(start > end) {
            return null;
        }

        int rootValue = preorder[rootIndex];
        TreeNode root = new TreeNode(rootValue);

        if(start == end) {
            return root;
        }

        int index = getRootIndex(inorder, start, end, rootValue);
        if(index != -1) {
            TreeNode left = build(inorder, start, index - 1, preorder, rootIndex + 1);
            TreeNode right = build(inorder, index + 1, end, preorder, rootIndex + index - start + 1);
            root.left = left;
            root.right = right;
        }
        return root;
    }

    int getRootIndex(int[] inorder, int start, int end, int rootValue) {
        for(int i = start; i <= end; ++i) {
            if(inorder[i] == rootValue) {
                return i;
            }
        }
        return -1;
    }
}
