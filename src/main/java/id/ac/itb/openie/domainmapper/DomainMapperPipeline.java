package id.ac.itb.openie.domainmapper;

import id.ac.itb.openie.models.*;
import id.ac.itb.openie.pipeline.IDomainIePipelineElement;
import id.ac.itb.openie.plugins.PluginLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DomainMapperPipeline implements IDomainIePipelineElement {

    private ArrayList<IDomainMapperPipelineElement> domainMapperPipelineElements = new ArrayList<>();
    private int totalDomainMapperProcessed = 0;
    private int currentDomainMapperProcessed = 0;
    private IDomainMapperPipelineElement currentlyDomainMapperProcess = null;
    private IDomainMapperPipelineHook domainMapperPipelineHook = null;
    private String rulesFileName = "";
    private boolean isGeneratingRules = true;
    private int totalFileRecognizedRelationToBeMapped = 0;
    private int currentFileMappedRecognizedRelation = 0;

    @Override
    public void execute() throws Exception {
        if (!domainMapperPipelineElements.isEmpty()) {
            PluginLoader pluginLoader = new PluginLoader();
            pluginLoader.registerAvailableExtensions(IDomainMapperHandler.class);

            HashMap<File, DomainDatas> domainDatasToGenerateRule = new HashMap<File, DomainDatas>();

            // READ DOMAIN DATA HERE
            for (Object iDomainMapperHandler : pluginLoader.getAllExtensions(IDomainMapperHandler.class)) {
                IDomainMapperHandler domainMapperHandler = (IDomainMapperHandler) iDomainMapperHandler;
                String pluginName = domainMapperHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Domain Mapper Domain Data File Reader")) {
                    DomainMapper domainMapper = new DomainMapper(domainMapperHandler);
                    domainDatasToGenerateRule = domainMapper.readDomainData();
                }
            }

            totalDomainMapperProcessed = domainMapperPipelineElements.size();
            currentDomainMapperProcessed = 0;
            HashSet<Rule> generatedRules = new HashSet<>();
            // GENERATE RULE HERE
            for (IDomainMapperPipelineElement runningDomainIePipelineElement : domainMapperPipelineElements) {
                // TODO generate rule in here
                this.currentlyDomainMapperProcess = runningDomainIePipelineElement;

                generatedRules.addAll(runningDomainIePipelineElement.execute(domainDatasToGenerateRule));
                rulesFileName += runningDomainIePipelineElement.getRuleName();

                currentDomainMapperProcessed++;
            }

            // WRITE RULE HERE
            for (Object iDomainMapperHandler : pluginLoader.getAllExtensions(IDomainMapperHandler.class)) {
                IDomainMapperHandler domainMapperHandler = (IDomainMapperHandler) iDomainMapperHandler;
                String pluginName = domainMapperHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Domain Mapper Rules File Writer")) {
                    DomainMapper domainMapper = new DomainMapper(domainMapperHandler);
                    domainMapper.writeRule(rulesFileName, generatedRules);
                }
            }

            isGeneratingRules = false;

            HashMap<File, RecognizedRelations> recognizedRelationsHashMap = new HashMap<>();
            // READ RECOGNIZED RELATION HERE
            for (Object iDomainMapperHandler : pluginLoader.getAllExtensions(IDomainMapperHandler.class)) {
                IDomainMapperHandler domainMapperHandler = (IDomainMapperHandler) iDomainMapperHandler;
                String pluginName = domainMapperHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Domain Mapper Recognized Relation File Reader")) {
                    DomainMapper domainMapper = new DomainMapper(domainMapperHandler);
                    recognizedRelationsHashMap = domainMapper.read();
                }
            }

            totalFileRecognizedRelationToBeMapped = recognizedRelationsHashMap.size();
            currentFileMappedRecognizedRelation = 0;

            HashMap<File, DomainRelations> domainRelationsHashMap = new HashMap<>();
            // MAPPING TO DOMAIN RELATION HERE
            for (Map.Entry<File, RecognizedRelations> pair : recognizedRelationsHashMap.entrySet()) {
                HashSet<DomainRelation> domainRelationResult = new HashSet<>();
                for (RecognizedRelation recognizedrelation : pair.getValue().getRecognizedRelations()) {
                    // MAPPING each recognized relation with each rule
                    for (Rule aturan : generatedRules) {
                        if (aturan.isSatisfyConstraint(recognizedrelation)) {
                            Pair<String, ArrayList<String>> domainRelation = aturan.mapToDomainRelation(recognizedrelation);
                            domainRelationResult.add(new DomainRelation(recognizedrelation.getOpenIERelation(), domainRelation.getLeft(), domainRelation.getRight()));
                        }
                    }
                }
                DomainRelations temp = new DomainRelations();
                temp.getDomainRelations().addAll(domainRelationResult);
                domainRelationsHashMap.put(pair.getKey(), temp);
                currentFileMappedRecognizedRelation++;
            }

            // WRITE DOMAIN RELATION HERE
            for (Object iDomainMapperHandler : pluginLoader.getAllExtensions(IDomainMapperHandler.class)) {
                IDomainMapperHandler domainMapperHandler = (IDomainMapperHandler) iDomainMapperHandler;
                String pluginName = domainMapperHandler.getPluginName();

                if (pluginName.equalsIgnoreCase("Domain Mapper Domain Relation File Writer")) {
                    DomainMapper domainMapper = new DomainMapper(domainMapperHandler);
                    for (Map.Entry<File, DomainRelations> pair : domainRelationsHashMap.entrySet()) {
                        domainMapper.write(pair.getKey(), pair.getValue());
                    }
                }
            }
        }else {
            System.out.println("Domain Mapper 0 element");
        }
    }

    public void setDomainMapperPipelineHook(IDomainMapperPipelineHook domainMapperPipelineHook) {
        this.domainMapperPipelineHook = domainMapperPipelineHook;
    }

    @Override
    public void willExecute() {
        if (domainMapperPipelineElements.size() > 0) {
            this.domainMapperPipelineHook.willExecute();
        }
    }

    @Override
    public void didExecute() {
        if (domainMapperPipelineElements.size() > 0) {
            this.domainMapperPipelineHook.didExecute();
        }
    }

    public boolean isGeneratingRules() {
        return isGeneratingRules;
    }

    public int getTotalFileRecognizedRelationToBeMapped() {
        return totalFileRecognizedRelationToBeMapped;
    }

    public int getCurrentFileMappedRecognizedRelation() {
        return currentFileMappedRecognizedRelation;
    }

    public DomainMapperPipeline addPipelineElement(IDomainMapperPipelineElement domainMapperPipelineElement) {
        domainMapperPipelineElements.add(domainMapperPipelineElement);
        return this;
    }

    public IDomainMapperPipelineElement getCurrentlyDomainMapperProcess() {
        return currentlyDomainMapperProcess;
    }

    public int getNumberOfDomainMapper() {
        return totalDomainMapperProcessed;
    }

    public int getCurrentDomainMapperProcessed() {
        return currentDomainMapperProcessed;
    }
}
