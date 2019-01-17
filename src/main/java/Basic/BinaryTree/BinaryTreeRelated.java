package Basic.BinaryTree;

import java.util.*;

public class BinaryTreeRelated {

    /**
     * 中序遍历二叉树，输入到数组中
     * 将数组做升序排序，因为二叉搜索树的中序遍历为升序排序
     * 再次终须遍历二叉树，将数组中的元素写回二叉树中
     * 保证二叉树的形状不发生变化
     *
     * @param root
     */
    public void revertBinaryTreeToBST(TreeNode root){
        if(root == null)
            return;

        int numOfNodes = countNodes(root);
        int[] array = new int[numOfNodes];
        int[] count = {0};

        storeBtToArray(root, array, count);
        Arrays.sort(array);
        count[0] = 0;
        storeArrayToBt(array, root, count);
    }

    private void storeArrayToBt(int[] array, TreeNode node, int[] count) {
        if(node == null)
            return;

        storeArrayToBt(array, node.left, count);
        node.val = array[count[0]];
        count[0] += 1;
        storeArrayToBt(array, node.right, count);
    }

    private void storeBtToArray(TreeNode node, int[] array, int[] count) {
        if(node == null)
            return;

        storeBtToArray(node.left, array, count);
        array[count[0]] = node.val;
        count[0] += 1;
        storeBtToArray(node.right, array,count);
    }

    private int countNodes(TreeNode root) {
        if(root == null)
            return 0;

        return 1 + countNodes(root.left) + countNodes(root.right);
    }


    public int findTwoNodesMinmumPathInBST(int value1, int value2, TreeNode root)
            throws Exception {
        if(root == null) {
            throw new Exception("Binary tree is empty.");
        }

        int larger = Math.max(value1, value2);
        int smaller = Math.min(value1, value2);

        TreeNode currentNode = root;
        while(true) {
            if (currentNode.val > larger) {
                if (currentNode.left == null) {
                    throw new Exception("Cannot find the node 1.");
                } else {
                    currentNode = currentNode.left;
                }
            } else if (currentNode.val < smaller) {
                if (currentNode.right == null) {
                    throw new Exception("Cannot find the node 2.");
                } else {
                    currentNode = currentNode.right;
                }
            } else {
                int stepToSmaller = countStep(currentNode, smaller);
                int stepToLarger = countStep(currentNode, larger);
                return stepToSmaller + stepToLarger;
            }
        }
    }

    private int countStep(TreeNode node, int value) throws Exception {
        if(node.val == value) {
            return 0;
        } else if(node.val > value) {
            if(node.left == null) {
                throw new Exception("Cannot find the node 3.");
            } else {
                return countStep(node.left, value) + 1;
            }
        } else {
            if(node.right == null) {
                throw new Exception("Cannot find the node 4.");
            } else {
                return countStep(node.right, value) + 1;
            }
        }
    }

    public int findMinDeepthInBinaryTree(TreeNode node) {
        if(node == null || (node.left == null && node.right == null)) {
            return 0;
        }

        int minDepthOfLeft = getDepth(node.left);
        int minDepthOfRight = getDepth(node.right);

        return Math.min(minDepthOfLeft, minDepthOfRight);
    }

    public int getDepth(TreeNode node) {
        if(node == null)
            return 0;

        return 1 + Math.min(getDepth(node.left), getDepth(node.right));
    }

    /**
     * 求二叉树中的最大路径和
     */
    public int finaMaxPathSumInBinaryTree(TreeNode node) {
        if(node == null) {
            return 0;
        }

        Res res = new Res();
        countPathSum(node, res);
        return res.value;
    }

    public void mirrorBinaryTree(TreeNode node) {
        if(node == null)
            return;

        TreeNode tmp = node.left;
        node.left = node.right;
        node.right = tmp;

        mirrorBinaryTree(node.left);
        mirrorBinaryTree(node.right);
    }

    private int countPathSum(TreeNode node, Res res) {
        if(node == null)
            return 0;

        int sumOfTree, maxPathSum;
        int sumOfNode = node.val;
        int sumOfLeft = countPathSum(node.left, res);
        int sumOfRight = countPathSum(node.right, res);

        if(sumOfLeft < 0 && sumOfRight < 0) {
            sumOfTree = sumOfNode;
            maxPathSum = sumOfNode;
        } else {
            sumOfTree = Math.max(sumOfLeft, sumOfRight) + sumOfNode;
            maxPathSum = Math.max(sumOfTree, sumOfLeft + sumOfRight + sumOfNode);
        }

        res.value = Math.max(res.value, maxPathSum);
        return sumOfTree;
    }


    /**
     * Time complexity: O(n)
     * Space complexity: O(n)
     *
     * @param node
     */
    public TreeNode convertBinaryTreeToDLL(TreeNode node) {
        if(node ==  null)
            return null;

        TreeNode root;
        TreeNode leftRoot = null;
        TreeNode rightRoot = null;

        if(node.left != null)
            leftRoot = convertBinaryTreeToDLL(node.left);

        if(node.right != null)
            rightRoot = convertBinaryTreeToDLL(node.right);

        node.right = rightRoot;
        if(rightRoot != null) {
            rightRoot.left = node;
        }

        if(leftRoot != null) {
            leftRoot.right = node;
            node.left = leftRoot;
            root = leftRoot;
        } else {
            root = node;
        }

        return root;

    }

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

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(-2);
        root.right = new TreeNode(13);
        root.left.left = new TreeNode(40);
        root.left.right = new TreeNode(-5);
        root.left.right.left = new TreeNode(18);
        root.left.right.right = new TreeNode(9);
        root.right.left = new TreeNode(12);
        root.right.right = new TreeNode(7);

        BinaryTreeRelated processor = new BinaryTreeRelated();

        System.out.println("Level-order traversal the original tree: ");
        BinarySearchTree.levelOrder(root);
        System.out.print("\n");


        int sum = processor.finaMaxPathSumInBinaryTree(root);
        System.out.println("Find the maximum path sum: " + sum);

        int depth = processor.findMinDeepthInBinaryTree(root);
        System.out.println("Find the minimum depth sum: " + depth);

        processor.mirrorBinaryTree(root);
        System.out.println("Level-order traversal the mirror tree: ");
        BinarySearchTree.levelOrder(root);
        System.out.print("\n");
        System.out.println("Pre-order traversal the mirror tree: ");
        BinarySearchTree.preOrder(root);
        System.out.print("\n");


        processor.revertBinaryTreeToBST(root);
        System.out.println("Level traversal the bst: ");
        BinarySearchTree.levelOrder(root);
        System.out.print("\n");

        System.out.println("Check the double link : ");
        TreeNode first = processor.convertBinaryTreeToDLL(root);
        if(first != null) {
            TreeNode current = first;
            while(current != null) {
               String msg = String.format("Value: %s, left: %s, right: %s.",
                        String.valueOf(current.val),
                        current.left != null  ? current.left.val  : "null",
                        current.right != null ? current.right.val : "null");
               System.out.println(msg);
               current = current.right;
            }
        }


        /*
        try {
            int step  = processor.findTwoNodesMinmumPathInBST(7, 40, root);
            System.out.println("Minimum step is " + step);
        } catch (Exception e) {
            System.out.println("Failed to find path. Reason:" + e.getMessage());
        }
        */


    }
}
