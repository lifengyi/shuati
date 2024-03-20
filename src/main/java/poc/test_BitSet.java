package poc;

import org.junit.Test;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;

public class test_BitSet {

    @Test
    public void test1() {
        class TreeNode {
            int val;
            TreeNode left;
            TreeNode right;
            TreeNode(int x) { val = x; }
        }

        TreeNode t1 = new TreeNode(2);
        TreeNode t2 = new TreeNode(10);

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(t1);
        queue.offer(t2);
        queue.offer(t1);
        System.out.println(queue.size());
        while(!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if(node == t1) {
                System.out.println("t1");
            } else if (node == t2) {
                System.out.println("t2");
            }
        }


        BitSet bits1 = new BitSet(64);
        //BitSet bits2 = new BitSet(16);

        // set some bits
        for (int i = 0; i < 64; i++) {
            //if ((i % 2) == 0)
            bits1.set(i);
            //if ((i % 5) != 0) bits2.set(i);
        }
        System.out.println("Initial pattern in bits1: ");
        System.out.println(bits1);
        //System.out.println("\nInitial pattern in bits2: ");
        //System.out.println(bits2);

        System.out.println(bits1.nextClearBit(0));

        /*
        // AND bits
        bits2.and(bits1);
        System.out.println("\nbits2 AND bits1: ");
        System.out.println(bits2);

        // OR bits
        bits2.or(bits1);
        System.out.println("\nbits2 OR bits1: ");
        System.out.println(bits2);

        // XOR bits
        bits2.xor(bits1);
        System.out.println("\nbits2 XOR bits1: ");
        System.out.println(bits2);
        */
    }
}
