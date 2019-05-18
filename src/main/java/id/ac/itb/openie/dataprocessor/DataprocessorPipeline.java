/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;

import id.ac.itb.openie.pipeline.IOpenIePipelineElement;
import id.ac.itb.openie.plugins.PluginLoader;
import id.ac.itb.openie.models.Relations;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public class DataprocessorPipeline implements IOpenIePipelineElement{
    
    private ArrayList<IDataprocessorPipelineElement> dataprocessorPipelineElements = new ArrayList<IDataprocessorPipelineElement>();
    private ArrayList<IDataprocessorPipelineElement> datatrainingprocessorPipelineElements = new ArrayList<IDataprocessorPipelineElement>();    
    private IDataprocessorPipelineHook dataprocessorPipelineHook = null;
    private IDataprocessorPipelineElement currentlyRunningDataprocessor = null;
    private int totalProcessedDataprocessor = 0;
    private int totalDocumentsToBeDataprocessed = 0;
    private int currentlyDataprocessedDocuments = 0;
    private boolean doTraining = true;
    
    public DataprocessorPipeline addPipelineElement(IDataprocessorPipelineElement dataprocessorPipelineElement) {
        dataprocessorPipelineElements.add(dataprocessorPipelineElement);
        datatrainingprocessorPipelineElements.add(dataprocessorPipelineElement);
        
        return this;
    }
    
    public DataprocessorPipeline addPipelineElementTraining(IDataprocessorPipelineElement dataprocessorPipelineElement) {
        datatrainingprocessorPipelineElements.add(0,dataprocessorPipelineElement);
        
        return this;
    }
    
    public ArrayList<IDataprocessorPipelineElement> getDataprocessorPipelineElements() {
        return this.dataprocessorPipelineElements;
    }
    
    public ArrayList<IDataprocessorPipelineElement> getDatatrainingprocessorPipelineElements() {
        return this.datatrainingprocessorPipelineElements;
    }
    
    
    public int getNumberOfDataprocessors() {
        int n = 0;

        for (IDataprocessorPipelineElement dataprocessorPipelineElement: dataprocessorPipelineElements) {
            if (((Dataprocessor)dataprocessorPipelineElement).getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor File Reader")) {
                continue;
            } else if (((Dataprocessor)dataprocessorPipelineElement).getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor File Writer")) {
                continue;
            } else {
                n++;
            }
        }

        return n;
    }
    
    private void addDefaultReaderAndWriter() {
        if (dataprocessorPipelineElements.size() > 0) {
            PluginLoader pluginLoader = new PluginLoader();
            pluginLoader.registerAvailableExtensions(IDataprocessorHandler.class);

            // Prepend preprocessor file reader if not exist
            for (Object iDataprocessorHandler: pluginLoader.getAllExtensions(IDataprocessorHandler.class)) {
                IDataprocessorHandler dataprocessorHandler = (IDataprocessorHandler) iDataprocessorHandler;
                String pluginName = dataprocessorHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Dataprocessor File Reader")) {
                    Dataprocessor dataprocessor = new Dataprocessor().setDataprocessorHandler(dataprocessorHandler);
                    dataprocessorPipelineElements.add(0, dataprocessor);
                }
            }
            
            for (Object iDataprocessorHandler: pluginLoader.getAllExtensions(IDataprocessorHandler.class)) {
                IDataprocessorHandler dataprocessorHandler = (IDataprocessorHandler) iDataprocessorHandler;
                String pluginName = dataprocessorHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Dataprocessor Data Training File Reader")) {
                    Dataprocessor dataprocessor = new Dataprocessor().setDataprocessorHandler(dataprocessorHandler);
                    datatrainingprocessorPipelineElements.add(0, dataprocessor);
                }
            }

            for (Object iDataprocessorHandler: pluginLoader.getAllExtensions(IDataprocessorHandler.class)) {
                IDataprocessorHandler dataprocessorHandler = (IDataprocessorHandler) iDataprocessorHandler;
                String pluginName = dataprocessorHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Dataprocessor File Writer")) {
                    Dataprocessor dataprocessor = new Dataprocessor().setDataprocessorHandler(dataprocessorHandler);
                    dataprocessorPipelineElements.add(dataprocessor);
                }
            }
            for (Object iDataprocessorHandler: pluginLoader.getAllExtensions(IDataprocessorHandler.class)) {
                IDataprocessorHandler dataprocessorHandler = (IDataprocessorHandler) iDataprocessorHandler;
                String pluginName = dataprocessorHandler.getPluginName();
                
                if (pluginName.equalsIgnoreCase("Dataprocessor Data Training File Writer")) {
                    Dataprocessor dataprocessor = new Dataprocessor().setDataprocessorHandler(dataprocessorHandler);
                    datatrainingprocessorPipelineElements.add(dataprocessor);
                }
            }
            
        }
    }
    
    @Override
    public void willExecute() {
        if (this.getNumberOfDataprocessors() > 0) {
            dataprocessorPipelineHook.willExecute();
        }
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Running data processor pipeline...");
        totalProcessedDataprocessor = 0;
        totalDocumentsToBeDataprocessed = 0;
        currentlyDataprocessedDocuments = 0;

        addDefaultReaderAndWriter();

        HashMap<File, Pair<Relations, Instances>> pipeQueue = new HashMap<>();
        HashMap<File, Pair<Relations, Instances>> nextPipeQueue = new HashMap<>();
        
        //do the datatraining
        for (IDataprocessorPipelineElement dataprocessorPipelineElement: datatrainingprocessorPipelineElements) {
            this.currentlyRunningDataprocessor = dataprocessorPipelineElement;

            if (((Dataprocessor)dataprocessorPipelineElement).getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor Data Training File Reader")) {
                HashMap<File, Pair<Relations, Instances>> featuredDocuments = dataprocessorPipelineElement.read();
                nextPipeQueue.putAll(featuredDocuments);
                totalDocumentsToBeDataprocessed = featuredDocuments.size();
            } else if (((Dataprocessor)dataprocessorPipelineElement).getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor Data Training File Writer")) {
                for (Map.Entry<File, Pair<Relations, Instances>> pair : pipeQueue.entrySet()) {
                    dataprocessorPipelineElement.write(pair.getKey(),pair.getValue().getLeft(), pair.getValue().getRight());
                }
            } else {
                this.totalProcessedDataprocessor++;

                Iterator<Map.Entry<File, Pair<Relations, Instances>>> it = pipeQueue.entrySet().iterator();

                currentlyDataprocessedDocuments = 0;

                while (it.hasNext()) {
                    Map.Entry<File, Pair<Relations, Instances>> pair = it.next();

                    HashMap<File, Pair<Relations, Instances>> dataprocessed = dataprocessorPipelineElement.execute(pair.getKey(), pair.getValue().getLeft(), pair.getValue().getRight());

                    nextPipeQueue.putAll(dataprocessed);
                    currentlyDataprocessedDocuments++;
                }
            }

            pipeQueue = nextPipeQueue;
            nextPipeQueue = new HashMap<>();
        }
        
        //do the dataset
        doTraining=false;
        totalProcessedDataprocessor = 0;
        totalDocumentsToBeDataprocessed = 0;
        currentlyDataprocessedDocuments = 0;
        pipeQueue = new HashMap<>();
        nextPipeQueue = new HashMap<>();
        for (IDataprocessorPipelineElement dataprocessorPipelineElement: dataprocessorPipelineElements) {
            this.currentlyRunningDataprocessor = dataprocessorPipelineElement;

            if (((Dataprocessor)dataprocessorPipelineElement).getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor File Reader")) {
                HashMap<File, Pair<Relations, Instances>> featuredDocuments = dataprocessorPipelineElement.read();
                nextPipeQueue.putAll(featuredDocuments);
                totalDocumentsToBeDataprocessed = featuredDocuments.size();
            } else if (((Dataprocessor)dataprocessorPipelineElement).getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor File Writer")) {
                for (Map.Entry<File, Pair<Relations, Instances>> pair : pipeQueue.entrySet()) {
                    dataprocessorPipelineElement.write(pair.getKey(),pair.getValue().getLeft(), pair.getValue().getRight());
                }
            } else {
                this.totalProcessedDataprocessor++;

                Iterator<Map.Entry<File, Pair<Relations, Instances>>> it = pipeQueue.entrySet().iterator();

                currentlyDataprocessedDocuments = 0;

                while (it.hasNext()) {
                    Map.Entry<File, Pair<Relations, Instances>> pair = it.next();

                    HashMap<File, Pair<Relations, Instances>> dataprocessed = dataprocessorPipelineElement.execute(pair.getKey(), pair.getValue().getLeft(), pair.getValue().getRight());

                    nextPipeQueue.putAll(dataprocessed);
                    currentlyDataprocessedDocuments++;
                }
            }

            pipeQueue = nextPipeQueue;
            nextPipeQueue = new HashMap<>();
        }
    }

    @Override
    public void didExecute() {
        if (this.getNumberOfDataprocessors() > 0) {
            dataprocessorPipelineHook.didExecute();
        }
    }
    
    public int getTotalProcessedDataprocessor() {
        return totalProcessedDataprocessor;
    }

    public int getTotalDocumentsToBeDataprocessed() {
        return totalDocumentsToBeDataprocessed;
    }

    public int getCurrentlyDataprocessedDocuments() {
        return currentlyDataprocessedDocuments;
    }
    public boolean isTraining() {
        return doTraining;
    }

    public IDataprocessorPipelineElement getCurrentlyRunningDataprocessor() {
        return currentlyRunningDataprocessor;
    }

    public DataprocessorPipeline setDataprocessorPipelineHook(IDataprocessorPipelineHook dataprocessorPipelineHook) {
        this.dataprocessorPipelineHook = dataprocessorPipelineHook;
        return this;
    }
    
}
