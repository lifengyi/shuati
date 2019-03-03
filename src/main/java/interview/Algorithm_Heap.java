package interview;

import java.util.*;



public class Algorithm_Heap {
}


class L407_Trapping_Rain_Water_II {
    /**
     * 407. Trapping Rain Water II
     *
     * @param heightMap
     * @return
     *
     * 1，矩阵由外向内的遍历
     * 2，灌水的外围短板原理，最短的模板决定水量
     *    heap维护外围的木板，并取得当前最小的木板
     */
    public int L407_trapRainWater(int[][] heightMap) {
        if(heightMap == null || heightMap.length == 0 || heightMap[0].length == 0)
            return 0;

        int maxWater = 0;
        int row = heightMap.length, col = heightMap[0].length;
        int[][] flags = new int[row][col];
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        PriorityQueue<Cell> heap = new PriorityQueue<>(new Comparator<Cell>(){
            @Override
            public int compare(Cell o1, Cell o2){
                return o1.height - o2.height;
            }
        });

        for(int i = 0; i < col; ++i) {
            heap.offer(new Cell(0, i, heightMap[0][i]));
            heap.offer(new Cell(row - 1, i, heightMap[row - 1][i]));
            flags[0][i] = 1;
            flags[row - 1][i] = 1;
        }
        for(int i = 0; i < row; ++i) {
            heap.offer(new Cell(i, 0, heightMap[i][0]));
            heap.offer(new Cell(i, col - 1, heightMap[i][col - 1]));
            flags[i][0] = 1;
            flags[i][col - 1] = 1;
        }

        while(!heap.isEmpty()) {
            Cell node = heap.poll();
            for(int i = 0; i < 4; i++){
                int nx = node.x + dx[i];
                int ny = node.y + dy[i];
                if(nx < 0 || nx > row - 1 || ny < 0 || ny > col - 1)
                    continue;
                if(flags[nx][ny] != 1) {
                    if(heightMap[nx][ny] >= node.height) {
                        heap.offer(new Cell(nx, ny, heightMap[nx][ny]));
                    } else {
                        maxWater += node.height - heightMap[nx][ny];
                        heap.offer(new Cell(nx, ny, node.height));
                    }
                    flags[nx][ny] = 1;
                }
            }
        }

        return maxWater;
    }

    class Cell {
        public int x;
        public int y;
        public int height;

        public Cell(int x, int y, int height) {
            this.x = x;
            this.y = y;
            this.height = height;
        }
    }
}





/**
 * 295. Find Median from Data Stream
 *
 * 中位数采用了heap来求解的思路
 *
 * 出错点：
 * 1.  5/2 返回2.0，  5/2.0返回2.5
 * 2.  PriorityQueue当元素为Integer的时候，默认为升序，
 *     即：MinHeap， 如果需要MaxHeap，获得降序列，则需要
 *     自定义Comparator，其中compare函数的两个形参应该为
 *     public int compare(Integer o1, Integer o2)
 *     而不是int。
 * 3.  比较的函数最好使用compareTo
 */
class L295_MedianFinder {

    PriorityQueue<Integer> leftMaxHeap;
    PriorityQueue<Integer> rightMinHeap;
    int totalNumber;

    /** initialize your data structure here. */
    public L295_MedianFinder() {
        Comparator<Integer> compl = new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2){
                return o2.compareTo(o1);
            }
        };

        leftMaxHeap = new PriorityQueue<Integer>(compl);
        rightMinHeap = new PriorityQueue<Integer>();
    }

    public void addNum(int num) {
        if((totalNumber & 1) == 0)
            leftMaxHeap.offer(num);
        else
            rightMinHeap.offer(num);

        if(!leftMaxHeap.isEmpty() && !rightMinHeap.isEmpty() && leftMaxHeap.peek() > rightMinHeap.peek()) {
            rightMinHeap.offer(leftMaxHeap.poll());
            leftMaxHeap.offer(rightMinHeap.poll());
        }
        totalNumber++;
    }

    public double findMedian() {
        if(totalNumber == 0)
            return 0;

        if((totalNumber & 1) == 0) {
            return (leftMaxHeap.peek() + rightMinHeap.peek())/2.0;
        } else {
            return leftMaxHeap.peek();
        }
    }
}


/**
 * 480. Sliding Window Median
 *
 * 1. 使用heap，但是不能使用PriorityQueue，因为需要挪出元素，使用TreeSet
 *
 * 2. 使用TreeSet,但是不能放入重复元素，所以需要引入一个类，额外引入id的概念，val相同则对id进行操作
 * 
 * 3. TreeSet默认就是升序列，可以定义Comparator对类进行比较，放入到TreeSet的定义中
 *    也可以在类的定义中直接实现compareTo函数
 *    同时定义 class Node implements Comparable<Node> {...}
 *
 * 4. 加节点，减节点时需要先操作节点的插入/移除操作，然后在进行节点总数的计数
 *
 * 5. 求中位数时，当为偶数，为中间两点相加除以2的平均数为double
 *    两位数相加需要注意溢出的情况，
 *    故不采用 （a+b）/2.0
 *    而采用： a/2.0 + b/2.0
 *
 * 6. 同样是溢出问题，在比较器中，定义如下
 *    if(this.val == other.value)
 *        return this.id - other.id;
 *    else
 *        return this.val - other.val;
 *
 *    两个实数相加需要防止溢出，同理两个实数相减也要防止溢出，
 *    例如： int a = 2^31 - 1;
 *          int b = -a;
 *          return a - b;
 *          需要修改比较器代码
 *
 *  如果怕int相关的加减操作溢出，可以以后对读入的int，在自己的数据结构体中
 *  作为long来存储，并进行后续的相关加减操作，而不用担心溢出的问题了。
 *
 */
class  L480_Sliding_Window_Median {
    TreeSet<Node> leftHeap = new TreeSet<>();
    TreeSet<Node> rightHeap = new TreeSet<>();
    int totalNumber = 0;

    public double[] medianSlidingWindow(int[] nums, int k) {
        double[] ret = new double[nums.length - k + 1];
        for(int i = 0; i < k - 1; ++i) {
            addNode(nums, i);
        }
        for(int i = k - 1; i < nums.length; ++i) {
            addNode(nums, i);
            ret[i - k + 1] = getMedian();
            removeNode(nums, i - k + 1);
        }

        return ret;
    }

    double getMedian() {
        if((totalNumber & 1) == 0)
            return leftHeap.last().val/2.0 + rightHeap.first().val/2.0;
        else
            return leftHeap.last().val;
    }

    void removeNode(int[] nums, int index) {
        Node node = new Node(index, nums[index]);
        if(leftHeap.contains(node)) {
            leftHeap.remove(node);
            if((totalNumber & 1) == 0) {
                leftHeap.add(rightHeap.pollFirst());
            }
        } else {
            rightHeap.remove(node);
            if((totalNumber & 1) != 0) {
                rightHeap.add(leftHeap.pollLast());
            }
        }
        totalNumber -= 1;
    }

    void addNode(int[]nums, int index) {
        if((totalNumber & 1) == 0)
            leftHeap.add(new Node(index, nums[index]));
        else
            rightHeap.add(new Node(index, nums[index]));

        if(!leftHeap.isEmpty()
                && !rightHeap.isEmpty()
                && leftHeap.last().val > rightHeap.first().val) {
            leftHeap.add(rightHeap.pollFirst());
            rightHeap.add(leftHeap.pollLast());
        }
        totalNumber += 1;
    }

    class Node implements Comparable<Node>{
        public int id;
        public int val;
        public Node(int id, int val) {
            this.id = id;
            this.val = val;
        }

        public int compareTo(Node other) {
            if(this.val == other.val)
                return this.id - other.id;
            else {
                if(this.val > 0 && other.val < 0)
                    return 1;
                else if(this.val < 0 && other.val > 0)
                    return -1;
                else
                    return this.val - other.val;
            }
        }
    }
}


class L347_Top_K_Frequent_Elements {
    class Node {
        int num;
        int count;

        public Node(int num, int count){
            this.num = num;
            this.count = count;
        }
    }

    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> cache = new HashMap<>();
        for(int num : nums) {
            if(cache.containsKey(num)) {
                cache.put(num, cache.get(num) + 1);
            } else {
                cache.put(num, 1);
            }
        }

        Comparator<Node> comp = new Comparator<Node>(){
            @Override
            public int compare(Node n1, Node n2) {
                return n1.count - n2.count;
            }
        };

        int count = 0;
        PriorityQueue<Node> heap = new PriorityQueue<>(comp);
        for(int num : cache.keySet()) {
            count = cache.get(num);
            if(heap.size() < k) {
                heap.offer(new Node(num, count));
            } else if(count >= heap.peek().count) {     //最小堆，头结点是最小结果,当前结果大于头结点，即可以放入heap
                heap.poll();
                heap.offer(new Node(num, count));
            }
        }

        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < k; ++i) {
            result.add(heap.poll().num);
        }
        return result;
    }
}