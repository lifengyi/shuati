package com.stevenli.interview.easy.interview;

import java.util.LinkedList;

public class Problem_iterator {
}

/**
 *  写法上注意  1. 使用栈来实现，
 *            2. 逻辑处理都放在hasNex中进行处理
 *            3. next函数只做pop操作
 */

class L173_Binary_Search_Tree_Iterator {

    LinkedList<TreeNode> stack;

    public L173_Binary_Search_Tree_Iterator(TreeNode root) {
        stack = new LinkedList<>();

        TreeNode node = root;
        while(node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    /** @return whether we have a next smallest number */
    public boolean hasNext() {
        if(stack.isEmpty()) {
            return false;
        }

        if(stack.peek().right != null) {
            TreeNode node = stack.pop();
            TreeNode tmp = node.right;
            while(tmp != null) {
                stack.push(tmp);
                tmp = tmp.left;
            }
            stack.push(node);
        }
        return true;
    }

    /** @return the next smallest number */
    public int next() {
        return stack.pop().val;
    }
}
