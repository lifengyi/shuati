package interview;

public class Problem_Palindrome {

}

class L5_LongestPalindromic_Substring {
    public int start  = 0;
    public int maxLen = 0;

    public String longestPalindrome(String s) {
        if(s == null || s.length() == 0) {
            return "";
        }

        char[] array = s.toCharArray();
        for(int i = 0; i < array.length; ++i) {
            validatePalindrome(array, i, i);
            validatePalindrome(array, i, i + 1);
        }

        return s.substring(start, start + maxLen);
    }

    void validatePalindrome(char[] array, int left, int right) {
        while(left >= 0 && right <= array.length - 1 && array[left] == array[right]) {
            left--;
            right++;
        }

        if(right - left - 1 > maxLen ) {
            maxLen = right - left - 1;
            start = left + 1;
        }
    }
}

class L5_LongestPalindrome_SubString_OptimizedByManacher {
    public String longestPalindrome(String s) {
        if(s == null || s.length() == 0) {
            return "";
        }

        char[] array = convert(s);
        int[] position = new int[array.length];

        int center = 0, right = 0;
        int resCenter = 0, resLen = 0;
        for(int i = 1; i < array.length; ++i) {
            if(i < right) {
                int mirror_i = 2 * center - i;
                position[i] = Math.min(right - i, position[mirror_i]);
            } else {
                position[i] = 1;
            }

            //expand right and compare the element
            while(i + position[i] < array.length && i - position[i] >= 0
                    && array[i + position[i]] == array[i - position[i]]) {
                position[i] += 1;
            }

            //check if we have a new "right"
            if(i + position[i] > right) {
                right = i + position[i];
                center = i;
            }
            if(position[i] > resLen) {
                resLen = position[i];
                resCenter = i;
            }
        }

        return s.substring((resCenter - resLen)/2, (resCenter - resLen)/2 + resLen - 1);
    }

    char[] convert(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("$#");
        for(int i = 0; i < s.length(); ++i) {
            sb.append(s.charAt(i)).append("#");
        }
        return sb.toString().toCharArray();
    }
}
