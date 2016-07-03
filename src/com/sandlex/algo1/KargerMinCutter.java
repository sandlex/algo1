package com.sandlex.algo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * The file contains the adjacency list representation of a simple undirected graph. There are 200 vertices labeled 1
 * to 200. The first column in the file represents the vertex label, and the particular row (other entries except the
 * first column) tells all the vertices that the vertex is adjacent to. So for example, the 6th row looks like :
 * "6	155	56	52	120	......". This just means that the vertex with label 6 is adjacent to (i.e., shares an edge with)
 * the vertices with labels 155,56,52,120,......,etc
 *
 * Your task is to code up and run the randomized contraction algorithm for the min cut problem and use it on the above
 * graph to compute the min cut. (HINT: Note that you'll have to figure out an implementation of edge contractions.
 * Initially, you might want to do this naively, creating a new graph from the old every time there's an edge contraction.
 * But you should also think about more efficient implementations.) (WARNING: As per the video lectures, please make sure
 * to run the algorithm many times with different random seeds, and remember the smallest cut that you ever find.)
 *
 * author: Alexey Peskov
 */
public class KargerMinCutter {

    private static Random rand = new Random();

    public static void main(String[] args) throws IOException {
        SortedSet<Integer> results = new TreeSet<>();
        int iterations = 0;
        while (iterations++ < 100) {
            results.add(executeTest());
        }

        System.out.println(results.first() == 17);
    }

    private static int executeTest() throws IOException {
        Map<Integer, List<Integer>> graph = readDataSet();
        while (graph.size() > 2) {
            Integer first = new ArrayList<>(graph.keySet()).get(rand.nextInt(graph.size()));
            Integer second = graph.get(first).get(rand.nextInt(graph.get(first).size()));
            graph.get(first).addAll(graph.get(second));
            graph.forEach((key, con) -> {
                while (con.contains(second)) {
                    con.set(con.indexOf(second), first);

                }
            });
            graph.get(first).removeAll(Collections.singletonList(first));
            graph.remove(second);
        }

        return graph.values().iterator().next().size();
    }

    private static Map<Integer, List<Integer>> readDataSet() throws IOException {
        Stream<String> stream = Files.lines(Paths.get("/Users/sandlex/dev/algo1/resources/com/sandlex/algo1/KargerMinCut.txt"));
        Map<Integer, List<Integer>> graph = new HashMap<>();
        stream.forEach(s -> {
            String[] arr = s.split("\t");
            List<Integer> connections = new ArrayList<>();
            Integer node = -1;
            for (int i = 0; i < arr.length; i++) {
                if (i == 0) {
                    node = Integer.valueOf(arr[i]);
                } else {
                    connections.add(Integer.valueOf(arr[i]));
                }
            }
            graph.put(node, connections);
        });

        return graph;
    }
}
