package com.stevenli.interview.easy.interview;

public class Practise_RDN {

    public int removeDuplicatedNumber1(int[] array) {

        if (array.length == 0) {
            return 0;
        } else if(array.length == 1) {
            return 1;
        }

        int newArrayEnd = 0;
        int iterateIndex = 1;
        while(iterateIndex < array.length) {
            if(array[newArrayEnd] == array[iterateIndex]) {
                iterateIndex++;
            } else {
                array[++newArrayEnd] = array[iterateIndex++];
            }
        }
        return newArrayEnd+1;
    }


    // allow maximum duplicats K times
    private int removeDuplicatedNumber2(int[] array, int duplicateFactor) {
        if(array.length == 0)
            return 0;

        int count = 1;
        int newArrayEnd = 0;
        int iterateIndex = 0;
        while(iterateIndex < array.length) {
            if(iterateIndex != 0) {
                if(array[newArrayEnd] == array[iterateIndex]) {
                    if(count++ < duplicateFactor) {
                        array[++newArrayEnd] = array[iterateIndex++];
                    } else {
                        iterateIndex++;
                    }
                } else {
                    array[++newArrayEnd] = array[iterateIndex++];
                    count=1;
                }
            } else {
                iterateIndex++;
            }
        }

        return newArrayEnd+1;
    }


    private int removeDuplicatedNumber1_v2(int[] array) {
        if(array.length == 0)
            return 0;

        int newArrayEnd = 0;
        int iterateIndex = 0;
        while(iterateIndex < array.length) {
            if(iterateIndex != 0) {
                if(array[newArrayEnd] == array[iterateIndex]) {
                    iterateIndex++;
                } else {
                    array[++newArrayEnd] = array[iterateIndex++];
                }
            } else {
                iterateIndex++;
            }
        }

        return newArrayEnd+1;
    }

    private int removeNumber(int[] array, int value) {
        int end = 0;
        int index = 0;
        while(index < array.length) {
            if(array[index]  == value) {
                index++;
            } else {
                if(end == index) {
                    end++; index++;
                } else {
                    array[end++] = array[index++];
                }
            }
        }

        return end;
    }

    private void moveZeros(int[] array) {
        int len = removeNumber(array, 0);
        System.out.println("len = " + len);
        int index = len;
        while(index < array.length) {
            array[index++] = 0;
        }
    }

    public static void main(String args[]) {
        Practise_RDN processor = new Practise_RDN();

        //int[] array1 = {1, 1, 2, 3, 4, 4, 4, 5, 5, 6, 7, 7, 9, 10, 11, 11};
        //int[] array1 = {1, 1};
        //int len_1 = processor.moveZeros(array1);

        int[] array1 = {0, 1, 0, 5, 6, 4, 0};
        processor.moveZeros(array1);

        for(int i : array1) {
            System.out.print(i + " ");
        }

    }
}
