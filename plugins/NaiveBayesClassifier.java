package classes;

import id.ac.itb.openie.classifier.IClassifierExtensionHandler;
import id.ac.itb.openie.models.Relations;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import weka.classifiers.bayes.*;
import weka.core.Instance;
import weka.core.Instances;
import java.util.Enumeration;



public class NaiveBayesClassifier extends Plugin {

    public NaiveBayesClassifier(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class NaiveBayesClassifierHandler extends IClassifierExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            String name = "Naive Bayes Classifier";
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
            NaiveBayes nb= new NaiveBayes();
            nb.buildClassifier(datatraining);
            Enumeration<Instance> enumIns = datatest.enumerateInstances();
            while(enumIns.hasMoreElements()){
                Instance ins = (Instance) enumIns.nextElement();
                ins.setValue(ins.classIndex(),nb.classifyInstance(ins));
            }
            return datatest;

        }
    }
}
