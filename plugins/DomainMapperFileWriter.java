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
import java.util.regex.Matcher;
import java.util.HashSet;

/**
 * Created by Irfan Ariq on 12/05/2019.
 */
public class DomainMapperFileWriter extends Plugin {

    public DomainMapperFileWriter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DomainMapperFileWriterHandler extends IDomainMapperFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Domain Mapper Domain Relation File Writer";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Output Domain Relation Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("DOMAIN_RELATION_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashMap<File, RecognizedRelations> read() throws Exception {
            return null;
        }

        @Override
        public void write(File file, DomainRelations domainRelations) throws Exception {
            System.out.println("Inside domain mapper writer 0 - write domain relation");

            if (getAvailableConfigurations().get("Output Domain Relation Directory") == null) {
                throw new Exception("Write (domain relation) directory path must be specified");
            } else {
                if (domainRelations != null) {
                    Utilities.writeToFile(availableConfigurations.get("Output Domain Relation Directory"), file.getName(),domainRelations.toString());
                }else {
                    Utilities.writeToFile(availableConfigurations.get("Output Domain Data Directory"), file.getName(), "");
                }
            }
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