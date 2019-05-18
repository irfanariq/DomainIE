/**
 * This is a simplified schema that shows each methods a user has to implement
 */
public abstract class IClassRecognizerExtensionHandler implements IClassRecognizerHandler{
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
     * @return listkata to recognize class domain
     * @throws Exception
     */
    public HashMap<String, ArrayList<String>> getWordList() throws Exception;

    /**
     *
     * @return listpattern to recognize class domain
     * @throws Exception
     */
    public HashMap<String, ArrayList<String>> getPatternList() throws Exception;
}
