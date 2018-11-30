package com.stevenli.interview.easy.interview;

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