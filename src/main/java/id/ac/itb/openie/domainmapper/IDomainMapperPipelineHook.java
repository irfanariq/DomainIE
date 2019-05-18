package id.ac.itb.openie.domainmapper;


/**
 *
 * @author Irfan Ariq
 */

public interface IDomainMapperPipelineHook {
    public void willExecute();
    public void didExecute();
}
