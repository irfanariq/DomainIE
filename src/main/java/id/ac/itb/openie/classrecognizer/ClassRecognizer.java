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
    public HashMap<File, Pair<Relations, RecognizedRelations>> execute(File file, Relations dataset) throws Exception {
        HashMap<String, ArrayList<String>> listKata = this.getClassRecognizerHandler().getWordList();
        HashMap<String, ArrayList<String>> listPola = this.getClassRecognizerHandler().getPatternList();
        System.out.println(listKata);

        HashMap<File, Pair<Relations, RecognizedRelations>> ret = new HashMap<>();

        // TODO recognize domain class in relation here

        Pair<Relations, RecognizedRelations> recRelatioins = Pair.of(dataset, new RecognizedRelations());

        for (Relation relasi: dataset.getRelations()) {
            RecognizedRelation recrel = new RecognizedRelation(relasi, listKata, listPola);
            recRelatioins.getValue().addRecogRelation(recrel);
        }
        ret.putIfAbsent(file, recRelatioins);

        return ret;
    }

    @Override
    public HashMap<File, DomainDatas> executeDomainData(File file, DomainDatas dataset) throws Exception {
        HashMap<String, ArrayList<String>> listKata = this.getClassRecognizerHandler().getWordList();
        HashMap<String, ArrayList<String>> listPola = this.getClassRecognizerHandler().getPatternList();
        System.out.println(listPola);

        HashMap<File,DomainDatas> ret = new HashMap<>();

        // TODO recognize domain class in relation here
        for (DomainData domainData: dataset.getDomainDatas()) {
            domainData.recognize(listKata, listPola);
        }
        ret.putIfAbsent(file, dataset);

        return ret;
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
