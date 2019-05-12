package id.ac.itb.openie.classrecognizer;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Relations;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.ExtensionPoint;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public interface IClassRecognizerHandler extends ExtensionPoint, Serializable {
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
     * @return listkata recognize class domain
     * @throws Exception
     */
    public HashMap<String, ArrayList<String>> getWordList() throws Exception;

    /**
     *
     * @return listpattern to recognize class domain
     * @throws Exception
     */
    public HashMap<String, ArrayList<String>> getPatternList() throws Exception;

    /**
     *
     * @return List of file and its content(in relations)
     * @throws Exception
     */
    public HashMap<File, Pair<Relations, RecognizedRelations>> read() throws Exception;

    /**
     *
     * @param file file where instances will be written (in txt format)
     * @param recognizedRelations of relations that has been recognized )
     * @throws Exception
     */
    public void write(File file, RecognizedRelations recognizedRelations) throws Exception;

    /**
     *
     * @return List of file and its content(in relations)
     * @throws Exception
     */
    public HashMap<File, DomainDatas> readDomainData() throws Exception;

    /**
     *
     * @param file file where instances will be written (in txt format)
     * @param domainData of relations that has been recognized )
     * @throws Exception
     */
    public void writeDomainData(File file, DomainDatas domainData) throws Exception;
}
