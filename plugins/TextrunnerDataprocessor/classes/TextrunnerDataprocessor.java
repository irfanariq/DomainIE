package classes;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;
import id.ac.itb.nlp.POSTagger;
import id.ac.itb.nlp.PhraseChunker;
import id.ac.itb.nlp.SentenceTokenizer;
import id.ac.itb.openie.relation.Relation;
import id.ac.itb.openie.relation.Relations;
import id.ac.itb.openie.utils.Utilities;
import id.ac.itb.openie.dataprocessor.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import id.ac.itb.openie.utils.*;
import java.util.HashMap;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.lang3.tuple.Pair;

public class TextrunnerDataprocessor extends Plugin {

    public TextrunnerDataprocessor(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class TextrunnerDataprocessorHandler extends IDataprocessorExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();


        public String getPluginName() {
            String name = "Textrunner Dataprocessor";
            return name;
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public Instances dataprocess(Relations relations, ArrayList<IFeatureHandler> listFeatures) throws Exception{
            //Relations rels = new Relations(documentToRelations(file));
            Instances out= getInstances(listFeatures);
            for(Relation rel:relations.getRelations()){
                Instance instance=featureRelation(rel,out,listFeatures);
                out.add(instance);
            }
            return out;
        }

        @Override
        public Relations documentToRelations(File f){
            Relations relations= new Relations();
            SentenceTokenizer sentenceTokenizer = new SentenceTokenizer();
            String document= Utilities.getFileContent(f);

            ArrayList<String> sentences = sentenceTokenizer.tokenizeSentence(document);
            PhraseChunker phraseChunker = new PhraseChunker();
            for(int b=0;b<sentences.size();b++){
                ArrayList<String[]> tags= phraseChunker.chunk(sentences.get(b));
                for(int i=0;i<tags.size();i++){
                    if(tags.get(i)[1].equalsIgnoreCase("NP")){
                        for(int k=i+1;k<tags.size();k++){
                             if(tags.get(k)[1].equalsIgnoreCase("NP")){
                                 if(k==i+1) break;
                                 Relation rel = new Relation(tags.get(i)[0].trim(),makeString(tags,i,k).trim(),tags.get(k)[0].trim(),f.getAbsolutePath(),b,sentences.get(b));
                                 relations.addRelation(rel);
                                 break;
                             }
                        }
                    }
                }                       
            } 
            return relations;
        }


        public String makeString(ArrayList<String[]> tag,int i, int k){
            String s="";
            for(int a=i+1;a<k;a++){
                s=s+" "+tag.get(a)[0].trim();
            }
            return s.replaceAll(",","");
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

            Instances instances = new Instances(getPluginName(),listatt,0);
            instances.setClass(classtarget);
            return instances;
        }


        public void dataprocessorWillRun() {
            System.out.println(this.getPluginName() + " will run..");
        }

        public void dataprocessorDidRun() {
            System.out.println(this.getPluginName() + " did run..");
        }


    }
}
