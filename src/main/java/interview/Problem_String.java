package interview;

import java.util.*;

public class Problem_String {
}


class L418_Sentence_Screen_Fitting {
    public int wordsTyping(String[] sentence, int rows, int cols) {
        StringBuilder sb = new StringBuilder();
        int maxLen = 0;
        for(String str : sentence) {
            sb.append(str).append(" ");
            maxLen = Math.max(maxLen, str.length());
        }

        if(maxLen > cols) {
            return 0;
        }

        // Tips: the string has one more " " at the tail
        String s = sb.toString();
        int len = s.length();

        int totalLen = 0, idx = 0;
        for(int i = 0; i < rows; ++i) {
            totalLen += cols;
            while(s.charAt(totalLen%len) != ' ') {
                totalLen--;
            }
            totalLen++;
        }
        System.out.println("totalLen = " + totalLen);
        return totalLen/len;
    }
}

/**
 * 1. 首先要能判断多少个word组成一行：
 * 这里统计读入的所有words的总长curLen，并需要计算空格的长度。假如已经读入words[0:i]。当curLen + i <=L 且加curLen + 1 + word[i+1].size() > L时，一行结束。
 *
 * 2. 知道一行的所有n个words，以及总长curLen之后要决定空格分配：
 * 平均空格数：k = (L - curLen) / (n-1)
 * 前m组每组有空格数k+1：m = (L - curLen) % (n-1)
 *
 * 例子：L = 21，curLen = 14，n = 4
 * k = (21 - 14) / (4-1) = 2
 * m = (21 - 14) % (4-1)  = 1
 * A---B--C--D
 *
 * 注意： 结尾没有空格
 *
 * 3. 特殊情况：
 * (a) 最后一行：当读入到第i = words.size()-1 个word时为最后一行。该行k = 1，m = 0
 * (b) 一行只有一个word：此时n-1 = 0，计算(L - curLen)/(n-1)会出错。该行k = L-curLen, m = 0
 */

class L68_Text_Justification {
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> ret = new ArrayList<>();
        if(words == null || words.length == 0) {
            return ret;
        }

        int start = 0, currentLength = 0;
        for(int i = 0; i < words.length; ++i) {
            currentLength += words[i].length();
            if(currentLength == maxWidth) {
                ret.add(createLine(words, start, i, maxWidth));
                start = i + 1;
                currentLength = 0;
            } else if(currentLength > maxWidth) {
                ret.add(createLine(words, start, i - 1, maxWidth));
                start = i;
                currentLength = words[i].length() + 1;
            } else {
                currentLength++;
            }
        }

        if(currentLength != 0) {
            ret.add(createLine(words, start, words.length - 1, maxWidth));
        }

        return ret;
    }

    String createLine(String[] words, int start, int end, int max) {
        if(start == end) {
            // one word in one line
            return createLine(words, start, end, max, 0, 0);
        } else if(end == words.length - 1) {
            // has the last word
            return createLine(words, start, end, max, 1, 0);
        } else {
            int totalWordsLen = 0;
            for(int i = start; i <= end; ++i) {
                totalWordsLen += words[i].length();
            }
            int totalSpaceNumbers = max - totalWordsLen;
            int minSpace = totalSpaceNumbers/(end - start);
            int remainingSpace = totalSpaceNumbers%(end - start);     // no space at tail
            return createLine(words, start, end, max, minSpace, remainingSpace);
        }
    }

    String createLine(String[] words, int start, int end, int max, int minimumSpace,
                      int additionalSpace) {
        StringBuilder sb = new StringBuilder();
        for(int i = start; i <= end; ++i) {
            sb.append(words[i]);
            if(i == end) {	                                         //do not update space at tail
                break;
            }
            for(int j = 0; j < minimumSpace; ++j) {
                sb.append(' ');
            }
            if(additionalSpace > 0) {
                sb.append(' ');
                additionalSpace--;
            }
        }

        while(sb.length() < max) {
            sb.append(' ');
        }
        return sb.toString();
    }
}


class L415_Add_Strings {
    public String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int index1 = num1.length() - 1;
        int index2 = num2.length() - 1;
        int carryover = 0;
        while(index1 >= 0 || index2 >= 0) {
            if(index1 >= 0) {
                carryover += num1.charAt(index1) - '0';
                index1--;
            }
            if(index2 >= 0) {
                carryover += num2.charAt(index2) - '0';
                index2--;
            }
            sb.append(carryover%10);
            carryover = carryover/10;
        }
        if(carryover != 0) {
            sb.append(carryover);
        }
        return sb.reverse().toString();
    }
}