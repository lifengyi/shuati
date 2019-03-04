package interview;

import java.util.*;

public class Interview_Linkedin {
}


class L238_Product_of_Array_Except_Self_ {
    public int[] productExceptSelf(int[] nums) {
        int[] result = new int[nums.length];

        result[0] = 1;
        for(int i = 1; i < nums.length; ++i) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        int factor = 1;
        for(int i = nums.length - 2; i >= 0; --i) {
            factor *= nums[i + 1];
            result[i] *= factor;
        }

        return result;
    }
}

class L23_Merge_k_Sorted_Lists_ {
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x;};
    }

    public ListNode L23_mergeKLists(ListNode[] lists) {
        if(lists == null || lists.length == 0)
            return null;

        PriorityQueue<ListNode> heap = new PriorityQueue<>(new Comparator<ListNode>(){
            @Override
            public int compare(ListNode o1, ListNode o2) {
                if(o1.val < o2.val)
                    return -1;
                if(o1.val > o2.val)
                    return 1;
                return 0;
            }
        });

        ListNode dummyHead = new ListNode(0);
        for(ListNode node : lists) {
            if(node == null)
                continue;

            heap.offer(node);
        }

        ListNode cur = dummyHead;
        while(!heap.isEmpty()) {
            cur.next = heap.poll();
            if(cur.next.next != null)
                heap.offer(cur.next.next);

            cur = cur.next;
            cur.next = null;
        }

        return dummyHead.next;
    }
}

/**
 *  Use preorder to serialize/deserialize tree
 */
class L297_Serialize_and_Deserialize_Binary_Tree_ {
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        if(root == null) {
            return sb.toString();
        }

        build(root, sb);
        return sb.toString();
    }

    void build(TreeNode node, StringBuilder sb) {
        if(node == null) {
            sb.append("null").append("#");
            return;
        }

        sb.append(node.val).append("#");
        build(node.left, sb);
        build(node.right, sb);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if(data == null || data.length() == 0) {
            return null;
        }

        LinkedList<String> queue = new LinkedList<>();
        String[] strings = data.split("#");
        queue.addAll(Arrays.asList(strings));

        return deserialize(queue);
    }

    TreeNode deserialize(LinkedList<String> list) {
        String s = list.poll();
        if(s.equals("null")) {
            return null;
        }

        TreeNode root = new TreeNode(Integer.valueOf(s));
        root.left = deserialize(list);
        root.right = deserialize(list);
        return root;
    }
}

class L33_Search_in_Rotated_Sorted_Array_ {
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1, middle = 0;
        int ret = -1;
        while(left <= right) {
            middle = left + (right - left)/2;
            if(nums[middle] == target) {
                ret = middle;
                break;
            } else if(nums[middle] < nums[right]) {
                if(target > nums[middle] && target <= nums[right]) {
                    left = middle + 1;
                } else {
                    right = middle - 1;
                }
            } else {
                if(target >= nums[left] && target < nums[middle]) {
                    right = middle - 1;
                } else {
                    left = middle + 1;
                }
            }
        }

        return ret;
    }
}


class L76_Minimum_window_SubString {
    public String minWindow(String s, String t) {
        if(s == null || t == null || s.length() == 0 || t.length() == 0 ) {
            return "";
        }
        if(s.length() < t.length()) {
            return "";
        }

        int[] destMap = new int[256];
        populate(t, destMap);
        int count = t.length();

        char[] array = s.toCharArray();
        int[] srcMap = new int[256];
        int minLen = Integer.MAX_VALUE, start = -1;

        for(int i = 0, j = 0; i < array.length; ++i) {
            while(j < array.length && !isMatched(srcMap, destMap, array[j], count)) {
                srcMap[array[j]] += 1;
                if(srcMap[array[j]] <= destMap[array[j]]) {
                    count--;
                }
                j++;
            }

            if(j == array.length) {
                break;
            }

            if(j - i + 1 < minLen) {
                minLen = j - i + 1;
                start = i;
            }

            srcMap[array[i]] -= 1;
            if(srcMap[array[i]] < destMap[array[i]]) {
                count++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    boolean isMatched(int[] src, int[] dst, char ch, int count) {
        if(src[ch] + 1 == dst[ch] && count - 1 == 0) {
            return true;
        }
        return false;
    }

    void populate(String s, int[] map) {
        for(int i = 0; i < s.length(); ++i) {
            map[s.charAt(i)] += 1;
        }
    }
}


class L716_Max_Stack_ {
    class Node {
        int val = 0;
        Node prev = null;
        Node next = null;

        public Node(int val){
            this.val = val;
        }
    }

    Node head = null;
    Node tail = null;
    TreeMap<Integer, List<Node>> cache = null;

    /** initialize your data structure here. */
    public L716_Max_Stack_() {
        cache = new TreeMap<>();
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
    }

    public void push(int x) {
        Node node = new Node(x);
        //add to list
        Node last = tail.prev;
        last.next = node;
        node.prev = last;
        node.next = tail;
        tail.prev = node;

        //add to map
        if(cache.containsKey(x)) {
            cache.get(x).add(node);
        } else {
            List<Node> list = new ArrayList<>();
            list.add(node);
            cache.put(x, list);
        }
    }

    public int pop() {
        Node node = tail.prev;
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;

        List<Node> list = cache.get(node.val);
        list.remove(list.size() - 1);
        if(list.size() == 0) {
            cache.remove(node.val);
        }
        return node.val;
    }

    public int top() {
        return tail.prev.val;
    }

    public int peekMax() {
        return cache.lastKey();
    }

    public int popMax() {
        int maxKey = cache.lastKey();
        List<Node> list = cache.get(maxKey);
        Node node = list.get(list.size() - 1);

        list.remove(list.size() - 1);
        if(list.size() == 0) {
            cache.remove(maxKey);
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = null;
        node.prev = null;

        return maxKey;
    }
}

class L50_Pow_ {
    public double myPow(double x, int n) {
        int k = n < 0 ? -n - 1 : n;         //少算1个
        double res = 1.0;
        while(k > 0) {
            res *= pow(x, lowbit(k));
            k -= lowbit(k);
        }

        return n < 0 ? 1/res/x : res;       //少算的1个，在这里补齐
    }

    int lowbit(int n) {
        return n & -n;
    }

    double pow(double x, int n) {
        double res = x;
        while(n != 1) {
            res *= res;         //注意是 res * res
            n = n >> 1;
        }
        return res;
    }


    public double myPow_v1(double x, int n) {
        if(n == 0) {
            return 1;
        }

        double res = 1.0;
        for(int i = n; i != 0; i /= 2) {
            if(i%2 != 0) {
                res *= x;
            }
            x *= x;
        }

        return n < 0 ? 1/res : res;
    }


    public double myPow_v2(double x, int n) {
        if(n < 0) {
            return 1/pow_v2(x, -n);
        } else {
            return pow_v2(x, n);
        }
    }

    double pow_v2(double x, int n) {
        if(n == 0) {
            return 1;
        }
        double half = pow_v2(x, n/2);
        if(n%2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
}

class L127_Word_Ladder_ {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> words = new HashSet<>();
        for(String word : wordList) {
            words.add(word);
        }
        words.add(beginWord);

        if(!words.contains(endWord)) {
            return 0;
        }

        LinkedList<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(beginWord);
        visited.add(beginWord);

        int size = 0, counter = 0;
        while(!queue.isEmpty()) {
            counter++;
            size = queue.size();
            for(int i = 0; i < size; ++i) {
                String cur = queue.poll();
                if(cur.equals(endWord)) {
                    return counter;
                }

                List<String> nexts = getNextStrings(cur, words);
                for(String next : nexts) {
                    if(!visited.contains(next)) {
                        visited.add(next);
                        queue.offer(next);
                    }
                }
            }
        }
        return 0;
    }

    List<String> getNextStrings(String str, Set<String> dict) {
        List<String> result = new ArrayList<>();
        char[] array = str.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            char ch = array[i];
            for(char tmp = 'a'; tmp <= 'z'; ++tmp) {
                if(tmp != ch) {
                    array[i] = tmp;
                    String newString = new String(array);
                    if(dict.contains(newString)) {
                        result.add(newString);
                    }
                }
            }
            array[i] = ch;
        }
        return result;
    }
}


class L236_Lowest_Common_Ancestor_of_a_Binary_Tree_ {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null || root == p || root == q) {
            return root;
        }

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if(left != null && right != null) {
            return root;
        } else if(left != null || right != null) {
            return left != null? left : right;
        }

        return null;
    }
}

class L339_nested_List_Weight_Sum {
    public int depthSum(List<NestedInteger> nestedList) {
        LinkedList<NestedInteger> queue = new LinkedList<>();
        queue.addAll(nestedList);

        int res = 0, sum = 0, size = 0, level = 0;
        while(!queue.isEmpty()) {
            level++;
            sum = 0;
            size = queue.size();
            for(int i = 0; i < size; ++i) {
                NestedInteger element = queue.poll();
                if(element.isInteger()) {
                    sum += element.getInteger();
                } else {
                    queue.addAll(element.getList());
                }
            }
            res += sum * level;
        }
        return res;
    }

    class NestedInteger {
        // Constructor initializes an empty nested list.
        public NestedInteger() {
        }
        // Constructor initializes a single integer.
        public NestedInteger(int value) {
        }
        // @return true if this NestedInteger holds a single integer, rather than a nested list.
        public boolean isInteger() {
            return false;
        }
        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger() {
            return 0;
        }
        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return null if this NestedInteger holds a single integer
        public List<NestedInteger> getList(){
            return new ArrayList<NestedInteger>();
        }
    }
}

class L364_Nested_List_Weight_Sum_II {

    public int depthSumInverse(List<NestedInteger> nestedList) {
        LinkedList<NestedInteger> queue = new LinkedList<>();
        queue.addAll(nestedList);

        int res = 0, sum = 0, size = 0;
        while(!queue.isEmpty()) {
            size = queue.size();
            for(int i = 0; i < size; ++i) {
                NestedInteger element = queue.poll();
                if(element.isInteger()) {
                    sum += element.getInteger();
                } else {
                    queue.addAll(element.getList());
                }
            }
            res += sum;
        }

        return res;
    }

    class NestedInteger {
        // Constructor initializes an empty nested list.
        public NestedInteger() {
        }
        // Constructor initializes a single integer.
        public NestedInteger(int value) {
        }
        // @return true if this NestedInteger holds a single integer, rather than a nested list.
        public boolean isInteger() {
            return false;
        }
        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger() {
            return 0;
        }
        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return null if this NestedInteger holds a single integer
        public List<NestedInteger> getList(){
            return new ArrayList<NestedInteger>();
        }
    }
}

class L170_Two_Sum_III_Data_Structure_Design_ {
    Map<Integer, Integer> map = null;
    List<Integer> list = null;

    /** Initialize your data structure here. */
    public L170_Two_Sum_III_Data_Structure_Design_() {
        map = new HashMap<>();
        list = new ArrayList<>();
    }

    /** Add the number to an internal data structure.. */
    public void add(int number) {
        list.add(number);
        map.put(number, map.getOrDefault(number, 0) + 1);
    }

    /** Find if there exists any pair of numbers which sum is equal to the value. */
    public boolean find(int value) {
        for(int number : list) {
            int num1 = number, num2 = value - number;
            if(num1 == num2 && map.get(num1) > 1) {
                return true;
            }
            if(num1 != num2 && map.containsKey(num2)) {
                return true;
            }
        }
        return false;
    }
}


class L254_Factor_Combinations {
    public List<List<Integer>> getFactors(int n) {
        List<List<Integer>> res = new ArrayList<>();
        if(n <= 1) {
            return res;
        }

        List<Integer> item = new ArrayList<>();
        dfs(n, 2, item, res);
        return res;
    }

    void dfs(int n, int start, List<Integer> item, List<List<Integer>> res) {
        if(n <= 1) {
            res.add(new ArrayList(item));
            return;
        }

        for(int i = start; i <= n; i++) {           //注意此处是小于等于n
            if(n%i == 0) {
                item.add(i);
                dfs(n/i, i, item, res);          //将i带入下次dfs中作为起点，
                                                    //这样保证结果集的序列是递增的，实现去重目的
                item.remove(item.size() - 1);
            }
        }
    }
}

/**
 *  使用分而治之的思想，根据层级关系分配数据
 *  层级关系不再是自顶向下，而是自底向上
 */
class L366_Find_Leaves_of_Binary_Tree {
    public List<List<Integer>> findLeaves(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if(root == null) {
            return res;
        }

        height(root, res);
        return res;
    }

    private int height(TreeNode node, List<List<Integer>> res) {
        if(node == null) {
            return -1;
        }

        int leftHeight = height(node.left, res);
        int rightHeight = height(node.right, res);
        int level = Math.max(leftHeight, rightHeight) + 1;

        if(res.size() == level) {
            res.add(new ArrayList<>());
        }
        res.get(level).add(node.val);
        return level;
    }
}


/**
 *  Flip Binary Tree
 *  In the flip operation, left most node becomes
 *  the root of flipped tree and its parent become
 *  its right child and the right sibling become
 *  its left child and same should be done for all
 *  left most nodes recursively.
 *
 *   root->left->left = root->right;
 *   root->left->right = root;
 *   root->left = NULL;
 *   root->right = NULL;
 */
class L156_Binary_Tree_Upside_Down {
    public TreeNode upsideDownBinaryTree(TreeNode root) {
        if(root == null) {
            return root;
        }

        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode node = root;
        while(node.left != null) {
            stack.push(node);
            node = node.left;
        }

        TreeNode head = node;  //new root
        while(!stack.isEmpty()) {
            node = stack.pop();
            rotate(node);
        }
        return head;
    }

    void rotate(TreeNode root) {
        root.left.left = root.right;
        root.left.right = root;
        root.left = null;
        root .right = null;
    }
}



/**
 *  不算水井排序，只能算是普通的设计题，只是设计到随机访问
 *
 *  list 就是为了随机访问才使用，通过随机出来的序列能O(1)访问元素
 *
 *  map  为了在插入和删除的时候实现O(1)时间内查找是否存在
 *
 *       为了O(1)删除，则map的value必须存储数据存储的索引
 *       但是删除一个元素之后，其他数据索引是否需要变更？
 *       为了不动其他数据的索引，将数据链表最后一个数据填补
 *       到被删除的这个数据位置上去；
 *       map中需要将尾部数据的存储索引进行更新，同时删除
 *       map中被覆盖元素的项
 */
class L380_Insert_Delete_getRandom_O1 {
    List<Integer> nums = null;
    Map<Integer, Integer> locs = null;
    Random random = null;

    public L380_Insert_Delete_getRandom_O1() {
        nums = new ArrayList<>();
        locs = new HashMap<>();
        random = new Random();
    }

    public boolean insert(int val) {
        if(locs.containsKey(val)) {
            return false;
        }

        locs.put(val, nums.size());
        nums.add(val);
        return true;
    }

    public boolean remove(int val) {
        if(!locs.containsKey(val)) {
            return false;
        }

        int index = locs.get(val);
        if(index < nums.size() - 1) {
            int lastValue = nums.get(nums.size() - 1);
            nums.set(index, lastValue);                     //对于ArrayList的O(1)删除法
            //注意此处的set操作，而非add操作
            locs.put(lastValue, index);
        }
        nums.remove(nums.size() - 1);
        locs.remove(val);
        return true;
    }

    public int getRandom() {
        int index = random.nextInt(nums.size());
        return nums.get(index);
    }
}


/**
 *  有点类似 3sum的处理方案，遍历所有的点，
 *  每一次遍历，再使用双指针进行内部遍历
 *
 *  特殊点：外层的遍历所有点，很巧妙，是从第三个开始，
 *  将最大边进行从小到大进行遍历；
 *
 */
class L611_Valid_Triangle_Number {
    public int triangleNumber(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int res = 0;
        Arrays.sort(nums);
        for(int third = 2; third < nums.length; ++third) {
            int first = 0, second = third - 1;
            while(first < second) {
                if(nums[first] + nums[second] > nums[third]) {
                    res += second - first;
                    second--;
                } else {
                    first++;
                }
            }
        }

        return res;
    }
}



class L256_Paint_House {
    public int minCost(int[][] costs) {
        if(costs == null || costs.length == 0 || costs[0].length < 3) {
            return 0;
        }

        for(int i = 1; i < costs.length; ++i) {
            costs[i][0] = costs[i][0] + Math.min(costs[i-1][1], costs[i-1][2]);
            costs[i][1] = costs[i][1] + Math.min(costs[i-1][0], costs[i-1][2]);
            costs[i][2] = costs[i][2] + Math.min(costs[i-1][1], costs[i-1][0]);
        }

        int index = costs.length - 1;
        return Math.min(costs[index][0], Math.min(costs[index][1], costs[index][2]));
    }
}


/**
 *   尽量把逻辑放到 hasNext中去
 */
class L341_Flatten_Nested_List_Iterator {

    LinkedList<NestedInteger> stack = null;

    public L341_Flatten_Nested_List_Iterator(List<NestedInteger> nestedList) {
        stack = new LinkedList<>();
        addElements(stack, nestedList);
    }

    public Integer next() {
        return stack.pop().getInteger();
    }

    public boolean hasNext() {
        NestedInteger ni = null;
        while(!stack.isEmpty() && !stack.peek().isInteger()) {
            ni= stack.pop();
            addElements(stack, ni.getList());
        }

        return !stack.isEmpty();  //防止 [[], [], []]这样的情况，所以始终返回对栈的判空
    }

    void addElements(LinkedList<NestedInteger> stack, List<NestedInteger> elements) {
        for(int i = elements.size() - 1; i >= 0; --i) {
            stack.push(elements.get(i));
        }
    }

    class NestedInteger {
        // @return true if this NestedInteger holds a single integer, rather than a nested list.
        public boolean isInteger() {
            return false;
        }
        // @return the single integer that this NestedInteger holds, if it holds a single integer
        // Return null if this NestedInteger holds a nested list
        public Integer getInteger(){
            return 0;
        }
        // @return the nested list that this NestedInteger holds, if it holds a nested list
        // Return null if this NestedInteger holds a single integer
        public List<NestedInteger> getList(){
            return new ArrayList<>();
        }
    }
}


/**
 *   dp[i][j], 前i个硬币组成j最少的硬币数是多少
 *   第一列设置为0，表示前i个硬币组成0最少的硬币数是0
 *   第一行设置为无效值，此处用amount + 1，没有用Integer.MAX_VALUE + 1
 *   （因为后续做Math.min操作中，有可能遇到 1 + Integer.MAX_VALUE,就会变成负数）
 *   同理，-1也不可以在这里作为无效值的代表，所以采用一个比较稳妥的值作为无效值
 *
 *   之所以-1和Integer.MAX_VALUE无法作为无效值，主要是因为我们用了min操作
 *
 */
class L322_Coin_Change_ {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for(int i = 1; i <= coins.length; ++i) {
            for(int j = coins[i - 1]; j <= amount; ++j) {
                dp[j] = Math.min(dp[j], 1 + dp[j - coins[i - 1]]);
                //无效值如果使用-1或者MAX_VALUE,这里可能出现负值
            }
        }

        return dp[amount] > amount ? -1 : dp[amount];
    }
}



class L20_Valid_Parentheses_ {
    public boolean isValid(String s) {
        LinkedList<Character> stack = new LinkedList<>();
        for(char c : s.toCharArray()) {
            if(c == '(')
                stack.push(')');
            else if(c == '[')
                stack.push(']');
            else if(c == '{')
                stack.push('}');
            else if(stack.isEmpty() || stack.pop() != c)
                return false;
        }

        return stack.isEmpty();
    }
}


/**
 * 区间DP
 */
class L516_Longest_Palindromic_Subsequence__ {
    public int longestPalindromeSubseq(String s) {
        if(s == null || s.length() == 0) {
            return 0;
        }

        int len = s.length();
        int[][] dp = new int[len][len];
        for(int i = 0; i < len * len; ++i) {
            dp[i/len][i%len] = -1;
        }

        return search(s, 0, s.length() - 1, dp);
    }

    int search(String s, int start, int end, int[][] dp) {
        if(start == end) {
            return 1;
        } else if(s.charAt(start) == s.charAt(end) && start + 1 == end) {
            return 2;
        } else if(dp[start][end] != -1) {
            return dp[start][end];
        }

        int res = 0;
        if(s.charAt(start) == s.charAt(end)) {
            res = 2 + search(s, start + 1, end - 1, dp);
        } else {
            res = Math.max(search(s, start + 1, end, dp), search(s, start, end - 1, dp));
        }
        dp[start][end] = res;
        return res;
    }
}



class L706_Design_HashMap {
    int num = 1001;
    int[][] hashmap = null;


    public L706_Design_HashMap() {
        hashmap = new int[num][0];
    }


    public void put(int key, int value) {
        if(hashmap[key%num].length == 0) {
            hashmap[key%num] = new int[num];
            Arrays.fill(hashmap[key%num], -1);
        }
        hashmap[key%num][key/num] = value;
    }


    public int get(int key) {
        if(hashmap[key%num].length == 0) {
            return -1;
        }

        return hashmap[key%num][key/num];
    }


    public void remove(int key) {
        if(hashmap[key%num].length != 0) {
            hashmap[key%num][key/num] = -1;
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
 * 这个题即使k = 2，仍然没有什么除搜索外好的解法，所有的解法时间复杂度都在多项式时间以上，
 * 所以事实上它是一个NP问题 (https://baike.baidu.com/item/NP%E9%97%AE%E9%A2%98/2860567?fr=aladdin)，
 * 更不用说当k > 2时。
 * 这里能采用的方法仅仅是不断搜索。但是即使是搜索，也有许多的优化和方式可做，这里给出一种直观的搜索方法，和另一种较快的DP方法。
 *
 *
 * 方法一（常规深度优先搜索）：
 *     1. 很容易想到每一个子集和必须为target = sum(nums) / k，如果除不尽，那么一定会返回False。
 *
 *     2. 先模拟出k个子集，对于nums中最后一个数n，将其弹出。遍历k个子集，只要加入n后，这个子集和不超过target，
 *        就把它加入这个子集当中，然后带着当前的选择，继续递归搜索nums（此时nums已不包括n）。
 *
 *     4. 重复上述过程，如果nums最后为空，那么说明搜索成功了。
 *
 *     这种方法十分直观，但是速度很慢，不过有一些加速方法可以采用，这里列举其中一些：
 *         a. k个子集从前到后递归，如果当前的子集和与前一个子集和相同，那么这个子集就不用试了，因为把n放到这个子集和放到前一个子集没有差别。
 *            我们只关心能否搜索到，并不关心具体的分配方案。
 *         b. 先把nums排序，并优先先放入最大的元素，这样能减少许多搜索路径。
 *            一旦找到nums[i] > target，那么就直接返回False。因为如果某一个元素，都超过了target，那么就一定不合题。
 *
 *     复杂度分析：
 *     a. 时间复杂度：O(k ^ N)，其中N时nums的长度，k是子集数。如果采用了优化方案a，则复杂度至少降到O(k ^ (N - k) * k!)，
 *        因为一开始会跳过很多和为0的子集，至少前k个元素的搜索次数不超过O(k!)。
 *     b. 空间复杂度：O(N)， 用于函数调用栈。
 *
 *
 *
 * 方法二：（构造一种序列化的搜索，相较方法一，减少冗余；不过该方法难度较大，且不好想）
 * 方法一尽管经过优化，但是理论的时间复杂度仍然很大，其中的重要原因是*存在部分重复的搜索，
 * 当nums和当前所有子集和相同时，之后的搜索运行了不止一次，而且如果只是分组排列不同，其实结果无差别，
 * 但在方法一中有可能会继续搜索。
 *
 * 方法一并不能解决这些问题。要解决重复搜索的问题，一种有效的方法是
 *      *构造一种序列化的搜索，并对于已搜索过的序列不重复搜索。由此引出方法二。
 *
 * 同方法一，首先target = sum(nums) / k。接着用变量used表示nums[i]的使用情况，当且仅当nums[i]已经用过时，
 * used的第i位为1。仍然是搜索，只不过这次的搜索方法是是
 *      *寻找一个nums的序列，使得按照这个序列使用nums的元素时，能够正好构造出一个接一个的子集。
 *
 * 接着我们的任务就变成了构造出这样一个序列。
 * 构造过程中，在确定序列中下一个元素时，需要遍历nums中的元素，*只有当used的对应位为0时，才表示这个元素还没有在序列中，
 * 也即还没有用过，可以去考虑这个元素。那么这个没有用过的元素，能否作为序列中的下一个元素呢？
 *
 * 这就需要一个变量todo，表示当前所有未用元素的和，
 * 此时序列下一个元素的大小不能超过remain = (todo - 1) % target + 1。
 * 这个式子的含义就是todo % target，只不过如果todo % target == 0 时，会得到target。remain也就是序列到目前为止，正在构造的这个子集和，还需要的值。
 * 当nums中的一个元素被认为能作为序列的下一个元素时，就把其对应的used中的位置为1，并让todo减去这个元素值，进行下一次递归搜索。
 * 注意当used给定时，todo是固定的！为了加速搜索过程，可*用一个数组visit记录当前used是否访问过，如果访问过，不用再往下搜索了，直接返回False。*因为如果访问过当前的used并且能搜索成功的话，整个递归栈早就停止搜索返回True了。
 * 当used的所有位全为1时，todo应该是0，此时搜索结束，返回True。
 *
 * 复杂度分析：
 * 时间复杂度：O(N * 2 ^ N)，N是nums的长度，因为只有2 ^ N个used情况，每种情况其自身只需要O(N)的时间去遍历nums。
 * 空间复杂度O(2 ^ N)，主要用于visit数组。
 *
 *
 *
 * 当K = 2， 可以转换成01背包
 *
 */
class L698_Partition_to_K_Equal_Sum_Subsets_ {
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



class L450_Delete_Node_in_a_BST_ {
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root == null) {
            return null;
        }

        if(key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if(key > root.val) {
            root.right = deleteNode(root.right, key);
        } else {
            if(root.left == null && root.right == null) {
                root = null;
            } else if(root.left == null) {
                return root.right;
            } else  if(root.right == null) {
                return root.left;
            } else {
                TreeNode node = findLeftMostNode(root.right);
                root.val = node.val;
                root.right = deleteNode(root.right, root.val);
            }
        }

        return root;
    }

    TreeNode findLeftMostNode(TreeNode root) {
        TreeNode node = root;
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }
}


class L373_Find_K_Pairs_with_Smallest_Sums {
    public List<int[]> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>(){
            public int compare(int[] a, int[] b) {
                return a[0] + a[1] - b[0] - b[1];
            }
        });

        List<int[]> res = new ArrayList<>();
        if(nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 || k == 0) {
            return res;
        }

        for(int i = 0; i < nums1.length; ++i) {
            pq.offer(new int[]{nums1[i], nums2[0], 0});
        }

        while(res.size() < k && pq.size() > 0) {
            int[] item = pq.poll();
            res.add(new int[]{item[0], item[1]});
            if(item[2] < nums2.length - 1) {
                item[2]++;
                item[1] = nums2[item[2]];
                pq.offer(item);
            }
        }

        return res;
    }
}


class L671_Second_Minimum_Node_In_a_Binary_Tree {
    public int findSecondMinimumValue(TreeNode root) {
        PriorityQueue<TreeNode> pq = new PriorityQueue<>(new Comparator<TreeNode>(){
            public int compare(TreeNode n1, TreeNode n2) {
                return n1.val - n2.val;
            }
        });
        pq.offer(root);

        int k = 2, prev = -1;
        while(k > 0 && pq.size() > 0) {
            TreeNode node = pq.poll();
            if(node.left != null) {
                pq.offer(node.left);
            }
            if(node.right != null) {
                pq.offer(node.right);
            }

            int cur = node.val;
            if (prev != cur){
                prev = cur;
                k--;
            }
        }

        return k == 0 ? prev : -1;
    }
}


class L243_Shortest_Word_Distance_ {
    public int shortestDistance(String[] words, String word1, String word2) {
        int i1 = -1, i2 = -1;
        int shortestDistance = Integer.MAX_VALUE;
        for(int i = 0; i < words.length; ++i) {
            if(words[i].equals(word1)) {
                i1 = i;
            } else if(words[i].equals(word2)) {
                i2 = i;
            }

            if(i1 != -1 && i2 != -1) {
                shortestDistance = Math.min(shortestDistance, Math.abs(i1 - i2));
            }
        }
        return shortestDistance;
    }
}


class L244_Shortest_Word_Distance_II_ {
    Map<String, List<Integer>> cache = null;

    public L244_Shortest_Word_Distance_II_(String[] words) {
        cache = new HashMap<>();
        for(int i = 0; i < words.length; ++i) {
            if(cache.containsKey(words[i])) {
                cache.get(words[i]).add(i);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                cache.put(words[i], list);
            }
        }
    }

    public int shortest(String word1, String word2) {
        List<Integer> l1 = cache.get(word1);
        List<Integer> l2 = cache.get(word2);
        int distance = Integer.MAX_VALUE;
        int i1 = 0, i2 = 0;
        while(i1 < l1.size() && i2 < l2.size()) {
            int num1 = l1.get(i1);
            int num2 = l2.get(i2);
            if(num1 < num2) {
                distance = Math.min(distance, num2 - num1);
                i1++;
            } else {
                distance = Math.min(distance, num1 - num2);
                i2++;
            }
        }
        return distance;
    }
}


class L245_Shortest_Word_Distance_III_ {
    public int shortestWordDistance(String[] words, String word1, String word2) {
        boolean isSame = word1.equals(word2);
        int p1 = -1, p2 = -1;
        int distance = Integer.MAX_VALUE;
        for(int i = 0; i < words.length; ++i) {
            if(words[i].equals(word1)) {
                if(isSame) {
                    p1 = p2;
                    p2 = i;
                } else {
                    p1 = i;
                }
            }
            if(!isSame && words[i].equals(word2)) {
                p2 = i;
            }
            if(p1 != -1 && p2 != -1) {
                distance = Math.min(distance, Math.abs(p1 - p2));
            }
        }
        return distance;
    }
}



class L101_Symmetric_Tree_ {
    public boolean isSymmetric(TreeNode root) {
        if(root == null) {
            return true;
        }

        return isSymmetric(root.left, root.right);
    }

    private boolean isSymmetric(TreeNode node1, TreeNode node2) {
        if(node1 == null && node2 == null) {
            return true;
        } else if(node1 == null || node2 == null) {
            return false;
        } else {
            if(node1.val == node2.val) {
                return isSymmetric(node1.left, node2.right) & isSymmetric(node1.right, node2.left);
            }
            return false;
        }
    }
}



class L543_Diameter_of_Binary_Tree {
    int max = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        height(root);
        return max;
    }

    int height(TreeNode root) {
        if(root == null) {
            return 0;
        }

        int leftHeight = height(root.left);
        int rightHeight = height(root.right);

        max = Math.max(max, leftHeight + rightHeight);
        return 1 + Math.max(leftHeight, rightHeight);
    }
}


class L152_Maximum_Product_Subarray_ {
    public int maxProduct(int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int maxRes = Integer.MIN_VALUE;
        int max = 1, min = 1, tmp = 0;
        for(int i = 0; i < nums.length; ++i) {
            tmp = max;
            max = Math.max(nums[i], Math.max(max * nums[i], min * nums[i]));
            min = Math.min(nums[i], Math.min(tmp * nums[i], min * nums[i]));
            maxRes = Math.max(maxRes, max);
        }
        return maxRes;
    }
}


class L61_Rotate_List {
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public ListNode rotateRight(ListNode head, int k) {
        if(head == null || k == 0) {
            return head;
        }

        ListNode node = head;
        int count = 1;
        while(node.next != null){
            node = node.next;
            count++;
        }

        if(k % count == 0) {        //注意是取模，不是等于
            return head;
        } else if(k > count) {
            k = k % count;
        }

        ListNode slow = head, quick = head;
        while(k > 0) {
            quick = quick.next;
            k--;
        }
        while(quick.next != null) {
            slow = slow.next;
            quick = quick.next;
        }

        ListNode newHead = slow.next;
        slow.next = null;
        quick.next = head;
        return newHead;
    }
}


class L35_Search_Insert_Position {
    public int searchInsert(int[] nums, int target) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int start = 0, end = nums.length;
        int mid = 0;
        while(start < end) {
            mid = start + (end - start)/2;
            if(nums[mid] == target) {
                return mid;
            }
            if(nums[mid] < target) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        return start;
    }
}


class L12_Integer_to_Roman_ {
    public String intToRoman(int num) {
        char[][] map = {
                {'I', 'V'},
                {'X', 'L'},
                {'C', 'D'},
                {'M'}
        };

        StringBuilder sb = new StringBuilder();
        char[] array = String.valueOf(num).toCharArray();
        int level = array.length - 1;
        for(int i = 0; i < array.length; ++i) {
            int value = array[i] - '0';
            if(value > 0 && value < 4) {
                for(int j = 0; j < value; ++j) {
                    sb.append(map[level][0]);
                }
            } else if(value == 4) {
                sb.append(map[level][0]).append(map[level][1]);
            } else if(value >= 5 && value < 9) {
                sb.append(map[level][1]);
                for(int j = 0; j < value - 5; ++j) {
                    sb.append(map[level][0]);
                }
            } else if(value == 9){
                sb.append(map[level][0]).append(map[level + 1][0]);
            }
            level--;
        }
        return sb.toString();
    }
}

class L261_Graph_Valid_Tree_ {
    class UnionFind {
        int counter = 0;
        int[] father = null;

        public UnionFind(int n) {
            this.counter = n;
            this.father = new int[n];
            for(int i = 0; i < n; ++i) {
                father[i] = i;
            }
        }

        public int findRoot(int p) {
            while(p != father[p]) {
                father[p] = father[father[p]];
                p = father[p];
            }
            return p;
        }

        public boolean isConnected(int p, int q) {
            return findRoot(p) == findRoot(q);
        }

        public void connect(int p, int q) {
            int rootp = findRoot(p);
            int rootq = findRoot(q);
            if(rootp != rootq) {
                father[rootp] = rootq;
                counter--;
            }
        }
    }

    public boolean validTree(int n, int[][] edges) {
        UnionFind uf = new UnionFind(n);
        for(int[] edge : edges) {
            if(uf.isConnected(edge[0], edge[1])) {
                return false;
            } else {
                uf.connect(edge[0], edge[1]);
            }
        }
        return uf.counter == 1 ? true : false;
    }
}


class L57_Insert_Interval_ {

    public class Interval {
        int start;
        int end;
        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }

    public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
        if(intervals == null || newInterval == null) {
            return intervals;
        }
        if(intervals.size() == 0) {
            intervals.add(newInterval);
            return intervals;
        }

        List<Interval> res = new ArrayList<>();
        boolean isAdded = false;
        for(int i = 0; i < intervals.size(); ++i) {
            Interval cur = intervals.get(i);
            if(isAdded == true || cur.end < newInterval.start) {        //如果已经加完了，就直接add所有节点
                res.add(cur);
            } else if(cur.start > newInterval.end) {
                isAdded = true;
                res.add(newInterval);
                res.add(cur);
            } else {
                newInterval.start = Math.min(cur.start, newInterval.start);
                newInterval.end = Math.max(cur.end, newInterval.end);
            }
        }

        if(!isAdded) {                 //判断是否已经加了新节点
            res.add(newInterval);
        }
        return res;
    }


    public List<Interval> insert_v1(List<Interval> intervals, Interval newInterval) {
        Comparator<Interval> comp = new Comparator<Interval>(){
            @Override
            public int compare(Interval i1, Interval i2) {
                return i1.start - i2.start;
            }
        };

        List<Interval> newIntervals = new ArrayList<>();
        newIntervals.addAll(intervals);
        newIntervals.add(newInterval);
        Collections.sort(newIntervals, comp);

        Interval first = newIntervals.get(0);
        int start = first.start;
        int end = first.end;

        List<Interval> result = new ArrayList<>();
        for(Interval interval : newIntervals) {
            if(interval.start <= end) {
                end = Math.max(end, interval.end);
            } else {
                result.add(new Interval(start, end));
                start = interval.start;
                end = interval.end;
            }
        }
        result.add(new Interval(start, end));
        return result;
    }
}




class L658_Find_K_Closet_Elements {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        LinkedList<Integer> res = new LinkedList<>();

        int index = Arrays.binarySearch(arr, x);
        if(index < 0) {
            index = -index - 1;
        }


        int left = index - 1;
        int right = index;
        for(int i = 0; i < k; ++i) {
            if(isLeftCloser(arr, x, left, right)) {
                res.offerFirst(arr[left]);
                left--;
            } else {
                res.offerLast(arr[right]);
                right++;
            }
        }

        return res;
    }

    boolean isLeftCloser(int[] arr, int target, int left, int right) {
        if(left < 0) {
            return false;
        } else if(right >= arr.length) {
            return true;
        }

        if(Math.abs(target - arr[left]) <= Math.abs(target - arr[right])) {
            return true;
        }
        return false;
    }
}

