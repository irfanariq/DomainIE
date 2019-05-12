package id.ac.itb.openie.classrecognizer;

import id.ac.itb.openie.models.*;
import id.ac.itb.openie.pipeline.IDomainIePipelineElement;
import id.ac.itb.openie.plugins.PluginLoader;
import org.apache.commons.lang3.tuple.Pair;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassRecognizerPipeline implements IDomainIePipelineElement {

    private List<IClassRecognizerPipelineElement> iClassRecognizerPipelineElements = new ArrayList<>();
    private List<IClassRecognizerPipelineElement> iClassRecognizerDomainDataPipelineElements = new ArrayList<>();
    private int totalProcessedClassRecognizer = 0;
    private int totalRelationToBeRecognized = 0;
    private int currentlyRecognizedRelation = 0;
    private int totalDomainDataToBeRecognized = 0;
    private int currentlyRecognizedDomainData = 0;
    private IClassRecognizerPipelineElement currentlyRunningClassRecognizer = null;
    private IClassRecognizerPipelineHook iClassRecognizerPipelineHook= null;

    private void addDefaultReaderAndWriter() {
        if (iClassRecognizerPipelineElements.size() > 0) {
            PluginLoader pluginLoader = new PluginLoader();
            pluginLoader.registerAvailableExtensions(IClassRecognizerHandler.class);

            System.out.println(pluginLoader.getAllExtensions(IClassRecognizerHandler.class).toString());
            System.out.println("add default reader writer");
            // Prepend preprocessor file reader if not exist
            for (Object iClassRecognizerHandler: pluginLoader.getAllExtensions(IClassRecognizerHandler.class)) {
                IClassRecognizerHandler classRecognizerHandler = (IClassRecognizerHandler) iClassRecognizerHandler;
                String pluginName = classRecognizerHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Class Recognizer File Reader")) {
                    ClassRecognizer classRecognizer = new ClassRecognizer(classRecognizerHandler);
                    iClassRecognizerPipelineElements.add(0, classRecognizer);
                }
            }

            for (Object iClassRecognizerHandler: pluginLoader.getAllExtensions(IClassRecognizerHandler.class)) {
                IClassRecognizerHandler classRecognizerHandler = (IClassRecognizerHandler) iClassRecognizerHandler;
                String pluginName = classRecognizerHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Class Recognizer File Writer")) {
                    ClassRecognizer classRecognizer = new ClassRecognizer(classRecognizerHandler);
                    iClassRecognizerPipelineElements.add(classRecognizer);
                }
            }

            // Domain Data
            for (Object iClassRecognizerHandler: pluginLoader.getAllExtensions(IClassRecognizerHandler.class)) {
                IClassRecognizerHandler classRecognizerHandler = (IClassRecognizerHandler) iClassRecognizerHandler;
                String pluginName = classRecognizerHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Class Recognizer Domain Data File Reader")) {
                    ClassRecognizer classRecognizer = new ClassRecognizer(classRecognizerHandler);
                    iClassRecognizerDomainDataPipelineElements.add(0, classRecognizer);
                }
            }

            for (Object iClassRecognizerHandler: pluginLoader.getAllExtensions(IClassRecognizerHandler.class)) {
                IClassRecognizerHandler classRecognizerHandler = (IClassRecognizerHandler) iClassRecognizerHandler;
                String pluginName = classRecognizerHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Class Recognizer Domain Data File Writer")) {
                    ClassRecognizer classRecognizer = new ClassRecognizer(classRecognizerHandler);
                    iClassRecognizerDomainDataPipelineElements.add(classRecognizer);
                }
            }
        }
    }

    @Override
    public void willExecute() {
        if (this.getNumberOfClassRecognizer() > 0) {
            iClassRecognizerPipelineHook.willExecute();
        }
    }

    private int getNumberOfClassRecognizer() {
        int ret = 0;
        for (IClassRecognizerPipelineElement extractorPipelineElement: iClassRecognizerPipelineElements) {
            if (((ClassRecognizer)extractorPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer File Reader")) {
                continue;
            } else if (((ClassRecognizer)extractorPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer File Writer")) {
                continue;
            } else {
                ret++;
            }
        }

        return ret;
    }

    @Override
    public void execute() throws Exception {
        System.out.println("Class Recognizer pipelinse is running ... ");

        System.out.println(iClassRecognizerPipelineElements.size());
        System.out.println(iClassRecognizerDomainDataPipelineElements.size());
        addDefaultReaderAndWriter();
        System.out.println(iClassRecognizerPipelineElements.size());
        System.out.println(iClassRecognizerDomainDataPipelineElements.size());

        HashMap<File, Pair<Relations, RecognizedRelations>> pipeQueue = new HashMap<>();
        HashMap<File, Pair<Relations, RecognizedRelations>> nextPipeQueue = new HashMap<>();

        // TODO implement this method
        // recognize relation
        try {
            for (IClassRecognizerPipelineElement classRecognizerPipelineElement: iClassRecognizerPipelineElements) {
                this.currentlyRunningClassRecognizer = classRecognizerPipelineElement;

                if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer File Reader")) {
                    System.out.println("cr pipeline - read relation");
                    HashMap<File, Pair<Relations, RecognizedRelations>> extractedRelations = classRecognizerPipelineElement.read();
                    nextPipeQueue.putAll(extractedRelations);
                    totalRelationToBeRecognized = extractedRelations.size();
                } else if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer File Writer")) {
                    System.out.println("cr pipeline - write recognized relation");
                    for (Map.Entry<File, Pair<Relations, RecognizedRelations>> pair : pipeQueue.entrySet()) {
                        classRecognizerPipelineElement.write(pair.getKey(), pair.getValue().getValue());
                    }
                } else {
                    System.out.println("cr pipeline - recognize relation");
                    System.out.println(((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName());
                    this.totalProcessedClassRecognizer++;

                    currentlyRecognizedRelation = 0;

                    for (Map.Entry<File, Pair<Relations, RecognizedRelations>> pair : pipeQueue.entrySet()) {
                        nextPipeQueue.putAll(classRecognizerPipelineElement.execute(pair.getKey(), pair.getValue().getKey()));
                        currentlyRecognizedRelation++;
                    }
                }

                pipeQueue = nextPipeQueue;
                nextPipeQueue = new HashMap<>();
            }

            totalProcessedClassRecognizer = 0;
            currentlyRecognizedDomainData = 0;
            HashMap<File, DomainDatas> pipeQueueDomainData = new HashMap<>();
            HashMap<File, DomainDatas> nextPipeQueueDomainData = new HashMap<>();
            // recognize domain data
            for (IClassRecognizerPipelineElement classRecognizerPipelineElement: iClassRecognizerDomainDataPipelineElements) {
                this.currentlyRunningClassRecognizer = classRecognizerPipelineElement;

                if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer Domain Data File Reader")) {
                    System.out.println("cr pipeline - read domain data");
                    HashMap<File, DomainDatas> extractedRelations = classRecognizerPipelineElement.readDomainData();
                    System.out.println("cr pipeline - read domain data done - " + extractedRelations.size());
                    nextPipeQueueDomainData.putAll(extractedRelations);
                    totalDomainDataToBeRecognized  = extractedRelations.size();
                } else if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer Domain Data File Writer")) {
                    System.out.println("cr pipeline - write recognized domain data " + pipeQueueDomainData.size());
                    for (Map.Entry<File, DomainDatas> pair : pipeQueueDomainData.entrySet()) {
                        classRecognizerPipelineElement.writeDomainData(pair.getKey(), pair.getValue());
                    }
                } else {
                    System.out.println("cr pipeline - recognize domain data");
                    System.out.println(((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName());
                    this.totalProcessedClassRecognizer++;

                    currentlyRecognizedDomainData = 0;

                    for (Map.Entry<File, DomainDatas> pair : pipeQueueDomainData.entrySet()) {
                        nextPipeQueueDomainData.putAll(classRecognizerPipelineElement.executeDomainData(pair.getKey(), pair.getValue()));
                        currentlyRecognizedDomainData++;
                    }
                }
                pipeQueueDomainData = nextPipeQueueDomainData;
                nextPipeQueueDomainData = new HashMap<>();
            }
        }catch (Exception e){
            System.out.println("cr pipeline catch exception");
            System.out.println(e);
            // TODO exception ketangkep tapi gabisa nampilin alert disini
            // new Alert(e.toString());
        }
    }

    @Override
    public void didExecute() {
        if (this.getNumberOfClassRecognizer() > 0) {
            iClassRecognizerPipelineHook.didExecute();
        }
    }

    public ClassRecognizerPipeline addPipelineElementInput(IClassRecognizerPipelineElement cr) {
        this.iClassRecognizerDomainDataPipelineElements.add(0, cr);
        return this;
    }

    public ClassRecognizerPipeline addPipelineElement(IClassRecognizerPipelineElement cr) {
        this.iClassRecognizerPipelineElements.add(cr);
        this.iClassRecognizerDomainDataPipelineElements.add(cr);
        return this;
    }

    public ClassRecognizerPipeline setClassRecognizerPipelineHook(IClassRecognizerPipelineHook iClassRecognizerPipelineHook) {
        this.iClassRecognizerPipelineHook = iClassRecognizerPipelineHook;
        return this;
    }
}
