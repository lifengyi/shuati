package interview;


import java.util.*;

public class LeetCode_Array {

    public static void main(String[]  args) {
        int[] array = {1,2, 3, 4, 5, 6};

    }

}



class L34_Search_for_a_Range {
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
}

class L41_First_Missing_Positive {public int L41_firstMissingPositive(int[] nums) {
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

}


class L55_Jump_Game {
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
}

class L66_Plus_One {
    public int[] plusOne(int[] digits) {
        int ret = 0;
        for(int i = digits.length - 1; i >= 0; --i) {
            ret = plusOne(digits[i]);
            digits[i] = ret;
            if(ret != 0) {
                return digits;
            }
        }

        int[] nums = new int[digits.length + 1];
        nums[0] = 1;
        return nums;
    }

    int plusOne(int num) {
        if(num  == 9) {
            return 0;
        } else {
            return num + 1;
        }
    }
}

class L128_Longest_Consecutive_Sequence_ {
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
}

class L152_Maximum_Product_Subarray {
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
}

class L198_House_Robber_ {
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
            // If we rob current cell, previous cell shouldn't be robbed. So, update the current value to previous one.
            int currRobbed = ifDidntRobPrevious + nums[i];

            // If we don't rob current cell, then the count should be max of the previous cell robbed and not robbed
            int currNotRobbed = Math.max(ifDidntRobPrevious, ifRobbedPrevious);

            // Update values for the next round
            ifDidntRobPrevious = currNotRobbed;
            ifRobbedPrevious = currRobbed;
        }

        return Math.max(ifRobbedPrevious, ifDidntRobPrevious);
    }
}

class L213_House_Robber_II_ {
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
}

class L337_House_Robber_III {
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
}

class L628_Maximum_Product_of_Three_Numbers {
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
}

class L162_Find_Peak_Element_ {
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
}

class L169_Majority_Element {
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
}

class L229_majority_Element_II {
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
}

class L189_Rotate_Array_ {
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
}

class L238_Product_of_Array_Except_Self {
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
}

class L268_Missing_Number {
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
}

class L283_Move_Zeroes {
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
}


class L349_Intersection_of_Two_Arrays {
    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        for(int num : nums1) {
            set.add(num);
        }

        List<Integer> list = new ArrayList<>();
        for(int num : nums2) {
            if(set.contains(num)) {
                list.add(num);
                set.remove(num);
            }
        }

        int[] result = new int[list.size()];
        for(int i = 0; i < result.length; ++i) {
            result[i] = list.get(i);
        }
        return result;
    }
}


/**
 * If the two arrays are sorted, then use 2 pointers,
 * each array has one pointer and compare these 2 pointers,
 * eaqual: both ++  or
 * move the pointer with smaller value
 */
class L350_Intersection_of_Two_Arrays_II {
    public int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int num : nums1) {
            if(map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        List<Integer> list = new ArrayList<>();
        for(int num : nums2) {
            if(map.containsKey(num)) {
                list.add(num);
                int count = map.get(num) - 1;
                if(count == 0) {
                    map.remove(num);
                } else {
                    map.put(num, count);
                }
            }
        }

        int[] result = new int[list.size()];
        for(int i = 0; i < result.length; ++i) {
            result[i] = list.get(i);
        }
        return result;
    }
}






