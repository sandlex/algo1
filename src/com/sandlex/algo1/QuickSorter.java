package com.sandlex.algo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * The file contains all of the integers between 1 and 10,000 (inclusive, with no repeats) in unsorted order. The integer
 * in the ith row of the file gives you the ith entry of an input array.
 *
 * Your task is to compute the total number of comparisons used to sort the given input file by QuickSort. As you know,
 * the number of comparisons depends on which elements are chosen as pivots, so we'll ask you to explore three different
 * pivoting rules.
 *
 * You should not count comparisons one-by-one. Rather, when there is a recursive call on a subarray of length m, you
 * should simply add m?1 to your running total of comparisons. (This is because the pivot element is compared to each
 * of the other m?1 elements in the subarray in this recursive call.)
 *
 * WARNING: The Partition subroutine can be implemented in several different ways, and different implementations can
 * give you differing numbers of comparisons. For this problem, you should implement the Partition subroutine exactly
 * as it is described in the video lectures (otherwise you might get the wrong answer).
 *
 * #1
 * For the first part of the programming assignment, you should always use the first element of the array as the pivot element.
 *
 * #2
 * Compute the number of comparisons (as in Problem 1), always using the final element of the given array as the pivot element.
 * Again, be sure to implement the Partition subroutine exactly as it is described in the video lectures.
 *
 * Recall from the lectures that, just before the main Partition subroutine, you should exchange the pivot element (i.e.,
 * the last element) with the first element.
 *
 * #3
 * Compute the number of comparisons (as in Problem 1), using the "median-of-three" pivot rule. [The primary motivation
 * behind this rule is to do a little bit of extra work to get much better performance on input arrays that are nearly
 * sorted or reverse sorted.] In more detail, you should choose the pivot as follows. Consider the first, middle, and
 * final elements of the given array. (If the array has odd length it should be clear what the "middle" element is; for
 * an array with even length 2k, use the kth element as the "middle" element. So for the array 4 5 6 7, the "middle"
 * element is the second one ---- 5 and not 6!) Identify which of these three elements is the median (i.e., the one whose
 * value is in between the other two), and use this as your pivot. As discussed in the first and second parts of this
 * programming assignment, be sure to implement Partition exactly as described in the video lectures (including exchanging
 * the pivot element with the first element just before the main Partition subroutine).
 *
 * EXAMPLE: For the input array 8 2 4 5 7 1 you would consider the first (8), middle (4), and last (1) elements; since 4
 * is the median of the set {1,4,8}, you would use 4 as your pivot element.
 *
 * SUBTLE POINT: A careful analysis would keep track of the comparisons made in identifying the median of the three
 * candidate elements. You should NOT do this. That is, as in the previous two problems, you should simply add m?1 to
 * your running total of comparisons every time you recurse on a subarray with length m.
 *
 * author: Alexey Peskov
 */
public class QuickSorter {

    enum PivotType {
        FIRST, LAST, MEDIAN
    }

    private static Long inversions;

    public static void main(String[] args) throws IOException {
        recursiveSort(readDataSet(), PivotType.FIRST);
        System.out.println(inversions == 162085);

        recursiveSort(readDataSet(), PivotType.LAST);
        System.out.println(inversions == 164123);

        recursiveSort(readDataSet(), PivotType.MEDIAN);
        System.out.println(inversions == 138382);
    }

    private static List<Integer> readDataSet() throws IOException {
        Stream<String> stream = Files.lines(Paths.get("/Users/sandlex/dev/algo1/resources/com/sandlex/algo1/QuickSort_IntegerArray.txt"));
        List<Integer> integers = new ArrayList<>();
        stream.mapToInt(Integer::parseInt).forEach(integers::add);
        inversions = 0l;
        return integers;
    }

    private static void recursiveSort(List<Integer> ints, PivotType pivotType) {
        if (ints.size() > 1) {
            inversions += (ints.size() - 1);

            switch (pivotType) {
                case FIRST:
                    break;
                case LAST:
                    Collections.swap(ints, 0, ints.size() - 1);
                    break;
                case MEDIAN:
                    findAndSwapMedianPivotElement(ints);
                    break;
            }

            int i = 1, j = 0;
            int pivot = ints.get(0);
            while (++j < ints.size()) {
                if (ints.get(j) < pivot) {
                    Collections.swap(ints, i, j);
                    i++;
                }
            }
            Collections.swap(ints, 0, i - 1);

            recursiveSort(ints.subList(0, i - 1), pivotType);
            recursiveSort(ints.subList(i, ints.size()), pivotType);
        }
    }

    private static void findAndSwapMedianPivotElement(List<Integer> ints) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        map.put(ints.get(0), 0);
        map.put(ints.get(ints.size() - 1), ints.size() - 1);
        if (ints.size() % 2 == 0) {
            map.put(ints.get(ints.size() / 2 - 1), ints.size() / 2 - 1);
        } else {
            map.put(ints.get(ints.size() / 2), ints.size() / 2);
        }
        if (map.size() == 3) {
            Iterator<Integer> it = map.values().iterator();
            it.next();
            Collections.swap(ints, 0, it.next());
        }
    }

}

