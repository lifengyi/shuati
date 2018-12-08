package interview;

import java.util.*;

public class Interview_Uber {
}


/**
 * 一个类似于单调递增栈实现去重
 */
class LintCode_1308_Factor_Combinations {
    public List<List<Integer>> getFactors(int n) {
        List<List<Integer>> ret = new ArrayList<>();
        if(n <= 1) {
            return ret;
        }

        List<Integer> comb = new ArrayList<>();
        dfs(n, 0, comb, ret);
        return ret;
    }

    void dfs(int n, int lastNumber, List<Integer> comb, List<List<Integer>> ret) {
        for(int i = 2; i <= Math.sqrt(n); ++i) {
            if(n%i == 0 && i >= lastNumber) {
                comb.add(i);
                comb.add(n/i);
                ret.add(new ArrayList(comb));

                comb.remove(comb.size() - 1);
                dfs(n/i, i, comb, ret);
                comb.remove(comb.size() - 1);
            }
        }
    }
}
