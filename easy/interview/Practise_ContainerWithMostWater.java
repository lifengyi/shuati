package com.stevenli.interview.easy.interview;

public class Practise_ContainerWithMostWater {

    public int getMostWater_O_N(int[] height) {
        if(height == null || height.length == 0) {
            return 0;
        }

        int max = 0;
        int left = 0;
        int right = height.length - 1;
        while(left < right) {
            max = Math.max(max, (right-left)*Math.min(height[left], height[right]));
            if(height[left] < height[right])
                left++;
            else
                right--;
        }

        return max;
    }

    //reduce the time of comparison
    public int getMostWater_O_N_v2(int[] height) {
        if(height == null || height.length == 0)
            return 0;

        int left = 0;
        int right = height.length - 1;
        int maxWater = 0;

        while(left < right) {
            System.out.println(String.format("left=%d, right=%d, max=%d, maxWater=%d",
                    left, right,
                    (right-left)*Math.min(height[left], height[right]),
                    maxWater));

            maxWater = Math.max(maxWater, (right - left)* (Math.min(height[left], height[right])));

            if(height[left] < height[right])  {
                int tmp = height[left++];
                while(height[left] < tmp)
                    left++;
            } else {
                int tmp=height[right--];
                while(height[right] < tmp)
                    right--;
            }
        }

        return maxWater;
    }

    public int getMostWater_O_NPowerOf2(int[] height) {
        if(height == null || height.length == 0)
            return 0;

        int maxWater = 0;
        for(int i=0; i<height.length; i++) {
            for(int j=i+1; j<height.length; j++) {
                maxWater = Math.max(maxWater, (j-i)*Math.min(height[i], height[j]));
            }
        }

        return maxWater;
    }

    public static void main(String[] args) {
        Practise_ContainerWithMostWater obj = new Practise_ContainerWithMostWater();

        int array[] = {3, 4, 1, 1, 1, 1, 1, 1, 4, 4};
        //int array[] = {6, 2, 3, 7, 4, 9, 10, 1, 8};
        int max = obj.getMostWater_O_N(array);
        System.out.println("max = " + max);

        int max2 = obj.getMostWater_O_NPowerOf2(array);
        System.out.println("max2 = " + max2);

        int max3 = obj.getMostWater_O_N_v2(array);
        System.out.println("max3 = " + max3);
    }
}
