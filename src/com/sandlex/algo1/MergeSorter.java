package com.sandlex.algo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * This file contains all of the 100,000 integers between 1 and 100,000 (inclusive) in some order,
 * with no integer repeated.
 *
 * Your task is to compute the number of inversions in the file given, where the ith row of the file
 * indicates the ith entry of an array.
 *
 * Because of the large size of this array, you should implement the fast divide-and-conquer algorithm
 * covered in the video lectures.
 *
 *
 * author: Alexey Peskov
 */
public class MergeSorter {

    private static Long inversions;

    public static void main(String[] args) throws IOException {
        Stream<String> stream = Files.lines(Paths.get("/Users/sandlex/dev/algo1/resources/com/sandlex/algo1/MergeSort_IntegerArray.txt"));
        List<Integer> integers = new ArrayList<>();
        stream.mapToInt(Integer::parseInt).forEach(integers::add);
        inversions = 0l;
        recursiveSort(integers);
        System.out.println(inversions);
    }

    private static List<Integer> recursiveSort(List<Integer> ints) {
        if (ints.size() > 1) {
            List<Integer> a = recursiveSort(ints.subList(0, ints.size() / 2));
            List<Integer> b = recursiveSort(ints.subList(ints.size() / 2, ints.size()));
            return merge(a, b);
        }

        return ints;
    }

    private static List<Integer> merge(List<Integer> a, List<Integer> b) {
        List<Integer> res = new ArrayList<>();
        int i = 0, j = 0;

        while (i < a.size() || j < b.size()) {
            if (i == a.size()) {
                res.add(b.get(j));
                j++;
            } else if (j == b.size()) {
                res.add(a.get(i));
                i++;
            } else if (a.get(i) < b.get(j)) {
                res.add(a.get(i));
                i++;
            } else {
                res.add(b.get(j));
                j++;
                inversions += (a.size() - i);
            }
        }
        return res;
    }
}
