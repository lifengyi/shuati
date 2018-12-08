package interview;

import java.util.*;

public class Algorithm_stack_monotonous_deque {
}

class L239_Sliding_Window_Maximum {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if(nums.length == 0 || k == 0)
            return new int[0];

        int[] array = new int[nums.length - k + 1];
        Deque<Integer> deque = new ArrayDeque<>();
        for(int i = 0; i < nums.length; ++i) {
            add(deque, nums, i);
            if(i >= k - 1) {
                array[i - k + 1] = getMax(deque, nums);
                remove(deque, i - k + 1);
            }
        }
        return array;
    }

    int getMax(Deque<Integer> deque, int[] nums) {
        return nums[deque.getFirst()];
    }

    void add(Deque<Integer> deque, int[] nums, int index) {
        while(!deque.isEmpty() && nums[deque.getLast()] < nums[index])
            deque.removeLast();
        deque.addLast(index);
    }

    void remove(Deque<Integer> deque, int index) {
        if(!deque.isEmpty() && deque.getFirst() == index) {
            deque.removeFirst();
        }
    }
}
