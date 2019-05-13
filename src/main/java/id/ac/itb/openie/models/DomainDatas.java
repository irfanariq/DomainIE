package id.ac.itb.openie.models;

import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainDatas {
    ArrayList<DomainData> domainDatas = new ArrayList<>();

    private DomainDatas() {
    }

    public static DomainDatas create() {
        return new DomainDatas();
    }

    public static DomainDatas fromFileWithoutClass(File file) {
        DomainDatas ret = new DomainDatas();
        String relationsString = Utilities.getFileContent(file);

        Pattern pola = Pattern.compile("openIERelation\\(Source:\\s(.*)\\nKalimat ke-(\\d*?):\\s(.*)\\nRelasi:\\s(.*?)" +
                "\\((.*)#\\s(.*)\\)\\n\\)\\s\\ndomainRelation\\(relasi\\((.*)\\)\\s\\nargumen\\((.*)\\)\\s\\n\\)\\s");

        Matcher m = pola.matcher(relationsString);

        while (m.find()) {
            Relation openIERelation = new Relation(m.group(5), m.group(4), m.group(6), m.group(1), Integer.valueOf(m.group(2)) - 1, m.group(3));
            DomainRelation domainRelation = new DomainRelation();

            domainRelation.setRelasi(m.group(7));
            ArrayList<String> domainArgmens = new ArrayList<>(Arrays.asList(m.group(8).split("#")));
            domainRelation.setArgumen(domainArgmens);
            domainRelation.setOpenIERelation(openIERelation);

            DomainData domainData = new DomainData(openIERelation, domainRelation);
            ret.add(domainData);
        }

        return ret;
    }

    public static DomainDatas fromFileWithClass(File file) {
        DomainDatas ret = new DomainDatas();
        String relationsString = Utilities.getFileContent(file);

        Pattern pola = Pattern.compile("openIERelation\\(Source:\\s(.*)\\nKalimat ke-(\\d*?):\\s(.*)\\nRelasi:\\s(.*?)" +
                "\\((.*)#\\s(.*)\\)\\n\\)\\s\\narg1Class\\((.*)\\)\\s\\narg2Class\\((.*)\\)\\s\\nrelClass\\((.*)\\)\\s" +
                "\\ndomainRelation\\(relasi\\((.*)\\)\\s\\nargumen\\((.*)\\)\\s\\n\\)\\s\\nrecognizedDomainArg\\((.*)" +
                "\\)\\s");

        Matcher m = pola.matcher(relationsString);

        while (m.find()) {
            Relation openIERelation = new Relation(m.group(5), m.group(4), m.group(6), m.group(1), Integer.valueOf(m.group(2)) - 1, m.group(3));
            DomainRelation domainRelation = new DomainRelation();

            domainRelation.setRelasi(m.group(10));
            ArrayList<String> domainArgmens = new ArrayList<>(Arrays.asList(m.group(11).split("#")));
            domainRelation.setArgumen(domainArgmens);

            String arg1ClassStr = m.group(7);
            String arg2ClassStr = m.group(8);
            String relClassStr = m.group(9);

            /**
             *  0 -> arg1 class = m.group(7)
             *  1 -> arg2 class = m.group(8)
             *  2 -> relation class = m.group(9)
             *  3 -> recognized domain argumen class = m.group(12)
             */
            ArrayList<String> classStr = new ArrayList<>(3);
            classStr.add(m.group(7));
            classStr.add(m.group(8));
            classStr.add(m.group(9));
            ArrayList<HashMap<String, Pair<Boolean, String>>> classList = new ArrayList<>(3);
            for (int i = 0; i < classStr.size(); i++){
                String currentStr = classStr.get(i);
                classList.add(extractStrToClassMap(currentStr));
            }

            ArrayList<HashMap<String, Pair<Boolean, String>>> recDomArgClass= new ArrayList<>();

            ArrayList<String> recDomArgClassStr = new ArrayList<>(Arrays.asList(m.group(12).split("<~.~>")));
            for (String recDomArg :recDomArgClassStr) {
                recDomArgClass.add(extractStrToClassMap(recDomArg));
            }

            ret.add(new DomainData(openIERelation,
                    classList.get(2),
                    classList.get(0),
                    classList.get(1),
                    domainRelation,
                    recDomArgClass));
        }

        return ret;
    }

    private static HashMap<String, Pair<Boolean, String>> extractStrToClassMap(String classStr) {
        /**
         * input    : kata-timsepakbola->true#bayern munchen<*>
         * output   : Map
         *
         */
        HashMap<String, Pair<Boolean, String>> ret = new HashMap<>();
        Pattern polaKelas = Pattern.compile("(.*)\\-\\>(\\w*)\\#(.*)");

        for (String entryMap: classStr.split("<\\*>")) {
            Matcher matcher = polaKelas.matcher(entryMap);
            if (matcher.find()) {
                String key = matcher.group(1);
                Pair value = Pair.of(Boolean.parseBoolean(matcher.group(2)), matcher.group(3));
                ret.putIfAbsent(key, value);
            }
        }
        return ret;
    }

    public String toStringWithClass() {
        StringBuilder ret = new StringBuilder();
        for (DomainData data : domainDatas) {
            ret.append(data.toStringWithClass());
        }
        return ret.toString();
    }

    public String toStringWihtoutClass() {
        StringBuilder ret = new StringBuilder();
        for (DomainData data : domainDatas) {
            ret.append(data.toStringWithoutClass());
        }
        return ret.toString();
    }

    public ArrayList<DomainData> getDomainDatas() {
        return domainDatas;
    }

    public DomainDatas add(DomainData domdat) {
        this.domainDatas.add(domdat);
        return this;
    }

    public DomainDatas add(DomainDatas domdat) {
        this.domainDatas.addAll(domdat.getDomainDatas());
        return this;
    }
}
