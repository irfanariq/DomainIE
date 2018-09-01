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
        public Relations documentToRelations(String document){
            Relations relations= new Relations();
            SentenceTokenizer sentenceTokenizer = new SentenceTokenizer();

            ArrayList<String> sentences = sentenceTokenizer.tokenizeSentence(document);
            PhraseChunker phraseChunker = new PhraseChunker();
            for(int b=0;b<sentences.size();b++){
                ArrayList<String[]> tags= phraseChunker.chunk(sentences.get(b));
                for(int i=0;i<tags.size();i++){
                    if(tags.get(i)[1].equalsIgnoreCase("NP")){
                        for(int k=i+1;k<tags.size();k++){
                             if(tags.get(k)[1].equalsIgnoreCase("NP")){
                                 if(k==i+1) break;
                                 Relation rel = new Relation(tags.get(i)[0].trim(),makeString(tags,i,k).trim(),tags.get(k)[0].trim(),"",b,sentences.get(b));
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

    }
}
