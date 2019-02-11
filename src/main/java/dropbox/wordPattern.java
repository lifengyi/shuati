package dropbox;

import java.util.*;

public class wordPattern {

    public boolean wordPattern_I(String pattern, String str) {
        if(pattern == null || str == null) {
            return false;
        }

        String[] strings = str.split(" ");
        if(strings.length != pattern.length()) {
            return false;
        }

        Map<String, Integer> m1 = new HashMap<>();
        int[] m2 = new int[256];

        for(int i = 0; i < pattern.length(); ++i) {
            String key1 = strings[i];
            int index2 = pattern.charAt(i);
            if(!m1.containsKey(key1) && m2[index2] == 0) {
                m1.put(key1, i + 1);
                m2[index2] = i + 1;
            } else if(!m1.containsKey(key1) || m2[index2] == 0 || m1.get(key1) != m2[index2]) {
                return false;
            }
        }

        return true;
    }


    public boolean wordPatternMatch_II(String pattern, String str) {
        Map<Character, String> cache = new HashMap<>();
        Set<String> visited = new HashSet<>();
        return dfs(pattern, 0, str, 0, cache, visited);
    }

    boolean dfs(String pattern, int idx1, String str, int idx2, Map<Character, String> cache, Set<String> visited) {
        if(idx1 == pattern.length() && idx2 == str.length()) {
            return true;
        } else if(idx1 == pattern.length() || idx2 == str.length()) {
            return false;
        }

        char ch = pattern.charAt(idx1);
        if(cache.containsKey(ch)) {
            String matchedString = cache.get(ch);
            if(str.startsWith(matchedString, idx2)) {
                return dfs(pattern, idx1 + 1, str, idx2 + matchedString.length(), cache, visited);
            } else {
                return false;
            }
        }

        for(int i = idx2; i < str.length(); ++i) {
            String toBeMatched = str.substring(idx2, i + 1);
            if(visited.contains(toBeMatched)) {
                continue;
            }

            visited.add(toBeMatched);
            cache.put(ch, toBeMatched);
            if(dfs(pattern, idx1 + 1, str, idx2 + toBeMatched.length(), cache, visited)) {
                return true;
            }
            cache.remove(ch);
            visited.remove(toBeMatched);
        }

        return false;
    }


}
