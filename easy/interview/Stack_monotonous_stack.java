package com.stevenli.interview.easy.interview;

import java.util.*;

/**
 * 找每个元素左边或者右边第一个比它自身小/大的元素
 * 用单调栈来维护
 */

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int val) {this.val = val;}
}

public class Stack_monotonous_stack {

    /**
     * Recursive implementation
     * Time: O(n^2)
     * Space: O(n)
     *
     * @param nums
     * @return
     */
    public TreeNode L654_constructMaximumBinaryTree_v2(int[] nums) {
        return constructMaxHelper(nums,0,nums.length-1);
    }

    private TreeNode constructMaxHelper(int[] nums,int start,int end){
        if(start == end){
            TreeNode node = new TreeNode(nums[start]);
            return node;
        }
        if(start<end){
            int maxIndex = findMax(nums, start,end);
            TreeNode root = new TreeNode(nums[maxIndex]);
            if(maxIndex == start){
                root.left = null;
            }
            else{
                root.left = constructMaxHelper(nums,start,maxIndex-1);
            }
            if(maxIndex == end){
                root.right = null;
            }
            else{
                root.right = constructMaxHelper(nums,maxIndex+1, end);
            }
            return root;
        }
        else{
            return null;
        }
    }

    private int findMax(int[]nums, int start, int end){
        int maxIndex = start;
        int max = nums[start];
        for(int i =start+1;i<=end;i++){
            if(nums[i] > max){
                max = nums[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * 654. Maximum Binary Tree
     *
     * Non-recursive implementation
     * Time: O(n)
     * Space: O(n)
     *
     * @param nums
     * @return
     */
    public TreeNode L654_constructMaximumBinaryTree(int[] nums) {
        if(nums == null || nums.length == 0)
            return null;

        LinkedList<TreeNode> monoStack = new LinkedList<>();
        for(int i = 0; i <= nums.length; ++i) {
            TreeNode newNode = new TreeNode(i == nums.length ? Integer.MAX_VALUE : nums[i]);
            while(!monoStack.isEmpty() && newNode.val > monoStack.peek().val) {
                TreeNode node = monoStack.pop();
                if(!monoStack.isEmpty() && monoStack.peek().val < newNode.val) {
                    monoStack.peek().right = node;
                } else {
                    newNode.left = node;
                }
            }
            monoStack.push(newNode);
        }

        return monoStack.pop().left;
    }

    /**
     * 给一个数组，返回一个大小相同的数组。
     * 返回的数组的第i个位置的值应当是，对于原数组中的第i个元素，
     * 至少往右走多少步，才能遇到一个比自己大的元素
     * （如果之后没有比自己大的元素，或者已经是最后一个元素，
     * 则在返回数组的对应位置放上-1）
     *
     * 简单的例子：
     * input:   5,3,1,2,4
     * return: -1 3 1 1 -1
     *
     * Time: O(N)
     * Space: O(N)
     */
    public int[] findNextLargerNumber(int[] array) {
        int []ret = new int[array.length];
        LinkedList<Integer> monoStack = new LinkedList<>();

        int j = 0;
        for(int i = 0; i < array.length; ++i) {
            while(!monoStack.isEmpty() && array[i] > array[monoStack.peek()]) {
                j = monoStack.pop();
                ret[j] = i - j;
            }
            monoStack.push(i);
        }

        while(!monoStack.isEmpty())
            ret[monoStack.poll()] = -1;

        return ret;
    }


    public static void main(String[] args) {
        Stack_monotonous_stack processor = new Stack_monotonous_stack();

        int[] test1 = {5,3, 1, 2, 4};
        int[] ret = processor.findNextLargerNumber(test1);
        for(int i : ret) {
            System.out.print(i + " ");
        }
        System.out.print("\n");
    }
}
