package dropbox;


import java.util.*;

public class multipleThreadsMessageQ_v2 {

    public static void main(String[] args) {
        try {
            Manager_v2 m = new Manager_v2(2);
            m.init("seed url");
            m.exec();
            m.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class Manager_v2 {
    private int capacity = 0;
    private Map<Integer, Thread> threads = null;
    private Worker_v2 worker = null;
    private MyWorkerBuffer mwb = null;

    public Manager_v2(int n) {
        this.capacity = n;
        this.threads = new HashMap<>();
        this.worker = new Worker_v2();
        this.mwb = new MyWorkerBuffer();
        this.worker.setQueue(this.mwb);

        for(int i = 0; i < this.capacity; ++i) {
            threads.put(i,  new Thread(this.worker));
        }
    }

    public void init(String seed) {
        this.mwb.enqueue(seed);
    }

    public void exec() {
        for(int threadId : this.threads.keySet()) {
            this.threads.get(threadId).start();
        }
    }

    public void join() throws InterruptedException {
        for(int threadId : this.threads.keySet()) {
            this.threads.get(threadId).join();
        }
    }
}


class Worker_v2 implements Runnable {
    private MyWorkerBuffer mwb = null;

    public void setQueue(MyWorkerBuffer mbq) {
        this.mwb = mbq;
    }

    public void run() {
        while(true) {
            String msg = mwb.dequeue();
            if(msg.equals("stop")) {
                // ctrl msg
                break;
            } else {
                // data msg
                System.out.println(String.format("Thread: %d, process url: %s",
                        Thread.currentThread().getId(),
                        msg));
                List<String> urls = getUrls(msg);
                try{
                    Thread.sleep(1*1000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                for(String url : urls) {
                    mwb.enqueue(url);
                }
            }
        }
    }

    public List<String> getUrls(String url) {
        List<String> urls = new ArrayList<>();
        urls.add(url + "1");
        urls.add(url + "2");
        urls.add(url + "3");
        return urls;
    }

}



/*
    simple blocking queue: 1 lock for read/write
    and will stop caching message if the size reacth to capacity
*/
class MyWorkerBuffer {
    private int capacity = 10;
    private LinkedList<String> list = new LinkedList<>();
    private Set<String> visited = new HashSet<>();
    private boolean outOfMemory = false;

    public synchronized String dequeue() {
        while(list.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        notifyAll();
        return list.poll();
    }

    public synchronized void enqueue(String msg) {
        System.out.println("Queue size: " + list.size());
        if(this.outOfMemory == true || this.list.size() >= capacity) {
            this.outOfMemory = true;
            System.out.println("No memeory for new msg: " + msg);
            return;
        }

        if(!visited.contains(msg)) {
            list.offer(msg);
            notifyAll();
        }
    }
}



