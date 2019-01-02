package interview;

import java.util.*;

public class Algorithm_two_pointers {
}


/**
 *  还有DP解法
 */
class L55_Jump_Games {
    public boolean canJump(int[] nums) {
        if(nums == null || nums.length == 0) {
            return true;
        }

        int maxJump = nums[0];
        for(int i = 1; i < nums.length; ++i) {
            if(i > maxJump) {
                return false;
            }

            maxJump = Math.max(maxJump, i + nums[i]);
        }

        return true;
    }
}

class L45_Jump_Game_II {
    public int jump(int[] nums) {
        if(nums == null || nums.length == 0 || nums.length == 1) {
            return 0;
        }

        int count = 1, maxJump = Math.min(nums.length - 1, nums[0]);
        int start = 1, end = maxJump;
        while(end < nums.length - 1) {
            count++;
            for(int i = start; i <= end; ++i) {
                maxJump = Math.max(maxJump, i + nums[i]);
            }
            maxJump = Math.min(nums.length - 1, maxJump);
            start = end + 1;
            end = maxJump;
        }

        return count;
    }
}

/**
 * 还有二分查找解法
 */
class L209_Minimum_Size_Subarray_Sum {
    public int minSubArrayLen(int s, int[] nums) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int minLen = Integer.MAX_VALUE;
        int j = 0, sum = 0, count = 0;
        for(int i = 0; i < nums.length; ++i) {
            while(j < nums.length && sum + nums[j] < s) {
                sum += nums[j];
                j++;
            }

            if(j == nums.length) {
                break;
            }

            minLen = Math.min(minLen, j - i + 1);
            sum -= nums[i];
        }

        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }
}


class L3_Longest_Substring_Without_Repeating_Characters {
    public int lengthOfLongestSubstring(String s) {
        if(s == null || s.length() == 0) {
            return 0;
        }

        int maxLen = 0;
        Set<Character> set = new HashSet<>();
        char[] array = s.toCharArray();

        for(int i = 0, j = i; i < array.length; ++i) {
            while(j < array.length && !set.contains(array[j])) {
                set.add(array[j]);
                j++;
            }

            maxLen = Math.max(maxLen, j - i);
            if(j == array.length) {
                break;
            }

            set.remove(array[i]);

        }

        return maxLen;
    }
}

class L159_Longest_Substring_with_At_Most_Two_Distinct_Characters {
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        if(s == null || s.length() == 0) {
            return 0;
        }

        int distinctFactor = 2;
        int maxLen = 0, distinctNumber = 0, value = 0;
        Map<Character, Integer> map = new HashMap<>();
        char[] array = s.toCharArray();

        for(int i = 0, j = i; i < array.length; ++i) {
            while(j < array.length
                    && !(!map.containsKey(array[j]) && distinctNumber + 1 > distinctFactor)) {
                if(map.containsKey(array[j])) {
                    map.put(array[j], map.get(array[j]) + 1);
                } else {
                    map.put(array[j], 1);
                    distinctNumber++;
                }
                j++;
            }

            maxLen = Math.max(maxLen, j - i);
            if(j == array.length) {
                break;
            }

            value = map.get(array[i]);
            if(value == 1) {
                map.remove(array[i]);
                distinctNumber--;
            } else {
                map.put(array[i], value - 1);
            }
        }

        return maxLen;
    }
}


class L15_3Sum {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        if(nums == null || nums.length < 3) {
            return result;
        }

        Arrays.sort(nums);
        int left = 0, right = 0;
        for(int i = 0; i < nums.length; ++i) {
            if(i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            left = i + 1;
            right = nums.length - 1;
            while(left < right) {
                if(nums[i] + nums[left] + nums[right] == 0) {
                    List<Integer> ret = new ArrayList<>();
                    ret.add(nums[i]);
                    ret.add(nums[left++]);
                    ret.add(nums[right--]);
                    result.add(ret);
                    while(left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    while(left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if(nums[i] + nums[left] + nums[right] < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return result;
    }
}

class L16_3Sum_Closet {
    public int threeSumClosest(int[] nums, int target) {
        if(nums == null || nums.length == 0) {
            return 0;
        }

        int minDiff = Integer.MAX_VALUE;
        int minResult = 0;
        int left, right;

        Arrays.sort(nums);
        for(int i = 0; i < nums.length; ++i) {
            left = i + 1;
            right = nums.length - 1;
            while(left < right) {
                int sum = nums[left] + nums[right] + nums[i];
                if(sum == target) {
                    return sum;
                } else {
                    int diff = Math.abs(target - sum);
                    if(diff < minDiff) {
                        minDiff = diff;
                        minResult = sum;
                    }
                    if(sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }

        return minResult;
    }
}

class L18_4Sum {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();

        if(nums == null || nums.length < 4) {
            return result;
        }

        Arrays.sort(nums);
        int sum = 0, left = 0, right = 0;
        for(int i = 0; i < nums.length; ++i) {
            if(i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            for(int j = i + 1; j < nums.length; ++j) {
                if(j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }

                left = j + 1;
                right = nums.length - 1;
                while(left < right) {
                    sum = nums[i] + nums[j] + nums[left] + nums[right];
                    if(sum == target) {
                        List<Integer> ret = new ArrayList<>();
                        ret.add(nums[i]);
                        ret.add(nums[j]);
                        ret.add(nums[left]);
                        ret.add(nums[right]);
                        result.add(ret);

                        left++;
                        right--;
                        while(left < right && nums[left] == nums[left - 1]) {
                            left++;
                        }
                        while(left < right && nums[right] == nums[right + 1]) {
                            right--;
                        }
                    } else if(sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }

        return result;
    }
}

/**
 *  不要求去掉重复项，如果有这个要求，则打开屏蔽代码
 */
class L454_4Sum_II {
    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        //sortArrays(A, B, C, D);
        Map<Integer, Integer> cache = initHashMap(A, B);

        int result = 0;
        int len = A.length, sum = 0;
        for(int i = 0; i < len; ++i) {
            //if(i > 0 && C[i] == C[i - 1]) {
            //    continue;
            //}
            for(int j = 0; j < len; ++j) {
                //if(j > 0 && D[j] == D[j - 1]) {
                //    continue;
                //}

                sum = C[i] + D[j];
                if(cache.containsKey(sum)) {
                    result += cache.get(sum);
                }
            }
        }
        return result;
    }

    Map<Integer, Integer> initHashMap(int[] A, int[] B) {
        Map<Integer, Integer> result = new HashMap<>();
        int len = A.length, sum = 0;
        for(int i = 0; i < len; ++i) {
            //if(i > 0 && A[i] == A[i - 1]) {
            //    continue;
            //}
            for(int j = 0; j < len; ++j) {
                //if(j > 0 && B[j] == B[j - 1]) {
                //    continue;
                //}

                sum = 0 - A[i] - B[j];
                if(result.containsKey(sum)) {
                    result.put(sum, result.get(sum) + 1);
                } else {
                    result.put(sum, 1);
                }
            }
        }
        return result;
    }

    void sortArrays(int[] A, int[] B, int[] C, int[] D) {
        Arrays.sort(A);
        Arrays.sort(B);
        Arrays.sort(C);
        Arrays.sort(D);
    }
}


class L76_Minimum_Window_Substring {
    public String minWindow(String s, String t) {
        if(s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }

        int[] targetHashMap = new int[256];
        int[] sourceHashMap = new int[256];
        char[] tArray = t.toCharArray();
        for(int i = 0; i < tArray.length; ++i) {
            targetHashMap[tArray[i]]++;
        }

        int minLen = Integer.MAX_VALUE, start = 0, end = 0;
        char[] sArray = s.toCharArray();
        for(int i = 0, j = i; i < sArray.length; ++i) {
            while(j < sArray.length && !validate(sourceHashMap, targetHashMap)) {
                sourceHashMap[sArray[j]]++;
                j++;
            }

            if(validate(sourceHashMap, targetHashMap)) {
                if(j - i < minLen) {
                    minLen = j - i;
                    start = i;
                    end = j;
                }
            }

            sourceHashMap[sArray[i]]--;
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, end);
    }

    boolean validate(int[] src, int[] target) {
        for(int i = 0; i < src.length; ++i) {
            if(target[i] > src[i]) {
                return false;
            }
        }
        return true;
    }
}

/**
 *  非固定双指针模板 + 字符串匹配包含判断
 *
 *  判断字符串的匹配包含关系：
 *      1.模板哈希表，一次性生成；
 *      2.源数据哈希表，动态维护；
 *      3.源数据哈希表中匹配满足模板的有效输入个数，
 *        窗口尾部进入数据，判断有效输入被加入：
 *              srcMap[idx] += 1;
 *              if (dstMap[idx] >= srcMap[idx]) {count--;}
 *        窗口头部挪走数据，判断有效输入被移出：
 *              srcMap[idx] -= 1;
 *              if (dstMap[idx] > srcMap[idx]) {count++;}
 */
class L76_Minimum_Window_Substring_v2 {
    public String minWindow(String s, String t) {
        if(s == null || t == null || s.length() < t.length()) {
            return "";
        }

        int[] dstMap = new int[256];
        for(int i = 0; i < t.length(); ++i) {
            dstMap[t.charAt(i)] += 1;
        }

        int minWindowSize = Integer.MAX_VALUE, minStartIndex = 0;
        int j = 0, count = t.length();
        int[] srcMap = new int[256];

        for(int i = 0; i < s.length(); ++i) {
            while(j < s.length() && !validate(s, j, srcMap, dstMap, count)) {
                srcMap[s.charAt(j)] += 1;
                if(dstMap[s.charAt(j)] >= srcMap[s.charAt(j)]) {
                    count--;
                }
                j++;
            }

            //the end of the string
            if(j == s.length()) {
                break;
            }

            //find a candidiate
            if(j - i + 1 < minWindowSize) {
                minWindowSize = j - i + 1;
                minStartIndex = i;
            }

            //move out the first character
            srcMap[s.charAt(i)] -= 1;
            if(dstMap[s.charAt(i)] > srcMap[s.charAt(i)]) {
                count++;
            }
        }

        return minWindowSize == Integer.MAX_VALUE ? "" : s.substring(minStartIndex, minStartIndex + minWindowSize);
    }

    boolean validate(String s, int index, int[] srcMap, int[] dstMap, int count) {
        char ch = s.charAt(index);

        //It's the last valid character
        if(dstMap[ch] == srcMap[ch] + 1 && count - 1 == 0) {
            return true;
        }

        return false;
    }
}

class L287_Find_the_Duplicate_Number {
    public int findDuplicate(int[] nums) {
        int slow = nums[0];
        int quick = nums[0];

        do{
            slow = nums[slow];
            quick = nums[nums[quick]];
        } while(slow != quick);

        quick = nums[0];
        while(slow != quick) {
            slow = nums[slow];
            quick = nums[quick];
        }

        return slow;
    }
}
