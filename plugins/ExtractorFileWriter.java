package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.extractor.IExtractorFileHandler;
import id.ac.itb.openie.models.Relations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by elvanowen on 2/24/17.
 */
public class ExtractorFileWriter extends Plugin {

    public ExtractorFileWriter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ExtractorFileWriterHandler extends IExtractorFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Extractor File Writer";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Output Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("EXTRACTIONS_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Pair<String, Relations>> read() throws Exception {
            return null;
        }

        @Override
        public void write(File file, Relations extracted) throws Exception {
            if (getAvailableConfigurations().get("Output Directory") == null) {
                throw new Exception("Write directory path must be specified");
            } else {
                if (extracted != null) {
                    Utilities.writeToFile(availableConfigurations.get("Output Directory"), file.getName(), extracted.toString());
                } else {
                    Utilities.writeToFile(availableConfigurations.get("Output Directory"), file.getName(), "");
                }
            }
        }

        @Override
        public void extractorWillRun() {

        }

        @Override
        public void extractorDidRun() {

        }
    }
}
