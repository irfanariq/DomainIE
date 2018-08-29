package id.ac.itb.openie.extractor;

import id.ac.itb.openie.relation.Relations;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.HashMap;
import weka.core.Instances;


public abstract class IExtractorExtensionHandler implements IExtractorHandler {

    private int extractorType;
    
    public HashMap<File, Pair<String, Relations>> read() throws Exception {
        return null;
    }
    
    public Relations extract(File file, String document, Relations extracted) throws Exception{
        return null;
    }
    
    
    public Relations extract(File file, Instances instance, Relations extracted) throws Exception{
        return null;
    }

    public void write(File file, Relations extracted) throws Exception {}
    
    public HashMap<File, Pair<Instances, Relations>> readML() throws Exception{
        return null;
    }

    public String toString() {
        return this.getPluginName();
    }
        
    public int getExtractorType(){
        return -1;
    }


}
