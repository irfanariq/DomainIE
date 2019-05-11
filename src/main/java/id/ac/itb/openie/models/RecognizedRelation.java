package id.ac.itb.openie.models;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecognizedRelation {
    private Relation openIERelation;
    private Map<String, Pair<Boolean, String>> recognizedRelation = new HashMap<>();;
    private Map<String, Pair<Boolean, String>> recognizedArg1 = new HashMap<>();;
    private Map<String, Pair<Boolean, String>> recognizedArg2 = new HashMap<>();;

    public RecognizedRelation(Relation openIERelation, Map<String, Pair<Boolean, String>> recognizedRelation, Map<String, Pair<Boolean, String>> recognizedArg1, Map<String, Pair<Boolean, String>> recognizedArg2) {
        this.openIERelation = openIERelation;
        this.recognizedRelation = recognizedRelation;
        this.recognizedArg1 = recognizedArg1;
        this.recognizedArg2 = recognizedArg2;
    }

    public RecognizedRelation(Relation openIERelation, Map<String, ArrayList<String>> listKata, Map<String, ArrayList<String>> listPattern) {
        this.openIERelation = openIERelation;
        this.recognizedArg1 = new HashMap<>();
        this.recognizedArg2 = new HashMap<>();
        this.recognizedRelation = new HashMap<>();

        // Recognize list Kata
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
            }
        }

        // Recognize list Pattern
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
            }
        }
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

    public String toString() {
        return "openIERelation(" + openIERelation + ") \n" +
                "arg1Class(" + toStringMap(recognizedArg1) + ") \n" +
                "arg2Class(" + toStringMap(recognizedArg2) + ") \n" +
                "relClass(" + toStringMap(recognizedRelation) + ") \n";
    }

    private String toStringMap(Map<String, Pair<Boolean, String>> peta){
        String ret = "";

        for (Map.Entry entry : peta.entrySet()) {
            Pair<Boolean, String> pair = (Pair) entry.getValue();
            ret += entry.getKey() + "->" + pair.getKey() + "#" + pair.getValue();
            ret += "<*>";
        }

        return ret;
    }
}
