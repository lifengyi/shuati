package dropbox;

import java.util.*;

public class readWriteLock_v2 {
}


/**
 * 1. writer is not reentrant
 *
 * 2. a) t1 get read lock
 *    b) t2 get write lock and wait
 *    c) t1 get read lock again(reentrant), then wait
 *
 *    dead lock
 */
class ReadWriteLock_v2 {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    public synchronized void lockRead() throws InterruptedException {
        while(writers > 0 && writeRequests > 0) {
            wait();
        }

        readers++;
    }

    public synchronized void unlockRead() {
        readers--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        writeRequests++;

        while(readers > 0 || writers > 0) {
            wait();
        }

        writeRequests--;
        writers++;
    }

    public synchronized void unlockWrite() {
        writers--;
        notifyAll();
    }
}


/**
 *  Improvement: Fix issue 2
 *
 *  Read rule: A thread is granted read reentrance
 *             1. if someone is wrting, return false
 *             2. if calling thread is holding the current read lock, return true;
 *             3. if someone send write read requests, return false;
 *             4. return true
 *
 *  Implementation:
 *             add a hashmap to record threads holding read lock
 *
 *  Write rule: A thread is granted write reentrance
 *             1. if someone is reading, return false;
 *             2. if calling thread is holding the current write lock, read true
 *             3. if someone is writing, return false;
 *             4. return true;
 *
 */

class ReadWriteLock_v3 {

    private Map<Thread, Integer> readLocks = new HashMap<>();

    private int writers = 0;
    private int writeRequests = 0;
    private Thread currentWriter = null;

    public synchronized void lockRead() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        while(!canGrantReadAccessToThread(currentThread)) {
            wait();
        }
        readLocks.put(currentThread, readLocks.getOrDefault(currentThread, 0) + 1);
    }

    public synchronized void unlockRead() {
        Thread currentThread = Thread.currentThread();
        if(!isHoldingReadLock(currentThread)) {
            throw new IllegalMonitorStateException("Not holding the read lock.");
        }
        int count = readLocks.get(currentThread);
        if(count == 1) {
            readLocks.remove(currentThread);
        } else {
            readLocks.put(currentThread, count - 1);
        }
        notifyAll();
    }


    public synchronized void lockWriter() throws InterruptedException {
        writeRequests++;
        Thread currentThread = Thread.currentThread();

        while(!canGrantWriteAccessToThread(currentThread)) {
            wait();
        }

        writeRequests--;
        writers++;
        currentWriter = currentThread;
    }

    public synchronized void unlockWriter() {
        Thread currentThread = Thread.currentThread();
        if(currentWriter == null || currentWriter != currentThread) {
            throw new IllegalMonitorStateException("Not holding the write lock");
        }

        writers--;
        if(writers == 0) {
            currentWriter = null;
        }
        notifyAll();
    }

    private boolean canGrantWriteAccessToThread(Thread thread) {
        if(readLocks.size() > 0) {
            return false;
        }
        if(currentWriter == null || currentWriter == thread) {
            return true;
        }
        if(writers > 0) {
            return false;
        }

        return true;
    }



    /**
     * Read rule: check writer -> myself is holder -> write requests
     */
    private boolean canGrantReadAccessToThread(Thread thread) {
        if(writers > 0) {
            return false;
        }

        if(isHoldingReadLock(thread)) {
            return true;
        }
        if(writeRequests > 0) {
            return false;
        }
        return true;
    }

    private boolean isHoldingReadLock(Thread thread) {
        return readLocks.containsKey(thread);
    }
}


/**
 *  Read -> Write
 *  Write => Read
 */
class ReadWriteLock_v4 {

    private Map<Thread, Integer> readLocks = new HashMap<>();

    private int writers = 0;
    private int writeRequests = 0;
    private Thread currentWriter = null;

    public synchronized void lockRead() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        while(!canGrantReadAccessToThread(currentThread)) {
            wait();
        }
        readLocks.put(currentThread, readLocks.getOrDefault(currentThread, 0) + 1);
    }

    public synchronized void unlockRead() {
        Thread currentThread = Thread.currentThread();
        if(!isHoldingReadLock(currentThread)) {
            throw new IllegalMonitorStateException("Not holding the read lock.");
        }
        int count = readLocks.get(currentThread);
        if(count == 1) {
            readLocks.remove(currentThread);
        } else {
            readLocks.put(currentThread, count - 1);
        }
        notifyAll();
    }


    public synchronized void lockWriter() throws InterruptedException {
        writeRequests++;
        Thread currentThread = Thread.currentThread();

        while(!canGrantWriteAccessToThread(currentThread)) {
            wait();
        }

        writeRequests--;
        writers++;
        currentWriter = currentThread;
    }

    public synchronized void unlockWriter() {
        Thread currentThread = Thread.currentThread();
        if(currentWriter == null || currentWriter != currentThread) {
            throw new IllegalMonitorStateException("Not holding the write lock");
        }

        writers--;
        if(writers == 0) {
            currentWriter = null;
        }
        notifyAll();
    }

    private boolean canGrantWriteAccessToThread(Thread thread) {
        //I'm the only reader
        if(isOnlyReader(thread)) {
            return true;
        }
        // the only place which we modified

        if(readLocks.size() > 0) {
            return false;
        }
        if(currentWriter == null || currentWriter == thread) {
            return true;
        }
        if(writers > 0) {
            return false;
        }

        return true;
    }

    private boolean isOnlyReader(Thread thread) {
        if(readLocks.size() == 1 && isHoldingReadLock(thread)) {
            return true;
        }
        return false;
    }


    /**
     * Read rule: check writer -> myself is holder -> write requests
     */
    private boolean canGrantReadAccessToThread(Thread thread) {
        //I'm the only writer
        if(isWriter(thread)) {
            return true;
        }
        // the only place which we modified

        if(writers > 0) {
            return false;
        }

        if(isHoldingReadLock(thread)) {
            return true;
        }
        if(writeRequests > 0) {
            return false;
        }
        return true;
    }

    private boolean isWriter(Thread thread) {
        if(currentWriter == thread) {
            return true;
        }
        return false;
    }

    private boolean isHoldingReadLock(Thread thread) {
        return readLocks.containsKey(thread);
    }
}



