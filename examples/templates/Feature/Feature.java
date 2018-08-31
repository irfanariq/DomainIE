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



public class Feature extends Plugin {

    public Feature(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class FeatureHandler implements IFeatureHandler {

    	public String getFeatureName(){
    		String name = "My Feature";
    		return name.replaceAll(" ","_");
    	}

    	public String getDescription(){
    		String description="my feature description";
    		return description;
    	}

    	public Pair<String,Object> getAttributeType(){
    	}

    	public Object calculate(Relation relation){

        }


    }
}