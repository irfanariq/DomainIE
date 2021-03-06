/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.ac.itb.gui.progressbar;

import id.ac.itb.openie.classrecognizer.ClassRecognizer;
import id.ac.itb.openie.classrecognizer.ClassRecognizerPipeline;
import id.ac.itb.openie.domainmapper.DomainMapper;
import id.ac.itb.openie.domainmapper.DomainMapperPipeline;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;


public class DomainMapperProgress extends javax.swing.JFrame {

    DomainMapperPipeline domainMapperPipeline;
    private Timer processTimer = null;
    private int tick;
    private int counter = 0;

    /**
     * Creates new form ExtractorProgress
     */
    public DomainMapperProgress() {
        initComponents();
    }

    public DomainMapperProgress(DomainMapperPipeline classifierPipeline) {
        this.domainMapperPipeline = classifierPipeline;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        dataprocessingDocumentsProgressLabel = new javax.swing.JLabel();
        dataprocessingPipelineProgressLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Combining data training");

        showProgressLabel();

        processTimer = new Timer(30, e -> {
            showProgressLabel();
        });
        processTimer.start();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(dataprocessingPipelineProgressLabel)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(dataprocessingDocumentsProgressLabel)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(dataprocessingPipelineProgressLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dataprocessingDocumentsProgressLabel)
                                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
        setTitle("Classifier Progress");
    }// </editor-fold>//GEN-END:initComponents

    private void showProgressLabel() {
        counter++;
        String dataprocessorName = "";
        String trail = StringUtils.repeat(".", tick) + StringUtils.repeat(" ", 4 - tick);

        if (counter % 10 == 0) {
            tick = (tick % 4) + 1;
        }

        if(domainMapperPipeline.isGeneratingRules()) {
            jLabel1.setText("Generating Rules");

            int totalCurrentlyDocumentsDataprocessed = domainMapperPipeline.getCurrentDomainMapperProcessed();
            int totalDocumentsToBeDataprocessed = domainMapperPipeline.getNumberOfDomainMapper();

            dataprocessingDocumentsProgressLabel.setText("Generating Rules" + trail);

            updateProgressBar(totalCurrentlyDocumentsDataprocessed, totalDocumentsToBeDataprocessed);

        }else {
            jLabel1.setText("Mapping to Domain Relation");

            int totalCurrentlyDocumentsDataprocessed = domainMapperPipeline.getCurrentFileMappedRecognizedRelation();
            int totalDocumentsToBeDataprocessed = domainMapperPipeline.getTotalFileRecognizedRelationToBeMapped();

            if (totalDocumentsToBeDataprocessed > 0 && totalCurrentlyDocumentsDataprocessed == totalDocumentsToBeDataprocessed) {
                dataprocessingDocumentsProgressLabel.setText("Mapping to Domain Relation Completed. Loading" + trail);
            } else {
                dataprocessingDocumentsProgressLabel.setText(" " + totalCurrentlyDocumentsDataprocessed + " / " + totalDocumentsToBeDataprocessed + " documents ");
            }

            updateProgressBar(totalCurrentlyDocumentsDataprocessed, totalDocumentsToBeDataprocessed);
        }


        if (domainMapperPipeline.getCurrentlyDomainMapperProcess() != null) {
            dataprocessorName = ((DomainMapper) domainMapperPipeline.getCurrentlyDomainMapperProcess()).getDomainMapperHandler().getPluginName();
        }

        int totalProcessedDataprocessor = domainMapperPipeline.getCurrentDomainMapperProcessed();
        int totalDataprocessor = domainMapperPipeline.getNumberOfDomainMapper();

        if (dataprocessorName.equalsIgnoreCase("")) {
            dataprocessingPipelineProgressLabel.setText("Setting up class domain mapper " + trail + StringUtils.repeat(" ", 40));
        } else {
            dataprocessingPipelineProgressLabel.setText("Running " + dataprocessorName + " ( " + totalProcessedDataprocessor + " / " + totalDataprocessor + " domain mapper" + (totalDataprocessor > 1 ? "s" : "") + " )");
        }
    }

    private void updateProgressBar(int numerator, int denominator) {
        jProgressBar1.setMinimum(0);
        jProgressBar1.setMaximum(denominator);
        jProgressBar1.setValue(numerator);
    }

    public void stopTimer() {
        processTimer.stop();
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
            java.util.logging.Logger.getLogger(ClassifierProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClassifierProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClassifierProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClassifierProgress.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClassifierProgress().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dataprocessingDocumentsProgressLabel;
    private javax.swing.JLabel dataprocessingPipelineProgressLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
