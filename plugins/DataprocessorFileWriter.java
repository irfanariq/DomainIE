package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.dataprocessor.IDataprocessorFileHandler;
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
import weka.core.*;


public class DataprocessorFileWriter extends Plugin {

    public DataprocessorFileWriter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DataprocessorFileWriterHandler extends IDataprocessorFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Dataprocessor File Writer";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Output Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("DATAPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
            availableConfigurations.putIfAbsent("Output Relation Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("DATAPROCESSRELATION_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Pair<Relations, Instances>> read() throws Exception {
            return null;
        }

        @Override
        public void write(File file, Relations relations,Instances instances) throws Exception {
            if (getAvailableConfigurations().get("Output Directory") == null) {
                throw new Exception("Write directory path must be specified");
            } else {
                Utilities.writeToFile(availableConfigurations.get("Output Relation Directory"), file.getName(), relations.toString());
                Utilities.saveInstancesToArffFile(instances, availableConfigurations.get("Output Directory")+File.separator+Utilities.nameFile(file.getName())+".arff");   
            }
        }
    }
}
