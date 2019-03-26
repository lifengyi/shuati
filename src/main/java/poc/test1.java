package poc;

import java.util.*;

public class test1 {

    public static void main(String[] args) {
        test1 proc = new test1();
        proc.inc("a");
        proc.inc("b");
        proc.inc("b");
        proc.inc("c");
        proc.inc("c");
        proc.dec("b");
        proc.dec("b");
    }

    class Node {
        Set<String> keys = null;
        Node prev = null;
        Node next = null;

        public Node() {
            this.keys = new HashSet<>();
        }

        public void add(String key) {
            keys.add(key);
        }

        public void remove(String key) {
            keys.remove(key);
        }

        public boolean isEmpty() {
            return keys.isEmpty();
        }
    }

    Map<String, Integer> dataMap = null;
    Map<Integer, Node> counterMap = null;
    Node head = null;
    Node tail = null;

    /** Initialize your data structure here. */
    public test1() {
        dataMap = new HashMap<>();
        counterMap = new HashMap<>();
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    /** Inserts a new key <Key> with value 1. Or increments an existing key by 1. */
    public void inc(String key) {
        if(!dataMap.containsKey(key)) {
            dataMap.put(key, 1);
            initCounter(key);
            System.out.println("1st time for " + key);
        } else {
            int cnt = dataMap.get(key);
            dataMap.put(key, cnt + 1);
            changeCounter(key, cnt, 1);
            System.out.println("update " + key);
        }

        System.out.println(head.next.keys.toString());
        System.out.println("Finish inc " + key + "\n\n");
    }

    /** Decrements an existing key by 1. If Key's value is 1, remove it from the data structure. */
    public void dec(String key) {
        if(!dataMap.containsKey(key)) {
            return;
        }

        int cnt = dataMap.get(key);
        if(cnt == 1) {
            dataMap.remove(key);
        } else {
            dataMap.put(key, cnt - 1);
        }
        changeCounter(key, cnt, -1);
    }

    private void initCounter(String key) {
        if(counterMap.containsKey(1)) {
            counterMap.get(1).add(key);
        } else {
            Node node = new Node();
            node.add(key);
            counterMap.put(1, node);
            addNextNode(head, node);
        }
    }

    private void changeCounter(String key, int curCount, int diff) {
        Node curNode = counterMap.get(curCount);

        int newCount = curCount + diff;
        Node newNode = counterMap.get(newCount);
        if(newNode == null) {
            newNode = new Node();
            counterMap.put(newCount, newNode);
            if(diff > 0) {
                addNextNode(curNode, newNode);
            } else {
                addPrevNode(curNode, newNode);
            }
        }

        newNode.add(key);
        curNode.remove(key);

        System.out.println(curNode.keys.toString());

        if(curNode.isEmpty()) {
            counterMap.remove(curCount);
            removeNodeFromList(curNode);
        }
    }

    void addNextNode(Node cur, Node next) {
        next.next = cur.next;
        next.prev = cur;
        next.prev.next = next;
        next.next.prev = next;
    }

    void addPrevNode(Node cur, Node prev) {
        prev.next = cur;
        prev.prev = cur.prev;
        prev.prev.next = prev;
        prev.next.prev = prev;
    }

    void removeNodeFromList(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
    }

    /** Returns one of the keys with maximal value. */
    public String getMinKey() {
        if(head.next == tail) {
            return "";
        }
        return (String)head.next.keys.iterator().next();
    }

    /** Returns one of the keys with Minimal value. */
    public String getMaxKey() {
        if(tail.prev == head) {
            return "";
        }
        return (String)tail.prev.keys.iterator().next();
    }
}
