package interview;

import java.util.*;

public class Alogorithm_sweep_line {
    //dummy
}

/**
 * 218. The Skyline Problem
 *
 * 错误处：
 * 1. buildId设置出错，start/end 共享一个buildId
 * 2. 从Heap中提取数据时，没有判断heap为空情况
 * 3. 当Heap为空时，应该提取当前高度为0
 * 4. 边缘case：
 *    当有若干building的起始点、重点互相重合，例如：起点与起点，终点于终点，起点与终点
 *    处理的时候，将这些重合点需要同时放到heap中，一起处理，
 *    不允许连续的不同起点，有相同的高度；
 *    也不允许相同的起点，出现不同的高度；
 *
 * 区间题中，飞机同一时间在天空最多的数量，这个题目提示了飞机先落下，再允许飞机起飞，
 * 这么要求主要是配合题目的结果，结果求的是同时在天上的飞机有多少，
 * 不同飞机的同时起飞、不同飞机的同时下落、不同飞机的同时起飞/下落 都要区分开来独立计算
 * 和这个天际线正好相反，天际线是要归并处理。
 */
class L218_The_Skyline_Problem {
    class Node implements Comparable<Node>{
        int indexValue; //the point which we need to check
        int height;
        int type;   //0: start, 1: end
        int buildingId; //id of buildings
        public Node(int indexValue, int height, int type, int buildingId){
            this.indexValue = indexValue;
            this.height = height;
            this.type = type;
            this.buildingId = buildingId;
        }

        public int compareTo(Node other) {
            if(this.indexValue == other.indexValue)
                return this.type - other.type;
            else
                return this.indexValue - other.indexValue;
        }
    }

    public List<int[]> getSkyline(int[][] buildings) {
        List<int[]> ret = new ArrayList<>();
        if(buildings.length == 0)
            return ret;

        Node[] nodeArray = new Node[buildings.length * 2];
        for(int i = 0; i < buildings.length; ++i) {
            nodeArray[i * 2]     = new Node(buildings[i][0], buildings[i][2], 0, i);
            nodeArray[i * 2 + 1] = new Node(buildings[i][1], buildings[i][2], 1, i);
        }

        TreeSet<Node> nodeTree = new TreeSet<>(new Comparator<Node>(){
            @Override
            public int compare(Node o1, Node o2){
                if(o1.height == o2.height) {
                    return o1.buildingId - o2.buildingId;
                } else{
                    return o1.height - o2.height;
                }
            }
        });

        Arrays.sort(nodeArray);
        int currentHeight = Integer.MIN_VALUE;
        for(int i = 0; i < nodeArray.length; ++i) {
            if(nodeArray[i].type == 0)
                nodeTree.add(nodeArray[i]);
            else
                nodeTree.remove(nodeArray[i]);

            if(i + 1 < nodeArray.length
                    && nodeArray[i + 1].indexValue == nodeArray[i].indexValue)
                continue;

            if(!nodeTree.isEmpty() && nodeTree.last().height != currentHeight) {
                currentHeight = nodeTree.last().height;
                int[] height = new int[2];
                height[0] = nodeArray[i].indexValue;
                height[1] = currentHeight;
                ret.add(height);
            } else if(nodeTree.isEmpty()) {
                currentHeight = 0;
                int[] height = new int[2];
                height[0] = nodeArray[i].indexValue;
                height[1] = currentHeight;
                ret.add(height);
            }
        }

        return ret;
    }
}

class L253_Meeting_Rooms_II_ {
    public class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }

    class Node implements Comparable<Node> {
        int id;
        int type;   //0: start, 1: end
        int value;
        public Node(int id, int type, int value) {
            this.id = id;
            this.type = type;
            this.value = value;
        }
        public int compareTo(Node other) {
            if(this.value == other.value) {
                return other.type - this.type;
            } else {
                return this.value - other.value;
            }
        }
    }

    public int minMeetingRooms(Interval[] intervals) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for(int i = 0; i < intervals.length; ++i){
            Node node1 = new Node(i, 0, intervals[i].start);
            Node node2 = new Node(i, 1, intervals[i].end);
            queue.offer(node1);
            queue.offer(node2);
        }
        Set<Integer> set = new HashSet<>();
        int maxRooms = 0;
        while(!queue.isEmpty()) {
            Node node = queue.poll();
            if(node.type == 0) {
                set.add(node.id);
            } else {
                set.remove(node.id);
            }
            maxRooms = Math.max(maxRooms, set.size());
        }
        return maxRooms;
    }
}