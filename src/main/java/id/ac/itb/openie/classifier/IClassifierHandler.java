/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.classifier;

import id.ac.itb.openie.relation.Relations;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.ExtensionPoint;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public interface IClassifierHandler extends ExtensionPoint, Serializable{
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
     * @param dataset datatest in Instances representation which want to be classified
     * @param datatraining data training in Instances representation
     * @return datatest as Instances with class target value
     * @throws Exception
     */
    public Instances classify(Instances datatest, Instances datatraining) throws Exception;

    /**
     *
     * @return List of file and its content(in instances representation)
     * @throws Exception
     */
    public HashMap<File, Instances> read() throws Exception;

    /**
     *
     * @param file file where instances will be written (in arff format)
     * @param instances instances of labeled data test )
     * @throws Exception
     */
    public void write(File file, Instances instances) throws Exception;
}
