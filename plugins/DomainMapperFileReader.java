package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.domainmapper.IDomainMapperFileHandler;
import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.utils.Utilities;
import id.ac.itb.openie.models.Rule;
import id.ac.itb.openie.models.DomainRelations;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;

/**
 * Created by Irfan Ariq on 12/05/2019.
 */
public class DomainMapperFileReader extends Plugin {

    public DomainMapperFileReader(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DomainMapperFileReaderHandler extends IDomainMapperFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Domain Mapper Recognized Relation File Reader";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Input Recognized Relation Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("RECOGNIZED_RELATION_OUPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, RecognizedRelations> read() throws Exception {
            System.out.println("Inside domain mapper reader 0 - read recognized relation");

            if (getAvailableConfigurations().get("Input Recognized Relation Directory") == null) {
                throw new Exception("Read (recognized relation) directory path must be specified");
            } else {
                HashMap<File, RecognizedRelations> pipelineItems = new HashMap<>();

                System.out.println("reading recognized relation ... ");

                ArrayList<File> files = Utilities.getDirectoryFiles(new File(availableConfigurations.get("Input Recognized Relation Directory")));

                for (File _file : files) {
                    pipelineItems.put(_file, new RecognizedRelations(_file));
                }

                return pipelineItems;
            }
        }

        @Override
        public void write(File file, DomainRelations domainRelations) throws Exception {
        }

        @Override
        public HashMap<File, DomainDatas> readDomainData() throws Exception {
            return null;
        }

        @Override
        public void writeRule(String fileName, HashSet<Rule> rules) throws Exception {
        }
    }
}
