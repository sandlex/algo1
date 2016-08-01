package com.sandlex.algo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * The goal of this problem is to implement the "Median Maintenance" algorithm (covered in the Week 5 lecture on heap
 * applications). The text file contains a list of the integers from 1 to 10000 in unsorted order; you should treat this
 * as a stream of numbers, arriving one by one. Letting xi denote the ith number of the file, the kth median mk is
 * defined as the median of the numbers x1,…,xk. (So, if k is odd, then mk is ((k+1)/2)th smallest number among x1,…,xk;
 * if k is even, then mk is the (k/2)th smallest number among x1,…,xk.)
 *
 * In the box below you should type the sum of these 10000 medians, modulo 10000 (i.e., only the last 4 digits). That is,
 * you should compute (m1+m2+m3+⋯+m10000)mod10000.
 *
 * OPTIONAL EXERCISE: Compare the performance achieved by heap-based and search-tree-based implementations of the algorithm.
 *
 * Test1:
 * data = [9, 9, 7, 1, 2, 3, 4, 5, 6, 7, 8, 9]
 * answer = [9, 9, 9, 7, 7, 3, 4, 4, 5, 5, 6, 6]
 *
 * Test2:
 * data = [2, 8, 9, 7, 3, 1, 4]
 * answer = [2, 2, 8, 7, 7, 3, 4]
 *
 * author: Alexey Peskov
 */
public class MedianMaintenanceSolver {

    public static void main(String[] args) throws IOException {
        List<Integer> data = readDataSet();

        //TODO Change if input elements are not distinct (Test1)
        SortedSet<Integer> setMin = new TreeSet<>();
        SortedSet<Integer> setMax = new TreeSet<>();

        int sum = 0;
        for (int val : data) {
            setMin.add(val);
            if (setMin.size() - setMax.size() > 1) {
                setMax.add(setMin.last());
                setMin.remove(setMin.last());
            }
            if (!setMin.isEmpty() && !setMax.isEmpty() && setMin.last() > setMax.first()) {
                setMin.add(setMax.first());
                setMax.add(setMin.last());
                setMax.remove(setMax.first());
                setMin.remove(setMin.last());
            }

            sum += setMin.last();
        }

        System.out.println( (sum % 10000) == 1213);
    }


    private static List<Integer> readDataSet() throws IOException {
        Stream<String> stream = Files.lines(Paths.get("/Users/sandlex/dev/algo1/resources/com/sandlex/algo1/Median.txt"));
        List<Integer> integers = new ArrayList<>();
        stream.mapToInt(Integer::parseInt).forEach(integers::add);
        return integers;
    }

}
