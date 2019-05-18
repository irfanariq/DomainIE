package id.ac.itb.openie.classrecognizer;

import id.ac.itb.openie.models.*;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ClassRecognizer implements IClassRecognizerPipelineElement{

    private IClassRecognizerHandler classRecognizerHandler;

    public ClassRecognizer() {

    }

    public ClassRecognizer setClassRecognizerHandler(IClassRecognizerHandler classRecognizerHandler) {
        this.classRecognizerHandler = classRecognizerHandler;
        return this;
    }

    public IClassRecognizerHandler getClassRecognizerHandler() {
        return classRecognizerHandler;
    }

    public ClassRecognizer(IClassRecognizerHandler iClassRecognizerHandler) {
        this.classRecognizerHandler = iClassRecognizerHandler;
    }

    @Override
    public HashMap<String, ArrayList<String>> getWordList() throws Exception {
        return this.classRecognizerHandler.getWordList();
    }

    @Override
    public HashMap<String, ArrayList<String>> getPatternList() throws Exception {
        return this.classRecognizerHandler.getPatternList();
    }

    @Override
    public HashMap<File, Pair<Relations, RecognizedRelations>> read() throws Exception {
        HashMap<File, Pair<Relations, RecognizedRelations>> ret = this.getClassRecognizerHandler().read();
        return ret;
    }

    @Override
    public void write(File file, RecognizedRelations recognizedRelations) throws Exception {
        this.getClassRecognizerHandler().write(file, recognizedRelations);
    }

    @Override
    public HashMap<File, DomainDatas> readDomainData() throws Exception {
        HashMap<File, DomainDatas> ret = this.getClassRecognizerHandler().readDomainData();
        return ret;
    }

    @Override
    public void writeDomainData(File file, DomainDatas domainDatas) throws Exception {
        this.getClassRecognizerHandler().writeDomainData(file, domainDatas);
    }

    @Override
    public String toString() {
        if (getClassRecognizerHandler().getAvailableConfigurations() != null) {
            String inputDirectory = getClassRecognizerHandler().getAvailableConfigurations().get("Input Directory");
            String outputDirectory = getClassRecognizerHandler().getAvailableConfigurations().get("Output Directory");

            if (inputDirectory != null) {
                return this.getClassRecognizerHandler().getPluginName() + " : " + inputDirectory;
            } else if (outputDirectory != null) {
                return this.getClassRecognizerHandler().getPluginName() + " : "  + outputDirectory;
            }
        }

        return this.getClassRecognizerHandler().getPluginName();
    }
}
