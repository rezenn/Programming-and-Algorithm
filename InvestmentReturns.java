import java.util.PriorityQueue;
public class InvestmentReturns  {
    static class Entry {
        int index1, index2, product;
     
        Entry(int index1, int index2, int product) {
            this.index1 = index1;
            this.index2 = index2;
            this.product = product;
        }
    }
    public static int findKthSmallestProduct(int[] returns1, int[] returns2, int k) {
        int m = returns1.length;
        int n = returns2.length;
        PriorityQueue<Entry> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.product, b.product));
        for (int i = 0; i < m; i++) {
            minHeap.offer(new Entry(i, 0, returns1[i] * returns2[0]));
        }
        Entry curr = null;
        while (k-- > 0) {
            curr = minHeap.poll();
            int i = curr.index1, j = curr.index2;
            if (j + 1 < n) {
                minHeap.offer(new Entry(i, j + 1, returns1[i] * returns2[j + 1]));
            }
        }
        return curr.product;
    }
    public static void main(String[] args) {
        int[] returns1 = {4, 2, 5};
        int[] returns2 = {3, 4};
        int k = 2;
        System.out.println("Output: " + findKthSmallestProduct(returns1, returns2, k)); 
        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        k = 6;
        System.out.println("Output: " + findKthSmallestProduct(returns3, returns4, k));
    } 
}
