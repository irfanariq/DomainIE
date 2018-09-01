/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.classifier;

import id.ac.itb.openie.relation.Relations;
import java.io.File;
import java.util.ArrayList;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public abstract class IClassifierFileHandler implements IClassifierHandler{
    public Instances classify(Instances dataset, Instances datatraining) throws Exception {
        return null;
    }
}
