package com.stevenli.interview.algorithm.basic.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by fenli on 1/9/17.
 */
public class SortProcessor {

    public int[] tmpArray;
    public static int count_1pivot_2way;
    public static int count_1pivot_3way;
    public static int count_1pivot_3way_V2;
    public static int count_2pivot_3way;

    public void sort(int[] input) {
        //quickSort_1Pivot_2WayPartition(input, 0, input.length-1);

        //quickSort_1pivot_3way(input, 0, input.length - 1);

        //quickSort_2pivot_3way(input, 0, input.length - 1);

        //insertionSort(input);

        //tmpArray = new int[input.length];
        //mergeSort(input, 0, input.length - 1);
    }

    // length > 286, use merge sort
    // use extra memory
    // O(nlogN)
    private void mergeSort(int[] input, int start, int end) {
        if(start < end) {
            int middle = start + (end - start)/2;

            mergeSort(input, start, middle);
            mergeSort(input, middle+1, end);

            doMerge(input, start, middle, end);
        }
    }

    private void doMerge(int[] input, int start, int middle, int end) {

        for(int i = start; i < end + 1; i++) {
            tmpArray[i] = input[i];
        }

        int i = start;
        int j = start;
        int k = middle + 1;
        while(j < middle + 1 || k < end + 1)
        {
            if(j == middle + 1) {
                input[i++] = tmpArray[k++];
            } else if (k == end + 1) {
                input[i++] = tmpArray[j++];
            } else if( tmpArray[j] <= tmpArray[k]) {
                input[i++] = tmpArray[j++];
            } else if ( tmpArray[j] > tmpArray[k]) {
                input[i++] = tmpArray[k++];
            }
        }
    }

    // length < 286, use quick sort
    // one pivot, two-way partition
    // O(nLogn) in average case
    // O(n^2) in worst case
    private void quickSort_1pivot_2way(int[]input, int start, int end) {



        if(start >= end)
            return;

        count_1pivot_2way++;

        /*
            TODO: enhancement, use insertion sort while end-start < 47
         */

        int index = partition_1pivot_2way(input, start, end);
        quickSort_1pivot_2way(input, start, index-1);
        quickSort_1pivot_2way(input, index+1, end);
    }

    private int partition_1pivot_2way(int[] input, int low, int high){

        int pivot = input[low];

        while (low < high) {
            while (low < high && input[high] >= pivot) --high;
            input[low] = input[high];

            while (low < high && input[low] <= pivot) ++low;
            input[high] = input[low];
        }

        input[low] = pivot;

        return low;
    }

    // quick sort
    // one pivot, 3-way partition

    /*
        出错点：
        1. Validation
        2. gt/lt/eq 每个指针的真实意义，
            gt: 大于元素的起始点
            eq: 等于元素的起始点
            lt: 小于元素的结束点
        3. low和high重合时，即为pivot存入点
     */
    private void quickSort_1pivot_3way(int[] input, int start, int end) {

        /*!!!Important: Validation*/
        if(start >= end)
            return;

        count_1pivot_3way++;

        if(end - start == 1) {
            if(input[start] > input[end]) {
                int tmp = input[start];
                input[start] = input[end];
                input[end] = tmp;
            }
            return;
        }

        int pivot = input[start];
        int low = start;
        int high = end;

        int gt = end;
        int lt = start;
        int eq = end;

        while(low < high) {

            //traverse from the tail first
            while(low < high) {
                if(input[high] > pivot) {
                    if (eq == gt) {
                        gt = high;
                        eq = high;
                    } else if (eq < gt) {
                        if(input[gt] > pivot)
                            gt--;
                        eq = high;
                        input[gt] = input[eq];
                        input[eq] = pivot;
                    }
                    high--;
                } else if (input[high] == pivot) {
                    eq = high;
                    high--;
                } else {
                    break;
                }
            }
            input[low] = input[high];

            //traverse from the head
            while(low < high && input[low] < pivot) {
                lt = low;
                low++;
            }
            input[high] = input[low];

        }

        input[low] = pivot;
        eq = high;              //fix the eq reference

        //printArray(input, start, lt);
        //printArray(input, lt+1, gt-1);
        //printArray(input, gt, end);
        //System.out.println("done");

        quickSort_1pivot_3way(input, start, lt);
        quickSort_1pivot_3way(input, gt, end);

    }

    // simple logic
    private void quickSort_1pivot_3way_V2(int[] input, int start, int end) {

        if(start >= end)
            return;

        count_1pivot_3way_V2++;

        if(end - start == 1 && input[start] > input[end]) {
            swap(input, start, end);
            return;
        }

        int low = start;
        int high = end;
        int pivot = input[start];

        int i = low + 1;
        while( i <= high ) {
            if(input[i] < pivot) {
                swap(input, i++, low++);
            } else if(input[i] == pivot) {
                i++;
            } else {
                while(i < high && input[high] > pivot)
                    high--;
                swap(input, i, high--);
            }
        }

        //printArray(input, start, low-1);
        //printArray(input, low, high);
        //printArray(input, high+1, end);
        //System.out.println("ok");

        quickSort_1pivot_3way_V2(input, start, low - 1);
        quickSort_1pivot_3way_V2(input, high + 1, end);
    }

    // quick sort
    // dual pivot, 3-way partition

    private void quickSort_2pivot_3way(int[] input, int start, int end) {

        if(start >= end)
            return;

        count_2pivot_3way++;

        if(end - start == 1 && input[start] > input[end]) {
            swap(input, start, end);
            return;
        }

        int i = end;
        while(i > start && input[start] == input[i])
            i--;

        if(i == start)
            return;

        swap(input, i, end);
        if(input[start] > input[end])
            swap(input, start, end);


        int low = start;
        int high = end;
        int pivot1 = input[start];
        int pivot2 = input[end];

        i = low + 1;
        while( i <= high ) {
            if(input[i] < pivot1) {
                swap(input, i++, low++);
            } else if(input[i] >= pivot1 && input[i] <= pivot2) {
                i++;
            } else {
                while(i < high && input[high] > pivot2)
                    high--;
                swap(input, i, high--);
            }
        }

        //printArray(input, start, low-1);
        //printArray(input, low, high);
        //printArray(input, high+1, end);
        //System.out.println("ok");


        if(low == start && high == end) {
            quickSort_2pivot_3way(input, ++low, --high);
        }
        else {
            quickSort_2pivot_3way(input, start, low - 1);
            quickSort_2pivot_3way(input, low, high);
            quickSort_2pivot_3way(input, high + 1, end);
        }

    }

    // length < 27, use insertion sort.
    // It's stable and a good choice when N is small.
    // O(n) in best case
    // O(n^2) in worst case
    private void insertionSort(int[] input) {
        for(int i = 1; i < input.length; i++)
        {
            int j = i;
            int tmp = input[j];

            while(j >= 0) {
                if(j == 0 || input[j-1] <= tmp) {
                    input[j] = tmp;
                    break;
                } else {
                    input[j] = input[j-1];
                    j--;
                }
            }

        }
    }

    private void insertionSort_V2(int[] input, int start, int end) {
        for(int i = start + 1; i <= end; i++) {
            for(int j = i; j > start && input[j] < input[j - 1]; j--) {
                swap(input, j, j - 1);
            }
        }
    }

    //counting sort, use it while sorting integers in limited range.

    public static void swap(int[] input, int from, int to) {
        int tmp = input[from];
        input[from] = input[to];
        input[to] = tmp;
    }

    public static void printArray(int[] input, int left, int right) {
        for(int i=left; i<=right; i++)
            System.out.print(input[i] + ",");
        System.out.print("\n");
    }

    public static void main(String args[]){

        //int[] array = {5, 4, 5, 5, 10, -1, 8, 5, 5};
        //int[] array = {5, 5, 8, 5, 5};
        //int[] array = {4, 3, 3, 3, 3};


        int[] array = new int[10000];
        Random rand = new Random();
        for(int i = 0; i < 10000; i++) {
            array[i] = rand.nextInt(1000000) + 1;
        }


        // sort it
        long startTime = 0;
        long endTime = 0;
        SortProcessor processor = new SortProcessor();

        /*
        int[] array_insertionSort = array.clone();
        processor.insertionSort_V2(array_insertionSort, 0, array_insertionSort.length - 1);
        SortProcessor.printArray(array_insertionSort, 0, array_insertionSort.length - 1);
        */

        int[] array_1pivot_2partition = array.clone();
        startTime = System.nanoTime();
        processor.quickSort_1pivot_2way(array_1pivot_2partition, 0, array.length - 1);
        endTime = System.nanoTime();
        System.out.println("1 pivot, 2-way partition,    time=" + (endTime - startTime)
                + ", loop count = " + count_1pivot_2way);


        int[] array_1pivot_3partition_V2 = array.clone();
        startTime = System.nanoTime();
        processor.quickSort_1pivot_3way_V2(array_1pivot_3partition_V2, 0, array.length - 1);
        endTime = System.nanoTime();
        System.out.println("1 pivot, 3-way partition V2, time=" + (endTime - startTime)
                + ", loop count = " + count_1pivot_3way_V2);


        //1 Pivot, 3-way partition quick sort
        int[] array_1pivot_3partition = array.clone();
        startTime = System.nanoTime();
        processor.quickSort_1pivot_3way(array_1pivot_3partition, 0, array.length - 1);
        endTime = System.nanoTime();
        System.out.println("1 pivot, 3-way partition,    time=" + (endTime - startTime)
                + ", loop count = " + count_1pivot_3way);


        // 2 Pivot, 3-way partition quick sort

        int[] array_2pivot_3partition = array.clone();
        startTime = System.nanoTime();
        processor.quickSort_2pivot_3way(array_2pivot_3partition, 0, array_2pivot_3partition.length - 1);
        endTime = System.nanoTime();
        System.out.println("2 pivot, 3-way partition,    time=" + (endTime - startTime)
                + ", loop count = " + count_2pivot_3way);


        //Verify it.
        //SortProcessor.printArray(array_1pivot_2partition, 0, array.length-1);
        //SortProcessor.printArray(array_1pivot_3partition, 0, array.length-1);
        //SortProcessor.printArray(array_1pivot_3partition_V2, 0, array.length-1);
        //SortProcessor.printArray(array_2pivot_3partition, 0, array.length-1);

        if(Arrays.equals(array_1pivot_2partition, array_1pivot_3partition))
            System.out.println("1pivot-2partion sorted array equals to 1pivot-3partition sorted array.");
        else
            System.out.println("1pivot-2partion sorted array NOT equals to 1pivot-3partition sorted array.");

        if(Arrays.equals(array_1pivot_2partition, array_1pivot_3partition_V2))
            System.out.println("1pivot-2partion sorted array equals to 1pivot-3partition_V2 sorted array.");
        else
            System.out.println("1pivot-2partion sorted array NOT equals to 1pivot-3partition_V2 sorted array.");

        if(Arrays.equals(array_1pivot_2partition,array_2pivot_3partition))
            System.out.println("1pivot-2partion sorted array equals to 2pivot-3partition sorted array.");
        else
            System.out.println("1pivot-2partion sorted array NOT equals to 2pivot-3partition sorted array.");

    }
}
