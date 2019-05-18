package classes;

import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.domainmapper.IDomainMapperExtensionHandler;
import id.ac.itb.openie.models.DomainDatas;
import id.ac.itb.openie.models.DomainData;
import id.ac.itb.openie.models.RecognizedRelations;
import id.ac.itb.openie.models.Rule;
import id.ac.itb.openie.models.DomainRelations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.ArrayList;

/**
 * Created by Irfan Ariq on 12/05/2019.
 */
public class MyCoveringAlgorithmDomainMapper extends Plugin {

    public MyCoveringAlgorithmDomainMapper(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class MyCoveringAlgorithmDomainMapperHandler extends IDomainMapperExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        @Override
        public String getPluginName() {
            return "My Covering Algorithm Domain Mapper";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            availableConfigurations.putIfAbsent("Rule Name", "My Covering Algorithm Rule");

            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public HashSet<Rule> generateRule(HashMap<File, DomainDatas> fileDomainDatasHashMap) throws Exception {
            HashSet<Rule> setAturan = new HashSet<>();
            HashSet<Rule> kandidat = new HashSet<>();

            for (Map.Entry entry : fileDomainDatasHashMap.entrySet()) {
                File file = (File) entry.getKey();
                DomainDatas domainDatas = (DomainDatas) entry.getValue();
                // LOG
                //System.out.println(domainDatas.toStringWithClass());
                for (DomainData domdat : domainDatas.getDomainDatas()) {
                    Rule test = createBaseRules(domdat);
                    if (test != null) {
                        kandidat.add(test);
                    }else {
                        System.out.println("FAILED");
                    }
                }
            }

            while (!kandidat.isEmpty()) {
                HashSet<Rule> kandidatTemp = new HashSet<>();
                for (Rule aturan : kandidat) {
                    kandidatTemp.addAll(generalize(aturan));
                }
                setAturan.addAll(kandidat);
                kandidat = kandidatTemp;
            }

            return setAturan;
        }

        // Bantuan ==================================================================================================
        private Rule createBaseRules(DomainData domainData) {
            /**
             * return rule if success
             * return null if fail
             * */
            Rule ret = new Rule();

            ret.setArg1Constraint(removeWordsClass(domainData.getRecognizedArg1()));
            ret.setArg2Constraint(removeWordsClass(domainData.getRecognizedArg2()));

            HashMap tempRelationMap = new HashMap();
            tempRelationMap.putAll(domainData.getRecognizedRelation());
            ret.setRelConstraint(tempRelationMap);

            ret.setRelasiDomain(new String(domainData.getDomainRelation().getRelasi()));
            ArrayList<HashMap<String, Pair<Boolean, String>>> domArgFrom = new ArrayList<>();

            boolean found = false;
            // nyari argumen domain relation dari mana
            for (int i = 0; i < domainData.getRecognizedDomainArg().size(); i++) {

                ngecekkelas:
                for (Map.Entry entry : domainData.getRecognizedDomainArg().get(i).entrySet()) {
                    String kunci = (String) entry.getKey();
                    Pair<Boolean, String> nilai = (Pair<Boolean, String>) entry.getValue();
                    if (domainData.getRecognizedArg1().containsKey(kunci) &&
                            domainData.getRecognizedArg1().get(kunci).getValue().equalsIgnoreCase(nilai.getValue())) {
                        domArgFrom.add(new HashMap<>());
                        domArgFrom.get(i).putIfAbsent(kunci,Pair.of(true, "inArg1"));
                        found = true;
                        break ngecekkelas;
                    }else if (domainData.getRecognizedArg2().containsKey(kunci) &&
                            domainData.getRecognizedArg2().get(kunci).getValue().equalsIgnoreCase(nilai.getValue())) {
                        domArgFrom.add(new HashMap<>());
                        domArgFrom.get(i).putIfAbsent(kunci,Pair.of(true, "inArg2"));
                        found = true;
                        break ngecekkelas;
                    }else if (domainData.getRecognizedRelation().containsKey(kunci) &&
                            domainData.getRecognizedRelation().get(kunci).getValue().equalsIgnoreCase(nilai.getValue())) {
                        domArgFrom.add(new HashMap<>());
                        domArgFrom.get(i).putIfAbsent(kunci,Pair.of(true, "inRel"));
                        found = true;
                        break ngecekkelas;
                    }
                }
            }

            if (found) {
                ret.setArgResultClass(domArgFrom);
            }else {
                ret = null;
            }

            return ret;
        }

        private HashMap<String, Pair<Boolean, String>> removeWordsClass(HashMap<String, Pair<Boolean, String>> kelas) {
            HashMap<String, Pair<Boolean, String>> ret = new HashMap<>();
            for (Map.Entry entry : kelas.entrySet()) {
                String kunci = (String) entry.getKey();
                Pair<Boolean, String> nilai = (Pair<Boolean, String>) entry.getValue();
                ret.putIfAbsent(kunci, Pair.of(nilai.getKey(), ""));
            }
            return ret;
        }

        private HashSet<Rule> generalize(Rule genrule) {
            /**
             * return set of generalized rule from "this" rule
             * return empty set if "this" rule is not generalize able
             */
            HashSet<Rule> ret = new HashSet<>();
            // TODO >> IMPLEMENT THIS METHOD

            for (Map.Entry entry : genrule.getArg1Constraint().entrySet()) {
                String kelas = (String) entry.getKey();
                if (genrule.checkDropConstraint(kelas, "arg1")) {
                    ret.add(genrule.dropConstraint(kelas, "arg1"));
                }
            }

            for (Map.Entry entry : genrule.getArg2Constraint().entrySet()) {
                String kelas = (String) entry.getKey();
                if (genrule.checkDropConstraint(kelas, "arg2")) {
                    ret.add(genrule.dropConstraint(kelas, "arg2"));
                }
            }

            for (Map.Entry entry : genrule.getRelConstraint().entrySet()) {
                String kelas = (String) entry.getKey();
                Pair<Boolean, String> value = (Pair<Boolean, String>) entry.getValue();
                if (value.getValue().equalsIgnoreCase("*")) {
                    // udah bebas katanya makanya langsung di drop
                    if (genrule.checkDropConstraint(kelas, "rel")) {
                        ret.add(genrule.dropConstraint(kelas, "rel"));
                    }
                }else {
                    ret.add(genrule.removeWordsInRelationConstraint(kelas));
                }
            }

            return ret;
        }
    }
}
