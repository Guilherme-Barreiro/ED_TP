package Colecoes.Estruturas;

public class ArraySearch {
    public static <T extends Comparable<? super T>> boolean linearSearch(T[] a, int min, int max, T target) {
        int index = min;
        boolean found = false;
        while (index <= max && !found) {
            if (a[index].compareTo(target) == 0) {
                found = true;
            }
            index++;
        }
        return found;
    }

    public static <T extends Comparable<? super T>> boolean binarySearch(T[] data, int min, int max, T target) {
        boolean found = false;
        int midpoint = (min + max) / 2;
        if (data[midpoint].compareTo(target) == 0)
            found = true;
        else if (data[midpoint].compareTo(target) > 0) {
            if (min <= midpoint - 1)
                found = binarySearch(data, min, midpoint - 1, target);
        } else if (midpoint + 1 <= max)
            found = binarySearch(data, midpoint + 1, max, target);
        return found;
    }
}
