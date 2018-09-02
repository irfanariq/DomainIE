package classes;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;
import id.ac.itb.openie.relation.Relation;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.lang3.tuple.Pair;
import id.ac.itb.openie.dataprocessor.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;



public class NumberStopwords extends Plugin {

    public NumberStopwords(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class NumberStopwordsHandler implements IFeatureHandler {

    	public String getFeatureName(){
    		String name = "number stopwords";
    		return name.replaceAll(" ","_");
    	}

    	public String getDescription(){
    		String description="berisi jumlah kata stopwords dalam relasi";
    		return description;
    	}

    	public Pair<String,Object> getAttributeType(){
    		String type="numeric";
    		Object values=null;
    		return Pair.of(type,values);
    	}

    	public Object calculate(Relation relation){
    		String relasi=relation.getRelationTriple().getMiddle().trim();
    		Double numstopword=new Double(numStopWords(relasi));
    		return numstopword;
    	}

        public int numStopWords(String s){
            File file = new File("resource"+File.separator+"formalization"+File.separator+"stopword.txt");
            String[] splited=s.split("\\s+");
            int num=0;
            for(String token:splited){
               try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(token.equalsIgnoreCase(line)) { 
                        num++;
                        break;
                    }
                }
                } catch(FileNotFoundException e) { 
                } 
            }
            return num;       
        }
    }
}