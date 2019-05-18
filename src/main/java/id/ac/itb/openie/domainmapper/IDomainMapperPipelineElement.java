package id.ac.itb.openie.domainmapper;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.DomainRelations;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Rule;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public interface IDomainMapperPipelineElement {
    public HashSet<Rule> execute(HashMap<File, DomainDatas> fileDomainDatasHashMap) throws Exception;
    public String getRuleName();

    public HashMap<File, RecognizedRelations> read() throws Exception;
    public void write(File file, DomainRelations recognizedRelations) throws Exception;

    public HashMap<File, DomainDatas> readDomainData() throws Exception;
    public void writeRule(String file, HashSet<Rule> rule) throws Exception;
}
