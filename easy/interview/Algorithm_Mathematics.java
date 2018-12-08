package interview;

public class Algorithm_Mathematics {

    private boolean isPrime(int num) {
        for(int i = 2; i * i <= num; i++) {
            if(num % i == 0)
                return false;
        }
        return num != 1;
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