package Linkedin;

public class stack_PushPullMiddle_O1 {

    public static void main(String[] args) {
        stack_Push_Pop_GetMiddle_O1 stack = new stack_Push_Pop_GetMiddle_O1();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        stack.pop();
        stack.pop();
        stack.pop();
        System.out.println(stack.getMiddle());
    }
}

class stack_PushPullMiddle {

    /* A Doubly Linked List Node */
    class DLLNode
    {
        DLLNode prev;
        int data;
        DLLNode next;
        DLLNode(int d){data=d;}
    }

    /* Representation of the stack data structure that supports findMiddle()
       in O(1) time.  The Stack is implemented using Doubly Linked List. It
       maintains pointer to head node, pointer to middle node and count of
       nodes */
    class myStack
    {
        DLLNode head;
        DLLNode mid;
        int count;
    }


    /* Function to create the stack data structure */
    myStack createMyStack()
    {
        myStack ms = new myStack();
        ms.count = 0;
        return ms;
    }


    /* Function to push an element to the stack */
    void push(myStack ms, int new_data)
    {

        /* allocate DLLNode and put in data */
        DLLNode new_DLLNode = new DLLNode(new_data);


        /* Since we are adding at the beginning,
          prev is always NULL */
        new_DLLNode.prev = null;

        /* link the old list off the new DLLNode */
        new_DLLNode.next = ms.head;

        /* Increment count of items in stack */
        ms.count += 1;

        /* Change mid pointer in two cases
           1) Linked List is empty
           2) Number of nodes in linked list is odd */
        if(ms.count == 1)
        {
            ms.mid=new_DLLNode;
        }
        else
        {
            ms.head.prev = new_DLLNode;

            if((ms.count % 2) != 0) // Update mid if ms->count is odd
                ms.mid=ms.mid.prev;
        }

        /* move head to point to the new DLLNode */
        ms.head = new_DLLNode;

    }

    /* Function to pop an element from stack */
    int pop(myStack ms)
    {
        /* Stack underflow */
        if(ms.count == 0)
        {
            System.out.println("Stack is empty");
            return -1;
        }

        DLLNode head = ms.head;
        int item = head.data;
        ms.head = head.next;

        // If linked list doesn't become empty, update prev
        // of new head as NULL
        if(ms.head != null)
            ms.head.prev = null;

        ms.count -= 1;

        // update the mid pointer when we have even number of
        // elements in the stack, i,e move down the mid pointer.
        if(ms.count % 2 == 0)
            ms.mid=ms.mid.next;

        return item;
    }

    // Function for finding middle of the stack
    int getMiddle(myStack ms)
    {
        if(ms.count == 0)
        {
            System.out.println("Stack is empty now");
            return -1;
        }
        return ms.mid.data;
    }
}



class stack_Push_Pop_GetMiddle_O1 {

    class Node {
        int val = 0;
        Node prev = null;
        Node next = null;

        public Node(int val) {
            this.val = val;
        }
    }

    Node head = null;
    Node tail = null;
    Node mid = null;
    int count = 0;

    public stack_Push_Pop_GetMiddle_O1() {
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
    }

    public void push(int val) {
        Node node = new Node(val);
        node.next = head.next;
        node.prev = head;
        head.next = node;
        node.next.prev = node;
        count++;
        if(count == 1) {
            mid = node;
        } else if(count%2 != 0) {
            mid = mid.prev;
        }
    }

    public int pop() {
        if(head.next == tail) {
            return -1;
        }

        Node top = head.next;
        head.next = top.next;
        head.next.prev = head;
        top.next = null;
        top.prev = null;
        count--;
        if(count == 0) {
            mid = null;
        } else if(count%2 == 0) {
            mid = mid.next;
        }

        return top.val;
    }

    public int getMiddle() {
        if(mid == null) {
            return -1;
        }

        return mid.val;
    }
}