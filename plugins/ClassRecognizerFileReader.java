package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.classrecognizer.IClassRecognizerFileHandler;
import id.ac.itb.openie.models.Relations;
import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by Irfan Ariq on 12/05/2019.
 */
public class ClassRecognizerFileReader extends Plugin {

    public ClassRecognizerFileReader(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ClassRecognizerFileReaderHandler extends IClassRecognizerFileHandler{

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Class Recognizer File Reader";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Input Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("POSTPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
            availableConfigurations.putIfAbsent("Input Domain Data Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("DOMAIN_DATA_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, Pair<Relations, RecognizedRelations>> read() throws Exception {
//            System.out.println("Inside class recognizer reader 0 - read relation");

            if (getAvailableConfigurations().get("Input Directory") == null) {
                throw new Exception("Read (relation) directory path must be specified");
            } else {
                HashMap<File, Pair<Relations, RecognizedRelations>> pipelineItems = new HashMap<>();

//                System.out.println("reading relation ... ");

                ArrayList<File> files = Utilities.getDirectoryFiles(new File(availableConfigurations.get("Input Directory")));

                for (File _file: files) {
                    pipelineItems.put(_file, Pair.of(new Relations(_file), null));
                }

                return pipelineItems;
            }
        }

        @Override
        public HashMap<File, DomainDatas> readDomainData() throws Exception {
            return null;
        }

        @Override
        public void write(File file, RecognizedRelations recognizedRelations) throws Exception {}

        @Override
        public void writeDomainData(File file, DomainDatas domainData) throws Exception {}

    }
}
