package dropbox;

import java.util.LinkedList;

public class chunkManager {
    /**
     * 1. 给一堆Chunk和一个file size,问给定的一堆Chunk能不能组成complete file.
     * Chunk就是一个左开右闭的区间, 如[0, 4)表示这个chunk包含0, 1, 2, 3这4个byte.
     * 给定的size是这个文件大小.
     * boolean isCompleteFile(List<Chunk> chunks, int size)
     * 例如:.
     * chunks = [[0, 1), [1, 3), [3, 4)]  size = 4   => 结果是true
     * chunks = [[0, 1), [1, 3), [3, 4)]  size = 5   => 结果是false
     * chunks = [[0, 1), [2, 3), [3, 4)]  size = 4   => 结果是false
     *
     * 2. 实现一个ChunkManager class, 实现一个函数, isCompleteForNow(Chunk chunkManager),
     *    每call一次这个函数,就会把chunk更新到这个类中,并判断当前类中已经有的list of chunkManager
     *    可不可以组成一个complete file
     * 例如:
     * ChunkManager chunkManager = new ChunkManager(4);
     * chunkManager.isCompleteForNow([0, 2)); -> false
     * chunkManager.isCompleteForNow([2, 3)); -> true
     *
     * 类似insert interval 要求多次插入，如何优化，可用segmentation tree
     *
     *
     *
     *
     * 另一种描述：
     * BitTorrent那一题。给一堆下载好的chunk的起始byte和结束byte，给File的大小，
     * 看看chunk有没有覆盖从0到file大小的所有byte。
     *
     * 接下来follow up，说会不停的给chunk，会call insertChunk n次，
     * isComplete n次，怎么样能够达到最优的时间和空间复杂度。
     *
     * 我一看感觉要用tree，但是一开始想歪了想到segment tree上去了，弄了一下发现不对。
     * 没办法只能厚着脸问小哥要hint，小哥说你直接用Balance BST表示interval不就好了嘛，
     * 干嘛segment tree那么麻烦。
     * 所以最后就是建就是一棵树，满足条件：
     *     left.end < root.start, right.start > root.end然后保持平衡就可以了。
     *
     * 接着就给了一堆平衡树的method,基本所有能想到的,比如getSeccessor这种都有）
     * 说假设已经弄好了，叫写代码。最后实现了,平均时间复杂度lgN然后空间复杂度n
     *
     */

    class SegementTree {

        class SegmentTreeNode {
            public int start = 0;
            public int end = 0;
            public int count = 0;

            public boolean isUpdateDelayed = false;

            public SegmentTreeNode left = null;
            public SegmentTreeNode right = null;

            public SegmentTreeNode(int start, int end) {
                this.start = start;
                this.end = end;
            }
        }

        public SegmentTreeNode root = null;

        public SegementTree(int size) {
            this.root = build(0, size - 1);
        }

        private SegmentTreeNode build(int start, int end) {
            if(start > end) {
                return null;
            }

            SegmentTreeNode node = new SegmentTreeNode(start, end);
            if(start != end) {
                int mid = start + (end - start)/2;
                node.left = build(start, mid);
                node.right = build(mid + 1, end);
            }
            return node;
        }

        public void update(int start, int end) {
            if(root == null) {
                return;
            }
            update(root, start, end);
        }

        public boolean isFull() {
            if(root == null) {
                return false;
            }
            return root.count == root.end - root.start + 1;
        }

        /**
         * Algorithm:
         *   1. if(node.start == start && node.end == end && start == end)
         *      it's a single point update => update the node
         *      return
         *
         *   2. if(node.start == start && node.end == end)
         *      it's a range update => update the node and tag "update delayed" flag
         *      return
         *
         *   3. if we reach here, it means [start, end] is included in current interval
         *      maybe it is inlucded in both left and right
         *
         *      check if "update delayed" flag is tagged: if yes,
         *          a) reset the flag for current node
         *          b) propagate the flag to left and right
         *          c) update value of left and right
         *
         *   4. Once finish 3), split [start, end] and update left and right
         */
        private void update(SegmentTreeNode node, int start, int end) {
            if(start > end) {
                return;
            }

            // [start, end] equals current node's interval
            if(node.start == start && node.end == end) {
                if(start == end) {
                    // single point update
                    node.count = 1;
                } else {
                    // range update, tag "update delayed"
                    if(node.count != end - start + 1) {
                        node.count = node.end - node.start + 1;
                        node.isUpdateDelayed = true;
                    }
                }
                return;
            }

            // [start, end] is included in left/right
            // check if "udpate delayed" is tagged,
            // if yes, propagate the flag;
            if(node.isUpdateDelayed) {
                propagateUpdateDelayed(node.left);
                propagateUpdateDelayed(node.right);
                node.isUpdateDelayed = false;
            }

            int mid = node.start + (node.end - node.start)/2;
            if(start <= mid) {
                update(node.left, start, Math.min(end, mid));
            }
            if(end >= node.right.start) {
                update(node.right, Math.max(start, mid + 1), end);
            }
            node.count = node.left.count + node.right.count;
        }

        void propagateUpdateDelayed(SegmentTreeNode node) {
            if(node.start == node.end) {
                node.count = 1;
                return;
            }

            if(node.count != node.end - node.start + 1) {
                node.count = node.end - node.start + 1;
                node.isUpdateDelayed = true;
            }
        }

        /**
         * Algorithm:
         *  1. if(node.start = start && node.end == end)
         *     return node value
         *
         *  2. Check if "update delayed" is tagged,
         *     if yes, propagate the flag to child and also update child's node value
         *
         *  3. Split [start, end] and query left and right
         */
        public int query(SegmentTreeNode node, int start, int end) {
            return 0;
        }

        public void verb() {
            LinkedList<SegmentTreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while(!queue.isEmpty()) {
                SegmentTreeNode node = queue.poll();
                if(node.left != null) {
                    queue.offer(node.left);
                }
                if(node.right != null) {
                    queue.offer(node.right);
                }
                System.out.println(String.format("Node: start=%d, end=%d, count=%d, delayedUpdate = %b",
                        node.start, node.end, node.count, node.isUpdateDelayed));
            }
        }
    }


    private SegementTree tree = null;
    private int size = 0;

    public chunkManager(int size) {
        this.size = size;
        this.tree = new SegementTree(size);
    }

    public boolean isCompeteForNow(int[] interval) {
        int start = interval[0];
        int end = interval[1] - 1;

        if(start > end || start >= size || end >= size
                || start < 0 || end < 0) {
            System.out.println("Invalid interval.");
            return tree.isFull();
        }

        tree.update(start, end);
        return tree.isFull();
    }

    public void verb() {
        tree.verb();
    }

    public static void main(String[] args) {
        chunkManager manager = new chunkManager(10);
        int[][] intervals = {
                {1, 8},
                {7, 9}
        };

        for(int[] interval : intervals) {
            manager.verb();
            System.out.println(manager.isCompeteForNow(interval));
        }

        System.out.println("end");
        manager.verb();
    }
}
