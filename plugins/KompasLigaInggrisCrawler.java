package classes;

import id.ac.itb.openie.crawler.ICrawlerExtensionHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by elvanowen on 2/24/17.
 */
public class KompasLigaInggrisCrawler extends Plugin {

    public KompasLigaInggrisCrawler(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class KompasLigaInggrisCrawlerHandler extends ICrawlerExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Kompas.com LigaInggris";
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

            urls.add("https://bola.kompas.com/liga-inggris");
//            urls.add("http://www.kompas.com");
//            urls.add("http://regional.kompas.com");
//            urls.add("http://lipsus.kompas.com");

            return urls;
        }

        public Boolean shouldCrawlerFollowLink(String link) {
//            return link.contains("kompas.com/read") || link.contains("kompas.com/pestaasia");
            return link.contains("bola.kompas.com/read");
        }

        public String extract(String url, String response) {
            Document doc = Jsoup.parse(response);

            Elements contents = doc.getElementsByClass("read__content");
            return contents.get(0).text();
        }

        public void crawlerWillRun() {
            System.out.println(this.getPluginName() + " will run..");
        }

        public void crawlerDidRun() {
            System.out.println(this.getPluginName() + " did run..");
        }
    }
}
