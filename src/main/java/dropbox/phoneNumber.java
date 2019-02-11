package dropbox;

import java.util.*;

/**
 *  注意 0 和 1作为空串的处理，是否需要跳过不处理
 */

public class phoneNumber {

    class TrieNode {
        char ch = 0;
        TrieNode[] child = null;
        boolean isWord = false;

        public TrieNode(char ch) {
            this.ch = ch;
            this.child = new TrieNode[26];
        }
    }

    TrieNode root = null;

    String[] map = {"", "", "abc", "def",
                    "ghi", "jkl", "mno",
                    "pqrs", "tuv", "wxyz"};

    public phoneNumber() {
        root = new TrieNode(' ');
    }

    private List<String> findAllWords(String digits, String[] dict) {
        buildTrieTree(dict);
        String[] matchedStrings = getMatchedStrings(digits);

        List<String> result = new ArrayList<>();
        String candidate = "";
        dfs(matchedStrings, 0, candidate, result);

        return result;
    }

    /**
     *
     * 问题1：
     * 给定一个数字字符串， 数字字符串根据手机电话号码分布，可以组成不同的字符串，
     * 同时给定一个字典，要求输出数字字符串所能组成的所有字符串，且输出字符串要存在于字典中；
     *
     * 1. 根据数字得到可能组成字符串的字符串数组
     * 2. DFS所有字符串数组，求得最终字符串
     * 3. 匹配最终字符串是否在字典里
     *
     *
     * 几个相关问题：
     * a. 字典存储：  哈希表， Trie树
     *    内存空间上的不同
     *
     * b. DFS组成最终字符串数组，如何优化DFS？
     *    需要字典存储进行支持, DFS过程中，对当前生成的字符串，
     *    即最终字符串的前缀，进行validdatePreix,Trie树是支持检测字符串前缀的；
     *
     * c. 最终字符串和字典进行匹配
     *
     *
     *
     * ************* 小follow up
     *   如果最终生成的字符串很长，非常长，但是不至于stack overflow，
     *   例如["AAAAAAAAAAAAAA", "BAAAAAAAAAAAAA"] 怎么处理？
     *
     *   注意：这个问题不是说字典很大，字典可以哈希表或者trie tree，
     *        如果字典大可以用trie tree优化空间
     *
     *   但是这个问题问的是最终字符串很大，怎么办？
     *   除了上述的validatePrefix优化之外，
     *   还可以缓存当前匹配中的数据类似于word break中的搜索+记忆化
     *
     *
     * *************   上述文图的follow up
     *
     * 加入数字字符串是7位数，字典中的字符串都是长度为3和4的
     * 如何判断数字字符串转换成的字符串能分解成字典中的字符串？
     *
     * 相当于在上述题目中，匹配字符串到字典的过程，有点类似 word break
     *
     * 和Word break不同的是，word break是给定数据串
     * 对已有字符串进行DFS遍历的同时，对分割的数据进行缓存，从而实现pruning
     *
     * 而本例中数据串是在动态生成的，所以没必要等到数据生成完毕之后再做Word break
     * 方法就是DFS过程中当数据大于3/4的时候，就进行 validatePrefix
     *
     *
     *      *  follow up:是给一个7位数字，字典里的词最小长度是3, 输出3+4, 4+3, 7个字母组成的valid词
     *      *  针对matchedStrings分成2中情况讨论， 3 + 4 和 4 + 3
     *      *
     *      *  List<String> ret1 = findAllMatchedWords(String[] matchedString, 0, 4);
     *      *  List<String> ret2 = findAllMatchedWords(String[] matchedString, 0, 3);
     *      *  return ret1 + ret2
     *      *
     *      *  List<String> findAllMathcedWords(String[] matchedString, int start, int end) {
     *      *      List<String> ret1, ret2;
     *      *      curIndex = start;
     *      *      dfs(matchedString, end, curIndex, candidate, ret1);
     *      *
     *      *      curIndex = end;
     *      *      dfs(matchedString, matchedString.length(), curIndex, candidate, ret2);
     *      *
     *      *      return ret1 * ret2;
     *      *  }
     */

    void dfs(String[] strings, int mainIndex, String candidate, List<String> result) {
        if(mainIndex == strings.length) {
            if(isWord(candidate)) {
                result.add(candidate);
            }
            return;
        }

        String curString = strings[mainIndex];
        if(curString.length() == 0) {
            //skip "1" and "0",  the empty string
            dfs(strings, mainIndex + 1, candidate, result);
        } else {
            for(int i = 0; i < curString.length(); ++i) {
                dfs(strings, mainIndex + 1, candidate + curString.charAt(i), result);
            }
        }

    }

    boolean isWord(String str) {
        TrieNode node = root;
        for(int i = 0; i < str.length(); ++i) {
            int index = str.charAt(i) - 'a';
            if(node.child[index] == null) {
                return false;
            }
            node = node.child[index];
        }
        return node.isWord;
    }

    String[] getMatchedStrings(String digits) {
        String[] result = new String[digits.length()];
        for(int i = 0; i < digits.length(); ++i) {
            int index = digits.charAt(i) - '0';
            result[i] = map[index];
        }
        return result;
    }


    void buildTrieTree(String[] dict) {
        for(String word : dict) {
            add(word);
        }
    }

    void add(String word) {
        TrieNode node = root;
        for(int i = 0; i < word.length(); ++i){
            char ch = word.charAt(i);
            int index = ch - 'a';
            if(node.child[index] == null) {
                node.child[index] = new TrieNode(ch);
            }
            node = node.child[index];
        }
        node.isWord = true;
    }


    public static void main(String[] args) {
        String[] dict = {"cat", "dog", "hen", "fni", "fxq", "bcde", "caaaa", "doogg"};
        String digits = "139710";

        phoneNumber proc = new phoneNumber();
        List<String> strings = proc.findAllWords(digits, dict);
        for(String string : strings) {
            System.out.println(string);
        }
    }
}
