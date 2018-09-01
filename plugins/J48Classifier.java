package classes;

import id.ac.itb.openie.classifier.IClassifierExtensionHandler;
import id.ac.itb.openie.relation.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import java.util.Enumeration;



public class J48Classifier extends Plugin {

    public J48Classifier(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class J48ClassifierHandler extends IClassifierExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            String name = "J48 Classifier";
            return name;
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
        public Instances classify(Instances datatest, Instances datatraining) throws Exception{
            datatest.setClassIndex(datatest.numAttributes() - 1);
            datatraining.setClassIndex(datatraining.numAttributes() - 1);
            J48 j48=new J48();
            j48.buildClassifier(datatraining);
            Enumeration<Instance> enumIns = datatest.enumerateInstances();
            while(enumIns.hasMoreElements()){
                Instance ins = (Instance) enumIns.nextElement();
                ins.setValue(ins.classIndex(),j48.classifyInstance(ins));
            }
            return datatest;

        }
    }
}
