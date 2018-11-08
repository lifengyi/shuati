package com.stevenli.interview.easy.interview;

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
