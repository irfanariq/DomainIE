package classes;

import id.ac.itb.openie.crawler.ICrawlerExtensionHandler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by elvanowen on 2/24/17.
 */
public class BolaNetMatchResultCrawler extends Plugin {

    public BolaNetMatchResultCrawler(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class BolaNetMatchResultCrawlerHandler extends ICrawlerExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "BolaNetMatchResult";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Max Pages to Fetch", "50");
            availableConfigurations.putIfAbsent("Max Depth of Crawling", "100");
            availableConfigurations.putIfAbsent("Regex Filter Pattern", ".*(\\.(css|js|gif|jpeg|jpg|png|mp3|mp3|zip|gz))$");
            availableConfigurations.putIfAbsent("User Agent String", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        public HashSet<String> getCrawlerStartingUrls() {
            HashSet<String> urls = new HashSet<String>();

            urls.add("https://www.bola.net/tag/hasil-pertandingan/");

            return urls;
        }

        public Boolean shouldCrawlerFollowLink(String link) {
            return  (link.contains("www.bola.net/") && link.contains("/hasil-pertandingan-"));
        }

        public String extract(String url, String response) {
            Document doc = Jsoup.parse(response);

            ArrayList<String> contents = new ArrayList<>();

            for (Element element: doc.getElementById("newspaging_content").getElementsByTag("p")) {
                 contents.add(element.text());
            }

            String ret = StringUtils.join(contents, ". ");
            return ret.replaceAll("\\.\\.",".");
        }

        public void crawlerWillRun() {
            System.out.println(this.getPluginName() + " will run..");
        }

        public void crawlerDidRun() {
            System.out.println(this.getPluginName() + " did run..");
        }
    }
}
