/**
 * This is a simplified schema that shows each methods a user has to implement
 */
public abstract class IExtractorExtensionLearningHandler implements IExtractorHandler {
    /**
     *
     * @return Plugin name
     */
    public String getPluginName();

    /**
     *
     * @return List of configuration name with its default value
     */
    public HashMap<String, String> getAvailableConfigurations();

    /**
     *
     * @param key Configuration name
     * @param value Configuration value
     */
    public void setAvailableConfigurations(String key, String value);
    /**
     *
     * @param instance data test that has been classified
     * @param extracted List of incomplete models from preprocessor
     * @return extracted relations
     * @throws Exception
     */
    public Relations extract(Instances instance, Relations extracted) throws Exception;

}
