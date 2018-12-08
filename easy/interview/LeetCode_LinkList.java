package interview;

import java.util.*;

class RandomListNode {
    int label;
    RandomListNode next, random;
    RandomListNode(int x) { this.label = x; }
}

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x;};
}


public class LeetCode_LinkList {



    /**
     * 148. Sort List
     *
     * @param head
     * @return
     */
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

    /**
     * 138. Copy List with Random Pointer
     *
     * @param head
     * @return
     */
    public RandomListNode L138_copyRandomList(RandomListNode head) {
        if(head == null)
            return null;

        RandomListNode cur = head;
        while(cur != null) {
            RandomListNode tmp = new RandomListNode(cur.label);
            tmp.next = cur.next;
            cur.next = tmp;
            cur = cur.next.next;
        }

        cur = head;
        while(cur != null) {
            if(cur.random != null) {
                cur.next.random = cur.random.next;
            }
            cur = cur.next.next;
        }

        RandomListNode newRandomListHead = new RandomListNode(0), curOfNewList = newRandomListHead;
        cur = head;
        while(cur != null) {
            curOfNewList.next = cur.next;
            cur.next = cur.next.next;
            cur = cur.next;
            curOfNewList = curOfNewList.next;
            curOfNewList.next = null;
        }

        return newRandomListHead.next;
    }

    public RandomListNode L138_copyRandomList_withExtraSpace(RandomListNode head) {
        if(head == null)
            return null;

        Map<RandomListNode, RandomListNode> cache = new HashMap<>();
        RandomListNode dummyRandomListHead = new RandomListNode(0);
        RandomListNode cur = head;
        RandomListNode dummyCur = dummyRandomListHead;
        while(cur != null) {
            dummyCur.next = new RandomListNode(cur.label);
            dummyCur = dummyCur.next;
            cache.put(cur, dummyCur);
            cur = cur.next;
        }

        cur = head;
        dummyCur = dummyRandomListHead.next;
        while(cur != null) {
            if(cur.random != null) {
                dummyCur.random = cache.get(cur.random);
            }

            cur = cur.next;
            dummyCur = dummyCur.next;
        }

        return dummyRandomListHead.next;
    }

    /**
     * 234. Palindrome Linked List
     *
     * @param head
     * @return
     */
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

    /**
     * 206. Reverse Linked List
     *
     * @param head
     * @return
     */
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

    /**
     * 160. Intersection of Two Linked Lists
     *
     * @param headA
     * @param headB
     * @return
     */
    public ListNode L160_getIntersectionNode(ListNode headA, ListNode headB) {
        if(headA == null || headB == null)
            return null;

        ListNode p = headA;
        ListNode q = headB;
        while(p != null && q != null){
            p = p.next;
            q = q.next;
        }

        int count = 0;
        ListNode nodeOfLongList, nodeOfShortList;
        if(p == null) {
            nodeOfShortList = headA;
            nodeOfLongList = headB;
            while(q != null) {
                count++;
                q = q.next;
            }
        } else {
            nodeOfShortList = headB;
            nodeOfLongList = headA;
            while(p != null){
                count++;
                p = p.next;
            }
        }

        while(count != 0) {
            nodeOfLongList = nodeOfLongList.next;
            count--;
        }

        while(nodeOfLongList != null && nodeOfShortList != null && nodeOfLongList != nodeOfShortList) {
            nodeOfLongList = nodeOfLongList.next;
            nodeOfShortList = nodeOfShortList.next;
        }

        return nodeOfLongList;
    }

    /**
     * 23. Merge k Sorted Lists
     *
     * @param lists
     * @return
     */
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
