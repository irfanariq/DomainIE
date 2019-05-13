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
public class DomainMapperDomainDataFileReader extends Plugin {

    public DomainMapperDomainDataFileReader(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DomainMapperDomainDataFileReaderHandler extends IDomainMapperFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Domain Mapper Domain Data File Reader";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Input Domain Data Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("RECOGNIZED_DOMAIN_DATA_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, DomainDatas> readDomainData() throws Exception {
            System.out.println("Inside domain mapper reader 0 - read rec domain data");

            if (getAvailableConfigurations().get("Input Domain Data Directory") == null) {
                throw new Exception("Read (rec domain data) directory path must be specified");
            } else {
                HashMap<File, DomainDatas> pipelineItems = new HashMap<>();

                System.out.println("reading rec domain data ... ");

                ArrayList<File> files = Utilities.getDirectoryFiles(new File(availableConfigurations.get("Input Domain Data Directory")));

                for (File _file : files) {
                    pipelineItems.put(_file, DomainDatas.fromFileWithClass(_file));
                }

                return pipelineItems;
            }
        }

        @Override
        public void write(File file, DomainRelations domainRelations) throws Exception {
        }

        @Override
        public HashMap<File, RecognizedRelations> read() throws Exception {
            return null;
        }

        @Override
        public void writeRule(String fileName, HashSet<Rule> rules) throws Exception {
        }
    }
}