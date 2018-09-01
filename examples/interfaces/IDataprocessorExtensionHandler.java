/**
 * This is a simplified schema that shows each methods a user has to implement
 */
public abstract class IDataprocessorExtensionHandler implements IDataprocessorHandler {
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
     * @param s String of corpus text
     * @return representation relations from corpus text that may be incomplete
     */
    public Relations documentToRelations(String s) throws Exception;
}
