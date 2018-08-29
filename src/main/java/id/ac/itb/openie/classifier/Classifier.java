/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.classifier;

import id.ac.itb.openie.relation.Relations;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public class Classifier implements IClassifierPipelineElement {

    private IClassifierHandler classifierHandler;
    
    public IClassifierHandler getClassifierHandler() {
        return classifierHandler;
    }
    
    public Classifier setClassifierHandler(IClassifierHandler classifierHandler) {
        this.classifierHandler = classifierHandler;
        return this;
    }

    public String toString() {
        if (getClassifierHandler().getAvailableConfigurations() != null) {
            String inputDirectory = getClassifierHandler().getAvailableConfigurations().get("Input Directory");
            String outputDirectory = getClassifierHandler().getAvailableConfigurations().get("Output Directory");

            if (inputDirectory != null) {
                return this.getClassifierHandler().getPluginName() + " : " + inputDirectory;
            } else if (outputDirectory != null) {
                return this.getClassifierHandler().getPluginName() + " : "  + outputDirectory;
            }
        }

        return this.getClassifierHandler().getPluginName();
    }
    
    
    @Override
    public HashMap<File, Instances> execute(File file, Instances dataset, Instances datatraining) throws Exception {
        HashMap<File, Instances> out = new HashMap<>();
        out.put(file, this.getClassifierHandler().classify(file, dataset, datatraining));
        return out;
    }

    @Override
    public HashMap<File, Instances> read() throws Exception {
        return this.getClassifierHandler().read();
    }

    @Override
    public void write(File file, Instances instances) throws Exception {
        this.getClassifierHandler().write(file, instances);
    }
    
}
