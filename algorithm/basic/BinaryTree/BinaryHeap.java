package com.stevenli.interview.algorithm.basic.BinaryTree;

import java.util.Arrays;

/**
 * ****************  Heap + Priority Queue  ****************
 *
 *  Complete Binary LeetCode_Tree: All levels are completely filled except possibly
 *                        the last level and the last level has all leaves
 *                        as left as possible.
 *
 * Let n be the number of elements in the heap and i be an arbitrary valid
 * index of the array storing the heap. If the tree root is at index 0, with
 * valid indices 0 through n − 1, then each element a at index i has:
 *      children at indices 2i + 1 and 2i + 2
 *      its parent at index floor((i − 1) ∕ 2).
 *
 * The traversal method use to achieve Array representation is Level Order
 *
 * Usage: Binary Heap (min heap or max heap), can be used to resolve
 * following issues:
 *   a) Dijkstra’s Shortest Path
 *   b) Prim’s Minimum Spanning LeetCode_Tree.
 *   c) K’th Largest Element in an array.
 *   d) Sort an almost sorted array
 *   e) Merge K Sorted Arrays.
 */
public class BinaryHeap {

    /**
     * Time complexity: O(N), but not O(nlogN)
     *
     * @param array
     */
    public void createMaxHeap(int[] array) {
        if(array == null || array.length == 0)
            return;

        for(int i = (array.length - 1)/2; i >= 0; i--) {
            heapifyMaxHeap(i, array.length - 1, array);
        }
    }


    /**
     *
     * @param index
     *          当前节点下标索引
     * @param len
     *          当前堆为节点的下标索引
     * @param heap
     *          堆
     */
    private void heapifyMaxHeap(int index, int len, int[] heap) {
        if( (2*index + 1) <= len) {
            //find right child and left child
            int largest = index;
            int left  = 2 * index;
            int right = 2 * index + 1;

            if(heap[left] > heap[largest])
                largest = left;

            if(heap[right] > heap[largest])
                largest = right;

            if(largest != index) {
                swap(index, largest, heap);
                heapifyMaxHeap(largest, len, heap);
            }
        } else if( (2*index) <= len) {
            //only find right child
            int left = 2 * index;
            if(heap[left] > heap[index]) {
                swap(left, index, heap);
                heapifyMaxHeap(left, len, heap);
            }
        }
        //it's a leave
    }

    /**
     * Time complexity: O(N), but not O(nlogN)
     *
     * @param array
     */
    public void createMinHeap(int[] array) {
        if(array == null || array.length == 0)
            return;

        for(int i = (array.length - 1)/2; i >= 0; i--){
            heapifyMinHeap(i, array.length - 1, array);
        }
    }

    private void heapifyMinHeap(int index, int lastIndex, int[] heap) {
        if((2*index + 1) <= lastIndex){
            int smallest = index;
            int left = 2 * index;
            int right = 2 * index + 1;

            if(heap[left] < heap[smallest])
                smallest = left;
            if(heap[right] < heap[smallest])
                smallest = right;
            if(smallest != index) {
                swap(index, smallest, heap);
                heapifyMinHeap(smallest, lastIndex, heap);
            }
        } else if(2*index <= lastIndex) {
            int left = 2*index;
            if(heap[left] < heap[index]) {
                swap(index, left, heap);
                heapifyMinHeap(left, lastIndex, heap);
            }
        }
    }

    private void swap(int from, int to, int[] array) {
        int tmp = array[to];
        array[to] = array[from];
        array[from] = tmp;
    }

    /**
     * Time complexity: O(NLogN)
     *
     * @param array
     */
    public void heapSortDescending(int[] array) {
        createMinHeap(array);
        int start = 0;
        int end = array.length - 1;
        while(start != end ) {
            heapSortMinHeap(start, end, array);
            end--;
        }
    }

    private void heapSortMinHeap(int index, int lastIndex, int[] heap) {
        int tmp = heap[index];
        heap[index] = heap[lastIndex];
        heap[lastIndex] = tmp;
        heapifyMinHeap(index, lastIndex - 1, heap);
    }

    /**
     * Time complexity: O(NLogN)
     *
     * @param array
     */
    public void heapSortAscending(int[] array) {
        createMaxHeap(array);
        int start = 0;
        int end = array.length - 1;
        while(start != end) {
            heapSortMaxHeap(start, end, array);
            end--;
        }
    }

    private void heapSortMaxHeap(int index, int lastIndex, int[] heap) {
        int tmp = heap[index];
        heap[index] = heap[lastIndex];
        heap[lastIndex] = tmp;
        heapifyMaxHeap(index, lastIndex - 1, heap);
    }


    /**
     *  insert a new node into a Heap
     *  heapify the tree with this node:
     *     if the index of this node is n, then heapify((n-1)/2, n - 1 , heap)
     *     then set n = (n - 1)/2, loop if n >= 0
     *
     *  time complexity is: O(logN)
     */
    public void insertIntoPriorityQueue(){

    }

    /**
     *  Remove the largest or smallest node
     *  which means that we should remove the root node.
     *  Then put the last node into the root and do heapify
     *
     *  Time complexity: O(LogN)
     */
    public void removeFromPriiorityQueue(){

    }

    /**
     * The Kth largest elements in array
     * 1. Create a Max Heap and extract the top value K times
     * 2. Create a Min Heap with the value from 0 to k-1,
     *    then compare value from k to n - 1 with the top of
     *    the heap, replace the node if node if bigger than
     *    the top and do heapify from
     *
     */
    public int getLargestKth(int[] array, int k) {
        int[] heap = getMinHeap(array, k);
        if(heap == null)
            return -1;

        for(int i = (heap.length-2)/2; i>= 0; i--)
            heapify(heap, i, heap.length - 1);

        for(int i = k; i < array.length; i++) {
            adjustHeap(heap, array[i]);
        }

        return heap[0];
    }

    private void adjustHeap(int[] heap, int num) {
        if(num > heap[0]) {
            heap[0] = num;
            heapify(heap, 0, heap.length - 1);
        }
    }

    //Create a Min Heap
    private void heapify(int[] heap, int begin, int end) {
        int parentIndex = begin;
        int leftIndex = 2 * parentIndex + 1;
        int rightIndex = 2 * parentIndex + 2;

        if(rightIndex <= end) {
            int smaller = parentIndex;
            if(heap[smaller] > heap[rightIndex])
                smaller = rightIndex;
            if(heap[smaller] > heap[leftIndex])
                smaller = leftIndex;
            if(smaller != parentIndex) {
                int tmp = heap[parentIndex];
                heap[parentIndex] = heap[smaller];
                heap[smaller] = tmp;
                heapify(heap, smaller, end);
            }
        } else if(leftIndex <= end) {
            if(heap[parentIndex] > heap[leftIndex]) {
                int tmp = heap[leftIndex];
                heap[leftIndex] = heap[parentIndex];
                heap[parentIndex] = tmp;
            }
        }
    }

    private int[] getMinHeap(int[]array, int size) {
        if(array == null || size > array.length )
            return null;

        int[] heap = Arrays.copyOfRange(array, 0, size);
        return heap;
    }


    public static void main(String[] args) {
        BinaryHeap processor = new BinaryHeap();

        int[] array1 = {4, 3, 8, 10, 11, 13, 7, 30, 17, 26};
        processor.createMaxHeap(array1);
        System.out.println("Max heap: " + Arrays.toString(array1));

        int[] array2 = {4, 3, 8, 10, 11, 13, 7, 30, 17, 26};
        processor.createMinHeap(array2);
        System.out.println("Min heap: " + Arrays.toString(array2));

        int[] array3 = {4, 3, 8, 10, 11, 13, 7, 30, 17, 26};
        processor.heapSortAscending(array3);
        System.out.println("Heap sort ascending:  " + Arrays.toString(array3));

        int[] array4 = {4, 3, 8, 10, 11, 13, 7, 30, 17, 26};
        processor.heapSortDescending(array4);
        System.out.println("Heap sort descending: " + Arrays.toString(array4));

        int[] array5 = {4, 3, 8, 10, 11, 13, 7, 30, 17, 26};
        int kth = processor.getLargestKth(array5, 3);
        System.out.println("3th larget number is : " + kth);
    }
}
