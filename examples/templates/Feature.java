package classes;

import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;
import id.ac.itb.openie.relation.Relation;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.lang3.tuple.Pair;
import id.ac.itb.openie.dataprocessor.*;


public class Feature extends Plugin {

    public Feature(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class FeatureHandler implements IFeatureHandler {

    	public String getFeatureName(){
            /* fill feature name e.g. `Number Token` in name variable */
    		String name = "My Feature";
    		return name.replaceAll(" ","_");
    	}

    	public String getDescription(){
            /* fill feature description in description variable */            
    		String description="my feature description";
    		return description;
    	}

    	public Pair<String,Object> getAttributeType(){
            /*TODO: return pair of string and object that denote attribute type. 
            String is attribute type, and object is possible value if attribute type numeric 
            or null if attribute type string or numeric */            
    	}

    	public Object calculate(Relation relation){
            /*TODO: return feature value from a relation. return string if feature type is nominal or string. 
            return double if feature type is numeric*/  
        }


    }
}