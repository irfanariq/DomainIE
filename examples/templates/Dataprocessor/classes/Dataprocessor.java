package classes;

import id.ac.itb.openie.extractor.IClassifierExtensionHandler;
import id.ac.itb.openie.relation.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class Dataprocessor extends Plugin {

    public Dataprocessor(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DataprocessorHandler extends IDataprocessorExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            /* fill data processor name e.g. `Textrunner dataprocessor` in name variable */
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
        public ArrayList<String> dataprocess(File file, String document, ArrayList<String> instances) throws Exception{

        }

        public void dataprocessorWillRun() {
            /* TODO: before extractor start crawling */
        }

        public void dataprocessorDidRun() {
            /* TODO: after extractor finish crawling */
        }


    }
}
