package com.stevenli.interview.easy.interview;

import java.util.*;

public class interview_Google {

}

class L34_Gas_Station {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int startIndex = 0, total = 0, sum = 0;
        for(int i = 0; i < gas.length; ++i) {
            sum += gas[i] - cost[i];
            if(sum < 0) {
                total += sum;
                sum = 0;
                startIndex = i + 1;
            }
        }
        total += sum;
        return total >= 0 ? startIndex : -1;
    }
    public int canCompleteCircuit_dp(int[] gas, int[] cost) {
        int n = gas.length;
        int[][] dp = new int[2][n];
        int prev = 0;

        for(int i = 1; i <= n; ++i) {
            for(int j = 0; j < n; ++j) {
                prev = (n + j - 1) % n;
                if(dp[(i - 1)%2][prev] < 0) {
                    dp[i%2][j] = -1;
                } else {
                    dp[i%2][j] = dp[(i - 1)%2][prev] + gas[prev] - cost[prev];
                }

                if(i == n && dp[i%2][j] >= 0) {
                    return j;
                }
            }
        }

        return -1;
    }
}

class L162_Find_Peak_Element_Google {
    public int findPeakElement(int[] nums) {
        if(nums == null || nums.length < 2 || nums[0] > nums[1]) {
            return 0;
        } else if(nums[nums.length - 1] > nums[nums.length - 2]) {
            return nums.length - 1;
        }

        int left = 1, right = nums.length - 2;
        int mid = 0;
        while(left < right) {
            mid = left + (right - left)/2;
            if(nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
                return mid;
            } else if (nums[mid] < nums[mid - 1]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}


class L205_Ismorphic_String {
    public boolean isIsomorphic(String s, String t) {
        int[] maps = new int[256];
        int[] mapt = new int[256];
        int cs = 0, ct = 0;
        for(int i = s.length() - 1; i >= 0; --i) {      // 倒序枚举是为了避免第0项是自己转自己导致逻辑出错 "ab"和"aa"
                                                        // 如果map表初始化为-1，则无需如此倒序枚举
            cs = (int)s.charAt(i);
            ct = (int)t.charAt(i);
            if(maps[cs] != mapt[ct]) {
                return false;
            }
            maps[cs] = mapt[ct] = i;
        }
        return true;
    }
}


class L146_LRU_Cache {
    class LRU<K, V> extends LinkedHashMap<K, V> {
        private int capacity = 0;

        public LRU(int capacity) {
            super(capacity * 2, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldestEntry) {
            if(size() > capacity) {
                return true;
            }
            return false;
        }
    }


    private LRU<Integer, Integer> lru = null;

    public L146_LRU_Cache(int capacity) {
        this.lru = new LRU(capacity);
    }

    public int get(int key) {
        return lru.containsKey(key) ? lru.get(key) : -1;
    }

    public void put(int key, int value) {
        lru.put(key, value);
    }
}


class L146_LRU_Cache_v2 {
    class Node {
        public int key;
        public int value;
        public Node prev;
        public Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            prev = null;
            next = null;
        }
    }

    private int capacity = 0;
    private Node head = null;
    private Node tail = null;
    private Map<Integer, Node> map = null;

    public L146_LRU_Cache_v2 (int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity * 2);
        this.head = new Node(0, 0);
        head.next = tail;
        this.tail = new Node(0, 0);
        tail.prev = head;
    }

    public int get(int key) {
        if(!map.containsKey(key)) {
            return -1;
        }

        Node node = map.get(key);
        if(node.next != tail) {
            pullNode(node);
            putAtTail(node);
        }

        return node.value;
    }

    public void put(int key, int value) {
        Node node = null;
        if(!map.containsKey(key)) {
            node = new Node(key, value);
            putAtTail(node);
        } else {
            node = map.get(key);
            node.value = value;
            pullNode(node);
            putAtTail(node);
        }
        map.put(key, node);
        if(map.size() > capacity) {
            deleteEldestNode();
        }
    }

    private void deleteEldestNode() {
        Node firstNode = head.next;
        map.remove(firstNode.key);

        pullNode(firstNode);
        firstNode.prev = null;
        firstNode.next = null;
    }

    private void pullNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void putAtTail(Node node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }
}


class L252_Meeting_Rooms {
    public class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }

    class Node {
        public int id;
        public int type;    //0:start, 1: end
        public int value;
        public Node(int id, int type, int value) {
            this.id = id;
            this.type = type;
            this.value = value;
        }
    }

    public static final int TYPE_START = 0;
    public static final int TYPE_END = 1;

    public boolean canAttendMeetings(Interval[] intervals) {
        PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>(){
            @Override
            public int compare(Node n1, Node n2) {
                if(n1.value == n2.value) {
                    return n2.type - n1.type;
                }
                return n1.value - n2.value;
            }
        });

        for(int i = 0; i < intervals.length; ++i) {
            Node node1 = new Node(i, TYPE_START, intervals[i].start);
            Node node2 = new Node(i, TYPE_END, intervals[i].end);
            queue.offer(node1);
            queue.offer(node2);
        }

        boolean canAttendMeeting = true;
        while(!queue.isEmpty()) {
            Node node = queue.poll();
            if(node.type == TYPE_START) {
                if(canAttendMeeting == false) {
                    return false;
                } else {
                    canAttendMeeting = false;
                }
            } else {
                canAttendMeeting = true;
            }
        }
        return true;
    }

}

