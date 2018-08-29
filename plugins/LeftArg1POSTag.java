package classes;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;
import id.ac.itb.openie.relation.Relation;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.lang3.tuple.Pair;
import id.ac.itb.openie.dataprocessor.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import id.ac.itb.nlp.POSTagger;



public class LeftArg1POSTag extends Plugin {

    public LeftArg1POSTag(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class LeftArg1POSTagHandler implements IFeatureHandler {

    	public String getFeatureName(){
    		String name = "Left Arg1 POStag";
    		return name.replaceAll(" ","_");
    	}

    	public String getDescription(){
    		String description="POStag dari sebelah kiri arg1. berisi NULL jika arg1 awal kata";
    		return description;
    	}

    	public Pair<String,Object> getAttributeType(){
    		String type="nominal";
    		List<String> values=Arrays.asList("VBI","VBT","CDP","NULL","CC","CD","OD","DT","FW","IN","JJ","MD","NEG","NN","NNP","NND","PR","PRP","RB","RP","SC","SYM","UH","VB","WH","X","Z");
    		return Pair.of(type,values);
    	}

    	public Object calculate(Relation relation){
            String sentence=relation.getOriginSentence().trim();
            String arg1=relation.getRelationTriple().getLeft().trim();
    		String[] splittedsentence=sentence.split("\\s+");
            String[] splitarg1=arg1.split("\\s+");
            List<String> postag = Arrays.asList("VBI","VBT","CDP","NULL","CC","CD","OD","DT","FW","IN","JJ","MD","NEG","NN","NNP","NND","PR","PRP","RB","RP","SC","SYM","UH","VB","WH","X","Z");

            
            int iarg1=stringLocatedAt(splitarg1[0],splittedsentence);
            
            if(iarg1-1<0){
                return "NULL";
            }else{
                String tag=getTag(splittedsentence[iarg1-1],sentence);
                if(postag.indexOf(tag)==-1){
                    return "NULL";
                }else{
                    return getTag(splittedsentence[iarg1-1],sentence);
                }
            }	
        }

        public static String getTag(String s,String sentence){
            POSTagger postagger= new POSTagger();
            ArrayList<String[]> tagsentence = postagger.tag(sentence);
            String[] splitsentence=sentence.split("\\s+");
            return tagsentence.get(stringLocatedAt(s,splitsentence))[1];
        }
        

         public static int stringLocatedAt(String word, String[] sentence){
            for(int i=0;i<sentence.length;i++){
                if(sentence[i].equalsIgnoreCase(word)){
                    return i;
                }
            }
            return -1;
        }

    }
}