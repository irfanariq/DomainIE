/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;
import id.ac.itb.openie.relation.Relation;
import java.io.Serializable;
import org.apache.commons.lang3.tuple.Pair;
import ro.fortsoft.pf4j.ExtensionPoint;
/**
 *
 * @author yoga
 */
public interface IFeatureHandler extends ExtensionPoint, Serializable{
    
    public String getFeatureName();
    
    public String getDescription();
    
    public Object calculate(Relation relation);
    
    public Pair<String, Object> getAttributeType();
       
}
