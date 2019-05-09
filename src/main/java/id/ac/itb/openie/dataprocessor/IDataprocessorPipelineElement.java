/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;

import id.ac.itb.openie.models.Relations;
import java.io.File;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public interface IDataprocessorPipelineElement {
    public HashMap<File, Pair<Relations, Instances>> execute(File file, Relations payload, Instances instances) throws Exception;
    public HashMap<File, Pair<Relations, Instances>> read() throws Exception;
    public void write(File file, Relations relation, Instances instances) throws Exception;
}
