package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.extractor.IExtractorFileHandler;
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

public class ExtractorMachineLearningFileReader extends Plugin {

    public ExtractorMachineLearningFileReader(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ExtractorMachineLearningFileReaderHandler extends IExtractorFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Extractor Machine Learning File Reader";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Input Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("CLASSIFIER_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
            availableConfigurations.putIfAbsent("Input Relation Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("DATAPROCESSRELATION_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Pair<Instances, Relations>> readML() throws Exception {
            if (getAvailableConfigurations().get("Input Directory") == null) {
                throw new Exception("Read directory path must be specified");
            } else {
                HashMap<File, Pair<Instances, Relations>> pipelineItems = new HashMap<>();

                ArrayList<File> files = Utilities.getDirectoryFiles(new File(availableConfigurations.get("Input Directory")));
                ArrayList<File> relfiles = Utilities.getDirectoryFiles(new File(availableConfigurations.get("Input Relation Directory")));

                for (int i=0;i<files.size();i++) {
                    pipelineItems.put(relfiles.get(i), Pair.of(Utilities.arffToInstances(files.get(i)),new Relations(relfiles.get(i))));
                }

                return pipelineItems;
            }
        }

        @Override
        public void write(File file, Relations extracted) throws Exception {}

        @Override
        public void extractorWillRun() {

        }

        @Override
        public void extractorDidRun() {

        }
    }
}
