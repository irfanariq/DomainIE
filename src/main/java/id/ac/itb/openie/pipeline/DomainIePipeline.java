package id.ac.itb.openie.pipeline;

import java.util.ArrayList;

public class DomainIePipeline {
    private ArrayList<IDomainIePipelineElement> iDomainIePipelineElements = new ArrayList<IDomainIePipelineElement>();

    public DomainIePipeline addPipelineElement(IDomainIePipelineElement domainIePipelineElements) {
        this.iDomainIePipelineElements.add(domainIePipelineElements);
        return this;
    }

    public void execute() throws Exception {
        System.out.println("Running DomainIE pipeline...");

        for (IDomainIePipelineElement domainIePipelineElement: iDomainIePipelineElements) {
            domainIePipelineElement.willExecute();
            domainIePipelineElement.execute();
            domainIePipelineElement.didExecute();
        }
    }
}
