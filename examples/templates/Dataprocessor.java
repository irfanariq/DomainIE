package classes;

import id.ac.itb.openie.dataprocessor.*;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;
import id.ac.itb.openie.relation.Relation;
import id.ac.itb.openie.relation.Relations;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class Dataprocessor extends Plugin {

    public Dataprocessor(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class DataprocessorHandler extends IDataprocessorExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            /* fill data processor name e.g. `Textrunner dataprocessor` in name variable */
            String name = "";
            return name;
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations;//use this in other function to get configuration that may be changed by user
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);//use this in other function to add configuration that user will be able to configure
        }

        @Override
        public Relations documentToRelations(String document){
            /* TODO: change document(corpus text) into Relations(set of relation) that may be incomplete as classifier input*/
        }

    }
}
