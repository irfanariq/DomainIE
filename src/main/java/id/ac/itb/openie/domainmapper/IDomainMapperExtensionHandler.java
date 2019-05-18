package id.ac.itb.openie.domainmapper;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.DomainRelations;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Rule;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public abstract class IDomainMapperExtensionHandler implements IDomainMapperHandler{
    public HashMap<File, RecognizedRelations> read() throws Exception {
        return null;
    }

    public void write(File file, DomainRelations domainRelations) throws Exception {}

    public HashMap<File, DomainDatas> readDomainData() throws Exception {
        return null;
    }

    public void writeRule(String fileName, HashSet<Rule> rules) throws Exception {}

    public String toString() {
        return this.getPluginName();
    }
}
