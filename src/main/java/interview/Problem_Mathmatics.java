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

// tips: 学习数字的pop和push => 学习数字的移位 + 数字的溢出判断
class L7_Reverse_Integer {
    public int reverse(int x) {
        int res = 0;
        while(x != 0) {
            if(res > Integer.MAX_VALUE/10) return 0;
            if(res == Integer.MAX_VALUE/10 && x%10 > 7) return 0;
            if(res < Integer.MIN_VALUE/10) return 0;
            if(res == Integer.MIN_VALUE/10 && x%10 < -8) return 0;

            res = res * 10 + x % 10;

            x = x/10;
        }

        return res;
    }
}

class L9_Palindrome_Number {
    public boolean isPalindrome(int x) {
        if(x < 0) return false;
        else if (x == 0) return true;

        int ret = 0, cur = x;     // cannot use x directly since we still need it to do comparison
        while(cur != 0) {
            if(ret > Integer.MAX_VALUE/10 || (ret == Integer.MAX_VALUE/10 && cur % 10 > 7)) {
                return false;
            }

            ret = ret * 10 + cur % 10;
            cur = cur/10;
        }

        return ret == x;
    }
}

class L12_Integer_to_Roman___ {
    public String intToRoman(int num) {
        if(num < 1 || num > 3999) {
            return "";
        }

        String[] ones =      {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        String[] tens =      {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] hundreds =  {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] thousands = {"", "M", "MM", "MMM"};

        // can be replaced by StringBuild which is much faster since no new object will be created.
        return thousands[num/1000] + hundreds[num%1000/100] + tens[num%100/10] + ones[num%10];
    }

    // use dictionary, but the dictionary is build by code but not static
    public String intToRoman_v2(int num) {
        if(num > 3999 || num < 1) {
            return "";
        }

        String[][] dic = new String[4][10];
        for(int i = 0; i < 4; ++i) {
            dic[i] = new String[10];
        }

        initUnitsPlace(dic[0]);
        initTensPlace(dic[1]);
        initHundredsPlace(dic[2]);
        initThousandsPlace(dic[3]);

        StringBuilder sb = new StringBuilder();
        int index = 0;
        while(num != 0) {
            int tmp = num%10;
            sb.insert(0, dic[index][tmp]);
            num = num/10;
            index++;
        }
        return sb.toString();
    }

    void initUnitsPlace(String[] dic) {
        dic[0] = "";
        dic[1] = "I";
        dic[2] = "II";
        dic[3] = "III";
        dic[4] = "IV";
        dic[5] = "V";
        dic[6] = "VI";
        dic[7] = "VII";
        dic[8] = "VIII";
        dic[9] = "IX";
    }

    void initTensPlace(String[] dic) {
        dic[0] = "";
        dic[1] = "X";
        dic[2] = "XX";
        dic[3] = "XXX";
        dic[4] = "XL";
        dic[5] = "L";
        dic[6] = "LX";
        dic[7] = "LXX";
        dic[8] = "LXXX";
        dic[9] = "XC";
    }

    void initHundredsPlace(String[] dic) {
        dic[0] = "";
        dic[1] = "C";
        dic[2] = "CC";
        dic[3] = "CCC";
        dic[4] = "CD";
        dic[5] = "D";
        dic[6] = "DC";
        dic[7] = "DCC";
        dic[8] = "DCCC";
        dic[9] = "CM";
    }

    void initThousandsPlace(String[] dic) {
        dic[0] = "";
        dic[1] = "M";
        dic[2] = "MM";
        dic[3] = "MMM";
    }
}


// use state machine
// tips : !!!! 学习数字的溢出判断
class L8_String_to_Integer_v2 {
    public int myAtoi(String s) {
        // 0: intial:
        //    0-9    => calculate ret and go 2,
        //    ' '  => ignore and continue,  keep 0
        //    '+'/'-' => set operator, go 1
        //    letters/. => go 3
        // 1. read operator:
        //    0    => ignore and continue, keep 1 ?
        //    1-9  => calculate ret using operator,  go 2
        //    others => go 3
        // 2. read a digit:
        //    0-9 => calculate ret using operator and keep 2, overflow(max/min) and set ret, go 3
        //    all others => 3
        // 3. stop and return: return ret
        int status = 0;
        int res = 0;
        int operator = 1;

        for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if(status == 0) {
                if(c == ' ') {
                    continue;
                } else if(c >= '0' && c <= '9') {
                    res = res * 10 + operator * (c - '0');
                    status = 2;
                } else if (c == '+' || c == '-') {
                    operator = c == '-' ? -1 : 1;
                    status = 1;
                } else {
                    status = 3;
                }
            } else if(status == 1) {
                if(c == '0') {
                    continue;
                } else if (c >= '1' && c <= '9') {
                    res = res * 10 + operator * (c - '0');
                    status = 2;
                } else {
                    status = 3;
                }
            } else if(status == 2) {
                if(c >= '0' && c <= '9') {
                    if(res > Integer.MAX_VALUE/10 || (res == Integer.MAX_VALUE/10 && c - '0' > 7)) {
                        res = Integer.MAX_VALUE;
                        status = 3;
                    } else if (res < Integer.MIN_VALUE/10 || (res == Integer.MIN_VALUE/10 && c - '0' > 8)) {
                        res = Integer.MIN_VALUE;
                        status = 3;
                    } else {
                        res = res * 10 + operator * (c - '0');
                    }
                } else {
                    status = 3;
                }

            }

            if(status == 3) {
                break;
            }
        }
        return res;

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
