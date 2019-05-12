package id.ac.itb.openie.classrecognizer;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class IClassRecognizerFileHandler implements IClassRecognizerHandler{

    public HashMap<String, ArrayList<String>> getWordList() throws Exception{
        return null;
    }

    public HashMap<String, ArrayList<String>> getPatternList() throws Exception {
        return null;
    }
}
