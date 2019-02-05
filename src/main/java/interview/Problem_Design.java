package interview;

import java.util.*;

public class Problem_Design {
}


/**
 * 需要设计成线程安全的类封装
 */
class L379_Design_Phone_Directory {
    Set<Integer> used = null;
    LinkedList<Integer> free = null;
    /** Initialize your data structure here
     @param maxNumbers - The maximum numbers that can be stored in the phone directory. */
    public L379_Design_Phone_Directory(int maxNumbers) {
        free = new LinkedList<>();
        used = new HashSet<>();
        for(int i = 0; i < maxNumbers; ++i) {
            free.offer(i);
        }
    }

    /** Provide a number which is not assigned to anyone.
     @return - Return an available number. Return -1 if none is available. */
    public int get() {
        if(free.isEmpty()) {
            return -1;
        }

        int num = free.poll();
        used.add(num);
        return num;
    }

    /** Check if a number is available or not. */
    public boolean check(int number) {
        return !used.contains(number);
    }

    /** Recycle or release a number. */
    public void release(int number) {
        if(used.contains(number)) {
            used.remove(number);
            free.offer(number);
        }
    }
}




class L379_Design_Phone_Directory_v2 {
    BitSet bitset;
    int max; // max limit allowed
    int smallestFreeIndex; // current smallest index of the free bit

    public L379_Design_Phone_Directory_v2(int maxNumbers) {
        this.bitset = new BitSet(maxNumbers);
        this.max = maxNumbers;
    }

    public int get() {
        // handle bitset fully allocated
        if(smallestFreeIndex == max) {
            return -1;
        }
        int num = smallestFreeIndex;
        bitset.set(smallestFreeIndex);
        //Only scan for the next free bit, from the previously known smallest free index
        smallestFreeIndex = bitset.nextClearBit(smallestFreeIndex);
        return num;
    }

    public boolean check(int number) {
        return bitset.get(number) == false;
    }

    public void release(int number) {
        //handle release of unallocated ones
        if(bitset.get(number) == false)
            return;
        bitset.clear(number);
        if(number < smallestFreeIndex) {
            smallestFreeIndex = number;
        }
    }
}


/**
 * 简单设计：使用queue实现：
 *         hit: 检测队列头，如果比timestamp - 300小的全部出队列，
 *              再将新的timestamp加入队列尾部
 *         get: 统计队列大小即可
 *
 * 扩展设计：如果再一个时间戳内有大量的hit，例如1000个，那么当过了5min
 *         之后，需要出队列这1000个节点，很耗时，所以不可取；
 *         采用当前双数组实现，一个用于记录时间戳，一个用于记录hit的数量
 *
 */
class L362_Design_Hit_Counter {
    int size = 0;
    int[] hits = null;
    int[] ts = null;


    public L362_Design_Hit_Counter() {
        size = 300;
        hits = new int[size];
        ts = new int[size];
    }

    public void hit(int timestamp) {
        int index = timestamp%size;
        if(ts[index] == timestamp) {
            hits[index]++;
        } else {
            ts[index] = timestamp;
            hits[index] = 1;
        }
    }

    public int getHits(int timestamp) {
        int count = 0;
        for(int i = 0; i < size; ++i) {
            if(timestamp - ts[i] < size) {      // 遍历整个数组，只记录5min内的数据
                                                // 很好的设计，原来考虑每次都要清空过期的数据
                                                // 这里允许保留过期数据，get做统计直接ignore
                count += hits[i];
            }
        }
        return count;
    }
}


class L146_LRU_Cache_ {
    class Node {
        int key = 0;
        int value = 0;
        Node prev = null;
        Node next = null;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    int capacity = 0;
    Node head = null;
    Node tail = null;
    Map<Integer, Node> cache = null;

    public L146_LRU_Cache_(int capacity) {
        this.capacity = capacity;

        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;

        cache = new HashMap<>();
    }

    public int get(int key) {
        if(cache.containsKey(key)) {
            Node node = cache.get(key);
            pullNode(node);
            appendNode(node);
            return node.value;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        if(cache.containsKey(key)) {
            //update cache
            Node node = cache.get(key);
            node.value = value;

            //update the link
            pullNode(node);
            appendNode(node);
        } else {
            //update the cache
            Node node = new Node(key, value);
            cache.put(key, node);

            //update the link
            appendNode(node);

            if(cache.size() > capacity) {
                //remove from link
                Node removedNode = head.next;
                pullNode(removedNode);

                //remove from cache
                cache.remove(removedNode.key);

            }
        }
    }

    void pullNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    void appendNode(Node node) {
        node.next = tail;
        node.prev = tail.prev;
        node.next.prev = node;
        node.prev.next = node;
    }
}


