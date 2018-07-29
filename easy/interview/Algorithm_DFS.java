package com.stevenli.interview.easy.interview;

import java.util.*;

public class Algorithm_DFS {

}

/**
 * 要求：
 * 1. 结果数据集需要去重
 * 2. 原有数据集中元素只能用1次
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
 * 要求：
 * 1. 结果数据集需要去重
 * 2. 原有数据集中元素只能用1次 => 和DFS递归索引相关
 * 3. 原有数据集中元素有重复出现 => 和循环体中重复数据处理相关
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

        //Arrays.sort(nums, begin, len);
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