package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.classifier.IClassifierFileHandler;
import id.ac.itb.openie.relation.Relations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import weka.core.Instances;

/**
 * Created by elvanowen on 2/24/17.
 */
public class ClassifierFileWriter extends Plugin {

    public ClassifierFileWriter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ClassifierFileWriterHandler extends IClassifierFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Classifier File Writer";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Output Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("CLASSIFIER_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Instances> read() throws Exception{
            return null;
        }
        

        @Override
    public void write(File file, Instances instances) throws Exception{
            if (getAvailableConfigurations().get("Output Directory") == null) {
                throw new Exception("Write directory path must be specified");
            } else {
                Utilities.saveInstancesToArffFile(instances,availableConfigurations.get("Output Directory")+File.separator+file.getName());
            }
        }

        @Override
        public void classifierWillRun() {

        }

        @Override
        public void classifierDidRun() {

        }
    }
}
