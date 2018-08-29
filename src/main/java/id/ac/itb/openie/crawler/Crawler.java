package id.ac.itb.openie.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by elvanowen on 2/22/17.
 */
public class Crawler extends WebCrawler implements ICrawlerPipelineElement {

    private static Crawler currentlyRunningCrawler = null;
    private static ArrayList<String> outputDirectories = new ArrayList<>(Arrays.asList(System.getProperty("user.dir") + File.separator + new Config().getProperty("CRAWLER_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
    private ICrawlerHandler crawlerHandler = null;
    private int totalDocumentCrawled = 0;

    static void addOutputDirectory(String outputDirectory) {
        outputDirectories.add(outputDirectory);
    }

    public static void clearOutputDirectory() {
        outputDirectories = new ArrayList<>();
    }

    public Crawler setCrawlerhandler(ICrawlerHandler crawlerhandler) {
        crawlerHandler = crawlerhandler;
        return this;
    }

    public ICrawlerHandler getCrawlerhandler() {
        return crawlerHandler;
    }

    private synchronized void setTotalDocumentCrawled(int totalDocumentCrawled) {
        this.totalDocumentCrawled = totalDocumentCrawled;
    }

    public synchronized int getTotalDocumentCrawled() {
        return totalDocumentCrawled;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentlyRunningCrawler.getCrawlerhandler().crawlerWillRun();
    }

    @Override
    public void onBeforeExit() {
        super.onBeforeExit();
        currentlyRunningCrawler.getCrawlerhandler().crawlerDidRun();
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String targetHref = url.getURL().toLowerCase();
        String referringHref = referringPage.getWebURL().getURL().toLowerCase();

//        System.out.println(referringHref + " -> " + targetHref);

        // If url is seed then allow
        for (String seedURL: currentlyRunningCrawler.getCrawlerhandler().getCrawlerStartingUrls()) {
            if (targetHref.equalsIgnoreCase(seedURL)) {
                return true;
            }
        }

        if (Pattern.compile(currentlyRunningCrawler.getCrawlerhandler().getAvailableConfigurations().get("Regex Filter Pattern")).matcher(targetHref).matches()) {
            return false;
        }

        return currentlyRunningCrawler.getCrawlerhandler().shouldCrawlerFollowLink(targetHref);
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println(url);

        currentlyRunningCrawler.setTotalDocumentCrawled(currentlyRunningCrawler.getTotalDocumentCrawled() + 1);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            String extractedContent = currentlyRunningCrawler.getCrawlerhandler().extract(url, html);
            writeToFile(url, extractedContent);
        }
    }

    private void writeToFile(String url, String content) {
        if (!url.equalsIgnoreCase("") && !content.equalsIgnoreCase("")) {
            for (String outputDirectory: outputDirectories) {
                Utilities.writeToFile(outputDirectory, url, content);
            }
        }
    }

    public void execute() throws Exception {
        if (crawlerHandler == null) throw new Exception("No Crawler Handler specified");

        currentlyRunningCrawler = this;
        totalDocumentCrawled = 0;

        CrawlConfig config = new CrawlConfig();

        // Set Internal Output Directory Config
        config.setCrawlStorageFolder("data;.crawl".replaceAll(";", Matcher.quoteReplacement(System.getProperty("file.separator"))));

        // Set Max Depth of Crawling Config
        String userMaxDepthOfCrawling = currentlyRunningCrawler.getCrawlerhandler().getAvailableConfigurations().get("Max Depth of Crawling");
        String defaultMaxDepthOfCrawling = "20";
        String maxDepthOfCrawling = userMaxDepthOfCrawling != null ? userMaxDepthOfCrawling : defaultMaxDepthOfCrawling;

        config.setMaxDepthOfCrawling(Integer.valueOf(maxDepthOfCrawling));

        // Set Max Pages to Fetch Config
        String userMaxPagesToFetch = currentlyRunningCrawler.getCrawlerhandler().getAvailableConfigurations().get("Max Pages to Fetch");
        String defaultMaxPagesToFetch = "50";
        String maxPagesToFetch = userMaxPagesToFetch != null ? userMaxPagesToFetch : defaultMaxPagesToFetch;

        config.setMaxPagesToFetch(Integer.valueOf(maxPagesToFetch));

        // Set User Agent String Config
        String userUserAgentString = currentlyRunningCrawler.getCrawlerhandler().getAvailableConfigurations().get("User Agent String");
        String defaultUserAgentString = "Open IE/1.0.0";
        String userAgentString = userUserAgentString != null ? userUserAgentString : defaultUserAgentString;

        config.setUserAgentString(userAgentString);

        config.setPolitenessDelay(10);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = null;

        try {
            controller = new CrawlController(config, pageFetcher, robotstxtServer);

            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            for (String seed: currentlyRunningCrawler.getCrawlerhandler().getCrawlerStartingUrls()) {
                controller.addSeed(seed);
            }

            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(this.getClass(), 1);
//            controller.waitUntilFinish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String outputDirectory = getCrawlerhandler().getAvailableConfigurations().get("Output Directory");

        if (outputDirectory != null) {
            return this.getCrawlerhandler().getPluginName() + " : "  + outputDirectory;
        } else {
            return this.getCrawlerhandler().getPluginName();
        }
    }
}
