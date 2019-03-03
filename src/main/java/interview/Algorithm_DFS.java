package interview;

import java.util.*;

public class Algorithm_DFS {

}

/**
 * 要求： 求所有子集
 * 1. 结果数据集需要去重
 * 2. 原有数据集中元素只能用1次
 *
 *
 * 思路：
 * 1. 排序原有数据集
 * 2. DFS递归，每次索引需要+1
 */
class L78_SubSets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return ret;
        }

        Arrays.sort(nums);
        List<Integer> subset = new ArrayList<>();
        dfs(nums, 0, subset, ret);
        return ret;
    }

    private void dfs(int[] nums, int start, List<Integer> subset, List<List<Integer>> result) {
        result.add(new ArrayList(subset));
        for(int i = start; i < nums.length; ++i) {
            subset.add(nums[i]);
            dfs(nums, i + 1, subset, result);
            subset.remove(subset.size() - 1);
        }
    }

    public List<List<Integer>> subsets_NoRecursion(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        int size = nums.length;

        for(int i = 0; i < (1 << size); ++i) {
            List<Integer> element = new ArrayList<>();
            for(int j = 0; j < size; ++j) {
                if((i & (1 << j)) != 0) {
                    element.add(nums[j]);
                }
            }
            ret.add(element);
        }

        return ret;
    }
}

/**
 * 要求： 求所有子集
 *
 * 1. 输入数据集有重复数据, 数据只能使用1次
 * 2. 结果数据集需要去重
 *
 *
 * 思路：
 * 1. 排序原有数据集
 * 2. DFS递归，每次索引需要+1
 * 3. DFS递归，循环体中非头结点，遇到和前一个节点重复，需要跳过不处理
 */
class L90_Subsets_II {
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


/**
 * 要求： 求子集中满足特殊条件，即子集和满足特殊值的所有子集
 *
 * 1. 输入数据集没有重复项，但是数据集中的数据可以无限次使用
 * 2. 输出集需要去重，不能有重复方案
 *
 * Input: candidates = [2,3,6,7], target = 7,
 * A solution set is:
 *  [
 *      [7],
 *      [2,2,3]
 *  ]
 *
 * Input: candidates = [2,3,5], target = 8,
 * A solution set is:
 *  [
 *      [2,2,2,2],
 *      [2,3,3],
 *      [3,5]
 *  ]
 *
 *
 */
class L39_Combination_Sum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if(candidates == null || candidates.length == 0) {
            return result;
        }

        Arrays.sort(candidates);
        List<Integer> combination = new ArrayList<>();
        dfs(candidates, 0, target, combination, result);
        return result;
    }

    void dfs(int[] candidates, int start, int remaining, List<Integer> combination, List<List<Integer>> result) {
        if(remaining == 0) {
            result.add(new ArrayList(combination));
            return;
        } else if(remaining < 0) {
            return;
        }

        for(int i = start; i < candidates.length; ++i) {
            combination.add(candidates[i]);
            dfs(candidates, i, remaining - candidates[i], combination, result);
            combination.remove(combination.size() - 1);
        }
    }
}


/**
 * 要求： 求子集中满足特殊条件，即子集和满足特殊值的所有子集
 *
 * 1. 输入数据集有重复项，数据集中的数据只使用1次
 * 2. 输出集需要去重，不能有重复方案
 *
 * Input: candidates = [10,1,2,7,6,1,5], target = 8,
 * A solution set is:
 *  [
 *      [1, 7],
 *      [1, 2, 5],
 *      [2, 6],
 *      [1, 1, 6]
 *  ]
 *
 * Input: candidates = [2,5,2,1,2], target = 5,
 * A solution set is:
 *  [
 *      [1,2,2],
 *      [5]
 *  ]
 */
class L40_Combination_Sum_II {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if(candidates == null || candidates.length == 0) {
            return result;
        }

        Arrays.sort(candidates);
        List<Integer> combination = new ArrayList<>();
        dfs(candidates, 0, target, combination, result);
        return result;
    }

    void dfs(int[] candidates, int start, int remaining, List<Integer> combination, List<List<Integer>> result) {
        if(remaining == 0) {
            result.add(new ArrayList(combination));
            return;
        } else if(remaining < 0) {
            return;
        }

        for(int i = start; i < candidates.length; ++i) {
            if(i != start && candidates[i] == candidates[i - 1]) {
                continue;
            }
            combination.add(candidates[i]);
            dfs(candidates, i + 1, remaining - candidates[i], combination, result);
            combination.remove(combination.size() - 1);
        }
    }
}

/**
 * 和I类似，输入为1-9，检测条件为两项：
 * 1. 和为n
 * 2. 组合中的数字必须为k
 *
 * 输入数据集没有重复项，但只能用1次
 */
class L216_Combination_Sum_III {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        if(k <= 0 || n <= 0 || k > n || (k != 1 && k == n)) {
            return result;
        }

        List<Integer> combination = new ArrayList<>();
        dfs(1, k, n, combination, result);
        return result;
    }

    void dfs(int index, int level, int remaining, List<Integer> combination, List<List<Integer>> result) {
        if(remaining == 0) {
            if(level == 0) {
                result.add(new ArrayList(combination));
            }
            return;
        } else if(remaining < 0) {
            return;
        }

        if(level == 0) {
            return;
        }
        level--;

        for(int i = index; i < 10; ++i) {
            combination.add(i);
            dfs(i + 1, level, remaining - i, combination, result);
            combination.remove(combination.size() - 1);
        }
    }
}

class L131_Palindrome_Partitioning {
    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        if(s == null || s.length() == 0) {
            return result;
        }

        char[] array = s.toCharArray();
        List<String> combination = new ArrayList<>();
        dfs(array, 0, combination, result);
        return result;
    }

    void dfs(char[] array, int start, List<String> combination, List<List<String>> result) {
        if(start == array.length) {
            result.add(new ArrayList(combination));
            return;
        }

        for(int i = start; i < array.length; ++i) {
            String newString = new String(array, start, i - start + 1);
            if(isPalindrome(newString)) {
                combination.add(newString);
                dfs(array, i + 1, combination, result);
                combination.remove(combination.size() - 1);
            }
        }
    }

    boolean isPalindrome(String s) {
        char[] array = s.toCharArray();
        int start = 0, end = array.length - 1;
        while(start < end) {
            if(array[start] != array[end]) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }
}

class L46_Permutations {
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

class L47_Permutations_II {
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

class L31_Next_Permutation {
    public void nextPermutation(int[] nums) {
        LinkedList<Integer> stack = new LinkedList<>();
        int len = nums.length;
        int begin = 0, prevIndex = -1;
        for(int i = len - 1; i >= 0; --i) {
            if(stack.isEmpty() || nums[i] >= nums[stack.peek()]) {
                stack.push(i);
                continue;
            }
            while(!stack.isEmpty() && nums[i] < nums[stack.peek()]) {
                prevIndex = stack.pop();
            }
            swap(nums, prevIndex, i);
            begin = i + 1;
            break;
        }

        //Arrays.Basic.sort(nums, begin, len);
        reverse(nums, begin);
    }

    public void nextPermutation_v2(int[] nums) {
        if(nums.length < 2) {
            return;
        }
        if(nums.length == 2) {
            swap(nums, 0, 1);
            return;
        }

        int begin = -1;
        for(int i = nums.length - 2; i >= 0; --i) {
            if(nums[i] < nums[i + 1]) {
                begin = i;
                break;
            }
        }


        if(begin != -1) {
            for(int j = nums.length - 1; j >= 0; --j) {
                if(nums[j] > nums[begin]) {
                    swap(nums, begin, j);
                    break;
                }
            }
        }

        reverse(nums, begin + 1);
    }

    void reverse(int[] nums, int start) {
        int left = start, right = nums.length - 1;
        while(left < right) {
            swap(nums, left, right);
            left++;
            right--;
        }
    }

    void swap(int[] nums, int p, int q) {
        nums[p] = nums[p] ^ nums[q];
        nums[q] = nums[p] ^ nums[q];
        nums[p] = nums[p] ^ nums[q];
    }
}


class L484_Find_Permutation {
    public int[] findPermutation(String s) {
        if(s == null || s.length() == 0) {
            return new int[0];
        }

        int stringLen = s.length();
        int[] result = new int[stringLen + 1];
        for(int i = 0; i < stringLen + 1; ++i) {
            result[i] = i + 1;
        }

        int begin = -1, end = -1;
        char[] array = s.toCharArray();
        for(int i = 0; i < stringLen; ++i) {
            if(array[i] == 'D') {
                if(begin == -1) {
                    begin = i;
                    end = i + 1;
                } else {
                    end++;
                }
            } else {
                if(begin != -1) {
                    reverse(result, begin, end);
                    begin = -1;
                    end = -1;
                }
            }
        }

        if(begin != -1) {
            reverse(result, begin, end);
        }

        return result;
    }

    void reverse(int[] nums, int start, int end) {
        while(start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }

    void swap(int[] nums, int p, int q) {
        nums[p] = nums[p] ^ nums[q];
        nums[q] = nums[p] ^ nums[q];
        nums[p] = nums[p] ^ nums[q];
    }
}

class L51_N_Queens {
    public List<List<String>> solveNQueens_v2(int n) {
        List<List<String>> result = new ArrayList<>();
        if(n < 1){
            return result;
        } else if(n == 1) {
            List<String> solution = new ArrayList<>();
            solution.add("Q");
            result.add(solution);
            return result;
        }

        List<Integer> solution = new ArrayList<>();
        dfs(n, 0, solution, result);
        return result;
    }


    void dfs(int n, int x, List<Integer> solution, List<List<String>> result) {
        if(x == n) {
            result.add(drawChessBoard(n, solution));
            return;
        }

        for(int i = 0; i < n; ++i) {
            if(validate(x, i, n, solution)) {
                solution.add(getNumber(x, i, n));
                dfs(n, x + 1, solution, result);
                solution.remove(solution.size() - 1);
            }
        }
    }

    List<String> drawChessBoard(int n , List<Integer> solution) {
        List<String> result = new ArrayList<>();
        for(int node : solution) {
            StringBuilder sb = new StringBuilder();
            int y = node%n;
            for(int i = 0; i < n; ++i) {
                if(i == y) {
                    sb.append("Q");
                } else {
                    sb.append(".");
                }
            }
            result.add(sb.toString());
        }
        return result;
    }

    int getNumber(int x, int y, int n) {
        return x * n + y;
    }

    boolean validate(int x, int y, int n, List<Integer> solution) {
        for(int node : solution) {
            int nx = node/n;
            int ny = node%n;

            if(y == ny) {
                return false;
            }
            if(x + y == nx + ny) {
                return false;
            }
            if(x-y == nx-ny) {
                return false;
            }
        }
        return true;
    }
}

class L332_Reconstruct_Itinerary {
    public List<String> findItinerary(String[][] tickets) {
        LinkedList<String> result = new LinkedList<>();
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for(String[] ticket : tickets) {
            if(!graph.containsKey(ticket[0])) {
                graph.put(ticket[0], new PriorityQueue<String>());
            }
            graph.get(ticket[0]).offer(ticket[1]);
        }

        dfs("JFK", graph, result);
        return (List<String>)result;
    }

    void dfs(String from, Map<String, PriorityQueue<String>> graph, LinkedList<String> result) {
        PriorityQueue<String> targets = graph.get(from);
        while(targets != null && !targets.isEmpty()) {
            dfs(targets.poll(), graph, result);
        }

        result.offerFirst(from);
    }
}



/**
 * 可以使用UnionFind来做，同时也可以使用DFS来做
 * 思考：以上两种都需要使用额外的空间，都是O(n * m)的空间
 *      并查集需要保存所有点的状态，DFS需要对所有点做visited的保存
 *
 * 如果不适用额外的空间，那么如何操作？
 * 使用DFS遍历所有元素，同时不设置visited数组，而是直接修改矩阵本身内容
 * 将访问过的元素由1设置为0，这样保证下次遍历的时候不会再访问到；
 *
 */
class L695_Max_Area_of_Island {
    public int maxAreaOfIsland(int[][] grid) {
        int max_area = 0;
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j < grid[0].length; j++)
                if(grid[i][j] == 1)max_area = Math.max(max_area, AreaOfIsland(grid, i, j));
        return max_area;
    }

    public int AreaOfIsland(int[][] grid, int i, int j) {
        if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j] == 1) {
            grid[i][j] = 0;
            return 1 + AreaOfIsland(grid, i + 1, j) + AreaOfIsland(grid, i - 1, j) + AreaOfIsland(grid, i, j - 1) + AreaOfIsland(grid, i, j + 1);
        }
        return 0;
    }
}


class L17_Letter_Combinations_of_a_Phone_Number {
    public String[] dict = {"", "", "abc", "def", "ghi",
            "jkl", "mno", "pqrs", "tuv",
            "wxyz"};
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if(digits == null || digits.length() == 0) {
            return result;
        }

        StringBuilder sb = new StringBuilder();
        dfs(digits, 0, sb, result);
        return result;
    }

    void dfs(String digits, int index, StringBuilder sb, List<String> result) {
        if(index == digits.length()) {
            result.add(sb.toString());
            return;
        }

        int indexOfDict = digits.charAt(index) - '0';
        String target = dict[indexOfDict];
        for(int i = 0; i < target.length(); ++i) {
            sb.append(target.charAt(i));
            dfs(digits, index + 1, sb, result);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}


/**
 * Input is 147, suppose we have finished 1 + 4 and reach 7
 * startIndex = 2 (7's index)
 * curTotalVal = 5;
 * returnBack(last) = 4;
 *
 * Now when we go for multiplication, we need last
 * value for evaluation as follows:
 * current val = curTotalVal - last + last * current val
 *
 * First we subtract last and then update last * current val for evaluation,
 * new last is last * current val
 * current val = 5 - 4 + 4 * 7
 * last = 4* 7
 */
class L282_Expression_Add_Operators {
    public List<String> addOperators(String num, int target) {
        List<String> result = new ArrayList<>();
        if(num == null || num.length() == 0) {
            return result;
        }

        dfs(num, 0, 0, 0, target, "", result);
        return result;
    }

    void dfs(String num, int startIndex, long curTotalVal, long returnBack,
             int target, String expr, List<String> result) {
        if(startIndex == num.length()) {
            if(curTotalVal == (long)target) {
                result.add(expr);
            }
            return;
        }

        for(int i = startIndex; i < num.length(); ++i) {
            if(i != startIndex && num.charAt(startIndex) == '0') {
                break;   //a number starts with 0
            }

            long curVal = Long.parseLong(num.substring(startIndex, i + 1));

            if(startIndex == 0) {
                dfs(num, i + 1, curTotalVal + curVal, curVal,
                        target, "" + curVal, result);
            } else {
                dfs(num, i + 1, curTotalVal + curVal, curVal,
                        target, expr + "+" + curVal, result);
                dfs(num, i + 1, curTotalVal - curVal, -curVal,
                        target, expr + "-" + curVal, result);
                dfs(num, i + 1, curTotalVal - returnBack + returnBack * curVal,
                        returnBack * curVal, target, expr + "*" + curVal, result);
            }
        }
    }
}


/**
 * time: O(k^n)
 * space: O(n) for the stack
 *
 * Can be optimized by search with memorization.
 *
 * 题目描述
 * 1.
 * 1. 给一个整数数组nums和一个正数k，判断是否能把nums分为k个非空子集，使得每一个子集的元素和相同。
 * 2. 注意：
 * 1. 1 <= k <= len(nums) <= 16
 * 2. 0 < nums[i] < 10000
 * 2. 样例
 * 1. 输入: nums = [4, 3, 2, 3, 5, 2, 1], k = 4
 * 2. 输出: True
 * 3. *解释: *nums可以分为4个子集：(5), (1, 4), (2,3), (2,3)。每个子集和相等。
 *
 * 解题思路分析
 *
 * 这个题即使k = 2，仍然没有什么除搜索外好的解法，所有的解法时间复杂度都在多项式时间以上，所以事实上它是一个NP问题 (https://baike.baidu.com/item/NP%E9%97%AE%E9%A2%98/2860567?fr=aladdin)，更不用说当k > 2时。这里能采用的方法仅仅是不断搜索。但是即使是搜索，也有许多的优化和方式可做，这里给出一种直观的搜索方法，和另一种较快的DP方法。
 * 方法一（常规深度优先搜索）：
 * 很容易想到每一个子集和必须为target = sum(nums) / k，如果除不尽，那么一定会返回False。
 * 先模拟出k个子集，对于nums中最后一个数n，将其弹出。遍历k个子集，只要加入n后，这个子集和不超过target，就把它加入这个子集当中，然后带着当前的选择，继续递归搜索nums（此时nums已不包括n）。
 * 重复上述过程，如果nums最后为空，那么说明搜索成功了。
 * 这种方法十分直观，但是速度很慢，不过有一些加速方法可以采用，这里列举其中一些：
 * k个子集从前到后递归，如果当前的子集和与前一个子集和相同，那么这个子集就不用试了，因为把n放到这个子集和放到前一个子集没有差别。我们只关心能否搜索到，并不关心具体的分配方案。
 * 先把nums排序，并优先先放入最大的元素，这样能减少许多搜索路径。
 * 一旦找到nums[i] > target，那么就直接返回False。因为如果某一个元素，都超过了target，那么就一定不合题。
 * 复杂度分析：
 * 时间复杂度：O(k ^ N)，其中N时nums的长度，k是子集数。如果采用了优化方案a，则复杂度至少降到O(k ^ (N - k) * k!)，因为一开始会跳过很多和为0的子集，至少前k个元素的搜索次数不超过O(k!)。
 * 空间复杂度：O(N)， 用于函数调用栈。
 * 方法二：（构造一种序列化的搜索，相较方法一，减少冗余；不过该方法难度较大，且不好想）
 * 方法一尽管经过优化，但是理论的时间复杂度仍然很大，其中的重要原因是*存在部分重复的搜索，*当nums和当前所有子集和相同时，之后的搜索运行了不止一次，而且如果只是分组排列不同，其实结果无差别，但在方法一中有可能会继续搜索。
 * 方法一并不能解决这些问题。要解决重复搜索的问题，一种有效的方法是*构造一种序列化的搜索，并对于已搜索过的序列不重复搜索。*由此引出方法二。
 * 同方法一，首先target = sum(nums) / k。接着用变量used表示nums[i]的使用情况，当且仅当nums[i]已经用过时，used的第i位为1。
 * 仍然是搜索，只不过这次的搜索方法是是*寻找一个nums的序列，使得按照这个序列使用nums的元素时，能够正好构造出一个接一个的子集。*接着我们的任务就变成了构造出这样一个序列。
 * 构造过程中，在确定序列中下一个元素时，需要遍历nums中的元素，*只有当used的对应位为0时，才表示这个元素还没有在序列中，*也即还没有用过，可以去考虑这个元素。那么这个没有用过的元素，能否作为序列中的下一个元素呢？
 * 这就需要一个变量todo，表示当前所有未用元素的和，此时序列下一个元素的大小不能超过remain = (todo - 1) % target + 1。这个式子的含义就是todo % target，只不过如果todo % target == 0 时，会得到target。remain也就是序列到目前为止，正在构造的这个子集和，还需要的值。
 * 当nums中的一个元素被认为能作为序列的下一个元素时，就把其对应的used中的位置为1，并让todo减去这个元素值，进行下一次递归搜索。
 * 注意当used给定时，todo是固定的！为了加速搜索过程，可*用一个数组visit记录当前used是否访问过，如果访问过，不用再往下搜索了，直接返回False。*因为如果访问过当前的used并且能搜索成功的话，整个递归栈早就停止搜索返回True了。
 * 当used的所有位全为1时，todo应该是0，此时搜索结束，返回True。
 * 复杂度分析：
 * 时间复杂度：O(N * 2 ^ N)，N是nums的长度，因为只有2 ^ N个used情况，每种情况其自身只需要O(N)的时间去遍历nums。
 * 空间复杂度O(2 ^ N)，主要用于visit数组。
 *
 */
class L698_Partition_to_K_Equal_Sum_Subsets {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = getSum(nums);
        if(sum%k != 0) {
            return false;
        }

        int target = sum/k;
        Arrays.sort(nums);

        if(nums[nums.length - 1] > target) {
            return false;
        }

        int i = nums.length - 1;
        for(; i >= 0; --i) {
            if(nums[i] == target) {
                k--;
            } else {
                break;
            }
        }

        int[] buckets = new int[k];
        return dfs(nums, i, buckets, target);
    }

    boolean dfs(int[] nums, int index, int[] buckets, int target) {
        if(index < 0) {
            return true;
        }

        for(int i = 0; i < buckets.length; ++i) {
            if(buckets[i] + nums[index] <= target) {
                buckets[i] += nums[index];
                if(dfs(nums, index - 1, buckets, target)) {
                    return true;
                }
                buckets[i] -= nums[index];
            }
        }
        return false;
    }

    private int getSum(int[] nums) {
        int sum = 0;
        for(int num : nums) {
            sum += num;
        }
        return sum;
    }


    /******************** 我是华丽丽的分割线 **************************/


    public boolean canPartitionKSubsets_v2(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if (sum % k > 0) {
            return false;
        }
        boolean[] visit = new boolean[1 << nums.length];
        return search(0, sum, visit, nums, sum / k);
    }

    boolean search(int used, int todo, boolean[] visit, int[] nums, int target) {
        if (todo == 0) {
            return true;
        }

        if (!visit[used]) {
            visit[used] = true;
            int remain = (todo - 1) % target + 1;
            for (int i = 0; i < nums.length; i++) {
                if ((((used >> i) & 1) == 0) && nums[i] <= remain) {
                    if (search(used | (1<<i), todo - nums[i], visit, nums, target)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}






