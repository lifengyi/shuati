package Basic.BinaryTree;

import java.util.*;

/**
 * The left subtree of a node contains only nodes with keys lesser
 * than the node’s key.
 *
 * The right subtree of a node contains only nodes with keys greater
 * than the node’s key.
 *
 * The left and right subtree each must also be a binary search tree.
 *
 * There must be no duplicate nodes.
 *
 * The InOrder traversal of BST is Basic.sort ascending.
 *
 */

enum TraversalOrder {preOrder, inOrder, postOrder, levelOrder };

public class BinarySearchTree {

    TreeNode root = null;

    public BinarySearchTree(int[] array) {
        if(array == null)
            return;

        Arrays.sort(array);
        createBSTFromSortedArray(array);
    }

    public void createBSTFromSortedArray(int[] array) {
        if(array == null)
            return;

        root = getRootNode(0, array.length - 1, array);
    }

    private TreeNode getRootNode(int start, int end, int[] array) {
        if(start > end) {
            return null;
        }

        int mid = (start + end)/2;
        TreeNode newRoot = new TreeNode(array[mid]);
        newRoot.left = getRootNode(start, mid - 1, array);
        newRoot.right = getRootNode(mid + 1, end, array);
        return newRoot;
    }

    public void traversal(TraversalOrder orderType) {
        if(root == null)
            System.out.println("BST is empty.");

        if(orderType.equals(TraversalOrder.preOrder)) {
            System.out.println("Pre order: ");
            preOrder(root);
            System.out.print("\n");
        } else if(orderType.equals(TraversalOrder.levelOrder)) {
            System.out.println("Level order: ");
            levelOrder(root);
            System.out.print("\n");
        } else if(orderType.equals(TraversalOrder.inOrder)) {
            System.out.println("In order:");
            inOrder(root);
            System.out.print("\n");
        } else if(orderType.equals(TraversalOrder.postOrder)) {

        } else {
            System.out.println("Unsupported order type.");
        }
     }

    public static void preOrder(TreeNode node) {
        if(node == null)
            return;

        System.out.print(node.val + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    public static void inOrder(TreeNode node) {
        if(node == null)
            return;

        inOrder(node.left);
        System.out.print(node.val + " ");
        inOrder(node.right);
    }

    public static void levelOrder(TreeNode node) {
        if(node == null)
            return;

        LinkedList<TreeNode> fifo = new LinkedList<>();
        fifo.add(node);

        while(!fifo.isEmpty()) {
            TreeNode cur = fifo.removeFirst();
            System.out.print(cur.val + " ");
            if(cur.left != null)
                fifo.add(cur.left);
            if(cur.right != null)
                fifo.add(cur.right);
        }
    }

    /**
     * How to process the duplicated value?
     * Replace the old value or just ignore it?
     *
     * @param value
     */
    public void addNodeToBST(int value) {
        if(root == null) {
            root = new TreeNode(value);
        } else {
            TreeNode currentNode = root;
            while(true){
                if(value > currentNode.val){
                    if(currentNode.right != null) {
                        currentNode = currentNode.right;
                    } else {
                        currentNode.right = new TreeNode(value);
                        break;
                    }
                } else if(value < currentNode.val) {
                    if(currentNode.left != null){
                        currentNode = currentNode.left;
                    } else{
                        currentNode.left = new TreeNode(value);
                        break;
                    }
                } else {
                    //ignore it
                    break;
                }
            }
        }
    }


    /**
     * Will return false if the element doen't exist
     *
     * the deleted node
     *  1, if has both left and right children
     *          Try to find either predecessor or successor.
     *          For example, if use the successor:
     *              the successor is the leftest node in right chile tree
     *              if successor has right child:
     *                  successor's parent points to successor's right child
     *                  copy value of successor to deleted node
     *              if successor has no right child:
     *                  successor's parent points to null
     *  2, if only has left child:
     *          parent points to the left child, the left child replaces the deleted node
     *  3, if only has right child:
     *          parent points to the right child, the right child replaces the deleted node
     *  4, if has no child
     *          parent points to null;
     *
     * @param value
     * @return
     */
    public boolean removeNodeFromBST(int value){
        if(root == null)
            return false;

        if(root.val == value) {
            if(root.left != null && root.right != null) {
                removeNodeWithChildren(root);
            } else if(root.left != null) {
                root = root.left;
            } else if(root.right != null) {
                root = root.right;
            } else {
                root = null;
            }
            return true;
        } else {
            TreeNode previousNode = null;
            TreeNode currentNode = root;

            while(currentNode != null) {
                if(value > currentNode.val) {
                    previousNode = currentNode;
                    currentNode = currentNode.right;
                } else if(value < currentNode.val) {
                    previousNode = currentNode;
                    currentNode = currentNode.left;
                } else {
                    //find the node
                    if(currentNode.left != null && currentNode.right != null) {
                        removeNodeWithChildren(currentNode);
                    } else if(currentNode.left != null) {
                        replaceNode(previousNode, currentNode, currentNode.left);
                    } else if(currentNode.right != null) {
                        replaceNode(previousNode, currentNode, currentNode.right);
                    } else {
                        replaceNode(previousNode, currentNode, null);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Remove the node which has two child trees
     * 1. Find the node's successor and successor's parent
     * 2. Remove the successor from node's right tree
     * 3. Use successor's value to override the deleted node
     *
     */
    private void removeNodeWithChildren(TreeNode node) {
        //1. Find the successor and successor's parent
        TreeNode[] nodes = findSuccessorAndSuccessorParentNode(node);

        if(nodes != null && nodes.length == 2) {
            TreeNode successorNodeParent = nodes[0];
            TreeNode successorNode = nodes[1];

            //2. Remove the successor node
            if(successorNode.right != null) {
                replaceNode(successorNodeParent, successorNode, successorNode.right);
            } else {
                replaceNode(successorNodeParent, successorNode, null);
            }

            //3. Use the successor's value to override the deleted node's value
            node.val = successorNode.val;
        }
    }

    private TreeNode[] findSuccessorAndSuccessorParentNode(TreeNode node) {
        if(node == null || node.right == null)
            return null;

        TreeNode successorNodeParent = node;
        TreeNode successorNode = node.right;
        while(successorNode.left != null) {
            successorNodeParent = successorNode;
            successorNode = successorNode.left;
        }

        TreeNode[] nodes = {successorNodeParent, successorNode};
        return nodes;
    }

    private void replaceNode(TreeNode parent, TreeNode oldChild, TreeNode newChild) {
        if(oldChild == parent.right) {
            parent.right = newChild;
        } else {
            parent.left = newChild;
        }
    }




    public static void main(String[] args) {


        int[] array = {2, 5, 10, 4, 9, 32, 16, 64, 12};
        BinarySearchTree bst = new BinarySearchTree(array);

        bst.traversal(TraversalOrder.preOrder);
        bst.traversal(TraversalOrder.levelOrder);
        bst.traversal(TraversalOrder.inOrder);
        System.out.print("\n");

        bst.addNodeToBST(3);
        bst.addNodeToBST(8);
        bst.addNodeToBST(13);
        bst.addNodeToBST(14);
        bst.addNodeToBST(48);
        bst.traversal(TraversalOrder.preOrder);
        bst.traversal(TraversalOrder.levelOrder);
        bst.traversal(TraversalOrder.inOrder);
        System.out.print("\n");


        System.out.println("Remove 10");
        bst.removeNodeFromBST(10);
        bst.traversal(TraversalOrder.preOrder);
        bst.traversal(TraversalOrder.levelOrder);
        bst.traversal(TraversalOrder.inOrder);
        System.out.print("\n");


        System.out.println("Remove 13,14");
        bst.removeNodeFromBST(13);
        bst.removeNodeFromBST(14);
        bst.traversal(TraversalOrder.preOrder);
        bst.traversal(TraversalOrder.levelOrder);
        bst.traversal(TraversalOrder.inOrder);
        System.out.print("\n");

        System.out.println("Remove 12/16");
        //bst.removeNodeFromBST(12);
        bst.removeNodeFromBST(16);
        bst.traversal(TraversalOrder.preOrder);
        bst.traversal(TraversalOrder.levelOrder);
        bst.traversal(TraversalOrder.inOrder);
        System.out.print("\n");

    }

}
