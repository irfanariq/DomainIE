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
    private int totalFileRelationToBeRecognized = 0;
    private int currentlyRecognizedRelationFile = 0;
    private int totalDomainDataFileToBeRecognized = 0;
    private int currentlyRecognizedDomainDataFile = 0;
    private IClassRecognizerPipelineElement currentlyRunningClassRecognizer = null;
    private IClassRecognizerPipelineHook iClassRecognizerPipelineHook= null;
    private boolean isRecognizeRelatioin = true;

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

    public int getNumberOfClassRecognizer() {
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

        addDefaultReaderAndWriter();

        HashMap<File, Pair<Relations, RecognizedRelations>> pipeQueue = new HashMap<>();
        HashMap<File, Pair<Relations, RecognizedRelations>> nextPipeQueue = new HashMap<>();

        // TODO implement this method
        // recognize relation
        try {
            for (IClassRecognizerPipelineElement classRecognizerPipelineElement: iClassRecognizerPipelineElements) {
                this.currentlyRunningClassRecognizer = classRecognizerPipelineElement;

                if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer File Reader")) {
                    HashMap<File, Pair<Relations, RecognizedRelations>> extractedRelations = classRecognizerPipelineElement.read();
                    nextPipeQueue.putAll(extractedRelations);
                    totalFileRelationToBeRecognized = extractedRelations.size();
                } else if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer File Writer")) {
                    for (Map.Entry<File, Pair<Relations, RecognizedRelations>> pair : pipeQueue.entrySet()) {
                        classRecognizerPipelineElement.write(pair.getKey(), pair.getValue().getValue());
                    }
                } else {
                    this.totalProcessedClassRecognizer++;

                    HashMap<String, ArrayList<String>> listKata = classRecognizerPipelineElement.getWordList();
                    HashMap<String, ArrayList<String>> listPola = classRecognizerPipelineElement.getPatternList();

                    currentlyRecognizedRelationFile = 0;

                    for (Map.Entry<File, Pair<Relations, RecognizedRelations>> pair : pipeQueue.entrySet()) {
                        RecognizedRelations recRelatioins = new RecognizedRelations();

                        for (Relation relasi: pair.getValue().getKey().getRelations()) {
                            RecognizedRelation recrel = new RecognizedRelation(relasi, listKata, listPola);
                            recRelatioins.addRecogRelation(recrel);
                        }

                        nextPipeQueue.put(pair.getKey(), Pair.of(pair.getValue().getLeft(), recRelatioins));
                        currentlyRecognizedRelationFile++;
                    }
                }

                pipeQueue = nextPipeQueue;
                nextPipeQueue = new HashMap<>();
            }

            isRecognizeRelatioin = false;
            totalProcessedClassRecognizer = 0;
            currentlyRecognizedDomainDataFile = 0;
            HashMap<File, DomainDatas> pipeQueueDomainData = new HashMap<>();
            HashMap<File, DomainDatas> nextPipeQueueDomainData = new HashMap<>();
            // recognize domain data
            for (IClassRecognizerPipelineElement classRecognizerPipelineElement: iClassRecognizerDomainDataPipelineElements) {
                this.currentlyRunningClassRecognizer = classRecognizerPipelineElement;

                if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer Domain Data File Reader")) {
                    HashMap<File, DomainDatas> extractedRelations = classRecognizerPipelineElement.readDomainData();
                    nextPipeQueueDomainData.putAll(extractedRelations);
                    totalDomainDataFileToBeRecognized = extractedRelations.size();
                } else if (((ClassRecognizer) classRecognizerPipelineElement).getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer Domain Data File Writer")) {
                    for (Map.Entry<File, DomainDatas> pair : pipeQueueDomainData.entrySet()) {
                        classRecognizerPipelineElement.writeDomainData(pair.getKey(), pair.getValue());
                    }
                } else {
                    this.totalProcessedClassRecognizer++;

                    currentlyRecognizedDomainDataFile = 0;

                    HashMap<String, ArrayList<String>> listKata = classRecognizerPipelineElement.getWordList();
                    HashMap<String, ArrayList<String>> listPola = classRecognizerPipelineElement.getPatternList();

                    for (Map.Entry<File, DomainDatas> pair : pipeQueueDomainData.entrySet()) {

                        for (DomainData domainData: pair.getValue().getDomainDatas()) {
                            domainData.recognize(listKata, listPola);
                        }
                        nextPipeQueueDomainData.putIfAbsent(pair.getKey(), pair.getValue());
                        currentlyRecognizedDomainDataFile++;
                    }
                }
                pipeQueueDomainData = nextPipeQueueDomainData;
                nextPipeQueueDomainData = new HashMap<>();
            }
        }catch (Exception e){
            System.out.println("cr pipeline catch exception");
            System.out.println(e);
            // TODO exception ketangkep tapi gabisa nampilin alert disini
            // throw e;
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

    public int getTotalProcessedClassRecognizer() {
        return totalProcessedClassRecognizer;
    }

    public int getTotalFileRelationToBeRecognized() {
        return totalFileRelationToBeRecognized;
    }

    public int getCurrentlyRecognizedRelationFile() {
        return currentlyRecognizedRelationFile;
    }

    public int getTotalDomainDataFileToBeRecognized() {
        return totalDomainDataFileToBeRecognized;
    }

    public int getCurrentlyRecognizedDomainDataFile() {
        return currentlyRecognizedDomainDataFile;
    }

    public boolean isRecognizeRelatioin() {
        return isRecognizeRelatioin;
    }

    public IClassRecognizerPipelineElement getCurrentlyRunningClassRecognizer() {
        return currentlyRunningClassRecognizer;
    }
}
