/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;

import id.ac.itb.openie.relation.Relations;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Pair;
import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import weka.core.Instances;

/**
 *
 * @author yoga
 */
public class Dataprocessor implements IDataprocessorPipelineElement {
    
    private IDataprocessorHandler dataprocessorHandler;
    private ArrayList<IFeatureHandler> arrayFeature= new ArrayList();
    
    public Dataprocessor(){
        PluginManager pluginManager = new DefaultPluginManager();
        for (Object iHandler: pluginManager.getExtensions(IFeatureHandler.class)){
            IFeatureHandler fh= (IFeatureHandler) iHandler;
            arrayFeature.add(fh);
        }
        //System.out.println("arrayFeature " + arrayFeature.size());
    }
    
    public ArrayList<IFeatureHandler> getArrayFeature(){
        return arrayFeature;
    }
    
    public IDataprocessorHandler getDataprocessorHandler() {
        return dataprocessorHandler;
    }
    
    public Dataprocessor setDataprocessorHandler(IDataprocessorHandler dataprocessorHandler) {
        this.dataprocessorHandler = dataprocessorHandler;
        return this;
    }

    public String toString() {
        if (getDataprocessorHandler().getAvailableConfigurations() != null) {
            String inputDirectory = getDataprocessorHandler().getAvailableConfigurations().get("Input Directory");
            String outputDirectory = getDataprocessorHandler().getAvailableConfigurations().get("Output Directory");

            if (inputDirectory != null) {
                return this.getDataprocessorHandler().getPluginName() + " : " + inputDirectory;
            } else if (outputDirectory != null) {
                return this.getDataprocessorHandler().getPluginName() + " : "  + outputDirectory;
            }
        }

        return this.getDataprocessorHandler().getPluginName();
    }
    
    @Override
    public HashMap<File, Pair<Relations, Instances>> execute(File file, Relations relations, Instances instances) throws Exception {
        HashMap<File, Pair<Relations,Instances>> out = new HashMap<>();
        if(relations == null){
            out.put(file, Pair.of(this.getDataprocessorHandler().documentToRelations(file),this.getDataprocessorHandler().dataprocess(this.getDataprocessorHandler().documentToRelations(file), arrayFeature)));           
        }else{
            out.put(file, Pair.of(relations,this.getDataprocessorHandler().dataprocess(relations, arrayFeature)));
        }
        return out;
    }

    @Override
    public HashMap<File, Pair<Relations, Instances>> read() throws Exception {
        return this.getDataprocessorHandler().read();
    }

    @Override
    public void write(File file, Relations relations, Instances instances) throws Exception {
        this.getDataprocessorHandler().write(file, relations, instances);
    }
    
}
