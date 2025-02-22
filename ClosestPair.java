public class ClosestPair {
    public static int[] closestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE;
        int[] closestPairIndices = new int[2];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int distance = Math.abs(x_coords[i] - x_coords[j]) + 
                Math.abs(y_coords[i] - y_coords[j]);

                if (distance < minDistance || (distance == minDistance
                        && (i < closestPairIndices[0] || 
                        (i == closestPairIndices[0] && j < closestPairIndices[1])))) {
                    minDistance = distance;
                    closestPairIndices[0] = i;
                    closestPairIndices[1] = j;
                }
            }
        }

        return closestPairIndices;
    }

    public static void main(String[] args) {
        int[] x_coords = { 1, 2, 3, 2, 4 };
        int[] y_coords = { 2, 3, 1, 2, 3 };

        int[] result = closestPair(x_coords, y_coords);
        System.out.println("Closest pair indices: [" + result[0] + ", " + result[1] + "]");
    }
}
