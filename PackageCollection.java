import java.util.*;
class PackageCollection {
    public static int minTraversal(int[] packages, int[][] roads) {
        int n = packages.length;
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }
        Set<Integer> packageNodes = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                packageNodes.add(i);
            }
        }
        if (packageNodes.isEmpty()) {
            return 0;
        }
        return bfs(0, graph, packageNodes) * 2;
    }
    private static int bfs(int start, Map<Integer, List<Integer>> graph, Set<Integer> packageNodes) {
        Queue<int[]> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(new int[]{start, 0}); 
        int edgesUsed = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0];
            int dist = current[1];
            if (visited.contains(node)) {
                continue;
            }
            visited.add(node);
            if (packageNodes.contains(node)) {
                edgesUsed += dist;
            }
            for (int neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    queue.add(new int[]{neighbor, dist + 1});
                }
            }
        }

        return edgesUsed;
    }

    public static void main(String[] args) {
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println("Minimum distance traveled for example 1: "+ minTraversal(packages1, roads1)); 

        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {6, 5}, {5, 7}};
        System.out.println("Minimum distance traveled for example 2: "+ minTraversal(packages2, roads2)); 
}
}