/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.gui;

import id.ac.itb.gui.alert.Alert;
import id.ac.itb.gui.config.ConfigDialog;
import id.ac.itb.gui.config.ConfigFeatureDialog;
import id.ac.itb.gui.dragdroplist.DragDropList;
import id.ac.itb.gui.progressbar.*;
import id.ac.itb.gui.viewer.DomainRelationViewer;
import id.ac.itb.gui.viewer.EvaluationViewer;
import id.ac.itb.gui.viewer.ExtractionViewer;
import id.ac.itb.gui.viewer.RulesViewer;
import id.ac.itb.openie.classifier.Classifier;
import id.ac.itb.openie.classifier.ClassifierPipeline;
import id.ac.itb.openie.classifier.IClassifierHandler;
import id.ac.itb.openie.classifier.IClassifierPipelineElement;
import id.ac.itb.openie.classifier.IClassifierPipelineHook;
import id.ac.itb.openie.classrecognizer.*;
import id.ac.itb.openie.config.Config;
import id.ac.itb.openie.crawler.*;
import id.ac.itb.openie.dataprocessor.Dataprocessor;
import id.ac.itb.openie.dataprocessor.DataprocessorPipeline;
import id.ac.itb.openie.dataprocessor.IDataprocessorHandler;
import id.ac.itb.openie.dataprocessor.IDataprocessorPipelineElement;
import id.ac.itb.openie.dataprocessor.IDataprocessorPipelineHook;
import id.ac.itb.openie.domainmapper.*;
import id.ac.itb.openie.evaluation.ExtractionsEvaluation;
import id.ac.itb.openie.evaluation.ExtractionsEvaluationLabeller;
import id.ac.itb.openie.evaluation.ExtractionsEvaluationModel;
import id.ac.itb.openie.evaluation.ExtractionsEvaluationResult;
import id.ac.itb.openie.extractor.*;
import id.ac.itb.openie.pipeline.DomainIePipeline;
import id.ac.itb.openie.pipeline.OpenIePipeline;
import id.ac.itb.openie.plugins.PluginLoader;
import id.ac.itb.openie.postprocess.*;
import id.ac.itb.openie.preprocess.*;
import id.ac.itb.openie.models.Relation;
import id.ac.itb.openie.models.Relations;
import id.ac.itb.util.UnzipUtility;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;

public class OpenIeJFrame extends javax.swing.JFrame {

    private DefaultListModel openIePipelineListModel;
    private DefaultListModel domainIePipelineListModel;
    private PluginLoader pluginLoader;
    private ExtractionsEvaluationLabeller extractionsEvaluationLabeller;

    /**
     * Creates new form CustomizeCrawlerJFrame
     */
    public OpenIeJFrame() {
        setupFolders();
        initPlugins();
        initComponents();
    }

    private void setupFolders() {
        File crawlerDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("CRAWLER_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File preprocessDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("PREPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File dataprocessDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DATAPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File classifierDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("CLASSIFIER_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File extractionDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("EXTRACTIONS_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File postprocessDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("POSTPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File labelDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("EVALUATION_LABEL_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File datatrainingDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DATATRAINING_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File processedDatatrainingDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DATATRAININGPROCESSED_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File dataprocessRelationDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DATAPROCESSRELATION_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

        //Domain
        File domainDataDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DOMAIN_DATA_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File recognizedRelationDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("RECOGNIZED_RELATION_OUPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File recognizedDomainDataDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("RECOGNIZED_DOMAIN_DATA_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File domainRelationDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DOMAIN_RELATION_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));
        File rulesDirectory = new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("RULES_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator"))));

        
        if (!crawlerDirectory.exists()) crawlerDirectory.mkdir();
        if (!preprocessDirectory.exists()) preprocessDirectory.mkdir();
        if (!dataprocessDirectory.exists()) dataprocessDirectory.mkdir();
        if (!classifierDirectory.exists()) classifierDirectory.mkdir();                
        if (!extractionDirectory.exists()) extractionDirectory.mkdir();
        if (!postprocessDirectory.exists()) postprocessDirectory.mkdir();
        if (!labelDirectory.exists()) labelDirectory.mkdir();
        if (!datatrainingDirectory.exists()) datatrainingDirectory.mkdir();
        if (!processedDatatrainingDirectory.exists()) processedDatatrainingDirectory.mkdir();
        if (!dataprocessRelationDirectory.exists()) dataprocessRelationDirectory.mkdir();
        if (!domainDataDirectory.exists()) domainDataDirectory.mkdir();
        if (!recognizedRelationDirectory.exists()) recognizedRelationDirectory.mkdir();
        if (!recognizedDomainDataDirectory.exists()) recognizedDomainDataDirectory.mkdir();
        if (!domainRelationDirectory .exists()) domainRelationDirectory .mkdir();
        if (!rulesDirectory.exists()) rulesDirectory.mkdir();
    }
    
    private void initPlugins() {
        Properties props = System.getProperties();
        props.setProperty("pf4j.mode", "development");
        props.setProperty("pf4j.pluginsDir", "plugins");
        
        pluginLoader = new PluginLoader();

        pluginLoader
                .registerAvailableExtensions(ICrawlerHandler.class)
                .registerAvailableExtensions(IPreprocessorHandler.class)
                .registerAvailableExtensions(IDataprocessorHandler.class)
                .registerAvailableExtensions(IClassifierHandler.class)
                .registerAvailableExtensions(IExtractorHandler.class)
                .registerAvailableExtensions(IPostprocessorHandler.class)
                .registerAvailableExtensions(IClassRecognizerHandler.class)
                .registerAvailableExtensions(IDomainMapperHandler.class);
    }
    
    private void loadPlugin() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Alert loading = new Alert("loading");
            loading.setVisible(true);
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        String target = System.getProperty("pf4j.pluginsDir", "plugins") + File.separator + selectedFile.getName();
                        File targetZip = new File(target);
                        String UnzipTarget = target.replaceFirst("[.][^.]+$", "");
                        Files.copy(selectedFile.toPath(), targetZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        
                        UnzipUtility unzipUtility = new UnzipUtility();
                        unzipUtility.unzip(target, System.getProperty("pf4j.pluginsDir", "plugins"));
                        targetZip.delete();
                        
                        try {
//                    Alert loading = new Alert("load plugin into system");
//                    loading.setVisible(true);

                            String[] cmd = new String[3];
                            cmd[0]="cmd.exe";
                            cmd[1]="/C";
                            cmd[2]="ant";
                            Runtime rt = Runtime.getRuntime();

                            Process pr = rt.exec(cmd);

                            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

                            String line=null;

                            System.out.println("Rebuilding app using ant.");

                            while((line=input.readLine()) != null) {
                                System.out.println(line);
                            }
                            int exitVal = pr.waitFor();

                            initPlugins();
                            openIESectionCrawlerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(ICrawlerHandler.class).toArray()));
                            openIESectionPreprocessComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IPreprocessorHandler.class).toArray()));
                            openIESectionDataprocessComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IDataprocessorHandler.class).toArray()));
                            openIESectionClassifierComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IClassifierHandler.class).toArray()));
                            openIESectionExtractionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IExtractorHandler.class).toArray()));
                            openIESectionPostprocessComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IPostprocessorHandler.class).toArray()));
                            domainIEClassRecognizerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IClassRecognizerHandler.class).toArray()));
                            domainIEDomainMapperComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IDomainMapperHandler.class).toArray()));

                            //loading.dispose();
                            //alert.dispose();
                            if (exitVal == 0) {
                            //                        new Alert("Plugins loaded successfully.").setVisible(true);
                            loading.dispose();
                             new Alert("Plugins loaded successfully.").setVisible(true);
                            } else {
                                throw new Error("Error loading plugin.");
                            }
                        } catch(Exception e) {
                            System.out.println(e.toString());
                            new Alert(e.getMessage()).setVisible(true);
                        }
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                
            };
            worker.execute();
            
        }
    }

    private void browseStartingDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        Preprocessor fileReaderPreprocessor = new Preprocessor();

        for (Object iPreprocessorHandler: pluginLoader.getAllExtensions(IPreprocessorHandler.class)) {
            IPreprocessorHandler preprocessorHandler = (IPreprocessorHandler) iPreprocessorHandler;
            String pluginName = preprocessorHandler.getPluginName();

            if (pluginName.equalsIgnoreCase("Preprocessor File Reader")) {
                fileReaderPreprocessor = new Preprocessor().setPreprocessorHandler(SerializationUtils.clone(preprocessorHandler));
            }
        }

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            fileReaderPreprocessor.getPreprocessorHandler().setAvailableConfigurations("Input Directory", selectedFile.getAbsolutePath());
            openIePipelineListModel.addElement(fileReaderPreprocessor);
        }
    }

    private void browseDataTrainingDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        Dataprocessor fileReaderDataprocessorDT = new Dataprocessor();

        for (Object iDataprocessorHandler: pluginLoader.getAllExtensions(IDataprocessorHandler.class)) {
            IDataprocessorHandler dataprocessorHandler = (IDataprocessorHandler) iDataprocessorHandler;
            String pluginName = dataprocessorHandler.getPluginName();

            if (pluginName.equalsIgnoreCase("Dataprocessor Data Training File Reader")) {
                fileReaderDataprocessorDT = new Dataprocessor().setDataprocessorHandler(SerializationUtils.clone(dataprocessorHandler));
            }
        }

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            fileReaderDataprocessorDT.getDataprocessorHandler().setAvailableConfigurations("Input Directory", selectedFile.getAbsolutePath());
            openIePipelineListModel.addElement(fileReaderDataprocessorDT);
        }
    }

    private void browseDomainDataDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        ClassRecognizer fileReaderDomainMapperDD = new ClassRecognizer();

        for (Object iClassRecognizerHandler : pluginLoader.getAllExtensions(IClassRecognizerHandler.class)) {
            IClassRecognizerHandler classRecognizerHandler = (IClassRecognizerHandler) iClassRecognizerHandler;
            String pluginName = classRecognizerHandler.getPluginName();

            if (pluginName.equalsIgnoreCase("Class Recognizer Domain Data File Reader")) {
                fileReaderDomainMapperDD = new ClassRecognizer().setClassRecognizerHandler(SerializationUtils.clone(classRecognizerHandler));
            }
        }

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            fileReaderDomainMapperDD.getClassRecognizerHandler().setAvailableConfigurations("Input Domain Data Directory", selectedFile.getAbsolutePath());
            domainIePipelineListModel.addElement(fileReaderDomainMapperDD);
        }
    }
    
    private void refreshEvaluationFilesList() {
        extractionsEvaluationLabeller = new ExtractionsEvaluationLabeller();

        evaluationSectionFilesjList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() { return extractionsEvaluationLabeller.getDocuments().size(); }
            public String getElementAt(int i) { return (i+1) + ". " + extractionsEvaluationLabeller.getDocuments().get(i).getName(); }
        });
    }

    private void refreshEvaluationRelationsList() {
        if (evaluationSectionFilesjList.getSelectedIndex() >= 0) {
            evaluationSectionRelationsjList.setModel(new javax.swing.AbstractListModel<String>() {
                Relations relations = extractionsEvaluationLabeller.getRelationsFromDocument(extractionsEvaluationLabeller.getDocuments().get(evaluationSectionFilesjList.getSelectedIndex()));

                public int getSize() {
                    return relations.getRelations().size();
                }

                public String getElementAt(int i) {
                    Relation selectedRelation = relations.getRelations().get(i);
                    return String.format("%s. #%s: %s(%s, %s)\n", (i+1), selectedRelation.getSentenceIndex() + 1, selectedRelation.getRelationTriple().getMiddle(), selectedRelation.getRelationTriple().getLeft(), selectedRelation.getRelationTriple().getRight());
                }
            });
        }
    }

    private void refreshEvaluationSentencesList() {
        evaluationSectionSentencesjList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                ArrayList<String> sentences = new ArrayList<>();

                if (evaluationSectionFilesjList.getSelectedIndex() >= 0) {
                    File selectedDocument = extractionsEvaluationLabeller.getDocuments().get(evaluationSectionFilesjList.getSelectedIndex());
                    sentences = extractionsEvaluationLabeller.getDocumentSentences(selectedDocument);
                }

                return sentences.size();
            }

            public String getElementAt(int i) {
                ArrayList<String> sentences = new ArrayList<>();

                if (evaluationSectionFilesjList.getSelectedIndex() >= 0) {
                    File selectedDocument = extractionsEvaluationLabeller.getDocuments().get(evaluationSectionFilesjList.getSelectedIndex());
                    sentences = extractionsEvaluationLabeller.getDocumentSentences(selectedDocument);
                }

                if (sentences.size() > 0) {
                    return (i+1) + ". " + sentences.get(i);
                } else {
                    return null;
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        openIePipelineListModel = new DefaultListModel();
        domainIePipelineListModel = new DefaultListModel();
        extractionsEvaluationLabeller = new ExtractionsEvaluationLabeller();
        openIePipelineDragDropList = new id.ac.itb.gui.dragdroplist.DragDropList(openIePipelineListModel);
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        loadPluginsLabel = new javax.swing.JLabel();
        loadPluginsButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        openIESectionPreprocessLabel = new javax.swing.JLabel();
        openIESectionPreprocessComboBox = new javax.swing.JComboBox<>();
        openIESectionAddPreprocessesButton = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        openIESectionExtractionLabel = new javax.swing.JLabel();
        openIESectionExtractionComboBox = new javax.swing.JComboBox<>();
        openIESectionSetFeaturesPipelineElementButton = new javax.swing.JButton();
        openIESectionAddExtractionButton = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        openIESectionPostprocessLabel = new javax.swing.JLabel();
        openIESectionPostprocessComboBox = new javax.swing.JComboBox<>();
        openIESectionAddPostprocessesButton = new javax.swing.JButton();
        openIESectionExecutionPipelineLabel = new javax.swing.JLabel();
        openIESectionRemovePipelineElementButton = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jScrollPane6 = new javax.swing.JScrollPane();
        openIESectionExecutePipelineElementButton = new javax.swing.JButton();
        openIESectionConfigurePipelineElementButton1 = new javax.swing.JButton();
        openIESectionCrawlerLabel = new javax.swing.JLabel();
        openIESectionCrawlerComboBox = new javax.swing.JComboBox<>();
        openIESectionAddCrawlersButton = new javax.swing.JButton();
        openExtractionViewerLabel = new javax.swing.JLabel();
        startingDirectoryLabel = new javax.swing.JLabel();
        browseStartingDirectoryButton = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        openExtractionPostprocessedViewerLabel = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        openIESectionDataprocessLabel = new javax.swing.JLabel();
        openIESectionDataprocessComboBox = new javax.swing.JComboBox<>();
        openIESectionAddDataprocessesButton = new javax.swing.JButton();
        DataTrainingDirectoryLabel = new javax.swing.JLabel();
        browseDataTrainingDirectoryButton1 = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JSeparator();
        openIESectionClassifierLabel = new javax.swing.JLabel();
        openIESectionClassifierComboBox = new javax.swing.JComboBox<>();
        openIESectionAddClassifierButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        evaluationSectionFilesjList = new javax.swing.JList<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        evaluationSectionSentencesjList = new javax.swing.JList<>();
        sentencesLabel = new javax.swing.JLabel();
        addNewRelationsLabel = new javax.swing.JLabel();
        argument1EvaluationTextField = new javax.swing.JTextField();
        relationEvaluationTextField = new javax.swing.JTextField();
        argument2EvaluationTextField = new javax.swing.JTextField();
        addEvaluationRelationButton = new javax.swing.JButton();
        addedRelationsLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        evaluationSectionRelationsjList = new javax.swing.JList<>();
        removeEvaluationButton = new javax.swing.JButton();
        runEvaluationButton = new javax.swing.JButton();
        saveEvaluationButton = new javax.swing.JButton();
        evaluationFilesLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        loadPluginsDomainLabel = new javax.swing.JLabel();
        loadPluginsDomainButton = new javax.swing.JButton();
        domainIEClassRecognizerLabel = new javax.swing.JLabel();
        domainIEClassRecognizerComboBox = new javax.swing.JComboBox<>();
        domainIEClassRecognizerAddButton = new javax.swing.JButton();
        jdomseparator1 = new javax.swing.JSeparator();
        jdomseparator2 = new javax.swing.JSeparator();
        jdomseparator3 = new javax.swing.JSeparator();
        domainIEDomainMapperLabel = new javax.swing.JLabel();
        domainIEDomainMapperComboBox = new javax.swing.JComboBox();
        domainIEDomainMapperAddButton = new javax.swing.JButton();
        domainIEBrowseDomainDataLabel = new javax.swing.JLabel();
        domainIEBrowseDomainDataButton = new javax.swing.JButton();
        domainIePipelineDragDropList = new DragDropList(domainIePipelineListModel);
        jDomScrollPane1 = new javax.swing.JScrollPane();
        domainIePipelineElementLabel = new javax.swing.JLabel();
        domainIEExecutePipelineButton = new javax.swing.JButton();
        domainIERemovePipelineButton = new javax.swing.JButton();
        domainIEConfigurePipelineButton = new javax.swing.JButton();
        openDomainRelationDomainMapperViewerLabel = new javax.swing.JLabel();
        openRulesDomainMapperViewerLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        loadPluginsLabel.setText("Load Plugins");

        loadPluginsButton.setText("Browse");
        loadPluginsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPluginsButtonActionPerformed(evt);
            }
        });

        openIESectionPreprocessLabel.setText("Preprocesses");

        openIESectionPreprocessComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IPreprocessorHandler.class).toArray()));

        openIESectionAddPreprocessesButton.setText("+");
        openIESectionAddPreprocessesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionAddPreprocessesButtonActionPerformed(evt);
            }
        });

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);

        openIESectionExtractionLabel.setText("Extraction");

        openIESectionExtractionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IExtractorHandler.class).toArray()));

        openIESectionAddExtractionButton.setText("+");
        openIESectionAddExtractionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionAddExtractionButtonActionPerformed(evt);
            }
        });

        openIESectionPostprocessLabel.setText("Postprocesses");

        openIESectionPostprocessComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IPostprocessorHandler.class).toArray()));

        openIESectionAddPostprocessesButton.setText("+");
        openIESectionAddPostprocessesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionAddPostprocessesButtonActionPerformed(evt);
            }
        });

        openIESectionExecutionPipelineLabel.setText("Execution Pipeline");

        openIESectionRemovePipelineElementButton.setText("Remove");
        openIESectionRemovePipelineElementButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        openIESectionRemovePipelineElementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionRemovePipelineElementButtonActionPerformed(evt);
            }
        });

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);

        openIESectionExecutePipelineElementButton.setText("Execute");
        openIESectionExecutePipelineElementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionExecutePipelineElementButtonActionPerformed(evt);
            }
        });

        openIESectionConfigurePipelineElementButton1.setText("Configure");
        openIESectionConfigurePipelineElementButton1.setEnabled(false);
        openIESectionConfigurePipelineElementButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionConfigurePipelineElementButton1ActionPerformed(evt);
            }
        });

        openIESectionCrawlerLabel.setText("Crawlers");

        openIESectionCrawlerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(ICrawlerHandler.class).toArray()));

        openIESectionAddCrawlersButton.setText("+");
        openIESectionAddCrawlersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionAddCrawlersButtonActionPerformed(evt);
            }
        });

        jScrollPane6.setViewportView(openIePipelineDragDropList);        
        openExtractionViewerLabel.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        openExtractionViewerLabel.setForeground(new java.awt.Color(0, 102, 255));
        openExtractionViewerLabel.setText("(open viewer)");
        openExtractionViewerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openExtractionViewerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame extractionViewer = new ExtractionViewer(new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("EXTRACTIONS_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                extractionViewer.setVisible(true);
            }
        });

        startingDirectoryLabel.setText("Starting Directory");

        browseStartingDirectoryButton.setText("Browse");
        browseStartingDirectoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseStartingDirectoryButtonActionPerformed(evt);
            }
        });

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);

        openExtractionPostprocessedViewerLabel.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        openExtractionPostprocessedViewerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openExtractionPostprocessedViewerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame extractionViewer = new ExtractionViewer(new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("POSTPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                extractionViewer.setVisible(true);
            }
        });

        openIePipelineDragDropList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent ev) {

                Object selectedPipelineElement = openIePipelineDragDropList.getSelectedValue();
                HashMap<String, String> availableConfigurations = null;

                if (selectedPipelineElement != null) {
                    if (selectedPipelineElement instanceof Crawler) {
                        availableConfigurations = ((Crawler)selectedPipelineElement).getCrawlerhandler().getAvailableConfigurations();
                    } else if (selectedPipelineElement instanceof Preprocessor) {
                        availableConfigurations = ((Preprocessor)selectedPipelineElement).getPreprocessorHandler().getAvailableConfigurations();
                    } else if(selectedPipelineElement instanceof Dataprocessor){
                        availableConfigurations = ((Dataprocessor)selectedPipelineElement).getDataprocessorHandler().getAvailableConfigurations();
                    } else if(selectedPipelineElement instanceof Classifier){
                        availableConfigurations = ((Classifier)selectedPipelineElement).getClassifierHandler().getAvailableConfigurations();
                    } else if (selectedPipelineElement instanceof Extractor) {
                        availableConfigurations = ((Extractor)selectedPipelineElement).getExtractorHandler().getAvailableConfigurations();
                    } else if (selectedPipelineElement instanceof Postprocessor) {
                        availableConfigurations = ((Postprocessor)selectedPipelineElement).getPostprocessorHandler().getAvailableConfigurations();
                    }

                    if (availableConfigurations != null && availableConfigurations.size() > 0) {
                        openIESectionConfigurePipelineElementButton1.setEnabled(true);
                    } else {
                        openIESectionConfigurePipelineElementButton1.setEnabled(false);
                    }
                    
                    if(selectedPipelineElement instanceof Dataprocessor){
                        Dataprocessor temp= (Dataprocessor)selectedPipelineElement;
                        if(!temp.getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor Data Training File Reader")){
                            openIESectionSetFeaturesPipelineElementButton.setEnabled(true);
                            System.out.println("masuk");
                        }else{
                            openIESectionSetFeaturesPipelineElementButton.setEnabled(false);                          
                        }
                    }else{
                        openIESectionSetFeaturesPipelineElementButton.setEnabled(false);
                    }

                    openIESectionRemovePipelineElementButton.setEnabled(true);
                }
            }
        });
        openExtractionPostprocessedViewerLabel.setForeground(new java.awt.Color(0, 102, 255));
        openExtractionPostprocessedViewerLabel.setText("(open viewer)");

        openIESectionDataprocessLabel.setText("Data Processes");

        openIESectionDataprocessComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IDataprocessorHandler.class).toArray()));

        openIESectionAddDataprocessesButton.setText("+");
        openIESectionAddDataprocessesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionAddDataprocessesButtonActionPerformed(evt);
            }
        });

        DataTrainingDirectoryLabel.setText("Data Training Directory");

        browseDataTrainingDirectoryButton1.setText("Browse");
        browseDataTrainingDirectoryButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseDataTrainingDirectoryButton1ActionPerformed(evt);
            }
        });

        jSeparator16.setOrientation(javax.swing.SwingConstants.VERTICAL);

        openIESectionClassifierLabel.setText("Classifier");

        openIESectionClassifierComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IClassifierHandler.class).toArray()));

        openIESectionAddClassifierButton.setText("+");
        openIESectionAddClassifierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionAddClassifierButtonActionPerformed(evt);
            }
        });
        
        openIESectionSetFeaturesPipelineElementButton.setText("Set Features");
        openIESectionSetFeaturesPipelineElementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openIESectionSetFeaturesPipelineElementButtonActionPerformed(evt);
            }
        });
        openIESectionSetFeaturesPipelineElementButton.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(openIESectionPreprocessLabel)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionPreprocessComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(openIESectionAddPreprocessesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionExtractionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openExtractionViewerLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionExtractionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(openIESectionAddExtractionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionPostprocessLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openExtractionPostprocessedViewerLabel))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionPostprocessComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openIESectionAddPostprocessesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jSeparator3)
                    .addComponent(jSeparator11)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(openIESectionConfigurePipelineElementButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(openIESectionRemovePipelineElementButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(openIESectionExecutePipelineElementButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(openIESectionSetFeaturesPipelineElementButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(loadPluginsLabel)
                            .addComponent(loadPluginsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startingDirectoryLabel)
                            .addComponent(browseStartingDirectoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionCrawlerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(openIESectionAddCrawlersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionCrawlerLabel)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(openIESectionExecutionPipelineLabel)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionDataprocessComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(openIESectionAddDataprocessesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(browseDataTrainingDirectoryButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionDataprocessLabel)
                                .addGap(199, 199, 199)
                                .addComponent(DataTrainingDirectoryLabel)))
                        .addGap(65, 65, 65)
                        .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionClassifierLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(openIESectionClassifierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openIESectionAddClassifierButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator15)
                    .addContainerGap()))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(openIESectionCrawlerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(openIESectionAddCrawlersButton))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(startingDirectoryLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(browseStartingDirectoryButton))
                            .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(loadPluginsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(loadPluginsButton))
                            .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(openIESectionCrawlerLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(openIESectionPreprocessLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(openIESectionPreprocessComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(openIESectionAddPreprocessesButton)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(openIESectionExtractionLabel)
                                    .addComponent(openExtractionViewerLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(openIESectionExtractionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(openIESectionAddExtractionButton)))
                            .addComponent(jSeparator10, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jSeparator13)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(openIESectionPostprocessLabel))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openExtractionPostprocessedViewerLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(openIESectionPostprocessComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(openIESectionAddPostprocessesButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(openIESectionDataprocessLabel)
                                .addComponent(DataTrainingDirectoryLabel))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(openIESectionDataprocessComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(openIESectionAddDataprocessesButton)
                                .addComponent(browseDataTrainingDirectoryButton1)))
                        .addComponent(jSeparator16))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(openIESectionClassifierLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(openIESectionClassifierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(openIESectionAddClassifierButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(openIESectionExecutionPipelineLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(openIESectionRemovePipelineElementButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(openIESectionConfigurePipelineElementButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(openIESectionSetFeaturesPipelineElementButton)
                        .addGap(98, 98, 98)
                        .addComponent(openIESectionExecutePipelineElementButton)
                        .addContainerGap())
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(220, 220, 220)
                    .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(263, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Open IE", jPanel5);

        // TODO ========================================================================================================

        jScrollPane2.setViewportView(evaluationSectionFilesjList);

        evaluationSectionFilesjList.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() { return extractionsEvaluationLabeller.getDocuments().size(); }
            public String getElementAt(int i) { return (i+1) + ". " + extractionsEvaluationLabeller.getDocuments().get(i).getName(); }
        });
        jScrollPane7.setViewportView(evaluationSectionSentencesjList);

        sentencesLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        sentencesLabel.setText("Sentences:");

        addNewRelationsLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        addNewRelationsLabel.setText("Add New Relations:");

        argument1EvaluationTextField.setText("1st Argument");
        argument1EvaluationTextField.setEnabled(false);
        argument1EvaluationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                argument1EvaluationTextFieldActionPerformed(evt);
            }
        });

        argument1EvaluationTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (argument1EvaluationTextField.getText().equalsIgnoreCase("1st Argument")) {
                    argument1EvaluationTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (argument1EvaluationTextField.getText().equalsIgnoreCase("")) {
                    argument1EvaluationTextField.setText("1st Argument");
                }
            }
        });

        evaluationSectionFilesjList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                refreshEvaluationSentencesList();
                refreshEvaluationRelationsList();
            }
        });

        evaluationSectionSentencesjList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (evaluationSectionSentencesjList.getSelectedIndex() >= 0) {
                    addEvaluationRelationButton.setEnabled(true);
                    argument1EvaluationTextField.setEnabled(true);
                    relationEvaluationTextField.setEnabled(true);
                    argument2EvaluationTextField.setEnabled(true);
                } else {
                    addEvaluationRelationButton.setEnabled(false);
                    argument1EvaluationTextField.setEnabled(false);
                    relationEvaluationTextField.setEnabled(false);
                    argument2EvaluationTextField.setEnabled(false);
                }

            }
        });

        relationEvaluationTextField.setText("Relation");
        relationEvaluationTextField.setEnabled(false);
        relationEvaluationTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (relationEvaluationTextField.getText().equalsIgnoreCase("Relation")) {
                    relationEvaluationTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (relationEvaluationTextField.getText().equalsIgnoreCase("")) {
                    relationEvaluationTextField.setText("Relation");
                }
            }
        });

        argument2EvaluationTextField.setText("2nd Argument");
        argument2EvaluationTextField.setEnabled(false);
        argument2EvaluationTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (argument2EvaluationTextField.getText().equalsIgnoreCase("2nd Argument")) {
                    argument2EvaluationTextField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (argument2EvaluationTextField.getText().equalsIgnoreCase("")) {
                    argument2EvaluationTextField.setText("2nd Argument");
                }
            }
        });
        addEvaluationRelationButton.setText("+");
        addEvaluationRelationButton.setEnabled(false);
        addEvaluationRelationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEvaluationRelationButtonActionPerformed(evt);
            }
        });

        
        
        addedRelationsLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        addedRelationsLabel.setText("Added Relations:");

        refreshEvaluationRelationsList();
        jScrollPane1.setViewportView(evaluationSectionRelationsjList);

        evaluationSectionRelationsjList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (evaluationSectionRelationsjList.getSelectedIndex() >= 0) {
                    removeEvaluationButton.setEnabled(true);
                } else {
                    removeEvaluationButton.setEnabled(false);
                }
            }
        });

        removeEvaluationButton.setText("Remove");
        removeEvaluationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEvaluationButtonActionPerformed(evt);
            }
        });

        runEvaluationButton.setText("Run Evaluation");
        runEvaluationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runEvaluationButtonActionPerformed(evt);
            }
        });

        saveEvaluationButton.setText("Save");
        removeEvaluationButton.setEnabled(false);
        saveEvaluationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveEvaluationButtonActionPerformed(evt);
            }
        });

        evaluationFilesLabel.setFont(new java.awt.Font("Lucida Grande", 0, 11)); // NOI18N
        evaluationFilesLabel.setText("Files:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(evaluationFilesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(argument1EvaluationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(relationEvaluationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(argument2EvaluationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addEvaluationRelationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sentencesLabel)
                                    .addComponent(addNewRelationsLabel)
                                    .addComponent(addedRelationsLabel))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(removeEvaluationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(saveEvaluationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSeparator4))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(runEvaluationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sentencesLabel)
                    .addComponent(evaluationFilesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addNewRelationsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(argument1EvaluationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(relationEvaluationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(argument2EvaluationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addEvaluationRelationButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addedRelationsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(removeEvaluationButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveEvaluationButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runEvaluationButton))
        );

        jPanel6.addComponentListener ( new ComponentAdapter() {
            public void componentShown ( ComponentEvent e ) {
                // reload files list in case user just do some crawling
                refreshEvaluationFilesList();
            }
        } );


        jTabbedPane1.addTab("Evaluation", jPanel6);

        // TODO BIKIN GUI ==============================================================================================

        loadPluginsDomainLabel.setText("Load Plugins");

        loadPluginsDomainButton.setText("Browse");
        loadPluginsDomainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPluginsButtonActionPerformed(e);
            }
        });

        domainIEClassRecognizerLabel.setText("Class Recognizer");
        domainIEClassRecognizerAddButton.setText("+");
        domainIEClassRecognizerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IClassRecognizerHandler.class).toArray()));
        domainIEClassRecognizerAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                domainIESectionAddClassRecognizerButtonActionPerformed(evt);
            }
        });
        jdomseparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        domainIEDomainMapperLabel.setText("Domain Relation Mapper");
        domainIEDomainMapperComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(pluginLoader.getImplementedExtensions(IDomainMapperHandler.class).toArray()));
        domainIEDomainMapperAddButton.setText("+");
        domainIEDomainMapperAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                domainIESectionAddDomainMapperButtonActionPerformed(e);
            }
        });
        domainIEBrowseDomainDataLabel.setText("Domain Data Directory");
        domainIEBrowseDomainDataButton.setText("Browse");
        domainIEBrowseDomainDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseDomainDataDirectoryButton1ActionPerformed(e);
            }
        });

        openDomainRelationDomainMapperViewerLabel.setForeground(new java.awt.Color(0, 102, 255));
        openDomainRelationDomainMapperViewerLabel.setText("(Domain Relation Viewer)");
        openDomainRelationDomainMapperViewerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openDomainRelationDomainMapperViewerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFrame extractionViewer = new DomainRelationViewer(
                        new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("POSTPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))),
                        new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DOMAIN_RELATION_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                extractionViewer.setVisible(true);
            }
        });

        openRulesDomainMapperViewerLabel.setForeground(new java.awt.Color(0, 102, 255));
        openRulesDomainMapperViewerLabel.setText("(Rules Viewer)");
        openRulesDomainMapperViewerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openRulesDomainMapperViewerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
//                JFrame extractionViewer = new DomainRelationViewer(new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DOMAIN_RELATION_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                JFrame rulesViewer = new RulesViewer(new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("RULES_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                rulesViewer.setVisible(true);
            }
        });
        jDomScrollPane1.setViewportView(domainIePipelineDragDropList);
        domainIePipelineDragDropList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent ev) {

                Object selectedPipelineElement = domainIePipelineDragDropList.getSelectedValue();
                HashMap<String, String> availableConfigurations = null;

                if (selectedPipelineElement != null) {
                    if (selectedPipelineElement instanceof ClassRecognizer) {
                        availableConfigurations = ((ClassRecognizer)selectedPipelineElement).getClassRecognizerHandler().getAvailableConfigurations();
                    }
                    else if (selectedPipelineElement instanceof DomainMapper) {
                        availableConfigurations = ((DomainMapper)selectedPipelineElement).getDomainMapperHandler().getAvailableConfigurations();
                    }

                    if (availableConfigurations != null && availableConfigurations.size() > 0) {
                        domainIEConfigurePipelineButton.setEnabled(true);
                    } else {
                        domainIEConfigurePipelineButton.setEnabled(false);
                    }

                    domainIERemovePipelineButton.setEnabled(true);
                }
            }
        });


        domainIePipelineElementLabel.setText("Execution Pipeline");
        domainIEExecutePipelineButton.setText("Execute");
        domainIEExecutePipelineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                domainIESectionExecutePipelineElementButtonActionPerformed(e);
            }
        });

        domainIERemovePipelineButton.setText("Remove");
        domainIERemovePipelineButton.setEnabled(false);
        domainIERemovePipelineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                domainIESectionRemovePipelineElementButtonActionPerformed(e);
            }
        });
        domainIEConfigurePipelineButton.setText("Configure");
        domainIEConfigurePipelineButton.setEnabled(false);
        domainIEConfigurePipelineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                domainIESectionConfigurePipelineElementButton1ActionPerformed(e);
            }
        });


        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);

        jPanel7Layout.setAutoCreateGaps(true);
        jPanel7Layout.setAutoCreateContainerGaps(true);

        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(loadPluginsDomainLabel)
                                        .addComponent(loadPluginsDomainButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdomseparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(domainIEClassRecognizerLabel)
                                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(domainIEClassRecognizerComboBox)
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(domainIEClassRecognizerAddButton)
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        )
                                        )
                                        .addComponent(jdomseparator3)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                    .addComponent(domainIEDomainMapperLabel)
                                                                    .addComponent(openDomainRelationDomainMapperViewerLabel)
                                                                    .addComponent(openRulesDomainMapperViewerLabel)
                                                                )
                                                                .addGroup(jPanel7Layout.createSequentialGroup()
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(domainIEDomainMapperComboBox)
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(domainIEDomainMapperAddButton)
//                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                )
                                                )
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(domainIEBrowseDomainDataLabel)
                                                        .addComponent(domainIEBrowseDomainDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                )

                                        )
                                )
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        )
                        .addComponent(jdomseparator2)
                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup()
                                        .addComponent(domainIePipelineElementLabel)
                                        .addComponent(jDomScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                )
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup()
                                        .addComponent(domainIERemovePipelineButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(domainIEConfigurePipelineButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(domainIEExecutePipelineButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        )
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
//                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addGroup(jPanel7Layout.createSequentialGroup()
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(loadPluginsDomainLabel)
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(loadPluginsDomainButton)
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                )
                                .addComponent(jdomseparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
//                                .addGap(10, 10 ,10)
                                .addGroup(jPanel7Layout.createSequentialGroup()
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel7Layout.createSequentialGroup()
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(domainIEClassRecognizerLabel)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(domainIEClassRecognizerComboBox)
                                                        .addComponent(domainIEClassRecognizerAddButton)
                                                )
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        )
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jdomseparator3)
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                            .addComponent(domainIEDomainMapperLabel)
                                                            .addComponent(openDomainRelationDomainMapperViewerLabel)
                                                            .addComponent(openRulesDomainMapperViewerLabel)
                                                        )
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(domainIEDomainMapperComboBox)
                                                                .addComponent(domainIEDomainMapperAddButton)
                                                        )
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                )
                                                .addGroup(jPanel7Layout.createSequentialGroup()
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(domainIEBrowseDomainDataLabel)
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(domainIEBrowseDomainDataButton)
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                )
                                        )
                                )
                        )
//                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdomseparator2)
                        .addComponent(domainIePipelineElementLabel)
//                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup()
                                .addGroup(jPanel7Layout.createSequentialGroup()
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDomScrollPane1)
//                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                )
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(domainIERemovePipelineButton)
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(domainIEConfigurePipelineButton)
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                )
                                        )
                                        .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(domainIEExecutePipelineButton)
//                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                )
                                        )
                                )
                        )
//                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                )
        );

        jTabbedPane1.addTab("Domain IE", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        setLocationRelativeTo(null);
        setTitle("Sistem Open IE Bahasa Indonesia");
    }// </editor-fold>//GEN-END:initComponents

    private void browseDomainDataDirectoryButton1ActionPerformed(ActionEvent e) {
        browseDomainDataDirectory();
    }

    private void domainIESectionExecutePipelineElementButtonActionPerformed(ActionEvent e) {
        // TODO add your handling code here:

        ClassRecognizerPipeline classRecognizerPipeline = new ClassRecognizerPipeline();
        DomainMapperPipeline domainMapperPipeline = new DomainMapperPipeline();
        DomainIePipeline domainIePipeline = new DomainIePipeline();

        for (int i = 0; i< domainIePipelineListModel.size(); i++) {
            Object selectedPipelineElement = domainIePipelineListModel.get(i);

            if (selectedPipelineElement instanceof IClassRecognizerPipelineElement) {
                ClassRecognizer cr = (ClassRecognizer) selectedPipelineElement;
                if(cr.getClassRecognizerHandler().getPluginName().equalsIgnoreCase("Class Recognizer Domain Data File Reader")){
                    classRecognizerPipeline.addPipelineElementInput(cr);
                }else{
                    IClassRecognizerPipelineElement classRecognizerPipelineElement = (IClassRecognizerPipelineElement) selectedPipelineElement;
                    classRecognizerPipeline.addPipelineElement(classRecognizerPipelineElement);
                }
            }
            else if (selectedPipelineElement instanceof IDomainMapperPipelineElement) {
                IDomainMapperPipelineElement domainMapperPipelineElement = (IDomainMapperPipelineElement) selectedPipelineElement;
                domainMapperPipeline.addPipelineElement(domainMapperPipelineElement);
            }
        }

        domainIePipeline.addPipelineElement(classRecognizerPipeline);
        domainIePipeline.addPipelineElement(domainMapperPipeline);

        classRecognizerPipeline.setClassRecognizerPipelineHook(new IClassRecognizerPipelineHook() {

            JFrame classRecognizerProgress = new ClassRecognizerProgress(classRecognizerPipeline);

            @Override
            public void willExecute() {
                System.out.println("Class recognizer mulai");
                classRecognizerProgress.setVisible(true);
            }

            @Override
            public void didExecute() {
                System.out.println("Class recognizer selesai");
                ((ClassRecognizerProgress) classRecognizerProgress).stopTimer();
                classRecognizerProgress.dispose();
            }
        });

        domainMapperPipeline.setDomainMapperPipelineHook(new IDomainMapperPipelineHook() {

            JFrame domainMapperProgress = new DomainMapperProgress(domainMapperPipeline);

            @Override
            public void willExecute() {
                domainMapperProgress.setVisible(true);
                System.out.println("Domain Mapper will Run ... ");
            }

            @Override
            public void didExecute() {
                System.out.println("Domain Mapper did Run ... ");

                ((DomainMapperProgress) domainMapperProgress).stopTimer();
                domainMapperProgress.dispose();

                JFrame extractionViewer = new DomainRelationViewer(
                        new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("POSTPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))),
                        new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("DOMAIN_RELATION_OUPUT_DATA").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                extractionViewer.setVisible(true);

            }
        });

        SwingWorker<String, Void> worker3 = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws InterruptedException {
                try {
                    domainIePipeline.execute();
                    Alert.main("Execution succeed");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "";
            }
        };

        worker3.execute();

    }

    private void domainIESectionConfigurePipelineElementButton1ActionPerformed(ActionEvent e) {
        // TODO
        Object selectedPipelineElement = domainIePipelineDragDropList.getSelectedValue();

        if (selectedPipelineElement != null) {
            if (selectedPipelineElement instanceof ClassRecognizer) {
                new ConfigDialog(((ClassRecognizer)selectedPipelineElement).getClassRecognizerHandler().getAvailableConfigurations()).setVisible(true);
            }
            else if (selectedPipelineElement instanceof DomainMapper) {
                new ConfigDialog(((DomainMapper)selectedPipelineElement).getDomainMapperHandler().getAvailableConfigurations()).setVisible(true);
            }
        }
    }

    private void domainIESectionRemovePipelineElementButtonActionPerformed(ActionEvent e) {
        // TODO
        domainIePipelineListModel.removeElement(domainIePipelineDragDropList.getSelectedValue());
    }

    private void domainIESectionAddClassRecognizerButtonActionPerformed(ActionEvent evt) {
        // TODO
        IClassRecognizerHandler classRecognizerHandler = (IClassRecognizerHandler) pluginLoader.getImplementedExtensions(IClassRecognizerHandler.class).get(domainIEClassRecognizerComboBox.getSelectedIndex());
        ClassRecognizer classRecognizer = new ClassRecognizer().setClassRecognizerHandler(SerializationUtils.clone(classRecognizerHandler));

        domainIePipelineListModel.addElement(classRecognizer);
        domainIePipelineDragDropList.printItems();
    }

    private void domainIESectionAddDomainMapperButtonActionPerformed(ActionEvent e) {
        IDomainMapperHandler domainMapperHandler = (IDomainMapperHandler) pluginLoader.getImplementedExtensions(IDomainMapperHandler.class).get(domainIEDomainMapperComboBox.getSelectedIndex());
        DomainMapper domainMapper = new DomainMapper().setDomainMapperHandler(SerializationUtils.clone(domainMapperHandler));

        domainIePipelineListModel.addElement(domainMapper);
        domainIePipelineDragDropList.printItems();
    }

    private void loadPluginsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadPluginsButtonActionPerformed
        // TODO add your handling code here:

        loadPlugin();
    }//GEN-LAST:event_loadPluginsButtonActionPerformed

    private void openIESectionAddCrawlersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionAddCrawlersButtonActionPerformed
        // TODO add your handling code here:

        ICrawlerHandler crawlerHandler = (ICrawlerHandler) pluginLoader.getImplementedExtensions(ICrawlerHandler.class).get(openIESectionCrawlerComboBox.getSelectedIndex());
        Crawler crawler = new Crawler().setCrawlerhandler(SerializationUtils.clone(crawlerHandler));

        openIePipelineListModel.addElement(crawler);
        openIePipelineDragDropList.printItems();

    }//GEN-LAST:event_openIESectionAddCrawlersButtonActionPerformed

    private void openIESectionAddPreprocessesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionAddPreprocessesButtonActionPerformed
        // TODO add your handling code here:

        IPreprocessorHandler preprocessorHandler = (IPreprocessorHandler) pluginLoader.getImplementedExtensions(IPreprocessorHandler.class).get(openIESectionPreprocessComboBox.getSelectedIndex());
        Preprocessor preprocessor = new Preprocessor().setPreprocessorHandler(SerializationUtils.clone(preprocessorHandler));

        openIePipelineListModel.addElement(preprocessor);
        openIePipelineDragDropList.printItems();

    }//GEN-LAST:event_openIESectionAddPreprocessesButtonActionPerformed

    private void openIESectionAddExtractionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionAddExtractionButtonActionPerformed
        // TODO add your handling code here:

        IExtractorHandler extractorHandler = (IExtractorHandler) pluginLoader.getImplementedExtensions(IExtractorHandler.class).get(openIESectionExtractionComboBox.getSelectedIndex());
        Extractor extractor = new Extractor().setExtractorHandler(SerializationUtils.clone(extractorHandler));

        openIePipelineListModel.addElement(extractor);
        openIePipelineDragDropList.printItems();

    }//GEN-LAST:event_openIESectionAddExtractionButtonActionPerformed

    private void openIESectionAddPostprocessesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionAddPostprocessesButtonActionPerformed
        // TODO add your handling code here:

        IPostprocessorHandler postprocessorHandler = (IPostprocessorHandler) pluginLoader.getImplementedExtensions(IPostprocessorHandler.class).get(openIESectionPostprocessComboBox.getSelectedIndex());
        Postprocessor postprocessor = new Postprocessor().setPostprocessorHandler(SerializationUtils.clone(postprocessorHandler));

        openIePipelineListModel.addElement(postprocessor);
        openIePipelineDragDropList.printItems();

    }//GEN-LAST:event_openIESectionAddPostprocessesButtonActionPerformed

    private void openIESectionRemovePipelineElementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionRemovePipelineElementButtonActionPerformed
        // TODO add your handling code here:
        openIePipelineListModel.removeElement(openIePipelineDragDropList.getSelectedValue());
    }//GEN-LAST:event_openIESectionRemovePipelineElementButtonActionPerformed

    private void openIESectionConfigurePipelineElementButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionConfigurePipelineElementButton1ActionPerformed
        // TODO add your handling code here:

        Object selectedPipelineElement = openIePipelineDragDropList.getSelectedValue();

        if (selectedPipelineElement != null) {
            if (selectedPipelineElement instanceof Extractor) {
                new ConfigDialog(((Extractor)selectedPipelineElement).getExtractorHandler().getAvailableConfigurations()).setVisible(true);
            } else if (selectedPipelineElement instanceof Preprocessor) {
                new ConfigDialog(((Preprocessor)selectedPipelineElement).getPreprocessorHandler().getAvailableConfigurations()).setVisible(true);
            } else if (selectedPipelineElement instanceof Crawler) {
                new ConfigDialog(((Crawler)selectedPipelineElement).getCrawlerhandler().getAvailableConfigurations()).setVisible(true);
            } else if (selectedPipelineElement instanceof Classifier) {
                new ConfigDialog(((Classifier)selectedPipelineElement).getClassifierHandler().getAvailableConfigurations()).setVisible(true);
            } else if (selectedPipelineElement instanceof Postprocessor) {
                new ConfigDialog(((Postprocessor)selectedPipelineElement).getPostprocessorHandler().getAvailableConfigurations()).setVisible(true);
            } else if (selectedPipelineElement instanceof Dataprocessor) {
                new ConfigDialog(((Dataprocessor)selectedPipelineElement).getDataprocessorHandler().getAvailableConfigurations()).setVisible(true);
            } 
        }

    }//GEN-LAST:event_openIESectionConfigurePipelineElementButton1ActionPerformed

    private void openIESectionSetFeaturesPipelineElementButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                                              
        // TODO add your handling code here:
        Object selectedPipelineElement = openIePipelineDragDropList.getSelectedValue();

        if (selectedPipelineElement != null) {
            if (selectedPipelineElement instanceof Dataprocessor) {
                System.out.println("feature dp "+((Dataprocessor)selectedPipelineElement).getArrayFeature().size());
                new ConfigFeatureDialog(((Dataprocessor)selectedPipelineElement).getArrayFeature()).setVisible(true);
                System.out.println("feature dp "+((Dataprocessor)selectedPipelineElement).getArrayFeature().size());
            } 
        }
    }   
    
    private void openIESectionExecutePipelineElementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionExecutePipelineElementButtonActionPerformed
        // TODO add your handling code here:

        CrawlerPipeline crawlerPipeline = new CrawlerPipeline();
        PreprocessorPipeline preprocessorPipeline = new PreprocessorPipeline();
        PostprocessorPipeline postprocessorPipeline = new PostprocessorPipeline();
        ExtractorPipeline extractorPipeline = new ExtractorPipeline();
        ClassifierPipeline classifierPipeline = new ClassifierPipeline();
        DataprocessorPipeline dataprocessorPipeline = new DataprocessorPipeline();
        OpenIePipeline openIePipeline = new OpenIePipeline();

        for (int i = 0; i< openIePipelineListModel.size(); i++) {
            Object selectedPipelineElement = openIePipelineListModel.get(i);

            
            if (selectedPipelineElement instanceof IExtractorPipelineElement) {
                IExtractorPipelineElement extractorPipelineElement = (IExtractorPipelineElement) selectedPipelineElement;
                extractorPipeline.addPipelineElement(extractorPipelineElement);
            } else if (selectedPipelineElement instanceof IClassifierPipelineElement) {
                IClassifierPipelineElement classifierPipelineElement = (IClassifierPipelineElement) selectedPipelineElement;
                classifierPipeline.addPipelineElement(classifierPipelineElement);
            } else if (selectedPipelineElement instanceof IDataprocessorPipelineElement) {
                Dataprocessor dp= (Dataprocessor) selectedPipelineElement;
                if(dp.getDataprocessorHandler().getPluginName().equalsIgnoreCase("Dataprocessor Data Training File Reader")){
                    dataprocessorPipeline.addPipelineElementTraining(dp);
                }else{
                    IDataprocessorPipelineElement dataprocessorPipelineElement = (IDataprocessorPipelineElement) selectedPipelineElement;
                    dataprocessorPipeline.addPipelineElement(dataprocessorPipelineElement);
                }
            } else if (selectedPipelineElement instanceof IPreprocessorPipelineElement) {
                Preprocessor pp=(Preprocessor) selectedPipelineElement;
                if(pp.getPreprocessorHandler().getPluginName().equalsIgnoreCase("Preprocessor File Reader")){
                    preprocessorPipeline.addPipelineElementInput(pp);
                }else{
                    IPreprocessorPipelineElement preprocessorPipelineElement = (IPreprocessorPipelineElement) selectedPipelineElement;
                    preprocessorPipeline.addPipelineElement(preprocessorPipelineElement);
                }
            } else if (selectedPipelineElement instanceof ICrawlerPipelineElement) {
                ICrawlerPipelineElement crawlerPipelineElement = (ICrawlerPipelineElement) selectedPipelineElement;
                crawlerPipeline.addPipelineElement(crawlerPipelineElement);
            } else if (selectedPipelineElement instanceof IPostprocessorPipelineElement) {
                IPostprocessorPipelineElement postprocessorPipelineElement = (IPostprocessorPipelineElement) selectedPipelineElement;
                postprocessorPipeline.addPipelineElement(postprocessorPipelineElement);
            }
        }

        openIePipeline.addPipelineElement(crawlerPipeline);
        openIePipeline.addPipelineElement(preprocessorPipeline);
        openIePipeline.addPipelineElement(dataprocessorPipeline);
        openIePipeline.addPipelineElement(classifierPipeline);
        openIePipeline.addPipelineElement(extractorPipeline);
        openIePipeline.addPipelineElement(postprocessorPipeline);

        crawlerPipeline.setCrawlerPipelineHook(new ICrawlerPipelineHook() {

            JFrame crawlerProgress = new CrawlerProgress(crawlerPipeline);

            @Override
            public void willExecute() {
                crawlerProgress.setVisible(true);
            }

            @Override
            public void didExecute() {
                ((CrawlerProgress) crawlerProgress).stopTimer();
                crawlerProgress.dispose();
            }
        });

        preprocessorPipeline.setPreprocessorPipelineHook(new IPreprocessorPipelineHook() {

            JFrame preprocessorProgress = new PreprocessorProgress(preprocessorPipeline);

            @Override
            public void willExecute() {
                preprocessorProgress.setVisible(true);
            }

            @Override
            public void didExecute() {
                ((PreprocessorProgress) preprocessorProgress).stopTimer();
                preprocessorProgress.dispose();
            }
        });
////implementasi ini
            dataprocessorPipeline.setDataprocessorPipelineHook(new IDataprocessorPipelineHook() {

            JFrame dataprocessorProgress = new DataprocessorProgress(dataprocessorPipeline);

            @Override
            public void willExecute() {
                dataprocessorProgress.setVisible(true);
            }

            @Override
            public void didExecute() {
                ((DataprocessorProgress) dataprocessorProgress).stopTimer();
                dataprocessorProgress.dispose();
            }
        });    
            
        classifierPipeline.setClassifierPipelineHook(new IClassifierPipelineHook() {

            JFrame classifierProgress = new ClassifierProgress(classifierPipeline);

            @Override
            public void willExecute() {
                classifierProgress.setVisible(true);
            }

            @Override
            public void didExecute() {
                ((ClassifierProgress) classifierProgress).stopTimer();
                classifierProgress.dispose();
            }
        });    
//        
        extractorPipeline.setExtractorPipelineHook(new IExtractorPipelineHook() {

            JFrame extractorProgress = new ExtractorProgress(extractorPipeline);

            @Override
            public void willExecute() {
                extractorProgress.setVisible(true);
            }

            @Override
            public void didExecute() {
                ((ExtractorProgress) extractorProgress).stopTimer();
                extractorProgress.dispose();

                if (postprocessorPipeline.getNumberOfPostprocessors() == 0) {
                    JFrame extractionViewer = new ExtractionViewer(new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("EXTRACTIONS_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                    extractionViewer.setVisible(true);
                }
            }
        });

        postprocessorPipeline.setPostprocessorPipelineHook(new IPostprocessorPipelineHook() {

            JFrame postprocessorProgress = new PostprocessorProgress(postprocessorPipeline);

            @Override
            public void willExecute() {
                postprocessorProgress.setVisible(true);
            }

            @Override
            public void didExecute() {
                ((PostprocessorProgress) postprocessorProgress).stopTimer();
                postprocessorProgress.dispose();

                if (postprocessorPipeline.getNumberOfPostprocessors() > 0) {
                    JFrame extractionViewer = new ExtractionViewer(new File(System.getProperty("user.dir") + File.separator + new Config().getProperty("POSTPROCESSES_OUTPUT_RELATIVE_PATH").replaceAll("\\.", Matcher.quoteReplacement(System.getProperty("file.separator")))));
                    extractionViewer.setVisible(true);
                }
            }
        });

        SwingWorker<String, Void> worker2 = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws InterruptedException {
                try {
                    openIePipeline.execute();
                    Alert.main("Execution succeed");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "";
            }
        };

        worker2.execute();
        

    }//GEN-LAST:event_openIESectionExecutePipelineElementButtonActionPerformed

    private void browseStartingDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseStartingDirectoryButtonActionPerformed
        // TODO add your handling code here:

        browseStartingDirectory();

    }//GEN-LAST:event_browseStartingDirectoryButtonActionPerformed

    private void addEvaluationRelationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEvaluationRelationButtonActionPerformed
        // TODO add your handling code here:

        File selectedDocument = extractionsEvaluationLabeller.getDocuments().get(evaluationSectionFilesjList.getSelectedIndex());
        String selectedSentence = extractionsEvaluationLabeller.getDocumentSentences(selectedDocument).get(evaluationSectionSentencesjList.getSelectedIndex());
        Relations relations = extractionsEvaluationLabeller.getRelationsFromDocument(selectedDocument);

        relations.addRelation(
                new Relation(
                        argument1EvaluationTextField.getText(),
                        relationEvaluationTextField.getText(),
                        argument2EvaluationTextField.getText(),
                        selectedDocument.getName(),
                        evaluationSectionSentencesjList.getSelectedIndex(),
                        selectedSentence
                ));

        argument1EvaluationTextField.setText("1st Argument");
        relationEvaluationTextField.setText("Relation");
        argument2EvaluationTextField.setText("2nd Argument");

        refreshEvaluationRelationsList();

    }//GEN-LAST:event_addEvaluationRelationButtonActionPerformed

    private void removeEvaluationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEvaluationButtonActionPerformed
        // TODO add your handling code here:

        File selectedDocument = extractionsEvaluationLabeller.getDocuments().get(evaluationSectionFilesjList.getSelectedIndex());
        Relations relations = extractionsEvaluationLabeller.getRelationsFromDocument(selectedDocument);
        relations.removeRelation(evaluationSectionRelationsjList.getSelectedIndex());

        refreshEvaluationRelationsList();

    }//GEN-LAST:event_removeEvaluationButtonActionPerformed

    private void saveEvaluationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveEvaluationButtonActionPerformed
        // TODO add your handling code here:

        extractionsEvaluationLabeller.persist(extractionsEvaluationLabeller.getDocuments().get(evaluationSectionFilesjList.getSelectedIndex()));

    }//GEN-LAST:event_saveEvaluationButtonActionPerformed

    private void runEvaluationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runEvaluationButtonActionPerformed
        // TODO add your handling code here:

        ExtractionsEvaluation extractionsEvaluation = new ExtractionsEvaluation(new ExtractionsEvaluationModel());
        extractionsEvaluation.evaluate();

        ExtractionsEvaluationResult extractionsEvaluationResult = extractionsEvaluation.getExtractionsEvaluationResult();

        EvaluationViewer evaluationViewer = new EvaluationViewer(extractionsEvaluationResult);
        evaluationViewer.setVisible(true);

    }//GEN-LAST:event_runEvaluationButtonActionPerformed

    private void argument1EvaluationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_argument1EvaluationTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_argument1EvaluationTextFieldActionPerformed

    private void openIESectionAddDataprocessesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionAddDataprocessesButtonActionPerformed
        IDataprocessorHandler dataprocessorHandler = (IDataprocessorHandler) pluginLoader.getImplementedExtensions(IDataprocessorHandler.class).get(openIESectionDataprocessComboBox.getSelectedIndex());
        Dataprocessor dataprocess = new Dataprocessor().setDataprocessorHandler(SerializationUtils.clone(dataprocessorHandler));
        
        openIePipelineListModel.addElement(dataprocess);
        openIePipelineDragDropList.printItems();
    }//GEN-LAST:event_openIESectionAddDataprocessesButtonActionPerformed

    private void browseDataTrainingDirectoryButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseDataTrainingDirectoryButton1ActionPerformed
        browseDataTrainingDirectory();
    }//GEN-LAST:event_browseDataTrainingDirectoryButton1ActionPerformed

    private void openIESectionAddClassifierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openIESectionAddClassifierButtonActionPerformed
        IClassifierHandler classifierHandler = (IClassifierHandler) pluginLoader.getImplementedExtensions(IClassifierHandler.class).get(openIESectionClassifierComboBox.getSelectedIndex());
        Classifier classifier = new Classifier().setClassifierHandler(SerializationUtils.clone(classifierHandler));

        openIePipelineListModel.addElement(classifier);
        openIePipelineDragDropList.printItems();
    }//GEN-LAST:event_openIESectionAddClassifierButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){ }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OpenIeJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private id.ac.itb.gui.dragdroplist.DragDropList openIePipelineDragDropList;
    private id.ac.itb.gui.dragdroplist.DragDropList domainIePipelineDragDropList;
    private javax.swing.JLabel DataTrainingDirectoryLabel;
    private javax.swing.JButton addEvaluationRelationButton;
    private javax.swing.JLabel addNewRelationsLabel;
    private javax.swing.JLabel addedRelationsLabel;
    private javax.swing.JTextField argument1EvaluationTextField;
    private javax.swing.JTextField argument2EvaluationTextField;
    private javax.swing.JButton browseDataTrainingDirectoryButton1;
    private javax.swing.JButton browseStartingDirectoryButton;
    private javax.swing.JLabel evaluationFilesLabel;
    private javax.swing.JList<String> evaluationSectionFilesjList; 
    private javax.swing.JList<String> evaluationSectionSentencesjList;
    private javax.swing.JList<String> evaluationSectionRelationsjList;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loadPluginsButton;
    private javax.swing.JLabel loadPluginsLabel;
    private javax.swing.JLabel openExtractionPostprocessedViewerLabel;
    private javax.swing.JLabel openExtractionViewerLabel;
    private javax.swing.JButton openIESectionAddClassifierButton;
    private javax.swing.JButton openIESectionAddCrawlersButton;
    private javax.swing.JButton openIESectionAddDataprocessesButton;
    private javax.swing.JButton openIESectionAddExtractionButton;
    private javax.swing.JButton openIESectionAddPostprocessesButton;
    private javax.swing.JButton openIESectionAddPreprocessesButton;
    private javax.swing.JComboBox<Object> openIESectionClassifierComboBox;
    private javax.swing.JLabel openIESectionClassifierLabel;
    private javax.swing.JButton openIESectionConfigurePipelineElementButton1;
    private javax.swing.JButton openIESectionSetFeaturesPipelineElementButton;
    private javax.swing.JComboBox<Object> openIESectionCrawlerComboBox;
    private javax.swing.JLabel openIESectionCrawlerLabel;
    private javax.swing.JComboBox<Object> openIESectionDataprocessComboBox;
    private javax.swing.JLabel openIESectionDataprocessLabel;
    private javax.swing.JButton openIESectionExecutePipelineElementButton;
    private javax.swing.JLabel openIESectionExecutionPipelineLabel;
    private javax.swing.JComboBox<Object> openIESectionExtractionComboBox;
    private javax.swing.JLabel openIESectionExtractionLabel;
    private javax.swing.JComboBox<Object> openIESectionPostprocessComboBox;
    private javax.swing.JLabel openIESectionPostprocessLabel;
    private javax.swing.JComboBox<Object> openIESectionPreprocessComboBox;
    private javax.swing.JLabel openIESectionPreprocessLabel;
    private javax.swing.JButton openIESectionRemovePipelineElementButton;
    private javax.swing.JTextField relationEvaluationTextField;
    private javax.swing.JButton removeEvaluationButton;
    private javax.swing.JButton runEvaluationButton;
    private javax.swing.JButton saveEvaluationButton;
    private javax.swing.JLabel sentencesLabel;
    private javax.swing.JLabel startingDirectoryLabel;
    // TODO =============== added variable
    private javax.swing.JLabel loadPluginsDomainLabel;
    private javax.swing.JButton loadPluginsDomainButton;
    private javax.swing.JLabel domainIEClassRecognizerLabel;
    private javax.swing.JComboBox<Object> domainIEClassRecognizerComboBox;
    private javax.swing.JButton domainIEClassRecognizerAddButton;
    private javax.swing.JSeparator jdomseparator1;
    private javax.swing.JSeparator jdomseparator2;
    private javax.swing.JSeparator jdomseparator3;
    private javax.swing.JLabel domainIEDomainMapperLabel;
    private javax.swing.JComboBox<Object> domainIEDomainMapperComboBox;
    private javax.swing.JButton domainIEDomainMapperAddButton;
    private javax.swing.JLabel domainIEBrowseDomainDataLabel;
    private javax.swing.JButton domainIEBrowseDomainDataButton;
    private javax.swing.JScrollPane jDomScrollPane1;
    private javax.swing.JLabel domainIePipelineElementLabel;
    private javax.swing.JButton domainIEExecutePipelineButton;
    private javax.swing.JButton domainIERemovePipelineButton;
    private javax.swing.JButton domainIEConfigurePipelineButton;
    private javax.swing.JLabel openDomainRelationDomainMapperViewerLabel;
    private javax.swing.JLabel openRulesDomainMapperViewerLabel;
    // End of variables declaration//GEN-END:variables
}
