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







