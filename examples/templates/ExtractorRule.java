package classes;

import id.ac.itb.openie.extractor.IExtractorExtensionRuleHandler;
import id.ac.itb.openie.relation.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;

public class ExtractorRule extends Plugin {

    public ExtractorRule(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ExtractorRuleHandler extends IExtractorExtensionRuleHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            /* fill extractor name e.g. `Reverb Extractor` in name variable */
            String name = "My Extractor";
            return name;
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations;//use this in other function to get configuration that may be changed by user
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);//use this in other function to add configuration that user will be able to configure
        }

        @Override
        public Relations extract(File file, String document, Relations extracted) throws Exception {
            /* TODO: Extract relations from document */
            return extracted;
        }

    }
}
