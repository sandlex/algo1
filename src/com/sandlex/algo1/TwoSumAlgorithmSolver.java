package com.sandlex.algo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * The goal of this problem is to implement a variant of the 2-SUM algorithm (covered in the Week 6 lecture on hash
 * table applications).
 *
 * The file contains 1 million integers, both positive and negative (there might be some repetitions!).This is your
 * array of integers, with the ith row of the file specifying the ith entry of the array.
 *
 * Your task is to compute the number of target values t in the interval [-10000,10000] (inclusive) such that there are
 * distinct numbers x,y in the input file that satisfy x+y=t. (NOTE: ensuring distinctness requires a one-line addition
 * to the algorithm from lecture.)
 *
 * Write your numeric answer (an integer between 0 and 20001) in the space provided.
 *
 * OPTIONAL CHALLENGE: If this problem is too easy for you, try implementing your own hash table for it. For example,
 * you could compare performance under the chaining and open addressing approaches to resolving collisions.
 *
 * author: Alexey Peskov
 */
public class TwoSumAlgorithmSolver {

    public static void main(String[] args) throws IOException {
        HashSet<Long> dataSet = readDataSet();
        int answer = 0;

        for (int targetValue = -10000; targetValue <= 10000; targetValue++) {
            if (pairExists(dataSet, targetValue)) {
                answer++;
            }
        }

        System.out.println(answer == 427);
    }

    private static boolean pairExists(HashSet<Long> dataSet, int targetValue) {
        for (Long val : dataSet) {
            if (dataSet.contains(targetValue - val)) {
                return true;
            }
        }

        return false;
    }

    private static HashSet<Long> readDataSet() throws IOException {
        Stream<String> stream = Files.lines(Paths.get("/Users/sandlex/dev/algo1/resources/com/sandlex/algo1/2sum.txt"));
        HashSet<Long> set = new LinkedHashSet<>();
        stream.forEach(s -> set.add(new Long(s)));

        return set;
    }
}
