package id.ac.itb.openie.models;

import org.apache.commons.lang3.tuple.Triple;

public class Relation {
    private String firstEntity = null;
    private String relation = null;
    private String secondEntity = null;
    private String originSentence = null;
    private int idxSentence;
    private String originFile = null;
    private Boolean classTarget;

    public Relation(String firstEntity, String relation, String secondEntity, String originFile, int idxSentence, String originSentence) {
        this.firstEntity = firstEntity;
        this.relation = relation;
        this.secondEntity = secondEntity;
        this.idxSentence = idxSentence;
        this.originSentence = originSentence;
        this.originFile = originFile;
    }
    
    
    public Relation(String firstEntity, String relation, String secondEntity, String originFile, int idxSentence, String originSentence, Boolean classTarget) {
        this.firstEntity = firstEntity;
        this.relation = relation;
        this.secondEntity = secondEntity;
        this.idxSentence = idxSentence;
        this.originSentence = originSentence;
        this.originFile = originFile;
        this.classTarget=classTarget;
    }

    public Relation(Triple<String, String, String> relation, String originFile, int idxSentence, String originSentence) {
        this.firstEntity = relation.getLeft();
        this.relation = relation.getMiddle();
        this.secondEntity = relation.getRight();
        this.idxSentence = idxSentence;
        this.originSentence = originSentence;
        this.originFile = originFile;
    }

    public String toString() {
        return String.format("Source: %s\nKalimat ke-%s: %s\nRelasi: %s(%s# %s)\n", originFile, (idxSentence + 1), originSentence, relation, firstEntity, secondEntity);
    }
    
    public String toStringWithClassTarget() {
        return String.format("Source: %s\nKalimat ke-%s: %s\nRelasi: %s(%s# %s)\nClass: %s", originFile, (idxSentence + 1), originSentence, relation, firstEntity, secondEntity,classTarget.toString());
    }

    public Triple<String, String, String> getRelationTriple() {
        return Triple.of(firstEntity, relation, secondEntity);
    }

    public String getOriginFile() {
        return this.originFile;
    }
    
    public void setOriginFile(String s){
        this.originFile=s;
    }

    public String getOriginSentence() {
        return this.originSentence;
    }
 

    public int getSentenceIndex() {
        return this.idxSentence;
    }
    
    public void setClassTarget(Boolean target){
        this.classTarget=new Boolean(target);
    }
    public Boolean getClassTarget(){
        return this.classTarget;
    }
}
