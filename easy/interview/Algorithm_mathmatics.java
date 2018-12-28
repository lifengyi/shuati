package interview;

public class Algorithm_mathmatics {

    /**
     * Great Common Divisor
     * @param a
     * @param b
     * @return
     */
    int gcd(int a , int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

class L263_Ugly_Number_ {
    public boolean isUgly(int num) {
        if(num <= 0) {
            return false;
        } else if(num == 1) {
            return true;
        }

        for(int i = 2; i <= 5; ++i) {
            while(num%i == 0) {
                num = num/i;
            }
        }
        return num == 1;
    }
}
