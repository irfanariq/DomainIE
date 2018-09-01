package id.ac.itb.openie.plugins;

import id.ac.itb.openie.classifier.IClassifierHandler;
import id.ac.itb.openie.crawler.ICrawlerHandler;
import id.ac.itb.openie.dataprocessor.IDataprocessorHandler;
import id.ac.itb.openie.extractor.IExtractorExtensionRuleHandler;
import id.ac.itb.openie.extractor.IExtractorHandler;
import id.ac.itb.openie.postprocess.IPostprocessorHandler;
import id.ac.itb.openie.preprocess.IPreprocessorHandler;
import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;

import java.util.*;

public class PluginLoader {

    private PluginManager pluginManager = null;
    private HashMap<Class, List> implementedExtensions = new HashMap<>(); // Exclude system default plugins like reader and writer
    private HashMap<Class, List> allAvailableExtensions = new HashMap<>(); // All plugins existed in plugins directory including reader and writer

    public PluginLoader() {
        this.pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public PluginLoader registerAvailableExtensions(Class type) {
        implementedExtensions.put(type, new ArrayList<>());
        allAvailableExtensions.put(type, new ArrayList<>());

        for (Object iHandler: pluginManager.getExtensions(type)) {
            if (iHandler instanceof ICrawlerHandler) {
                ICrawlerHandler crawlerHandler = (ICrawlerHandler) iHandler;
                String pluginName = crawlerHandler.getPluginName();

                allAvailableExtensions.get(type).add(crawlerHandler);

                if (!pluginName.equalsIgnoreCase("Crawler File Writer")) {
                    implementedExtensions.get(type).add(crawlerHandler);
                }
            } else if (iHandler instanceof IPreprocessorHandler) {
                IPreprocessorHandler preprocessorHandler = (IPreprocessorHandler) iHandler;
                String pluginName = preprocessorHandler.getPluginName();

                allAvailableExtensions.get(type).add(preprocessorHandler);
                
                if (!pluginName.equalsIgnoreCase("Preprocessor File Reader") && !pluginName.equalsIgnoreCase("Preprocessor File Writer")) {
                    implementedExtensions.get(type).add(preprocessorHandler);
                }
            } else if (iHandler instanceof IDataprocessorHandler) {
                IDataprocessorHandler dataprocessorHandler = (IDataprocessorHandler) iHandler;
                String pluginName = dataprocessorHandler.getPluginName();

                allAvailableExtensions.get(type).add(dataprocessorHandler);
                
                if (!pluginName.equalsIgnoreCase("Dataprocessor File Reader") && !pluginName.equalsIgnoreCase("Dataprocessor File Writer") && !pluginName.equalsIgnoreCase("Dataprocessor Data Training File Reader") && !pluginName.equalsIgnoreCase("Dataprocessor Data Training File Writer")) {
                    implementedExtensions.get(type).add(dataprocessorHandler);
                }
            } else if (iHandler instanceof IClassifierHandler) {
                IClassifierHandler classifierHandler = (IClassifierHandler) iHandler;
                String pluginName = classifierHandler.getPluginName();

                allAvailableExtensions.get(type).add(classifierHandler);
                
                if (!pluginName.equalsIgnoreCase("Classifier File Reader") && !pluginName.equalsIgnoreCase("Classifier File Writer") && !pluginName.equalsIgnoreCase("Classifier Data Training File Reader")) {
                    implementedExtensions.get(type).add(classifierHandler);
                }
            } else if (iHandler instanceof IExtractorHandler) {
                IExtractorHandler extractorHandler = (IExtractorHandler) iHandler;
                String pluginName = extractorHandler.getPluginName();

                allAvailableExtensions.get(type).add(extractorHandler);
                                
                if (!pluginName.equalsIgnoreCase("Extractor File Reader") && !pluginName.equalsIgnoreCase("Extractor File Writer")  && !pluginName.equalsIgnoreCase("Extractor Machine Learning File Reader")) {
                    implementedExtensions.get(type).add(extractorHandler);
                }
            } else if (iHandler instanceof IPostprocessorHandler) {
                IPostprocessorHandler postprocessorHandler = (IPostprocessorHandler) iHandler;
                String pluginName = postprocessorHandler.getPluginName();

                allAvailableExtensions.get(type).add(postprocessorHandler);

                if (!pluginName.equalsIgnoreCase("Postprocessor File Reader") && !pluginName.equalsIgnoreCase("Postprocessor File Writer")) {
                    implementedExtensions.get(type).add(postprocessorHandler);
                }
            }
        }

        return this;
    }

    public List getAllExtensions(Class type) {
        Collections.sort(allAvailableExtensions.get(type), new Comparator<Object>() {
            public int compare(Object lhs, Object rhs) {
                return lhs.toString().compareTo(rhs.toString());
            }
        });

        return allAvailableExtensions.get(type);
    }

    public List getImplementedExtensions(Class type) {
        Collections.sort(implementedExtensions.get(type), new Comparator<Object>() {
            public int compare(Object lhs, Object rhs) {
                return lhs.toString().compareTo(rhs.toString());
            }
        });

        return implementedExtensions.get(type);
    }
}
