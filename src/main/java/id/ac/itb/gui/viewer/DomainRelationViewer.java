/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.gui.viewer;

import id.ac.itb.openie.models.DomainRelations;
import id.ac.itb.openie.models.Relation;
import id.ac.itb.openie.models.Relations;
import id.ac.itb.openie.utils.Utilities;
import org.apache.commons.lang3.StringUtils;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DomainRelationViewer extends javax.swing.JFrame {

    private File extractDirectory;
    private File domainRelationDirectory;
    private Relations allRelations = new Relations();
    private DomainRelations domainRelations = new DomainRelations();
    private ArrayList<File> files = new ArrayList<>();
    private int currentRelationPointerIndex = 0;
    private ArrayList<Object> currentHighlights = new ArrayList<>();
    private File currentlySelectedFile;
    private HashMap<String, Relations> relationsByFilename = new HashMap<>();
    private HashMap<String, DomainRelations> domainRelationByFilename = new HashMap<>();
    private Highlighter highlighter;

    /**
     * Creates new form ExtractionViewer
     */
    public DomainRelationViewer() {
        initComponents();
    }

    public DomainRelationViewer(File extractDirectiory, File domainRelationDirectory) {
        this.extractDirectory = extractDirectiory;
        this.domainRelationDirectory = domainRelationDirectory;
        loadExtractions();
        initComponents();
    }

    private void highlightCurrentRelation() {
        Highlighter.HighlightPainter relationPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.orange);
        Highlighter.HighlightPainter argumentPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.green);

        String fileContent = StringUtils.join(Utilities.getFileContent(currentlySelectedFile), "");
        Relation relation = relationsByFilename.get(currentlySelectedFile.getName()).getRelations().get(currentRelationPointerIndex);

        String sentence = relation.getOriginSentence();
        sentence = StringUtils.strip(sentence, ".").trim();

        String rel = relation.getRelationTriple().getMiddle();
        String arg1 = relation.getRelationTriple().getLeft();
        String arg2 = relation.getRelationTriple().getRight();

        System.out.println("Sentence: " + sentence);
        System.out.println(String.format("Relation: %s(%s, %s)", rel, arg1, arg2));
                System.out.println("arg 1 " + arg1);
        System.out.println("arg 2 " + arg2);


        // Remove old highlights
        for (Object highlight: currentHighlights) {
            highlighter.removeHighlight(highlight);
        }

        // Highlight each word in 1st argument separately
        String arg1Sentence = sentence;
        int offsetArg1Sentence = fileContent.indexOf(sentence);
        for (String word: arg1.split("\\s")) {
            int pointerArg1Start = arg1Sentence.indexOf(" " + word + " ") + 1;
            int pointerArg1End = pointerArg1Start + word.length();
            try {
                System.out.println(pointerArg1Start + " "+ pointerArg1End);
                if (pointerArg1Start >= 0) {

                    arg1Sentence = arg1Sentence.substring(pointerArg1End);

                    // Add offset
                    currentHighlights.add(highlighter.addHighlight(offsetArg1Sentence + pointerArg1Start, offsetArg1Sentence + pointerArg1End, argumentPainter));

                    offsetArg1Sentence += pointerArg1End;
                }
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        // Highlight each word in relation separately
        String relSentence = arg1Sentence;
        int offsetRelSentence = offsetArg1Sentence;
        for (String word: rel.split("\\s")) {
            int pointerRelStart = relSentence.indexOf(" " + word + " ") + 1;
            int pointerRelEnd = pointerRelStart + word.length();

            try {
                if (pointerRelStart >= 0) {
                    relSentence = relSentence.substring(pointerRelEnd);

                    // Add offset
                    currentHighlights.add(highlighter.addHighlight(offsetRelSentence + pointerRelStart, offsetRelSentence + pointerRelEnd, relationPainter));

                    offsetRelSentence += pointerRelEnd;
                }
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        // Highlight each word in 2nd argument separately
        String arg2Sentence = relSentence;
        int offsetArg2Sentence = offsetRelSentence;
        for (String word: arg2.split("\\s")) {
            int pointerArg2Start = arg2Sentence.indexOf(" " + word) + 1;
            int pointerArg2End = pointerArg2Start + word.length();

            try {
                if (pointerArg2Start >= 0) {
                    arg2Sentence = arg2Sentence.substring(pointerArg2End);

                    // Add offset
                    currentHighlights.add(highlighter.addHighlight(offsetArg2Sentence + pointerArg2Start, offsetArg2Sentence + pointerArg2End, argumentPainter));

                    offsetArg2Sentence += pointerArg2End;
                }
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void showCurrentPointerProgress() {
        int totalRelations = relationsByFilename.get(currentlySelectedFile.getName()).getRelations().size();

        if (totalRelations > 0) {
            additionalInformationLabel.setText(totalRelations + " relation extracted." + " Pointer : " + (currentRelationPointerIndex+1) + "/" + totalRelations);
        } else {
            additionalInformationLabel.setText("No relation extracted.");
        }
    }

    private void loadExtractions() {
        ArrayList<File> relationsFile = Utilities.getDirectoryFiles(this.extractDirectory);

        for (File file: relationsFile) {
            allRelations.addRelations(new Relations(file));
        }

        HashSet<File> _files = new HashSet<>();

        for (Relation relation: allRelations.getRelations()) {
            File relationSource = new File(relation.getOriginFile());
            _files.add(relationSource);

            relationsByFilename.putIfAbsent(relationSource.getName(), new Relations());
            relationsByFilename.put(relationSource.getName(), relationsByFilename.get(relationSource.getName()).addRelation(relation));
        }

        files = new ArrayList<>(_files);

        ArrayList<File> domainRelationsFile = Utilities.getDirectoryFiles(this.domainRelationDirectory);

        for (File file: domainRelationsFile) {
            domainRelationByFilename.putIfAbsent(file.getName(), new DomainRelations(file));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        additionalInformationLabel = new javax.swing.JLabel();
        domainIERelationLabel = new javax.swing.JLabel();
        openIERelatioonLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<String>();
        nextButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<String>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<String>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        additionalInformationLabel.setText("3 allRelations extracted");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setMargin(new Insets(8,8,8,8));
        jScrollPane2.setViewportView(jTextArea1);

        jList1.setModel(new javax.swing.AbstractListModel() {
            public int getSize() { return files.size(); }
            public String getElementAt(int i) { return (i+1) + ". " + files.get(i).getName(); }
        });
        jScrollPane3.setViewportView(jList1);

        highlighter = jTextArea1.getHighlighter();

        jList1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Reset pointer
                currentRelationPointerIndex = 0;

                // Set selected file
                currentlySelectedFile = files.get(jList1.getSelectedIndex());

                String fileContent = StringUtils.join(Utilities.getFileContent(currentlySelectedFile), "");
                jTextArea1.setText(fileContent);
                setTitle(currentlySelectedFile.getName());

                showCurrentPointerProgress();
                highlightCurrentRelation();

                System.out.println(relationsByFilename.get(currentlySelectedFile.getName()).getRelations().size());
                System.out.println(domainRelationByFilename.get(currentlySelectedFile.getName()).getDomainRelations().size());
//                System.out.println(relationsByFilename.get(currentlySelectedFile.getName()).getRelations());

                jList3.setModel(new javax.swing.AbstractListModel() {
                    public int getSize() { return relationsByFilename.get(currentlySelectedFile.getName()).getRelations().size(); }
                    public Object getElementAt(int i) { return (i+1) + ". " + relationsByFilename.get(currentlySelectedFile.getName()).getRelations().get(i).toStringReadable(); }
                });
                jList2.setModel(new javax.swing.AbstractListModel() {
                    public int getSize() { return domainRelationByFilename.get(currentlySelectedFile.getName()).getDomainRelations().size(); }
                    public Object getElementAt(int i) { return (i+1) + ". " + domainRelationByFilename.get(currentlySelectedFile.getName()).getDomainRelations().get(i).toStringReadable(); }
                });

            }
        });

        jList3.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                currentRelationPointerIndex = jList3.getSelectedIndex();

                showCurrentPointerProgress();
                highlightCurrentRelation();
            }
        });

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        previousButton.setText("Previous");
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        domainIERelationLabel.setText("DomainIE Relations");
        openIERelatioonLabel.setText("OpenIE Relations");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jScrollPane4.setViewportView(jList2);

        jScrollPane5.setViewportView(jList3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(additionalInformationLabel)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(previousButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nextButton)
                                                .addGap(4, 4, 4)
                                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(2, 2, 2)
                                                .addComponent(closeButton))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup()
                                                        .addComponent(openIERelatioonLabel)
                                                        .addComponent(jScrollPane5)
                                                )
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup()
                                                        .addComponent(domainIERelationLabel)
                                                        .addComponent(jScrollPane4)
                                                )
                                        )

                                )
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(openIERelatioonLabel)
                                                                .addComponent(jScrollPane5)
                                                        )
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(domainIERelationLabel)
                                                                .addComponent(jScrollPane4)
                                                        )
                                                )
                                        )
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(closeButton)
                                                .addComponent(additionalInformationLabel)
                                                .addComponent(nextButton)
                                                .addComponent(previousButton))
                                        .addComponent(jSeparator1))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        this.dispose();

    }

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        if (currentRelationPointerIndex - 1 >=0) {
            currentRelationPointerIndex--;

            highlightCurrentRelation();
            showCurrentPointerProgress();
        }
    }

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        int totalRelations = relationsByFilename.get(currentlySelectedFile.getName()).getRelations().size();

        if (currentRelationPointerIndex + 1 < totalRelations) {
            currentRelationPointerIndex++;

            highlightCurrentRelation();
            showCurrentPointerProgress();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DomainRelationViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DomainRelationViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DomainRelationViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DomainRelationViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DomainRelationViewer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel additionalInformationLabel;
    private javax.swing.JLabel domainIERelationLabel;
    private javax.swing.JLabel openIERelatioonLabel;
    private javax.swing.JButton closeButton;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    // End of variables declaration
}
