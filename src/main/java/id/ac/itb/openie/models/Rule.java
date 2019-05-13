package id.ac.itb.openie.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rule {
    HashMap<String, Pair<Boolean, String>> arg1Constraint = new HashMap<>();
    HashMap<String, Pair<Boolean, String>> arg2Constraint = new HashMap<>();
    HashMap<String, Pair<Boolean, String>> relConstraint = new HashMap<>();
    ArrayList<HashMap<String, Pair<Boolean, String>>> argResultClass = new ArrayList<>();
    String relasiDomain = "";

    public Rule() {
    }

    public Rule copy () {
        Rule ret = new Rule();

        HashMap tempMap1 = new HashMap();
        tempMap1.putAll(getArg1Constraint());
        ret.setArg1Constraint(tempMap1);

        HashMap tempMap2 = new HashMap();
        tempMap2.putAll(getArg2Constraint());
        ret.setArg2Constraint(tempMap2);

        HashMap tempMap3 = new HashMap();
        tempMap3.putAll(getRelConstraint());
        ret.setRelConstraint(tempMap3);

        ArrayList tempArr = new ArrayList();
        tempArr.addAll(getArgResultClass());
        ret.setArgResultClass(tempArr);

        ret.setRelasiDomain(new String(getRelasiDomain()));
        return ret;
    }

    public boolean checkDropConstraint(String kelas, String from) {
        /**
         * return true if kelas can be dropped
         * return false if kelas cant be dropped
         * */
        boolean ret = false;

        for (Map<String, Pair<Boolean, String>> kelasArg: argResultClass) {
            if (!kelasArg.containsKey(kelas)) {
                ret = true;
                break;
            }else if (!kelasArg.get(kelas).getValue().toLowerCase().contains(from)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public Rule dropConstraint (String kelas, String from) {
        /**
         * @param from "arg1" to drop constraint from argument 1
         * @param from "arg2" to drop constraint from argument 2
         * @param from other string to drop constraint from relation
         * @return Rule after kelas dropped
         * @return the same rule if there is no kelas in Constraint
         */

        Rule ret = this.copy();

        if (from.equalsIgnoreCase("arg1")) {
            if (ret.getArg1Constraint().containsKey(kelas)) {
                ret.getArg1Constraint().remove(kelas);
            }
        }else if (from.equalsIgnoreCase("arg2")) {
            if (ret.getArg2Constraint().containsKey(kelas)) {
                ret.getArg2Constraint().remove(kelas);
            }
        }else {
            if (ret.getRelConstraint().containsKey(kelas)) {
                ret.getRelConstraint().remove(kelas);
            }
        }

        return ret;
    }

    public Rule removeWordsInRelationConstraint (String kelas) {
        /**
         * @param kelas string domain class
         * @return Rule after words in kelas removed
         * @return the same rule if there is no kelas in Constraint
         */

        Rule ret = this.copy();

        if (ret.getRelConstraint().containsKey(kelas)) {
            ret.getRelConstraint().remove(kelas);
            ret.getRelConstraint().putIfAbsent(kelas, Pair.of(true, "*"));
        }

        return ret;
    }

    public boolean isSatisfyConstraint(RecognizedRelation recognizedRelation) {
        boolean ret = true;

        for (Map.Entry entry : arg1Constraint.entrySet()) {
            String kelas = (String) entry.getKey();
            if (!recognizedRelation.getRecognizedArg1().containsKey(kelas)) {
                ret = false;
                break;
            }
        }

        if (ret) {
            for (Map.Entry entry : arg2Constraint.entrySet()) {
                String kelas = (String) entry.getKey();
                if (!recognizedRelation.getRecognizedArg2().containsKey(kelas)) {
                    ret = false;
                    break;
                }
            }
        }

        if (ret) {
            for (Map.Entry entry : relConstraint.entrySet()) {
                String kelas = (String) entry.getKey();
                Pair<Boolean, String> value = (Pair<Boolean, String>) entry.getValue();
                if (!recognizedRelation.getRecognizedRelation().containsKey(kelas)) {
                    // ga ada kelasnya
                    ret = false;
                    break;
                }else {
                    if (!value.getValue().equalsIgnoreCase("*")) {
                        // kata di relas ga bebas
                        if (!recognizedRelation.getRecognizedRelation().get(kelas).getValue().equalsIgnoreCase(value.getValue())) {
                            // cek katanya sama ga
                            ret = false;
                            break;
                        }
                    }
                }
            }
        }

        return ret;
    }

    public Pair<String, ArrayList<String>> mapToDomainRelation(RecognizedRelation recognizedRelation) {
        /**
         *
         * @return if constraint is satisfied return pair with relasi in left side and argumen in right side
         * @return if constrint is not satisfied return empty string and empty argumen
         * */
        String relasi = "";
        ArrayList<String> argumen = new ArrayList<>();

        if (isSatisfyConstraint(recognizedRelation)) {
            relasi = this.relasiDomain;
            for (Map<String, Pair<Boolean, String>> argClass : argResultClass) {
                String argDomain = "";
                for (Map.Entry entry : argClass.entrySet()) {
                    String kelas = (String) entry.getKey();
                    Pair<Boolean, String> value = (Pair<Boolean, String>) entry.getValue();
                    if (value.getValue().equalsIgnoreCase("inarg1")) {
                        argDomain = recognizedRelation.getRecognizedArg1().get(kelas).getValue();
                    }else if (value.getValue().equalsIgnoreCase("inarg2")) {
                        argDomain = recognizedRelation.getRecognizedArg2().get(kelas).getValue();
                    }else if (value.getValue().equalsIgnoreCase("inrel")) {
                        argDomain = recognizedRelation.getRecognizedRelation().get(kelas).getValue();
                    }
                }
                argumen.add(argDomain);
            }
        }

        return Pair.of(relasi, argumen);
    }

    public String toStringIfStatement() {
        StringBuilder ret = new StringBuilder("IF ");

        boolean hasArg1 = false;
        if (!arg1Constraint.isEmpty()) {
            hasArg1 = true;
            ret.append("arg1 has ");
            for (Map.Entry entry : arg1Constraint.entrySet()) {
                String kelas = (String) entry.getKey();
                ret.append(kelas).append(" ");
            }
        }

        boolean hasRel = false;
        if (!relConstraint.isEmpty()) {
            hasRel = true;
            if (hasArg1) {
                ret.append("AND ");
            }
            ret.append("pred has ");
            for (Map.Entry entry : relConstraint.entrySet()) {
                String kelas = (String) entry.getKey();
                Pair<Boolean, String> value = (Pair<Boolean, String>) entry.getValue();
                if (!value.getValue().equalsIgnoreCase("*")) {
                    ret.append(kelas).append("(").append(value.getValue()).append(") ");
                }else {
                    ret.append(kelas).append(" ");
                }
            }
        }
        if (!arg2Constraint.isEmpty()) {
            if (hasRel) {
                ret.append("AND ");
            }
            ret.append("arg2 has ");
            for (Map.Entry entry : arg2Constraint.entrySet()) {
                String kelas = (String) entry.getKey();
                ret.append(kelas).append(" ");
            }
        }
        ret.append("\nTHEN ");
        ret.append(relasiDomain);
        ret.append("(");

        for (Map<String, Pair<Boolean, String>> kelas : argResultClass) {
            for (Map.Entry entry : kelas.entrySet()) {
                String key = (String) entry.getKey();
                Pair<Boolean, String> value = (Pair<Boolean, String>) entry.getValue();
                ret.append(key).append(" ").append(value.getValue());
            }
        }

        ret.append(")");

        return ret.toString();
    }

    public void setArg1Constraint(HashMap<String, Pair<Boolean, String>> arg1Constraint) {
        this.arg1Constraint = arg1Constraint;
    }

    public void setArg2Constraint(HashMap<String, Pair<Boolean, String>> arg2Constraint) {
        this.arg2Constraint = arg2Constraint;
    }

    public void setRelConstraint(HashMap<String, Pair<Boolean, String>> relConstraint) {
        this.relConstraint = relConstraint;
    }

    public void setArgResultClass(ArrayList<HashMap<String, Pair<Boolean, String>>> argResultClass) {
        this.argResultClass = argResultClass;
    }

    public void setRelasiDomain(String relasiDomain) {
        this.relasiDomain = relasiDomain;
    }

    public HashMap<String, Pair<Boolean, String>> getArg1Constraint() {
        return arg1Constraint;
    }

    public HashMap<String, Pair<Boolean, String>> getArg2Constraint() {
        return arg2Constraint;
    }

    public HashMap<String, Pair<Boolean, String>> getRelConstraint() {
        return relConstraint;
    }

    public ArrayList<HashMap<String, Pair<Boolean, String>>> getArgResultClass() {
        return argResultClass;
    }

    public String getRelasiDomain() {
        return relasiDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rule))
            return false;
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rule rule = (Rule) o;
        return new EqualsBuilder().
                append(arg1Constraint, rule.arg1Constraint).
                append(arg2Constraint, rule.arg2Constraint).
                append(relConstraint, rule.relConstraint).
                append(relasiDomain, rule.relasiDomain).
                append(argResultClass, rule.argResultClass).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(arg1Constraint).
                append(arg2Constraint).
                append(relConstraint).
                append(relasiDomain).
                append(argResultClass).
                toHashCode();
    }
}
