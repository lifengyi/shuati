package dropbox;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Thread.*;

public class logHit {

    /**
     * 1. Circular array做，误差分析
     *    真是世界里一般使用memecache + TTL 实现rate limiter
     *    即可以使用hashmap，但是需要一个单独的垃圾回收机制，需要单独线程实现，
     *    所以选择circular array
     *
     * 2. 怎么做测试，unit test, correct and memory, use dummy time for memory test
     *    Change the function signature, add timestamp as input parameter.
     *
     * 3. 多线程。master slave model
     *
     *
     * 4. 多线程的话 lock和unlock怎么写，modify code
     *    使用读写锁，或者ConcurrentHashMap(但需要实现垃圾回收)
     *    真是世界一般使用memcache+TTL实现rate limiter，如果此题使用hashmap实现，则ocurrentHashMap也不错
     *
     * 5. 不用chaining array的话， 怎么做，HashMap or queue
     *    In real world, we use memcache to store the pair with TTL
     *
     *
     * 1. 加全局锁，两个方法加上synchronized
     *
     * 2. 封装方法调用，把命令放到queue上，然后server在另一端不断地取task执行，
     *    这样解耦了consumer和producer，异步化方法调用
     *
     * 3. ConcurrentHashMap来代替数组，因为log是读少写多，只需要在getHit的时候加锁
     *
     * 4. 如果只有固定的几个admin，那就固定给一个  ConcurrentHashMap，用admin ID当key，内层就可以HashMap了，因为admin们不会影响别人
     *
     * 5. 在4的基础上，如果admin并不是一定要全局的数据（可以只做些sampling），那可以用ThreadLocal，完全抛弃锁。
     *
     * 6. AtomicLongAdder 是一个不错的方法
     */

    public static void main(String[] args) throws InterruptedException {
        logHit proc = new logHit();
        proc.logHit();
        sleep(5*1000);

        for(int i = 0; i < 10; ++i) {
            proc.logHit();
            proc.logHit();
            proc.logHit();
            proc.logHit();
            proc.logHit();
            sleep(6*1000);
        }

        sleep(10 * 1000);
        proc.logHit();

        System.out.println(proc.getHit());

        System.out.println(Arrays.toString(proc.timestamps));
        System.out.println(Arrays.toString(proc.counters));
    }

    int size = 0;
    long[] timestamps = null;
    long[] counters = null;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public logHit() {
        this.size = 60; //seconds
        this.timestamps = new long[size];
        this.counters = new long[size];
    }

    public void logHit() {
        long time = System.currentTimeMillis()/1000;
        int index = (int)time%size;

        lock.writeLock().lock();
        if(timestamps[index] != time) {
            timestamps[index] = time;
            counters[index] = 1;
        } else {
            counters[index]++;
        }
        lock.writeLock().unlock();
    }

    /**
     * Get all the hits in latest 5 mins
     */
    public long getHit() {
        int counter = 0;
        long currentTime = System.currentTimeMillis()/1000;

        lock.readLock().lock();
        for(int i = 0; i < size; ++i) {
            if(currentTime - timestamps[i] < size) {
                counter += counters[i];
            }
        }
        lock.readLock().unlock();
        return counter;
    }
}
