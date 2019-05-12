package classes;

import id.ac.itb.openie.classrecognizer.IClassRecognizerExtensionHandler;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Created by elvanowen on 2/24/17.
 */
public class SepakBolaMatchClassRecognizer extends Plugin {

    public SepakBolaMatchClassRecognizer(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class SepakBolaMatchClassRecognizerHandler extends IClassRecognizerExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {

            String name = "Sepak Bola Match Result Class Recognizer";
            return name;
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        private HashMap<String, ArrayList<String>> listKata;
        private HashMap<String, ArrayList<String>> listPattern;

        private void initVariable() {
            listKata = new HashMap<>();

            String timSepakBolaStr = "Barcelona,Arsenal,Liverpool,Chelsea,Southampton,Tottenham,Ajax,Juventus,Bayern Munchen,Watford," +
                    "Manchester United,Real Madrid,Eibar,Genoa,AS Roma,Manchester City,Real Sociaded,Deportivo Alaves,Burnley,Dynamo Kiev," +
                    "Valencia,Fiorentiona,Torino,Villareal,Videoton,Napoli,Eintracht Frankfurt,Udinese,Inter Milan,Getafe,Rayo Vallecano" +
                    "Huddersfield,Lazio,Leicester,Athletic Bilbao";
            ArrayList<String> timSepakBola = new ArrayList<String>(Arrays.asList(timSepakBolaStr.split(",")));

            String winIndicatorStr = "menang,mengalahkan,menumbangkan,memenangkan,menjinakkan,taklukkan,menaklukkan,kemenangan,menggilas";
            ArrayList<String> winIndicator = new ArrayList<String>(Arrays.asList(winIndicatorStr.toLowerCase().split(",")));

            String loseIndicatorStr = "kalah,dikalahkan";
            ArrayList<String> loseIndicator = new ArrayList<String>(Arrays.asList(loseIndicatorStr.toLowerCase().split(",")));

            String drawIndicatorStr = "ditahan imbang,imbang,seri,tertahan";
            ArrayList<String> drawIndicator = new ArrayList<String>(Arrays.asList(drawIndicatorStr.toLowerCase().split(",")));

            listKata.putIfAbsent("timsepakbola", timSepakBola);
            listKata.putIfAbsent("winindicator", winIndicator);
            listKata.putIfAbsent("loseindicator", loseIndicator);
            listKata.putIfAbsent("drawindicator", drawIndicator);

            listPattern = new HashMap<>();

            ArrayList<String> skorlist = new ArrayList<>();
            skorlist.add("(\\d+)\\s?-\\s?(\\d+)");

            ArrayList<String> menitlist = new ArrayList<>();
            menitlist.add("ke\\s?-\\s?(\\d+)");
            menitlist.add("Ke\\s?-\\s?(\\d+)");

            listPattern.putIfAbsent("skor", skorlist);
            listPattern.putIfAbsent("skor", menitlist);
        }


        @Override
        public HashMap<String, ArrayList<String>> getWordList() throws Exception{
            if (listKata == null) {
                initVariable();
            }
            return listKata;
        }

        @Override
        public HashMap<String, ArrayList<String>> getPatternList() throws Exception{
            if (listPattern == null) {
                initVariable();
            }
            return listPattern;
        }
    }
}
