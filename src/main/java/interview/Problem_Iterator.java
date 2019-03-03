package interview;

import java.util.LinkedList;

public class Problem_Iterator {
}

/**
 *  写法上注意  1. 使用栈来实现，
 *            2. 逻辑处理都放在hasNex中进行处理
 *            3. next函数只做pop操作
 */

class L173_Binary_Search_Tree_Iterator {

    LinkedList<TreeNode> stack = null;
    TreeNode top = null;

    public L173_Binary_Search_Tree_Iterator(TreeNode root) {
        stack = new LinkedList<>();

        TreeNode node = root;
        while(node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    /** @return the next smallest number */
    public int next() {
        int ret = -1;
        if(hasNext()) {
            ret = top.val;
            top = null;
        }

        return ret;
    }

    /** @return whether we have a next smallest number */
    public boolean hasNext() {
        if(top != null) {
            return true;
        }

        if(stack.isEmpty()) {
            return false;
        }

        top = stack.pop();
        if(top.right != null) {
            TreeNode next = top.right;
            while(next != null) {
                stack.push(next);
                next = next.left;
            }
        }

        return true;
    }
}

class L173_Binary_Search_Tree_Iterator_v2 {
    LinkedList<TreeNode> stack = null;

    public L173_Binary_Search_Tree_Iterator_v2(TreeNode root) {
        stack = new LinkedList<>();

        TreeNode node = root;
        while(node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    /** @return the next smallest number */
    public int next() {
        TreeNode node = stack.pop();

        if(node.right != null) {
            TreeNode next = node.right;
            while(next != null) {
                stack.push(next);
                next = next.left;
            }
        }

        return node.val;
    }

    /** @return whether we have a next smallest number */
    public boolean hasNext() {
        return !stack.isEmpty();
    }
}