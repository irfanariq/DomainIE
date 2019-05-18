package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.classrecognizer.IClassRecognizerFileHandler;
import id.ac.itb.openie.models.Relations;
import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.utils.Utilities;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.apache.commons.lang3.tuple.Pair;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by Irfan Ariq on 12/05/2019.
 */
public class ClassRecognizerFileWriter extends Plugin {

    public ClassRecognizerFileWriter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ClassRecognizerFileWriterHandler extends IClassRecognizerFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Class Recognizer File Writer";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Output Recognized Relation Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("RECOGNIZED_RELATION_OUPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
            availableConfigurations.putIfAbsent("Output Domain Data Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("RECOGNIZED_DOMAIN_DATA_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Pair<Relations, RecognizedRelations>> read() throws Exception {
            return null;
        }

        @Override
        public HashMap<File, DomainDatas> readDomainData() throws Exception {
            return null;
        }

        @Override
        public void write(File file, RecognizedRelations recognizedRelations) throws Exception {
            if (getAvailableConfigurations().get("Output Recognized Relation Directory") == null) {
                throw new Exception("Write directory path must be specified");
            } else {
                if (recognizedRelations != null) {
                    Utilities.writeToFile(availableConfigurations.get("Output Recognized Relation Directory"), file.getName(), recognizedRelations.toString());
                } else {
                    Utilities.writeToFile(availableConfigurations.get("Output Recognized Relation Directory"), file.getName(), "");
                }
            }
        }

        @Override
        public void writeDomainData(File file, DomainDatas domainData) throws Exception {}

    }
}
