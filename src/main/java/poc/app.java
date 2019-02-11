package poc;

import org.junit.Test;

import java.util.*;

public class app {

    @Test
    public void test_string() {
        String a = "abc";
        System.out.println(a.startsWith("ab"));
        System.out.println(a.startsWith("b"));
        System.out.println(a.startsWith("bc", 1));
        System.out.println(a.startsWith("bcd", 1));
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
