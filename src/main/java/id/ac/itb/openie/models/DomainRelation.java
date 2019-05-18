package id.ac.itb.openie.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

public class DomainRelation {
    private Relation openIERelation;
    private String relasi;
    private ArrayList<String> argumen;

    public DomainRelation() {

    }

    public DomainRelation(Relation openIERelation, String relasi, ArrayList<String> argumen) {
        this.openIERelation = openIERelation;
        this.relasi = relasi;
        this.argumen = argumen;
    }

    public String toStringWithOpenIERelation() {
        return "openIERelation(" + openIERelation + ") \n" +
                "relasi(" + relasi + ") \n" +
                "argumen(" + toStringArray(argumen) + ") \n";
    }

    public String toStringWithoutOpenIERelation() {
        return "relasi(" + relasi + ") \n" +
                "argumen(" + toStringArray(argumen) + ") \n";
    }

    public String toStringReadable() {
        return relasi + "(" + toStringArray(argumen) + ") \n";
    }

    private String toStringArray(ArrayList<String> argumen) {
        String ret = "";

        for (String arg : argumen){
            ret += arg;
            ret += "#";
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DomainRelation))
            return false;
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DomainRelation that = (DomainRelation) o;
        return new EqualsBuilder().
                // append(openIERelation, that.openIERelation).
                        append(relasi, that.relasi).
                        append(argumen, that.argumen).
                        isEquals();

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                // append(openIERelation).
                        append(relasi).
                        append(argumen).
                        toHashCode();
    }

    public void setOpenIERelation(Relation openIERelation) {
        this.openIERelation = openIERelation;
    }

    public Relation getOpenIERelation() {
        return openIERelation;
    }

    public String getRelasi() {
        return relasi;
    }

    public ArrayList<String> getArgumen() {
        return argumen;
    }

    public void setRelasi(String relasi) {
        this.relasi = relasi;
    }

    public void setArgumen(ArrayList<String> argumen) {
        this.argumen = argumen;
    }
}
