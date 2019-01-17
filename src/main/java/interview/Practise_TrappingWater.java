package interview;

public class Practise_TrappingWater {

    //scan the array from left to right
    //find each separate container
    public int calculate_v1(int[] array) {
        int maxHigh = 0;
        int maxHighIndex = 0;
        for(int i=0; i<array.length; i++)
        {
            if(array[i] > maxHigh) {
                maxHigh = array[i];
                maxHighIndex = i;
            }
        }

        int count = 0;
        int leftBar = 0;
        for(int index=0; index<maxHighIndex; index++){
            if(array[index] < leftBar) {
                count += leftBar - array[index];
            } else if (array[index] > leftBar) {
                leftBar = array[index];
            }
        }

        int rightBar = 0;
        for(int index = array.length-1; index>maxHighIndex; index--) {
            if(array[index] < rightBar) {
                count += rightBar - array[index];
            } else if(array[index] > rightBar) {
                rightBar = array[index];
            }
        }

        return count;
    }

    //scan from both left and right
    //move the shorter bar and counter the water
    public int calculate_v2(int[] array) {
        int left = 0;
        int right = array.length - 1;
        int count = 0;

        while(left != right) {
            if(array[left] <= array[right]) {
                //move the left bar, the shorter bar
                int currentBarValue = array[left++];
                while(left != right) {
                    if(currentBarValue >= array[left]) {
                        //lower bar, calculate the water
                        count += currentBarValue - array[left];
                        left++;
                    } else {
                        //find a higher bar
                        break;
                    }
                }
            } else {
                //move the right bar
                int currentBarValue = array[right--];
                while(right != left) {
                    if(currentBarValue >= array[right]) {
                        //lower bar, calculate the water
                        count += currentBarValue - array[right];
                        right--;
                    } else {
                        //find a higher bar
                        break;
                    }
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        Practise_TrappingWater practiseTrappingWater = new Practise_TrappingWater();

        //int array[] = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        //int array[] = {0, 3, 0, 0, 4, 6, 0, 2};
        //int array[] = {2, 0, 4, 0, 5, 4, 3, 1, 0, 3};
        //int array[] = {1, 2, 3, 4, 9, 4, 3};
        int array[] = {1, 2, 3, 4, 0, 2, 4, 0};
        int ret1 = practiseTrappingWater.calculate_v1(array);
        System.out.println("ret1 = " + ret1);

        int ret2 = practiseTrappingWater.calculate_v2(array);
        System.out.println("ret2 = " + ret2);
    }
}
