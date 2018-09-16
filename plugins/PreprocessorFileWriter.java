package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.preprocess.IPreprocessorFileHandler;
import id.ac.itb.openie.utils.Utilities;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by elvanowen on 2/24/17.
 */
public class PreprocessorFileWriter extends Plugin {

    public PreprocessorFileWriter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class PreprocessorFileWriterHandler extends IPreprocessorFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Preprocessor File Writer";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Output Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("PREPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, String> read() throws Exception {
            return null;
        }

        @Override
        public void write(File file, String preprocessed) throws Exception {
            if (getAvailableConfigurations().get("Output Directory") == null) {
                throw new Exception("Write directory path must be specified");
            } else {
                Utilities.writeToFile(availableConfigurations.get("Output Directory"), file.getName(), preprocessed);
            }
        }

        @Override
        public void preprocessorWillRun() {

        }

        @Override
        public void preprocessorDidRun() {

        }
    }
}
