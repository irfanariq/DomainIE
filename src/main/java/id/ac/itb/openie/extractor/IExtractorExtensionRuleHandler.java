package id.ac.itb.openie.extractor;

import id.ac.itb.openie.models.Relations;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.HashMap;
import weka.core.Instances;


public abstract class IExtractorExtensionRuleHandler implements IExtractorHandler {

    private int extractorType;
    
    public HashMap<File, Pair<String, Relations>> read() throws Exception {
        return null;
    }
      
    public Relations extract(Instances instance, Relations extracted) throws Exception{
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
        return 0;
    }
    
    public void extractorWillRun() {
        System.out.println(this.getPluginName() + " will run..");
    }
    
    public void extractorDidRun() {
        System.out.println(this.getPluginName() + " did run..");
    }
    
    
}
