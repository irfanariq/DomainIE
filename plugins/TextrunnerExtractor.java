package classes;

import id.ac.itb.openie.extractor.IExtractorExtensionLearningHandler;
import id.ac.itb.openie.relation.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import weka.core.Instances;
import weka.core.Instance;
import java.util.ArrayList;
import java.util.Enumeration;


/**
 * Created by elvanowen on 2/24/17.
 */
public class TextrunnerExtractor extends Plugin {

    public TextrunnerExtractor(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class TextrunnerExtractorHandler extends IExtractorExtensionLearningHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            String name = "Textrunner Extractor";

            return name + " ML";        
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public Relations extract(Instances instances, Relations extracted) throws Exception {
            Enumeration<Instance> enumIns = instances.enumerateInstances();
            ArrayList<Integer> listindex= new ArrayList();
            int i=0;
            while(enumIns.hasMoreElements()){
                Instance ins = (Instance) enumIns.nextElement();
                double classVal=ins.value(ins.numAttributes()-1);
                if(classVal==1.0){
                    listindex.add(i);
                }
                i++;
            }
            for(int j=listindex.size()-1;j>=0;j--){
                extracted.removeRelation(listindex.get(j));
            }
            return extracted;
        }

    }
}
