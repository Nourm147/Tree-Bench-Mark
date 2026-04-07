package Algorithms;

import java.util.Random;

public class QuickSort {

    public void performSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private void quickSort(int[] arr, int start, int end) {

        if (start < end) {

            int finalPos = partition(arr, start, end);

            quickSort(arr, start, finalPos - 1);
            quickSort(arr, finalPos + 1, end);
        }
    }

    private int getPivot(int start, int end) {
        return new Random().nextInt(end - start + 1) + start;
    }

    private int partition(int[] arr, int start, int end) {
        int pivot = getPivot(start, end);
        if (pivot != end) {
            swap(arr, pivot, end); // swap with end to start partition
        }

        int i = start;

        for (int j = start; j < end; j++) {
            if (arr[j] < arr[end]) {
                if (i != j) {
                    swap(arr, i, j);
                }
                i++;
            }
        }
        if (i != end) {
            swap(arr, i, end); // get the element in its correct place
        }

        return i;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
