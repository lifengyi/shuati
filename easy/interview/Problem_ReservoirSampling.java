package interview;

import java.util.Arrays;
import java.util.Random;

public class Problem_ReservoirSampling {

}


/**
 * Reservoir Sampling
 */
class L398_Random_Pick_Index {
    int[] nums = null;
    Random rand = null;

    public L398_Random_Pick_Index(int[] nums) {
        this.nums = nums.clone();
        this.rand = new Random();
    }

    public int pick(int target) {
        int ret = 0;
        int count = 0;
        for(int i = 0; i < nums.length; ++i) {
            if(nums[i] != target) {
                continue;
            }
            count++;
            if(rand.nextInt(count) == 0) {
                ret = i;
            }
        }
        return ret;
    }
}


class L384_Shuffle_Array {
    int[] nums = null;
    Random rand = null;

    public L384_Shuffle_Array(int[] nums) {
        this.nums = nums.clone();
        this.rand = new Random();
    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        return nums;
    }

    /** Returns a random shuffling of the array. */
    public int[] shuffle() {
        int[] ret = nums.clone();
        for(int i = 0; i < ret.length; ++i) {
            int j = rand.nextInt(i + 1);
            swap(ret, i, j);
        }
        return ret;
    }

    void swap(int[] nums, int i, int j) {
        int tmp = nums[j];
        nums[j] = nums[i];
        nums[i] = tmp;
    }
}



class L382_Linked_List_Random_Node {
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
  }

    ListNode root = null;
    Random rand = null;

    public L382_Linked_List_Random_Node(ListNode head) {
        root = head;
        rand = new Random();
    }

    /** Returns a random node's value. */
    public int getRandom() {
        if(root == null) {
            return -1;
        }

        int ret = root.val;
        int count = 1;
        ListNode node = root.next;
        while(node != null) {
            if(rand.nextInt(count + 1) == 0) {
                ret = node.val;
            }
            count++;
            node = node.next;
        }

        return ret;
    }
}






