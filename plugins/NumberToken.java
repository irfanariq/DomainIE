package classes;

import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import id.ac.itb.openie.relation.Relation;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.lang3.tuple.Pair;
import id.ac.itb.openie.dataprocessor.*;

public class NumberToken extends Plugin {

    public NumberToken(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class NumberTokenHandler implements IFeatureHandler {

    	public String getFeatureName(){
    		String name = "number token";
    		return name.replaceAll(" ","_");
    	}

    	public String getDescription(){
    		String description="berisi jumlah kata dalam relasi";
    		return description;
    	}

    	public Pair<String,Object> getAttributeType(){
    		String type="numeric";
    		Object values=null;
    		return Pair.of(type,values);
    	}

    	public Object calculate(Relation relation){
    		String relasi=relation.getRelationTriple().getMiddle();
    		Double numtoken=new Double(relasi.split("\\s+").length);
    		return numtoken;
    	}
    }
}