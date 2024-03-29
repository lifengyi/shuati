package interview;

import java.util.*;

/**
 * 找每个元素左边或者右边第一个比它自身小/大的元素
 * 用单调栈来维护
 */

public class Algorithm_Stack {


    /**
     * 给一个数组，返回一个大小相同的数组。
     * 返回的数组的第i个位置的值应当是，对于原数组中的第i个元素，
     * 至少往右走多少步，才能遇到一个比自己大的元素
     * （如果之后没有比自己大的元素，或者已经是最后一个元素，
     * 则在返回数组的对应位置放上-1）
     *
     * 简单的例子：
     * input:   5,3,1,2,4
     * return: -1 3 1 1 -1
     *
     * Time: O(N)
     * Space: O(N)
     */
    public int[] findNextLargerNumber(int[] array) {
        int []ret = new int[array.length];
        LinkedList<Integer> monoStack = new LinkedList<>();

        int j = 0;
        for(int i = 0; i < array.length; ++i) {
            while(!monoStack.isEmpty() && array[i] > array[monoStack.peek()]) {
                j = monoStack.pop();
                ret[j] = i - j;
            }
            monoStack.push(i);
        }

        while(!monoStack.isEmpty())
            ret[monoStack.poll()] = -1;

        return ret;
    }


    public static void main(String[] args) {
        Algorithm_Stack processor = new Algorithm_Stack();

        int[] test1 = {5,3, 1, 2, 4};
        int[] ret = processor.findNextLargerNumber(test1);
        for(int i : ret) {
            System.out.print(i + " ");
        }
        System.out.print("\n");
    }
}


/**
 * 456. 132 Pattern
 *
 * S1  小， S2  大， S3  中
 * 正向找，可以找到S1, S2, 然后当处理S3的时候，发现新来的数据比S2小，但是也比S1小，那么就没法处理
 * 逆向找，可以看到S1和S3是邻接关系，找一个最大的S2，最大的S3，
 * 维护这一对数组中能找到的最大，第二大的数据，只需要比较新来的数据
 * 假如比最大的还要大，那么替换最大的，最大的pop出栈，替换第二大的
 * 假如比最大的小，但是比第二大的要大，那么继续入栈
 * 假如比最大的小，比第二大的也小，那么可以返回true
 *
 * 可以见我们需要转为这道题目为顺序查找数据一个比一个大的序列
 *
 * 那么栈应该维护一个递增（有小就pop）？还是递减（有大就pop）？
 * 假如维护递增序列，当来一个小的数据时，pop出所有栈中元素，无法确认第一大，第二大元素
 * 相反维护递减序列，当来大数据pop出所有，栈中维护有最大数据，pop出来的数据中有第二大元素
 *
 * 所以递减序列（有大就pop）可以让我们维护一个当前最大的数据在栈中
 * 递增序列（有小就pop）可以维护一个当前最小的数据在栈中
 *
 * 同理如果找213Pathern的题目，也就是： S1:Mid, S2:Small, S3:Big
 * 那么S2和S1形成邻接降序序列，那么我们应该维护一个最小，一个第二小
 * 正向遍历，有小的数据就pop维护一个递增的stack，
 * 栈内为当前能用的最小数据，pop出来的维护一个第二小的数据，当新数据比第二小大即可
 */
class L456_123Pattern {

    public boolean L456_find132pattern(int[] nums) {
        int s3 = Integer.MIN_VALUE;
        LinkedList<Integer> monoStack = new LinkedList<>();
        for(int i = nums.length - 1; i >= 0; --i) {
            if(nums[i] < s3)
                return true;
            while(!monoStack.isEmpty() && nums[i] > nums[monoStack.peek()]) {
                s3 = Math.max(s3, nums[monoStack.pop()]);
            }
            monoStack.push(i);
        }

        return false;
    }

}


class L84_Largest_Rectangle_in_Historgram {
    /**
     * 84. Largest Rectangle in Histogram
     *
     * Time : O(n)
     * Space: O(n)
     *
     * @param heights
     * @return
     *
     *
     * 数组中每个元素为高度，以每个高度为当前基准，向左右寻找比自己小的元素
     * 如果新数据为小，则pop栈顶数据，改高度的数据可以求得它所能达到的最大面积
     * 有小就pop，即维护一个递增序列，到最后放入一个最小的数据，pop出所有栈内
     * 数据求面积，此刻栈内数据所代表的柱状图都是享有可以延伸到最右边来求面积的
     */
    public int L84_largestRectangleArea(int[] heights) {
        LinkedList<Integer> monoStack = new LinkedList<>();
        int currentHeight = 0, indexInStack = 0, left = 0;
        int maxRectancle = 0;
        for(int i = 0; i <= heights.length; ++i) {
            if(i == heights.length)
                currentHeight = -1;
            else
                currentHeight = heights[i];
            while(!monoStack.isEmpty() && currentHeight < heights[monoStack.peek()]) {
                //pop the height + do calculation + compare/save it
                indexInStack = monoStack.pop();
                left = monoStack.isEmpty() ? 0 : monoStack.peek() + 1;
                maxRectancle = Math.max(maxRectancle, (i - left) * heights[indexInStack]);
            }
            monoStack.push(i);
        }

        return maxRectancle;
    }
}

class L85_Maximal_Rectangle {
    public int L85_maximalRectangle(char[][] matrix) {
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;

        int maxArea = 0;
        int row = matrix.length, column = matrix[0].length;
        int[] cache = new int[column];
        int heigh = 0, indexInStack = 0, left = 0;
        LinkedList<Integer> monoStack = new LinkedList<>();

        for(int i = 0; i < row; ++i) {
            for(int j = 0; j <= column; ++j) {
                if(j == column)
                    heigh = -1;
                else {
                    heigh = getHeigh(matrix, i, j);
                    cache[j] = heigh;
                }
                while(!monoStack.isEmpty() && heigh < cache[monoStack.peek()]) {
                    indexInStack = monoStack.pop();
                    left = monoStack.isEmpty() ? 0 : monoStack.peek() + 1;
                    maxArea = Math.max(maxArea, (j - left) * cache[indexInStack]);
                }

                if(j < column)
                    monoStack.push(j);
            }
        }

        return maxArea;
    }

    private int getHeigh(char[][] matrix, int row, int column) {
        int count = 0;
        for(int i = row; i < matrix.length; ++i) {
            if(matrix[i][column] == '1')  ++count;
            else break;
        }
        return count;
    }
}


/**
 * 654. Maximum Binary Tree
 *
 * Non-recursive implementation
 * Time: O(n)
 * Space: O(n)
 *
 */
class L654_Maximum_Binary_Tree {
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        if(nums == null || nums.length == 0)
            return null;

        LinkedList<TreeNode> monoStack = new LinkedList<>();
        for(int i = 0; i <= nums.length; ++i) {
            TreeNode newNode = new TreeNode(i == nums.length ? Integer.MAX_VALUE : nums[i]);
            while(!monoStack.isEmpty() && newNode.val > monoStack.peek().val) {
                TreeNode node = monoStack.pop();
                if(!monoStack.isEmpty() && monoStack.peek().val < newNode.val) {
                    monoStack.peek().right = node;
                } else {
                    newNode.left = node;
                }
            }
            monoStack.push(newNode);
        }

        return monoStack.pop().left;
    }

    /**
     * Recursive implementation
     * Time: O(n^2)
     * Space: O(n)
     *
     * @param nums
     * @return
     */
    public TreeNode L654_constructMaximumBinaryTree_v2(int[] nums) {
        return constructMaxHelper(nums,0,nums.length-1);
    }

    private TreeNode constructMaxHelper(int[] nums,int start,int end){
        if(start == end){
            TreeNode node = new TreeNode(nums[start]);
            return node;
        }
        if(start<end){
            int maxIndex = findMax(nums, start,end);
            TreeNode root = new TreeNode(nums[maxIndex]);
            if(maxIndex == start){
                root.left = null;
            }
            else{
                root.left = constructMaxHelper(nums,start,maxIndex-1);
            }
            if(maxIndex == end){
                root.right = null;
            }
            else{
                root.right = constructMaxHelper(nums,maxIndex+1, end);
            }
            return root;
        }
        else{
            return null;
        }
    }

    private int findMax(int[]nums, int start, int end){
        int maxIndex = start;
        int max = nums[start];
        for(int i =start+1;i<=end;i++){
            if(nums[i] > max){
                max = nums[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}


class L224_Basic_Calculator {
    public int calculate(String s) {
        if(s == null || s.length() == 0) {
            return 0;
        }

        int ret = 0, sign = 1;
        LinkedList<Integer> stack = new LinkedList<>();
        char[] array = s.toCharArray();

        for(int i = 0; i < array.length; ++i) {
            char ch = array[i];
            if(ch == '+') {
                sign = 1;
            } else if(ch == '-') {
                sign = -1;
            } else if(ch == ' ') {
                continue;
            } else if(ch == '(') {
                stack.push(ret);
                stack.push(sign);
                ret = 0;
                sign = 1;
            } else if (ch == ')') {
                int signInStack = stack.pop();
                int retInStack = stack.pop();
                ret = retInStack + signInStack * ret;
            } else {
                int number = 0;
                while(i < array.length && array[i] >= '0' && array[i] <= '9') {
                    number = number * 10 + array[i] - '0';
                    i++;
                }
                ret += sign * number;
                i--;    //go back to the index of the last number
            }
        }

        return ret;
    }
}


class L227_Basic_Calculator_II {
    public int calculate(String s) {
        int sign = 1, multiply = 0, divide = 0;
        LinkedList<Integer> stack = new LinkedList<>();
        char[] array = s.toCharArray();

        for(int i = 0; i < array.length; ++i) {
            char ch = array[i];
            if(ch == '+') {
                sign = 1;
            } else if(ch == '-') {
                sign = -1;
            } else if(ch == ' ') {
                continue;
            } else if(ch == '*') {
                multiply = 1;
            } else if(ch == '/') {
                divide = 1;
            } else if (ch >= '0' && ch <= '9') {
                int number = 0;
                while(i < array.length && array[i] >= '0' && array[i] <= '9') {
                    number = number * 10 + array[i] - '0';
                    i++;
                }
                i--;

                if(multiply == 1) {
                    multiply = 0;
                    int numberInStack = stack.pop();
                    numberInStack *= number;
                    stack.push(numberInStack);
                } else if (divide == 1) {
                    divide = 0;
                    int numberInStack = stack.pop();
                    numberInStack /= number;
                    stack.push(numberInStack);
                } else {
                    stack.push(sign * number);
                    sign = 1;
                }
            }
        }
        int count = 0;
        while(!stack.isEmpty()) {
            count += stack.pop();
        }
        return count;
    }
}


class L772_Basic_Calculator_III_ {
    public int calculate(String s) {
        LinkedList<Long> numbers = new LinkedList<>();
        LinkedList<Character> ops = new LinkedList<>();
        char[] array = s.toCharArray();

        for (int i = 0; i < array.length; ++i) {
            char ch = array[i];
            if (ch == ' ') {
                continue;
            } else if (ch >= '0' && ch <= '9') {
                long num = 0;
                while (i < array.length
                        && array[i] >= '0'
                        && array[i] <= '9') {
                    num = num * 10 + array[i] - '0';
                    i++;
                }

                i--; //recover i;
                numbers.push(num);
            } else if (ch == '(') {
                ops.push(ch);
            } else if (ch == ')') {
                while (ops.peek() != '(') {
                    doCalculation(numbers, ops);
                }
                ops.pop();  //pop the '('
            } else {
                // ops: + - * / ，
                // 注意此处判断，当前op没有比之前op高的时候，就需要将之前的进行计算求解
                // 即：所有+-操作都归一，这个思想和basic calculator I中是类似的
                while (!ops.isEmpty() && !isHigherThanPrev(ch, ops.peek()) && ops.peek() != '(') {
                    doCalculation(numbers, ops);
                }
                ops.push(ch);
            }
        }

        while (numbers.size() > 1 && !ops.isEmpty()) {
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
        if (op == '+') {
            return first + second;
        } else if (op == '-') {
            return first - second;
        } else if (op == '*') {
            return first * second;
        } else if (op == '/') {
            return first / second;
        }

        return 0;
    }

    private boolean isHigherThanPrev(char cur, char prev) {
        if ((prev == '+' || prev == '-')
                && (cur == '*' || cur == '/')) {
            return true;
        }

        return false;
    }
}


class L770_Basic_Calculator_IV {

    public List<String> basicCalculatorIV(String expression, String[] evalVars, int[] evalInts) {
        Map<String, Integer> evalMap = new HashMap();
        for (int i = 0; i < evalVars.length; ++i)
            evalMap.put(evalVars[i], evalInts[i]);

        return parse(expression).evaluate(evalMap).toList();
    }

    public Poly make(String expr) {
        Poly ans = new Poly();
        List<String> list = new ArrayList();
        if (Character.isDigit(expr.charAt(0))) {
            ans.update(list, Integer.valueOf(expr));
        } else {
            list.add(expr);
            ans.update(list, 1);
        }
        return ans;
    }

    public Poly combine(Poly left, Poly right, char symbol) {
        if (symbol == '+') return left.add(right);
        if (symbol == '-') return left.sub(right);
        if (symbol == '*') return left.mul(right);
        throw null;
    }

    public Poly parse(String expr) {
        List<Poly> bucket = new ArrayList();
        List<Character> symbols = new ArrayList();
        int i = 0;
        while (i < expr.length()) {
            if (expr.charAt(i) == '(') {
                int bal = 0, j = i;
                for (; j < expr.length(); ++j) {
                    if (expr.charAt(j) == '(') bal++;
                    if (expr.charAt(j) == ')') bal--;
                    if (bal == 0) break;
                }
                bucket.add(parse(expr.substring(i+1, j)));
                i = j;
            } else if (Character.isLetterOrDigit(expr.charAt(i))) {
                int j = i;
                search : {
                    for (; j < expr.length(); ++j)
                        if (expr.charAt(j) == ' ') {
                            bucket.add(make(expr.substring(i, j)));
                            break search;
                        }
                    bucket.add(make(expr.substring(i)));
                }
                i = j;
            } else if (expr.charAt(i) != ' ') {
                symbols.add(expr.charAt(i));
            }
            i++;
        }

        for (int j = symbols.size() - 1; j >= 0; --j)
            if (symbols.get(j) == '*')
                bucket.set(j, combine(bucket.get(j), bucket.remove(j+1), symbols.remove(j)));

        if (bucket.isEmpty()) return new Poly();
        Poly ans = bucket.get(0);
        for (int j = 0; j < symbols.size(); ++j)
            ans = combine(ans, bucket.get(j+1), symbols.get(j));

        return ans;
    }
}

class Poly {
    HashMap<List<String>, Integer> count;

    Poly() {
        count = new HashMap();
    }

    void update(List<String> key, int val) {
        this.count.put(key, this.count.getOrDefault(key, 0) + val);
    }

    Poly add(Poly that) {
        Poly ans = new Poly();
        for (List<String> k: this.count.keySet())
            ans.update(k, this.count.get(k));
        for (List<String> k: that.count.keySet())
            ans.update(k, that.count.get(k));
        return ans;
    }

    Poly sub(Poly that) {
        Poly ans = new Poly();
        for (List<String> k: this.count.keySet())
            ans.update(k, this.count.get(k));
        for (List<String> k: that.count.keySet())
            ans.update(k, -that.count.get(k));
        return ans;
    }

    Poly mul(Poly that) {
        Poly ans = new Poly();
        for (List<String> k1: this.count.keySet())
            for (List<String> k2: that.count.keySet()) {
                List<String> kNew = new ArrayList();
                for (String x: k1) kNew.add(x);
                for (String x: k2) kNew.add(x);
                Collections.sort(kNew);
                ans.update(kNew, this.count.get(k1) * that.count.get(k2));
            }
        return ans;
    }

    Poly evaluate(Map<String, Integer> evalMap) {
        Poly ans = new Poly();
        for (List<String> k: this.count.keySet()) {
            int c = this.count.get(k);
            List<String> free = new ArrayList();
            for (String token: k) {
                if (evalMap.containsKey(token))
                    c *= evalMap.get(token);
                else
                    free.add(token);
            }
            ans.update(free, c);
        }
        return ans;
    }

    int compareList(List<String> A, List<String> B) {
        int i = 0;
        for (String x: A) {
            String y = B.get(i++);
            if (x.compareTo(y) != 0) return x.compareTo(y);
        }
        return 0;
    }
    List<String> toList() {
        List<String> ans = new ArrayList();
        List<List<String>> keys = new ArrayList(this.count.keySet());
        Collections.sort(keys, (a, b) ->
                a.size() != b.size() ? b.size() - a.size() : compareList(a, b));

        for (List<String> key: keys) {
            int v = this.count.get(key);
            if (v == 0) continue;
            StringBuilder word = new StringBuilder();
            word.append("" + v);
            for (String token: key) {
                word.append('*');
                word.append(token);
            }
            ans.add(word.toString());
        }
        return ans;
    }
}



