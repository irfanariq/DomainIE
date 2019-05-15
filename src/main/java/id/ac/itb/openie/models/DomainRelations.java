package id.ac.itb.openie.models;

import id.ac.itb.openie.utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainRelations {
    private ArrayList<DomainRelation> domainRelations = new ArrayList<>();

    public DomainRelations() {
    }

    public DomainRelations(File file) {
        // TODO Implement this method
        String relationsString = Utilities.getFileContent(file);

        Pattern pola = Pattern.compile("openIERelation\\(Source:\\s(.*)\\nKalimat ke-(\\d*?):\\s(.*)\\nRelasi:\\s(.*?)\\((.*)#\\s(.*)\\)\\n\\)\\s\\nrelasi\\((.*)\\)\\s\\nargumen\\((.*)\\)\\s\\n");

        Matcher m = pola.matcher(relationsString);

        while (m.find()) {
            Relation openIERelation = new Relation(m.group(5), m.group(4), m.group(6), m.group(1),
                    Integer.valueOf(m.group(2)) - 1, m.group(3));

            DomainRelation domainRelation = new DomainRelation();
            domainRelation.setRelasi(m.group(7));
            ArrayList<String> domainArgmens = new ArrayList<>(Arrays.asList(m.group(8).split("#")));
            domainRelation.setArgumen(domainArgmens);
            domainRelation.setOpenIERelation(openIERelation);

            this.domainRelations.add(domainRelation);
        }
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
