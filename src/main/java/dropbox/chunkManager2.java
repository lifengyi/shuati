package dropbox;

import java.util.Comparator;
import java.util.TreeSet;

public class chunkManager2 {

    class Interval implements Comparable<Interval> {
        int start = 0;
        int end = 0;
        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int compareTo(Interval other) {
            return this.start - other.start;
        }
    }


    class BST<E> {

        E head = null;
        E tail = null;

        public E getFirst() {
            return head;
        }
        public E getLast() {
            return tail;
        }

        public int size() {
            return 0;
        }

        //return the greatest element in this set
        // which is less than or equal to the given element
        // return null if there's no such element
        public E floor(E element) {
            return null;
        }

        //return the least element in this set
        //which is greater than or equal to the given element
        // return null if there's no such element
        public E ceiling(E element) {
            return null;
        }

        // return the next node of the input element
        public E next(E element) {
            return null;
        }

        public void add(E element) {

        }

        public void remove(E element) {

        }

        //return the next element of the given element
        public E getSuccessor(E element) {
            return null;
        }
    }

    private BST<Interval>  tree = null;
    private int size = 0;

    public chunkManager2(int size) {
        this.size = size;
        this.tree = new BST<Interval>();
    }

    public void insertChunk(int[] chunk) {
        Interval cur = new Interval(chunk[0], chunk[1]);

        Interval prev = tree.floor(cur);
        Interval next = tree.ceiling(cur);

        // if tree has a node which has the same start with current chunk
        // remove it
        if(prev != null && next != null && prev == next) {
            cur.end = Math.max(cur.end, next.end);
            tree.remove(next);
            prev = tree.floor(cur);
            next = tree.ceiling(cur);
        }

        // if ceiling node is included in current chunk
        // remove it
        while(next != null && next.end <= cur.end) {
            Interval tobeDeleted = next;
            next = tree.getSuccessor(next);
            tree.remove(tobeDeleted);
        }

        if(prev == null && next == null) {
            tree.add(cur);
        } else if(prev == null) {
            if(cur.end < next.start){
                tree.add(cur);
            } else {
                cur.end = Math.max(cur.end, next.end);
                tree.remove(next);
                tree.add(cur);
            }
        } else if(next == null) {
            if(cur.start > prev.end) {
                tree.add(cur);
            } else if(cur.end > prev.end) {
                cur.start = prev.start;
                tree.remove(prev);
                tree.add(cur);
            }
            //ignore if current chunk is included in prev
        } else {
            //注意左闭合区间定义在这里的体现
            int gapStart = prev.end;
            int gapEnd = next.start;
            if (cur.start <= gapStart && cur.end >= gapEnd) {
                tree.remove(prev);
                tree.remove(next);
                cur.start = prev.start;
                cur.end = next.end;
            } else if (cur.start <= gapStart) {
                tree.remove(prev);
                cur.start = prev.start;
            } else if (cur.end >= gapEnd) {
                tree.remove(next);
                cur.end = next.end;
            }
            tree.add(cur);
        }
    }

    public boolean isFileComplete() {
        if(tree.size() != 1) {
            return false;
        }

        Interval node = tree.getFirst();
        if(node.start == 0 && node.end == size - 1) {
            return true;
        }

        return false;
    }
}
