package classes;

import id.ac.itb.openie.extractor.IExtractorExtensionHandler;
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


/**
 * Created by elvanowen on 2/24/17.
 */
public class Extractor extends Plugin {

    public Extractor(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ExtractorHandler extends IExtractorExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            String name = "My Extractor";

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
        public Relations extract(File file, Instances instances, Relations extracted) throws Exception {
           
        }

        public void extractorWillRun() {
            System.out.println(this.getPluginName() + " will run..");
        }

        public void extractorDidRun() {
            System.out.println(this.getPluginName() + " did run..");
        }

        public int getExtractorType(){
            return 1;
        }
    }
}
