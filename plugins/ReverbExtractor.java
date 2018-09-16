package classes;

import id.ac.itb.openie.extractor.IExtractorExtensionRuleHandler;
import id.ac.itb.openie.relation.Relations;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;

/**
 * Created by elvanowen on 2/24/17.
 */
public class ReverbExtractor extends Plugin {

    public ReverbExtractor(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ReverbExtractorHandler extends IExtractorExtensionRuleHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {

            String name = "Reverb Extractor";
            return name;
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public Relations extract(File file, String document, Relations extracted) throws Exception {
            return new ReverbExtraction().extract(file, document, extracted);
        }
    }
}
