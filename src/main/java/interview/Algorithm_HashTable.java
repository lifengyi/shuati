package interview;

import java.util.*;

/**
 *  Implementation of Rolling Hash is based on Rabin Fingerprint
 *
 *  Rabin Karp uses a rolling hash to quickly filter out positions
 *  of the text that cannot match the pattern, and then checks for
 *  a match at the remaining positions.
 *
 *  Rabin Karp uses following approach to implement O(m + n) instead of O(m * n)
 *  1. rolling hash:  roll the "window" in source string
 *  2. hash the target and pattern string, compare integer value instead of string value
 *
 *  If the string is too long, the final hash value may have overflow issue.
 *  We have to do MOD and the mod value should be larger enough to reduce the conflict
 *  It is better to use prime number.
 *
 *  12/06/2023
 */

public class Algorithm_HashTable {
}


class L28_Find_the_index_of_the_first_occurrence_in_a_string {
    public int strStr(String haystack, String needle) {
        int BASE = 31, MOD = 10001;

        if (haystack == null || needle == null || needle.length() > haystack.length()) {
            return -1;
        }

        int m = needle.length();
        if (m == 0) {
            return -1;
        }

        // i
        // abcde
        int power = 1;
        for (int i = 0; i < m; ++i) {
            power = (power * BASE) % MOD;
        }

        // abc
        int targetHashCode = 0;
        for (int i = 0; i < m; ++i) {  // m but not m - 1, 因为移位后，成为最高位的时候，就是有m，而不是m-1
            targetHashCode = (targetHashCode * BASE + needle.charAt(i)) % MOD;
        }

        int hashCode = 0;
        for (int i = 0; i < haystack.length(); ++i) {
            hashCode = (hashCode * BASE + haystack.charAt(i)) % MOD;
            if (i < m - 1) {
                continue;
            }

            // m = 3
            //    i
            // abcde
            if (i >= m) {
                hashCode = hashCode - (haystack.charAt(i - m) * power) % MOD; // 减去最高位的值需要先MOD
                // Add mod if hash code is less than 0 instead of moding above hashCode
                if (hashCode < 0) {
                    hashCode += MOD;
                }
            }

            // m = 3
            //   i
            // abcde
            if (hashCode == targetHashCode && haystack.substring(i - m + 1, i + 1).equals(needle)) {
                return i - m + 1;
            }
        }

        return -1;
    }
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

class L609_Find_Duplicate_File_in_System {
    public List<List<String>> findDuplicate(String[] paths) {
        List<List<String>> result = new ArrayList<>();

        Map<String, Set<String>> map = new HashMap<>();
        for(String path : paths) {
            String[] elements = path.split(" ");
            String curPath = elements[0];
            for(int i = 1; i < elements.length; ++i) {
                int indexOfBracket = elements[i].indexOf('(');
                String fileName = elements[i].substring(0, indexOfBracket);
                String content = elements[i].substring(indexOfBracket + 1,
                        elements[i].length() - 1);
                if(!map.containsKey(content)) {
                    map.put(content, new HashSet<>());
                }
                String fullPath = curPath + "/" + fileName;
                map.get(content).add(fullPath);
            }
        }

        for(String content : map.keySet()) {
            if(map.get(content).size() != 1) {
                List<String> list = new ArrayList<>();
                list.addAll(map.get(content));
                result.add(list);
            }
        }

        return result;
    }
}


class L811_Subdomain_Visit_Count {
    public List<String> subdomainVisits(String[] cpdomains) {
        List<String> result = new ArrayList<>();
        if(cpdomains == null || cpdomains.length == 0) {
            return result;
        }

        Map<String, Integer> cache = new HashMap<>();
        for(String cpdomain : cpdomains) {
            String[] items = cpdomain.split(" ");
            int count = Integer.parseInt(items[0]);
            String domain = items[1];

            List<String> allSubDomains = getAllSubdomains(domain);
            for(String subdomain : allSubDomains) {
                if(cache.containsKey(subdomain)) {
                    cache.put(subdomain, cache.get(subdomain) + count);
                } else {
                    cache.put(subdomain, count);
                }
            }
        }
        for(String key : cache.keySet()) {
            String pair = cache.get(key) + " " + key;
            result.add(pair);
        }
        return result;
    }

    List<String> getAllSubdomains(String domain) {
        List<String> result = new ArrayList<>();
        result.add(domain);

        int index = -1;
        while((index = domain.indexOf(".")) != -1) {
            domain = domain.substring(index + 1, domain.length());
            result.add(domain);
        }
        return result;
    }
}


/**
 * 这道题目其实就是一个 Rolling Hash的应用场景
 * 多用于巨型字符串匹配问题，也用于查找重复文件
 *
 * 理解 Rolling Hash的原理！
 */
class L187_Repeated_DNA_Sequences {
    public List<String> findRepeatedDnaSequences(String s) {
        int dnaLength = 10;
        if(s == null || s.length() < dnaLength) {
            return new ArrayList<>();
        }

        Set<String> seenStrings = new HashSet<>();
        Set<String> dnaSeq = new HashSet<>();
        String subString = null;
        for(int i = 0; i < s.length(); ++i) {
            if(i < dnaLength - 1) {
                continue;
            }

            //   i
            // abcde
            subString = s.substring(i - dnaLength + 1, i + 1);
            if(seenStrings.contains(subString)) {
                dnaSeq.add(subString);
            } else {
                seenStrings.add(subString);
            }
        }

        return new ArrayList<>(dnaSeq);
    }
}