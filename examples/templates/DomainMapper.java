package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.domainmapper.IDomainMapperExtensionHandler;
import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.DomainData;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Rule;
import id.ac.itb.openie.models.DomainRelations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.ArrayList;

/**
 * Created by Irfan Ariq on 18/05/19.
 */
public class MyCoveringAlgorithmDomainMapper extends Plugin {

    public MyCoveringAlgorithmDomainMapper(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class MyCoveringAlgorithmDomainMapperHandler extends IDomainMapperExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        @Override
        public String getPluginName() {
            /* Return domain mapper name e.g. 'Covering Algorithm Domain Mapper'*/
            return "";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            /* List of configurations available in UI interface */
            availableConfigurations.putIfAbsent("Rule Name", "Name of rule that will be saved");

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashSet<Rule> generateRule(HashMap<File, DomainDatas> fileDomainDatasHashMap) throws Exception {
            /* TODO : Generate rule from domain data*/
            return setAturan;
        }
    }
}
