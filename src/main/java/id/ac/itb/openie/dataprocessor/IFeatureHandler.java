/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.openie.dataprocessor;
import id.ac.itb.openie.models.Relation;
import java.io.Serializable;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.ExtensionPoint;
/**
 *
 * @author yoga
 */
public interface IFeatureHandler extends ExtensionPoint, Serializable{
    /**
     * @return feature name
     */
    public String getFeatureName();
    /**
     * @return feature description
     */
    public String getDescription();
    /**
     *
     * @param relation models as preprocess result
     * @return feature value
     */
    public Object calculate(Relation relation);
    /**
     *
     * @return Pair of string and object(String of attribute type and possible value) as representation attribute type 
     */
    public Pair<String, Object> getAttributeType();
       
}
