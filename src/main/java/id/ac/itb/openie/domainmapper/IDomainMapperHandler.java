package id.ac.itb.openie.domainmapper;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.DomainRelations;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Rule;
import org.pf4j.ExtensionPoint;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public interface IDomainMapperHandler extends ExtensionPoint, Serializable {
    /**
     *
     * @return Plugin name
     */
    public String getPluginName();

    /**
     *
     * @return List of configuration name with its default value
     */
    public HashMap<String, String> getAvailableConfigurations();

    /**
     *
     * @param key Configuration name
     * @param value Configuration value
     */
    public void setAvailableConfigurations(String key, String value);

    /**
     *
     * @param fileDomainDatasHashMap domain data file to generate rule
     * @return set of rule to mapping Relation to DomainRelation
     * @throws Exception
     */
    public HashSet<Rule> generateRule(HashMap<File, DomainDatas> fileDomainDatasHashMap) throws Exception;

    /**
     *
     * @return List of file and its content(in recognized relations)
     * @throws Exception
     */
    public HashMap<File, RecognizedRelations> read() throws Exception;

    /**
     *
     * @param file file where domain relations will be written (in txt format)
     * @param domainRelations of domain relations that has been created
     * @throws Exception
     */
    public void write(File file, DomainRelations domainRelations) throws Exception;

    /**
     *
     * @return List of file and its content(in domaindata)
     * @throws Exception
     */
    public HashMap<File, DomainDatas> readDomainData() throws Exception;

    /**
     *
     * @param fileName name file where rule will be written (in txt format)
     * @param rules of relations that has been recognized )
     * @throws Exception
     */
    public void writeRule(String fileName, HashSet<Rule> rules) throws Exception;
}
