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
import java.util.HashSet;
import java.util.regex.Matcher;
import java.lang.StringBuilder;

/**
 * Created by Irfan Ariq on 12/05/2019.
 */
public class DomainMapperRuleFileWriter extends Plugin {

    public DomainMapperRuleFileWriter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DomainMapperRuleFileWriterHandler extends IDomainMapperFileHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Domain Mapper Rules File Writer";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Output Rules Directory", System.getProperty("user.dir") + File.separator + new Config().getProperty("RULES_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

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
        public void write(File file, DomainRelations domainRelations) throws Exception {}

        @Override
        public HashMap<File, DomainDatas> readDomainData() throws Exception {
            return null;
        }

        @Override
        public void writeRule(String fileName, HashSet<Rule> rules) throws Exception {
//            System.out.println("Inside domain mapper writer 0 - write rules ");

            if (getAvailableConfigurations().get("Output Rules Directory") == null) {
                throw new Exception("Write (Rules) directory path must be specified");
            } else {
//                System.out.println("Writing rules ... ");
                if (rules != null) {
                    // LOG
                    //System.out.println(" ============== >> FINAL RULE " + rules.size() + " << ================= ");
                    StringBuilder simpenRule = new StringBuilder();
                    simpenRule.append(" ============== >> ").append(rules.size()).append(" FINAL RULE << ================= \n");

                    for (Rule aturan : rules) {
                        simpenRule.append(aturan.toStringIfStatement()).append("\n").append("\n");
                        // LOG
//                        System.out.println(aturan.toStringIfStatement());
//                        System.out.println(aturan.toString());
//                        System.out.println();
                    }
                    Utilities.writeToFile(availableConfigurations.get("Output Rules Directory"),fileName,simpenRule.toString());
                }else {
                    Utilities.writeToFile(availableConfigurations.get("Output Rules Directory"), fileName, "");
                }
            }
        }
    }
}