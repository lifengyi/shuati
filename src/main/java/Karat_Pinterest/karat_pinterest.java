package Karat_Pinterest;

import java.util.*;

public class karat_pinterest {
}

/**
 * # Now let's switch over to the back-end of our social network. We have some automated
 * batch jobs that we use to handle expensive tasks that run periodically throughout the
 * day, like updating statistics for the most popular posts. We've been given some input
 * that shows the dependencies between each of these batch jobs.
 *
 * # For example, in this input, "clean" must be executed before "mapper" can execute.
 *
 * # Given the last execution time for each step of the workflow, we want to find the
 * set of all steps that are "stale" -- steps that have not executed since the last time
 * one of their precursor steps executed. For example, in this case, "update" is in the
 * output because "mapper" must occur before "update", but "update" has not been executed
 * since the last time "mapper" was executed. If a task is stale, all tasks after it are
 * stale too -- so "statistics" is stale because of "mapper".
 *
 * #                          /--> reducer
 * #          /---> mapper --|
 * # clean --|                \--> update --\
 * #          \                              --> statistics
 * #           \---> metadata --------------/
 * #                         \
 * #                          \--> timestamp
 *
 * # Sample input:
 * # precursor_steps = [
 * #   ["clean", "mapper"],
 * #   ["metadata", "statistics"],
 * #   ["mapper", "update"],
 * #   ["update", "statistics"],
 * #   ["clean", "metadata"],
 * #   ["mapper", "reducer"],
 * #   ["metadata", "timestamp"],
 *
 * # last_execution_times = [
 * #   ["clean", "20170302-1129"],
 * #   ["mapper", "20170302-1155"],
 * #   ["update", "20170302-1150"],
 * #   ["statistics", "20170302-1153"],
 * #   ["metadata", "20170302-1130"],
 * #   ["reducer", "20170302-1540"],
 *
 * # Sample output (in any order):
 *
 * # find_stale_steps(precursor_steps, last_execution_times) =
 * #   ["update", "statistics", "timestamp"]
 */

class Karat_Find_Stale_Job {
    String[] findStaleSteps(String[] jobs, String[] schedule) {
        // 构建有向图，从入度为0的起点开始遍历图(BFS)
        //
        // 如果入度为0的节点没有时间戳，直接放入结果集
        //
        // 根据前驱节点遍历后驱节点时，判断：
        //   1. 后驱节点没有时间信息，将后驱节点放入结果集
        //   2. 前驱节点时间小于后驱节点的时间，放入队列，继续搜索
        //   3. 后驱节点的时间大于等于前驱节点的时间，将后驱节点放入结果集
        //
        // 根据结果集中的点，进行遍历，遍历得到的点，包括结果集中原有的点即为最终结果集；
        //
        return new String[0];
    }
}


/**
 *
 * We want to find employees who badged into our secured room unusually
 * often. We have an unordered list of names and access times over a single
 * day. Access times are given as three or four-digit numbers using 24-hour
 * time, such as "800" or "2250".
 *
 * Write a function that finds anyone who badged into the room 3 or more
 * times in a 1-hour period, and returns each time that they badged in during
 * that period. (If there are multiple 1-hour periods where this was true,
 * just return the first one.)
 * * badge_records = [
 *
 * * ["Paul", 1355],
 * * ["Jennifer", 1910],
 * * ["John", 830],
 * * ["Paul", 1315],
 * * ["John", 835],
 * * ["Paul", 1405],
 * * ["Paul", 1630],
 * * ["John", 855],
 * * ["John", 915],
 * * ["John", 930],
 * * ["Jennifer", 1335],
 * * ["Jennifer", 730],
 * * ["John", 1630],
 *
 */
class Karat_User_Visit_Count {
    /**
     *  将所用用户数据存储到哈希表，key为用户名，value为时间list
     *  遍历哈希表，检测用户的时间轴中是否有超过3个数据
     *  没有：跳过
     *  如有：将时间轴数据排序，二分查找从起始时间加上1小时后的时间
     *       这需要封装个函数，当前时间加上60分钟后得到下一个整数
     *       表示的时间需要单独实现
     */

}

/**
 *    1    2  3
 *  /  \ /     \
 * 4    5       6
 *              \
 *               7
 * 输入是int[][] input, input[0]是input[1] 的parent，比如 {{1,4}, {1,5}, {2,5}, {3,6}, {6,7}}会形成上面的图
 * 第一问是只有0个parents和只有1个parent的节点
 * 第二问是 两个指定的点有没有公共祖先
 * 第三问是就一个点的最远祖先，如果有好几个就只需要输出一个就好，举个栗子，这里5的最远祖先可以是1或者2，输出任意一个就可以
 */

class Karat_Common_Ancestor {
    /**
     * 哈希表存储，key为每一个点，value为list，存储所有的父亲节点
     * 1. 遍历哈希表，输出答案
     * 2. 分别求两个指定点的所有祖先，得到两个集合，针对1个集合中的元素
     *    检测是否存在于另一个集合中；
     * 3. 从该点出发，BFS遍历所有的父亲节点，每次遍历记录当前层次中的第一个节点
     *    并最后返回该节点
     */
}


/**
 * Intuit 网上coding competition的一道题给一个矩阵，矩阵里的每个元素是1，但是其中分布着一些长方形区域，
 * 这些长方形区域中的元素为0. 要求输出每个长方形的位置（用长方形的左上角元素坐标和右下角元素坐标表示）。
 * example：
 * input:
 * [
 * [1,1,1,1,1,1], .
 * [0,0,1,0,1,1],
 * [0,0,1,0,1,0],
 * [1,1,1,0,1,0],
 * [1,0,0,1,1,1]
 * ]
 * output:.
 * [
 * [1,0,2,1],
 * [1,3,3,3],
 * [2,5,3,5],
 * [4,1,4,2]
 * ]
 * 如果 Matrix 中有多个由0组成的长方体，请返回多套值（前提每两个长方体之间是不会连接的，所以放心）. 不改变输入的做法
 * 不过还有第三问，就是connected components.
 * 第三问 基本上就是leetcode connected components,只不过是返回一个listoflist，每个list是一个component的所有点坐标
 * 那个图是1,01,0组成的矩阵，0组成的就是各种图形。跟前面关系的确不大
 * 如果矩阵里有多个不规则的形状，返回这些形状。这里需要自己思考并定义何谓“返回这些形状”
 */

class Karat_Find_Rectangle {
    /**
     *   基本上使用union-find来求解，对1的节点不操作，对0的操作进行合并
     *
     *   1. 根据题意0都组成矩形，那么遍历所有岛屿：
     *         如果岛屿长度为1且元素值为0： 则命中
     *         如果岛屿长度大于1： 则命中
     *
     *   2. connected components，基本上就是记录0的总数，以及合并的总数
     *      0的总数 - 合并的总数 = 0岛屿的个数
     *
     *   3. 需要记录每个岛屿的元素总数，同时记录岛屿中x,y的最大值最小值，
     *      检测xMax - xMin + 1 * yMax - yMin + 1 是否和 岛屿的总数相等
     *      就能得到这是否是一个规则的形状；
     */
}


/**
 * 第一題是給你一個string例如"2+3-999"回傳計算結果int.
 *
 * 第二題加上parenthesis 例如"2+((8+2)+(3-999))"一樣回傳計算結果
 *
 * 第三道题是加了变量名的。。会给你一个map比如{'a':1, 'b':2, 'c':3}，
 * 假设输入为"a+b+c+1"输出要是7，如果有未定义的变量，比如"a+b+c+1+d"输出就是7+d
 */
class Karat_Expression_Basic_Calculator {
    /**
     *  前两题基本上是leetcode原题
     */


    public static void main(String[] args) {
        String s = " express - exp + a + 32 + b - ((2 + 19 - c) + e ) - f + 24 + express + exp - e";
        //String s = "a + b + 4 + 2 - (c + d)";
        String[] express = {"a", "c"};
        int[] value = {5, 4};

        Karat_Expression_Basic_Calculator proc = new Karat_Expression_Basic_Calculator();
        System.out.println(proc.expressCalculator(s, express, value));
    }

    class Tuple {
        int sum = 0;
        int sign = 1;
        String express = null;

        public Tuple(int sum, int sign, String express) {
            this.sum = sum;
            this.sign = sign;
            this.express = express;
        }
    }

    String expressCalculator(String s, String[] express, int[] value) {
        int sum = 0, sign = 1;
        StringBuilder sb = new StringBuilder();
        LinkedList<Tuple> stack = new LinkedList<>();

        Map<String, Integer> cache = new HashMap<>();
        populate(cache, express, value);

        for(int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if(ch == ' ') {
                continue;
            } else if(ch == '+') {
                sign = 1;
            } else if(ch == '-') {
                sign = -1;
            } else if (ch >= '0' && ch <= '9') {
                int number = 0;
                while(i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                    number = number * 10 + s.charAt(i) - '0';
                    i++;
                }
                i--;
                sum += sign * number;
            } else if(ch >= 'a' && ch <= 'z') {
                StringBuilder tmp = new StringBuilder();
                while(i < s.length() && s.charAt(i) >= 'a' && s.charAt(i) <= 'z') {
                    tmp.append(s.charAt(i));
                    i++;
                }
                i--;

                //check if express has value => update sum or string
                if(cache.containsKey(tmp.toString())) {
                    sum += sign * cache.get(tmp.toString());
                } else {
                    if(sign == 1) {
                        sb.append('+');
                    } else {
                        sb.append('-');
                    }
                    sb.append(tmp.toString());
                }
            } else if(ch == '(') {
                Tuple tup = new Tuple(sum, sign, sb.toString());
                stack.push(tup);
                sum = 0;
                sign = 1;
                sb = new StringBuilder();
            } else if (ch == ')') {
                Tuple tup = stack.pop();
                sum += tup.sum + tup.sign * sum;

                //convert the sign in string
                StringBuilder tmp = new StringBuilder();
                merge(tmp, tup.express, tup.sign, sb.toString());
                sb = tmp;
            }
        }

        //simplify the express using HashMap
        return sum + simplify(sb.toString());
    }

    String simplify(String s) {
        Map<String, Integer> map = new HashMap<>();
        int sign = 1;
        for(int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if(ch == '+') {
                sign = 1;
            } else if(ch == '-') {
                sign = -1;
            } else if (ch >= 'a' && ch <= 'z'){
                StringBuilder sb = new StringBuilder();
                while(i < s.length() && s.charAt(i) >= 'a' && s.charAt(i) <= 'z') {
                    sb.append(s.charAt(i));
                    i++;
                }
                i--;

                String tmp = sb.toString();
                if(map.containsKey(tmp)) {
                    map.put(tmp, map.get(tmp) + sign);
                } else {
                    map.put(tmp, sign);
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for(String key : map.keySet()) {
            if(map.get(key) == 0) {
                continue;
            }
            if(map.get(key) == 1) {
                result.append('+').append(key);
            } else if(map.get(key) == -1) {
                result.append('-').append(key);
            }else {
                if(map.get(key) > 0) {
                    result.append('+');
                }
                result.append(map.get(key)).append('*').append(key);
            }
        }
        return result.toString();
    }

    void populate(Map<String, Integer> cache, String[] strings, int[] values) {
        for(int i = 0; i < strings.length; ++i) {
            cache.put(strings[i], values[i]);
        }
    }

    void merge(StringBuilder sb, String first, int sign, String second) {
        sb.append(first);
        if(sign == 1) {
            sb.append(second);
        } else {
            for(int i = 0; i < second.length(); ++i) {
                if(second.charAt(i) == '+') {
                    sb.append('-');
                } else if(second.charAt(i) == '-') {
                    sb.append('+');
                } else {
                    sb.append(second.charAt(i));
                }
            }
        }
    }
}

/**
 * 第二题：给每个user访问历史记录，找出两个user之间longest continuous common history
 * 输入： [
 *              ["3234.html", "xys.html", "7hsaa.html"], // user1
 *              ["3234.html", ''sdhsfjdsh.html", "xys.html", "7hsaa.html"] // user2
 *            ], user1 and user2 （指定两个user求intersect）
 *
 * 输出：["xys.html", "7hsaa.html"]
 *
 * user0 = [ "/nine.html", "/four.html", "/six.html", "/seven.html", "/one.html" ]
 * user2 = [ "/nine.html", "/two.html", "/three.html", "/four.html", "/six.html", "/seven.html" ]
 * user1 = [ "/one.html", "/two.html", "/three.html", "/four.html", "/six.html" ]
 * user3 = [ "/three.html", "/eight.html" ].
 */

class Karate_Find_Longest_Common_History {
    public void findLongestCommonUrl() {
        String s1 = "abcdefgha";
        String s2 = "abc123abcdefghabcdefab";

        int index = 0, maxLen = Integer.MIN_VALUE;
        int row = s1.length();
        int col = s2.length();
        int[] dp = new int[col + 1];

        for(int i = 1; i <= row; ++i) {
            for(int j = col; j > 0; --j) {
                if(s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[j] = dp[j - 1] + 1;
                    if(dp[j] > maxLen) {
                        index = j;
                        maxLen = dp[j];
                    }
                }
            }
            //System.out.println(Arrays.toString(dp));
        }
        System.out.println(s2.substring(index - maxLen, index));
    }
}


/**
 * 第一题：类似meeting rooms，输入是一个int[][] meetings, int start, int end,
 * 每个数都是时间，13：00 =》 1300， 9：30 =》 18930， 看新的meeting 能不能安排到meetings
 * ex: {[1300, 1500], [930, 1200],[830, 845]},
 *
 * 新的meeting
 *   [820, 830], return true;
 *   [1450, 1500] return false;
 *
 *
 * 第二题：类似merge interval，唯一的区别是输出，输出空闲的时间段，merge完后，再把两两个之间的空的输出就好，
 * 注意要加上0 - 第一个的start time
 *
 */
class Karat_meeting {
    /**
     *  类似于Merge interval， 排序所有的meetings，按照start开始排列
     *  同时也放入新的meeting，遍历，如果发现有重合的即返回错；
     *  排列可以选择多种：放入list在排列，或者直接挨个放入Heap，再挨个取出（遍历）
     */

    /**
     * 第二题就是纯的merge interval，只不过输出空闲时间，
     * 不要忘记输出最开始的0-开始时间的那一段，还有尾巴的那一段；
     */


    /**
     * 有权重，分别相加，输出所有的权重
     */

    public static void main(String[] args) {
        int[][] input = {
                {2,  9},
                {3,  12},
                {7,  8},
                {12, 18}
        };
        int[] weights = {1, 5, 3, 7};
        Karat_meeting proc = new Karat_meeting();
        List<List<Integer>> ret = proc.findAllInterval(input, weights);
        for(List<Integer> ele : ret) {
            System.out.println(ele.toString());
        }
    }

    class Node implements Comparable<Node>{
        int value = 0;
        int type = -1; //0:start, 1: end
        int weight = 0;

        public Node(int value, int type, int weight) {
            this.value = value;
            this.type = type;
            this.weight = weight;
        }

        public int compareTo(Node other) {
            if(this.value == other.value) {
                return other.type - this.type;
            } else {
                return this.value - other.value;
            }
        }
    }

    List<List<Integer>> findAllInterval(int[][] intervals, int[] weights) {

        Node[] nodes = new Node[intervals.length * 2];
        for(int i = 0; i < intervals.length; ++i) {
            Node start = new Node(intervals[i][0], 0, weights[i]);
            Node end = new Node(intervals[i][1], 1, weights[i]);
            nodes[2 * i] = start;
            nodes[2 * i + 1] = end;
        }

        Arrays.sort(nodes);

        int totalWeight = 0, lastEnd = 0;
        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0; i < nodes.length; ++i) {
            Node cur = nodes[i];
            if(totalWeight != 0) {
                add(result, lastEnd, cur.value, totalWeight);
            }
            lastEnd = cur.value;
            if(cur.type == 0) {
                totalWeight += cur.weight;
            } else {
                totalWeight -= cur.weight;
            }
        }
        return result;
    }

    void add(List<List<Integer>> result, int lastIndex, int curIndex, int weight) {
        List<Integer> list = new ArrayList<>();
        list.add(lastIndex);
        list.add(curIndex);
        list.add(weight);
        result.add(list);
    }
}





/**
 * For example, with below contents
 * 'This is the first line.\nThis is the second line.'
 *   S3Key.next() -> 'This is '
 *   S3Key.next() -> 'the firs'
 *  S3Key.next() -> 't line.\n'
 *   S3Key.next() -> 'This is '
 *   S3Key.next() -> 'the seco'
 *   S3Key.next() -> 'nd line.'
 *   S3Key.next() -> ''
 * The files can be very large and therefore impractical to fully download before
 * working with them.
 * Write a wrapper around S3Key with function next_line() which produces the next
 * line in a file.
 *   sln.next_line() => 'This is the first line.\n
 *   sln.next_line() => This is the second line
 */



class Karat_Wrap_Read_Line {
    class S3Key {
        String next() {
            return "...\n";
        }
    }

    StringBuilder sb = new StringBuilder();
    S3Key s3 = new S3Key();

    String next_line() {
        if(sb.length() < 4 * 1024) {
            getOne4K(sb);   //always try to fill the buffer to 4K
        }

        if(sb.length() == 0) {
            return "";
        }

        int index = -1;
        if(sb.indexOf("\n") != -1) {
            // assume that max length of a line < 4K
            String ret = sb.substring(0, index + 1);
            sb.delete(0, index + 1);
            return ret;
        } else {
            // if there's no '\n', it means to the end;
            return sb.toString();
        }
    }

    int getOne4K(StringBuilder sb) {
        String str = "";
        while(sb.length() < 4 * 1024 && (str = s3.next()).length() != 0) {
            sb.append(str);
        }
        if(str.length() == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}


/**
 * Given 2 input arrays of integers, unsorted.
 * 从两个array各拿出一个数相加，成为一个pair， 找出
 * largest k pairs
 *
 *   2, 3,-1,0, 5
 *   1, 4, 2
 *
 *   k = 5 --> (5, 4), (5, 2), (3, 4), (5, 1), (2, 4)
 */

class Karat_Find_TopK_in_Two_Arrays {

    class Tuple {
        int sum = 0;
        int index1 = 0;
        int index2 = 0;
        public Tuple(int sum, int index1, int index2) {
            this.sum = sum;
            this.index1 = index1;
            this.index2 = index2;
        }
    }

    public static void main(String[] args) {
        int[] a1 = {4, 5, 10, 2, 0, -5};
        int[] a2 = {9, 10, 2, 6};
        Karat_Find_TopK_in_Two_Arrays proc = new Karat_Find_TopK_in_Two_Arrays();
        List<List<Integer>> ret = proc.findTopKinTwoArrays(a1, a2, 3);
        for(List<Integer> ele : ret) {
            System.out.println(ele.toString());
        }
    }

    List<List<Integer>> findTopKinTwoArrays(int[] array1, int [] array2, int k) {

        Comparator<Integer> compl = new Comparator<Integer>(){
            @Override
            public int compare(Integer i1, Integer i2) {
                return i2.compareTo(i1);
            }
        };
        Integer[] a1 = convert(array1);
        Integer[] a2 = convert(array2);
        Arrays.sort(a1, compl);
        Arrays.sort(a2, compl);

        PriorityQueue<Tuple> heap = new PriorityQueue<>(new Comparator<Tuple>(){
            @Override
            public int compare(Tuple t1, Tuple t2) {
                return t2.sum - t1.sum;
            }
        });
        Tuple tup = new Tuple(a1[0] + a2[0], 0, 0);
        heap.offer(tup);

        List<List<Integer>> result = new ArrayList<>();
        while(!heap.isEmpty() && k != 0) {
            Tuple cur = heap.poll();
            int i1 = cur.index1, i2 = cur.index2;

            List<Integer> element = new ArrayList<>();
            element.add(a1[i1]);
            element.add(a2[i2]);
            result.add(element);
            k--;

            heap.offer(new Tuple(a1[i1 + 1] + a2[i2], i1 + 1, i2));
            heap.offer(new Tuple(a1[i1] + a2[i2 + 1], i1, i2 + 1));
        }
        return result;
    }

    Integer[] convert(int[] array) {
        Integer[] result = new Integer[array.length];
        for(int i = 0; i < array.length; ++i) {
            result[i] = Integer.valueOf(array[i]);
        }
        return result;
    }
}