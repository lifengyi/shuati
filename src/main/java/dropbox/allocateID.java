package dropbox;


    /**
     *  Define # of buckets : K
     *  Split n nubmer into K buckets
     *
     *  Use BST/Segment tree to store buckets
     *  space: O(n/8)
     *  time:  get:  O(klogk) + O(n/k)
     *         check/release : O(logk) + O(1)
     *
     *
     *  Use HashTable to store buckets
     *  bucketId <-> bucket (use ByteSet to store phone number)
     *  space: O(n/8)
     *  time: get: O(k) + O(n/k)
     *        check/release: O(1);
     *
     *
     *  space: O(4n)
     *  time:  get: O(1)
     *         check/releasae: O(1)
     */

import java.util.*;

class Solution{
    public static void main(String[] args) {
        //PhoneDirectories_1 pd = new PhoneDirectories_1(5);
        //PhoneDirectories_2 pd = new PhoneDirectories_2(5);
        PhoneDirectories_3 pd = new PhoneDirectories_3(12, 5);

        for(int i = 0; i < 15; ++i) {
            System.out.println(pd.get());
        }
        System.out.println(pd.check(2));
        System.out.println(pd.check(4));
        pd.release(2);
        pd.release(3);
        pd.release(1);
        pd.release(11);
        System.out.println(pd.check(2));
        System.out.println(pd.check(4));

        System.out.println("Get again.");
        for(int i = 0; i < 8; ++i) {
            System.out.println(pd.get());
        }

    }
}


// space: O(n/8) + O(4k)
// time:  get: O(1)
//        check: O(1)
//        release: O(1)
class PhoneDirectories_3 {
    private int capacity = 0;
    private int totalBuckets = 0;

    private int normalBucketSize = 0;
    private int lastBucketSize = 0;

    private BitSet[] buckets = null;
    private int[] positions = null;

    private int nextPos = 0;

    public PhoneDirectories_3(int n, int k) {
        this.capacity = n + 1;
        this.totalBuckets = k + 1;
        this.normalBucketSize = capacity/k;
        this.lastBucketSize = capacity%k;

        buckets = new BitSet[totalBuckets];
        positions = new int[totalBuckets];
        for(int i = 0; i < totalBuckets; ++i) {
            buckets[i] = new BitSet();                  // ****** bug 1
            positions[i] = (i + 1) % (totalBuckets);
        }
    }

    public int get() {
        // no available bucket
        if(positions[nextPos] == -1) {
            return -1;
        }

        //current bucket is not full
        int currentBucketIndex = nextPos;                       // ****** bug 2
        BitSet currentBucket = buckets[currentBucketIndex];

        int currentBucketSize = normalBucketSize;

        if(currentBucketIndex == totalBuckets - 1) {
            //last bucket size may be less than normal bucket size
            currentBucketSize = lastBucketSize;
        }

        int index = currentBucket.nextClearBit(0);
        if(index < currentBucketSize) {
            //find an avaliable slot;
            currentBucket.set(index);

            //tips: check next clear set
            if(currentBucket.nextClearBit(0) == currentBucketSize) {        // ****** bug 3
                int posIndex = nextPos;
                nextPos = positions[nextPos];
                positions[posIndex] = -1;
            }

            int result = index + currentBucketIndex * normalBucketSize;
            return result;
        }
        return -1;
    }

    public boolean check(int number) {
        if(number >= capacity) {
            return false;
        }

        int pos = number/normalBucketSize;
        int index = number%normalBucketSize;
        BitSet bucket = buckets[pos];
        return !bucket.get(index);
    }

    public void release(int number) {
        if(number >= capacity) {
            return;
        }

        int pos = number/normalBucketSize;
        int index = number%normalBucketSize;
        BitSet bucket = buckets[pos];
        bucket.clear(index);

        if(positions[pos] == -1) {                          // ****** bug 5
            positions[pos] = nextPos;
            nextPos = pos;
        }
    }


}

// sapce: O(n/8)
// time: get: O(n)
//       check: O(1)
//       release: O(1)
class PhoneDirectories_2 {

    private int capacity = 0;
    private BitSet bitset = null;

    public PhoneDirectories_2(int n) {
        this.capacity = n + 1;
        this.bitset = new BitSet(capacity);
    }

    public int get() {
        int index = bitset.nextClearBit(0);
        if(index < capacity) {
            bitset.set(index);
            return index;
        }
        return -1;
    }

    public boolean check(int number) {
        if(number >= capacity) {
            return false;
        }

        return !bitset.get(number);
    }

    public void release(int number) {
        if(number >= capacity) {
            return;
        }
        bitset.clear(number);
    }

}


//space: O(4n) bytes
//time: get: O(1)
//      check O(1)
//      release O(1)
class PhoneDirectories_1 {

    private int capacity = 0;
    private int[] directory = null;
    private int nextPos = 0;

    public PhoneDirectories_1(int n) {
        this.capacity = n + 1;
        this.directory = new int[capacity];
        for(int i = 0; i < capacity; ++i) {
            directory[i] = (i + 1) % capacity;
        }
    }

    public int get() {
        if(directory[nextPos] == -1) {
            return -1;
        }
        int index = nextPos;
        nextPos = directory[nextPos];
        directory[index] = -1;
        return index;
    }

    public boolean check(int number) {
        if(number >= capacity) {
            return false;
        }
        return directory[number] != -1;
    }

    public void release(int number) {
        if(number >= capacity || directory[number] != -1) {
            return;
        }

        directory[number] = nextPos;
        nextPos = number;
    }
}