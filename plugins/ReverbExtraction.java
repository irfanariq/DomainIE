package classes;

import id.ac.itb.nlp.POSTagger;
import id.ac.itb.nlp.SentenceTokenizer;
import id.ac.itb.openie.models.Relation;
import id.ac.itb.openie.models.Relations;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by elvanowen on 2/9/17.
 */

class ReverbExtraction {

    /* Reverb extraction rule */
    /* Extract verb then find two nearest noun phrase */
    /* Extract verb follows below rules: */
    /* Pattern : V | VP | VW*P */
    /* V : kata kerja (verb) */
    /* W : kata benda (noun) | kata sifat (adj) | kata keterangan (adv) | kata ganti (pronoun) | determiners */
    /* P : preposisi */
    private ArrayList<String> extractVerbs(String sentence) {
        POSTagger posTagger = new POSTagger();
        ArrayList<String[]> tags = posTagger.tag(sentence);
        ArrayList<String> tagsArr = new ArrayList<>();

        for(int i = 0; i < tags.size(); i++) tagsArr.add(i + "$" + tags.get(i)[0] + "/" + tags.get(i)[1]);

        String regexString = StringUtils.join(tagsArr, " ");
        String regexExtractRelationPattern = "(?:[\\w$]+\\/MD.?\\s?)*(?:[\\w$]+\\/VB.?\\s?)(?:[\\w$]+\\/CDP\\s?|[\\w$]+\\/WP\\s?|[\\w$]+\\/PR.?\\s?|[\\w$]+\\/RB\\s?|[\\w$]+\\/JJ\\s?|[\\w$]+\\/NN.?\\s?|[\\w$]+\\/DT\\s?)*(?:[\\w$]+\\/IN\\s?)|(?:[\\w$]+\\/MD.?\\s?)*(?:[\\w$]+\\/VB.?\\s?)(?:[\\w$]+\\/IN\\s?)?";

        Pattern p = Pattern.compile(regexExtractRelationPattern);
        Matcher m = p.matcher(regexString);

        ArrayList<String> extractedVerbItems = new ArrayList<>();

        while (m.find()) {
            String matches = m.group(); // e.g. "11$memutuskan/VBI 12$untuk/IN "
            extractedVerbItems.addAll(Arrays.asList(matches.split(" ")));
        }

        ArrayList<String> extractedVerbs = new ArrayList<>();

        int previousVerbIdx = -1;
        String verbString = "";
        for (String verb: extractedVerbItems) {
            if (verb.contains("$")) {
                int idx = Integer.valueOf(verb.split("\\$")[0]);
                String currentVerb = verb.split("/")[0].split("\\$")[1];

                if (previousVerbIdx == -1) {
                    verbString += currentVerb + " ";
                } else if (previousVerbIdx + 1 == idx) {
                    verbString += currentVerb + " ";
                } else {
                    extractedVerbs.add(verbString.trim());
                    verbString = currentVerb + " ";
                }

                previousVerbIdx = idx;
            }
        }

        extractedVerbs.add(verbString.trim());
//        System.out.println(extractedVerbs);

        return extractedVerbs;
    }

    private ArrayList<String> extractArguments(String sentence) {
        POSTagger posTagger = new POSTagger();
        ArrayList<String[]> tags = posTagger.tag(sentence);
        //System.out.println(tags.get(1)[0]+"  as das das dsa as d "+tags.get(1)[1]);
        ArrayList<String> tagsArr = new ArrayList<>();

        for(int i = 0; i < tags.size(); i++) tagsArr.add(i + "$" + tags.get(i)[0] + "/" + tags.get(i)[1]);

        String regexString = StringUtils.join(tagsArr, " ");

//        System.out.println(regexString);

        String regexExtractArgumentPattern = "(?:(?:[\\w$()&]|\\d+[,.-]\\d+)+\\/NN.?\\s?)+|(?:(?:[\\w$()&]|\\d+[,.-]\\d+)+\\/JJ\\s?)*(?:(?:[\\w$()&]|\\d+[,.-]\\d+)+\\/NN.?\\s?)|(?:(?:[\\w$()&]|\\d+[,.-]\\d+)+\\/FW\\s?)|(?:(?:[\\w$()&]|\\d+[,.-]\\d+)+\\/CD.?\\s?)";

        Pattern p = Pattern.compile(regexExtractArgumentPattern);
        Matcher m = p.matcher(regexString);

        ArrayList<String> extractedArgumentItems = new ArrayList<>();

        while (m.find()) {
            String matches = m.group(); // e.g. "1$situs/NN 2$resmi/JJ 3$Bank/NN 4$Mandiri/NNP "
            extractedArgumentItems.addAll(Arrays.asList(matches.split(" ")));
        }

        ArrayList<String> extractedArguments = new ArrayList<>();

        int previousArgumentIdx = -1;
        String argumentString = "";
        for (String argument: extractedArgumentItems) {
            if (argument.contains("$")) {
                int idx = Integer.valueOf(argument.split("\\$")[0]);
                String currentArgument = argument.split("/")[0].split("\\$")[1];

                if (previousArgumentIdx == -1) {
                    argumentString += currentArgument + " ";
                } else if (previousArgumentIdx + 1 == idx) {
                    argumentString += currentArgument + " ";
                } else {
                    extractedArguments.add(argumentString.trim());
                    argumentString = currentArgument + " ";
                }

                previousArgumentIdx = idx;
            }
        }

        extractedArguments.add(argumentString.trim());
//        System.out.println(extractedArguments);

        return extractedArguments;
    }

    private Relations extractRelationFromSentence(File file, String sentence, int idxSentence){

        Relations relations = new Relations();
        ArrayList<String> extractedVerbs = extractVerbs(sentence);
        ArrayList<String> extractedArguments = extractArguments(sentence);

        for (String verb: extractedVerbs) {

            int verbIdx = sentence.indexOf(verb);
            int closestLeftArgumentIdx = -1, closestRightArgumentIdx = -1;
            String closestLeftArgument = "", closestRightArgument = "";

            for(String argument: extractedArguments) {
                int argumentIdx = sentence.indexOf(argument);
                if (argumentIdx + argument.length() < verbIdx) {
                    if (closestLeftArgumentIdx == -1 || closestLeftArgumentIdx < argumentIdx) {
                        closestLeftArgumentIdx = argumentIdx;
                        closestLeftArgument = argument;
                    }
                }

                if (argumentIdx > verbIdx + verb.length()) {
                    if (closestRightArgumentIdx == -1 || closestRightArgumentIdx > argumentIdx) {
                        closestRightArgumentIdx = argumentIdx;
                        closestRightArgument = argument;
                    }
                }
            }

            if (closestLeftArgumentIdx != -1 && closestRightArgumentIdx != -1) {
                relations.addRelation(new Relation(closestLeftArgument, verb, closestRightArgument, file.getAbsolutePath(), idxSentence, sentence));
            }
        }

        return relations;
    }

    public Relations extract(File file, String payload, Relations previouslyExtracted) throws Exception {

        SentenceTokenizer sentenceTokenizer = new SentenceTokenizer();
        Relations output = new Relations();

        if (previouslyExtracted != null) output.addRelations(previouslyExtracted);

        ArrayList<String> sentences = sentenceTokenizer.tokenizeSentence(payload);

        for (int i=0;i<sentences.size();i++) {
            Relations _extractedRelations = extractRelationFromSentence(file, sentences.get(i), i);
            output.addRelations(_extractedRelations);
        }

        System.out.println(output);

        return output;
    }
}
