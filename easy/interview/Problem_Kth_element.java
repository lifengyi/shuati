package interview;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Problem_Kth_element {

}


class L378_Kth_Smallest_Element_in_a_Sorted_Matrix {

    /**
     * O(n) : nlog(K)
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
}
