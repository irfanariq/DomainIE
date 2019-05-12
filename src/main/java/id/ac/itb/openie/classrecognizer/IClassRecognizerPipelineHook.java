package id.ac.itb.openie.classrecognizer;

/**
 *
 * @author Irfan Ariq
 * */

public interface IClassRecognizerPipelineHook {
    public void willExecute();
    public void didExecute();
}
