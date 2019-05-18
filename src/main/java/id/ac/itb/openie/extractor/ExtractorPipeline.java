package id.ac.itb.openie.extractor;

import id.ac.itb.openie.pipeline.IOpenIePipelineElement;
import id.ac.itb.openie.plugins.PluginLoader;
import id.ac.itb.openie.models.Relations;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weka.core.Instances;

public class ExtractorPipeline implements IOpenIePipelineElement {
    
    private ArrayList<IExtractorPipelineElement> extractorPipelineElements = new ArrayList<IExtractorPipelineElement>();
    private int totalProcessedExtractor = 0;
    private int totalDocumentsToBeExtracted = 0;
    private int currentlyExtractedDocuments = 0;
    private IExtractorPipelineElement currentlyRunningExtractor = null;
    private IExtractorPipelineHook extractorPipelineHook = null;
    private boolean isMachineLearning;
    
    public ExtractorPipeline addPipelineElement(IExtractorPipelineElement extractorPipelineElement) {
        extractorPipelineElements.add(extractorPipelineElement);
        return this;
    }
    
    public ArrayList<IExtractorPipelineElement> getExtractorPipelineElements() {
        return this.extractorPipelineElements;
    }
    
    public int getNumberOfExtractors() {
        int n = 0;
        
        for (IExtractorPipelineElement extractorPipelineElement: extractorPipelineElements) {
            if (((Extractor)extractorPipelineElement).getExtractorHandler().getPluginName().equalsIgnoreCase("Extractor File Reader")) {
                continue;
            } else if (((Extractor)extractorPipelineElement).getExtractorHandler().getPluginName().equalsIgnoreCase("Extractor File Writer")) {
                continue;
            } else if (((Extractor)extractorPipelineElement).getExtractorHandler().getPluginName().equalsIgnoreCase("Extractor Machine Learning File Reader")) {
                continue;
            } else {
                n++;
            }
        }
        
        return n;
    }
    
    private void addDefaultReaderAndWriter() {
        if (extractorPipelineElements.size() > 0) {
            PluginLoader pluginLoader = new PluginLoader();
            pluginLoader.registerAvailableExtensions(IExtractorHandler.class);
            Extractor checkextractor = (Extractor) extractorPipelineElements.get(0);
            
            if(checkextractor.getExtractorHandler().getExtractorType()==0){
                System.out.println("aturan");
                // Prepend preprocessor file reader if not exist
                isMachineLearning=false;
                for (Object iExtractorHandler: pluginLoader.getAllExtensions(IExtractorHandler.class)) {
                    IExtractorHandler extractorHandler = (IExtractorHandler) iExtractorHandler;
                    String pluginName = extractorHandler.getPluginName();
                    
                    if (pluginName.equalsIgnoreCase("Extractor File Reader")) {
                        Extractor extractor = new Extractor().setExtractorHandler(extractorHandler);
                        extractorPipelineElements.add(0, extractor);
                    }
                }
                
                for (Object iExtractorHandler: pluginLoader.getAllExtensions(IExtractorHandler.class)) {
                    IExtractorHandler extractorHandler = (IExtractorHandler) iExtractorHandler;
                    String pluginName = extractorHandler.getPluginName();
                    
                    if (pluginName.equalsIgnoreCase("Extractor File Writer")) {
                        Extractor extractor = new Extractor().setExtractorHandler(extractorHandler);
                        extractorPipelineElements.add(extractor);
                    }
                }
            }else{
                System.out.println("pembelajaran mesin");

                isMachineLearning=true;
                
                for (Object iExtractorHandler: pluginLoader.getAllExtensions(IExtractorHandler.class)) {
                    IExtractorHandler extractorHandler = (IExtractorHandler) iExtractorHandler;
                    String pluginName = extractorHandler.getPluginName();
                    
                    if (pluginName.equalsIgnoreCase("Extractor Machine Learning File Reader")) {
                        Extractor extractor = new Extractor().setExtractorHandler(extractorHandler);
                        extractorPipelineElements.add(0, extractor);
                    }
                }
                
                for (Object iExtractorHandler: pluginLoader.getAllExtensions(IExtractorHandler.class)) {
                    IExtractorHandler extractorHandler = (IExtractorHandler) iExtractorHandler;
                    String pluginName = extractorHandler.getPluginName();
                    
                    if (pluginName.equalsIgnoreCase("Extractor File Writer")) {
                        Extractor extractor = new Extractor().setExtractorHandler(extractorHandler);
                        extractorPipelineElements.add(extractor);
                    }
                }
            }
            
        }
    }
    
    @Override
    public void willExecute() {
        if (this.getNumberOfExtractors() > 0) {
            extractorPipelineHook.willExecute();
        }
    }
    
    public void execute() throws Exception {
        System.out.println("Running extractor pipeline...");
        totalProcessedExtractor = 0;
        totalDocumentsToBeExtracted = 0;
        currentlyExtractedDocuments = 0;
        
        addDefaultReaderAndWriter();
        
        if(!isMachineLearning){
            HashMap<File, Pair<String, Relations>> pipeQueue = new HashMap<>();
            HashMap<File, Pair<String, Relations>> nextPipeQueue = new HashMap<>();
            
            for (IExtractorPipelineElement extractorPipelineElement: extractorPipelineElements) {
                this.currentlyRunningExtractor = extractorPipelineElement;
                
                if (((Extractor)extractorPipelineElement).getExtractorHandler().getPluginName().equalsIgnoreCase("Extractor File Reader")) {
                    HashMap<File, Pair<String, Relations>> extractedRelations = extractorPipelineElement.read();
                    nextPipeQueue.putAll(extractedRelations);
                    totalDocumentsToBeExtracted = extractedRelations.size();
                } else if (((Extractor)extractorPipelineElement).getExtractorHandler().getPluginName().equalsIgnoreCase("Extractor File Writer")) {
                    for (Map.Entry<File, Pair<String, Relations>> pair : pipeQueue.entrySet()) {
                        extractorPipelineElement.write(pair.getKey(), pair.getValue().getRight());
                    }
                } else {
                    this.totalProcessedExtractor++;
                    
                    Iterator<Map.Entry<File, Pair<String, Relations>>> it = pipeQueue.entrySet().iterator();
                    
                    currentlyExtractedDocuments = 0;
                    
                    while (it.hasNext()) {
                        Map.Entry<File, Pair<String, Relations>> pair = it.next();
                        
                        HashMap<File, Pair<String, Relations>> extracted = extractorPipelineElement.execute(pair.getKey(), pair.getValue().getLeft(), pair.getValue().getRight());
                        
                        nextPipeQueue.putAll(extracted);
                        currentlyExtractedDocuments++;
                    }
                }
                
                pipeQueue = nextPipeQueue;
                nextPipeQueue = new HashMap<>();
            }
        }else{
            HashMap<File, Pair<Instances, Relations>> pipeQueue = new HashMap<>();
            HashMap<File, Pair<Instances, Relations>> nextPipeQueue = new HashMap<>();
            
            for (IExtractorPipelineElement extractorPipelineElement: extractorPipelineElements) {
                this.currentlyRunningExtractor = extractorPipelineElement;
                
                if (((Extractor)extractorPipelineElement).getExtractorHandler().getPluginName().equalsIgnoreCase("Extractor Machine Learning File Reader")) {
                    HashMap<File, Pair<Instances, Relations>> extractedRelations = extractorPipelineElement.readML();
                    nextPipeQueue.putAll(extractedRelations);
                    totalDocumentsToBeExtracted += extractedRelations.size();
                } else if (((Extractor)extractorPipelineElement).getExtractorHandler().getPluginName().equalsIgnoreCase("Extractor File Writer")) {
                    for (Map.Entry<File, Pair<Instances, Relations>> pair : pipeQueue.entrySet()) {
                        extractorPipelineElement.write(pair.getKey(), pair.getValue().getRight());
                    }
                } else {
                    this.totalProcessedExtractor++;
                    
                    Iterator<Map.Entry<File, Pair<Instances, Relations>>> it = pipeQueue.entrySet().iterator();
                    
                    currentlyExtractedDocuments = 0;
                    
                    while (it.hasNext()) {
                        Map.Entry<File, Pair<Instances, Relations>> pair = it.next();
                        
                        HashMap<File, Pair<Instances, Relations>> extracted = extractorPipelineElement.execute(pair.getKey(), pair.getValue().getLeft(), pair.getValue().getRight());
                        
                        nextPipeQueue.putAll(extracted);
                        currentlyExtractedDocuments++;
                    }
                }
                
                pipeQueue = nextPipeQueue;
                nextPipeQueue = new HashMap<>();
            }
        }
        
    }
    
    @Override
    public void didExecute() {
        if (this.getNumberOfExtractors() > 0) {
            extractorPipelineHook.didExecute();
        }
    }
    
    public int getTotalProcessedExtractor() {
        return totalProcessedExtractor;
    }
    
    public int getTotalDocumentsToBeExtracted() {
        return totalDocumentsToBeExtracted;
    }
    
    public int getCurrentlyExtractedDocuments() {
        return currentlyExtractedDocuments;
    }
    
    public IExtractorPipelineElement getCurrentlyRunningExtractor() {
        return currentlyRunningExtractor;
    }
    
    public ExtractorPipeline setExtractorPipelineHook(IExtractorPipelineHook extractorPipelineHook) {
        this.extractorPipelineHook = extractorPipelineHook;
        return this;
    }
}
