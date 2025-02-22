import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.*;
import java.util.regex.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
public class WebCrawler {
    private final ExecutorService executorService;
    private final ConcurrentLinkedQueue<String> urlQueue;
    private final HashSet<String> visitedUrls;
    private final int maxDepth;
    public WebCrawler(int numThreads, int maxDepth) {
        this.executorService = Executors.newFixedThreadPool(numThreads);
        this.urlQueue = new ConcurrentLinkedQueue<>();
        this.visitedUrls = new HashSet<>();
        this.maxDepth = maxDepth;
    }
    public void startCrawling(String startUrl) {
        urlQueue.add(startUrl);
        crawl(0);
    }
    private void crawl(int depth) {
        if (depth > maxDepth) return;
        int batchSize = urlQueue.size();
        CountDownLatch latch = new CountDownLatch(batchSize);
        for (int i = 0; i < batchSize; i++) {
            String url = urlQueue.poll();
            if (url != null && visitedUrls.add(url)) {
                executorService.execute(() -> {
                    fetchAndProcess(url);
                    latch.countDown();
                });
            } else {
                latch.countDown();
            }
        }
        try {
            latch.await();  
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        crawl(depth + 1);
    }
    private void fetchAndProcess(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println("Crawled: " + url);
            extractLinks(doc);
        } catch (IOException e) {
            System.err.println("Failed to fetch: " + url);
        }
    }
    private void extractLinks(Document doc) {
        Elements links = doc.select("a[href]");
        for (var link : links) {
            String nextUrl = link.absUrl("href");
            if (isValidUrl(nextUrl)) {
                urlQueue.add(nextUrl);
            }
        }
    }
    private boolean isValidUrl(String url) {
        String regex = "^(http|https)://.*";
        return Pattern.matches(regex, url) && !visitedUrls.contains(url);
    }
    public void shutdown() {
        executorService.shutdown();
    }
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler(5, 2);
        crawler.startCrawling("https://example.com");
        crawler.shutdown();
    }
}
