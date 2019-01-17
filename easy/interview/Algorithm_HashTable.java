package interview;

import java.util.*;

public class Algorithm_HashTable {
}


class L249_Group_Shifted_Strings {
    public List<List<String>> groupStrings(String[] strings) {
        Map<String, List<String>> map = new HashMap<>();
        for(String string : strings) {
            String basic = getBasicString(string);
            if(!map.containsKey(basic)) {
                List<String> list = new ArrayList<>();
                list.add(string);
                map.put(basic, list);
            } else {
                map.get(basic).add(string);
            }
        }

        List<List<String>> ret = new ArrayList<>();
        for(String key : map.keySet()) {
            ret.add(map.get(key));
        }
        return ret;
    }

    private String getBasicString(String str) {
        StringBuilder sb = new StringBuilder();
        int diff = str.charAt(0) - 'a';
        for(int i = 0; i < str.length(); ++i) {
            char ch = (char)(str.charAt(i) - diff);
            if(ch < 'a') {
                ch = (char)(ch + 26);
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}


class L49_Group_Anagrams {
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> result = new ArrayList<>();
        if(strs == null || strs.length == 0) {
            return result;
        }

        Map<String, List<String>> map = new HashMap<>();
        String orderedString = null;
        for(String str : strs) {
            char[] array = str.toCharArray();
            Arrays.sort(array);
            orderedString = new String(array);
            if(map.containsKey(orderedString)) {
                map.get(orderedString).add(str);
            } else {
                List<String> list = new ArrayList<>();
                list.add(str);
                map.put(orderedString, list);
            }
        }

        for(String key : map.keySet()) {
            result.add(map.get(key));
        }
        return result;
    }
}


class L734_Sentence_Similarity {
    public boolean areSentencesSimilar(String[] words1, String[] words2, String[][] pairs) {
        if(words1 == null && words2 == null) {
            return true;
        } else if(words1 == null || words2 == null
                || words1.length != words2.length) {
            return false;
        }

        Map<String, Set<String>> map = new HashMap<>();
        for(String[] pair : pairs) {
            if(!map.containsKey(pair[0])) {
                map.put(pair[0], new HashSet<>());
            }
            map.get(pair[0]).add(pair[1]);

            if(!map.containsKey(pair[1])) {
                map.put(pair[1], new HashSet<>());
            }
            map.get(pair[1]).add(pair[0]);
        }
        String s1 = null, s2 = null;
        for(int i = 0; i < words1.length; ++i) {
            s1 = words1[i];
            s2 = words2[i];
            if(s1.equalsIgnoreCase(s2)) {
                continue;
            }
            if(!map.containsKey(s1) || !map.containsKey(s2)
                    || !map.get(s1).contains(s2)) {
                return false;
            }
        }
        return true;
    }
}