package com.sandlex.algo1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * The file contains the edges of a directed graph. Vertices are labeled as positive integers from 1 to 875714. Every
 * row indicates an edge, the vertex label in first column is the tail and the vertex label in second column is the
 * head (recall the graph is directed, and the edges are directed from the first column vertex to the second column
 * vertex). So for example, the 11th row looks liks : "2 47646". This just means that the vertex with label 2 has an
 * outgoing edge to the vertex with label 47646
 *
 * Your task is to code up the algorithm from the video lectures for computing strongly connected components (SCCs),
 * and to run this algorithm on the given graph.
 *
 * Output Format: You should output the sizes of the 5 largest SCCs in the given graph, in decreasing order of sizes,
 * separated by commas (avoid any spaces). So if your algorithm computes the sizes of the five largest SCCs to be 500,
 * 400, 300, 200 and 100, then your answer should be "500,400,300,200,100" (without the quotes). If your algorithm finds
 * less than 5 SCCs, then write 0 for the remaining terms. Thus, if your algorithm computes only 3 SCCs whose sizes are
 * 400, 300, and 100, then your answer should be "400,300,100,0,0" (without the quotes). (Note also that your answer
 * should not have any spaces in it.)
 *
 * WARNING: This is the most challenging programming assignment of the course. Because of the size of the graph you may
 * have to manage memory carefully. The best way to do this depends on your programming language and environment, and
 * we strongly suggest that you exchange tips for doing this on the discussion forums.
 *
 * kosaraju.txt Answer: 3,3,3,0,0
 * kosaraju1.txt Answer: 3,3,3,0,0
 * kosaraju2.txt Answer: 3,3,2,0,0
 * kosaraju3.txt Answer: 3,3,1,1,0
 * kosaraju4.txt Answer: 7,1,0,0,0
 * kosaraju5.txt Answer: 6,3,2,1,0
 *
 * author: Alexey Peskov
 */
public class KosarajuSolver {

    public static void main(String[] args) throws IOException {
        String path = "/Users/sandlex/dev/algo1/resources/com/sandlex/algo1/kosaraju5.txt";
        String separator = " ";

        List<Integer> scc = new KosarajuSolver().solve(path, separator);
        System.out.println(scc.size());
    }

    private class NodeInfo {
        private List<Integer> heads = new ArrayList<>();
        private boolean isVisited = false;
        private int order;

        public NodeInfo(int head) {
            heads.add(head);
        }

        public NodeInfo() {
        }

        public void addHead(int head) {
            heads.add(head);
        }

        public void markVisited() {
            isVisited = true;
        }

        public List<Integer> getHeads() {
            return heads;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

        public boolean isVisited() {
            return isVisited;
        }
    }

    private int currentOrder = 0;
    private int currentNumberOfNodes = 0;

    public List<Integer> solve(String path, String separator) throws IOException {
        Map<Integer, NodeInfo> invertedGraph = buildGraph(path, separator, null);
        for (int i = invertedGraph.size(); i > 0; i--) {
            dfsLoop(invertedGraph, i, true);
        }

        Map<Integer, NodeInfo> secondGraph = buildGraph(path, separator, invertedGraph);
        invertedGraph.clear();

        List<Integer> sccs = new ArrayList<>();
        for (int i = secondGraph.size(); i > 0; i--) {
            currentNumberOfNodes = 0;
            dfsLoop(secondGraph, i, false);
            if (currentNumberOfNodes > 0) {
                sccs.add(currentNumberOfNodes);
            }
        }

        sccs.sort((Integer o1, Integer o2) -> o2 - o1);

        return sccs;
    }

    private void dfsLoop(Map<Integer, NodeInfo> graph, int startingNode, boolean isFirstPass) {
        NodeInfo node = graph.get(startingNode);
        if (node.isVisited()) {
            return;
        }
        node.markVisited();
        for (Integer head : node.getHeads()) {
            dfsLoop(graph, head, isFirstPass);
        }

        if (isFirstPass) {
            node.setOrder(++currentOrder);
        } else {
            currentNumberOfNodes++;
        }
    }

    private Map<Integer, NodeInfo> buildGraph(String path, String separator, Map<Integer, NodeInfo> invertedGraph) throws IOException {
        Stream<String> stream = Files.lines(Paths.get(path));
        Map<Integer, NodeInfo> graph = new TreeMap<>();
        stream.forEach(s -> {
            String[] arr = s.split(separator);

            Integer tail, head;
            if (invertedGraph == null) {
                tail = Integer.valueOf(arr[1]);
                head = Integer.valueOf(arr[0]);
            } else {
                tail = invertedGraph.get(Integer.valueOf(arr[0])).getOrder();
                head = invertedGraph.get(Integer.valueOf(arr[1])).getOrder();
            }

            if (!graph.containsKey(tail)) {
                graph.put(tail, new NodeInfo(head));
            } else {
                graph.get(tail).addHead(head);
            }

            // handle hanging node
            if (!graph.containsKey(head)) {
                graph.put(head, new NodeInfo());
            }
        });

        return graph;
    }

}
