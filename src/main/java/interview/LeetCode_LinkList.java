package interview;

import java.util.*;



public class LeetCode_LinkList {

}

class L148_Sort_List {
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x;};
    }

    public ListNode L148_sortList(ListNode head) {
        if(head == null || head.next == null)
            return head;

        if(head.next.next == null) {
            ListNode newHead;
            if(head.val > head.next.val) {
                newHead = head.next;
                newHead.next = head;
                newHead.next.next = null;
            } else {
                newHead = head;
            }
            return newHead;
        }

        ListNode slow = head, fast = head, prev=null;
        while(fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        prev.next = null;
        ListNode first = L148_sortList(head);
        ListNode second = L148_sortList(slow);

        return L148_mergeSortedList(first, second);
    }

    private ListNode L148_mergeSortedList(ListNode first, ListNode second) {
        if(first == null && second == null) return null;
        if(first == null)  return second;
        if(second == null) return first;

        ListNode dummyHead = new ListNode(0), cur = dummyHead, firstCur = first, secondCur = second;
        while(firstCur != null && secondCur != null) {
            if(firstCur.val < secondCur.val) {
                cur.next = firstCur;
                firstCur = firstCur.next;
                cur = cur.next;
            } else {
                cur.next = secondCur;
                secondCur = secondCur.next;
                cur = cur.next;
            }
        }
        if(firstCur != null) {
            cur.next = firstCur;
        }
        if(secondCur != null) {
            cur.next = secondCur;
        }

        return dummyHead.next;
    }
}

class L234_Palindrome_Linked_List {

    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x;};
    }

    public boolean L234_isPalindrome_withoutSpace(ListNode head) {
        if(head == null || head.next == null)
            return true;

        ListNode slow, fast;
        slow = head;
        fast = head;
        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode nodeOfSecondHalf = null;
        if(fast == null) {
            //even, reverse list from here;
            nodeOfSecondHalf = L234_reverseList(slow);
        } else {
            nodeOfSecondHalf = L234_reverseList(slow.next);
        }

        ListNode nodeOfFirstHalf = head;
        while(nodeOfFirstHalf != null && nodeOfSecondHalf != null) {
            if(nodeOfFirstHalf.val != nodeOfSecondHalf.val)
                return false;
            nodeOfFirstHalf = nodeOfFirstHalf.next;
            nodeOfSecondHalf = nodeOfSecondHalf.next;
        }

        return true;
    }

    private ListNode L234_reverseList(ListNode head) {
        if(head == null)
            return null;
        if(head.next == null)
            return head;

        ListNode cur = head;
        ListNode prev = null;
        ListNode next;
        while(cur != null) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    public boolean L234_isPalindrome_withSpace(ListNode head) {
        if(head == null || head.next == null)
            return true;

        LinkedList<ListNode> stack = new LinkedList<>();
        ListNode slow, fast;
        slow = head;
        fast = head;
        while(fast != null && fast.next != null) {
            stack.push(slow);
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode node = null;
        if(fast == null) {
            //even
            node = slow;
        } else {
            //odd, the middle element
            node = slow.next;
        }

        while(!stack.isEmpty() && node != null) {
            ListNode tmp = stack.removeFirst();
            if(tmp.val != node.val)
                return false;
            node = node.next;
        }
        if(!stack.isEmpty() || node != null)
            return false;

        return true;
    }
}


class L206_Reverse_Linked_List {
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x;};
    }

    public ListNode L206_reverseList_recur(ListNode head) {
        ListNode newHead = null;
        return L206_reverse(newHead, head);
    }

    private ListNode L206_reverse(ListNode newHead, ListNode oldHead) {
        if(oldHead == null)
            return newHead;

        ListNode next = oldHead.next;
        oldHead.next = newHead;
        newHead = oldHead;
        oldHead = next;
        return L206_reverse(newHead, oldHead);
    }

    public ListNode L206_reverseList_iter(ListNode head) {
        if(head == null)
            return null;
        if(head.next == null)
            return head;

        ListNode cur = head;
        ListNode prev = null;
        ListNode next;
        while(cur != null) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }

        return prev;
    }
}

class L160_Intersection_of_Two_Linked_Lists {
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if(headA == null || headB == null) {
            return null;
        }

        ListNode node = headA;
        while(node.next != null) {
            node = node.next;
        }
        node.next = headB;

        ListNode slow = headA, quick = headA;
        do {
            slow = slow.next;
            if(quick.next == null) {
                quick = null;
            } else {
                quick = quick.next.next;
            }
        } while(slow != quick && slow != null && quick != null);

        if(quick == null || slow == null) {
            node.next = null;           //recover
            return null;
        }

        quick = headA;
        while(quick != slow) {
            quick = quick.next;
            slow = slow.next;
        }

        node.next = null;               //recover
        return quick;
    }
}


class L23_Merge_k_Sorted_Lists {
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x;};
    }

    public ListNode L23_mergeKLists(ListNode[] lists) {
        if(lists == null || lists.length == 0)
            return null;

        PriorityQueue<ListNode> heap = new PriorityQueue<>(new Comparator<ListNode>(){
            @Override
            public int compare(ListNode o1, ListNode o2) {
                if(o1.val < o2.val)
                    return -1;
                if(o1.val > o2.val)
                    return 1;
                return 0;
            }
        });

        ListNode dummyHead = new ListNode(0);
        for(ListNode node : lists) {
            if(node == null)
                continue;

            heap.offer(node);
        }

        ListNode cur = dummyHead;
        while(!heap.isEmpty()) {
            cur.next = heap.poll();
            if(cur.next.next != null)
                heap.offer(cur.next.next);

            cur = cur.next;
            cur.next = null;
        }

        return dummyHead.next;
    }

    public ListNode L23_mergeKLists_v1(ListNode[] lists) {
        if(lists == null || lists.length == 0)
            return null;

        int length = 0;
        ListNode[] newLists = new ListNode[lists.length];
        for(int i = 0; i < lists.length; i++) {
            if(lists[i] != null) {
                newLists[length] = lists[i];
                length++;
            }
        }

        L23_initMinHeap(newLists, length - 1);
        return L23_getListNode(newLists, length - 1);
    }

    private ListNode L23_getListNode(ListNode[] lists, int lastIndex) {
        ListNode dummyHead = new ListNode(0);
        ListNode currentNode = dummyHead;
        while(lists[0] != null) {
            //save the node to the new link
            currentNode.next = lists[0];
            currentNode = currentNode.next;

            //remove the node from the heap
            lists[0] = lists[0].next;
            if(lists[0] != null) {
                L23_heapify(lists, 0, lastIndex);
            } else {
                if(lastIndex != 0) {
                    L23_swap(lists, 0, lastIndex);
                    lastIndex--;
                    L23_heapify(lists, 0, lastIndex);
                }
            }
        }
        return dummyHead.next;
    }

    private void L23_initMinHeap(ListNode[] lists, int lastIndex) {
        for(int i = (lastIndex - 1)/2; i >= 0; i--)
            L23_heapify(lists, i, lastIndex);
    }

    private void L23_heapify(ListNode[] lists, int currentIndex, int lastIndex) {
        if(currentIndex == lastIndex)
            return;

        int leftIndex  = 2 * currentIndex + 1;
        int rightIndex = 2 * currentIndex + 2;
        if(leftIndex > lastIndex)
            return;
        if(rightIndex <= lastIndex) {
            int smaller = currentIndex;
            if(lists[leftIndex].val < lists[smaller].val)
                smaller = leftIndex;
            if(lists[rightIndex].val < lists[smaller].val)
                smaller = rightIndex;
            if(smaller != currentIndex) {
                L23_swap(lists, currentIndex, smaller);
                L23_heapify(lists, smaller, lastIndex);
            }
        } else {
            if(lists[leftIndex].val < lists[currentIndex].val) {
                L23_swap(lists, currentIndex, leftIndex);
                L23_heapify(lists, leftIndex, lastIndex);
            }
        }
    }

    private void L23_swap(ListNode[] lists, int src, int target) {
        ListNode tmp  = lists[src];
        lists[src]  = lists[target];
        lists[target] = tmp;
    }
}


class L57_Insert_Interval {

    public class Interval {
        int start;
        int end;
        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }
    public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
        Comparator<Interval> comp = new Comparator<Interval>(){
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        };

        List<Interval> newIntervals = new ArrayList<>();
        newIntervals.addAll(intervals);
        newIntervals.add(newInterval);
        Collections.sort(newIntervals, comp);

        Interval first = newIntervals.get(0);
        int start = first.start;
        int end = first.end;

        List<Interval> result = new ArrayList<>();
        for(Interval interval : newIntervals) {
            if(interval.start <= end) {
                end = Math.max(end, interval.end);
            } else {
                result.add(new Interval(start, end));
                start = interval.start;
                end = interval.end;
            }
        }
        result.add(new Interval(start, end));
        return result;
    }
}



class L2_Add_Two_Numbers {

    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0), prev = dummyHead;
        int carryover = 0;
        while(l1 != null || l2 != null) {
            if(l1 != null) {
                carryover += l1.val;
                l1 = l1.next;
            }
            if(l2 != null) {
                carryover += l2.val;
                l2 = l2.next;
            }
            ListNode cur = new ListNode(carryover%10);
            prev.next = cur;
            prev = cur;
            carryover /= 10;
        }

        if(carryover != 0) {
            ListNode cur = new ListNode(carryover);
            prev.next = cur;
        }

        return dummyHead.next;
    }
}


class L56_Merge_Intervals {
    public class Interval {
        int start;
        int end;
        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }

    public List<Interval> merge(List<Interval> intervals) {
        List<Interval> result = new ArrayList<>();
        if(intervals == null || intervals.size() == 0) {
            return result;
        }


        Comparator<Interval> comp = new Comparator<Interval>(){
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        };

        Collections.sort(intervals,comp);

        Interval first = intervals.get(0);
        int start = first.start, end = first.end;
        for(Interval interval : intervals) {
            if(interval.start <= end) {
                end = Math.max(end, interval.end);
            } else {
                result.add(new Interval(start, end));
                start = interval.start;
                end = interval.end;
            }
        }

        result.add(new Interval(start, end));
        return result;
    }
}


class L24_Swap_Nodes_in_Pairs {
    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode swapPairs(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }

        ListNode dummyHead = new ListNode(0);
        dummyHead.next = head;
        ListNode prev = dummyHead, first = head, second = first.next, last = second.next;

        while(last != null) {
            swap(prev, first, second, last);
            prev = first;   // Now first is pointing to the last node, but not the second.
            first = last;
            if(first.next == null) {
                second = null;
                break;
            }
            second = first.next;
            last = second.next;
        }

        if(first != null && second != null) {
            swap(prev, first, second, last);
        }

        return dummyHead.next;
    }

    void swap(ListNode prev, ListNode first, ListNode second, ListNode last) {
        prev.next = second;
        second.next = first;
        first.next = last;
    }
}


class L138_Copy_List_with_Random_Pointer {
    class RandomListNode {
        int label;
        RandomListNode next, random;
        RandomListNode(int x) { this.label = x; }
    };

    public RandomListNode copyRandomList(RandomListNode head) {
        if(head == null) {
            return head;
        }

        duplicateNode(head);
        RandomListNode first = head, second = null;
        while(first != null) {
            second = first.next;
            if(first.random == null) {
                second.random = null;
            } else {
                second.random = first.random.next;
            }
            first = first.next.next;
        }

        RandomListNode newHead = head.next;
        first = head;
        second = first.next;
        while(first != null) {
            first.next = second.next;
            first = first.next;
            if(first != null) {
                second.next = first.next;
                second = second.next;
            }
        }
        return newHead;
    }

    void duplicateNode(RandomListNode head) {
        RandomListNode node = head;
        while(node != null) {
            RandomListNode newNode = new RandomListNode(node.label);
            newNode.next = node.next;
            node.next = newNode;
            node = newNode.next;
        }
    }

    public RandomListNode copyRandomList_hashmap(RandomListNode head) {
        if(head == null) {
            return head;
        }

        Map<RandomListNode, RandomListNode> map = new HashMap<>();
        RandomListNode node = head;
        while(node != null) {
            RandomListNode newNode = new RandomListNode(node.label);
            map.put(node, newNode);
            node = node.next;
        }

        for(RandomListNode oldNode : map.keySet()) {
            RandomListNode newNode = map.get(oldNode);
            newNode.next = map.get(oldNode.next);
            newNode.random = map.get(oldNode.random);
        }

        return map.get(head);
    }
}
