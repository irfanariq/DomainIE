package id.ac.itb.openie.domainmapper;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.DomainRelations;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Rule;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class DomainMapper implements IDomainMapperPipelineElement{

    private IDomainMapperHandler domainMapperHandler;

    public DomainMapper(IDomainMapperHandler domainMapperHandler) {
        this.domainMapperHandler = domainMapperHandler;
    }

    public DomainMapper() {

    }

    public DomainMapper setDomainMapperHandler(IDomainMapperHandler domainMapperHandler) {
        this.domainMapperHandler = domainMapperHandler;
        return this;
    }

    public IDomainMapperHandler getDomainMapperHandler() {
        return domainMapperHandler;
    }

    @Override
    public HashSet<Rule> execute(HashMap<File, DomainDatas> fileDomainDatasHashMap) throws Exception {
        return this.domainMapperHandler.generateRule(fileDomainDatasHashMap);
    }

    @Override
    public String getRuleName() {
        String ret = this.domainMapperHandler.getPluginName();
        if (this.domainMapperHandler.getAvailableConfigurations().containsKey("Rule Name")) {
            ret = this.domainMapperHandler.getAvailableConfigurations().get("Rule Name");
        }
        return ret;
    }

    @Override
    public HashMap<File, RecognizedRelations> read() throws Exception {
        return this.domainMapperHandler.read();
    }

    @Override
    public void write(File file, DomainRelations domainRelations) throws Exception {
        this.domainMapperHandler.write(file, domainRelations);
    }

    @Override
    public HashMap<File, DomainDatas> readDomainData() throws Exception {
        return this.domainMapperHandler.readDomainData();
    }

    @Override
    public void writeRule(String fileName, HashSet<Rule> rules) throws Exception {
        this.domainMapperHandler.writeRule(fileName, rules);
    }


    @Override
    public String toString() {
        if (getDomainMapperHandler().getAvailableConfigurations() != null) {
            String inputDirectory = getDomainMapperHandler().getAvailableConfigurations().get("Input Directory");
            String outputDirectory = getDomainMapperHandler().getAvailableConfigurations().get("Output Directory");

            if (inputDirectory != null) {
                return this.getDomainMapperHandler().getPluginName() + " : " + inputDirectory;
            } else if (outputDirectory != null) {
                return this.getDomainMapperHandler().getPluginName() + " : "  + outputDirectory;
            }
        }

        return this.getDomainMapperHandler().getPluginName();
    }
}
