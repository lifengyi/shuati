package com.stevenli.interview.easy.interview;

import java.util.*;

public class LeetCode_String {

    /**
     * 224. Basic Calculator
     *
     * @param s
     * @return
     */
    public int L224_alculate(String s) {
        if(s == null || s.length() == 0)
            return 0;

        LinkedList<Integer> stack = new LinkedList<>();
        int sign = 1, ret = 0;
        char[] array = s.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            if(array[i] >= '0' && array[i] <= '9') {
                int number = 0;
                while(i < array.length && array[i] >= '0' && array[i] <= '9') {
                    number = number * 10 + (array[i] - '0');
                    ++i;
                }
                ret += sign * number;
                --i;
            } else if(array[i] == '+'){
                sign = 1;
            } else if(array[i] == '-') {
                sign = -1;
            } else if(array[i] == '(') {
                stack.push(ret);
                stack.push(sign);
                ret = 0;
                sign = 1;
            } else if(array[i] == ')') {
                sign = stack.pop();
                ret = stack.pop() + sign * ret;
            }
        }

        return ret;
    }

    public int L224_calculate_v2(String s) {
        if(s == null | s.length() == 0)
            return 0;

        LinkedList<Integer> stack = new LinkedList<>();

        //state: 0, ready to get any input; 1, counting numbers
        int state = 0, sign = 1;
        int start = 0, ret = 0;
        char[] array = s.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            if(array[i] >= '0' && array[i] <= '9') {
                if(state == 0) {
                    state = 1;
                    start = i;
                }
            } else if (array[i] == '(') {
                stack.push(ret);
                stack.push(sign);
                ret = 0;
                sign = 1;
            } else if(array[i] == ')') {
                if(state == 1) {
                    ret += sign * Integer.valueOf(s.substring(start, i));
                    state = 0;
                }
                sign = stack.pop();
                ret = stack.pop() + sign * ret;
            }else if (array[i] == '+' || array[i] == '-' || array[i] == ' ') {
                if(state == 1) {
                    ret += sign * Integer.valueOf(s.substring(start, i));
                    state = 0;
                }
                if(array[i] == '+') sign = 1;
                if(array[i] == '-') sign = -1;
            }
        }

        if(state == 1) {
            ret += sign * Integer.valueOf(s.substring(start, array.length));
            state = 0;
        }

        return ret;
    }

    /**
     * 91. Decode Ways
     *
     * @param s
     * @return
     */
    public int L91_numDecodings(String s) {
        char[] array = s.toCharArray();
        int fn_1=2, fn_2=1, fn = 0;
        if(array[0] == '0')
            return 0;
        if(array.length >= 2) {
            if(array[1] == '0')
                fn_1 -= 1;
            if(array[0] > '2' || (array[0] == '2' && array[1] > '6'))
                fn_1 -= 1;
        }

        if(array.length == 1)
            return fn_2;
        if(array.length == 2)
            return fn_1;
        for(int i = 2; i < array.length; ++i){
            if(array[i] == '0')
                fn_1 = 0;
            if(array[i-1] == '0' || array[i-1] > '2' || (array[i-1] == '2' && array[i] > '6'))
                fn_2 = 0;
            fn = fn_1 + fn_2;
            fn_2 = fn_1;
            fn_1 = fn;
        }
        return fn;
    }

    /**
     * 17. Letter Combinations of a Phone Number
     *
     * @param digits
     * @return
     */
    public List<String> L17_letterCombinations(String digits) {
        List<String> list = new ArrayList<>();
        if(digits == null || digits.length() == 0)
            return list;

        String[] letterCache = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        list.add("");
        for(int i = 0; i < digits.length(); ++i) {
            String letters = letterCache[digits.charAt(i)-'0'];
            list = letterComb(letters, list);
        }
        return list;
    }

    private List<String> letterComb(String letters, List<String> list) {
        List<String>  ret = new ArrayList<>();
        for(String element : list) {
            for(int i = 0; i < letters.length(); ++i) {
                ret.add(element + letters.charAt(i));
            }
        }
        return ret;
    }

    /**
     * 8. String to Integer (atoi)
     */
    enum Type {BEGIN, SIGN_FOUND, NUM_FOUND};
    public int L8_myAtoi(String str) {
        int start=0, end=0, flag=1, i=0;
        Type myType = Type.BEGIN;
        char[] array = str.toCharArray();
        for(; i < array.length; ++i) {
            if(array[i] == ' '){
                if(myType.equals(Type.BEGIN)) {
                    //continue
                } else if(myType.equals(Type.SIGN_FOUND)) {
                    break;
                } else {
                    end = i;
                    break;
                }
            } else if(array[i] >= '0' && array[i] <= '9') {
                if(!myType.equals(Type.NUM_FOUND)){
                    myType = Type.NUM_FOUND;
                    start = i;
                }
            } else if(array[i] == '+' || array[i] == '-'){
                if(myType.equals(Type.BEGIN)) {
                    myType = Type.SIGN_FOUND;
                    flag = array[i] == '+' ? 1 : -1;
                } else if(myType.equals(Type.SIGN_FOUND)) {
                    break;
                } else {
                    end = i;
                    break;
                }
            } else {
                if(myType.equals(Type.NUM_FOUND)) {
                    end = i;
                    break;
                } else {
                    break;
                }
            }
        }

        if(!myType.equals(Type.NUM_FOUND))
            return 0;
        if(end == 0)
            end = i;

        String numberString = str.substring(start, end);
        numberString = adjust(numberString);
        String maxString = String.valueOf(Integer.MAX_VALUE);
        String minString = String.valueOf(Integer.MIN_VALUE);
        minString = minString.substring(1, minString.length());
        String targetString = flag > 0 ? maxString : minString;
        if(validate(numberString, targetString)) {
            if(flag < 0) numberString = "-" + numberString;
            return Integer.valueOf(numberString);
        } else {
            return flag > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
    }

    private String adjust(String str) {
        int start = 0;
        while(start < str.length() && str.charAt(start) == '0')
            start++;

        return start < str.length() ? str.substring(start, str.length()) : "0";
    }

    private boolean validate(String src, String target) {
        if(src.length() > target.length())
            return false;
        else if(src.length() < target.length())
            return true;
        else {
            for(int i = 0; i < src.length(); ++i) {
                if(src.charAt(i) < target.charAt(i))
                    return true;
                else if(src.charAt(i) > target.charAt(i))
                    return false;

            }
            return true;
        }
    }

    /**
     * 387. First Unique Character in a String
     *
     * @param s
     * @return
     */
    public int L387_firstUniqChar(String s) {
        if(s == null || s.length() == 0)
            return -1;
        if(s.length() == 1)
            return 0;

        char[] array = s.toCharArray();
        int[] freq = new int[26];
        for(int i = 0; i < array.length; ++i) {
            freq[array[i] - 'a']++;
        }
        for(int i = 0; i < array.length; ++i) {
            if(freq[array[i] - 'a'] == 1)
                return i;
        }
        return -1;
    }

    /**
     * 38. Count and Say
     *
     * @param n
     * @return
     */
    public String L38_countAndSay(int n){
        String ret = "1";
        for(int i = 1; i < n; ++i){
            ret = countAndSay(ret);
        }
        return ret;
    }

    private String countAndSay(String n) {
        char[] chars = n.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < chars.length; ++i) {
            int count = 1;
            for(;i+1 < chars.length && chars[i] == chars[i+1]; ++i,++count){
            }
            sb.append(count).append(chars[i]);
        }
        return sb.toString();
    }

    public String L38_countAndSay_recur(int n) {
        if(n == 1)
            return "1";

        char[] chars = L38_countAndSay_recur(n-1).toCharArray();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < chars.length; ++i) {
            int count = 1;
            for(;i+1 < chars.length && chars[i] == chars[i+1]; ++i,++count){
            }
            sb.append(count).append(chars[i]);
        }
        return sb.toString();
    }

    /**
     * 28. Implement strStr()
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int L28_strStr(String haystack, String needle) {
        if(haystack == null || needle == null)
            return -1;
        if(haystack.length() == 0 && needle.length() == 0)
            return 0;
        if(haystack.length() == 0)
            return -1;
        if(needle.length() == 0)
            return 0;

        int ret = -1;
        char[] chOfNeedle = needle.toCharArray();
        char[] chOfHaystack = haystack.toCharArray();
        for(int i = 0; i < chOfHaystack.length; ++i){
            if(chOfHaystack[i] == chOfNeedle[0]) {
                int end = i + chOfNeedle.length;
                if(end > chOfHaystack.length)
                    break;
                int j = i;
                for(; j < end && chOfHaystack[j] == chOfNeedle[j-i]; ++j) {
                }
                if(j == end) {
                    ret = i;
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * 20. Valid Parentheses
     *
     * @param s
     * @return
     */

    public boolean L20_isValid(String s) {
        LinkedList<Character> stack = new LinkedList<>();
        for(char c : s.toCharArray()) {
            if(c == '(')
                stack.push(')');
            else if(c == '[')
                stack.push(']');
            else if(c == '{')
                stack.push('}');
            else
            if(stack.isEmpty() || stack.pop() != c)
                return false;
        }

        return stack.isEmpty();
    }


    public boolean L20_isValid_V2(String s) {
        LinkedList<Character> stack = new LinkedList<>();
        Map<Character, Character> map = new HashMap<>();
        map.put('(',')');
        map.put('[',']');
        map.put('{','}');
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; ++i){
            if(map.containsKey(chars[i])) {
                stack.push(chars[i]);
            }
            else {
                if(stack.isEmpty() || chars[i] != map.get(stack.pop()))
                    return false;
            }
        }

        if(!stack.isEmpty())
            return false;
        else
            return true;
    }

    /**
     * 14. Longest Common Prefix
     *
     * @param strs
     * @return
     */
    public String L14_longestCommonPrefix(String[] strs) {
        if(strs == null || strs.length == 0)
            return "";

        String prefix = strs[0];
        for(int i = 1; i < strs.length; ++i) {
            while(strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
        }

        return prefix;
    }


    public String[][] table = {{"I","V","X"},
            {"X","L","C"},
            {"C","D","M"},
            {"M", null, null}};

    /**
     * 12. Integer to Roman
     *
     * @param num
     * @return
     */
    public String L12_intToRoman(int num) {
        if(num > 3999)
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append(intToRoman((num/1000)%10, 3));
        sb.append(intToRoman((num/100)%10, 2));
        sb.append(intToRoman((num/10)%10, 1));
        sb.append(intToRoman(num%10, 0));
        return sb.toString();
    }

    private String intToRoman(int num, int pos) {
        StringBuilder sb = new StringBuilder();
        switch(num) {
            case 1:
                sb.append(table[pos][0]);
                break;
            case 2:
                sb.append(table[pos][0]);
                sb.append(table[pos][0]);
                break;
            case 3:
                sb.append(table[pos][0]);
                sb.append(table[pos][0]);
                sb.append(table[pos][0]);
                break;
            case 4:
                sb.append(table[pos][0]);
                sb.append(table[pos][1]);
                break;
            case 5:
                sb.append(table[pos][1]);
                break;
            case 6:
                sb.append(table[pos][1]);
                sb.append(table[pos][0]);
                break;
            case 7:
                sb.append(table[pos][1]);
                sb.append(table[pos][0]);
                sb.append(table[pos][0]);
                break;
            case 8:
                sb.append(table[pos][1]);
                sb.append(table[pos][0]);
                sb.append(table[pos][0]);
                sb.append(table[pos][0]);
                break;
            case 9:
                sb.append(table[pos][0]);
                sb.append(table[pos][2]);
                break;
            default:
                break;
        }
        return sb.toString();
    }

    /**
     * 13. Roman to Integer
     *
     * @param s
     * @return
     */
    public int L13_romanToInt(String s) {
        Map<Character, Integer> cache = new HashMap<>();
        cache.put('I', 1);
        cache.put('V',5);
        cache.put('X',10);
        cache.put('L',50);
        cache.put('C',100);
        cache.put('D',500);
        cache.put('M',1000);

        int num = 0, i = 0;
        while(i < s.length()){
            if(i+1 < s.length() && cache.get(s.charAt(i)) < cache.get(s.charAt(i+1))) {
                num -= cache.get(s.charAt(i++));
            } else {
                num += cache.get(s.charAt(i++));
            }
        }

        return num;
    }


    /**
     * 3. Longest Substring Without Repeating Characters
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int i = 0, j = 0, max = 0, count = 0;
        Set<Character> cache = new HashSet<>();
        while(j < s.length()) {
            if(!cache.contains(s.charAt(j))){
                cache.add(s.charAt(j++));
                count++;
                max = Math.max(max, count);
            } else {
                cache.remove(s.charAt(i++));
                count--;
            }
        }
        return max;
    }

}
