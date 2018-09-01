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
     * @param s String of corpus text
     * @return representation relations from corpus text that may be incomplete
     */
    public Relations documentToRelations(String s) throws Exception;
    
    /**
     *
     * @return List of file and its content in pair of Relations and Instances(Empty)
     * @throws Exception
     */
    public HashMap<File, Pair<Relations,Instances>> read() throws Exception;

    /**
     *
     * @param file output title
     * @param relation relation that have been processed
     * @param instances representation of featured relation, can be ? target value
     * @throws Exception
     */
    public void write(File file, Relations relation, Instances instances) throws Exception;

}
