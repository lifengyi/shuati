package interview;

import java.util.*;

public class Problem_BinaryTree_Traverse {
}

class LintCode_L66_Binary_Tree_Preorder_Traversal {
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        TreeNode cur = root, next = null;
        while(cur != null) {
            next = cur.left;
            if(next == null) {
                res.add(cur.val);
                cur = cur.right;
            } else {
                while(next.right != null && next.right != cur) {
                    next = next.right;
                }
                if(next.right == cur) {
                    next.right = null;
                    cur = cur.right;
                } else {
                    res.add(cur.val);
                    next.right = cur;
                    cur = cur.left;
                }
            }
        }
        return res;
    }

    public List<Integer> preorderTraversal_stack(TreeNode root) {
        // write your code here
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        LinkedList<TreeNode> stack = new LinkedList<>();
        stack.push(root);
        while(!stack.isEmpty()) {
            TreeNode node = stack.pop();
            res.add(node.val);
            if(node.right != null) {
                stack.push(node.right);
            }
            if(node.left != null) {
                stack.push(node.left);
            }
        }
        return res;
    }

    public List<Integer> preorderTraversal_recursion(TreeNode root) {
        // write your code here
        List<Integer> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        traverse(root, result);
        return result;
    }

    void traverse(TreeNode node, List<Integer> result) {
        if(node == null) {
            return;
        }

        result.add(node.val);
        traverse(node.left, result);
        traverse(node.right, result);
    }
}



class LintCode_L67_Binary_Tree_Inorder_Traversal {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        TreeNode cur = root, next = null;
        while(cur != null) {
            next = cur.left;
            if(next == null) {
                res.add(cur.val);
                cur = cur.right;
            } else {
                while(next.right != null && next.right != cur) {
                    next = next.right;
                }
                if(next.right == null) {
                    next.right = cur;
                    cur = cur.left;
                } else {
                    res.add(cur.val);
                    next.right = null;
                    cur = cur.right;
                }
            }
        }
        return res;
    }

    public List<Integer> inorderTraversal_stack(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        TreeNode cur = root;
        LinkedList<TreeNode> stack = new LinkedList<>();

        while(cur != null || !stack.isEmpty()) {
            if(cur == null) {
                cur = stack.pop();
                res.add(cur.val);
                cur = cur.right;
            } else {
                stack.push(cur);
                cur = cur.left;
            }
        }
        return res;
    }

    public List<Integer> inorderTraversal_stack_v1(TreeNode root) {
        // write your code here
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode node = root;
        while(node != null) {
            stack.push(node);
            node = node.left;
        }

        TreeNode cur = null;
        while(!stack.isEmpty()) {
            cur = stack.pop();
            res.add(cur.val);

            TreeNode next = cur.right;
            while(next != null) {
                stack.push(next);
                next = next.left;
            }
        }

        return res;
    }

    public List<Integer> inorderTraversal_recursion(TreeNode root) {
        // write your code here
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        traverse(root, res);
        return res;
    }

    void traverse(TreeNode node, List<Integer> res) {
        if(node == null) {
            return;
        }

        traverse(node.left, res);
        res.add(node.val);
        traverse(node.right, res);
    }
}



class LintCode_L68_Binary_Tree_Postorder_Traversal {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        TreeNode prev = null, cur = null;
        LinkedList<TreeNode> stack = new LinkedList<>();
        stack.push(root);

        while(!stack.isEmpty()) {
            cur = stack.peek();
            if(prev == null || prev.left == cur || prev.right == cur) {
                if(cur.left == null && cur.right == null) {
                    stack.pop();
                    res.add(cur.val);
                } else if(cur.left == null) {
                    stack.push(cur.right);
                } else {
                    stack.push(cur.left);
                }
            } else if(prev == cur.left){
                if(cur.right == null) {
                    stack.pop();
                    res.add(cur.val);
                } else {
                    stack.push(cur.right);
                }
            } else if(prev == cur.right) {
                stack.pop();
                res.add(cur.val);
            }

            prev = cur;
        }
        return res;
    }

    public List<Integer> postorderTraversal_recursion(TreeNode root) {
        // write your code here
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        traverse(root, res);
        return res;
    }

    void traverse(TreeNode node, List<Integer> res) {
        if(node == null) {
            return;
        }

        traverse(node.left, res);
        traverse(node.right, res);
        res.add(node.val);
    }
}


class L173_Binary_Search_Tree_Iterator_ {
    LinkedList<TreeNode> stack = null;
    TreeNode top = null;

    public L173_Binary_Search_Tree_Iterator_(TreeNode root) {
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


class L173_Binary_Search_Tree_Iterator__ {

    LinkedList<TreeNode> stack = null;
    TreeNode top = null;

    public L173_Binary_Search_Tree_Iterator__(TreeNode root) {
        stack = new LinkedList<>();

        TreeNode node = root;
        while(node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    /** @return the next smallest number */
    public int next() {
        int ret = top.val;
        top = null;
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

class L173_Binary_Search_Tree_Iterator_v2_ {
    LinkedList<TreeNode> stack = null;

    public L173_Binary_Search_Tree_Iterator_v2_(TreeNode root) {
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





