import java.util.*;

class TrendingHashtags {
    public static List<Map.Entry<String, Integer>> topTrendingHashtags(List<String[]> tweets) {
        Map<String, Integer> hashtagCount = new HashMap<>();

        for (String[] tweetData : tweets) {
            String tweet = tweetData[2];
            String date = tweetData[3];

            if (!date.startsWith("2024-02")) continue;

            String[] words = tweet.split(" ");
            for (String word : words) {
                if (word.startsWith("#")) {
                    hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1);
                }
            }
        }
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());
        sortedHashtags.sort((a, b) -> b.getValue().equals(a.getValue()) ? a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue());
        return sortedHashtags.subList(0, Math.min(3, sortedHashtags.size()));
    }
    public static void main(String[] args) {
        List<String[]> tweets = Arrays.asList(
            new String[]{"135", "13", "Enjoying a great start to the day. #HappyDay #MorningVibes", "2024-02-01"},
            new String[]{"136", "14", "Another #HappyDay with good vibes! #FeelGood", "2024-02-03"},
            new String[]{"137", "15", "Productivity peaks! #WorkLife #ProductiveDay", "2024-02-04"},
            new String[]{"138", "16", "Exploring new tech frontiers. #TechLife #Innovation", "2024-02-04"},
            new String[]{"139", "17", "Gratitude for today's moments. #HappyDay #Thankful", "2024-02-05"},
            new String[]{"140", "18", "Innovation drives us. #TechLife #FutureTech", "2024-02-07"},
            new String[]{"141", "19", "Connecting with nature's serenity. #Nature #Peaceful", "2024-02-09"}
        );
        List<Map.Entry<String, Integer>> result = topTrendingHashtags(tweets);
        System.out.println("Output:");
        System.out.println("+-----------+-------+");
        System.out.println("| hashtag   | count |");
        System.out.println("+-----------+-------+");
        for (Map.Entry<String, Integer> entry : result) {
            System.out.printf("| %-9s | %5d |\n", entry.getKey(), entry.getValue());
        }
        System.out.println("+-----------+-------+");
    }
}
