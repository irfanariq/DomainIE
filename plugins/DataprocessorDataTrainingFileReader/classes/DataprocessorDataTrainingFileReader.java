package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.dataprocessor.IDataprocessorFileHandler;
import id.ac.itb.openie.relation.Relations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import weka.core.*;

public class DataprocessorDataTrainingFileReader extends Plugin {

    public DataprocessorDataTrainingFileReader(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DataprocessorDataTrainingFileReaderHandler extends IDataprocessorFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Dataprocessor Data Training File Reader";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Input Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("DATATRAINING_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Pair<Relations, Instances>> read() throws Exception {
            if (getAvailableConfigurations().get("Input Directory") == null) {
                throw new Exception("Read directory path must be specified");
            } else {
                HashMap<File, Pair<Relations, Instances>> pipelineItems = new HashMap<>();

                 ArrayList<File> files = Utilities.getDirectoryFiles(new File(availableConfigurations.get("Input Directory")));

                 for (File _file: files) {
                     pipelineItems.put(_file, Pair.of(Relations.fileWithTargetToRelations(_file), null));
                 }

                return pipelineItems;

               
            }
        }

        @Override
        public void write(File file, Relations relations, Instances instances) throws Exception {}

        @Override
        public void dataprocessorWillRun() {

        }

        @Override
        public void dataprocessorDidRun() {

        }
    }
}
