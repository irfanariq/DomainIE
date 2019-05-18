/**
 * This is a simplified schema that shows each methods a user has to implement
 */
public abstract class IDomainMapperExtensionHandler implements IDomainMapperHandler  {
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
     * @param fileDomainDatasHashMap domain data file to generate rule
     * @return set of rule to mapping Relation to DomainRelation
     * @throws Exception
     */
    public HashSet<Rule> generateRule(HashMap<File, DomainDatas> fileDomainDatasHashMap) throws Exception;
}
