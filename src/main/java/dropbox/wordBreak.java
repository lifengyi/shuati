package dropbox;

import java.util.*;

public class wordBreak {

    /**
     *  可以求解是否有匹配，同时也可以求解匹配的个数
     *
     *  注意各种前提条件： wordDict中是否有重复数据，是否有空字符串等
     */
    public boolean wordBreak_I(String s, List<String> wordDict) {
        int len = s.length();
        int[] dp = new int[len + 1];
        dp[0] = 1;

        for(int i = 0; i < len; ++i) {
            if(dp[i] == 0) {
                continue;
            }

            for(String word : wordDict) {
                if(s.startsWith(word, i)) {
                    dp[i + word.length()] += dp[i];
                }
            }
        }

        return dp[len] > 0;
    }

    public List<String> wordBreak_II(String s, List<String> wordDict) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<String> results = new LinkedList<>();
        List<String> candidate = new LinkedList<>();
        dfs(s, 0, wordDict, map, candidate, results);
        return results;
    }

    boolean dfs(String s, int startIndex, List<String> wordDict, Map<Integer, List<Integer>> map,
                List<String> candidate, List<String> results) {
        // reach the tail
        if(startIndex == s.length()) {
            save(candidate, results);
            return true;
        }

        // This index was accessed before
        if(map.containsKey(startIndex)) {
            if(map.get(startIndex).size() == 0) {
                return false;
            } else {
                save(s, startIndex, map, candidate, results);
                return true;
            }
        }

        //first time to access this index
        boolean isMatched = false;
        map.put(startIndex, new LinkedList<>());
        for(String word : wordDict) {
            if(s.startsWith(word, startIndex)) {
                int nextPos = startIndex + word.length();

                candidate.add(word);
                if(dfs(s, nextPos, wordDict, map, candidate, results)) {
                    map.get(startIndex).add(nextPos);
                    isMatched = true;
                }
                candidate.remove(candidate.size() - 1);
            }
        }

        return isMatched;
    }

    void save(String s, int startIndex, Map<Integer, List<Integer>> map, List<String> candidate, List<String> results) {
        if(startIndex == s.length()) {
            save(candidate, results);
            return;
        }

        for(int nextIndex : map.get(startIndex)) {
            candidate.add(s.substring(startIndex, nextIndex));
            save(s, nextIndex, map, candidate, results);
            candidate.remove(candidate.size() - 1);
        }
    }

    void save(List<String> candidate, List<String> results) {
        StringBuilder sb = new StringBuilder();
        for(String element : candidate) {
            sb.append(element).append(" ");
        }
        results.add(sb.toString().trim());
    }


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
