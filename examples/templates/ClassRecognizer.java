package classes;

import id.ac.itb.openie.classrecognizer.IClassRecognizerExtensionHandler;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Created by Irfan Ariq on 18/05/19.
 */
public class ClassRecognizer extends Plugin {

    public ClassRecognizer(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class ClassRecognizerHandler extends IClassRecognizerExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            /* Return class recognizer name e.g. 'English Premiere League Class Recognizer'*/
            String name = "";
            return name;
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations; //use this in other function to get configuration that may be changed by user
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);//use this in other function to add configuration that user will be able to configure
        }

        @Override
        public HashMap<String, ArrayList<String>> getWordList() throws Exception{
            /* TODO : return class domain list with its words list*/
            return listKata;
        }

        @Override
        public HashMap<String, ArrayList<String>> getPatternList() throws Exception{
            /* TODO : return class domain list with its regular expression pattern*/
            return listPattern;
        }
    }
}
