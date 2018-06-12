package com.stevenli.interview.easy.interview;

import com.stevenli.interview.algorithm.basic.BinaryTree.TreeNode;

import java.util.*;

public class LeetCode_Array {

    /**
     * TODO: 1. Two Sum
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] L1_twoSum(int[] nums, int target) {
        int[] ret = new int[2];
        Map<Integer, Integer> cache = new HashMap<>();
        for(int i = 0; i < nums.length; i++)
            cache.put(nums[i], i);

        for(int i = 0; i< nums.length; i++){
            int key = target - nums[i];
            if(cache.containsKey(key)) {
                ret[0] = i;
                ret[1] = cache.get(key);
                break;
            }
        }

        return ret;
    }


    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        double ret = 0;
        int len = nums1.length + nums2.length;
        if(!isEven(len)) {
            int median = (len+1)/2;
            for(int i=0,j=0, counter=1;;counter++)
            {
                if(i < nums1.length && j < nums2.length) {
                    if(counter == median) {
                        return Math.min(nums1[i], nums2[j]);
                    } else {
                        if(nums1[i] < nums2[j])
                            i++;
                        else
                            j++;
                    }
                } else if (i < nums1.length) {
                    if(counter == median) return nums1[i];
                    else i++;
                } else if (j < nums2.length) {
                    if(counter == median) return nums2[j];
                    else j++;
                }
            }
        } else {
            int median1 = (len - 1)/2;
            int median2 = (len - 1)/2 + 1;
            double ret1=0, ret2=0;

            for(int i=0,j=0, counter=0;;counter++)
            {
                if(i < nums1.length && j < nums2.length) {
                    if(counter == median1) {
                        if(nums1[i] < nums2[j]) {
                            ret1 = nums1[i];
                            i++;
                        } else {
                            ret1 = nums2[j];
                            j++;
                        }
                    } else if (counter == median2) {
                        ret2 = Math.min(nums1[i], nums2[j]);
                        return (ret1+ret2)/2;
                    }else {
                        if(nums1[i] < nums2[j]) i++;
                        else j++;
                    }
                } else if (i < nums1.length) {
                    if(counter == median1) {
                        ret1 = nums1[i];
                        i++;
                    }
                    else if(counter == median2) {
                        ret2 = nums1[i];
                        return (ret1 + ret2)/2;
                    }
                    else
                        i++;
                } else if (j < nums2.length) {
                    if(counter == median1) {
                        ret1 = nums2[j];
                        j++;
                    }
                    else if(counter == median2) {
                        ret2 = nums2[j];
                        return (ret1 + ret2)/2;
                    }
                    else
                        j++;
                }
            }
        }
    }

    private boolean isEven(int num) {
        if((num&1) == 0) return true;
        return false;
    }

    /**
     * TODO: 33. Search in Rotated Sorted Array
     *
     * @param nums
     * @param target
     * @return
     */
    public int L33_search(int[] nums, int target) {
        int begin = 0;
        int end = nums.length - 1;

        while(begin <= end) {

            int middle = (begin + end)/2;
            if(nums[middle] == target) {
                return middle;
            }

            if(nums[middle] < nums[end]) {
                if(target > nums[middle] && target <= nums[end])
                    begin = middle + 1;
                else
                    end = middle - 1;
            } else {
                if(target >= nums[begin] && target < nums[middle])
                    end = middle -1;
                else
                    begin = middle + 1;
            }

        }

        return -1;
    }

    /**
     * TODO: 34. Search for a Range
     */
    public int[] L34_searchRange(int[] nums, int target) {
        int begin = 0;
        int end = nums.length - 1;
        int targetIndex = -1;

        int[] ret = new int[2];
        ret[0] = -1;
        ret[1] = -1;

        if(nums == null || nums.length == 0)
            return ret;

        while(begin <= end) {
            int middle = (begin + end)/2;
            if(nums[middle] == target) {
                targetIndex = middle;
                break;
            } else if(nums[middle] < target) {
                begin = middle + 1;
            } else {
                end = middle - 1;
            }
        }

        if(targetIndex != -1) {
            int startPos = targetIndex;
            int endPos = targetIndex;
            while( endPos + 1 < nums.length && nums[endPos] == nums[endPos + 1]) {
                endPos++;
            }
            while(startPos - 1 >= 0 && nums[startPos] == nums[startPos - 1]) {
                startPos--;
            }

            ret[0] = startPos;
            ret[1] = endPos;
            return ret;
        }

        return ret;
    }


    /**
     * TODO: 41. First Missing Positive
     *
     * @param nums
     * @return
     */
    public int L41_firstMissingPositive(int[] nums) {
        int index = 0;
        while(index < nums.length) {
            if(nums[index] == index + 1 || nums[index] <= 0 || nums[index] > nums.length) {
                index++;
            } else if(nums[index] != index + 1 && nums[index] != nums[nums[index] - 1]){
                //错误2， 在这个else if判断中遗漏了第二个判断项目，即当前节点
                //和所要交换的目标节点拥有相同的值，也就是对方节点已经满足条件了，不需要再换了
                //加入第二限制向，以及加else分支。

                int tmp = nums[nums[index] - 1];    //错误3， 如果此处缓存的是nums[index]
                                                    //那么当： nums[index] = nums[nums[index] - 1]
                                                    //下一行如此写讲出错： nums[nums[index] - 1]
                                                    //因为nums[index] 已经改变
                                                    //可以采用现在的方案，或者nums[tmp-1] = tmp;
                nums[nums[index] - 1] = nums[index];
                nums[index] = tmp;
            } else {
                index++;
            }
        }

        index = 0;
        while(index < nums.length) {
            if(nums[index] != index + 1)
                return index + 1;
            index++;                    //错误1， 白痴错误，一楼该行
        }

        return index + 1;       //全部满足，需要+1返回正确的下一个正整数
    }


    /**
     * TODO: 49. Group Anagrams
     *
     * @param strs
     * @return
     */
    public List<List<String>> L49_groupAnagrams(String[] strs) {
        List<List<String>> ret = new ArrayList<>();
        if(strs == null || strs.length == 0)
            return ret;

        Map<String, List<String>> anagramCache = new HashMap<>();
        for(String str : strs) {
            char[] charArray = str.toCharArray();
            Arrays.sort(charArray);
            String pattern = new String(charArray);

            if(anagramCache.containsKey(pattern)) {
                List<String> value = anagramCache.get(pattern);
                value.add(str);
            } else {
                List<String> value = new ArrayList<>();
                value.add(str);
                anagramCache.put(pattern, value);
            }
        }

        //collect the return value
        //for(String key : anagramCache.keySet()) {
        //    ret.add(anagramCache.get(key));
        //}
        ret = (ArrayList<List<String>>) anagramCache.values();

        return ret;
    }


    public List<List<String>> L49_groupAnagrams_v2(String[] strs) {
        List<List<String>> ret = new ArrayList<>();
        if(strs == null || strs.length == 0)
            return ret;

        Map<String, List<String>> anagramCache = new HashMap<>();
        for(String str : strs) {
            String pattern = getAnagramPattern(str);
            if(anagramCache.containsKey(pattern)) {
                List<String> value = anagramCache.get(pattern);
                value.add(str);
            } else {
                List<String> value = new ArrayList<>();
                value.add(str);
                anagramCache.put(pattern, value);
            }
        }

        //collect the return value
        for(String key : anagramCache.keySet()) {
            ret.add(anagramCache.get(key));
        }

        return ret;
    }

    //Only support the following cases:
    //1. All the characters in string is lower case
    //2. String length < 999
    //3. No other special characters
    private String getAnagramPattern(String str) {
        int[] cache = new int[26];
        byte[] stringBytes = str.getBytes();
        for(byte element : stringBytes) {
            cache[element-97] += 1;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 26; i++) {
            if(cache[i] != 0) {
                sb.append(String.format("%02d%03d", i, cache[i]));
            }
        }

        return sb.toString();
    }


    /**
     * TODO: 53. Maximum Subarray
     *
     * Kadane算法
     *
     * @param nums
     * @return
     */
    public int L53_maxSubArray(int[] nums) {
        if(nums == null || nums.length == 0)
            return 0;

        int maxSum = Integer.MIN_VALUE;
        int maxSoFar = 0;
        for(int i : nums) {
            maxSoFar = Math.max(i, maxSoFar + i);
            maxSum = Math.max(maxSum, maxSoFar);
        }

        return maxSum;
    }


    /**
     * TODO: 55. Jump Game
     *
     * @param nums
     * @return
     */
    public boolean L55_canJump(int[] nums) {
        int maxJump = 0;
        for(int i = 0; i < nums.length - 1; i++) {
            maxJump = Math.max(maxJump, i + nums[i]);
            if(maxJump >= nums.length - 1)
                return true;
            if(maxJump == i)
                return false;
        }
        return true;
    }

    public boolean L55_canJump_v3(int[] nums) {
        if(nums == null || nums.length < 2)
            return true;

        for(int i = 0; i < nums.length - 1;) {
            int step = nums[i];
            if(step == 0)
                return false;
            if(step + i >= nums.length - 1)
                return true;

            int longestJump = 0;
            int longestJumpIndex = 0;
            for(int j = 1; j <= step; j++) {
                if(nums[i+j] + i + j >= longestJump) {
                    longestJump = nums[i+j] + i + j;
                    longestJumpIndex = i+j;
                }
            }

            i = longestJumpIndex;
        }

        return true;
    }

    public boolean L55_canJump_v2(int[] nums) {
        if(nums == null || nums.length < 2)
            return true;

        return ifReachLastIndex(nums, 0);
    }

    private boolean ifReachLastIndex(int[] nums, int index) {
        if(index == nums.length - 1 || index + nums[index] >= nums.length - 1)
            return true;

        if(nums[index] == 0)
            return false;

        for(int i = nums[index]; i > 0; i--) {
            boolean ret = ifReachLastIndex(nums, index + i);
            if(ret) return true;
        }

        return false;
    }


    /**
     * TODO: 66. Plus One
     * @param digits
     * @return
     */
    public int[] L66_plusOne(int[] digits) {
        int extraOne = 1;
        for(int i = digits.length - 1; i >= 0; i--) {
            int tmp = digits[i];
            digits[i] = (tmp + extraOne)%10;
            extraOne = (tmp + extraOne)/10;
        }

        if(extraOne == 1){
            int[] ret = new int[digits.length + 1];
            ret[0] = 1;
            return ret;
        } else {
            return digits;
        }
    }


    /**
     * TODO: 75. Sort Colors
     *
     * @param nums
     */
    public void L75_sortColors(int[] nums) {
        if(nums == null || nums.length == 1)
            return;

        int positionOfZero = 0;
        int positionOfTwo = nums.length - 1;
        for(int i = 0; i <= positionOfTwo;) {
            // Enhancement
            //while(positionOfTwo >= i && nums[positionOfTwo] == 2) positionOfTwo--;
            //if(i > positionOfTwo)
            //    break;

            if(nums[i] == 0) {
                if(positionOfZero != i) {
                    int tmp = nums[positionOfZero];
                    nums[positionOfZero] = nums[i];
                    nums[i] = tmp;
                    positionOfZero++;
                    i++;
                } else {
                    positionOfZero++;
                    i++;
                }
            } else if(nums[i] == 2) {
                int tmp = nums[positionOfTwo];
                nums[positionOfTwo] = nums[i];
                nums[i] = tmp;
                positionOfTwo--;
            } else {
                i++;        // 原来i++放在for的条件里，这个if branch可以省略
                            // 但是为了当num==2时继续操作当前i，就把i++拿出来了
                            // 犯了一个错误就是，改了设计之后，忘记加这个了。切忌！！！！！
            }
        }
    }


    /**
     * TODO: 121. Best Time to Buy and Sell Stock
     *
     * @param prices
     * @return
     */
    public int L121_maxProfit(int[] prices) {
        int lowestBuy = Integer.MAX_VALUE;
        int maxProfit = 0;
        for(int i = 0; i < prices.length; i++) {
            lowestBuy = Math.min(lowestBuy, prices[i]);
            maxProfit = Math.max(maxProfit, prices[i] - lowestBuy);
        }

        return maxProfit;
    }

    /**
     * TODO: 122. Best Time to Buy and Sell Stock II
     *
     * @param prices
     * @return
     */
    public int L122_maxProfit(int[] prices) {
        int maxProfit = 0;
        for(int i = 0; i < prices.length - 1; i++) {
            if(prices[i+1] > prices[i])
                maxProfit += prices[i+1] - prices[i];
        }

        return maxProfit;
    }

    public int L122_maxProfit_v2(int[] prices) {
        if(prices == null || prices.length == 0)
            return 0;

        int maxProfit = 0;
        int maxProfixOfEachTransaction = 0;
        int lowestBuyOfEachTransaction = prices[0];
        int highestSellPricesofEachTransaction = prices[0];

        for(int i = 1; i < prices.length; i++) {
            if(prices[i] < highestSellPricesofEachTransaction) {
                maxProfit += maxProfixOfEachTransaction;
                maxProfixOfEachTransaction = 0;
                lowestBuyOfEachTransaction = prices[i];
                highestSellPricesofEachTransaction = prices[i];
            } else {
                lowestBuyOfEachTransaction = Math.min(lowestBuyOfEachTransaction, prices[i]);
                if(prices[i] - lowestBuyOfEachTransaction > maxProfixOfEachTransaction) {
                    highestSellPricesofEachTransaction = prices[i];
                    maxProfixOfEachTransaction = prices[i] - lowestBuyOfEachTransaction;
                }
            }

        }

        if(maxProfixOfEachTransaction != 0)
            maxProfit += maxProfixOfEachTransaction;

        return maxProfit;
    }


    /**
     * TODO: 128. Longest Consecutive Sequence
     *
     * @param nums
     * @return
     */
    public int L128_longestConsecutive(int[] nums) {
        Set<Integer> cache = new HashSet<>();
        for(int i = 0; i < nums.length; i++) {
            if(!cache.contains(nums[i]))
                cache.add(nums[i]);
        }

        int maxLength = 0;
        for(int number : nums) {
            if(cache.isEmpty())
                break;
            if(cache.contains(number)) {

                int length = 1;
                int successorNumber = number + 1;
                int predecessorNumber = number - 1;
                while(cache.contains(successorNumber)) {
                    cache.remove(successorNumber);
                    length++;
                    successorNumber++;
                }
                while(cache.contains(predecessorNumber)) {
                    cache.remove(predecessorNumber);
                    length++;
                    predecessorNumber--;
                }
                maxLength = Math.max(maxLength, length);
                cache.remove(number);
            }
        }

        return maxLength;
    }


    /**
     * TODO: 152. Maximum Product Subarray
     *
     * @param nums
     * @return
     */
    public int L152_maxProduct(int[] nums) {
        int ret = nums[0];
        int max = ret;
        int min = ret;
        for(int i = 1; i < nums.length; i++) {
            int tmp = max;
            max = Math.max(nums[i], Math.max(max * nums[i], min * nums[i]));
            min = Math.min(nums[i], Math.min(tmp * nums[i], min * nums[i]));

            ret = Math.max(ret, max);
        }
        return ret;
    }

    public int L152_maxProduct_v2(int[] nums) {
        if(nums == null || nums.length == 0)
            return 0;

        int maxProduct = Integer.MIN_VALUE;
        int minSoFar = 1;
        int maxSoFar = 1;
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] < 0) {
                int tmp = maxSoFar;
                maxSoFar = minSoFar;
                minSoFar = tmp;
            }

            maxSoFar = Math.max(nums[i], maxSoFar * nums[i]);
            minSoFar = Math.min(nums[i], minSoFar * nums[i]);

            maxProduct = Math.max(maxProduct, maxSoFar);
        }

        return maxProduct;
    }

    /**
     * 198. House Robber
     *
     * @param nums
     * @return
     */
    public int L198_rob(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        if (nums.length == 1)
            return nums[0];
        if (nums.length == 2)
            return Math.max(nums[0], nums[1]);

        int[] prevRobMoney = new int[2];
        prevRobMoney[0] = nums[0];
        prevRobMoney[1] = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            int maxRobMoney = Math.max(prevRobMoney[1], prevRobMoney[0] + nums[i]);
            prevRobMoney[0] = prevRobMoney[1];
            prevRobMoney[1] = maxRobMoney;
        }

        return prevRobMoney[1];
    }


    public int L198_rob_v0(int[] nums) {
        int rob = 0;
        int giveup = 0;
        for (int n : nums){
            int robi = giveup + n;
            int giveupi = Math.max(rob, giveup);
            rob = robi;
            giveup = giveupi;
        }
        return Math.max(rob, giveup);
    }

    public int L198_rob_v2(int[] nums) {
        if(nums == null || nums.length == 0)
            return 0;

        if(nums.length == 1)
            return nums[0];
        if(nums.length == 2)
            return Math.max(nums[0], nums[1]);

        int previousPreviousTotalRobMoney = nums[0];
        int previousTotalRobMoney = nums[1];
        int previousHouseMoney = nums[1];
        for(int i = 2; i < nums.length; i++) {
            int currentRobMoney = 0;
            /**
             * if the currentHouseMoney is more than previousHouseMoney
             */
            if(nums[i] >= previousHouseMoney){
                /**
                 * previous house is robbed:
                 *     compare and get max:  TotalMoneyOfPP + currentHouseMoney   vs   TotalMoneyOfP + currentHouseMoney - prevousHouseMoney
                 * previous house is not robbed:
                 *     compare and get max:  (TotalMoneyOfPP == TotalMoneyOfP)   + currentHouseMoney
                 */
                currentRobMoney = Math.max(previousPreviousTotalRobMoney + nums[i],
                        previousTotalRobMoney + nums[i] - previousHouseMoney);
            } else {
                /**
                 * previous house is robbed:
                 *      compare and get max:   TotalMoneyOfPP + currentHouseMoney  vs  TotalMoneyOfP
                 * previous house is not robbed:
                 *      compare and get max:  (TotalMoneyOfPP == TotalMoneyOfP) + currentHouseMoney
                 */
                currentRobMoney = Math.max(previousPreviousTotalRobMoney + nums[i], previousTotalRobMoney);
            }

            previousPreviousTotalRobMoney = previousTotalRobMoney;
            previousTotalRobMoney = currentRobMoney;
            previousHouseMoney = nums[i];
        }

        return previousTotalRobMoney;
    }

    public int L198_rob_v3(int[] nums) {
        int ifRobbedPrevious = 0;       // max monney can get if rob current house
        int ifDidntRobPrevious = 0;     // max money can get if not rob current house

        // We go through all the values, we maintain two counts, 1) if we rob this cell, 2) if we didn't rob this cell
        for (int i = 0; i < nums.length; i++) {
            // If we rob current cell, previous cell shouldn't be robbed. So, add the current value to previous one.
            int currRobbed = ifDidntRobPrevious + nums[i];

            // If we don't rob current cell, then the count should be max of the previous cell robbed and not robbed
            int currNotRobbed = Math.max(ifDidntRobPrevious, ifRobbedPrevious);

            // Update values for the next round
            ifDidntRobPrevious = currNotRobbed;
            ifRobbedPrevious = currRobbed;
        }

        return Math.max(ifRobbedPrevious, ifDidntRobPrevious);
    }

    /**
     * TODO: 213. House Robber II
     *
     * @param nums
     * @return
     */
    public int L213_rob(int[] nums) {
        if(nums == null || nums.length == 0)
            return 0;
        if(nums.length == 1)
            return nums[0];
        if(nums.length == 2)
            return Math.max(nums[0], nums[1]);

        return Math.max(rob(nums, 0, nums.length - 1), rob(nums, 1, nums.length));
    }

    private int rob(int[] nums, int begin, int end) {
        if(end - begin == 2)
            return Math.max(nums[begin], nums[begin + 1]);

        int[] prevMaxRobbed = new int[2];
        prevMaxRobbed[0] = nums[begin];
        prevMaxRobbed[1] = Math.max(nums[begin], nums[begin + 1]);

        for(int i = begin + 2; i < end; i++) {
            int currentRobbed = Math.max(prevMaxRobbed[1], prevMaxRobbed[0] + nums[i]);
            prevMaxRobbed[0] = prevMaxRobbed[1];
            prevMaxRobbed[1] = currentRobbed;
        }

        return prevMaxRobbed[1];
    }


    /**
     * TODO: 337. House Robber III
     *
     */
    int maxRobMoney = 0;

    public int L337_rob(TreeNode root) {
        int[] ret = robMoney(root);
        return Math.max(ret[0], ret[1]);
    }

    private int[] robMoney(TreeNode node){
        //0: rob the current node, 1: not rob the current node
        int[] ret = new int[2];

        if(node == null)
            return ret;

        int[] left = robMoney(node.left);
        int[] right = robMoney(node.right);

        ret[0] = left[1] + right[1] + node.val;
        ret[1] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        return ret;
    }


    /**
     * TODO: 628. Maximum Product of Three Numbers
     *
     * @param nums
     * @return
     */
    public int L628_maximumProduct(int[] nums) {
        int max1 = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;
        int max3 = Integer.MIN_VALUE;
        int min1 = Integer.MAX_VALUE;
        int min2 = Integer.MAX_VALUE;
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] > max1) {
                max3 = max2; max2 = max1; max1 = nums[i];
            } else if(nums[i] > max2) {
                max3 = max2; max2 = nums[i];
            } else if(nums[i] > max3) {
                max3 = nums[i];
            }
            if(nums[i] < min1) {
                min2 = min1; min1 = nums[i];
            } else if(nums[i] < min2) {
                min2 = nums[i];
            }
        }

        return Math.max(max3*max2*max1, min1*min2*max1);
    }

    public int L628_maximumProduct_v2(int[] nums) {
        Arrays.sort(nums);
        return Math.max(nums[0] * nums[1] * nums[nums.length - 1],
                nums[nums.length - 1] * nums[nums.length - 2] * nums[nums.length - 3]);
    }

    /**
     * TODO: 162. Find Peak Element
     *
     * @param nums
     * @return
     */
    public int L162_findPeakElement(int[] nums) {
        if(nums.length == 1)
            return 0;

        int left = 0;
        int right = nums.length - 1;
        while(left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] < nums[mid + 1])
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }


    /**
     * TODO: 169. Majority Element
     * Boyer–Moore majority vote algorithm
     *
     * @param nums
     * @return
     */
    //O(n) + O(1)
    public int L169_majorityElement(int[] nums) {
        int ret = 0;
        for(int i = 0; i < 32; i++) {
            int counter = 0;
            for(int j = 0; j < nums.length; j++) {
                if(((1 << i) & nums[j]) == (1 << i)) {
                    counter++;
                }
            }
            if(counter > nums.length/2)
                ret |= 1 << i;
        }
        return ret;
    }

    //O(nlogN) + O(1)
    public int L169_majorityElement_v2(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length/2];
    }

    //O(n) + O(1)
    public int L169_majorityElement_v1(int[] nums) {
        int currentNum = nums[0];
        int currentNumCounter = 1;
        for(int i = 1; i < nums.length; i++) {
            if(currentNumCounter == 0) {
                currentNum = nums[i];
                currentNumCounter = 1;
            } else if (currentNum == nums[i]) {
                currentNumCounter++;
            } else {
                currentNumCounter--;
            }
        }

        //if the majority element is not guranteed, then we need to scan the array again
        //to verify if the result is the majority element
        return currentNum;
    }


    /**
     * TODO: 229. Majority Element II
     *
     * @param nums
     * @return
     */
    public List<Integer> L229_majorityElement_II(int[] nums) {
        List<Integer> majorityElements = new ArrayList<>();
        if(nums == null || nums.length == 0)
            return majorityElements;

        int candidate1 = 0;
        int counter1 = 0;
        int candidate2 = 0;
        int counter2 = 0;
        for(int i = 0; i < nums.length; i++) {
            if(counter1 == 0 && (counter2 == 0 || candidate2 != nums[i])) {
                candidate1 = nums[i];
                counter1++;
            } else if(candidate1 == nums[i]) {
                counter1++;
            } else if(counter2 == 0) {
                candidate2 = nums[i];
                counter2++;
            } else if(candidate2 == nums[i]) {
                counter2++;
            } else {
                counter1--;
                counter2--;
            }
        }

        counter1 = 0;
        counter2 = 0;
        for(int num : nums) {
            if(num == candidate1) counter1++;
            else if(num == candidate2) counter2++;
        }
        if(counter1 > nums.length/3) majorityElements.add(candidate1);
        if(counter2 > nums.length/3) majorityElements.add(candidate2);
        return majorityElements;
    }


    /**
     * TODO: 189. Rotate Array
     *
     * @param nums
     * @param k
     */
    public void L189_rotate(int[] nums, int k) {
        int setNumber = gcd(nums.length, k);
        for(int i = 0; i < setNumber; i++) {
            int firstIndex = i;
            int cachedValue = nums[i];
            int currentIndex = firstIndex;
            int nextIndex = 0;
            do{
                nextIndex = (currentIndex + k) % nums.length;
                cachedValue ^= nums[nextIndex];
                nums[nextIndex] ^= cachedValue;
                cachedValue ^= nums[nextIndex];
                currentIndex = nextIndex;
            } while(currentIndex != firstIndex);
        }
    }

    private int gcd(int a, int b) {
        return b==0 ? a : gcd(b, a % b);
    }

    public void L189_rotate_v2(int[] nums, int k){
        if(k > nums.length) k = k % nums.length;
        if(k == 0) return;
        int[] buf = Arrays.copyOfRange(nums, nums.length - k, nums.length);
        for(int i = nums.length - k - 1, j = 0; i >= 0; i--,j++)
            nums[nums.length - 1 - j] = nums[i];

        for(int i = 0; i < k; i++) {
            nums[i] = buf[i];
        }
    }

    /**
     * TODO: 238. Product of Array Except Self
     *
     * @param nums
     * @return
     */
    public int[] L238_productExceptSelf(int[] nums) {
        int[] ret = new int[nums.length];
        int total = 1;
        int numberOfZero = 0;
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] != 0) {
                total = total * nums[i];
            }
            else {
                numberOfZero++;
                if(numberOfZero >= 2)
                    return ret;
            }
        }

        for(int i = 0; i < nums.length; i++) {
            if(nums[i] == 0)
                ret[i] = total;
            else
                ret[i] = (numberOfZero == 1 ? 0 : total / nums[i]);
        }

        return ret;
    }

    public int[] L238_productExceptSelf_v2(int[] nums) {
        int[] ret = new int[nums.length];
        ret[0] = 1;
        for(int i = 1; i < nums.length; i++) {
            ret[i] = ret[i - 1] * nums[i - 1];
        }

        int productFromTail = nums[nums.length - 1];
        for(int i = nums.length - 2; i >= 0; i--) {
            ret[i] *= productFromTail;
            productFromTail *= nums[i];
        }

        return ret;
    }

    /**
     * TODO: 268. Missing Number
     *
     * @param nums
     * @return
     */
    public int L268_missingNumber(int[] nums) {
        int ret = 0;
        for(int i = 0; i < nums.length; i++) {
            ret ^= i;
            ret ^= nums[i];
        }
        ret ^= nums.length;
        return ret;
    }

    public int L268_missingNumber_v1(int[] nums) {
        int i = 0;
        while(i < nums.length) {
            if(nums[i] > nums.length - 1 || nums[i] == i){
                i++;
            } else {
                int tmp = nums[i];
                nums[i] = nums[tmp];
                nums[tmp] = tmp;
            }
        }
        for(int j = 0; j < nums.length; j++)
            if(j != nums[j])
                return j;

        return nums.length;
    }


    /**
     * TODO: 283. Move Zeroes
     *
     * @param nums
     */
    public void L283_moveZeroes(int[] nums) {
        int newEndOfArray = 0;
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] != 0) {
                if(newEndOfArray != i)
                    nums[newEndOfArray] = nums[i];
                newEndOfArray++;
            }
        }
        while(newEndOfArray < nums.length) {
            nums[newEndOfArray] = 0;
            newEndOfArray++;
        }
    }


    /**
     * 287. Find the Duplicate Number
     *
     * @param nums
     * @return
     */
    public int L287_findDuplicate(int[] nums) {
        int i = nums[0];
        int j = nums[0];
        do{
            i = nums[i];
            j = nums[nums[j]];
        }while(i != j);

        j = nums[0];
        while(i != j) {
            i = nums[i];
            j = nums[j];
        }

        return i;
    }


    /**
     * TODO: 289. Game of Life
     *
     * @param board
     */
    public void L289_gameOfLife_v3(int[][] board) {
        if(board.length == 0 || (board.length == 1 && board[0].length == 0))
            return;

        int[] lookupBuffer = new int[512];
        for(int i = 0; i < 512; i++) {
            int bitCounter = Integer.bitCount(i);
            if(bitCounter == 3 || (bitCounter == 4 && (i & 16) == 16))
                lookupBuffer[i] = 1;
        }

        int[][] buffer = new int[board.length][board[0].length];
        for(int i = 0; i < board.length; i++) {
            int env = (i-1 >= 0 && board[i-1][0] == 1 ? 4:0)
                    + (board[i][0] == 1 ? 2:0)
                    + (i+1 < board.length && board[i+1][0] == 1 ? 1:0);
            for(int j = 0; j < board[i].length; j++) {
                env = (env % 64) * 8 + (i-1 >= 0 && j+1 < board[i].length && board[i-1][j+1] == 1 ? 4:0)
                        + (j+1 < board[i].length && board[i][j+1] == 1 ? 2:0)
                        + (i+1 < board.length && j+1 < board[i].length && board[i+1][j+1] == 1 ? 1:0);
                buffer[i][j] = lookupBuffer[env];
            }
        }
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = buffer[i][j];
            }
        }
    }

    public void L289_gameOfLife_v2(int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                int neighbours = getNeighbours(board, i, j);
                if(board[i][j] == 1 && (neighbours == 2 || neighbours == 3)) {
                    board[i][j] |= 2;
                } else if(board[i][j] == 0 && neighbours == 3) {
                    board[i][j] |= 2;
                }
            }
        }
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = board[i][j] >> 1;
            }
        }
    }

    private int getNeighbours(int[][]board, int p, int q) {
        int counter = 0;
        for(int i = Math.max(p-1, 0); i <= Math.min(p+1, board.length-1); i++){
            for(int j = Math.max(q-1, 0); j <= Math.min(q+1, board[0].length-1); j++){
                if(i==p && j==q)
                    continue;
                if((board[i][j] & 1) == 1)
                    counter++;
            }
        }
        return counter;
    }

    public void L289_gameOfLife(int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                int liveCounter = 0;
                if(i-1 >= 0 && j-1 >= 0 && (board[i-1][j-1] == 1 || board[i-1][j-1] == 2))
                    liveCounter++;
                if(i-1 >= 0 && (board[i-1][j] == 1 || board[i-1][j] == 2))
                    liveCounter++;
                if(i-1 >= 0 && j+1 < board[i].length && (board[i-1][j+1] == 1 || board[i-1][j+1] == 2))
                    liveCounter++;
                if(j-1 >= 0 && (board[i][j-1] == 1 || board[i][j-1] == 2))
                    liveCounter++;
                if(j+1 < board[i].length && (board[i][j+1] == 1 || board[i][j+1] == 2))
                    liveCounter++;
                if(i+1 < board.length && j-1 >= 0 && (board[i+1][j-1] == 1 || board[i+1][j-1] == 2))
                    liveCounter++;
                if(i+1 < board.length && (board[i+1][j] == 1 || board[i+1][j] == 2))
                    liveCounter++;
                if(i+1 < board.length && j+1 < board[i].length && (board[i+1][j+1] == 1 || board[i+1][j+1] == 2))
                    liveCounter++;

                if(board[i][j] == 1 && (liveCounter < 2 || liveCounter > 3)) {
                    board[i][j] = 2;
                } else if (board[i][j] == 0 && liveCounter == 3) {
                    board[i][j] = 3;
                }
            }
        }
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = board[i][j]%2;
            }
        }
    }

    /**
     * 48. Rotate Image
     *
     * @param matrix
     */
    public void L48_rotate(int[][] matrix) {
        //transpose the matrix
        int tmp;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = i + 1; j < matrix.length; j++) {
                tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        //if clockwise, then reverse left to right
        //if anticlockwise, then reverse up to down
        for(int i = 0; i < matrix.length; i++) {
            int left = 0;
            int right = matrix.length - 1;
            while(left < right) {
                tmp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = tmp;
                left++;
                right--;
            }
        }
    }

    public void L48_rotate_v2(int[][] matrix) {
        rotateMatrix(matrix, 0, matrix.length - 1);
    }

    /**
     * 48. Rotate Image
     *
     * @param matrix
     * @param begin
     * @param end
     */
    private void rotateMatrix(int[][] matrix, int begin, int end) {
        if(begin >= end)
            return;

        int cacheValue, x, y, tmp;
        for(int i = begin; i < end; i++){
            x = begin;
            y = i;
            cacheValue = matrix[x][y];
            for(int counter = 0; counter < 4; counter++){
                cacheValue ^= matrix[y][begin + end - x];
                matrix[y][begin + end - x] ^= cacheValue;
                cacheValue ^= matrix[y][begin + end - x];
                tmp = x;
                x = y;
                y = begin + end - tmp;
            }
        }

        rotateMatrix(matrix, begin+1, end-1);
    }


    /**
     * 54. Spiral Matrix
     *
     * @param matrix
     * @return
     */
    public List<Integer> L54_spiralOrder(int[][] matrix) {
        List<Integer> ret = new ArrayList<>();

        if(matrix.length == 0 || matrix[0].length == 0)
            return ret;

        int rowBegin = 0;
        int rowEnd = matrix.length - 1;
        int colBegin = 0;
        int colEnd = matrix[0].length - 1;

        while(rowBegin <= rowEnd && colBegin <= colEnd) {
            for(int i = colBegin; i <= colEnd; i++)
                ret.add(matrix[rowBegin][i]);
            if(++rowBegin > rowEnd)
                break;

            for(int i = rowBegin; i <= rowEnd; i++)
                ret.add(matrix[i][colEnd]);
            if(--colEnd < colBegin)
                break;

            for(int i = colEnd; i >= colBegin; i--)
                ret.add(matrix[rowEnd][i]);
            rowEnd--;

            for(int i = rowEnd; i >= rowBegin; i--){
                ret.add(matrix[i][colBegin]);
            }
            colBegin++;
        }

        return ret;
    }

    public static void main(String[]  args) {
        int[] array = {1,2, 3, 4, 5, 6};

        LeetCode_Array processor = new LeetCode_Array();

        //processor.rotate(array, 2);

        /*
        int[] ret = processor.twoSum(array, 6);
        for(int i : ret)
            System.out.println(i);

        int[] array1 = {1,3};
        int[] array2 = {2};
        double ret2 = processor.findMedianSortedArrays(array1, array2);
        System.out.println("ret2 = " + ret2);
        */

        ///int ret = processor.firstMissingPositive(array);
        //System.out.println(ret);

        //int ret = processor.maxProfit(array);
        //System.out.println(ret);
    }



}

