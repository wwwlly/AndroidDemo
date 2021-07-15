package com.kemp.demo.arithmetic;

import com.kemp.demo.utils.DebugLog;

/**
 * 排序算法
 */
public class TestSort {

    /**
     * 冒泡排序
     * <p>
     * 它重复地走访过要排序的元素列，一次比较两个相邻的元素，如果他们的顺序（如从大到小、首字母从A到Z）错误就把他们交换过来。
     * 走访元素的工作是重复地进行直到没有相邻元素需要交换，也就是说该元素已经排序完成。
     */
    private static void bubbleSort(int[] arrs) {
        boolean flag = false;
        //为什么是arrs.length - 1趟？比如5个数按照此算法排序好4个数就可以了
        for (int i = 0; i < arrs.length - 1; i++) {
            flag = false;
//            for (int j = arrs.length - 1; j > i; j--) {
//                if (arrs[j] > arrs[j - 1]) {
//                    int temp = arrs[j];
//                    arrs[j] = arrs[j - 1];
//                    arrs[j - 1] = temp;
//                    flag = true;
//                }
//                printArrays(arrs);
//            }

            for (int j = 0; j < arrs.length - 1 - i; j++) {
                if (arrs[j] > arrs[j + 1]) {
                    int temp = arrs[j];
                    arrs[j] = arrs[j + 1];
                    arrs[j + 1] = temp;
                    flag = true;
                }
                printArrays(arrs);
            }
//            if (!flag) {
//                break;
//            }
//            printArrays(arrs);
        }
    }

    /**
     * 选择排序
     * <p>
     * 选择排序（Selection sort）是一种简单直观的排序算法。它的工作原理是每一次从待排序的数据元素中选出最小（或最大）的一个元素，
     * 存放在序列的起始位置，直到全部待排序的数据元素排完。 选择排序是不稳定的排序方法（比如序列[5， 5， 3]第一次就将第一个[5]与[3]交换，
     * 导致第一个5挪动到第二个5后面）。
     */
    private static void selectionSort(int[] arrs) {
        int size = arrs.length;
        for (int i = 0; i < size - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (arrs[j] < arrs[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arrs[i];
            arrs[i] = arrs[minIndex];
            arrs[minIndex] = temp;
            printArrays(arrs);
        }
    }

    /**
     * 插入排序
     * <p>
     * 插入排序的基本操作就是将一个数据插入到已经排好序的有序数据中，从而得到一个新的、个数加一的有序数据，算法适用于少量数据的排序，
     * 时间复杂度为O(n^2)。是稳定的排序方法。
     *
     * @param arrs
     */
    private static void insertSort(int[] arrs) {
        if (arrs == null || arrs.length < 2) {
            return;
        }
        int size = arrs.length;
        for (int i = 1; i < size; i++) {
            DebugLog.d("i = " + i);
            for (int j = i; j > 0; j--) {
                if (arrs[j] < arrs[j - 1]) {
                    int temp = arrs[j];
                    arrs[j] = arrs[j - 1];
                    arrs[j - 1] = temp;

                    printArrays(arrs);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 快速排序
     * 快速排序（Quicksort）是对冒泡排序的一种改进。
     * 它的基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，
     * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
     *
     * @param arrs
     */
    private static void quickSort(int[] arrs, int low, int high) {

        int l = low;
        int h = high;
        int key = arrs[low];
        while (l < h) {

            while (l < h && arrs[h] >= key)//从右向左找第一个小于key的值
                h--;
            if (l < h) {
                arrs[l] = arrs[h];
                l++;
            }

            while (l < h && arrs[l] < key)//从左向右找第一个大于key的值
                l++;
            if (l < h) {
                arrs[h] = arrs[l];
                h--;
            }
            arrs[l] = key;

            DebugLog.d("start l:" + l + ",h:" + h);
            printArrays(arrs);

            quickSort(arrs, low, l - 1);
            quickSort(arrs, l + 1, high);
            DebugLog.d("end l:" + l + ",h:" + h);
        }

    }

    public static void test() {
        int[] arrs = new int[]{25, 45, 56, 12, 5, 89, 33};
        printArrays(arrs);
//        bubbleSort(arrs);
//        selectionSort(arrs);
//        insertSort(arrs);
        quickSort(arrs, 0, arrs.length - 1);
    }

    private static void printArrays(int[] arrs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < arrs.length; i++) {
            if (i == arrs.length - 1) {
                sb.append(arrs[i]);
            } else {
                sb.append(arrs[i]).append(", ");
            }
        }
        sb.append("]").append("\n");
        DebugLog.d(sb.toString());
    }
}
