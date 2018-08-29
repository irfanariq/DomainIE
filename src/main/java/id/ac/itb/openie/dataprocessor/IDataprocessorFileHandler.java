/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;

import id.ac.itb.openie.relation.Relations;
import java.io.File;
import java.util.ArrayList;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public abstract class IDataprocessorFileHandler implements IDataprocessorHandler {
    public Instances dataprocess( Relations relation, ArrayList<IFeatureHandler> listFeatures) throws Exception {
        return null;
    }
    
    public Relations documentToRelations(File f) throws Exception{
        return null;
    }


}
