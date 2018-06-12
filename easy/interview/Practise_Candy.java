package com.stevenli.interview.easy.interview;

public class Practise_Candy {

    public int[] deliverCandy(int[] array) {
        int[] ret = new int[array.length];
        int count = 0;

        if(array == null || array.length == 0)
            return ret;

        //greedy algorithm
        //scan from left to right, be sure that all the right guys
        //has the higher rating will have 1 more candy than the left
        //guy
        int index = 0;
        while(index < array.length) {
            if(index == 0) {
                ret[index] = 1;
            } else if(array[index] > array[index-1]) {
                ret[index] = ret[index-1] + 1;
            } else {
                ret[index]  = 1;
            }

            count += ret[index];
            index++;
        }

        index = array.length - 1;
        while(index >= 0) {
            if(index == array.length - 1) {
                ret[index] += 1;
            } else if(array[index] > array[index + 1]) {
                ret[index] = ret[index + 1] +1;
            } else {
                ret[index] += 1;
            }

            count += ret[index];
            index--;
        }

        System.out.println("count = " + count);
        return ret;

    }

    public static void main(String[] args) {
        Practise_Candy practiseCandy = new Practise_Candy();

        int array[] = {2, 1, 1, 4, 7, 9, 4, 3, 2, 9, 1};
        int[] ret = practiseCandy.deliverCandy(array);
        for(int i : array) {
            System.out.print(i + " ");
        }
        System.out.print("\n");

        for(int i : ret) {
            System.out.print(i + " ");
        }
        System.out.print("\n");
    }
}
