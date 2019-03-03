package poc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class test_token {
    int size = 0;
    int maxCapacity;
    int tokensPerUnit;
    int timeInNanos;
    LocalDateTime lastFillTime;

    test_token(int maxCapacity, int tokensPerUnit, int time, ChronoUnit unit) {
        this.maxCapacity = maxCapacity;
        this.tokensPerUnit = tokensPerUnit;
        timeInNanos = (int) Duration.of(time, unit).toNanos();
        lastFillTime = LocalDateTime.now();
        size = 0;
    }

    public void getTokens(int n) {
        if (n < 0 || n > maxCapacity) {
            throw new RuntimeException("You can only take a number of token greater than or equal to 0 and less than or equal to maxCapacity");
        }
        int taken = 0;

        while (true) {
            synchronized (this) {
                refill();
                if (size >= 1) {  // <====优化后的代码
                    size -= 1;
                    taken++;
                    System.out.println("Thread " + Thread.currentThread().getId() + " gets 1 tokens");
                }
            }
            if (taken == n) {
                // System.out.println("Thread "+Thread.currentThread().getId()+" gets "+taken+" tokens");
                break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void put(int n) {
        size = Math.min(maxCapacity, size+n);
    }

    private synchronized void refill() {
            LocalDateTime now = LocalDateTime.now();
        long durationInNano = Duration.between(lastFillTime, now).toNanos();
        long tokensToAdd = Math.max(0, tokensPerUnit*durationInNano/timeInNanos);
        if(tokensToAdd>0) {
            size = Math.min(maxCapacity, (int) (size+tokensToAdd));
            lastFillTime = now;
        }
    }
}
