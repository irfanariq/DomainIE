package id.ac.itb.openie.extractor;

import id.ac.itb.openie.relation.Relations;

import java.io.File;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import weka.core.Instances;


public abstract class IExtractorFileHandler implements IExtractorHandler {

    public Relations extract(File file, String document, Relations extracted) throws Exception {
        return null;
    }
    
    public Relations extract(File file, Instances instance, Relations extracted) throws Exception{
        return null;
    }

    public HashMap<File, Pair<String, Relations>> read() throws Exception{
        return null;
    }
    
    public HashMap<File, Pair<Instances, Relations>> readML() throws Exception{
        return null;
    }
    public int getExtractorType(){
        return -1;
    }

}
