package id.ac.itb.openie.extractor;

import id.ac.itb.openie.relation.Relations;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.HashMap;
import weka.core.Instances;


public class Extractor implements IExtractorPipelineElement {

    private IExtractorHandler extractorHandler;
    private int totalDocumentExtracted = 0;

    public IExtractorHandler getExtractorHandler() {
        return extractorHandler;
    }

    public Extractor setExtractorHandler(IExtractorHandler extractorHandler) {
        this.extractorHandler = extractorHandler;
        return this;
    }

    public String toString() {
        if (getExtractorHandler().getAvailableConfigurations() != null) {
            String inputDirectory = getExtractorHandler().getAvailableConfigurations().get("Input Directory");
            String outputDirectory = getExtractorHandler().getAvailableConfigurations().get("Output Directory");

            if (inputDirectory != null) {
                return this.getExtractorHandler().getPluginName() + " : " + inputDirectory;
            } else if (outputDirectory != null) {
                return this.getExtractorHandler().getPluginName() + " : "  + outputDirectory;
            }
        }

        return this.getExtractorHandler().getPluginName();
    }

    @Override
    public HashMap<File, Pair<String, Relations>> execute(File file, String payload, Relations relations) throws Exception {
        HashMap<File, Pair<String, Relations>> output =  new HashMap<>();
        output.put(file, Pair.of(payload, this.getExtractorHandler().extract(file, payload, relations)));

        return output;
    }
    
    public HashMap<File, Pair<Instances, Relations>> execute(File file, Instances instances, Relations relations) throws Exception{
        HashMap<File, Pair<Instances, Relations>> output =  new HashMap<>();
        output.put(file,Pair.of(instances, this.getExtractorHandler().extract(file, instances, relations)));

        return output;
    }


    @Override
    public HashMap<File, Pair<String, Relations>> read() throws Exception {
        return this.getExtractorHandler().read();
    }
    
    @Override
    public HashMap<File, Pair<Instances, Relations>> readML() throws Exception {
        return this.getExtractorHandler().readML();
    }


    @Override
    public void write(File file, Relations extracted) throws Exception {
        this.getExtractorHandler().write(file, extracted);
    }

    public int getTotalDocumentExtracted() {
        return totalDocumentExtracted;
    }

    public Extractor setTotalDocumentExtracted(int totalDocumentExtracted) {
        this.totalDocumentExtracted = totalDocumentExtracted;
        return this;
    }
}
