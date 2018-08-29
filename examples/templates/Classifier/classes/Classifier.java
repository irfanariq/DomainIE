package classes;

import id.ac.itb.openie.extractor.IClassifierExtensionHandler;
import id.ac.itb.openie.relation.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;


public class Classifier extends Plugin {

    public Classifier(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ClassifierHandler extends IClassifierExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            /* fill Classifier name e.g. `Naive bayes` in name variable */
            String name = "";
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
        public ArrayList<String> classify(File file, String document, ArrayList<String> instances) throws Exception{
            /* TODO: Extract relations from document */
            return ;
        }

        public void classifierWillRun() {
            /* TODO: before extractor start crawling */
        }

        public void classifierDidRun() {
            /* TODO: after extractor finish crawling */
        }


    }
}
