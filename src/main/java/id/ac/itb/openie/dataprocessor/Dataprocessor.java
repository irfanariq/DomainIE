/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;

import id.ac.itb.openie.relation.Relation;
import id.ac.itb.openie.relation.Relations;
import id.ac.itb.openie.utils.Utilities;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
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
    
    public Instances dataprocess(Relations relations, ArrayList<IFeatureHandler> listFeatures) throws Exception{
        //Relations rels = new Relations(documentToRelations(file));
        Instances out= getInstances(listFeatures);
        for(Relation rel:relations.getRelations()){
            Instance instance=featureRelation(rel,out,listFeatures);
            out.add(instance);
        }
        return out;
    }
    
    public Instances getInstances(ArrayList<IFeatureHandler> listFeatures){
        
        ArrayList<Attribute> listatt = new ArrayList<>();
        for(IFeatureHandler feature:listFeatures){
            Pair<String,Object> typeval = feature.getAttributeType();
            Attribute att;
            if(typeval.getLeft().equalsIgnoreCase("numeric")){
                att=new Attribute(feature.getFeatureName());
                listatt.add(att);
            } else if(typeval.getLeft().equalsIgnoreCase("nominal")){
                List<String> val=(List<String>) typeval.getRight();
                att=new Attribute(feature.getFeatureName(),val);
                listatt.add(att);
            } else if(typeval.getLeft().equalsIgnoreCase("string")){
                att=new Attribute(feature.getFeatureName(),(List<String>)null);
                listatt.add(att);
            }
            
        }
        List<String> yesnovalues = Arrays.asList("yes","no");
        //put classtarget last in the list
        Attribute classtarget = new Attribute("classtarget", yesnovalues);
        listatt.add(classtarget);
        
        Instances instances = new Instances(dataprocessorHandler.getPluginName(),listatt,0);
        instances.setClass(classtarget);
        return instances;
    }
    
    public Instance featureRelation(Relation rel, Instances instances,ArrayList<IFeatureHandler> listFeatures){
        Instance newins=new DenseInstance(listFeatures.size()+1);
        newins.setDataset(instances);
        int x=0;
        for(IFeatureHandler feature:listFeatures){
            Pair<String,Object> typeval = feature.getAttributeType();
            if(typeval.getLeft().equalsIgnoreCase("numeric")){
                newins.setValue(x,(Double)feature.calculate(rel));
            } else if(typeval.getLeft().equalsIgnoreCase("nominal")){
                newins.setValue(x,(String)feature.calculate(rel));
            } else if(typeval.getLeft().equalsIgnoreCase("string")){
                newins.setValue(x,(String)feature.calculate(rel));
            }
            x=x+1;
        }
        
        if (rel.getClassTarget()!= null){
            if (rel.getClassTarget()) newins.setValue(x,"yes");
            else newins.setValue(x,"no");
        }
        
        return newins;
    }
    
    @Override
    public HashMap<File, Pair<Relations, Instances>> execute(File file, Relations relations, Instances instances) throws Exception {
        HashMap<File, Pair<Relations,Instances>> out = new HashMap<>();
        String filecontent= Utilities.getFileContent(file);
        if(relations == null){
            Relations rels=this.getDataprocessorHandler().documentToRelations(filecontent);
            rels.setFileRelations(file.getAbsolutePath());
            out.put(file, Pair.of(rels,dataprocess(this.getDataprocessorHandler().documentToRelations(filecontent), arrayFeature)));           
        }else{
            out.put(file, Pair.of(relations,dataprocess(relations, arrayFeature)));
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
