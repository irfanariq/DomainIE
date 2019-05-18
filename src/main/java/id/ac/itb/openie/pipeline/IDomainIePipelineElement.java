package id.ac.itb.openie.pipeline;

public interface IDomainIePipelineElement {
    public void willExecute();
    public void execute() throws Exception;
    public void didExecute();
}
