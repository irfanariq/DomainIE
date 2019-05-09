/**
 * This is a simplified schema that shows each methods a user has to implement
 */
public abstract class IExtractorExtensionRuleHandler implements IExtractorHandler {
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
     * @param document String containing original preprocessed file content
     * @param extracted List of models extracted from previous extractor
     * @return extracted relations
     * @throws Exception
     */
    public Relations extract(File file, String document, Relations extracted) throws Exception;

}
