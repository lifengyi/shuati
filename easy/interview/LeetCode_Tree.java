package com.stevenli.interview.easy.interview;

import com.stevenli.interview.algorithm.basic.BinaryTree.TreeLinkNode;
import com.stevenli.interview.algorithm.basic.BinaryTree.TreeNode;

import java.util.*;

public class LeetCode_Tree {
    public int findBottomLeftValue(TreeNode root) {
        if(root == null)
            return 0;

        int ret = -1;
        LinkedList<TreeNode> fifo = new LinkedList<>();
        fifo.add(root);

        while(true) {
            TreeNode currentNode = fifo.removeFirst();
            if(currentNode.right != null){
                fifo.add(currentNode.right);
            }
            if(currentNode.left != null) {
                fifo.add(currentNode.left);
            }
            if(fifo.isEmpty()) {
                ret = currentNode.val;
                break;
            }
        }
        return ret;
    }

    /**
     * TODO: 108 Convert Sorted Array to Binary Search LeetCode_Tree
     *
     * @param root
     * @return
     */
    public TreeNode convertBSTToGreatTree(TreeNode root) {
        if(root == null)
            return null;

        convert(root, 0);

        return root;
    }

    private int convert(TreeNode node, int value) {
        if(node == null)
            return 0;

        int rightValue = 0;
        int leftValue = 0;
        if(node.right != null) {
            rightValue = convert(node.right, 0);
        }

        node.val = node.val + value + rightValue;

        if(node.left != null) {
            leftValue = convert(node.left, node.val);
        } else {
            leftValue = node.val;
        }

        return leftValue;
    }


    /**
     * TODO: 94. Binary LeetCode_Tree Inorder Traversal
     *
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> nodeList = new ArrayList<>();
        if(root == null)
            return nodeList;

        traversal_v2(root, nodeList);

        return nodeList;
    }

    private void traversal_v2(TreeNode root, List<Integer> list) {
        if(root == null)
            return;

        TreeNode currentNode = root;
        while(currentNode != null) {
            if(currentNode.left == null) {
                //no left child tree
                list.add(currentNode.val);
                currentNode = currentNode.right;
            } else {
                //find predecessor in left child tree
                TreeNode predecessor = currentNode.left;
                while(predecessor.right != null && predecessor.right != currentNode)
                    predecessor = predecessor.right;

                //first time to access the current root
                if(predecessor.right == null) {
                    predecessor.right = currentNode;
                    currentNode = currentNode.left;
                } else {
                    //seconde time to access the current root
                    predecessor.right = null;
                    list.add(currentNode.val);
                    currentNode = currentNode.right;
                }
            }
        }
    }

    private void traversal(TreeNode root, List<Integer> list) {
        if(root == null)
            return;

        if(root.left != null) {
            traversal(root.left, list);
        }

        list.add(root.val);

        if(root.right != null) {
            traversal(root.right, list);
        }
    }


    /**
     * TODO: 101. Symmetric LeetCode_Tree
     *
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {
        if(root == null)
            return true;

        return isSymmetric(root.left, root.right);
    }

    private boolean isSymmetric_V2(TreeNode leftNode, TreeNode rightNode) {
        if(leftNode == null && rightNode == null)
            return true;
        if(leftNode == null || rightNode == null)
            return false;

        LinkedList<TreeNode> leftFifo = new LinkedList<>();
        LinkedList<TreeNode> rightFifo = new LinkedList<>();
        leftFifo.push(leftNode);
        rightFifo.push(rightNode);

        boolean isSymmetricFlag = true;
        while(!leftFifo.isEmpty() && !rightFifo.isEmpty()) {
            TreeNode left = leftFifo.removeFirst();
            TreeNode right = rightFifo.removeFirst();

            //check value
            if(left.val != right.val) {
                isSymmetricFlag = false;
                break;
            }

            //check structure
            if((left.left == null && right.right != null)
                    || (left.left != null && right.right == null)
                    || (left.right == null && right.left != null)
                    || (left.right != null && right.left == null)) {
                isSymmetricFlag = false;
                break;
            }

            if(left.right != null && right.left != null) {
                leftFifo.push(left.right);
                rightFifo.push(right.left);
            }
            if(left.left != null && right.right != null) {
                leftFifo.push(left.left);
                rightFifo.push(right.right);
            }
        }

        if(!leftFifo.isEmpty() || !rightFifo.isEmpty())
            isSymmetricFlag = false;

        return isSymmetricFlag;
    }

    private boolean isSymmetric(TreeNode left, TreeNode right) {
        if(left == null && right == null)
            return true;

        if((left != null && right == null) || (left == null && right != null))
            return false;

        if(left.val == right.val) {
            return isSymmetric(left.left, right.right) && isSymmetric(left.right, right.left);
        } else {
            return false;
        }
    }


    /**
     * TODO: 102. Binary LeetCode_Tree Level Order Traversal
     *
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if(root == null)
            return ret;

        LinkedList<TreeNode> fifo1 = new LinkedList<>();
        LinkedList<TreeNode> fifo2 = new LinkedList<>();
        fifo1.push(root);
        while(!fifo1.isEmpty() || !fifo2.isEmpty()) {
            saveElements(fifo1, fifo2, ret);
            saveElements(fifo2, fifo1, ret);
        }

        return ret;
    }

    private void saveElements(LinkedList<TreeNode> fifoIn, LinkedList<TreeNode> fifoOut, List<List<Integer>> list) {
        List<Integer> elements = new ArrayList<>();
        while(!fifoIn.isEmpty()){
            TreeNode tmp = fifoIn.removeFirst();
            elements.add(tmp.val);
            if(tmp.left != null)
                fifoOut.add(tmp.left);
            if(tmp.right != null)
                fifoOut.add(tmp.right);
        }
        if(!elements.isEmpty())
            list.add(elements);
    }

    public List<List<Integer>> levelOrder_v2(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if(root == null)
            return ret;

        LinkedList<TreeNode> fifo = new LinkedList<>();
        fifo.add(root);

        while(!fifo.isEmpty()) {
            List<Integer> elements = new ArrayList<>();
            int counter = fifo.size();
            while(counter != 0) {
                //get node
                TreeNode node = fifo.removeFirst();
                elements.add(node.val);
                if(node.left != null) {
                    fifo.add(node.left);
                }
                if(node.right != null) {
                    fifo.add(node.right);
                }
                counter--;
            }
            ret.add(elements);
        }

        return ret;
    }


    /**
     * TODO: 103. Binary LeetCode_Tree Zigzag Level Order Traversal
     *
     * @param root
     * @return
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if(root == null)
            return ret;

        LinkedList<TreeNode> fifo = new LinkedList<>();
        fifo.add(root);
        int level = 0;

        while(!fifo.isEmpty()) {
            int counter = fifo.size();
            List<Integer> nodes = new ArrayList<>();

            while(counter-- != 0) {
                TreeNode node = null;
                if(level%2 == 0) {
                    node = fifo.removeFirst();
                    nodes.add(node.val);
                    if(node.left != null)
                        fifo.add(node.left);
                    if(node.right != null)
                        fifo.add(node.right);
                } else {
                    node = fifo.removeLast();
                    nodes.add(node.val);
                    if(node.right != null)
                        fifo.push(node.right);
                    if(node.left != null)
                        fifo.push(node.left);
                }
            }
            ret.add(nodes);
            level++;
        }

        return ret;
    }


    /**
     * TODO: 105. Construct Binary LeetCode_Tree from Preorder and Inorder Traversal
     *
     */
    int preOrderIndex = 0;
    Map<Integer, Integer> inorderMap = new HashMap<>();

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        TreeNode root = getRoot(preorder, inorder, 0, inorder.length);
        return root;
    }

    private TreeNode getRoot(int[] preorder, int[] inorder, int begin, int end) {
        if(begin >= end || preOrderIndex >= preorder.length)
            return null;

        int currentRootValue = preorder[preOrderIndex++];

        /*
        int index = -1;
        for(index = begin; index < end; index++) {
            if(inorder[index] == currentRootValue)
                break;
        }*/
        int index = inorderMap.get(currentRootValue);

        /**
         *  If we use HashMap to cache the inorder array,
         *  then use it instead of iterating inorder array,
         *  it will reduce the time from 20ms to 5ms.
         */


        TreeNode root = new TreeNode(currentRootValue);
        root.left = getRoot(preorder, inorder, begin, index);
        root.right = getRoot(preorder, inorder, index + 1, end);

        return root;
    }


    /**
     * TODO: 108. Convert Sorted Array to Binary Search LeetCode_Tree
     *
     */
    public void connect(TreeLinkNode root) {
        if(root == null)
            return;

        LinkedList<TreeLinkNode> fifo = new LinkedList<>();
        fifo.add(root);

        while(!fifo.isEmpty()) {
            int num = fifo.size();
            while(num-- > 0) {
                TreeLinkNode node = fifo.removeFirst();
                if(num > 0) {
                    node.next = fifo.getFirst();
                }
                if(node.left != null)
                    fifo.add(node.left);
                if(node.right != null)
                    fifo.add(node.right);
            }
        }
    }

    public void connect_v2(TreeLinkNode root) {
        if(root == null)
            return;

        TreeLinkNode currentNode = root;
        while(currentNode != null) {
            TreeLinkNode firstNode = currentNode;
            while(firstNode != null) {
                if(firstNode.left != null) firstNode.left.next = firstNode.right;
                if(firstNode.right != null && firstNode.next != null) firstNode.right.next = firstNode.next.left;
                firstNode = firstNode.next;
            }
            currentNode = currentNode.left;
        }
    }


    /**
     * TODO: 124. Binary LeetCode_Tree Maximum Path Sum
     *
     */
    int maxPathSumValue = 0;
    boolean findValue = false;

    public int maxPathSum(TreeNode root) {
        caculateMaxPathSum(root);
        return maxPathSumValue;
    }

    public int caculateMaxPathSum(TreeNode root) {
        if(root == null)
            return 0;

        int rootValue = root.val;
        int leftValue = caculateMaxPathSum(root.left);
        int rightValue = caculateMaxPathSum(root.right);
        int returnValue = 0;

        if(leftValue < 0 && rightValue < 0) {
            returnValue = rootValue;
            if(findValue)
                maxPathSumValue = Math.max(maxPathSumValue, returnValue);
            else {
                maxPathSumValue = returnValue;
                findValue = false;
            }
        } else {
            returnValue = rootValue + Math.max(leftValue, rightValue);
            if(findValue)
                maxPathSumValue = Math.max(Math.max(returnValue, rootValue + rightValue + leftValue) , maxPathSumValue);
            else {
                maxPathSumValue = Math.max(returnValue, rootValue + rightValue + leftValue);
                findValue = false;
            }

        }

        return returnValue;
    }


    int maxPathSumValue_V2 = Integer.MIN_VALUE;

    public int maxPathSum_v2(TreeNode root) {
        caculateMaxPathSum_v2(root);
        return maxPathSumValue;
    }

    public int caculateMaxPathSum_v2(TreeNode root) {
        if(root == null)
            return 0;

        int rootValue = root.val;
        int leftValue = Math.max(0, maxPathSum_v2(root.left));
        int rightValue = Math.max(0, maxPathSum_v2(root.right));

        maxPathSumValue_V2 = Math.max(rootValue + rightValue + leftValue, maxPathSumValue_V2);

        return rootValue + Math.max(leftValue, rightValue);
    }


    /**
     * TODO: 230. Kth Smallest Element in a BST
     *
     */
    int kthSmallestCounter = 0;

    public int kthSmallest(TreeNode root, int k) {
        if(root.left != null) {
            int leftValue = kthSmallest(root.left, k);
            if(kthSmallestCounter == k)
                return leftValue;
        }

        kthSmallestCounter++;
        if(kthSmallestCounter == k)
            return root.val;

        if(root.right != null) {
            int rightValue = kthSmallest(root.right, k);
            if(kthSmallestCounter == k)
                return rightValue;
        }

        return root.val;
    }

    /**
     * TODO: 236. Lowest Common Ancestor of a Binary LeetCode_Tree
     *
     * v1, both of the two nodes must exist in binary tree.
     *
     * V2 failed to pass tests.
     *
     * @param root
     * @param p
     * @param q
     * @return
     */

    /**
     * Condition:
     * 1. Node p and q must exist in root tree
     * 2. LeetCode_Tree my have multiple nodes which have the same val
     *
     * Solution:
     * Compare the node address to find the ancestor
     *
     * Verify:
     * If p exists in tree and q doesn't exists in tree, it will return p.
     * This is wrong. So p and q must be in tree.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q)
            return root;

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if(left != null && right != null)
            return root;

        return left != null ? left : right;
    }


    /**
     * Condition:
     * 1. Node p and q may not exist in root tree.
     * 2. LeetCode_Tree shouldn't have nodes with the same value
     *
     * Solution:
     * Compare the node's value to find the ancestor
     */
    boolean isAncestorFound = false;
    public TreeNode lowestCommonAncestor_v2(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode ancestor = findAncestor(root, p, q);
        if(isAncestorFound)
            return ancestor;
        else
            return null;
    }

    public TreeNode findAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null)
            return null;

        TreeNode firstNode = null;

        if(root.val == p.val || root.val == q.val){
            firstNode = root;
        }

        TreeNode left = findAncestor(root.left, p, q);
        if(left != null) {
            if(isAncestorFound) {
                return left;
            } else {
                if(firstNode != null) {
                    isAncestorFound = true;
                    return root;
                } else {
                    firstNode = left;
                }
            }
        }

        TreeNode right = findAncestor(root.right, p, q);
        if(right != null) {
            if(isAncestorFound) {
                return right;
            } else {
                if(firstNode != null) {
                    isAncestorFound = true;
                    return root;
                } else {
                    firstNode = right;
                }
            }
        }

        return firstNode;
    }


    /**
     * TODO: 297. Serialize and Deserialize Binary LeetCode_Tree
     */

    private String NULL_STRING = "NULL";
    private String SPLITER = ",";

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if(root == null)
            return null;

        StringBuilder sb = new StringBuilder();
        buildString(root, sb);
        return sb.toString();
    }

    private void buildString(TreeNode node, StringBuilder sb){
        if(node == null) {
            sb.append(NULL_STRING).append(SPLITER);
        }
        else {
            sb.append(node.val).append(SPLITER);
            buildString(node.left, sb);
            buildString(node.right, sb);
        }
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if(data == null || data.trim().length() == 0)
            return null;

        LinkedList<String> fifo = new LinkedList<>();
        fifo.addAll(Arrays.asList(data.split(SPLITER)));
        return getRoot(fifo);
    }

    private TreeNode getRoot(LinkedList<String> fifo) {
        if(fifo.isEmpty())
            return null;

        String nodeValue = fifo.removeFirst();
        if(nodeValue.equals(NULL_STRING))
            return null;

        TreeNode root = new TreeNode(Integer.valueOf(nodeValue));
        root.left = getRoot(fifo);
        root.right = getRoot(fifo);

        return root;
    }


    /**
     * TODO: 98. Validate Binary Search LeetCode_Tree
     *
     * @param root
     * @return
     */
    public boolean isValidBST(TreeNode root) {
        return isValidBST(root, null, null);
    }

    private boolean isValidBST(TreeNode node, TreeNode min, TreeNode max) {
        if (node == null)
            return true;

        if (node.left == null && node.right == null)
            return true;

        boolean leftValue = false;
        boolean rightValue = false;

        if (node.left == null ||
                (node.left.val < node.val
                        && (min == null || node.left.val > min.val))) {
            leftValue = isValidBST(node.left, min, node);
            if (!leftValue)
                return false;
        }

        if (node.right == null ||
                (node.right.val > node.val
                        && (max == null || node.right.val < max.val))) {
            rightValue = isValidBST(node.right, node, max);
            if (!rightValue)
                return false;
        }

        return leftValue && rightValue;
    }

    public static void main(String[] args) {
        /*
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(-2);
        root.right = new TreeNode(13);
        root.left.left = new TreeNode(40);
        root.left.right = new TreeNode(-5);
        root.left.right.left = new TreeNode(18);
        root.left.right.right = new TreeNode(9);
        root.right.left = new TreeNode(12);
        root.right.right = new TreeNode(7);

        LeetCode_Tree tree = new LeetCode_Tree();
        int leftNodeVal = tree.findBottomLeftValue(root);
        System.out.println("The leftmost value in last row is " + leftNodeVal);

        System.out.println("Level order the original tree:");
        BinaryTreeTraversal.levelOrder_df(root);


        BinaryTreeRelated processor = new BinaryTreeRelated();
        processor.revertBinaryTreeToBST(root);
        System.out.println("Level order the original BST:");
        BinaryTreeTraversal.levelOrder_df(root);

        tree.convertBSTToGreatTree(root);
        System.out.println("Convert BST to great tree:");
        BinaryTreeTraversal.levelOrder_df(root);
        */


        /*
        TreeNode root = new TreeNode(37);
        root.left = new TreeNode(-34);
        root.left.right = new TreeNode(-100);
        root.right = new TreeNode(-48);
        root.right.left = new TreeNode(-100);
        root.right.right = new TreeNode(48);
        root.right.right.left = new TreeNode(-54);

        TreeNode testNode = new TreeNode(-71);
        root.right.right.left.left = testNode;
        root.right.right.left.right = new TreeNode(-22);
        root.right.right.left.right.right = new TreeNode(8);

        LeetCode_Tree tree = new LeetCode_Tree();

        TreeNode node = tree.lowestCommonAncestor(root, testNode, new TreeNode(-711));
        if(node !=null)
            System.out.println("node val = " + node.val);
        else
            System.out.println("return null.");
         */

        /*
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);

        LeetCode_Tree tree = new LeetCode_Tree();
        String out = tree.serialize(root);
        if(out==null || out.trim().length() == 0)
            System.out.println("string is empty.");
        else
            System.out.println("String = " + out);

        tree.deserialize(out);
        */
        /*
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(15);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(20);
        LeetCode_Tree tree = new LeetCode_Tree();
        if(tree.isValidBST(root)) {
            System.out.println("bst.");
        } else {
            System.out.println("not bst.");
        }
        */

        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(2);
        //root.right = new TreeNode(15);
        //root.right.left = new TreeNode(11);

    }
}
