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
import ro.fortsoft.pf4j.ExtensionPoint;
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
     * @param document String containing original preprocessed file content
     * @param extracted List of relation extracted from previous extractor
     * @return extracted relations
     * @throws Exception
     */
    public Instances classify(File file, Instances dataset, Instances datatraining) throws Exception;

    /**
     *
     * @return List of file and its content
     * @throws Exception
     */
    public HashMap<File, Instances> read() throws Exception;

    /**
     *
     * @param extracted String containing original crawled content
     * @throws Exception
     */
    public void write(File file, Instances instances) throws Exception;

    /**
     * Hook to be called before extractor will run
     */
    public void classifierWillRun();

    /**
     * Hook to be called before extractor have run
     */
    public void classifierDidRun();
}
