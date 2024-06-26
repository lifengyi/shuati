package interview;

import java.util.*;

public class Algorithm_BFS {

}


class L323_Number_Of_Connected_Components_in_an_Undirected_Graph_ {
    public int countComponents_v2(int n, int[][] edges) {
        if(n == 0 || edges == null) {
            return 0;
        }

        if(edges.length == 0) {
            return n;
        }

        Map<Integer, Set<Integer>> graph = initializeGraph(n, edges);
        int[] nodes = new int[n];
        int count = 0, index = 0;
        while((index = getUnvisitedNode(nodes, index)) != -1) {
            count++;
            connect(graph, nodes, index);
        }

        return count;
    }

    private void connect(Map<Integer, Set<Integer>> graph, int[] nodes, int node) {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(node);
        nodes[node] = 1;

        while(!queue.isEmpty()) {
            int index = queue.poll();
            for(int neighbour : graph.get(index)) {
                if(nodes[neighbour] == 0) {
                    queue.offer(neighbour);
                    nodes[neighbour] = 1;
                }
            }
        }
    }

    private int getUnvisitedNode(int[] nodes, int index) {
        for(int i = index; i < nodes.length; ++i) {
            if(nodes[i] == 0)
                return i;
        }
        return -1;
    }

    private Map<Integer, Set<Integer>> initializeGraph(int n, int[][] edges){
        Map<Integer, Set<Integer>> ret = new HashMap<>();
        for(int i = 0; i < n; ++i) {
            ret.put(i, new HashSet<Integer>());
        }

        for(int[] edge : edges) {
            ret.get(edge[0]).add(edge[1]);
            ret.get(edge[1]).add(edge[0]);
        }

        return ret;
    }
}


class L261_Graph_Valid_Tree__ {
    public boolean validTree(int n, int[][] edges) {
        if(edges == null || edges.length + 1 != n) {
            return false;
        }

        Map<Integer, Set<Integer>> graph = generateGraph(n, edges);
        LinkedList<Integer> queue = new LinkedList<>();
        Set<Integer> set = new HashSet<>();
        int count = 0;

        queue.offer(0);
        set.add(0);
        while(!queue.isEmpty()) {
            int node = queue.poll();
            count++;
            for(int neighbour : graph.get(node)) {
                if(!set.contains(neighbour)) {
                    queue.offer(neighbour);
                    set.add(neighbour);
                }
            }
        }

        if(count == n) {
            return true;
        }

        return false;
    }

    private Map<Integer, Set<Integer>> generateGraph(int n, int[][] edges) {
        Map<Integer, Set<Integer>> ret = new HashMap<>();
        for(int i = 0; i < n; ++i) {
            ret.put(i, new HashSet<Integer>());
        }

        for(int[] edge : edges) {
            ret.get(edge[0]).add(edge[1]);
            ret.get(edge[1]).add(edge[0]);
        }

        return ret;
    }
}

class L102_Binary_Tree_Level_Order_Traversal {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if(root == null) {
            return ret;
        }

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for(int i = 0; i < size; ++i) {
                TreeNode node = queue.poll();
                level.add(node.val);

                if(node.left != null) {
                    queue.offer(node.left);
                }
                if(node.right != null) {
                    queue.offer(node.right);
                }
            }
            ret.add(level);
        }

        return ret;
    }
}


class L103_Binary_Tree_Zigzag_Level_Order_Traversal {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<>();
        if(root == null) {
            return ret;
        }

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int count = 0;
        while(!queue.isEmpty()) {
            int size = queue.size();
            LinkedList<Integer> level = new LinkedList<>();
            for(int i = 0; i < size; ++i) {
                TreeNode node = queue.poll();
                if(node.left != null) {
                    queue.offer(node.left);
                }
                if(node.right != null) {
                    queue.offer(node.right);
                }
                if((count & 1) == 1) {
                    level.addFirst(node.val);
                } else {
                    level.addLast(node.val);
                }
            }
            ret.add((List<Integer>)level);
            count++;
        }

        return ret;
    }

    public List<List<Integer>> zigzagLevelOrder_v2(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if(root == null) {
            return result;
        }

        int level = 0;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);

        while(!queue.isEmpty()) {
            int size = queue.size();
            if(level%2 == 0) {
                // poll node from head
                // process left -> right and put them to tail
                processLeftToRight(queue, size, result);
            } else {
                // poll node from tail
                // process right -> left and put them to head
                processRightToLeft(queue, size, result);
            }
            level++;
        }
        return result;
    }

    private void processLeftToRight(LinkedList<TreeNode> queue, int size,
                                    List<List<Integer>> result) {
        List<Integer> candidate = new ArrayList<>();
        for(int i = 0; i < size; ++i) {
            TreeNode node = queue.pollFirst();
            candidate.add(node.val);

            if(node.left != null) {
                queue.offerLast(node.left);
            }
            if(node.right != null) {
                queue.offerLast(node.right);
            }
        }
        result.add(candidate);
    }

    private void processRightToLeft(LinkedList<TreeNode> queue, int size,
                                    List<List<Integer>> result) {
        List<Integer> candidate = new ArrayList<>();
        for(int i = 0; i < size; ++i) {
            TreeNode node = queue.pollLast();
            candidate.add(node.val);

            if(node.right != null) {
                queue.offerFirst(node.right);
            }
            if(node.left != null) {
                queue.offerFirst(node.left);
            }
        }
        result.add(candidate);
    }
}

class UndirectedGraphNode {
    int label;
    List<UndirectedGraphNode> neighbors;
    UndirectedGraphNode(int x) {
        label = x;
        neighbors = new ArrayList<UndirectedGraphNode>();
    }
}

class L133_Clone_Graph_v2 {

    class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    public Node cloneGraph(Node node) {
        if(node == null) {
            return null;
        }

        Map<Node, Node> map = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            if(visited.contains(curNode)) {
                continue;
            }
            visited.add(curNode);

            // copy point
            if(!map.containsKey(curNode)) {
                Node newNode = new Node(curNode.val, new ArrayList<>());
                map.put(curNode, newNode);
            }

            // copy edges
            for(Node neighbor : curNode.neighbors) {
                if(!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                }

                if(!map.containsKey(neighbor)) {
                    Node newNeighbor = new Node(neighbor.val, new ArrayList<>());
                    map.put(neighbor, newNeighbor);
                }

                map.get(curNode).neighbors.add(map.get(neighbor));
            }
        }

        return map.get(node);
    }
}

class L133_Clone_Graph {
    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        if(node == null) {
            return null;
        }

        List<UndirectedGraphNode> nodes = initializeAllNodes(node);
        Map<UndirectedGraphNode, UndirectedGraphNode> map = new HashMap<>();
        //copy all nodes
        copyNodes(nodes, map);

        //copy all edges
        copyEdges(nodes, map);

        return map.get(node);
    }

    private void copyNodes(List<UndirectedGraphNode> nodes, Map<UndirectedGraphNode, UndirectedGraphNode> map) {
        for(UndirectedGraphNode node : nodes) {
            map.put(node, new UndirectedGraphNode(node.label));
        }
    }

    private void copyEdges(List<UndirectedGraphNode> nodes, Map<UndirectedGraphNode, UndirectedGraphNode> map) {
        for(UndirectedGraphNode node : nodes) {
            UndirectedGraphNode newNode = map.get(node);
            for(UndirectedGraphNode neighbor : node.neighbors) {
                newNode.neighbors.add(map.get(neighbor));
            }
        }
    }

    private List<UndirectedGraphNode> initializeAllNodes(UndirectedGraphNode node) {
        List<UndirectedGraphNode> nodes = new ArrayList<>();

        LinkedList<UndirectedGraphNode> queue = new LinkedList<>();
        Set<UndirectedGraphNode> set = new HashSet<>();
        queue.offer(node);
        set.add(node);

        while(!queue.isEmpty()) {
            UndirectedGraphNode currentNode = queue.poll();
            for(UndirectedGraphNode currentNeighbor : currentNode.neighbors) {
                if(!set.contains(currentNeighbor)) {
                    queue.offer(currentNeighbor);
                    set.add(currentNeighbor);
                }
            }
        }

        nodes.addAll(set);
        return nodes;
    }
}

/**
 * queue + set, set不能省略；
 * 如果没有set检测，当目标字符串没在目标集中时
 * 会引起死循环
 */
class L127_Word_Ladder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if(wordList == null || wordList.isEmpty()) {
            return 0;
        }

        Set<String> wordSet = getAllWords(wordList);
        LinkedList<String> queue = new LinkedList<>();
        Set<String> set = new HashSet<>();
        queue.offer(beginWord);
        set.add(beginWord);
        int count = 1;
        while(!queue.isEmpty()) {
            count++;
            int size = queue.size();
            for(int i = 0; i < size; ++i) {
                String word = queue.poll();
                List<String> nextWords = getValidWords(word, wordSet);
                for(String nextWord : nextWords) {
                    if(nextWord.equals(endWord)) {
                        return count;
                    }

                    if(!set.contains(nextWord)) {
                        queue.offer(nextWord);
                        set.add(nextWord);
                    }
                }
            }
        }

        return 0;
    }

    private List<String> getValidWords(String word, Set<String> wordSet) {
        List<String> ret = new ArrayList<>();
        char[] wordArray = word.toCharArray();
        for(int i = 0; i < wordArray.length; ++i) {
            char temp = wordArray[i];
            for(char ch = 'a'; ch <= 'z'; ch++) {
                if(ch != temp) {
                    wordArray[i] = ch;
                    String nextWord = new String(wordArray);
                    if(wordSet.contains(nextWord)) {
                        ret.add(nextWord);
                    }
                }
            }
            wordArray[i] = temp;
        }

        return ret;
    }

    private Set<String> getAllWords(List<String> wordList) {
        Set<String> ret = new HashSet<>();
        for(String word : wordList) {
            ret.add(word);
        }
        return ret;
    }
}


class L126_word_ladder_II_bfs_outOfMemory {
    class WordLadderNode {
        private String value;
        private Set<String> sequence;

        public WordLadderNode(String value) {
            this.value = value;
            this.sequence = new LinkedHashSet<>();
            this.sequence.add(value);
        }

        public WordLadderNode(WordLadderNode node, String value) {
            this.value = value;
            this.sequence = node.getSequence();
            this.sequence.add(value);
        }

        public boolean contains(String value) {
            return sequence.contains(value);
        }

        public String getValue() {
            return this.value;
        }

        public Set<String> getSequence() {
            return new LinkedHashSet(this.sequence);
        }

        public List<String> getSequenceList() {
            return new ArrayList(this.sequence);
        }

    }

    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> ret = new ArrayList<>();
        if(wordList == null || wordList.isEmpty()) {
            return ret;
        }

        Set<String> wordSet = getWordSet(wordList);
        if(!wordSet.contains(endWord)) {
            return ret;
        }

        LinkedList<WordLadderNode> queue = new LinkedList<>();
        queue.offer(new WordLadderNode(beginWord));
        int minSeqSize = Integer.MAX_VALUE;

        while(!queue.isEmpty()) {
            WordLadderNode currentNode = queue.poll();
            List<WordLadderNode> nodeList = getNextWordLadderNodes(currentNode, wordSet);
            for(WordLadderNode node : nodeList) {
                String nodeValue = node.getValue();
                if(nodeValue.equals(endWord)) {
                    List<String> seq = node.getSequenceList();
                    minSeqSize = addNewSequence(ret, minSeqSize, seq);
                } else {
                    queue.offer(node);
                }
            }
        }

        return ret;
    }

    private int addNewSequence(List<List<String>> result, int minSeqSize, List<String> sequence) {
        int size = sequence.size();
        if(size == minSeqSize) {
            result.add(sequence);
        } else if ( size < minSeqSize) {
            result.clear();
            result.add(sequence);
        } else {
            size = minSeqSize;
        }

        return size;
    }

    private List<WordLadderNode> getNextWordLadderNodes(WordLadderNode node, Set<String> wordSet) {
        List<WordLadderNode> ret = new ArrayList<>();
        char[] nodeValueArray = node.getValue().toCharArray();

        for(int i = 0; i < nodeValueArray.length; ++i) {
            char temp = nodeValueArray[i];
            for(char ch = 'a'; ch <= 'z'; ch++) {
                if(ch != temp) {
                    nodeValueArray[i] = ch;
                    String nextWord = new String(nodeValueArray);
                    if(wordSet.contains(nextWord) && !node.contains(nextWord)) {
                        WordLadderNode nextNode = new WordLadderNode(node, nextWord);
                        ret.add(nextNode);
                    }
                }
            }
            nodeValueArray[i] = temp;
        }

        return ret;
    }

    private Set<String> getWordSet(List<String> wordList) {
        Set<String> ret = new HashSet<>();
        for(String word : wordList) {
            ret.add(word);
        }
        return ret;
    }
}

class L126_word_ladder_II_dfs_timeLimit {
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> ret = new ArrayList<>();
        if(wordList == null || wordList.isEmpty()) {
            return ret;
        }

        Set<String> wordSet = getWordSet(wordList);
        Map<String, Set<String>> graph = generateGraph(beginWord, wordList, wordSet);

        Set<String> sequence = new LinkedHashSet<>();
        sequence.add(beginWord);
        dfs(beginWord, endWord, graph, sequence, ret);

        return ret;
    }


    private void dfs(String curWord, String endWord, Map<String, Set<String>> graph, Set<String> sequence, List<List<String>> result) {
        if(curWord.equals(endWord)) {
            addSequence(result, new ArrayList(sequence));
            return;
        }

        Set<String> neighbours = graph.get(curWord);
        for(String neighbour : neighbours) {
            if(sequence.contains(neighbour)) {
                continue;
            }
            sequence.add(neighbour);
            dfs(neighbour, endWord, graph, sequence, result);
            sequence.remove(neighbour);
        }
    }

    private void addSequence(List<List<String>> result, List<String> sequence) {
        if(result.size() == 0) {
            result.add(sequence);
        } else {
            int curSeqSize = result.get(0).size();
            int seqSize = sequence.size();
            if(seqSize == curSeqSize) {
                result.add(sequence);
            } else if(seqSize < curSeqSize) {
                result.clear();
                result.add(sequence);
            }
        }
    }

    private Map<String, Set<String>> generateGraph(String beginWord, List<String> wordList, Set<String> wordSet) {
        Map<String, Set<String>> ret = new HashMap<>();

        ret.put(beginWord, new HashSet<String>());
        for(String word : wordList) {
            ret.put(word, new HashSet<String>());
        }

        addNeighbors(beginWord, ret, wordSet);
        for(String word : wordList) {
            addNeighbors(word, ret, wordSet);
        }

        return ret;
    }

    private void addNeighbors(String word, Map<String, Set<String>> graph, Set<String> wordSet) {
        Set<String> neighbors = graph.get(word);
        char[] array = word.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            char temp = array[i];
            for(char ch = 'a'; ch <= 'z'; ++ch) {
                if(ch == temp) {
                    continue;
                }
                array[i] = ch;
                String nextWord = new String(array);
                if(wordSet.contains(nextWord)) {
                    neighbors.add(nextWord);
                }
            }
            array[i] = temp;
        }
    }



    private Set<String> getWordSet(List<String> wordList) {
        Set<String> ret = new HashSet<>();
        for(String word : wordList) {
            ret.add(word);
        }
        return ret;
    }
}

/**
 * 1. 创建字符串变迁转换图，注意beginWord和endWord是否在图中的各种情况
 * 2. BFS遍历图，得到从begin到end的最短路径，制作出分层信息表（当然存在无法到达end的情况）
 * 3. 根据之前的分层信息表，从end到begin做DFS，逐级分层遍历，得到路径图
 *
 */
class L126_word_ladder_II_bfs_dfs {
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> ret = new ArrayList<>();
        if(wordList == null || wordList.isEmpty()) {
            return ret;
        }

        Set<String> wordSet = getWordSet(wordList);
        // endWord doesn't exist in word list
        if(!wordSet.contains(endWord)) {
            return ret;
        }
        if(!wordSet.contains(beginWord)) {
            wordSet.add(beginWord);
        }

        Map<String, Set<String>> graph = generateGraph(wordSet);
        Map<String, Integer> distance = new HashMap<>();
        bfs(beginWord, endWord, graph, distance);
        if(!distance.containsKey(endWord)) {
            return ret;
        }

        LinkedList<String> sequence = new LinkedList<>();
        sequence.offerFirst(endWord);
        dfs(endWord, beginWord, graph, distance, sequence, ret);

        return ret;
    }

    private void bfs(String beginWord, String endWord, Map<String, Set<String>> graph, Map<String, Integer> distance) {
        int pathSize = 0;
        LinkedList<String> queue = new LinkedList<>();
        Set<String> set = new HashSet<>();

        queue.offer(beginWord);
        set.add(beginWord);
        distance.put(beginWord, pathSize);

        while(!queue.isEmpty()) {
            int size = queue.size();
            pathSize++;
            for(int i = 0; i < size; ++i) {
                String currentWord = queue.poll();
                for(String nextWord : graph.get(currentWord)) {
                    if(!set.contains(nextWord)) {
                        queue.offer(nextWord);
                        set.add(nextWord);
                        distance.put(nextWord, pathSize);
                    }
                }
            }
        }
    }

    private void dfs(String curWord, String endWord, Map<String, Set<String>> graph, Map<String, Integer> distance, LinkedList<String> sequence, List<List<String>> result) {
        if(curWord.equals(endWord)) {
            result.add(new ArrayList(sequence));
            return;
        }

        Set<String> neighbours = graph.get(curWord);
        for(String neighbour : neighbours) {
            if(distance.get(neighbour) >= distance.get(curWord)) {
                continue;
            }
            sequence.offerFirst(neighbour);
            dfs(neighbour, endWord, graph, distance, sequence, result);
            sequence.pollFirst();
        }
    }

    private Map<String, Set<String>> generateGraph(Set<String> wordSet) {
        Map<String, Set<String>> graph = new HashMap<>();

        for(String word : wordSet) {
            graph.put(word, new HashSet<String>());
            addNeighbors(word, graph, wordSet);
        }

        return graph;
    }

    private void addNeighbors(String word, Map<String, Set<String>> graph, Set<String> wordSet) {
        Set<String> neighbors = graph.get(word);
        char[] array = word.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            char temp = array[i];
            for(char ch = 'a'; ch <= 'z'; ++ch) {
                if(ch == temp) {
                    continue;
                }
                array[i] = ch;
                String nextWord = new String(array);
                if(wordSet.contains(nextWord)) {
                    neighbors.add(nextWord);
                }
            }
            array[i] = temp;
        }
    }



    private Set<String>  getWordSet(List<String> wordList) {
        Set<String> ret = new HashSet<>();
        for(String word : wordList) {
            ret.add(word);
        }
        return ret;
    }
}

class DirectedGraphNode {
    int label;
    ArrayList<DirectedGraphNode> neighbors;
    DirectedGraphNode(int x) {
        label = x;
        neighbors = new ArrayList<DirectedGraphNode>();
    }
}

class LintCode_topological_sorting {

    public ArrayList<DirectedGraphNode> topSort_bfs(ArrayList<DirectedGraphNode> graph) {
        // write your code here
        ArrayList<DirectedGraphNode> ret = new ArrayList<>();
        if(graph == null) {
            return ret;
        }

        Map<DirectedGraphNode, Integer> indegree = countIndegree(graph);
        LinkedList<DirectedGraphNode> queue = new LinkedList<>();
        for(DirectedGraphNode node : graph) {
            if(indegree.get(node) == 0) {
                queue.offer(node);
                ret.add(node);
            }
        }
        while(!queue.isEmpty()) {
            DirectedGraphNode currentNode = queue.poll();
            for(DirectedGraphNode neighbor : currentNode.neighbors) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if(indegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                    ret.add(neighbor);
                }
            }
        }

        return ret;
    }

    private Map<DirectedGraphNode, Integer> countIndegree(ArrayList<DirectedGraphNode> graph) {
        Map<DirectedGraphNode, Integer> indegree = new HashMap<>();
        for(DirectedGraphNode node : graph) {
            indegree.put(node, 0);
        }
        for(DirectedGraphNode node : graph) {
            for(DirectedGraphNode neighbor : node.neighbors) {
                indegree.put(neighbor, indegree.get(neighbor) + 1);
            }
        }
        return indegree;
    }
}

class L207_course_Schedule {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        if(numCourses == 0 || prerequisites == null) {
            return false;
        }

        Map<Integer, List<Integer>> graph = generateGraph(numCourses, prerequisites);
        int[] indegree = countIndegree(numCourses, graph);

        LinkedList<Integer> queue = new LinkedList<>();
        for(int i = 0; i < numCourses; ++i) {
            if(indegree[i] == 0) {
                queue.offer(i);
            }
        }

        if(queue.size() == 0){
            return false;
        }

        int count = 0;
        while(!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            for(int neighbor : graph.get(course)) {
                indegree[neighbor]--;
                if(indegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return count == numCourses ? true : false;
    }

    private Map<Integer, List<Integer>> generateGraph(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for(int i = 0; i < numCourses; ++i) {
            graph.put(i, new ArrayList<Integer>());
        }
        for(int[] edge : prerequisites) {
            int to = edge[0];
            int from = edge[1];
            graph.get(from).add(to);
        }
        return graph;
    }

    private int[] countIndegree(int numCourses, Map<Integer, List<Integer>> graph) {
        int[] indegree = new int[numCourses];
        for(int i = 0; i < numCourses; ++i) {
            for(int neighbor : graph.get(i)) {
                indegree[neighbor]++;
            }
        }
        return indegree;
    }
}

class L210_Course_Schedule_II {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        Map<Integer, Set<Integer>> edges = createDefaultGraph(numCourses);
        int[] indegree = new int[numCourses];
        populateGraph(edges, indegree, prerequisites);

        LinkedList<Integer> queue = new LinkedList<>();
        for(int i = 0; i < indegree.length; ++i) {
            if(indegree[i] == 0) {
                queue.offer(i);
            }
        }

        int[] ret = new int[numCourses];
        int count = 0;
        while(!queue.isEmpty()) {
            int num = queue.poll();
            ret[count++] = num;

            for(int next : edges.get(num)) {
                indegree[next]--;
                if(indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        if(count == numCourses) {
            return ret;
        }
        return new int[0];
    }

    void populateGraph(Map<Integer, Set<Integer>> edges, int[] indegree,
                       int[][] prerequisites) {
        for(int[] prerequisite : prerequisites) {
            int first = prerequisite[1];
            int second = prerequisite[0];
            edges.get(first).add(second);
            indegree[second]++;
        }
    }

    Map<Integer, Set<Integer>> createDefaultGraph(int n) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for(int i = 0; i < n; ++i) {
            graph.put(i, new HashSet<Integer>());
        }
        return graph;
    }
}

class L444_Sequence_Reconstruction {
    public boolean sequenceReconstruction(int[] org, List<List<Integer>> seqs) {
        if(org == null || org.length == 0 || seqs == null || seqs.size() == 0) {
            return false;
        }

        Map<Integer, Set<Integer>> graph = generateGraph(seqs);
        int nodeNumber = graph.size();
        Map<Integer, Integer> indegree = countIndegree(graph);

        LinkedList<Integer> queue = new LinkedList<>();
        for(int node : indegree.keySet()) {
            if(indegree.get(node) == 0) {
                queue.offer(node);
            }
        }

        if(queue.size() != 1) {
            return false;
        }

        List<Integer> order = new ArrayList<>();
        int totalCount = 0;
        while(!queue.isEmpty()) {
            int levelCount = 0;
            int node = queue.poll();
            totalCount++;
            order.add(node);
            for(int neighbor : graph.get(node)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if(indegree.get(neighbor) == 0) {
                    if(++levelCount != 1) {
                        return false;
                    }
                    queue.offer(neighbor);
                }
            }
        }

        if(totalCount != nodeNumber || totalCount != org.length) {
            return false;
        }

        System.out.println(order.toString());
        int index = 0;
        for(int node : order) {
            if(node != org[index]) {
                return false;
            }
            index++;
        }

        return true;
    }

    private Map<Integer, Set<Integer>> generateGraph(List<List<Integer>> seqs) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();

        for(List<Integer> seq : seqs) {
            for(int node : seq) {
                if(!graph.containsKey(node)) {
                    graph.put(node, new HashSet<Integer>());
                }
            }
        }

        for(List<Integer> seq : seqs) {
            int seqSize = seq.size();
            if(seqSize < 2) {
                continue;
            }
            for(int i = 1; i < seqSize; ++i) {
                int from = seq.get(i - 1);
                int to = seq.get(i);
                graph.get(from).add(to);
            }
        }

        return graph;
    }

    private Map<Integer, Integer> countIndegree(Map<Integer, Set<Integer>> graph) {
        Map<Integer, Integer> indegree = new HashMap<>();
        for(int node : graph.keySet()) {
            indegree.put(node, 0);
        }
        for(int node : graph.keySet()) {
            for(int neighbor : graph.get(node)) {
                indegree.put(neighbor, indegree.get(neighbor) + 1);
            }
        }

        return indegree;
    }
}

/**
 * 注意: 二位矩阵保存2个坐标的方法
 */
class L200_Number_of_Islands_ {
    public int numIslands(char[][] grid) {
        if(grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int count = 0;
        for(int i = 0; i < grid.length; ++i) {
            for(int j = 0; j < grid[0].length; ++j) {
                if(grid[i][j] == '1') {
                    count++;
                    bfs(i, j, grid);
                }
            }
        }
        return count;
    }

    void bfs(int x, int y, char[][] grid) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        int col = grid[0].length;
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(x * col + y);
        grid[x][y] = '0';
        while(!queue.isEmpty()) {
            int node = queue.poll();
            int nodex = node/col;
            int nodey = node%col;
            for(int i = 0; i < 4; ++i) {
                int nx = nodex + dx[i];
                int ny = nodey + dy[i];
                if(validate(nx, ny, grid) && grid[nx][ny] == '1') {
                    queue.offer(nx * col + ny);
                    grid[nx][ny] = '0';
                }
            }
        }
    }

    boolean validate(int x, int y, char[][]grid) {
        if(x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            return false;
        }
        return true;
    }
}

/**
 * Check if meet the following requirements:
 * 1. If there's no number in seqs/seq
 * 2. If there're numbers bigger than max number
 * 3. If there're numbers whose order is not matched with the order in org
 * 4. If the edge's number is n-1 (n is the number of nodes and the edge
 *    must connect the adjacent nodes)
 */
class L444_Sequence_Reconstruction_check_Node_Edge {
    public boolean sequenceReconstruction(int[] org, List<List<Integer>> seqs) {
        if(org == null || seqs == null || org.length == 0 || seqs.size() == 0) {
            return false;
        }

        int maxNumber = org.length;
        int totalEdge = maxNumber - 1;
        int[] index = getIndexMap(org);
        int[] visited = new int[index.length];
        boolean hasNumber = false;
        for(List<Integer> seq : seqs) {
            int seqSize = seq.size();
            if(seqSize == 0) {
                continue;
            }
            hasNumber = true;
            if(seqSize == 1) {
                if(seq.get(0) > maxNumber || seq.get(1) <= 0) {
                    return false;
                }
                continue;
            }
            for(int i = 1; i < seqSize; ++i) {
                int pre = seq.get(i - 1);
                int cur = seq.get(i);
                if(cur > maxNumber || pre > maxNumber
                        || cur <= 0 || pre <= 0
                        ||index[pre] >= index[cur]) {
                    return false;
                }
                if(index[pre] + 1 == index[cur]){
                    if(visited[pre] == 1) {
                        continue;
                    }
                    visited[pre] = 1;
                    totalEdge--;
                }
            }
        }

        return (hasNumber && totalEdge == 0) ? true : false;

    }

    private int[] getIndexMap(int[] org) {
        int[] index = new int[org.length + 1];
        index[0] = -1;
        for(int i = 0; i < org.length; ++i) {
            index[org[i]] = i;
        }
        return index;
    }
}

class LintCode_573_Build_Post_Office_II {
    public int WALL = 2;
    public int HOUSE = 1;
    public int EMPTY = 0;

    public int shortestDistance(int[][] grid) {
        // write your code here
        if(grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }

        int row = grid.length;
        int col = grid[0].length;
        int[][] distance = new int[row][col];
        int[][] visitedTimes = new int[row][col];
        List<Integer> houseList = new ArrayList<>();
        for(int i = 0; i < row; ++i) {
            for(int j = 0; j < col; ++j) {
                if(grid[i][j] == HOUSE) {
                    houseList.add(i * col + j);
                }
            }
        }

        for(int house : houseList) {
            bfs(house, grid, distance, visitedTimes);
        }

        return getShortestDistance(grid, houseList.size(), distance, visitedTimes);
    }

    private void bfs(int house, int[][] grid, int[][] distance, int[][] visitedTimes) {
        int row = grid.length;
        int col = grid[0].length;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        int step = 0;

        int[][] visited = new int[row][col];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(house);

        while(!queue.isEmpty()) {
            int size = queue.size();
            step++;
            for(int i = 0; i < size; ++i) {
                int curHouse = queue.poll();
                int x = curHouse/col;
                int y = curHouse%col;
                for(int j = 0; j < 4; ++j) {
                    int nx = x + dx[j];
                    int ny = y + dy[j];
                    if(validate(nx, ny, grid) && visited[nx][ny] != 1) {
                        queue.offer(nx * col + ny);
                        visited[nx][ny] = 1;
                        distance[nx][ny] += step;
                        visitedTimes[nx][ny] += 1;
                    }
                }
            }
        }
    }

    private boolean validate(int x, int y, int[][]grid) {
        if(x < 0 || y < 0 || x >= grid.length || y >= grid[0].length
                || grid[x][y] != EMPTY) {
            return false;
        }

        return true;
    }

    private int getShortestDistance(int[][] grid, int houseNumber, int[][] distance, int[][] visitedTimes) {
        int row = grid.length;
        int col = grid[0].length;
        int shortestDistance = Integer.MAX_VALUE;
        boolean allHousesFound = false;
        for(int i = 0; i < row; ++i) {
            for(int j = 0; j < col; ++j) {
                if(grid[i][j] == EMPTY && visitedTimes[i][j] >= houseNumber) {
                    allHousesFound = true;
                    shortestDistance = Math.min(shortestDistance, distance[i][j]);
                }
            }
        }

        return allHousesFound ? shortestDistance : -1;
    }
}

class LintCode_611_Knight_Shortest_Path {
    class Point {
        int x;
        int y;
        Point() { x = 0; y = 0; }
        Point(int a, int b) {
            x = a;
            y = b;
        }
    }
    public boolean EMPTY = false;
    public boolean BARRIER = true;
    public int shortestPath(boolean[][] grid, Point source, Point destination) {
        // write your code here
        if(grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }
        if(!validate(source.x, source.y, grid)
                || !validate(destination.x, destination.y, grid)
                || grid[source.x][source.y] == BARRIER
                || grid[destination.x][destination.y] == BARRIER) {
            return -1;
        }

        int col = grid[0].length;
        int srcIndex = source.x * col + source.y;
        int destIndex = destination.x * col + destination.y;
        if(srcIndex == destIndex) {
            return 0;
        }

        int shortestPath = 0;
        int[] dx = {1, 1, -1, -1, 2, 2, -2, -2};
        int[] dy = {2, -2, 2, -2, 1, -1, 1, -1};

        LinkedList<Integer> queue = new LinkedList<>();
        Set<Integer> set = new HashSet<>();
        queue.offer(srcIndex);
        set.add(srcIndex);

        while(!queue.isEmpty()) {
            int size = queue.size();
            shortestPath++;
            for(int i = 0; i < size; ++i) {
                int cur = queue.poll();
                int x = cur/col;
                int y = cur%col;
                for(int j = 0; j < 8; ++j) {
                    int nx = x + dx[j];
                    int ny = y + dy[j];
                    if(validate(nx, ny, grid) && grid[nx][ny] == EMPTY) {
                        int neighbor = nx * col + ny;
                        if(neighbor == destIndex) {
                            return shortestPath;
                        }
                        if(!set.contains(neighbor)) {
                            set.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }

        return -1;
    }

    private boolean validate(int x, int y, boolean[][] grid) {
        if(x < 0 || y < 0 || x >= grid.length || y >= grid[0].length) {
            return false;
        }
        return true;
    }
}


/**
 *  迷宫系列：
 *  I: 普通的BFS解法，遍历每一次撞墙后的stop点，
 *     check是否访问过来排除节点，即已经访问过的节点不再处理
 *     遍历完整个图中的所有可能撞墙点；
 *
 *  II: 在上述的过程中求解撞墙点的最短路径，可以理解为单源最短路径
 *      有当前节点出发，所有撞墙点都是一个有效的顶点；
 *      实现优化，省略了visited空间，可以做一个length的矩阵
 *      根据长度是否长于已有值来进行排除节点，即已经访问过的有最短路径的节点不再处理
 *      遍历完整个图的所有有效顶点；
 *      改进：其实从堆里面取出目标节点的话，就可以退出了；
 *      因为从堆中取出节点就表示了，当前取出的点最短路径已经确定了
 *      Dijkstra算法
 *
 *  III：在上述的题目里加入条件，最短路径下，路径的字典序
 *       节点类中加入一个新的元素，字符串类型，用于保存走过的路径
 *       堆的排序中，按照长度如果相同，则按照路径的字典序
 *       犹豫不需要求解最短路径的长度，可以省去length矩阵
 *       但是取消了这个矩阵，就需要维护visited矩阵，
 *       当节点从堆里取出，标注已经访问过，后续如果再取出来，
 *       无需比较length了（因为也没有length表来保存结果），
 *       直接根据visited来丢弃已经处理过的节点，因为之前已经取出来过了
 *       即该点最短路径已经确定；
 *
 *       对于每个撞墙点，找neighbour，返回的条件是：
 *       1： 撞墙，或  2：进洞
 */
class L490_The_Maze_ {
    public boolean hasPath(int[][] maze, int[] start, int[] destination) {
        if(maze == null || maze.length == 0 || maze[0].length == 0) {
            return false;
        }

        int row = maze.length;
        int col = maze[0].length;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        int[][] visited = new int[row][col];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(getNum(start[0], start[1], col));
        visited[start[0]][start[1]] = 1;

        while(!queue.isEmpty()) {
            int num = queue.poll();
            for(int i = 0; i < 4; ++i) {
                int nx = num/col;
                int ny = num%col;
                while(isSpace(nx + dx[i], ny + dy[i], maze, visited)) {
                    nx += dx[i];
                    ny += dy[i];
                }
                if(visited[nx][ny] == 1) {
                    continue;
                }
                if(nx == destination[0] && ny == destination[1]) {
                    return true;
                }
                visited[nx][ny] = 1;
                queue.offer(getNum(nx, ny, col));
            }
        }
        return false;
    }

    boolean isSpace(int x, int y, int[][] maze, int[][] visited) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length
                && maze[x][y] == 0) {
            return true;
        }
        return false;
    }

    int getNum(int x, int y, int col) {
        return x * col + y;
    }
}


/**
 *  参考Dijkstra算法的求解
 *  O(mn * log(mn))
 *
 *  是否需要考虑到寻找neighbour所花费时间复杂度？
 *  O(mn * log(mn) + mn * Max(m,n))
 *
 */
class L505_The_Maze_II_ {
    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        if(maze == null || maze.length == 0 || maze[0].length == 0) {
            return -1;
        }

        int row = maze.length;
        int col = maze[0].length;
        int minSteps = Integer.MAX_VALUE;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        PriorityQueue<Tuple> queue = new PriorityQueue<>(new Comparator<Tuple>(){
            @Override
            public int compare(Tuple t1, Tuple t2) {
                return t1.steps - t2.steps;
            }
        });

        int[][] length = new int[row][col];
        for(int i = 0; i < row * col; ++i) {
            length[i/col][i%col] = Integer.MAX_VALUE;
        }

        queue.offer(new Tuple(start[0], start[1], 0));
        length[start[0]][start[1]] = 0;

        while(!queue.isEmpty()) {
            Tuple curTuple = queue.poll();

            for(int j = 0; j < 4; ++j) {
                int x = curTuple.x;
                int y = curTuple.y;
                int steps = curTuple.steps;
                while(isSpace(x + dx[j], y + dy[j], maze)) {
                    x += dx[j];
                    y += dy[j];
                    steps++;
                }

                if(length[x][y] <= steps) {
                    continue;
                }

                length[x][y] = steps;
                queue.offer(new Tuple(x, y, steps));
            }
        }

        if(length[destination[0]][destination[1]] == Integer.MAX_VALUE) {
            return -1;
        }
        return length[destination[0]][destination[1]];
    }

    boolean isSpace(int x, int y, int[][] maze) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length
                && maze[x][y] == 0) {
            return true;
        }
        return false;
    }

    class Tuple {
        int x;
        int y;
        int steps;
        public Tuple(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }
    }
}

class L499_The_Maze_III_ {
    class Tuple implements Comparable<Tuple> {
        int x = 0;
        int y = 0;
        int s = 0;
        String p = "";
        public Tuple(int x, int y , int s, String p) {
            this.x = x;
            this.y = y;
            this.s = s;
            this.p = p;
        }

        public void appendDirection(String dir) {
            this.p = this.p + dir;
        }

        public int compareTo(Tuple other) {
            if(this.s != other.s) {
                return this.s - other.s;
            }
            return this.p.compareTo(other.p);
        }
    }

    String[] dir = {"u", "d", "l", "r"};
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};
    private final String NOT_FOUND = "impossible";

    public String findShortestWay(int[][] maze, int[] ball, int[] hole) {
        if(maze == null || maze.length == 0 || maze[0].length == 0) {
            return NOT_FOUND;
        }

        int row = maze.length;
        int col = maze[0].length;
        PriorityQueue<Tuple> queue = new PriorityQueue<>();
        int[][] visited = new int[row][col];

        queue.offer(new Tuple(ball[0], ball[1], 0, ""));
        while(!queue.isEmpty()) {
            Tuple tuple = queue.poll();
            if(visited[tuple.x][tuple.y] == 1) {
                continue;
            }

            visited[tuple.x][tuple.y] = 1;
            if(tuple.x == hole[0] && tuple.y == hole[1]) {
                return tuple.p;
            }

            for(int i = 0; i < 4; ++i) {
                Tuple stopPoint = walk(tuple, i, maze, hole);
                queue.offer(stopPoint);
            }
        }
        return NOT_FOUND;
    }

    private Tuple walk(Tuple tuple, int idx, int[][] maze, int[] hole) {
        int x = tuple.x;
        int y = tuple.y;
        int steps = tuple.s;
        String path = tuple.p + dir[idx];
        while(isSpace(x + dx[idx], y + dy[idx], maze)) {
            x += dx[idx];
            y += dy[idx];
            steps++;
            if(x == hole[0] && y == hole[1]) {
                return new Tuple(x, y, steps, path);
            }
        }
        return new Tuple(x, y, steps, path);
    }

    private boolean isSpace(int x, int y, int[][] maze) {
        if(x >= 0 && x < maze.length && y >= 0 && y < maze[0].length
                && maze[x][y] != 1) {
            return true;
        }
        return false;
    }
}


/**
 *  有向图拓扑排序判断是否有闭环： 通过判断节点访问的总数是否和图节点总数相同
 *  无向图DFS判断是否有闭环：    通过访问数组，检测是否访问到一个曾经访问过的节点
 *
 *  有向图拓扑排序判断是否有孤岛： 基本不涉及孤岛问题，因为孤岛会被当做indegree为0在第一时间被处理
 *  无向图DFS判断是否有孤岛：    通过判断节点访问的总数是否和图节点总数相同
 *
 *
 *  269的题目我的理解有歧义，我的理解是：基本上是一个有向无环图，所有words中出现的字符都是一个节点
 *  1. 有向图出现闭环，为错
 *  2. 有向图出现孤岛，为错，以为无法判断孤岛和其他节点的优先顺序
 *  3. 有多个入度为0的节点，为错，因为无法判断这些点的优先顺序
 *      a->c  b->c， ab无法判断预先顺序
 *  4. 出现平行节点，为错，以为无法判断这些点的优先顺序
 *      a->b  a->c， bc无法判断有限顺序
 *
 *  在此题意理解的基础上，证明是否存在一个决定节点顺序的路径，
 *  只能使用DFS遍历图，只要存在有一条遍历的路径满足一下条件：
 *  a. DFS过程中遇到visited节点，就是错；(检查条件1)
 *  b. 遍历判断是否能访问到所有节点，不能就是错（检查条件2、4）
 *  c. 判断起始的时候入度为0的节点，超过1个，就是错；（检查条件3，这是唯一一个用到入度的地方）
 *
 *  在有向图的深度遍历实现中，有一点类似332_Reconstruct_Itinerary
 *  那道题说明了肯定存在一个路径，同时也给定了起点和终点，
 *  也同时限定了给定输入中，不存在重复边；所以实现比较容易
 *  但那时需要cover所有边，本题是需要cover所有点，有些许不同，
 *
 *  但是深度遍历的思想以及代码实现上基本是类似的；
 *
 *  这道题目中首先不肯定存在这么一条路径，需要去找到；同时起点也需要自己去找
 *  有向/无向图的深度遍历中，在临接表不再使用set进行存储，
 *  因为深度遍历需要对邻接表进行挪出/挪进临接点，所以最好使用queue
 *  332题中，需要在有向图中找到一个字母顺序最小的路径，所以用了PriorityQueue
 *  如果此题按照我的理解来进行解题的话，则可以使用LinkedList实现Queueu进行操作offer/pull操作
 *  同时为了排除重复边的问题，首先生成使用set的图，最后图生成完毕之后，再最后转成使用queue的图
 *
 *  这个题目的本意没那么复杂，就是一个普通的拓扑排序问题
 */
class L269_Alien_Dictionary {
    class Solution {
        public String alienOrder(String[] words) {
            Map<Character, Set<Character>> graph = new HashMap<>();
            Map<Character, Integer> indegree = new HashMap<>();
            boolean ret = populateGraph(words, graph, indegree);
            if(ret != true) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            int visited = 0;
            LinkedList<Character> queue = new LinkedList<>();
            for(char ch : indegree.keySet()) {
                if(indegree.get(ch) == 0) {
                    queue.offer(ch);
                    visited++;
                    sb.append(ch);
                }
            }
            while(!queue.isEmpty()) {
                char ch = queue.poll();
                for(char next : graph.get(ch)) {

                    indegree.put(next, indegree.get(next) - 1);
                    if(indegree.get(next) == 0) {
                        visited++;
                        queue.offer(next);
                        sb.append(next);
                    }
                }
            }
            //拓扑排序的闭环检测是通过判断节点访问的个数， 孤岛默认被算入到入度为0的启示节点
            //dfs的闭环检测是通过访问节点判断，孤岛通过判断节点访问的个数；
            if(visited != graph.size()) {
                return "";
            }
            return sb.toString();
        }

        boolean populateGraph(String[] words, Map<Character, Set<Character>> graph, Map<Character, Integer> indegree) {
            for(String word : words) {
                for(int i = 0; i < word.length(); ++i) {
                    graph.put(word.charAt(i), new HashSet<>());
                    indegree.put(word.charAt(i), 0);
                }
            }

            for(int i = 1; i < words.length; ++i) {
                String w1 = words[i - 1];
                String w2 = words[i];
                int len = Math.min(w1.length(), w2.length());
                int j = 0;
                for(; j < len; ++j) {
                    char from = w1.charAt(j), to = w2.charAt(j);
                    if(from != to) {
                        //判断重复边，图可以不判断，但是有向图必须判断，否则入度计算有问题
                        if(!graph.get(from).contains(to)) {
                            graph.get(from).add(to);
                            indegree.put(to, indegree.get(to) + 1);
                        }
                        break;
                    }
                }

                //字典中的数据本身错误，导致无解
                if(j >= len && w1.length() > len) {
                    return false;
                }
            }
            return true;
        }
    }
}


class L928_Minimiza_Malware_Spread_II {
    public int minMalwareSpread(int[][] graph, int[] initial) {
        Map<Integer, Set<Integer>> graphMap = new HashMap<>();
        populateGraph(graph, graphMap);

        int minScope = Integer.MAX_VALUE, result = -1;
        int count = 0;
        for(int i : initial) {
            count = bfs(graphMap, i, initial);
            if(count < minScope) {
                minScope = count;
                result = i;
            } else if(count == minScope) {
                result = Math.min(result, i);
            }
        }
        return result;
    }

    void populateGraph(int[][] graph, Map<Integer, Set<Integer>> map) {
        int len = graph.length;
        for(int i = 0; i < len; ++i) {
            map.put(i, new HashSet<Integer>());
        }

        for(int i = 0; i < len; ++i) {
            for(int j = i + 1; j < len; ++j) {
                if(graph[i][j] == 1) {
                    map.get(i).add(j);
                    map.get(j).add(i);
                }
            }
        }
    }

    int bfs(Map<Integer, Set<Integer>> graph, int excludeNode, int[] initial) {
        LinkedList<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        visited.add(excludeNode);
        for(int i : initial) {
            if(i == excludeNode) {
                continue;
            }
            queue.offer(i);
            visited.add(i);
        }

        int count = 0;
        while(!queue.isEmpty()){
            int cur = queue.poll();
            count++;
            for(int next : graph.get(cur)) {
                if(visited.contains(next)) {
                    continue;
                }
                queue.offer(next);
                visited.add(next);
            }
        }
        return count;
    }
}





