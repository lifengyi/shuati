package interview;

import java.util.*;

public class Interview_Facebook {

}


/**
 * Convert a non-negative integer to its english words representation.
 * Given input is guaranteed to be less than 231 - 1.
 */
class L273_Integer_to_English_Words {
    String[] s1 = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
            "Sixteen", "Seventeen", "Eighteen", "Nineteen"};

    String[] s2 = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

    int oneBillion  = 1000000000;
    int oneMillion  = 1000000;
    int oneThousand = 1000;
    int oneHundred  = 100;

    public String numberToWords(int num) {
        if(num == 0) {
            return "Zero";
        }

        StringBuilder sb = new StringBuilder();

        generateWords(sb, num/oneBillion, "Billion");
        generateWords(sb, num%oneBillion/oneMillion, "Million");
        generateWords(sb, num%oneMillion/oneThousand, "Thousand");
        createNumbers(sb, num%oneThousand);
        return sb.toString().trim();
    }

    void generateWords(StringBuilder sb, int num, String suffix) {
        if(createNumbers(sb, num)) {
            sb.append(" ").append(suffix).append(" ");
        }
    }

    boolean createNumbers(StringBuilder sb, int num) {
        if(num == 0) {
            return false;
        }

        boolean hasHundred = false;
        int temp = num/100;
        if(temp != 0) {
            sb.append(s1[temp]).append(" ").append("Hundred");
            hasHundred = true;
        }

        temp = num%100;
        if(temp != 0) {
            if(hasHundred) {
                sb.append(" ");
            }

            if(temp < 20) {
                sb.append(s1[temp]);
            } else {
                sb.append(s2[temp/10]);
                if(temp%10 != 0) {
                    sb.append(" ").append(s1[temp%10]);
                }
            }
        }

        return true;
    }
}


/**
 *  Remove the minimum number of invalid parentheses in order to make the input string valid.
 *  Return all possible results.
 *
 *  Note: The input string may contain letters other than the parentheses ( and ).
 */

/**
 *  It requires minimum change, but DFS traverse all the subsets of this string,
 *  which means if the code even find a longer substring that meets the requirement,
 *  it still try to traverse other shorter substring. We need to improve it.
 *
 *  DFS may lead to stack overflow if the string is very long
 *
 *  Use BFS to resolve it:
 *      1. scan the string and remove any '(' or ')'
 *      2. Put the new string into queue
 *      3. Validate strings in queue;
 *  It can resolve the potential stack overflow issue,
 *  but will not reduce the useless computation.
 *
 *  Only remove redundant '(' or ')'
 *  Use DFS implementation + pruning
 *
 */

class L301_Remove_Invalid_Parentheses_DFS {
    public List<String> removeInvalidParentheses(String s) {
        List<String> result = new ArrayList<>();
        if(s == null || s.length() == 0) {
            result.add("");
            return result;
        }

        char[] array = s.toCharArray();
        List<Character> cache = new ArrayList<>();
        int expected = 0;

        dfs(array, 0, cache, expected, result);
        if(result.size() == 0) {
            result.add("");
        }
        return result;
    }

    void dfs(char[] array, int start, List<Character> cache, int expected, List<String> result) {
        if(expected == 0) {
            flush(cache, result);
        }

        if(start == array.length) {
            return;
        }

        for(int i = start; i < array.length; ++i) {
            if(i != start && array[i] == array[i - 1]) {
                continue;
            }

            if(array[i] == '(') {
                cache.add(array[i]);
                expected++;
                dfs(array, i + 1, cache, expected, result);
                expected--;
                cache.remove(cache.size() - 1);
            } else if(array[i] == ')') {
                if(expected > 0) {
                    cache.add(array[i]);
                    expected--;
                    dfs(array, i + 1, cache, expected, result);
                    expected++;
                    cache.remove(cache.size() - 1);
                }
            } else {
                cache.add(array[i]);
                dfs(array, i + 1, cache, expected, result);
                cache.remove(cache.size() - 1);
            }
        }
    }

    void flush(List<Character> cache, List<String> result) {
        if(cache.size() == 0) {
            return;
        }

        if(result.size() != 0 && result.get(0).length() > cache.size()) {
            return;
        }

        if(result.size() != 0 && result.get(0).length() < cache.size()) {
            result.clear();
        }

        StringBuilder sb = new StringBuilder();
        for(char ch : cache) {
            sb.append(ch);
        }
        result.add(sb.toString());
    }
}


class L301_Remove_Invalid_Parentheses_BFS {
    public List<String> removeInvalidParentheses(String s) {
        List<String> result = new ArrayList<>();
        if(s == null || s.length() == 0) {
            result.add("");
            return result;
        }

        LinkedList<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(s);
        visited.add(s);

        int size = 0;
        while(!queue.isEmpty()) {
            size = queue.size();
            for(int i = 0; i < size; ++i) {
                String str = queue.poll();
                if(validate(str)) {
                    result.add(str);
                } else {
                    update(queue, visited, str);
                }
            }
            if(result.size() != 0) {
                break;
            }
        }

        if(result.size() == 0) {
            result.add("");
        }
        return result;
    }

    void update(LinkedList<String> queue, Set<String> visited, String s) {
        char[] array = s.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            if(array[i] == '(' || array[i] == ')') {
                String subString = s.substring(0, i) + s.substring(i + 1);
                if(subString.length() != 0 && !visited.contains(subString)) {
                    visited.add(subString);
                    queue.offer(subString);
                }
            }
        }
    }

    boolean validate(String s) {
        char[] array = s.toCharArray();
        int count = 0;
        for(int i = 0; i < array.length; ++i) {
            if(array[i] == '(') {
                count++;
            } else if(array[i] == ')') {
                count--;
                if(count < 0) {
                    break;
                }
            }
        }

        return count == 0;
    }
}


class L301_Remove_Invalid_Parentheses_DFSwithPruning {
    char[] s1 = {'(', ')'};
    char[] s2 = {')', '('};

    public List<String> removeInvalidParentheses(String s) {
        List<String> ret = new ArrayList<>();
        if(s == null || s.length() == 0) {
            ret.add("");
            return ret;
        }

        int scanCursor = 0, removeCursor = 0;
        dfs(s, removeCursor, s1, ret);
        if(ret.size() == 0) {
            ret.add("");
        }
        return ret;
    }

    void dfs(String s, int removeCur, char[] compare, List<String> result) {
        int count = 0;
        char[] array = s.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            if(array[i] == compare[0]) {
                count++;
            } else if(array[i] == compare[1]) {
                count--;
            }
            if(count >= 0) {
                continue;
            }

            //remove all the possible ')'/'(' and run dfs again;
            //removeCur在整个index之前的删除点已经被尝试过了，此次dfs如果要尝试
            //删除的话，请从该索引开往往后尝试
            for(int j = removeCur; j <= i; ++j) {
                //条件首先是匹配中第二个选项值，可能是")"也可能是"("
                //还有一个条件是避免重复项被选中，即使a[j]!=a[j-1]
                //由于条件中有j-1所以当第一个为命中项时，时可以尝试的
                if(array[j] == compare[1] && (j == removeCur || array[j] != array[j - 1])) {
                    String tmp = s.substring(0, j) + s.substring(j + 1);
                    dfs(tmp, j, compare, result);
                }
            }
            return;
        }

        String reversed = new StringBuilder(s).reverse().toString();
        if(compare[0] == '(') {
            dfs(reversed, 0, s2, result);
        } else {
            result.add(reversed);
        }
    }
}

class L301_Remove_Invalid_Parentheses_DFSwithPruning_BFS {
    public List<String> removeInvalidParentheses(String s) {
        List<String> ret = new ArrayList<>();
        if(s == null || s.length() == 0) {
            ret.add("");
            return ret;
        }

        LinkedList<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(s);
        visited.add(s);
        int size = 0;

        while(!queue.isEmpty()) {
            size = queue.size();
            for(int i = 0; i < size; ++i) {
                String tmp = queue.poll();
                if(validate(tmp)) {
                    ret.add(tmp);
                } else if (ret.size() == 0){
                    addQueue(queue, visited, tmp);
                }
            }
            if(ret.size() != 0) {
                break;
            }
        }

        if(ret.size() == 0) {
            ret.add("");
        }
        return ret;
    }

    boolean validate(String s) {
        char[] array = s.toCharArray();
        int count = 0;
        for(int i = 0; i < array.length; ++i) {
            if(array[i] == '(') {
                count++;
            } else if(array[i] == ')') {
                count--;
            }

            if(count < 0) {
                break;
            }
        }

        return count == 0;
    }


    /**
     *  可以尝试把上面validate和这个addQueue函数结合起来
     *  首先判断count的情况：
     *    等于0，该字符串放入result
     *    大于0，删除所有可能左括号
     *    小于0，删除所有可能右括号
     *
     *    用visited来进行去重
     */
    void addQueue(LinkedList<String> queue, Set<String> visited, String s) {
        char[] array = s.toCharArray();
        int count = 0;
        for(int i = 0; i < array.length; ++i) {
            if(array[i] == '(') {
                count++;
            } else if(array[i] == ')') {
                count--;
            }

            if(count < 0) {
                removeBracket(array, i + 1, ')', queue, visited);
                break;
            }
        }

        if(count > 0) {
            removeBracket(array, array.length, '(', queue, visited);
        }
    }

    void removeBracket(char[] array, int end, char bracket, LinkedList<String> queue, Set<String> visited) {
        for(int i = 0; i < end; ++i) {
            if(array[i] == bracket) {
                String tmp = new String(array, 0, i) + new String(array, i + 1, array.length - i - 1);
                if(!visited.contains(tmp)) {
                    visited.add(tmp);
                    queue.offer(tmp);
                }
            }
        }
    }
}


class L15_3Sum_fb {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return result;
        }

        int left = 0, right = 0;
        Arrays.sort(nums);
        for(int i = 0; i < nums.length; ++i) {
            if(i != 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            left = i + 1;
            right = nums.length - 1;
            while(left < right) {
                if(nums[i] + nums[left] + nums[right] == 0) {
                    List<Integer> solution = new ArrayList<>();
                    solution.add(nums[i]);
                    solution.add(nums[left]);
                    solution.add(nums[right]);
                    result.add(solution);
                    left++;
                    right--;
                    while(left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    while(left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if (nums[i] + nums[left] + nums[right] < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
}

class L157_Read_N_Characters_Given_Read4_I {
    public int read(char[] buf, int n) {
        if(n <= 0 ) {
            return 0;
        }

        int total = 0;
        int dataLen = 0, dataIndex = 0;
        char[] cache = new char[4];

        while(total < n) {
            dataLen = read4(cache);
            if(dataLen == 0) {
                break;
            }
            while(total < n && dataIndex < dataLen) {
                buf[total++] = cache[dataIndex++];
            }

            if(total == n) {
                break;
            }

            dataIndex = 0;
        }

        return total;
    }

    private int read4(char[] buf) {
        return 0;
    }
}

class L158_Read_N_Characters_Given_Read4_II {
    char[] remainBuf = new char[4];
    int remainSize = 0;
    int remainIndex = 0;

    public int read(char[] buf, int n) {
        if(n <= 0) {
            return 0;
        }

        int total = 0;
        while(total < n) {
            while(total < n && remainIndex < remainSize) {
                buf[total++] = remainBuf[remainIndex++];
            }

            if(total == n) {
                break;
            }

            remainSize = read4(remainBuf);
            remainIndex = 0;
            if(remainSize == 0) {
                break;
            }
        }

        return total;
    }

    private int read4(char[] buf) {
        return 0;
    }
}


class L621_Task_Scheduler {
    public int leastInterval(char[] tasks, int n) {
        if(tasks == null || tasks.length == 0) {
            return 0;
        }
        if(n == 0) {
            return tasks.length;
        }

        int maxFreq = 0, maxCnt = 0, index = 0;
        char[] array = new char[26];
        for(int i = 0; i < tasks.length; ++i) {
            index = tasks[i] - 'A';
            array[index] += 1;
            if(array[index] == maxFreq) {
                maxCnt++;
            } else if(array[index] > maxFreq) {
                maxCnt = 1;
                maxFreq = array[index];
            }
        }

        if(maxCnt >= n + 1) {
            return tasks.length;
        }

        int emptySlots = (maxFreq - 1) * (n + 1 - maxCnt);
        emptySlots = Math.max(0, emptySlots - (tasks.length - maxFreq * maxCnt));
        return tasks.length + emptySlots;
    }
}


class L31_Next_Permutation_ {
    public void nextPermutation(int[] nums) {
        if(nums == null || nums.length < 2) {
            return;
        }

        int index = nums.length - 1;
        while(index > 0 && nums[index] <= nums[index - 1]) {
            index--;
        }

        if(index == 0) {
            reverse(nums, 0, nums.length - 1);
            //Arrays.Basic.sort(nums);
            return;
        }

        int pos = index - 1;
        index = nums.length - 1;
        while(index > pos && nums[index] <= nums[pos]) {
            index--;
        }
        swap(nums, pos, index);
        reverse(nums, pos + 1, nums.length - 1);
        //Arrays.Basic.sort(nums, pos + 1, nums.length);
        return;
    }

    void swap(int[] nums, int s1, int s2) {
        nums[s1] = nums[s1] ^ nums[s2];
        nums[s2] = nums[s1] ^ nums[s2];
        nums[s1] = nums[s1] ^ nums[s2];
    }

    void reverse(int[] nums, int s1, int s2) {
        while(s1 < s2) {
            swap(nums, s1++, s2--);
        }
    }
}

/**
 * 不仅可以求得总共几种字符串方案，还能求得具体方案
 */
class L91_Decode_Ways_DFS {
    public int numDecodings(String s) {
        if(s == null || s.length() == 0) {
            return 0;
        }

        char[] array = s.toCharArray();
        if(array[0] == '0') {
            return 0;
        }

        int[] count = new int[1];
        dfs(array, 0, count);
        return count[0];
    }

    void dfs(char[] array, int start, int[] count) {
        if(start == array.length) {
            count[0] += 1;
            return;
        } else if(start == array.length - 1) {
            if(array[start] != '0') {
                count[0] += 1;
            }
            return;
        }


        if(validateOneDigital(array, start)) {
            dfs(array, start + 1, count);
        }

        if(validateTwoDigitals(array, start)) {
            dfs(array, start + 2, count);
        }
    }

    boolean validateOneDigital(char[] array, int n) {
        return array[n] == '0' ? false : true;
    }

    boolean validateTwoDigitals(char[] array, int n) {
        if(array[n] == '1') {
            return true;
        }

        if(array[n] == '2' && array[n + 1] - '0' <= 6) {
            return true;
        }

        return false;
    }
}





class L67_Add_Binary {
    public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1;
        int carryover = 0, sum = 0;

        while(i >= 0 || j >= 0) {
            sum = carryover;
            if(i >= 0) {
                sum += a.charAt(i) - '0';
            }
            if(j >= 0) {
                sum += b.charAt(j) - '0';
            }
            sb.append(sum%2);
            carryover = sum/2;
            i--;
            j--;
        }

        if(carryover != 0) {
            sb.append(carryover);
        }
        return sb.reverse().toString();
    }
}



/**
 * 1, If p.charAt(j) == s.charAt(i) :  dp[i][j] = dp[i-1][j-1];
 * 2, If p.charAt(j) == '.' : dp[i][j] = dp[i-1][j-1];
 * 3, If p.charAt(j) == '*':  here are two sub conditions:
 *      1   if p.charAt(j-1) != s.charAt(i) : dp[i][j] = dp[i][j-2]  //in this case, a* only counts as empty
 *      2   if p.charAt(i-1) == s.charAt(i) or p.charAt(i-1) == '.':
 *              dp[i][j] = dp[i-1][j]    //in this case, a* counts as multiple a
 *              or dp[i][j] = dp[i][j-1]   // in this case, a* counts as single a
 *              or dp[i][j] = dp[i][j-2]   // in this case, a* counts as empty
 */
class L10_Regular_Expression_Matching {
    public boolean isMatch(String s, String p) {
        char[] array = s.toCharArray();
        char[] pattern = p.toCharArray();
        if(pattern.length != 0 && pattern[0] == '*') {
            return false;
        }

        int[][] dp = new int[array.length + 1][pattern.length + 1];
        init(dp, pattern);

        for(int i = 1; i < dp.length; ++i) {
            for(int j = 1; j < dp[0].length; ++j) {
                if(array[i - 1] == pattern[j - 1] || pattern[j - 1] == '.') {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                if(pattern[j - 1] == '*') {
                    if(array[i - 1] != pattern[j - 2] && pattern[j - 2] != '.' && pattern[j - 2] != '*') {
                        dp[i][j] = dp[i][j - 2];
                    } else {
                        // []* => empty | single | multiple
                        dp[i][j] = dp[i][j - 2] | dp[i][j - 1] | dp[i - 1][j];
                    }
                }
            }
        }
        return dp[array.length][pattern.length] == 1 ? true : false;
    }

    void init(int[][] dp, char[] pattern) {
        dp[0][0] = 1;
        for(int i = 1; i < dp[0].length; ++i) {
            if(pattern[i - 1] == '*' && dp[0][i - 2] == 1) {
                dp[0][i] = 1;
            }
        }
    }
}

class L10_Regular_Expression_Matching_OptimizedByRollingArray {
    public boolean isMatch(String s, String p) {
        char[] array = s.toCharArray();
        char[] pattern = p.toCharArray();
        if(pattern.length != 0 && pattern[0] == '*') {
            return false;
        }

        int[][] dp = new int[2][pattern.length + 1];
        for(int i = 0; i <= array.length; ++i) {
            if(i == 0) {
                dp[0][0] = 1;
            } else {
                dp[i%2][0] = 0;
            }
            for(int j = 1; j <= pattern.length; ++j) {
                if(i == 0) {
                    if(pattern[j - 1] == '*') {
                        dp[i%2][j] = dp[i%2][j-2];
                    }
                    continue;
                }

                if(array[i - 1] == pattern[j - 1] || pattern[j - 1] == '.') {
                    dp[i%2][j] = dp[(i - 1)%2][j - 1];
                } else if(pattern[j - 1] == '*') {
                    if(array[i - 1] != pattern[j - 2] && pattern[j - 2] != '.' && pattern[j - 2] != '*') {
                        dp[i%2][j] = dp[i%2][j - 2];
                    } else {
                        // []* => empty | single | multiple
                        dp[i%2][j] = dp[i%2][j - 2] | dp[i%2][j - 1] | dp[(i - 1)%2][j];
                    }
                } else {
                    dp[i%2][j] = 0;
                }
            }
        }
        return dp[array.length%2][pattern.length] == 1 ? true : false;
    }
}

class L44_Wildcard_Matching {
    public boolean isMatch(String s, String p) {
        if(s == null || p == null) {
            return false;
        }

        char[] string = s.toCharArray();
        char[] pattern = p.toCharArray();
        int[][] dp = new int[string.length + 1][pattern.length + 1];
        init(dp, pattern);

        for(int i = 1; i <= string.length; ++i) {
            for(int j = 1; j <= pattern.length; ++j) {
                if(string[i - 1] == pattern[j - 1] || pattern[j - 1] == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if(pattern[j - 1] == '*') {
                    dp[i][j] = dp[i - 1][j - 1] | dp[i - 1][j] | dp[i][j - 1];
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        return dp[string.length][pattern.length] == 1 ? true : false;
    }

    void init(int[][] dp, char[] pattern) {
        dp[0][0] = 1;
        for(int j = 1; j <= pattern.length; ++j) {
            if(pattern[j - 1] == '*') {
                dp[0][j] = dp[0][j - 1];
            }
        }
    }
}

/**
 *  滚动数组优化：
 *  1. 保证第一行第一列是动态更新（注意代码写法）
 *  2. 动态规划空间中无论值是什么，都需要强行写入以覆盖之前的值
 *  3. 注意动态规划空间的索引ij对应到原有数据集中都需要-1操作
 */
class L44_Wildcard_Matching_OptimizedByRollingArray {
    public boolean isMatch(String s, String p) {
        if(s == null || p == null) {
            return false;
        }

        char[] string = s.toCharArray();
        char[] pattern = p.toCharArray();
        int[][] dp = new int[2][pattern.length + 1];

        for(int i = 0; i <= string.length; ++i) {
            //init first column
            if(i == 0) {
                dp[i%2][0] = 1;
            } else {
                dp[i%2][0] = 0;
            }

            for(int j = 1; j <= pattern.length; ++j) {
                //init first line
                if(i == 0) {
                    if(pattern[j - 1] == '*') {
                        dp[0][j] = dp[0][j - 1];
                    }
                    continue;
                }

                if(string[i - 1] == pattern[j - 1] || pattern[j - 1] == '?') {
                    dp[i%2][j] = dp[(i - 1)%2][j - 1];
                } else if(pattern[j - 1] == '*') {
                    dp[i%2][j] = dp[(i - 1)%2][j - 1] | dp[(i - 1)%2][j] | dp[i%2][j - 1];
                } else {
                    dp[i%2][j] = 0;
                }
            }
        }

        return dp[string.length%2][pattern.length] == 1 ? true : false;
    }
}


/**
 *  边缘case忘记了一个点的start和一个点的end重合时候的情形
 *  需要定义当相等出现时，优先级如何定义
 *
 *  扫描线问题： 可以遍历点的时候，使用容器存储点，在扫描过程中
 *             做出相应的计算，容器可以是set，map，heap等
 *             容器的选择取决于求解的结果是什么
 */
class L253_Meeting_Rooms_II {
    public class Interval {
        int start;
        int end;
        Interval() { start = 0; end = 0; }
        Interval(int s, int e) { start = s; end = e; }
    }

    class Node implements Comparable<Node> {
        int id;
        int val;
        int type;  //0:start, 1:end

        public Node(int id, int val, int type) {
            this.id = id;
            this.val = val;
            this.type = type;
        }

        public int compareTo(Node other) {
            if(this.val == other.val) {
                return other.type - this.type;
            }
            return this.val - other.val;
        }
    }

    public int minMeetingRooms(Interval[] intervals) {
        if(intervals == null || intervals.length == 0) {
            return 0;
        }

        int result = Integer.MIN_VALUE;
        Node[] nodes = init(intervals);
        Arrays.sort(nodes);

        Set<Integer> ids = new HashSet<>();
        for(int i = 0; i < nodes.length; ++i) {
            if(nodes[i].type == 0) {
                ids.add(nodes[i].id);
            } else {
                ids.remove(nodes[i].id);
            }
            result = Math.max(result, ids.size());
        }

        return result;
    }

    Node[] init(Interval[] intervals) {
        Node[] nodes = new Node[intervals.length * 2];
        int index = 0;
        for(int i = 0; i < intervals.length; ++i) {
            nodes[index++] = new Node(i, intervals[i].start, 0);
            nodes[index++] = new Node(i, intervals[i].end, 1);
        }
        return nodes;
    }
}





/**
 *  438. Find All Anagrams in a String
 *      固定双指针问题 + Anagram 字符串匹配包含问题（
 *                      生成目标字符串的map，
 *                      动态维护当前输入字符后的map
 *                      动态维护当前有效输入的totoalCount)
 *
 *  567 Permutation in String （Permutation就是anagram）
 *      固定双指针 + Anagram 字符串匹配包含问题
 *
 *  76. Minimum Window Substring
 *      非固定双指针 + 字符串匹配包含问题
 */
class L438_Find_All_anagrams_In_a_String {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if(s == null || s.length() < p.length()) {
            return result;
        }

        int[] pattern = new int[26];
        for(int i = 0; i < p.length(); ++i) {
            pattern[p.charAt(i) - 'a'] += 1;
        }

        int[] map = new int[26];
        int size = p.length();
        int index = 0, count = size;
        for(int i = 0; i < s.length(); ++i) {
            index = s.charAt(i) - 'a';
            map[index] += 1;
            if(pattern[index] > 0 && pattern[index] >= map[index]) {
                count--;
            }

            if(i >= size - 1) {
                if(count == 0) {
                    result.add(i - size + 1);
                }

                index = s.charAt(i - size + 1) - 'a';
                map[index] -= 1;
                if(pattern[index] > map[index]) {
                    count++;
                }
            }
        }

        return result;
    }
}


/**
 * 1. 用DFS or DFS + Memorizaion 都是挂
 * 2. 使用BFS，lint过，leet挂，
 * 3. BFS + Memorizaion，leet还是挂
 *
 * 进一步优化：首先使用优先队列，这样我们每次取出的都是当前匹配情况下最远端的
 *           即最靠近字符串尾部的地方进行下一次的匹配，这样能优化时间复杂度
 *
 *
 * 再进一步优化：
 *        此处并没有实现，在匹配字符串的过程中，我们使用了 startsWith，
 *        还可以考虑如下：
 *        a. 将list转为Set，摒弃重复字符串，以及异常字符串，例如大于目标字符串的串
 *        b. 同时记下当前set中最大的数据长度max
 *        c. 比较的时候独处index，从当前index开始遍历到最远端max，当然必须在目标
 *           字符串的长度范围内，对每个判断是否能匹配成功，判断的依据就是当前index
 *           到max之间的每一个子串是否包含在set里，同时加上记忆化判断
 *
 *        区别在于：当前实现的版本遍历字典，并和当前索引做匹配；进一步优化就是遍历当前
 *                索引下可能匹配的所有字符串，并检查哈希表内是否存在；
 *
 *                substring + set.contains vs startsWith
 *
 * DP解法：
 *    参考Word Break III的2种DP
 *
 */
class L139_Word_Break {
    public boolean wordBreak(String s, List<String> wordDict) {
        int[] visited = new int[s.length()];
        PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>(){
            @Override
            public int compare(Integer i1, Integer i2) {
                return i2.compareTo(i1);
            }
        });
        int index = 0, end = 0;
        queue.add(index);
        while(!queue.isEmpty()) {
            index = queue.poll();
            for(String word : wordDict) {
                end = index + word.length();
                if(end > s.length() || (end < s.length() && visited[end] == 1)) {
                    continue;
                }
                if(s.startsWith(word, index)) {
                    if(end == s.length()) {
                        return true;
                    }
                    visited[index] = 1;
                    queue.add(end);
                }
            }
        }

        return false;
    }
}

/**
 * 普通DFS在极限case下肯定会挂，考虑优化pruning，
 *
 * search + memorization
 *
 */
class L140_Word_Break_II {
    public List<String> wordBreak_II_optimized(String s, List<String> wordDict) {
        if (s == null || s.length() == 0 || wordDict == null || wordDict.size() == 0) {
            return new ArrayList<>();
        }
        Map<String, List<String>> map = new HashMap<>();
        return backtracking(map, s, wordDict);
    }

    private List<String> backtracking(Map<String, List<String>> map, String s, List<String> wordDict) {
        if (map.containsKey(s)) {
            return map.get(s);
        }

        List<String> res = new ArrayList<>();
        if (s.length() == 0) {
            res.add("");
            return res;
        }

        for (String word : wordDict) {
            if (s.startsWith(word)) {
                List<String> list = backtracking(map, s.substring(word.length()), wordDict);
                for (String temp : list) {
                    if (temp.length() == 0) {
                        res.add(word);
                    } else {
                        res.add(word + " " + temp);
                    }
                }
            }
        }
        map.put(s, res);
        return res;
    }
}


/**
 *  字符串长度为n，字典长度为k
 *  DP : O(nk)
 *  DP2 : O(n*n)
 *  方案的选取取决于数据特点：字符串过长 vs 字典过长
 *
 *  遍历字符串中所有索引的过程中：
 *  如果发现该索引不可达，则跳过不处理
 *  如果可达，则采取上述两种方案的一种进行比较；
 *
 *  word break I
 *      dp[i]代表是否可达，dp[0] = 1
 *
 *  word break III
 *      dp[i]代表到达该索引的可行解方案数量，dp[0] = 1;
 *
 *  variant： 求解可行解中，长度最大的可行解的长度
 *      dp[i]代表到达该索引的所有可行解中，长度最大的可行解的长度
 *
 *
 */
class LintCode_L683_Word_Break_III_nk {
    public int wordBreak3(String s, Set<String> dict) {
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        s = s.toUpperCase();
        Set<String> set = new HashSet<>();
        for(String word : dict) {
            set.add(word.toUpperCase());
        }

        for(int i = 0; i < dp.length; ++i) {
            if(dp[i] == 0) {
                continue;
            }

            for(String word : set) {
                if(word.length() == 0 || i + word.length() >= dp.length) {
                    continue;
                }

                if(s.startsWith(word, i)) {
                    dp[i + word.length()] += dp[i];
                }
            }
        }


        return dp[s.length()];
    }
}

class LintCode_L683_Word_Break_III_nn {
    public int wordBreak3(String s, Set<String> dict) {
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;

        s = s.toUpperCase();
        Set<String> newSet = new HashSet<>();
        for(String word : dict) {
            newSet.add(word.toUpperCase());
        }

        for(int i = 0; i < dp.length; ++i) {
            if(dp[i] == 0) {
                continue;
            }

            for(int j = i + 1; j <= s.length(); ++j) {
                String tmp = s.substring(i, j);
                if(newSet.contains(tmp)) {
                    dp[j] += dp[i];
                }
            }
        }
        return dp[s.length()];
    }
}

/**
 * 求可行解中长度最大的可行解的长度
 */
class Word_Break_variant {
    public int wordBreak_variant(String s, Set<String> dict) {
        int[] dp = new int[s.length() + 1];
        dp[0] = 0;

        s = s.toUpperCase();
        Set<String> newSet = new HashSet<>();
        for(String word : dict) {
            newSet.add(word.toUpperCase());
        }

        for(int i = 0; i < dp.length; ++i) {
            if(i != 0 && dp[i] == 0) {
                continue;
            }

            for(int j = i + 1; j <= s.length(); ++j) {
                String tmp = s.substring(i, j);
                if(newSet.contains(tmp)) {
                    dp[j] = Math.max(dp[j], dp[i] + 1);
                }
            }
        }
        return dp[s.length()];
    }
}


/**
 * 121. Best Time to Buy and Sell Stock
 *      只能一次买一次卖
 *      思路：遍历一次，每次保存当前最小价格，和目前价格比较：
 *      1. 如果有获利，则和全局获利比较并保存
 *      2. 如果获利相同，则获利为0
 *      3. 如果没有获利，则获利为0，且当前价保存为最小价
 *
 * 122. Best Time to Buy and Sell Stock II
 *      能多次买卖，但是必须买卖相对应
 *      思路：累加所有上涨的区间
 *
 *
 * 123. Best Time to Buy and Sell Stock III
 * 188. Best Time to Buy and Sell Stock IV
 *      最多k（k=2）笔交易
 *      思路：维护两个dp空间
 *           dp1[i][j],第i天第j次交易，所能达到的最大盈利：
 *           dp1[i][j] = Max
 *               当天不交易：dp1[i-1][j]
 *               当天交易： dp2[i][j]
 *
 *           dp2[i][j],第i天第j次交易，且当天必须交易所能达到的最大盈利
 *           dp2[i][j] = Max
 *                今天的股价不计入盈利，今天买今天卖，做第j次交易：     dp1[i-1][j-1]
 *                今天的股价计入盈利，过去买的都在今天卖，做第j次交易：  dp2[i-1][j] + diff(A[i-1], A[i])
 *
 *      思考：本题在选取当前节点后，所采用的求解方法，区别之前遇到的方法：
 *               1. O(1)情况下获得解
 *               2. O(n)情况下获得解，即在一个范围内枚举所有可能结果
 *                  例如，扔鸡蛋的DP不优化求解方案
 *      区别：
 *           本题和1类似，区别在于1中的当选择当前情况之后，可以通过前一个dp状态值以及当前取值来直接获得
 *           本题中，无法直接获得，还需要进行二次dp才能获得该值
 *
 *           本题和2的区别在于，2是知道在某一区间范围内，一系列值中的最优解，就是当前最优解，
 *           本题中，可以想象到也是在一定范围的一些列值的最优解，例如第二次交易中所有价格情况下求最优解，
 *           但是很难得知这个所谓的一定范围到底取自什么范围；
 *
 *      样题比较：
 *           比如说 689. Maximum Sum of K(K=3) Non-Overlapping Subarrays
 *           和这两道题的区别就才于此，689可以在O(1)时间内得到当前节点被选中（参与）时的结果
 *           但是这两道题目当前节点被选中（参与）时的结果不是那么容易得到；
 *
 *      类似题：
 *           Lintcode 42. Maximum Subarray II
 *           Lintcode 43. Maximum Subarray III
 *           注意小小区别：股票可以0交易，即保证不亏钱，
 *           但是42/43有可能为负值出现，如何处理？
 *
 *
 * 714. Best Time to Buy and Sell Stock with Transaction Fee
 *
 *
 */
class L121_Best_Time_to_Buy_and_Sell_Stock {
    public int maxProfit(int[] prices) {
        if(prices == null || prices.length == 0) {
            return 0;
        }

        int max = prices[0], min = prices[0];
        int maxProfit = 0;
        for(int i = 1; i < prices.length; ++i) {
            if(prices[i] > max) {
                max = prices[i];
                maxProfit = Math.max(maxProfit, max - min);
            } else if(prices[i] < min) {
                max = prices[i];
                min = prices[i];
            }
        }
        return maxProfit;
    }

    public int maxProfit_dp(int[] prices) {
        if(prices == null || prices.length == 0){
            return 0;
        }

        int[] dp = new int[prices.length];
        dp[0] = 0;
        int max = prices[0], min = prices[0];
        for(int i = 1; i < prices.length; ++i) {
            if(prices[i] > max) {
                max = prices[i];
                dp[i] = max - min;
            } else if (prices[i] <= max && prices[i] >= min) {
                dp[i] = dp[i - 1];
            } else {
                dp[i] = 0;
                max = prices[i];
                min = prices[i];
            }
        }

        int maxProfit = 0;
        for(int value : dp) {
            maxProfit = Math.max(maxProfit, value);
        }
        return maxProfit;
    }
}


class L122_Best_Time_to_Buy_and_Sell_Stock_II {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        for(int i = 0; i < prices.length - 1; i++) {
            if(prices[i+1] > prices[i])
                maxProfit += prices[i+1] - prices[i];
        }

        return maxProfit;
    }
}


class L123_Best_Time_to_Buy_and_Sell_Stock_III {

    public int maxProfit(int[] prices) {
        int n = 3;
        int[] dp1 = new int[n];
        int[] dp2 = new int[n];

        for(int i = 1; i < prices.length; ++i) {
            for(int j = n - 1; j > 0; --j) {
                dp2[j] = Math.max(dp2[j] + prices[i] - prices[i - 1], dp1[j - 1]);
                dp1[j] = Math.max(dp1[j], dp2[j]);
            }
        }
        return dp1[n - 1];
    }
}

class L188_Best_Time_to_Buy_and_Sell_Stock_IV {
    public int maxProfit(int k, int[] prices) {
        if(k > prices.length) {
            return getMaxProfit(prices);
        }

        int[] dp1 = new int[k + 1];
        int[] dp2 = new int[k + 1];
        for(int i = 1; i < prices.length; ++i) {
            for(int j = k; j > 0; --j) {
                dp2[j] = Math.max(dp2[j] + prices[i] - prices[i - 1], dp1[j - 1]);
                dp1[j] = Math.max(dp1[j], dp2[j]);
            }
        }
        return dp1[k];
    }

    int getMaxProfit(int[] prices) {
        int profit = 0;
        for(int i = 1; i < prices.length; ++i) {
            profit += Math.max(0, prices[i] - prices[i - 1]);
        }
        return profit;
    }
}


/**
 * 维护两个DP空间，买卖股票下"当前最大盈利"
 * （持有情况已经包含其中，如果上一次买，买dp当前为持有；
 * 如果上一次卖，卖dp当前为持有）
 *
 * sold[i]有两个状态:
 *      第i-1的时候卖了股票： sell[i - 1]  没有股票可卖
 *      第i-1的时候买了股票： prices[i] + buy[i - 1] - fee
 *                        上次买股票后的总体盈利加上现在的价格即当前卖出后的盈利
 *
 *
 * 初始化为扣除当前买入价，即使先扣除买股票的成本，代表当前buy股票后的总体盈利
 * buy[i]也有两个状态:
 *      第i-1的时候卖了股票： sell[i-1] - prices[i] 必须买入股票持有
 *      第i-1的时候买了股票： buy[i - 1]            继续持有
 *
 */
class L714_Best_Time_to_Buy_and_Sell_Stock_with_Transaction_Fee {
    public int maxProfit(int[] prices, int fee) {
        int n = prices.length;
        int[] sell = new int[n];
        int[] buy = new int[n];
        buy[0] = -prices[0];
        for(int i = 1; i < prices.length; ++i) {
            sell[i] = Math.max(sell[i - 1], prices[i] + buy[i - 1] - fee);
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
        }
        return sell[n - 1];
    }
}

/**
 *  维护两个dp空间
 */
class L309_Best_Time_to_Buy_and_Sell_Stock_with_Cooldown {
    public int maxProfit(int[] prices) {
        if(prices == null || prices.length < 2) {
            return 0;
        }

        int n = prices.length;
        int[] sell = new int[n];
        int[] buy = new int[n];
        buy[0] = -prices[0];
        sell[1] = Math.max(0, prices[1] - prices[0]);
        buy[1] = Math.max(-prices[0], -prices[1]);
        for(int i = 2; i < prices.length; ++i) {
            sell[i] = Math.max(sell[i - 1], prices[i] + buy[i - 1]);
            buy[i] = Math.max(buy[i - 1], sell[i - 2] - prices[i]);
        }
        return sell[n - 1];
    }
}



class L689_Maximum_Sum_of_3_Non_Overlapping_Subarrays {
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int[] preSum = getPreSum(nums, k);
        int n = 3;
        int[][] dp = new int[n + 1][nums.length + 1];

        for(int i = 1; i <= n; ++i) {
            int startIndex = i * k;
            for(int j = startIndex; j <= nums.length; ++j) {    //  Error 1: "<= length", not "< length"
                dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j - k] + preSum[j]);
            }
        }

        return trace(preSum, dp, k);
    }

    int[] trace(int[] preSum, int[][] dp, int k) {
        int[] ret = new int[dp.length - 1];
        int x = dp.length - 1;
        int y = dp[0].length - 1;
        while(x > 0) {
            if(dp[x][y - 1] == dp[x][y]) {
                y = y - 1;
            } else {
                ret[x - 1] = y - k;     // Error 2: save the start index, not the end indx
                x = x - 1;
                y = y - k;
            }
        }
        return ret;
    }

    int[] getPreSum(int[] nums, int k) {
        int[] preSum = new int[nums.length + 1];
        int sum = 0;
        for(int i = 0; i < nums.length; ++i) {
            sum += nums[i];
            if(i >= k - 1) {
                preSum[i + 1] = sum;
                sum -= nums[i - k + 1];
            }
        }
        return preSum;
    }
}


/**
 *
 *   代码思想类同： L188_Best_Time_to_Buy_and_Sell_Stock_IV
 *   思想： 需要维护2个dp空间
 *
 *   代码写法类同： L689_Maximum_Sum_of_3_Non_Overlapping_Subarrays
 *   即：保证pick的序列数和当前pick的起始数字的序列是匹配的；
 *
 */
class Lintcode_42_Maximum_Subarray_II {

    public int maxTwoSubArrays(List<Integer> nums) {
        int[] preSum = getPreSum(nums);
        int k = 2;
        int n = nums.size();
        int[][] dp1 = new int[2][n + 1];
        int[][] dp2 = new int[2][n + 1];
        for(int i = 1; i <= k; ++i) {
            dp1[i%2][i] = preSum[i];
            dp2[i%2][i] = preSum[i];
            for(int j = i + 1; j <= n; ++j) {
                dp2[i%2][j] = Math.max(dp2[i%2][j - 1], dp1[(i - 1)%2][j - 1]) + nums.get(j - 1);
                dp1[i%2][j] = Math.max(dp2[i%2][j], dp1[i%2][j - 1]);
            }
        }
        return dp1[k%2][n];
    }

    int[] getPreSum(List<Integer> nums) {
        int[] preSum = new int[nums.size() + 1];
        for(int i = 1; i <= nums.size(); ++i) {
            preSum[i] = preSum[i - 1] + nums.get(i - 1);
        }

        return preSum;
    }
}

/**
 *  代码思想以及写法类同： L188_Best_Time_to_Buy_and_Sell_Stock_IV
 *
 *  比照上面的写法，可以发现是考虑横纵坐标的选取
 *
 *  上面写法：本能选取ith pick， jth number，所以只能使用二维数组/滚动数组优化实现
 *  这个写法：DP中出现的选于不选的问题，导致当前数字存在取和不取的情况，可以做为横轴
 *          ith number， jth pick
 *
 *  dp1[i][j] 前i个数字组成j个数组，最大和
 *
 *  dp2[i][j] 前i个数字组成j个数组，当前第i个数字必须被使用
 *
 *
 *  dp1[i][j] =  不取i:  dp1[i-1][j]
 *               取i :   dp2[i][j]
 *
 *  dp2[i][j] = 取i， i独立为一个数组：  dp1[i-1][j-1] + A[i]
 *                   i和包含在最后一个数组中: dp2[i-1][j] + A[i]
 */
class Lintcode_42_Maximum_Subarray_II_v2 {
    public int maxTwoSubArrays(List<Integer> nums) {
        int[] preSum = getPreSum(nums);

        int n = nums.size();
        int k = 2;
        int[] dp1 = new int[k + 1];
        int[] dp2 = new int[k + 1];

        for(int i = 1; i <= n; ++i) {
            for(int j = k; j > 0; --j) {
                if(j > i) {
                    continue;
                } else if(j == i) {
                    dp1[j] = preSum[i];
                    dp2[j] = preSum[i];
                } else {
                    dp2[j] = Math.max(dp2[j], dp1[j - 1]) + nums.get(i - 1);
                    dp1[j] = Math.max(dp2[j], dp1[j]);
                }
            }
        }

        return dp1[k];
    }

    int[] getPreSum(List<Integer> nums) {
        int[] preSum = new int[nums.size() + 1];
        for(int i = 1; i < preSum.length; ++i) {
            preSum[i] = preSum[i - 1] + nums.get(i - 1);
        }
        return preSum;
    }
}


class Lintcode_43_Maximum_Subarray_III {
    public int maxSubArray(int[] nums, int k) {
        int[] preSum = getPreSum(nums);

        int n = nums.length;
        int[] dp1 = new int[k + 1];
        int[] dp2 = new int[k + 1];

        for(int i = 1; i <= n; ++i) {
            for(int j = k; j > 0; --j) {
                if(j > i) {
                    continue;
                } else if(j == i) {
                    dp1[j] = preSum[i];
                    dp2[j] = preSum[i];
                } else {
                    dp2[j] = Math.max(dp2[j], dp1[j - 1]) + nums[i - 1];
                    dp1[j] = Math.max(dp2[j], dp1[j]);
                }
            }
        }

        return dp1[k];
    }

    int[] getPreSum(int[] nums) {
        int[] preSum = new int[nums.length + 1];
        for(int i = 1; i < preSum.length; ++i) {
            preSum[i] = preSum[i - 1] + nums[i - 1];
        }
        return preSum;
    }
}


class L290_Word_Pattern {
    public boolean wordPattern(String pattern, String str) {
        if(pattern == null || str == null) {
            return false;
        }

        if(pattern.length() == 0 && str.length() == 0) {
            return true;
        } else if(pattern.length() == 0 || str.length() == 0) {
            return false;
        }

        String[] strings = str.split(" ");
        if(pattern.length() != strings.length) {
            return false;
        }

        int[] m1 = new int[256];
        Map<String, Integer> m2 = new HashMap<>();

        for(int i = 0; i < pattern.length(); ++i) {
            char ch = pattern.charAt(i);
            String key = strings[i];
            if(m1[ch] == 0 && !m2.containsKey(key)) {
                m1[ch] = i + 1;
                m2.put(key, i + 1);
            } else if (m1[ch] == 0 || !m2.containsKey(key) || m1[ch] != m2.get(key)) {
                return false;
            }
        }

        return true;
    }
}


class L291_Word_Pattern_II {
    public boolean wordPatternMatch(String pattern, String str) {
        Map<Character, String> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        return dfs(pattern, 0, str, 0, map, set);
    }

    private boolean dfs(String pattern, int patternIndex, String str, int start,
                        Map<Character, String> map, Set<String> set) {
        if(patternIndex == pattern.length() && start == str.length()) {
            return true;
        } else if(patternIndex == pattern.length() || start == str.length()) {
            return false;
        }

        char ch = pattern.charAt(patternIndex);
        if(map.containsKey(ch)) {
            String prefix = map.get(ch);
            if(comparePrefix(str, prefix, start)) {
                return dfs(pattern, patternIndex + 1, str, start + prefix.length(), map, set);
            } else {
                return false;
            }
        }

        for(int i = start; i < str.length(); ++i) {
            String prefix = str.substring(start, i + 1);
            if(set.contains(prefix)) {
                continue;
            }

            set.add(prefix);
            map.put(ch, prefix);
            boolean ret = dfs(pattern, patternIndex + 1, str, start + prefix.length(), map, set);
            if(ret == true) {
                return true;
            }
            set.remove(prefix);
        }

        map.remove(ch);
        return false;
    }

    private boolean comparePrefix(String str, String prefix, int start) {
        int i = start, j = 0;
        for(; i < str.length() && j < prefix.length(); ++i, ++j) {
            if(str.charAt(i) != prefix.charAt(j)) {
                return false;
            }
        }
        if(j != prefix.length()) {
            return false;
        }
        return true;
    }
}

class L791_Custom_Sort_String {
    public String customSortString(String S, String T) {
        int[] map = new int[26];
        for(int i = 0; i < T.length(); ++i) {
            char ch = T.charAt(i);
            map[ch - 'a'] += 1;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < S.length(); ++i) {
            char ch = S.charAt(i);
            int num = map[ch - 'a'];
            for(int j = 0; j < num; ++j) {
                sb.append(ch);
            }
            map[ch - 'a'] = 0;
        }

        for(int i = 0; i < map.length; ++i) {
            for(int j = 0; j < map[i]; ++j) {
                sb.append((char)(i + 'a'));
            }
        }
        return sb.toString();
    }
}


class L958_Check_Completeness_of_a_Binary_Tree {
    class ANode {
        TreeNode node = null;
        int index = 0;
        public ANode (TreeNode node, int index){
            this.node = node;
            this.index = index;
        }
    }

    public boolean isCompleteTree(TreeNode root) {
        List<ANode> list = new ArrayList<>();
        list.add(new ANode(root, 0));
        int index = 0;
        while(index < list.size()) {
            ANode cur = list.get(index++);
            TreeNode node = cur.node;
            int idx = cur.index;
            if(node.left != null) {
                list.add(new ANode(node.left, 2 * idx + 1));
            }
            if(node.right != null) {
                list.add(new ANode(node.right, 2 * idx + 2));
            }
        }

        return list.size() == list.get(list.size() - 1).index + 1;
    }
}

/**
 * 对树进行层级遍历：
 *
 * 还没存在节点丢失情况：
 *   1. 遇到节点丢失：
 *      a. 如果是左缺右有，则直接返回失败;
 *      b. 否则，就标记一下，表示此刻发现第一次缺失节点，缺右边，或同时缺左右两边，
 *      c. 将可能存在的节点入队列
 *   2. 将节点入队列；
 *
 * 已经存在节点丢失情况：所有队列里的节点都只能是叶子节点，否则就出错；
 */
class L958_Check_Completeness_of_a_Binary_Tree_v2 {
    public boolean isCompleteTree(TreeNode root) {
        if(root == null) {
            return true;
        }

        boolean isEnding = false;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if(isEnding == true && (node.left != null || node.right != null)) {
                return false;
            }

            if(isEnding == false) {
                if(node.left == null && node.right != null) {
                    return false;
                }
                if(node.left == null || node.right == null) {
                    isEnding = true;
                }
                if(node.left != null) {
                    queue.offer(node.left);
                }
                if(node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return true;
    }
}


class L528_Random_Pick_with_Weight {
    Random rand = null;
    int size = 0;
    int[] sum = null;

    public L528_Random_Pick_with_Weight(int[] w) {
        sum = new int[w.length];
        sum[0] = w[0];
        for(int i = 1; i < w.length; ++i) {
            sum[i] = sum[i - 1] + w[i];
        }

        rand = new Random();
        size = sum[sum.length - 1];
    }

    public int pickIndex() {
        int randNum = rand.nextInt(size);
        int index = Arrays.binarySearch(sum, randNum + 1);  // 注意，寻找的事randNum + 1
        if(index < 0) {
            index = -index-1;
        }
        return index;
    }
}


class L772_Basic_Calculator_III {
    public int calculate(String s) {
        LinkedList<Long> numbers = new LinkedList<>();
        LinkedList<Character> ops = new LinkedList<>();
        char[] array = s.toCharArray();

        for(int i = 0; i < array.length; ++i) {
            char ch = array[i];
            if(ch == ' ') {
                continue;
            } else if(ch >= '0' && ch <= '9') {
                long num = 0;
                while(i < array.length
                        && array[i] >= '0'
                        && array[i] <= '9') {
                    num = num * 10 + array[i] - '0';
                    i++;
                }

                i--; //recover i;
                numbers.push(num);
            } else if(ch == '(') {
                ops.push(ch);
            } else if(ch == ')') {
                while(ops.peek() != '(') {
                    doCalculation(numbers, ops);
                }
                ops.pop();  //pop the '('
            } else {
                //ops: + - * / ，
                // 注意此处判断，当前op没有比之前op高的时候，就需要将之前的进行计算求解
                // 即：所有+-操作都归一，这个思想和basic calculator I中是类似的
                while(!ops.isEmpty() && !isHigherThanPrev(ch, ops.peek()) && ops.peek() != '(') {
                    doCalculation(numbers, ops);
                }
                ops.push(ch);
            }
        }

        while(numbers.size() > 1 && !ops.isEmpty()) {
            doCalculation(numbers, ops);
        }

        return numbers.pop().intValue();
    }

    void doCalculation(LinkedList<Long> numbers, LinkedList<Character> ops) {
        char op = ops.pop();
        long second = numbers.pop();
        long first = numbers.pop();
        numbers.push(calculate(first, second, op));
    }

    long calculate(long first, long second, char op) {
        if(op == '+') {
            return first + second;
        } else if(op == '-') {
            return first - second;
        } else if(op == '*') {
            return first * second;
        } else if(op == '/') {
            return first/second;
        }

        return 0;
    }

    private boolean isHigherThanPrev(char cur, char prev) {
        if((prev == '+' || prev == '-')
                && (cur == '*' || cur == '/')) {
            return true;
        }

        return false;
    }
}









