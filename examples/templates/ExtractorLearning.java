package classes;

import id.ac.itb.openie.extractor.IExtractorExtensionLearningHandler;
import id.ac.itb.openie.relation.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import weka.core.Instances;
import weka.core.Instance;
import java.util.ArrayList;
import java.util.Enumeration;

public class ExtractorLearning extends Plugin {

    public Extractor(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ExtractorLearningHandler extends IExtractorExtensionLearningHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            /* fill extractor name e.g. `textrunner extractor` in name variable */
            String name = "My Extractor";
            return name + " ML";       
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
        public Relations extract(Instances instances, Relations extracted) throws Exception {
           /*TODO: complete extracted as set of relation by using classified datatest(instances) */
        }
    }
}
