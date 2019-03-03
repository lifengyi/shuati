package Basic.BinaryTree;

import java.util.*;

/**
 * 1. # of leaves in h level:  n = 2 to the power of (h - 1)
 * 2. # of nodes of a h level tree:  N = (2 to the power of h) - 1
 * 3. 1 => 3, # of leaves is n, then the the minimal high is : h >= logN + 1
 * 4. 2 => 4, # of nodes is N, then the the minimal high is : h >= log(N+1)
 * 5, n0: the number of leaves, n2: the node which has two children
 *    n0 = n2 + 1
 *
 *  Full Binary LeetCode_Tree: All the node has 0 or 2 leaves.
 *                   L = t + 1;
 *                   L : leaves
 *                   t : internal nodes
 *
 *  Complete Binary LeetCode_Tree: All levels are completely filled except possibly
 *                        the last level and the last level has all leaves
 *                        as left as possible.
 *                        Usage:
 *                        1. Binary Heap (min heap or max heap), can be
 *                        used to resolve following issues:
 *                          a) Dijkstra’s Shortest Path
 *                          b) Prim’s Minimum Spanning LeetCode_Tree.
 *                          c) K’th Largest Element in an array.
 *                          d) Sort an almost sorted array
 *                          e) Merge K Sorted Arrays.
 *
 *  Balance Binary LeetCode_Tree: A binary tree is balanced when the high of the tree
 *                      is logN, and N is the number of the nodes.
 *                      Usage: AVL tree
 *
 */


public class BinaryTreeTraversal {

    /**
     *  二叉树遍历性能分析：
     *  1. 递归遍历：时间复杂度为 O(n)， 递归算法的空间复杂度为：递归深度N * 每次递归的
     *              空间复杂度（1） = O(n)
     *  2. 非递归遍历：时间复杂度为O(n), 空间复杂度为树的深度，故也为O(n)
     *  3. Morris遍历： 时间复杂度为O(n)， 空间复杂度为O(1)
     */


    /**
     * Morris Traversal, based on Threaded Binary LeetCode_Tree
     *
     * 1. 如果当前节点的左子树为空，则输出当前节点并将其右孩子作为当前节点。
     * 2. 如果当前节点的左子树不为空，在左子树中找最右的叶节点，即当前节点的 predecessor。
     *     a) 如果前驱节点的右孩子为空:
     *          i)   将它的右孩子设置为当前节点
     *          ii)  当前节点更新为当前节点的左孩子
     *     b) 如果前驱节点的右孩子为当前节点:
     *          i)   将它的右孩子重新设为空（恢复树的形状）
     *          ii)  输出当前节点
     *          iii) 当前节点更新为当前节点的右孩子。
     * 3. 重复以上1、2直到当前节点为空。
     *
     */
    public void inOrder_Morris(TreeNode node) {
        if(node == null)
            return;

        TreeNode cur = node;
        while(cur != null) {
            if(cur.left == null) {
                System.out.print(cur.val + " ");
                cur = cur.right;
            } else {
                TreeNode tmp = cur.left;
                while(tmp.right != null && tmp.right != cur) {
                    tmp = tmp.right;
                }

                if(tmp.right == null){
                    tmp.right = cur;
                    cur = cur.left;
                } else if(tmp.right == cur) {
                    tmp.right = null;
                    System.out.print(cur.val + " ");
                    cur = cur.right;
                }
            }
        }
    }

    public void preOrder_Morris(TreeNode node) {
        if(node == null)
            return;

        TreeNode cur = node;
        while(cur != null) {
            //shouldn't visit node here
            //System.out.print(cur.value + " ");
            if(cur.left == null) {
                System.out.print(cur.val + " ");
                cur = cur.right;
            } else {
                TreeNode tmp = cur.left;
                while(tmp.right != null && tmp.right != cur) {
                    tmp = tmp.right;
                }

                if(tmp.right == null) {
                    tmp.right = cur;
                    System.out.print(cur.val + " ");
                    cur = cur.left;
                } else if(tmp.right == cur) {
                    tmp.right = null;
                    cur = cur.right;
                }
            }
        }
    }





    public void postOrder_Morris(TreeNode node) {
        if(node == null)
            return;

        TreeNode dummy = new TreeNode(0);
        dummy.left = node;
        TreeNode cur = dummy;
        while(cur != null){
            if(cur.left == null) {
                cur = cur.right;
            } else {
                TreeNode preNode = cur.left;
                while(preNode.right != null && preNode.right != cur) {
                    preNode = preNode.right;
                }

                if(preNode.right == null) {
                    preNode.right = cur;
                    cur = cur.left;
                } else {
                    /**
                     * This the only place where we print all nodes
                     * Reverse-output all the nodes from current node's
                     * left child to current node's predecessor.
                     */
                    reversePrint(cur.left, preNode, cur);
                    preNode.right = null;
                    cur = cur.right;
                }
            }
        }
    }

    private void reversePrint(TreeNode from, TreeNode to, TreeNode currentNode) {
        if(from == to) {
            System.out.print(from.val + " ");
        } else {
            reverse(from, to, currentNode);
            TreeNode cur = to;
            while(true) {
                System.out.print(cur.val + " ");
                //check this before we move cur in case of from==to
                //this case occurs while printing the left leave
                if(cur == from)
                    break;
                cur = cur.right;
            }
            reverse(to, from, currentNode);
        }
    }

    private void reverse(TreeNode from, TreeNode to, TreeNode currentNode) {
        TreeNode first = from;
        TreeNode second = from.right;
        TreeNode third;
        while(first != to) {
            third = second.right;
            second.right = first;
            first = second;
            second = third;
        }

        from.right = currentNode;
    }

    public void preOrder_df_stack(TreeNode node) {
        if(node == null)
            return;

        Stack<TreeNode> stack = new Stack<>();
        TreeNode p = node;
        while(p != null || !stack.empty()) {
            if(p != null) {
                System.out.print(p.val + " ");
                stack.push(p);
                p = p.left;
            } else if (!stack.empty()) {
                TreeNode top = stack.pop();
                p = top.right;
            }
        }
    }

    public void preOrder_df_stack_V2(TreeNode node) {
        if(node == null)
            return;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(node);

        while(!stack.isEmpty()){
            TreeNode first = stack.pop();
            System.out.print(first.val + " ");
            if(first.right != null)
                stack.push(first.right);
            if(first.left != null)
                stack.push(first.left);
        }
    }

    public void inOrder_df_stack(TreeNode node) {
        if(node == null)
            return;

        Stack<TreeNode> stack = new Stack<>();
        TreeNode p = node;
        while(p != null || !stack.isEmpty()) {
            if(p != null){
                stack.push(p);
                p = p.left;
            } else if(!stack.isEmpty()) {
                TreeNode top = stack.pop();
                System.out.print(top.val + " ");
                p = top.right;
            }
        }
    }



    public  void postOrder_df_stack(TreeNode node) {
        if(node == null)
            return;

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

    public void postOrder_df_stack_v2(TreeNode node) {
        if(node == null)
            return;

        Stack<TreeNode> stack = new Stack<>();
        TreeNode p = node;

        while(p != null || !stack.isEmpty()) {
            if(p != null) {
                p.tag = Tag.left;
                stack.push(p);
                p = p.left;
            } else if(!stack.isEmpty()) {
                TreeNode top = stack.pop();
                if(top.tag == Tag.right) {
                    System.out.print(top.val + " ");
                } else {
                    top.tag = Tag.right;
                    stack.push(top);
                    p = top.right;
                }
            }
        }

    }

    public void preOrder_df_recursion(TreeNode node) {
        if(node == null)
            return;

        System.out.print(node.val + " ");
        if(node.left != null) {
            preOrder_df_recursion(node.left);
        }
        if(node.right != null) {
            preOrder_df_recursion(node.right);
        }
    }

    public void inOrder_df_recursion(TreeNode node) {
        if(node == null)
            return;

        if(node.left != null) {
            inOrder_df_recursion(node.left);
        }

        System.out.print(node.val + " ");

        if(node.right != null) {
            inOrder_df_recursion(node.right);
        }

    }

    public void postOrder_df_recursion(TreeNode node){
        if(node == null)
            return;

        if(node.left != null)
            postOrder_df_recursion(node.left);
        if(node.right != null)
            postOrder_df_recursion(node.right);

        System.out.print(node.val + " ");
    }


    public static void levelOrder_df(TreeNode node) {
        LinkedList<TreeNode> fifo = new LinkedList<>();
        fifo.add(node);

        while(!fifo.isEmpty()) {
            TreeNode first = fifo.removeFirst();
            System.out.print(first.val + " ");
            if(first.left != null)
                fifo.add(first.left);
            if(first.right != null)
                fifo.add(first.right);
        }
        System.out.print("\n");
    }

    public void test(TreeNode node) {
        if(node == null)
            return;

        Stack<TreeNode> stack = new Stack<>();

        TreeNode p = node;
        TreeNode lastVisitNode = null;
        while(p != null || !stack.isEmpty()) {
            if(p != null) {
                //lastVisitNode = p;
                stack.push(p);
                p = p.left;
            } else if (!stack.isEmpty()) {
                TreeNode currentTopNode = stack.pop();
                if(lastVisitNode == currentTopNode.right) {
                    System.out.print(currentTopNode.val + " ");
                    lastVisitNode = currentTopNode;
                } else if(lastVisitNode == currentTopNode.left) {
                    stack.push(currentTopNode);
                    p = currentTopNode.right;
                    lastVisitNode = currentTopNode;
                } else {
                    System.out.print(currentTopNode.val + " ");
                    lastVisitNode = currentTopNode;
                }
            }
        }
    }

    public void test2(TreeNode node) {
        if(node == null)
            return;

        TreeNode dummy = new TreeNode(0);
        dummy.left = node;
        TreeNode currentNode = dummy;
        while(currentNode != null) {
            if(currentNode.left == null) {
                //Print leaf node
                //System.out.print(currentNode.value + " ");
                //Go to right child, maybe the right node is the successor
                currentNode = currentNode.right;
            } else {
                TreeNode currentNodePredecessor = currentNode.left;
                while(currentNodePredecessor.right != null
                        && currentNodePredecessor.right != currentNode) {
                    currentNodePredecessor = currentNodePredecessor.right;
                }

                //It's the first time we visit this node. It's the root of a new child tree
                if(currentNodePredecessor.right == null) {
                    //System.out.print(currentNode.value + " ");
                    currentNodePredecessor.right = currentNode;
                    currentNode = currentNode.left;
                } else {
                    //It's the second time we visit this node.
                    //System.out.print(currentNode.value + " ");

                    reversePrint(currentNode.left, currentNodePredecessor, currentNode);
                    currentNode = currentNode.right;
                    currentNodePredecessor.right = null;
                }
            }
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        //root.left.right.left = new TreeNode(8);
        root.left.right.right = new TreeNode(9);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        BinaryTreeTraversal processor = new BinaryTreeTraversal();

        System.out.println("In-order: ");
        processor.inOrder_df_recursion(root);
        System.out.print("\n");
        processor.inOrder_df_stack(root);
        System.out.print("\n");
        processor.inOrder_Morris(root);
        System.out.print("\n");

        System.out.println("Pre-order: ");
        processor.preOrder_df_recursion(root);
        System.out.print("\n");
        processor.preOrder_df_stack(root);
        System.out.print("\n");
        processor.preOrder_df_stack_V2(root);
        System.out.print("\n");
        processor.preOrder_Morris(root);
        System.out.print("\n");

        System.out.println("Post-order: ");
        processor.postOrder_df_recursion(root);
        System.out.print("\n");
        processor.postOrder_df_stack(root);
        System.out.print("\n");
        processor.postOrder_df_stack_v2(root);
        System.out.print("\n");
        processor.postOrder_Morris(root);
        System.out.print("\n");

        System.out.println("Test");
        processor.test(root);
        System.out.print("\n");
        System.out.println("Test2");
        processor.test2(root);
        System.out.print("\n");

        System.out.println("Print node based on breadth first: ");
        processor.levelOrder_df(root);
        System.out.print("\n");
    }
}




