package interview;

public class Problem_Mathmatics {
}

class gcd {
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


/**
 * 四平方和定理，如果一个数有因子4，
 * 那么去掉4因子之后并不影响其平方和性质
 * 如果一个数除余8等于7，那么其是由4个数平方构成
 */
class L279_Perfect_Squares_Mathematics {
    public int numSquares(int n) {
        while(n%4 == 0) {
            n /= 4;
        }
        if(n%8 == 7) {
            return 4;
        }

        for(int i = 0; i * i <= n; ++i){
            int j = (int)Math.sqrt(n - i * i);
            if((j * j + i * i) == n) {
                return i == 0 ? 1 : 2;
            }
        }

        return 3;
    }
}
