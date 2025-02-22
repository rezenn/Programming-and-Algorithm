public class CriticalTemp {

    public static void main(String[] args) {
        int k = 15;
        int n = 35;
        
        int[][] dp = new int[k + 1][n + 1];

        for(int i = 1; i <= k; i++){
            dp[i][0] = 0;
            dp[i][1] = 1;
        }
        for(int j = 1; j <= n; j++){
            dp[1][j] = j;
        }
        for(int i = 2; i <= k; i++){
            for(int j = 2; j <= n; j++){
                dp[i][j] = Integer.MAX_VALUE;
                for (int x = 1; x <= j; x++) {
                    int cost = 1 + Math.max(dp[i - 1][x - 1], dp[i][j - x]);
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }

        }
        System.out.println("The minimum measurement required is: " + dp[k][n]);
    }
}

