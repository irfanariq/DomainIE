package id.ac.itb.openie.domainmapper;

import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.Rule;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public abstract class IDomainMapperFileHandler implements IDomainMapperHandler{

    public HashSet<Rule> generateRule(HashMap<File, DomainDatas> fileDomainDatasHashMap) throws Exception {
        return null;
    }
}
