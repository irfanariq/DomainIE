/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;

import id.ac.itb.openie.relation.Relations;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import ro.fortsoft.pf4j.ExtensionPoint;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public interface IDataprocessorHandler extends ExtensionPoint, Serializable{
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
     * @param document String containing original preprocessed file content
     * @param instances List of features extracted from data
     * @return extracted relations
     * @throws Exception
     */
    public Instances dataprocess( Relations relations, ArrayList<IFeatureHandler> listFeatures) throws Exception;

    public Relations documentToRelations(File f) throws Exception;
    
    /**
     *
     * @return List of file and its content
     * @throws Exception
     */
    public HashMap<File, Pair<Relations,Instances>> read() throws Exception;

    /**
     *
     * @param file output title
     * @param instances output 
     * @throws Exception
     */
    public void write(File file, Relations relation, Instances instances) throws Exception;

    /**
     * Hook to be called before extractor will run
     */
    public void dataprocessorWillRun();

    /**
     * Hook to be called before extractor have run
     */
    public void dataprocessorDidRun();
    

}
