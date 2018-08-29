package id.ac.itb.openie.extractor;

public interface IExtractorPipelineHook {
    public void willExecute();
    public void didExecute();
}
