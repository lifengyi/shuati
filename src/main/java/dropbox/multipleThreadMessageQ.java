package dropbox;


import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;


/**
 *  多线程爬虫： 需要沟通几个问题：
 *  1， 是否检测重复网页， 是： 增加set
 *
 *  2. 是否可以假设内存queue肯定能装下所有网页，
 *     例如： 分配2G内存给queue和set， 每部分有1g内存
 *           假设每个url占用1k长度，那么上限处理是1M的网页url
 *
 *           这个单机多线程的爬虫只能爬< 一百万的链接，也就是在处理任务来之前需要知道处理的规模，
 *           如果超过就不应该用这个单机爬虫系统。如果处理作业的规模超过了系统处理能力还是需要它来
 *           处理的话，可以设置一个capacity参数，如果Queue的URL接近容量了，让所有爬虫线程把已
 *           经抓到链接写log输出，等log写完了，结束线程
 *
 *  3. 如何回收所有的子线程，如果2成立，那么3就不是问题
 *
 */


public class multipleThreadMessageQ {
    public static void main(String[] args) throws InterruptedException {
        Manager manager = new Manager(4);
        manager.submitTask("son task 1");

        manager.submitTask("son task 2");
        manager.submitTask("son task 3");

        manager.submitTask("son task 4");
        manager.submitTask("son task 5");
        manager.submitTask("son task 6");

        manager.submitTask("mom task");
        manager.submitTask("son task 7");
        manager.submitTask("son task 8");

        manager.execute();

        while(true) {
            if(manager.checkWorkDone()) {
                break;
            } else {
                System.out.println("Work is not done.");
                Thread.sleep(10 * 1000);
            }
        }

        manager.shutdown();
        System.out.println("End.");
    }
}


class Manager {
    private int numberOfWorkers = 0;
    private Map<Integer, Thread> workers = null;
    private Work work = null;

    private MyBlockingQueue bq = null;

    public Manager(int size) {
        this.numberOfWorkers = size;

        this.work = new Work();
        this.bq = new MyBlockingQueue(10);
        this.work.setQueue(bq);

        this.workers = new HashMap<>();
        for(int i = 0; i < numberOfWorkers; ++i) {
            workers.put(i, new Thread(work));
        }
    }

    public void execute() {
        for(int key : workers.keySet()) {
            workers.get(key).start();
        }
    }

    public void submitTask(String task) {
        bq.put(task);
    }

    public boolean checkWorkDone() {
        for(int key : workers.keySet()) {
            if(workers.get(key).getState() != Thread.State.WAITING) {
                return false;
            }
        }

        return bq.size() == 0;
    }

    public void shutdown() {
        for(int i = 0; i < numberOfWorkers; ++i) {
            bq.put("end task");
        }
    }
}


class Work implements Runnable{

    private MyBlockingQueue bq = null;
    private int status = 0;

    public void setQueue(MyBlockingQueue bq) {
        this.bq = bq;
        this.status = 1;
    }

    public void run() {
        while(status == 1) {
            String task = bq.get();
            if(task.equals("end task")) {
                System.out.println(String.format("thread:%d, get end task msg",
                        Thread.currentThread().getId()));
                break;
            } else {
                handle(task);
            }
        }
    }

    private void handle(String task) {
        System.out.println(String.format("thread:%d, get task: %s.",
                Thread.currentThread().getId(), task));
        if(task.equals("mom task")) {
            bq.put("mom task");
        }

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyBlockingQueue_simple {

    private List<Object> queue = new LinkedList<>();
    private int  limit = 10;

    public MyBlockingQueue_simple(int limit){
        this.limit = limit;
    }


    public synchronized void enqueue(Object item)
            throws InterruptedException  {
        while(this.queue.size() == this.limit) {
            wait();
        }
        if(this.queue.size() == 0) {
            notifyAll();
        }
        this.queue.add(item);
    }


    public synchronized Object dequeue()
            throws InterruptedException{
        while(this.queue.size() == 0){
            wait();
        }
        if(this.queue.size() == this.limit){
            notifyAll();
        }

        return this.queue.remove(0);
    }

}

class MyBlockingQueue {

    class Node {
        String value = null;
        Node next = null;
        public Node(String value) {
            this.value = value;
        }
    }

    private int capacity = 0;

    private Node head;
    private Node last;
    private AtomicInteger size = null;


    private ReentrantLock putLock = new ReentrantLock();
    private Condition notFull = putLock.newCondition();     // sent by put/get

    private ReentrantLock getLock = new ReentrantLock();
    private Condition notEmpty = getLock.newCondition();    // sent by put

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.head = this.last = new Node("Dummy Header");
        size = new AtomicInteger(0);
    }

    public int size() {
        return size.get();
    }

    public void put(String task) {
        int curCounter = -1;
        putLock.lock();
        try {
            /**
             * A thread can also wake up without being notified,
             * interrupted, or timing out, a so-called spurious wakeup.
             * While this will rarely occur in practice, applications
             * must guard against it by testing for the condition that
             * should have caused the thread to be awakened, and continuing
             * to wait if the condition is not satisfied. In other words,
             * waits should always occur in loops, like this one:
             */
             while(size.get() == capacity) {
                 notFull.await();
             }
             enqueue(task);
             curCounter = size.getAndIncrement();
             if(curCounter + 1 < capacity) {
                 notFull.signal();
             }
        } catch(Exception e) {
            System.out.println("Catch exception: " + e.getMessage());
        }finally {
            putLock.unlock();
        }
        if(curCounter == 0) {
            signalNotEmpty();
        }
    }

    private void signalNotEmpty() {
        getLock.lock();
        try {
            notEmpty.signal();
        } finally {
            getLock.unlock();
        }
    }

    public String get() {
        int curCounter = -1;
        String result = null;
        getLock.lock();
        try {
            while(size.get() == 0) {
                notEmpty.await();
            }
            result = dequeue();
            curCounter = size.getAndDecrement();
            if(curCounter - 1 > 0) {
                notEmpty.signal();
            }
        } catch (Exception e) {
            System.out.println("Catch exception: " + e.getMessage());
        } finally {
            getLock.unlock();
        }

        if(curCounter == capacity) {
            signalNotFull();
        }
        return result;
    }

    private void signalNotFull() {
        putLock.lock();
        try{
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    private void enqueue(String task) {
        Node node = new Node(task);
        last.next = node;
        last = last.next;
    }

    private String dequeue() {
        Node first = head.next;
        if (first == last) {
            last = head;
        }
        head.next = first.next;

        first.next = null;
        return first.value;
    }
}
