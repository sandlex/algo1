package com.sandlex.algo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * In this programming problem you'll code up Dijkstra's shortest-path algorithm.
 *
 * The file contains an adjacency list representation of an undirected weighted graph with 200 vertices labeled 1 to 200.
 * Each row consists of the node tuples that are adjacent to that particular vertex along with the length of that edge.
 * For example, the 6th row has 6 as the first entry indicating that this row corresponds to the vertex labeled 6. The
 * next entry of this row "141,8200" indicates that there is an edge between vertex 6 and vertex 141 that has length 8200.
 * The rest of the pairs of this row indicate the other vertices adjacent to vertex 6 and the lengths of the corresponding
 * edges.
 *
 * Your task is to run Dijkstra's shortest-path algorithm on this graph, using 1 (the first vertex) as the source vertex,
 * and to compute the shortest-path distances between 1 and every other vertex of the graph. If there is no path between
 * a vertex v and vertex 1, we'll define the shortest-path distance between 1 and v to be 1000000.
 *
 * You should report the shortest-path distances to the following ten vertices, in order: 7,37,59,82,99,115,133,165,188,197.
 * You should encode the distances as a comma-separated string of integers. So if you find that all ten of these vertices
 * except 115 are at distance 1000 away from vertex 1 and 115 is 2000 distance away, then your answer should be
 * 1000,1000,1000,1000,1000,2000,1000,1000,1000,1000. Remember the order of reporting DOES MATTER, and the string should
 * be in the same order in which the above ten vertices are given. The string should not contain any spaces. Please type
 * your answer in the space provided.
 *
 * IMPLEMENTATION NOTES: This graph is small enough that the straightforward O(mn) time implementation of Dijkstra's
 * algorithm should work fine. OPTIONAL: For those of you seeking an additional challenge, try implementing the heap-based
 * version. Note this requires a heap that supports deletions, and you'll probably need to maintain some kind of mapping
 * between vertices and their positions in the heap.
 */
public class DijkstraSolver {

    public static void main(String[] args) throws IOException {
        String path = "/Users/sandlex/dev/algo1/resources/com/sandlex/algo1/dijkstraData.txt";
        String separator = "\\t";
        int endNode = 197;
        Map<Integer, NodeInfo> graph = new DijkstraSolver().solve(path, separator, endNode);

        String result = String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d",
                graph.get(7).shortestDistance, graph.get(37).shortestDistance, graph.get(59).shortestDistance,
                graph.get(82).shortestDistance, graph.get(99).shortestDistance, graph.get(115).shortestDistance,
                graph.get(133).shortestDistance, graph.get(165).shortestDistance, graph.get(188).shortestDistance,
                graph.get(197).shortestDistance);

        System.out.println("2599,2610,2947,2052,2367,2399,2029,2442,2505,3068".equals(result));
    }

    private class NodeInfo {

        private class Head {
            private int node;
            private int distance;

            public Head(int head, int distance) {
                this.node = head;
                this.distance = distance;
            }
        }

        private List<Head> heads = new ArrayList<>();
        private int shortestDistance;

        public void addHead(Integer node, Integer distance) {
            heads.add(new Head(node, distance));
        }

    }

    public Map<Integer, NodeInfo> solve(String path, String separator, int endNode) throws IOException {
        Map<Integer, NodeInfo> graph = buildGraph(path, separator);
        Set<Integer> visited = new HashSet<>();
        visited.add(1);

        while (!visited.contains(endNode)) {
            int closestHead = 0;
            int distanceToClosestHead = Integer.MAX_VALUE;
            for (Integer nodeNum : visited) {
                NodeInfo node = graph.get(nodeNum);

                for (NodeInfo.Head head : node.heads) {
                    if (!visited.contains(head.node)) {
                        if (node.shortestDistance + head.distance < distanceToClosestHead) {
                            closestHead = head.node;
                            distanceToClosestHead = node.shortestDistance + head.distance;
                        }
                    }
                }
            }

            visited.add(closestHead);
            graph.get(closestHead).shortestDistance = distanceToClosestHead;
        }

        return graph;
    }

    private Map<Integer, NodeInfo> buildGraph(String path, String separator) throws IOException {
        Stream<String> stream = Files.lines(Paths.get(path));
        Map<Integer, NodeInfo> graph = new HashMap<>();
        stream.forEach(s -> {
            String[] arr = s.split(separator);
            NodeInfo nodeInfo = new NodeInfo();
            for (int i = 1; i < arr.length; i++) {
                String[] pair = arr[i].split(",");
                nodeInfo.addHead(Integer.valueOf(pair[0]), Integer.valueOf(pair[1]));
            }

            graph.put(Integer.valueOf(arr[0]), nodeInfo);
        });

        return graph;
    }

}