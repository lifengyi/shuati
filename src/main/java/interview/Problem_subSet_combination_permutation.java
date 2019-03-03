package interview;

import java.util.*;

public class Problem_subSet_combination_permutation {

}

/**
 * subset
 * DFS遍历每一个节点，每次DFS遍历时，当前子集就是一个有效的子集
 * 每次遍历时，都需要将当前索引进行+1操作
 *
 * Combination：某个子集满足一定特殊的情况
 * 基于上面subset的解法进行求解，需要判断combination满足的条件
 * I: 数据不重复，但可用多次，结果sum满足K：
 *              a. 排序数组，这样就能实现当超过K时，不在处理后续数据
 *              b. 对每次DFS时的有效子集判断是否满足K
 *              c. 每次DFS索引不需要进行+1操作，因为当前数据可用多次
 *
 * II:数据有重复，但只能用1次，结果去重且sum满足K：
 *              a. 排序数组
 *              b. 对每次DFS有效子集判断是否满足K
 *              c. 由于需要去重，所以遍历的过程中遇到相同数据，
 *                 处理第一个，且要跳过后续重复数据        <<<---- 表示重复值和后面其他元素在一起后，出现重复结果；
 *                                                           重复值中的前者所求得结果基本已经涵盖了后者所能求得的结果
 *              d. 每次DFS索引进行+1操作
 *
 * III: 数据不重复（但只能用1次），结果个数满足n结果满足K
 *              a. 排序数组
 *              b. 对每次DFS有效自己判断是否满足K和N
 *              c. 每次DFS索引需要进行+1操作
 *
 * IV: 同上述III一样,唯一区别：数据可以多次使用，故索引不需要进行+1操作
 *     本题可以通过该方法DFS求得所有解，但是题目要求求总数，
 *     故极限情况下会超时，采用DP更恰当
 *
 *
 * Permutation:
 * 和subset/combination的解法基本上没什么关系，但是很类似
 *
 * I: 普通的DFS，每次DFS遍历都是从输入数组的首部开始进行遍历，
 *    同时记录visited标记，对已经处理的数据标注，不会重复处理
 *    每次DFS遍历的时候动态维护visited和临时的solution解
 *    当满足solution里填充了所有数据后，刷新到最终输出数据集
 *
 * II: 和I基本一样，唯一区别是要去重，排列去重条件满足：
 *     1. 当DFS遍历到某一位不是首位， and
 *     2. 该位的数值和前一位的数值相同，and    <<<---- 1和2，基本上和Combination II类似
 *
 *     3. 前一位并没有被选中             <<<---- 特殊的条件，和Combination区别在于这里
 *                                            表示重复值之间互相出现，并填补对方的位置，导致数据重复；
 *                                           的多个重复值需要被捆绑在一起来看待；
 *                                           重复值中的前者求得的结果和后者基本上是相同的；
 *
 *     思想上可以考虑成将数值相同若干位捆绑成虚拟的一个数值，
 *     相同数值的若干位只有在第一位被选中时，前面的才能被选中，
 *     否则一律跳过不处理，即去重；
 *
 */

class L90_Subsets_II_ {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return result;
        }

        Arrays.sort(nums);
        List<Integer> subset = new ArrayList<>();
        dfs(nums, 0, subset, result);
        return result;
    }

    private void dfs(int[] nums, int start, List<Integer> subset, List<List<Integer>> result) {
        result.add(new ArrayList(subset));

        for(int i = start; i < nums.length; ++i) {
            if(i != start && nums[i] == nums[i - 1]) {
                continue;
            }
            subset.add(nums[i]);
            dfs(nums, i + 1, subset, result);
            subset.remove(subset.size() - 1);
        }
    }
}

class L39_Combination_Sum_ {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ret = new ArrayList<>();
        if(candidates == null || candidates.length == 0) {
            return ret;
        }

        Arrays.sort(candidates);
        List<Integer> comb = new ArrayList<>();
        dfs(candidates, 0, 0, target, comb, ret);
        return ret;
    }

    void dfs(int[] candidates, int index, int sum, int target,
             List<Integer> comb, List<List<Integer>> result) {
        if(sum == target) {
            result.add(new ArrayList(comb));
            return;
        }

        //Start from index to avoid duplicate results
        for(int i = index; i < candidates.length; ++i) {
            if(sum + candidates[i] > target) {
                break;
            }
            comb.add(candidates[i]);
            dfs(candidates, i, sum + candidates[i], target, comb, result);
            comb.remove(comb.size() - 1);
        }
    }
}


class L40_Combination_Sum_II_ {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if(candidates == null || candidates.length == 0) {
            return result;
        }

        Arrays.sort(candidates);
        List<Integer> comb = new ArrayList<>();
        dfs(candidates, 0, 0, target, comb, result);
        return result;
    }

    void dfs(int[] candidates, int start, int sum, int target,
             List<Integer> comb, List<List<Integer>> result) {
        if(sum == target) {
            result.add(new ArrayList(comb));
            return;
        }

        for(int i = start; i < candidates.length; ++i) {
            if(i != start && candidates[i] == candidates[i - 1]) {
                continue;
            }

            if(sum + candidates[i] > target) {
                break;
            }

            comb.add(candidates[i]);
            dfs(candidates, i + 1, sum + candidates[i], target, comb, result);
            comb.remove(comb.size() - 1);
        }
    }
}


class L216_Combination_Sum_III_ {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> comb = new ArrayList<>();
        dfs(1, k, n, comb, result);
        return result;
    }

    void dfs(int start, int num, int remaining,
             List<Integer> comb, List<List<Integer>> result) {
        if(num == 0 && remaining == 0) {
            result.add(new ArrayList(comb));
            return;
        } else if(num == 0 || remaining == 0) {
            return;
        }

        for(int i = start; i <= 9; ++i) {
            if(remaining - i < 0) {
                break;
            }

            comb.add(i);
            dfs(i + 1, num - 1, remaining - i, comb, result);
            comb.remove(comb.size() - 1);
        }
    }
}


class L377_Combination_Sum_IV {
    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1;

        for(int i = 1; i <= target; ++i) {
            for(int j = 0; j < nums.length; ++j) {
                if(i >= nums[j]) {
                    dp[i] += dp[i - nums[j]];
                }
            }
        }
        return dp[target];
    }
}



class L46_Permutations_ {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return result;
        }

        List<Integer> list = new ArrayList<>();
        int[] visited = new int[nums.length];
        dfs(nums, list, visited, result);
        return result;
    }

    void dfs(int[] nums, List<Integer> list, int[] visited, List<List<Integer>> result) {
        if(list.size() == nums.length) {
            result.add(new ArrayList(list));
            return;
        }

        for(int i = 0; i < nums.length; ++i) {
            if(visited[i] == 0) {
                visited[i] = 1;
                list.add(nums[i]);
                dfs(nums, list, visited, result);
                list.remove(list.size() - 1);
                visited[i] = 0;
            }
        }
    }
}



class L47_Permutations_II_ {
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return result;
        }

        Arrays.sort(nums);
        List<Integer> list = new ArrayList<>();
        int[] visited = new int[nums.length];
        dfs(nums, list, visited, result);
        return result;
    }

    void dfs(int[] nums, List<Integer> list, int[] visited, List<List<Integer>> result) {
        if(list.size() == nums.length) {
            result.add(new ArrayList(list));
            return;
        }

        for(int i = 0; i < nums.length; ++i) {
            if(i > 0 && nums[i] == nums[i - 1] && visited[i - 1] == 0) {
                continue;
            }

            if(visited[i] == 1 ) {
                continue;
            }

            visited[i] = 1;
            list.add(nums[i]);
            dfs(nums, list, visited, result);
            visited[i] = 0;
            list.remove(list.size() - 1);
        }
    }
}










