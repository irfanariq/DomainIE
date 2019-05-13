package id.ac.itb.openie.models;

import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecognizedRelations {
    private ArrayList<RecognizedRelation> recognizedRelations = new ArrayList<>();

    public RecognizedRelations() {
    }

    public RecognizedRelations(File file) {
        String relationsString = Utilities.getFileContent(file);

        Pattern pola = Pattern.compile("openIERelation\\(Source:\\s(.*)\\nKalimat ke-(\\d*?):\\s(.*)\\nRelasi:\\s(.*?)\\" +
                "((.*)#\\s(.*)\\)\\n\\)\\s\\narg1Class\\((.*)\\)\\s\\narg2Class\\((.*)\\)\\s\\nrelClass\\((.*)\\)\\s\\n");
        Matcher m = pola.matcher(relationsString);

        Pattern polaKelas = Pattern.compile("(.*)\\-\\>(\\w*)\\#(.*)");

        while (m.find()) {
            Relation openIERelation = new Relation(m.group(5), m.group(4), m.group(6), m.group(1), Integer.valueOf(m.group(2)) - 1, m.group(3));
            /**
             *  0 -> arg1 class
             *  1 -> arg2 class
             *  2 -> relation class
             */
            ArrayList<String> classStr = new ArrayList<>(3);
            classStr.add(m.group(7));
            classStr.add(m.group(8));
            classStr.add(m.group(9));
            ArrayList<HashMap<String, Pair<Boolean, String>>> classList = new ArrayList<>(3);
            for (int i = 0; i < classStr.size(); i++){
                String currentStr = classStr.get(i);
                classList.add(new HashMap<>());
                for (String entryMap: currentStr.split("<\\*>")) {
                    Matcher matcher = polaKelas.matcher(entryMap);
                    if (matcher.find()) {
                        String key = matcher.group(1);
                        Pair value = Pair.of(Boolean.parseBoolean(matcher.group(2)), matcher.group(3));
                        classList.get(i).putIfAbsent(key, value);
                    }
                }
            }

            recognizedRelations.add(new RecognizedRelation(openIERelation, classList.get(2), classList.get(0), classList.get(1)));
        }
    }

    @Override
    public String toString() {
        return StringUtils.join(recognizedRelations, "\n");
    }

    public RecognizedRelations addRecogRelation(RecognizedRelation recognizedRelation) {
        this.recognizedRelations.add(recognizedRelation);
        return this;
    }
    public RecognizedRelations addRecogRelation(RecognizedRelations recognizedRelations) {
        this.recognizedRelations.addAll(recognizedRelations.getRecognizedRelations());
        return this;
    }

    public ArrayList<RecognizedRelation> getRecognizedRelations() {
        return recognizedRelations;
    }

}
