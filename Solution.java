import java.util.*;
class Solution {
    private int[] parent;  
    private int[] rank;   
    public Solution(int n) {
        parent = new int[n + 1]; 
        rank = new int[n + 1];   
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
    public int minCost(int n, int[] modules, int[][] connection) {
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{0, i + 1, modules[i]});
        }
        for (int[] conn : connection) {
            edges.add(new int[]{conn[0], conn[1], conn[2]});
        }
        edges.sort(Comparator.comparingInt(a -> a[2]));
        Solution dsu = new Solution(n);
        int totalCost = 0;
        int edgesUsed = 0;
        for (int[] edge : edges) {
            int device1 = edge[0];
            int device2 = edge[1];
            int cost = edge[2];
            if (dsu.find(device1) != dsu.find(device2)) {
                dsu.union(device1, device2);
                totalCost += cost;
                edgesUsed++;
                if (edgesUsed == n) {
                    break;
                }
            }
        }return totalCost;
    }
    public static void main(String[] args) {
        Solution solution = new Solution(3);  
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connection = {
            {1, 2, 1}, 
            {2, 3, 1}
        };
        System.out.println(solution.minCost(n, modules, connection)); 
    }
}
