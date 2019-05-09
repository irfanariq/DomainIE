package classes;

import id.ac.itb.openie.classifier.IClassifierExtensionHandler;
import id.ac.itb.openie.models.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import weka.core.Instances;


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
            return availableConfigurations;//use this in other function to get configuration that may be changed by user
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);//use this in other function to add configuration that user will be able to configure
        }

        @Override
        public Instances classify(Instances datatest, Instances datatraining) throws Exception{
            /* TODO: add target class value in datatest using model trained from datatraining and return the datatest  */
            return ;
        }
    }
}
