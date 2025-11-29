package Colecoes.Estruturas;

public class Sorting {
    public static <T extends Comparable<? super T>> void selectionSort(T[] data) {
        int min;
        T temp;
        for (int i = 0; i < data.length - 1; i++) {
            min = i;
            for (int j = i + 1; j < data.length; j++) {
                if (data[j].compareTo(data[min]) < 0) {
                    min = j;
                }
            }
            temp = data[min];
            data[min] = data[i];
            data[i] = temp;
        }
    }

    public static <T extends Comparable<? super T>> void insertionSort(T[] data) {
        for (int i = 1; i < data.length; i++) {
            T key = data[i];
            int position = i;
            while (position > 0 && data[position - 1].compareTo(key) > 0) {
                data[position] = data[position - 1];
                position--;
            }
            data[position] = key;
        }
    }

    public static <T extends Comparable<? super T>> void bubbleSort(T[] data) {
        int position, scan;
        T temp;
        for (position = data.length - 1; position >= 0; position--) {
            for (scan = 0; scan <= position - 1; scan++) {
                if (data[scan].compareTo(data[scan + 1]) > 0) {
                    temp = data[scan];
                    data[scan] = data[scan + 1];
                    data[scan + 1] = temp;
                }
            }
        }
    }

    public static <T extends Comparable<? super T>> void quickSort(T[] data, int min, int max) {
        if (min >= max) return;
        int p = findPartition(data, min, max);
        quickSort(data, min, p);
        quickSort(data, p + 1, max);
    }

    private static <T extends Comparable<? super T>> int findPartition(T[] data, int min, int max) {
        int left = min, right = max;
        T partitionelement = data[(min + max) / 2];

        while (true) {

            while (data[left].compareTo(partitionelement) < 0) left++;

            while (data[right].compareTo(partitionelement) > 0) right--;

            if (left >= right) return right;

            T temp = data[left];
            data[left] = data[right];
            data[right] = temp;
            left++;
            right--;
        }
    }


    public static <T extends Comparable<? super T>> void mergeSort(T[] data, int min, int max) {
        T[] temp;
        int index1, left, right;

        if (min == max) {
            return;
        }
        int size = max - min + 1;
        int pivot = (min + max) / 2;
        temp = (T[]) new Comparable[size];
        mergeSort(data, min, pivot);
        mergeSort(data, pivot + 1, max);

        for (index1 = 0; index1 < size; index1++) {
            temp[index1] = data[min + index1];
        }

        left = 0;
        right = pivot - min + 1;
        for (index1 = 0; index1 < size; index1++) {
            if (right <= max - min) {
                if (left <= pivot - min) {
                    if (temp[left].compareTo(temp[right]) > 0) {
                        data[index1 + min] = temp[right++];
                    } else {
                        data[index1 + min] = temp[left++];
                    }
                } else {
                    data[index1 + min] = temp[right++];
                }
            } else {
                data[index1 + min] = temp[left++];
            }
        }
    }
}
