package id.ac.itb.openie.extractor;

import id.ac.itb.openie.relation.Relations;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.HashMap;
import weka.core.Instances;


public interface IExtractorPipelineElement {
    public HashMap<File, Pair<String, Relations>> execute(File file, String payload, Relations relations) throws Exception;
    public HashMap<File, Pair<Instances, Relations>> execute(File file, Instances instances, Relations relations) throws Exception;

    public HashMap<File, Pair<String, Relations>> read() throws Exception;
    public HashMap<File, Pair<Instances, Relations>> readML() throws Exception;

    public void write(File file, Relations extracted) throws Exception;
}
