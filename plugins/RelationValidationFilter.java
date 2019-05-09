package classes;

import id.ac.itb.nlp.NamedEntityTagger;
import id.ac.itb.nlp.PhraseChunker;
import id.ac.itb.openie.postprocess.IPostprocessorExtensionHandler;
import id.ac.itb.openie.models.Relation;
import id.ac.itb.openie.models.Relations;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.HashMap;

/**
 * Created by elvanowen on 2/24/17.
 */
public class RelationValidationFilter extends Plugin {

    public RelationValidationFilter(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class RelationValidationFilterHandler extends IPostprocessorExtensionHandler {

        HashMap<String, String> availableConfigurations = new HashMap<>();

        public String getPluginName() {
            return "Relation Validation Filter";
        }

        @Override
        public HashMap<String, String> getAvailableConfigurations() {
            return null;
        }

        @Override
        public void setAvailableConfigurations(String key, String value) {
            availableConfigurations.put(key, value);
        }

        @Override
        public Relations postprocess(Relations relations, Relations postprocessed) throws Exception {
            PhraseChunker phraseChunker = new PhraseChunker();
            NamedEntityTagger namedEntityTagger = new NamedEntityTagger();

            // for (Relation models: relations.getRelations()) {
            //     System.out.println(namedEntityTagger.tag(models.getOriginSentence()));
            // }

            return relations;
        }

        @Override
        public void postprocessorWillRun() {

        }

        @Override
        public void postprocessorDidRun() {

        }
    }
}
