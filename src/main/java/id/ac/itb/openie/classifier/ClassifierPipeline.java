/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.classifier;

import id.ac.itb.openie.pipeline.IOpenIePipelineElement;
import id.ac.itb.openie.plugins.PluginLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public class ClassifierPipeline implements IOpenIePipelineElement {

    private ArrayList<IClassifierPipelineElement> classifierPipelineElements = new ArrayList<IClassifierPipelineElement>();
    private ArrayList<IClassifierPipelineElement> classifiertrainingPipelineElements = new ArrayList<IClassifierPipelineElement>();
    private IClassifierPipelineHook classifierPipelineHook = null;
    private IClassifierPipelineElement currentlyRunningClassifier = null;
    private int totalProcessedClassifier = 0;
    private int totalDocumentsToBeClassified = 0;
    private int currentlyClassifiedDocuments = 0;
    private boolean doCombineTraining = true;
    
    public ClassifierPipeline addPipelineElement(IClassifierPipelineElement classifierPipelineElement) {
        classifierPipelineElements.add(classifierPipelineElement);
        return this;
    }
    
    public boolean isCombining(){
        return doCombineTraining;
    }
    
    public ArrayList<IClassifierPipelineElement> getClassifierPipelineElements() {
        return this.classifierPipelineElements;
    }
    
    public int getNumberOfClassifiers() {
        int n = 0;

        for (IClassifierPipelineElement classifierPipelineElement: classifierPipelineElements) {
            if (((Classifier)classifierPipelineElement).getClassifierHandler().getPluginName().equalsIgnoreCase("Classifier File Reader")) {
                continue;
            } else if (((Classifier)classifierPipelineElement).getClassifierHandler().getPluginName().equalsIgnoreCase("Classifier File Writer")) {
                continue;
            } else {
                n++;
            }
        }

        return n;
    }
    
    private void addDefaultReaderAndWriter() {
        if (classifierPipelineElements.size() > 0) {
            PluginLoader pluginLoader = new PluginLoader();
            pluginLoader.registerAvailableExtensions(IClassifierHandler.class);

            // Prepend preprocessor file reader if not exist
            for (Object iClassifierHandler: pluginLoader.getAllExtensions(IClassifierHandler.class)) {
                IClassifierHandler classifierHandler = (IClassifierHandler) iClassifierHandler;
                String pluginName = classifierHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Classifier File Reader")) {
                    Classifier classifier = new Classifier().setClassifierHandler(classifierHandler);
                    classifierPipelineElements.add(0, classifier);
                }
            }

            for (Object iClassifierHandler: pluginLoader.getAllExtensions(IClassifierHandler.class)) {
                IClassifierHandler classifierHandler = (IClassifierHandler) iClassifierHandler;
                String pluginName = classifierHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Classifier Data Training File Reader")) {
                    Classifier classifier = new Classifier().setClassifierHandler(classifierHandler);
                    classifiertrainingPipelineElements.add(0, classifier);
                }
            }
            
            for (Object iClassifierHandler: pluginLoader.getAllExtensions(IClassifierHandler.class)) {
                IClassifierHandler classifierHandler = (IClassifierHandler) iClassifierHandler;
                String pluginName = classifierHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Classifier File Writer")) {
                    Classifier classifier = new Classifier().setClassifierHandler(classifierHandler);
                    classifierPipelineElements.add(classifier);
                }
            }
        }
    }
    
    @Override
    public void willExecute() {
        if (this.getNumberOfClassifiers() > 0) {
            classifierPipelineHook.willExecute();
        }
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Running classifier pipeline...");
        totalProcessedClassifier = 0;
        totalDocumentsToBeClassified = 0;
        currentlyClassifiedDocuments = 0;

        
        addDefaultReaderAndWriter();

        Instances datatraining=null;
        
        
        

        //combine data training
        
        for (IClassifierPipelineElement classifierPipelineElement: classifiertrainingPipelineElements) {
            this.currentlyRunningClassifier = classifierPipelineElement;

            if (((Classifier)classifierPipelineElement).getClassifierHandler().getPluginName().equalsIgnoreCase("Classifier Data Training File Reader")) {
                HashMap<File, Instances> featuredDocuments = classifierPipelineElement.read();
                totalDocumentsToBeClassified = featuredDocuments.size();
                System.out.println("size " + totalDocumentsToBeClassified);
                Iterator<Map.Entry<File, Instances>> it = featuredDocuments.entrySet().iterator();
                
                if(it.hasNext()){
                    Map.Entry<File, Instances> pair = it.next();
                    datatraining = pair.getValue();
                }
                currentlyClassifiedDocuments = 0;

                while (it.hasNext()) {
                    Map.Entry<File, Instances> pair = it.next();

                    Enumeration<Instance> enumIns = pair.getValue().enumerateInstances();
                    while(enumIns.hasMoreElements()){
                        Instance ins = (Instance) enumIns.nextElement();
                        datatraining.add(ins);
                    }
                    currentlyClassifiedDocuments++;
                }
            } 
        }
   
        
        //classify
        currentlyClassifiedDocuments = 0;
        doCombineTraining=false;
        
        HashMap<File, Instances> pipeQueue = new HashMap<>();
        HashMap<File, Instances> nextPipeQueue = new HashMap<>();
        for (IClassifierPipelineElement classifierPipelineElement: classifierPipelineElements) {
            this.currentlyRunningClassifier = classifierPipelineElement;

            if (((Classifier)classifierPipelineElement).getClassifierHandler().getPluginName().equalsIgnoreCase("Classifier File Reader")) {
                HashMap<File, Instances> featuredDocuments = classifierPipelineElement.read();
                nextPipeQueue.putAll(featuredDocuments);
                totalDocumentsToBeClassified += featuredDocuments.size();
            } else if (((Classifier)classifierPipelineElement).getClassifierHandler().getPluginName().equalsIgnoreCase("Classifier File Writer")) {
                for (Map.Entry<File, Instances> pair : pipeQueue.entrySet()) {
                    classifierPipelineElement.write(pair.getKey(), pair.getValue());
                }
            } else {
                this.totalProcessedClassifier++;

                Iterator<Map.Entry<File, Instances>> it = pipeQueue.entrySet().iterator();

                currentlyClassifiedDocuments = 0;

                while (it.hasNext()) {
                    Map.Entry<File, Instances> pair = it.next();

                    HashMap<File, Instances> classified = classifierPipelineElement.execute(pair.getKey(), pair.getValue(), datatraining);

                    nextPipeQueue.putAll(classified);
                    currentlyClassifiedDocuments++;
                }
            }

            pipeQueue = nextPipeQueue;
            nextPipeQueue = new HashMap<>();
        }
    }

    @Override
    public void didExecute() {
        if (this.getNumberOfClassifiers() > 0) {
            classifierPipelineHook.didExecute();
        }
    }
    
    public int getTotalProcessedClassifier() {
        return totalProcessedClassifier;
    }

    public int getTotalDocumentsToBeClassified() {
        return totalDocumentsToBeClassified;
    }

    public int getCurrentlyClassifiedDocuments() {
        return currentlyClassifiedDocuments;
    }

    public IClassifierPipelineElement getCurrentlyRunningClassifier() {
        return currentlyRunningClassifier;
    }

    public ClassifierPipeline setClassifierPipelineHook(IClassifierPipelineHook classifierPipelineHook) {
        this.classifierPipelineHook = classifierPipelineHook;
        return this;
    }
    
}
