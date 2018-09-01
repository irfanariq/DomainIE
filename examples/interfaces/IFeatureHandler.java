/**
 * This is a simplified schema that shows each methods a user has to implement
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
     * @param relation relation as preprocess result
     * @return feature value
     */
    public Object calculate(Relation relation);
    /**
     *
     * @return Pair of string and object(String of attribute type and possible value) as representation attribute type 
     */
    public Pair<String, Object> getAttributeType();
       
