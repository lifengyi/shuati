package com.stevenli.interview.easy.interview;

import java.util.*;

public class Algorithm_trie_tree {

}

class L208_Trie {
    class TrieNode {
        public TrieNode[] children;
        public boolean hasWord;

        public TrieNode() {
            hasWord = false;
            children = new TrieNode[26];
            for(int i = 0; i < 26; ++i) {
                children[i] = null;
            }
        }

    }

    TrieNode root;

    /** Initialize your data structure here. */
    public L208_Trie() {
        root = new TrieNode();
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        TrieNode node = root;
        char[] array = word.toCharArray();
        int index = 0;
        for(int i = 0; i < array.length; ++i) {
            index = array[i] - 'a';
            if(node.children[index] != null) {
                node = node.children[index];
            } else {
                node.children[index] = new TrieNode();
                node = node.children[index];
            }
        }
        node.hasWord = true;
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        TrieNode node = root;
        char[] array = word.toCharArray();
        int  index = 0;
        for(int i = 0; i < array.length; ++i) {
            index = array[i] - 'a';
            if(node.children[index] == null) {
                return false;
            } else {
                node = node.children[index];
            }
        }

        return node.hasWord;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        char[] array = prefix.toCharArray();
        int  index = 0;
        for(int i = 0; i < array.length; ++i) {
            index = array[i] - 'a';
            if(node.children[index] == null) {
                return false;
            } else {
                node = node.children[index];
            }
        }

        return true;
    }
}


class L211_Add_and_Search_Word {
    class TrieNode {
        public TrieNode[] children;
        public boolean hasWord;

        public TrieNode() {
            hasWord = false;
            children = new TrieNode[26];
            for(int i = 0; i < 26; ++i) {
                children[i] = null;
            }
        }
    }

    TrieNode root;

    /** Initialize your data structure here. */
    public L211_Add_and_Search_Word() {
        root = new TrieNode();
    }

    /** Adds a word into the data structure. */
    public void addWord(String word) {
        TrieNode node = root;
        char[] array = word.toCharArray();
        int index = 0;
        for(int i = 0; i < array.length; ++i) {
            index = array[i] - 'a';
            if(node.children[index] != null) {
                node = node.children[index];
            } else{
                node.children[index] = new TrieNode();
                node = node.children[index];
            }
        }
        node.hasWord = true;
    }

    /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
    public boolean search(String word) {
        if(word == null || word.length() == 0) {
            return true;
        }

        return search(word, 0, root);
    }

    private boolean search(String word, int index, TrieNode node) {
        if(index == word.length()) {
            return node.hasWord;
        }

        char c = word.charAt(index);
        if(c == '.') {
            for(int i = 0; i < 26; ++i) {
                if(node.children[i] != null && search(word, index + 1, node.children[i])) {
                    return true;
                }
            }
        } else if (node.children[c - 'a'] != null){
            return search(word, index + 1, node.children[c - 'a']);
        }

        return false;
    }
}


/**
 * 使用flag矩阵标志曾经搜索过的cell，不好之处：
 * 1. 额外开辟空间
 * 2. 在exist逻辑处理函数中，是可以要注意set/reset
 *    无论成功和失败都需要reset，导致代码后期扩展可能会出错
 *
 * 替换方案：当前矩阵字符和字符串当前索引字符匹配时，将矩阵单元
 * 字符替换上一个不可能出现的字符，然后当返回的时候在恢复。同样
 * 需要set/reset，可以改进代码只需要开始set，和最后统一reset
 */
class L79_Word_Search {
    public boolean exist(char[][] board, String word) {
        if(board == null || board.length == 0 || board[0].length == 0
                || word == null || word.length() == 0) {
            return false;
        }

        int[][] flags = new int[board.length][board[0].length];
        for(int i = 0; i < board.length; ++i) {
            for(int j = 0; j < board[0].length; ++j) {
                if(exist(board, i, j, word, 0, flags)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean exist(char[][] board, int x, int y, String word, int index, int[][] flags) {
        if(flags[x][y] == 1 || board[x][y] != word.charAt(index)) {
            return false;
        }

        if(index == word.length() - 1) {
            return true;
        }

        flags[x][y] = 1;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for(int i = 0; i < 4; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if(validate(board, nx, ny) && flags[nx][ny] == 0) {
                if(exist(board, nx, ny, word, index + 1, flags)) {
                    flags[x][y] = 0;
                    return true;
                }
            }
        }

        flags[x][y] = 0;
        return false;
    }

    private boolean validate(char[][] board, int x, int y) {
        if(x >= 0 && x < board.length && y >=0 && y < board[0].length) {
            return true;
        }
        return false;
    }
}


/**
 * 1. 在原来矩阵上用#来覆盖原有值，避免在意使用这个单元
 * 同时在使用反向数组遍历周边4各节点时，需要避开'#'单元
 *
 * 2. dfs递归函数定义为当前board上的节点，TrieTree上
 * 的父节点，判断父节点上是否能存在和当前board节点相同的
 * 子节点，不同返回。 如果相同，继续判断是否已经是一个word
 * 的结尾，是则放入结果集；继续处理周边4个节点。
 *
 * 3. DFS特点: 将结果集带入函数，在递归函数的最尾部判断，
 * 如果结果满足，则放入结果集；整个递归过程中，只是做dfs
 * 遍历所有可能节点，可能需要处理临时结果集中数据的放入和
 * 移出，但不做任何检查。
 *
 */
class L212_Word_Search_II {
    public List<String> findWords(char[][] board, String[] words) {
        List<String> ret = new ArrayList<>();
        if(board == null || board.length == 0 || board[0].length == 0
                || words == null || words.length == 0) {
            return ret;
        }

        TrieTree tree = new TrieTree();
        for(String word : words) {
            tree.add(word);
        }

        for(int i = 0; i < board.length; ++i) {
            for(int j = 0; j < board[0].length; ++j) {
                search(board, i, j, tree.root, ret);
            }
        }

        return ret;
    }

    private boolean validate(char[][] board, int x, int y) {
        if(x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] == '#'){
            return false;
        }
        return true;
    }

    private void search(char[][] board, int x, int y, TrieNode node, List<String> result) {
        //check if match
        char temp = board[x][y];
        if(node.children[temp - 'a'] == null) {
            return;
        }

        //check if it's a end of word
        node = node.children[temp - 'a'];
        if(node.hasWord) {
            if(!result.contains(node.value)) {
                result.add(node.value);
            }
        }

        //process if there's more children
        board[x][y] = '#';
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for(int i = 0; i < 4; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if(validate(board, nx, ny)) {
                search(board, nx, ny, node, result);
            }
        }

        board[x][y] = temp;
        return;
    }

    class TrieTree {
        TrieNode root;

        public TrieTree() {
            root = new TrieNode();
        }

        public void add(String word) {
            char[] array = word.toCharArray();
            TrieNode node = root;
            int index = 0;
            for(int i = 0; i < array.length; ++i) {
                index = array[i] - 'a';
                if(node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.hasWord = true;
            node.value = word;
        }
    }
    class TrieNode {
        public TrieNode[] children;
        public boolean hasWord;
        public String value;

        public TrieNode() {
            hasWord = false;
            children = new TrieNode[26];
            for(int i = 0; i < 26; ++i) {
                children[i] = null;
            }
        }
    }
}


/**
 * 出现的问题：
 *   1. 总体思路： DFS + Trie Tree pruning
 *
 *      程序主题使用DFS递归函数，递归主要用于遍历
 *      所有节点，并通过TrieTree实现剪枝
 *
 *      递归函数中带入临时结果集和最终结果集，递归
 *      中间不做任何处理，到递归尾部判断临时结果集
 *      是否满足，满足即放入最终结果集
 *
 *   2. TireTree/TrieNode的改进：
 *      中间节点引入缓存，放置该前缀下所有出现的字符串集合
 *
 *   3. TireTree在创建的时候，add字符串时需要注意规避空串
 *      同时遍历字符串引入square时也要注意规避
 *      当然，题意上也说明了word长度至少为1
 *
 *   4. 临时结果集和最终结果集都是List，所以临时结果集在
 *      递归过程中动态加减内部元素，会影响最终结果集，故
 *      临时结果集放入最终结果集时，需要重新new对象
 */
class L425_Word_Square {
    public List<List<String>> wordSquares(String[] words) {
        List<List<String>> ret = new ArrayList<>();
        if(words == null || words.length == 0) {
            return ret;
        }

        TrieTree tree = new TrieTree();
        for(String word : words) {
            if(word.length() > 0) {
                tree.add(word);
            }
        }

        List<String> wordSquare = new ArrayList<>();
        for(String word : words) {
            if(word.length() > 0) {
                wordSquare.add(word);
                search(tree, wordSquare, word.length(), ret);
                wordSquare.remove(wordSquare.size() - 1);
            }
        }

        return ret;
    }

    private String getPrefix(List<String> wordSquare) {
        int index = wordSquare.size();
        StringBuilder sb = new StringBuilder();
        for(String word : wordSquare) {
            sb.append(word.charAt(index));
        }
        return sb.toString();
    }

    private void search(TrieTree tree, List<String> wordSquare, int n, List<List<String>> result) {
        if(wordSquare.size() == n) {
            result.add(new ArrayList<>(wordSquare));
            return;
        }

        String prefix = getPrefix(wordSquare);
        List<String> wordList = tree.startWith(prefix);
        if(wordList == null) {
            return;
        }

        for(String word : wordList) {
            if(word.length() == n) {
                wordSquare.add(word);
                search(tree, wordSquare, n, result);
                wordSquare.remove(wordSquare.size() - 1);
            }
        }
    }

    class TrieTree {
        TrieNode root;

        public TrieTree() {
            root = new TrieNode();
        }

        public void add(String word) {
            char[] array = word.toCharArray();
            TrieNode node = root;
            int index = 0;
            for(int i = 0; i < array.length; ++i) {
                index = array[i] - 'a';
                if(node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
                node.wordList.add(word);
            }
            node.hasWord = true;
        }

        public List<String> startWith(String prefix) {
            char[] array = prefix.toCharArray();
            TrieNode node = root;
            int index = 0;
            for(int i = 0; i < array.length; ++i) {
                index = array[i] - 'a';
                if(node.children[index] == null) {
                    return null;
                }
                node = node.children[index];
            }

            return node.wordList;
        }
    }

    class TrieNode {
        public boolean hasWord;
        public TrieNode[] children;
        public List<String> wordList;

        public TrieNode() {
            hasWord = false;
            wordList = new ArrayList<>();
            children = new TrieNode[26];
            for(int i = 0; i < 26; ++i) {
                children[i] = null;
            }
        }
    }
}



