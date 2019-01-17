package interview;

import java.util.*;

public class LeetCode_Tree {
    public static void main(String[] args) {

    }
}

class L513_Find_Bottom_Left_Tree_Value {
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
}

class L101_Symmetric_Tree {
    public boolean isSymmetric(TreeNode root) {
        if(root == null)
            return true;

        return isSymmetric(root.left, root.right);
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
}

class L105_Construct_Binary_Tree_from_Preorder_and_Inorder_Traversal {
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
}

class L108_Convert_Sorted_Array_to_Binary_Search_Tree {
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
}


class L230_Kth_Smallest_Element_in_a_BST {
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

}


class L236_Lowest_Common_Ancestor_of_a_Binary_Tree {

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
}

class L297_Serialize_and_Deserialize_Binary_Tree {
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
}

class L98_Validate_Binary_Search_Tree {
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
}
