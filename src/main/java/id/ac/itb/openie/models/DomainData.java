package id.ac.itb.openie.models;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainData {
    private Relation openIERelation;
    private Map<String, Pair<Boolean, String>> recognizedRelation = new HashMap<>();
    private Map<String, Pair<Boolean, String>> recognizedArg1 = new HashMap<>();
    private Map<String, Pair<Boolean, String>> recognizedArg2 = new HashMap<>();
    private DomainRelation domainRelation;
    private ArrayList<Map<String, Pair<Boolean, String>>> recognizedDomainArg = new ArrayList<>();

    public DomainData(Relation openIERelation, DomainRelation domainRelation) {
        this.openIERelation = openIERelation;
        this.domainRelation = domainRelation;
    }

    public DomainData(Relation openIERelation, Map<String, Pair<Boolean, String>> recognizedRelation, Map<String, Pair<Boolean, String>> recognizedArg1, Map<String, Pair<Boolean, String>> recognizedArg2, DomainRelation domainRelation, ArrayList<Map<String, Pair<Boolean, String>>> recognizedDomainArg) {
        this.openIERelation = openIERelation;
        this.recognizedRelation = recognizedRelation;
        this.recognizedArg1 = recognizedArg1;
        this.recognizedArg2 = recognizedArg2;
        this.domainRelation = domainRelation;
        this.recognizedDomainArg = recognizedDomainArg;
    }

    public DomainData recognize(Map<String, ArrayList<String>> listKata, Map<String, ArrayList<String>> listPattern) {

        for (int i = 0; i < domainRelation.getArgumen().size(); i++) {
            recognizedDomainArg.add(new HashMap<>());
        }

        // List Kata
        for (Map.Entry<String, ArrayList<String>> entry : listKata.entrySet()) {
            String currKey = entry.getKey().toLowerCase().replaceAll(" ", "");
            ArrayList<String> words = entry.getValue();
            for (String kata : words) {
                if (openIERelation.getFirstEntity().toLowerCase().contains(kata.toLowerCase())) {
                    recognizedArg1.putIfAbsent(currKey, new Pair<>(true, kata));
                }
                if (openIERelation.getRelation().toLowerCase().contains(kata.toLowerCase())) {
                    recognizedRelation.putIfAbsent(currKey, new Pair<>(true, kata));
                }
                if (openIERelation.getSecondEntity().toLowerCase().contains(kata.toLowerCase())) {
                    recognizedArg2.putIfAbsent(currKey, new Pair<>(true, kata));
                }
                for (int i = 0; i < domainRelation.getArgumen().size(); i++) {
                    if (domainRelation.getArgumen().get(i).toLowerCase().contains(kata.toLowerCase())) {
                        recognizedDomainArg.get(i).putIfAbsent(currKey, new Pair<>(true, kata));
                    }
                }
            }
        }

        // List Pattern
        for (Map.Entry<String, ArrayList<String>> entry : listPattern.entrySet()) {
            String currKey = entry.getKey().toLowerCase().replaceAll(" ","");
            ArrayList<String> patterns = entry.getValue();
            for (String pola : patterns) {
                Pattern patt = Pattern.compile(pola);
                Matcher relMatch = patt.matcher(openIERelation.getRelation());
                Matcher arg1Match = patt.matcher(openIERelation.getFirstEntity());
                Matcher arg2Match = patt.matcher(openIERelation.getSecondEntity());

                if (relMatch.find()) {
                    recognizedRelation.putIfAbsent(currKey, new Pair<>(true, relMatch.group(0)));
                }
                if (arg1Match.find()) {
                    recognizedArg1.putIfAbsent(currKey, new Pair<>(true, arg1Match.group(0)));
                }
                if (arg2Match.find()) {
                    recognizedArg2.putIfAbsent(currKey, new Pair<>(true, arg2Match.group(0)));
                }

                for (int i = 0; i < domainRelation.getArgumen().size(); i++) {
                    Matcher argDom = patt.matcher(domainRelation.getArgumen().get(i));
                    if (argDom.find()) {
                        recognizedDomainArg.get(i).putIfAbsent(currKey, new Pair<>(true, argDom.group(0)));

                    }
                }
            }
        }

        return this;
    }

    public String toStringWithoutClass() {
        return "openIERelation(" + openIERelation + ") \n" +
                "domainRelation("+ domainRelation.toStringWithoutOpenIERelation() + ") \n" ;
    }

    public String toStringWithClass() {
        return "openIERelation(" + openIERelation + ") \n" +
                "arg1Class(" + toStringMapClass(recognizedArg1) + ") \n" +
                "arg2Class(" + toStringMapClass(recognizedArg2) + ") \n" +
                "relClass(" + toStringMapClass(recognizedRelation) + ") \n" +
                "domainRelation("+ domainRelation.toStringWithoutOpenIERelation() + ") \n" +
                "recognizedDomainArg(" + toStringDomainRec(recognizedDomainArg) + ") \n";
    }

    private String toStringMapClass(Map<String, Pair<Boolean, String>> peta){
        String ret = "";

        for (Map.Entry entry : peta.entrySet()) {
            Pair<Boolean, String> pair = (Pair) entry.getValue();
            ret += entry.getKey() + "->" + pair.getKey() + "#" + pair.getValue();
            ret += "<*>";
        }

        return ret;
    }

    private String toStringDomainRec(ArrayList<Map<String, Pair<Boolean, String>>>  array) {
        String ret = "";

        for (Map<String, Pair<Boolean, String>> map : array) {
            ret += toStringMapClass(map);
            ret += "<~.~>";
        }
        return ret;
    }

    public void setOpenIERelation(Relation openIERelation) {
        this.openIERelation = openIERelation;
    }

    public void setDomainRelation(DomainRelation domainRelation) {
        this.domainRelation = domainRelation;
    }

    public Relation getOpenIERelation() {
        return openIERelation;
    }

    public Map<String, Pair<Boolean, String>> getRecognizedRelation() {
        return recognizedRelation;
    }

    public Map<String, Pair<Boolean, String>> getRecognizedArg1() {
        return recognizedArg1;
    }

    public Map<String, Pair<Boolean, String>> getRecognizedArg2() {
        return recognizedArg2;
    }

    public DomainRelation getDomainRelation() {
        return domainRelation;
    }

    public ArrayList<Map<String, Pair<Boolean, String>>> getRecognizedDomainArg() {
        return recognizedDomainArg;
    }
}
