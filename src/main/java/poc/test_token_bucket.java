package poc;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

class test_token_bucket {
    public static void main(String[] args) {
        TokenBucket tb = new TokenBucket(10, 1);
        Worker worker = new Worker(tb);
        for(int i = 0; i < 5; ++i) {
            new Thread(worker).start();
        }
    }
}

class Worker implements Runnable {
    private TokenBucket tb = null;
    private Random rand = null;

    public Worker(TokenBucket tb) {
        this.tb = tb;
        this.rand = new Random();
    }

    public void run() {
        while(true) {
            Date date = new Date();
            //System.out.println(String.format("%s thread:%d, try to get token.",
            //        date.toString(),
             //       Thread.currentThread().getId()));

            int num = 3;
            tb.get(num);
            date = new Date();
            System.out.println(String.format("%s thread:%d, get token %d successfully.",
                    date.toString(),
                    Thread.currentThread().getId(),
                    num));
            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class TokenBucket {

    private int capacity = 0;
    private double fillRate = 0L;
    private long lastRefillTimeStamp = 0;

    private AtomicInteger size = null;

    private ReentrantLock getLock = new ReentrantLock();
    private Condition notEmpty = getLock.newCondition();

    private ReentrantLock putLock = new ReentrantLock();

    public TokenBucket(int capacity, double fillRate) {
        this.capacity = capacity;
        this.fillRate = fillRate;
        this.size = new AtomicInteger(capacity);
        this.lastRefillTimeStamp = System.currentTimeMillis()/1000;
    }

    public void get(int nTokens) {
        if(nTokens == 0) {
            return;
        }

        getLock.lock();
        try {
            while(size.get() < nTokens) {
                notEmpty.await(10, TimeUnit.SECONDS);
                put();
            }

            int curTokens = size.getAndAdd(-nTokens);
            if(curTokens - nTokens > 0) {
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
        if(putLock.tryLock()) {
            try{
                long currentTimeStamp = System.currentTimeMillis()/1000;
                if(currentTimeStamp != lastRefillTimeStamp) {
                    newTokens = (int)(fillRate * (currentTimeStamp - lastRefillTimeStamp));
                    remainingTokens = size.get();
                    size.set(Math.min(capacity, newTokens + remainingTokens));
                    lastRefillTimeStamp = currentTimeStamp;
                    log(String.format("remaining token: %d, add to %d.",
                            remainingTokens,
                            Math.min(capacity, newTokens + remainingTokens)));
                }
            } finally {
                putLock.unlock();
            }
            //if(remainingTokens == 0 && newTokens > 0) {
            //    signalNotEmpty();
            //}
        }
    }

    void signalNotEmpty() {
        getLock.lock();
        try {
            notEmpty.notifyAll();
        } finally {
            getLock.unlock();
        }
    }

    void log(String str) {
        Date date = new Date();
        System.out.println(String.format("%s, thread:%d, %s",
                date.toString(),
                Thread.currentThread().getId(),
                str));
    }
}
