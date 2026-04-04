package Input;

public class InputGenerator {

    public static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * 10 * size);
        }
        return arr;
    }

    public static int[] generateSemiSortedArray(int size, int percentageUnsorted) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i + 1; // Sorted integers from 1 to size
        }
        // Shuffle a percentage of the array to make it semi-sorted
        for (int i = 0; i < (size * percentageUnsorted / 100); i++) {
            int j = (int) (Math.random() * size);
            int k = (int) (Math.random() * size);
            int temp = arr[j];
            arr[j] = arr[k];
            arr[k] = temp;
        }
        return arr;
    }
}
