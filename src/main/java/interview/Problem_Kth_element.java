package interview;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Problem_Kth_element {

}

/**
 * Kth element in xxx
 *
 * Array:   1. O(nlogn): Sort + search the Kth element
 *          2. O(n + klogn): Put all elements into Heap and poll K times
 *          3. O(k + (n-k)logk): Put first K elements into Heap.
 *                            For rest elements, compare/switch them with the heap top
 *          4. O(n): Quick Select
 *
 *
 * BST:     O(n): Use inorder traversal to find the element
 *          O(logn): Make each node maintain the size of current tree.
 *                   Use binary search to find the kth element.
 *
 *
 * Sorted Matrix:
 *          O(k + (n * m - k)logk): Crate a k-sized heap. Compare/switch other elements with the heap top
 *          O(n * 1 * log(max - min)) : Use binary search to find the kth element.
 *
 *
 * 对于BST和Sorted Matrix， 改进算法都是基于二分搜索的，
 * 即定位中间节点，比较当前节点条件是否满足（比自己小的元素是否满足k个）
 *
 * 区别在于在算法实现的判断条件上有些许区别：
 *   1. BST定位的节点就是当前的中间节点；
 *   2. Sorted Array定位的节点是取值中点，未必存在于当前列表中，
 *      需要在满足条件前提下让左右所以无限趋近直到重合；
 *
 *
 */


class L378_Kth_Smallest_Element_in_a_Sorted_Matrix {

    /**
     * Time Complexity: k + (n * m - k)logk
     *
     */
    public int kthSmallest(int[][] matrix, int k) {
        PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer p, Integer q) {
                return q.compareTo(p);
            }
        });

        for(int i = 0; i < matrix.length; ++i) {
            for(int j = 0; j < matrix[0].length; ++j) {
                if(queue.size() < k) {
                    queue.offer(matrix[i][j]);
                } else if(matrix[i][j] < queue.peek()) {
                    queue.poll();
                    queue.offer(matrix[i][j]);
                }
            }
        }
        return queue.poll();
    }

    /**
     *  Time Complexty:  n * 1 * log(max - min)
     */
    public int kthSmallest_OptimizedByBinarySearch(int[][] matrix, int k) {
        int row = matrix.length;
        int col = matrix[0].length;
        int left = matrix[0][0], right = matrix[row - 1][col - 1] + 1, middle = 0;
        while(left < right) {
            middle = left + (right - left)/2;
            int num = findLessThanMiddle(matrix, middle);
            if(num < k) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }

    int findLessThanMiddle(int[][] matrix, int num) {
        int count = 0;
        for(int i = 0; i < matrix.length; ++i) {
            int j = matrix[0].length - 1;
            while(j >= 0 && matrix[i][j] > num) {       /* 此处有所优化，正常应该是对每一行的每一列从0开始遍历 */
                j--;                                    /* 这样可能是该函数复杂度就是 n * m * log(diff)   */
            }                                           /* 此处从尾部开始遍历，这样保证对列上的遍历，只遍历1次 */
            count += j + 1;                             /* 即 n * 1，而不是 n * m，利用每一行都已排序的特点 */
        }
        return count;
    }
}
