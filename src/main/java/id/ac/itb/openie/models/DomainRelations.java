package id.ac.itb.openie.models;

import java.io.File;
import java.util.ArrayList;

public class DomainRelations {
    private ArrayList<DomainRelation> domainRelations = new ArrayList<>();

    public DomainRelations() {
    }

    public DomainRelations(File file) {
        // TODO Implement this method
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (DomainRelation data : domainRelations) {
            ret.append(data.toStringWithOpenIERelation());
        }
        return ret.toString();
    }

    public DomainRelations addDomainRelation(DomainRelation domainRelation) {
        this.domainRelations.add(domainRelation);
        return this;
    }
    public DomainRelations addDomainRelation(DomainRelations domainRelation) {
        this.domainRelations.addAll(domainRelation.getDomainRelations());
        return this;
    }

    public ArrayList<DomainRelation> getDomainRelations() {
        return domainRelations;
    }
}
