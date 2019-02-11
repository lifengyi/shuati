package poc;

class MyThread implements Runnable {
    private int ticket = 5;
    private String name = null;

    public MyThread(String threadName) {
        this.name = threadName;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            if (ticket > 0) {
                System.out.println(String.format("thread name = %s, ticket = %d.", name, ticket--));
            }
        }
    }
}



public class thread_test {
    public static void main(String[] args) {
        //new Thread(new MyThread("Thread-1")).start();
        //new Thread(new MyThread("Thread-2")).start();
        //new Thread(new MyThread("Thread-3")).start();

        //vs
        MyThread mt = new MyThread("Thread-my");
        new Thread(mt).start();
        new Thread(mt).start();
        new Thread(mt).start();
    }
}

