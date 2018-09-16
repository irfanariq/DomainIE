package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.classifier.IClassifierFileHandler;
import id.ac.itb.openie.relation.Relations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import weka.core.Instances;


public class ClassifierDataTrainingFileReader extends Plugin {

    public ClassifierDataTrainingFileReader(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ClassifierDataTrainingFileReaderHandler extends IClassifierFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Classifier Data Training File Reader";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Input Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("DATATRAININGPROCESSED_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Instances> read() throws Exception {
            if (getAvailableConfigurations().get("Input Directory") == null) {
                throw new Exception("Read directory path must be specified");
            } else {
                HashMap<File, Instances> pipelineItems = new HashMap<>();

                ArrayList<File> files = Utilities.getDirectoryFiles(new File(availableConfigurations.get("Input Directory")));

                for (File _file: files) {
                    pipelineItems.put(_file, Utilities.arffToInstances(_file));
                }

                return pipelineItems;

            }
        }

        @Override
        public void write(File file, Instances instances) throws Exception {}

    }
}
