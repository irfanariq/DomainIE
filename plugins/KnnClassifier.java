package classes;

import id.ac.itb.openie.classifier.IClassifierExtensionHandler;
import id.ac.itb.openie.relation.Relations;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;
import java.util.Enumeration;



public class KnnClassifier extends Plugin {

    public KnnClassifier(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class KnnClassifierHandler extends IClassifierExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            String name = "Knn Classifier";
            return name;
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {

            availableConfigurations.putIfAbsent("n","3");
            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public Instances classify(Instances dataset, Instances datatraining) throws Exception{
            int k = Integer.parseInt(availableConfigurations.get("n"));
            dataset.setClassIndex(dataset.numAttributes() - 1);
            datatraining.setClassIndex(datatraining.numAttributes() - 1);
            IBk ibk=new IBk(k);
            ibk.buildClassifier(datatraining);
            Enumeration<Instance> enumIns = dataset.enumerateInstances();
            while(enumIns.hasMoreElements()){
                Instance ins = (Instance) enumIns.nextElement();
                ins.setValue(ins.classIndex(),ibk.classifyInstance(ins));
            }
            return dataset;

        }


    }
}
