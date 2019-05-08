package classes;

import id.ac.itb.openie.preprocess.IPreprocessorExtensionHandler;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.HashMap;

/**
 * Created by elvanowen on 3/12/17.
 */
public class BolaNetMatchResultNormalization extends Plugin {

    public BolaNetMatchResultNormalization(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class BolaNetMatchResultNormalizationHandler extends IPreprocessorExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        @Override
        public String getPluginName() {
            return "BolaNetMatchResult Normalization";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return availableConfigurations;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        /* Function  : Remove document metadata */
        /* Input     : Atletico Madrid dalam laga leg kedua 16 besar Liga Champions, Rabu (13/3) dini hari tadi.*/
        /* Output    : Atletico Madrid dalam laga leg kedua 16 besar Liga Champions.*/
        protected String removeMatchDate(String fileContent) {
            return fileContent.replaceAll("(\\, (Senin|Selasa|Rabu|Kamis|Jumat|Sabtu|Minggu)\\s\\(\\d+\\/\\d+\\)[\\s\\w]*\\.)",".");
        }

        /* Function  : Remove document metadata */
        /* Input     : terjadi kemarin ini.
                        Baca ulasan pertandingan selengkapnya di bawah ini ya, Bolaneters!
                        Baca ulasan selengkapnya di bawah ini ya, Bolaneters!
                        Scroll ke bawah untuk menyimak jalannya permainan.
                        Scroll ke bawah untuk menyimak jalannya permainan
                        Scroll ke bawah untuk menyimak jalannya pertandingan
                        Scroll ke bawah untuk membaca informasi selengkapnya. */
        /* Output    : terjadi kemarin ini. */
        protected String removeEndingText(String fileContent) {
            return fileContent.replaceAll("(Baca ulasan pertandingan selengkapnya di bawah ini ya\\, Bolaneters\\!|" +
                    "Baca ulasan selengkapnya di bawah ini ya\\, Bolaneters\\!|" +
                    "Scroll ke bawah untuk menyimak jalannya permainan\\.|" +
                    "Scroll ke bawah untuk menyimak jalannya permainan|" +
                    "Scroll ke bawah untuk menyimak jalannya pertandingan|" +
                    "Scroll ke bawah untuk membaca informasi selengkapnya\\.)","");
        }

        /* Function  : Remove document metadata */
        /* Input     : Bola.net - Zebra Ujian SIM... */
        /* Output    : Zebra Ujian SIM... */
        private String removeMetadata(String fileContent) {
            return fileContent.replaceFirst("(Bola\\.net \\- )","").trim();
        }

        @Override
        public String preprocess(String document, String payload) throws Exception {
            String preprocessedPayload = removeMetadata(payload);
            preprocessedPayload = removeMatchDate(preprocessedPayload);
            preprocessedPayload = removeEndingText(preprocessedPayload);

            return preprocessedPayload;
        }

        @Override
        public void preprocessorWillRun() {

        }

        @Override
        public void preprocessorDidRun() {

        }
    }
}
