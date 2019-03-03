package dropbox;

import java.util.*;

/**
 * in memory的map支持transaction，begin返回一个transactionID,
 * 实现 : put(transacId, k, v)
 *       get(transactId, k ,v)
 *       commit，
 * 需要考虑多个transaction交错的情况以及abort
 *
 *
 *
 * Isolation Level:
 *
 * 更新丢失(Lost update)
 *    两个事务都同时更新一行数据，一个事务对数据的更新把另一个事务对数据的更新覆盖了。
 *    比如CMS系统中，两个同时打开一篇文章进行修改，一个人先保存，另一个人后保存，
 *    后保存的就覆盖了先保存的那个人的内容，这就造成更新丢失。这是因为系统没有执行任
 *    何的锁操作，因此并发事务并没有被隔离开来。在并发事务处理带来的问题中，“更新丢失”
 *    通常应该是完全避免的。但防止更新丢失，并不能单靠数据库事务控制器来解决，需要应用
 *    程序对要更新的数据加必要的锁来解决，因此，防止更新丢失应该是应用的责任。
 *
 * 脏读（Dirty reads）
 *     一个事务读取到了另一个事务未提交的数据操作结果。这是相当危险的，因为很可能所有的
 *     操作都被回滚。
 *
 * 不可重复读（Non-repeatable Reads）
 *     一个事务对同一行数据重复读取两次，但是却得到了不同的结果。比如事务T1读取某一数据后，
 *     事务T2对其做了修改，当事务T1再次读该数据时得到与前一次不同的值。又叫虚读。
 *
 * 幻读（Phantom Reads）
 *     事务在操作过程中进行两次查询，第二次查询的结果包含了第一次查询中未出现的数据或者缺少
 *     了第一次查询中出现的数据（这里并不要求两次查询的SQL语句相同）。这是因为在两次查询过
 *     程中有另外一个事务插入数据造成的。
 *
 *     不可重复读的重点是修改某个记录字段，幻读的重点在于新增或者删除记录。
 *     对于前者, 只需要锁住满足条件的记录。对于后者, 要锁住满足条件及其相近的记录。
 *
 * “脏读”、“不可重复读”和“幻读”，其实都是数据库读一致性问题，必须由数据库提供一定的事务隔离机制来解决。
 *
 *                               脏读             不可重复读       幻读
 *  未提交读
 *  Read uncommitted              Yes               Yes           Yes
 *
 *  已提交读                        No                Yes          Yes
 *  Read committed
 *
 *  可重复读                        No                 No          Yes
 *  Repeatable read
 *
 *  可序列化                        No                 No           No
 *  Serializable
 *
 *  Read uncommitted: 允许脏读取，但不允许更新丢失。如果一个事务已经开始写数据，
 *                    则另外一个事务则不允许同时进行写操作，但允许其他事务读此行数据。
 *                    该隔离级别可以通过“排他写锁”实现。
 *
 *  Read committed:   允许不可重复读取，但不允许脏读取。这可以通过“瞬间共享读锁”和“排他写锁”实现。
 *                    读取数据的事务允许其他事务继续访问该行数据，但是未提交的写事务将会禁止其他事
 *                    务访问该行。
 *
 *  Repeatable read:  禁止不可重复读取和脏读取，但是有时可能出现幻读数据。这可以通过“共享读锁”和“排他写锁”实现。
 *                    读取数据的事务将会禁止写事务（但允许读事务），写事务则禁止任何其他事务
 *
 *                    事务与事务之间的隔离与互斥
 *
 *  Serializable:     提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。
 *                    仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作
 *                    的事务访问到。
 *
 *                    事务和非事务之间的隔离与互斥
 *
 *
 *
 *  lock algorithm：
 *      write a key which is being written by other transaction id, exception + roll back
 *      write a key which is being read by other transaction id, exception + roll back
 *      otherwise return OK
 *
 *      read  key which is being written by other transaction id, exception + roll back
 *      otherwise return OK
 *
 */
public class kvTransaction {

    public static void main(String[] args) {
        kvTransaction tr = new kvTransaction();

            tr.start();
            tr.start();
            //int val1 = tr.get(1, "a");
            //int val2 = tr.get(2, "b");
            tr.put(1, "a", 3);
            tr.put(2, "b", 4);
            tr.get(2, "a");
            tr.commit(1);
            tr.commit(2);

            //System.out.println(e.getMessage());
            tr.dump();

    }

    class Locker {
        public int readerId = -1;
        public int writerId = -1;
    }

    class Operation {
        public int type = 0;  //1: Update, 2: Delete
        public String key = "";
        public int value = 0;

        public Operation(int type , String key, int value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }
    }

    //store all the data
    private Map<String, Integer> data = new HashMap<>();

    // key is data key, value is read/write lock for this data key
    private Map<String, Locker> lockers = new HashMap<>();

    // key is transaction id
    // value is a list of data keys involved in this transaction
    private Map<Integer, List<String>> transactions = new HashMap<>();

    // key is transaction id
    // value is a list of roll back operations
    // it's a stack
    private Map<Integer, LinkedList<Operation>> rollback = new HashMap<>();


    private int count = 0;

    public void dump() {
        for(String key : data.keySet()) {
            System.out.println(String.format("key: %s, value: %d.", key, data.get(key)));
        }
    }

    public void start() {
        count++;
        transactions.put(count, new ArrayList<>());
    }

    public void commit(int transactionId) {
        cleanup(transactionId);
    }


    private void cleanup(int transactionId) {
        removeRollBack(transactionId);
        removeTransaction(transactionId);
    }

    // Remove rollback entry
    private void removeRollBack(int transactionId) {
        if(rollback.containsKey(transactionId)) {
            rollback.remove(transactionId);
        }
    }

    // Remove read/write lock + remove transaction entry
    private void removeTransaction(int transactionId) {
        if (transactions.containsKey(transactionId)) {
            // 1. Remove read/write lock of the dataMap hold by me
            for (String dataKey : transactions.get(transactionId)) {
                Locker lock = lockers.get(dataKey);
                if (lock.readerId == transactionId) {
                    lock.readerId = -1;
                }
                if (lock.writerId == transactionId) {
                    lock.writerId = -1;
                }
            }
            transactions.remove(transactionId);
        }
    }

    /**
     * write a key which is being written by other transaction id, exception + roll back
     * write a key which is being read by other transaction id, exception + roll back
     *
     */
    public void put(int transactionId, String dataKey, int dataValue) {
        if(!transactions.containsKey(transactionId)) {
            /*
            throw new IllegalMonitorStateException(
                    String.format("Put failed, transaction: %d, key: %s, value: %d. " +
                            "Reason: Invalid transaction id ",
                            transactionId, dataKey, dataValue));
             */
            System.out.println(String.format("Put failed, transaction: %d, key: %s, value: %d. " +
                            "Reason: Invalid transaction id ",
                    transactionId, dataKey, dataValue));
        }

        try {
            // add data key in transaction
            transactions.get(transactionId).add(dataKey);

            // fetch locker for data key
            if(!lockers.containsKey(dataKey)) {
                lockers.put(dataKey, new Locker());
            }

            Locker locker = lockers.get(dataKey);
            if((locker.readerId != -1 && locker.readerId != transactionId) ||
                    (locker.writerId != -1 && locker.writerId != transactionId)) {
                throw new Exception("Read/Write lock is hold by other transaction id.");
            }

            // lock writeLock
            locker.writerId = transactionId;

            //generate rollback op
            generateRollbackOp(transactionId, dataKey, dataValue);

            data.put(dataKey, dataValue);
        } catch (Exception e) {
            doRollback(transactionId);
            /*
            throw new IllegalMonitorStateException(
                    String.format("Put failed, transaction: %d, key: %s, value: %d. Reason:%s",
                            transactionId,
                            dataKey,
                            dataValue,
                            e.getMessage()));
                            */
            System.out.println(String.format("Put failed, transaction: %d, key: %s, value: %d. Reason:%s",
                    transactionId,
                    dataKey,
                    dataValue,
                    e.getMessage()));
        }
    }

    void generateRollbackOp(int transactionId, String dataKey, int dataValue) {
        if(!rollback.containsKey(transactionId)) {
            rollback.put(transactionId, new LinkedList<>());
        }
        LinkedList<Operation> OpStack = rollback.get(transactionId);

        Operation op = null;
        if(data.containsKey(dataKey)) {
            //update => update
            op = new Operation(1, dataKey, data.get(dataKey));
        } else {
            //create => delete
            op = new Operation(2, dataKey, dataValue);
        }

        OpStack.push(op);
    }

    /**
     *  write/read key which is being hold by me, OK
     *
     *  read  key which is being written by other transaction id, exception + roll back
     */
    public int get(int transactionId, String dataKey) {
        if(!transactions.containsKey(transactionId)) {
            /*
            throw new IllegalMonitorStateException(
                    String.format("Get failed, transaction: %d, key: %s. " +
                                    "Reason: Invalid transaction id",
                            transactionId, dataKey));
                            */
            System.out.println(String.format("Get failed, transaction: %d, key: %s. " +
                            "Reason: Invalid transaction id",
                    transactionId, dataKey));
        }

        try {
            // add data key in transaction
            transactions.get(transactionId).add(dataKey);

            // fetch locker for data key
            if(!lockers.containsKey(dataKey)) {
                lockers.put(dataKey, new Locker());
            }

            Locker locker = lockers.get(dataKey);
            if(locker.writerId != -1 && locker.writerId != transactionId) {
                throw new Exception("Write lock is hold by other transaction id.");
            }

            //lock readLock
            locker.readerId = transactionId;

            return data.getOrDefault(dataKey, -1);

        } catch (Exception e) {
            doRollback(transactionId);
            /*
            throw new IllegalMonitorStateException(
                    String.format("Get failed, transaction: %d, key: %s. Reason: %s",
                            transactionId, dataKey, e.getMessage()));
                            */
            System.out.println(String.format("Get failed, transaction: %d, key: %s. Reason: %s",
                    transactionId, dataKey, e.getMessage()));
        }
        return -1;
    }

    private void doRollback(int transactionId) {
        //do operations in rollback list
        if(rollback.containsKey(transactionId)) {
            LinkedList<Operation> opStack = rollback.get(transactionId);
            while(!opStack.isEmpty()) {
                Operation op = opStack.pop();
                doOperation(op);
            }
        }

        //cleanup
        cleanup(transactionId);
    }

    private void doOperation(Operation op) {
        if(op.type == 1) {
            //update
            data.put(op.key, op.value);
        } else if(op.type == 2) {
            //delete
            data.remove(op.key);
        }
    }
}
