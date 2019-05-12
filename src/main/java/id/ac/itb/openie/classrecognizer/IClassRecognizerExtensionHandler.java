package id.ac.itb.openie.classrecognizer;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Relations;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.HashMap;

public abstract class IClassRecognizerExtensionHandler implements IClassRecognizerHandler{

    public HashMap<File, Pair<Relations, RecognizedRelations>> read() throws Exception {
        return null;
   }

    public void write(File file, RecognizedRelations recognizedRelations) throws Exception {}

    public HashMap<File, DomainDatas> readDomainData() throws Exception {
        return null;
    }

    public void writeDomainData(File file, DomainDatas domainData) throws Exception {}

    public String toString() {
        return this.getPluginName();
    }
}
