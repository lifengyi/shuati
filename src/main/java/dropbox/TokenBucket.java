package dropbox;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;


public class TokenBucket {

    public static void main(String[] args) {
        MyTokenBucket tb = new MyTokenBucket(11, 0.5);
        Worker worker = new Worker(tb);
        for(int i = 0; i < 30; ++i) {
            new Thread(worker).start();
        }
    }
}

class Worker implements Runnable {
    private MyTokenBucket tb = null;
    private Random rand = null;

    public Worker(MyTokenBucket tb) {
        this.tb = tb;
        this.rand = new Random();
    }

    public void run() {
        while(true) {

            tb.get(3);

            Date date = new Date();
            System.out.println(String.format("%s thread:%d, get token 3 successfully.",
                    date.toString(),
                    Thread.currentThread().getId()));

            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyTokenBucket {

    private int capacity = 0;
    private double fillRate = 0L;
    private long lastRefillTimeStamp = 0;

    //private AtomicInteger size = null;
    private int size = 0;

    private ReentrantLock getLock = new ReentrantLock();
    private Condition notEmpty = getLock.newCondition();

    private ReentrantLock putLock = new ReentrantLock();

    public MyTokenBucket(int capacity, double fillRate) {
        this.capacity = capacity;
        this.fillRate = fillRate;
        this.size = capacity;
        this.lastRefillTimeStamp = System.currentTimeMillis()/1000;
    }

    public void get(int nTokens) {
        if(nTokens == 0) {
            return;
        }

        getLock.lock();
        try {
            while(size < nTokens) {
                notEmpty.await(10, TimeUnit.SECONDS);
                put();
            }

            size -= nTokens;
            if(size > 0) {      // this code is needed when put/refill doesn't exist.
                notEmpty.signalAll();
            }
        } catch(Exception e) {
            System.out.println("Catch exception: %s"
                    + e.getMessage());
        }finally {
            getLock.unlock();
        }
    }

    public void put() {
        int remainingTokens = -1, newTokens = -1;
        //if(putLock.tryLock()) {
            //try{
                long currentTimeStamp = System.currentTimeMillis()/1000;
                if(currentTimeStamp != lastRefillTimeStamp) {
                    newTokens = (int)(fillRate * (currentTimeStamp - lastRefillTimeStamp));
                    remainingTokens = size;
                    //size.set(Math.min(capacity, newTokens + remainingTokens));
                    size = Math.min(capacity, newTokens + remainingTokens);
                    lastRefillTimeStamp = currentTimeStamp;
                    System.out.println(String.format("\nthread %d wake up, add token to %d.\n",
                            Thread.currentThread().getId(),
                            Math.min(capacity, newTokens + remainingTokens)));
                }
            //} finally {
          //      putLock.unlock();
            //}
            //if(remainingTokens == 0 && newTokens > 0) {
            //    signalNotEmpty();
            //}
        //}
    }

    /*
    void signalNotEmpty() {
        getLock.lock();
        try {
            notEmpty.notifyAll();
        } finally {
            getLock.unlock();
        }
    }
    */
}
