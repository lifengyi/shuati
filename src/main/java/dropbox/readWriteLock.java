package dropbox;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class readWriteLock {
    private int readers;
    private int writers;
    private int waitingWriters;

    /**
     * Get a read lock
     *
     * @throws InterruptedException
     */
    public synchronized void lockRead() throws InterruptedException {
        while (writers > 0 || waitingWriters > 0) {
            this.wait();
        }
        readers++;
    }

    /**
     * Release a read lock
     */
    public synchronized void unLockRead() {
        readers--;
        notifyAll();
    }

    /**
     * Get a write lock
     *
     * @throws InterruptedException
     */
    public synchronized void lockWrite() throws InterruptedException {
        waitingWriters++;
        while (readers > 0 || writers > 0) {
            this.wait();
        }
        waitingWriters--;
        writers++;
    }

    /**
     * Release a write lock
     */
    public synchronized void unLockWrite() {
        writers--;
        notifyAll();
    }
}

/**
 *
 * 上面实现的简单的读写锁，在一些情况下容易导致死锁。例如：
 *
 * （1）读的重进入。若一个线程获得了读锁，在释放该锁之前再一次申请读锁，而此时已经有其他线程正在申请写锁，
 *     此时第一个线程由于有写请求而阻塞，而第二个线程由于第一个线程还未释放读锁也进入阻塞状态，形成死锁。
 *     正常的情况应该是，如果一个线程已经获取了读锁，再次申请读锁时可以直接获取读锁，而不会因为有其他写
 *     请求而阻塞。
 *
 * （2）写的重进入。若一个线程获得了写锁，在释放该锁之前再一次申请写锁，此时会因为第一个写锁还未释放而进入
 *     阻塞状态。正常的情况应该是，如果一个线程已经获取了写锁，再次请求写锁时应该可以直接获取写锁。
 *
 * （3）从读到写的重进入。若一个线程获得了读锁，在释放该锁之前又申请了写锁，此时会因为第一个读锁还未释放而进
 *     入阻塞状态。正常的情况应该是，如果一个线程获取了读锁，当再次申请写锁时，如果当前除了自身外没有其他获
 *     取了读锁的线程，那么应当直接获得写锁。
 *
 * （4）从写到读的重进入。若一个线程获得了写锁，在释放该锁之前又申请了读锁，此时会因为第一个写锁还未释放而进入
 *     阻塞状态。正常的情况应该是，如果一个线程获得了写锁，再次申请读锁时可以直接获得锁。
 *
 *
 * 针对以上四种重进入的情况，我们应该对原先的读写锁进行一些优化：
 *
 * （1）针对读的重进入，我们可以在读写锁中维护一个map，key为线程对象，value为该线程获取的读锁数目。当申请获
 *     取读锁时，首先判断当前线程是否存在于map中且读锁数目是否大于0，若是，表示该线程已经获得过了读锁，应该
 *     允许其再次获得读锁。获取读锁后，将map中当前线程的读锁数目加1
 *
 * （2）针对写的重进入，我们可以在读写锁中维护一个线程对象writingThread保存当前获取写锁的线程，同时用一个整
 *     型变量writeAccesses记录当前获取写锁的线程的写锁数目。申请获取写锁时，首先判断当前线程是否等于writingThread，
 *     若是表示当前线程已经获得了写锁，允许其再次获得写锁。获取写锁后，将writeAccesses加1，并把writingThread指向当
 *     前线程对象
 *
 * （3）针对读到写的重进入，首先需要判断当前线程是否是唯一一个获得读锁的线程，这可以通过检查map来判断，
 *     若是，则允许该线程获取写锁。
 *
 * （4）针对写到读的重进入，首先需要判断当前线程是否已经获得了写锁，这可以通过writingThread是否等于当前线程对象来判断，
 *     若是，则允许该线程获得读锁。
 *
 */

class ReadWriteLock {

    private Map<Thread, Integer> readingThreads;
    private Thread writingThread = null;
    private int writingAccesses = 0;
    private int waitingWriters = 0;

    public ReadWriteLock() {
        // TODO Auto-generated constructor stub
        this.readingThreads = new HashMap<Thread, Integer>();
    }

    /**
     * Get a read lock
     *
     * @throws InterruptedException
     */
    public synchronized void lockRead() throws InterruptedException {
        Thread thread = Thread.currentThread();
        while (!this.canGrantReadAccess(thread)) {
            this.wait();
        }
        // Put current thread into list and count the read accesses
        this.readingThreads.put(thread, this.getReadAccessors(thread) + 1);
    }

    /**
     * Release read lock
     */
    public synchronized void unLockRead() {
        Thread thread = Thread.currentThread();
        // If current thread has not a read lock, throw exception
        if (!this.isReader(thread)) {
            throw new IllegalMonitorStateException("Calling Thread does not"
                    + " hold a read lock on this ReadWriteLock");
        }
        int accessCount = this.getReadAccessors(thread) - 1;
        if (accessCount <= 0) {
            this.readingThreads.remove(thread);
        } else {
            this.readingThreads.put(thread, accessCount);
        }
        this.notifyAll();
    }

    /**
     * Get a write lock
     *
     * @throws InterruptedException
     */
    public synchronized void lockWrite() throws InterruptedException {
        this.waitingWriters++;
        Thread thread = Thread.currentThread();
        while (!this.canGrantWriteAccess(thread)) {
            this.wait();
        }
        this.waitingWriters--;
        this.writingThread = thread;
        this.writingAccesses++;
    }

    /**
     * Release write lock
     */
    public synchronized void unLockWrite() {
        Thread thread = Thread.currentThread();
        // If current thread has not a write lock, throw exception
        if (!this.isWriter(thread)) {
            throw new IllegalMonitorStateException("Calling Thread does not"
                    + " hold the write lock on this ReadWriteLock");
        }
        this.writingAccesses--;
        if (this.writingAccesses <= 0) {
            this.writingThread = null;
        }
        this.notifyAll();
    }

    /**
     * Check if the thread can be granted read access
     *
     * @param thread
     * @return
     */
    private boolean canGrantReadAccess(Thread thread) {
        // If the thread is already a reader or a writer, return true
        if (this.isReader(thread) || this.isWriter(thread))
            return true;
        // If there are writers or waiting writers, return falses
        if (this.writingThread != null || this.waitingWriters > 0)
            return false;
        return true;
    }

    /**
     * Check if the thread can be granted write access
     *
     * @param thread
     * @return
     */
    private boolean canGrantWriteAccess(Thread thread) {
        // If the thread is the only reader or already a writer, return true
        if (this.isOnlyReader(thread) || this.isWriter(thread))
            return true;
        // If there are readers or writers, return false
        if (this.readingThreads.size() > 0 || this.writingThread != null)
            return false;
        return true;
    }

    /**
     * Get the count of read accesses of the thread
     *
     * @param thread
     * @return
     */
    private int getReadAccessors(Thread thread) {
        Integer accessCount = this.readingThreads.get(thread);
        if (accessCount == null) {
            return 0;
        }
        return accessCount.intValue();
    }

    /**
     * Check if the thread has got read access
     *
     * @param thread
     * @return
     */
    private boolean isReader(Thread thread) {
        if (this.readingThreads.get(thread) != null
                && this.readingThreads.get(thread).intValue() > 0)
            return true;
        return false;
    }

    /**
     * Check if the thread is the only one that has got read access
     *
     * @param thread
     * @return
     */
    private boolean isOnlyReader(Thread thread) {
        if (this.readingThreads.size() == 1
                && this.readingThreads.get(thread) != null)
            return true;
        return false;
    }

    /**
     * Check if the thread has got write access
     *
     * @param thread
     * @return
     */
    private boolean isWriter(Thread thread) {
        return this.writingThread != null && this.writingThread == thread ? true
                : false;
    }




    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ReadWriteLock lock = new ReadWriteLock();
        Resource res = new Resource(lock);

        // testReaderWriter(res);
        // testMultiReaders(res);
        testMultiWriters(res);
    }

    /**
     * Test case of a read thread and write thread
     *
     * @param res
     */
    public static void testReaderWriter(Resource res) {
        new Thread(new ReadTask(res), "reader").start();
        new Thread(new WriteTask(res), "writer").start();
    }

    /**
     * Test case of three read threads
     *
     * @param res
     */
    public static void testMultiReaders(Resource res) {
        new Thread(new ReadTask(res), "reader-01").start();
        new Thread(new ReadTask(res), "reader-02").start();
        new Thread(new ReadTask(res), "reader-03").start();
    }

    /**
     * Test case of three write threads
     *
     * @param res
     */
    public static void testMultiWriters(Resource res) {
        new Thread(new WriteTask(res), "writer-01").start();
        new Thread(new WriteTask(res), "writer-02").start();
        new Thread(new WriteTask(res), "writer-03").start();
    }
}

/**
 * Resource POJO
 *
 * @author Charles Chen
 * @date 2014年10月2日
 */
class Resource {
    private ReadWriteLock lock;

    public Resource(ReadWriteLock lock) {
        // TODO Auto-generated constructor stub
        this.lock = lock;
    }

    public void read() {
        try {
            lock.lockRead();
            for (int i = 0; i < 5; i++) {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println("Read " + i);
            }
        } catch (Exception e) {
        } finally {
            lock.unLockRead();
        }
    }

    public void write() {
        try {
            lock.lockWrite();
            for (int i = 0; i < 5; i++) {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println("Write " + i);
            }
        } catch (Exception e) {
        } finally {
            try {
                lock.unLockWrite();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

/**
 * Reader
 *
 * @author Charles Chen
 * @date 2014年10月2日
 */
class ReadTask implements Runnable {
    private Resource res;

    public ReadTask(Resource res) {
        // TODO Auto-generated constructor stub
        this.res = res;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        res.read();
    }
}

/**
 * Writer
 *
 * @author Charles Chen
 * @date 2014年10月2日
 */
class WriteTask implements Runnable {
    private Resource res;

    public WriteTask(Resource res) {
        // TODO Auto-generated constructor stub
        this.res = res;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        res.write();
    }
}
