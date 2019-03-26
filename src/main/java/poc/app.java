package poc;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class app {

    @Test
    public void test_string() {
        String a = "abc";
        System.out.println(a.startsWith("ab"));
        System.out.println(a.startsWith("b"));
        System.out.println(a.startsWith("bc", 1));
        System.out.println(a.startsWith("bcd", 1));
        System.out.println(-3/2);
        Set<Integer> set = new HashSet<>();
        List<Integer> list = new ArrayList(set);
    }

    @Test
    public void test_array() {
        int[][] array = new int[3][0];
        System.out.println(array[0].length);
        int[] a1 = {1, 2};
        int[] a2 = {3, 4};
        array[0] = a1;
        array[1] = a2;
        array[2] = new int[3];
        array[2][2] = 9;
        System.out.println(Arrays.toString(array[0]));
        System.out.println(Arrays.toString(array[1]));
        System.out.println(Arrays.toString(array[2]));
    }

    @Test
    public void test_StringBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append(123);
        System.out.println(sb.toString());

        sb.deleteCharAt(1);
        System.out.println(sb.toString());

        sb.append("456");
        System.out.println(sb.toString());

        sb.delete(0, sb.length() - 1);
        System.out.println(sb.toString());

        String[] array = {"0000", "1000", "0010"};
        Arrays.sort(array);
        System.out.println(Arrays.toString(array));

        int n = Integer.MIN_VALUE;
        System.out.println(n);
        System.out.println(-n - 1);
    }

    @Test
    public void test1() {
        int[] nums = {3, 4, 5, 1, 3};
        if(nums == null || nums.length == 0) {
            System.out.println("exit 1");
            return;
        }
        int start = 0, end = nums.length - 1;
        if(nums[start] < nums[end]) {
            System.out.println("exit 2 with " + start);
            return;
        }

        int mid = 0, res = -1;
        while(start <= end) {
            mid = start + (end - start)/2;
            System.out.println("mid=" + mid);
            if(mid == nums.length - 1 && nums[mid] < nums[mid - 1]) {
                res = nums[mid];
                System.out.println("exit 3 with " + mid);
                break;
            } else if(nums[mid] < nums[mid - 1] && nums[mid] > nums[mid + 1]) {
                res = nums[mid];
                System.out.println("exit 4 with " + mid);
                break;
            } else {
                if(nums[mid] > nums[start]) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }

        return;
    }

    @Test
    public void test2() {
        int[] nums = {2, 1};
        System.out.println(findMin(nums));
        String s = "abc";
        s.toCharArray();
    }

    public int findMin(int[] nums) {
        // If the list has just one element then return that element.
        if (nums.length == 1) {
            return nums[0];
        }

        // initializing left and right pointers.
        int left = 0, right = nums.length - 1;

        // if the last element is greater than the first element then there is no rotation.
        // e.g. 1 < 2 < 3 < 4 < 5 < 7. Already sorted array.
        // Hence the smallest element is first element. A[0]
        if (nums[right] > nums[0]) {
            return nums[0];
        }

        // Binary search way
        while (right >= left) {
            // Find the mid element
            int mid = left + (right - left) / 2;

            // if the mid element is greater than its next element then mid+1 element is the smallest
            // This point would be the point of change. From higher to lower value.
            if (nums[mid] > nums[mid + 1]) {
                return nums[mid + 1];
            }

            // if the mid element is lesser than its previous element then mid element is the smallest
            if (nums[mid - 1] > nums[mid]) {
                return nums[mid];
            }

            // if the mid elements value is greater than the 0th element this means
            // the least value is still somewhere to the right as we are still dealing with elements
            // greater than nums[0]
            if (nums[mid] > nums[0]) {
                left = mid + 1;
            } else {
                // if nums[0] is greater than the mid value then this means the smallest value is somewhere to
                // the left
                right = mid - 1;
            }
        }
        return -1;
    }

    @Test
    public void test_double() {
        int size = 0;
        double a = 1.6;
        size = Math.max(size, (int)(4 + a));
        System.out.println(size);
        Date date = new Date();
        date.toString();

        BlockingQueue<String> bq = new LinkedBlockingQueue<>();
    }

    @Test
    public void test_Bitset() {
        BitSet bitset = new BitSet(10);
        for(int i = 0; i < 4; ++i) {
            System.out.println("1:" + bitset.get(i));
            bitset.set(i);
            System.out.println("2:" + bitset.get(i));
        }
        System.out.println(bitset.size());
        System.out.println(bitset.nextClearBit(0));
    }

    @Test
    public void test_TreeSet() {
        TreeSet<Node>  tree = new TreeSet<>();
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        tree.add(n2);
        tree.add(n1);

        System.out.println(tree.first().value);
        System.out.println(tree.last().value);
        tree.first().value = 9;

        System.out.println("now?");
        System.out.println(tree.first().value);
        System.out.println(tree.last().value);
    }

    class Node implements Comparable<Node>{
        int value = 0;
        public Node(int value) {
            this.value = value;
        }

        public int compareTo(Node other) {
            return this.value - other.value;
        }
    }

    @Test
    public void test_currentTime() {
        long time = System.currentTimeMillis();
        System.out.println((time/1000)%300);
        System.out.println(time/1000%300);
    }


    @Test
    public void test_Arrays() {
        int[] array1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(Arrays.toString(array1));


        System.arraycopy(array1, 4, array1, 0, 2);

        System.out.println(Arrays.toString(array1));
        System.out.println(array1.length);

        //for(int i = 2; i < array1.length; ++i) {
        //   array1[i] = 11;
        //}
        //System.out.println(Arrays.toString(array1));
    }
}
