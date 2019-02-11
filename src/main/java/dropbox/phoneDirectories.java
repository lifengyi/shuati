package dropbox;

public class phoneDirectories {
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



}
