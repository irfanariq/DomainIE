package id.ac.itb.openie.classrecognizer;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Relations;
import org.apache.commons.lang3.tuple.Pair;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public interface IClassRecognizerPipelineElement {
    public HashMap<String, ArrayList<String>> getWordList() throws Exception;
    public HashMap<String, ArrayList<String>> getPatternList() throws Exception;
    public HashMap<File, Pair<Relations, RecognizedRelations>> read() throws Exception;
    public void write(File file,RecognizedRelations recognizedRelations) throws Exception;
    public HashMap<File, DomainDatas> readDomainData() throws Exception;
    public void writeDomainData(File file, DomainDatas domainDatas) throws Exception;
}
