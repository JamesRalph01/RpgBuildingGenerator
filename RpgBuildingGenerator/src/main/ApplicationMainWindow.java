/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import floorplanner.FloorPlanner;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.ProgressMonitor;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author chrisralph
 */
public class ApplicationMainWindow extends javax.swing.JFrame implements PropertyChangeListener {

    /**
     * Creates new form NewApplicationMainWindow
     */
    
    private Controller controller;
    private GLCanvas viewerPanel;
    private ProgressMonitor progressBar;
    
    public ApplicationMainWindow() {
        initComponents();
//        progressBar = new ProgressMonitor();
//        this.add(progressBar);
//        progressBar.setVisible(false);
        
        controller = new Controller();
        controller.setprogressBar(progressBar);
        setController(controller);
        
        tabPane.setSelectedComponent(designerPanel);
    }
    
    public ApplicationMainWindow(FPSAnimator animator) {
        this();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (animator.isStarted()) {
                    System.out.println("Stopping FPSAnimator...");
                    animator.stop();
                    System.out.println("FPSAnimator stopped...");
                }
                System.out.println("Closing Window...");
                System.exit(0);
            }
        });
                
    }
    
    public void setGLCanvas(GLCanvas canvas, String pos) {
        tabPane.addTab("Viewer", canvas); 
        viewerPanel = canvas;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
        controller.setprogressBar(progressBar);
        designerPanel.setController(controller);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttongroupType = new javax.swing.ButtonGroup();
        buttongroupTheme = new javax.swing.ButtonGroup();
        tabPane = new javax.swing.JTabbedPane();
        designerPanel = new designer.DesignerPanel();
        panelOptions = new javax.swing.JPanel();
        labelOptionsTitle = new javax.swing.JLabel();
        labelType = new javax.swing.JLabel();
        radioTavern = new javax.swing.JRadioButton();
        radioChurch = new javax.swing.JRadioButton();
        radioHouse = new javax.swing.JRadioButton();
        buttonGenerate = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        sliderEcon = new javax.swing.JSlider();
        labelEconimic = new javax.swing.JLabel();
        labelPoor = new javax.swing.JLabel();
        labelWealthy = new javax.swing.JLabel();
        buttonSave = new javax.swing.JButton();
        labelTheme = new javax.swing.JLabel();
        radioMedieval = new javax.swing.JRadioButton();
        radioModern = new javax.swing.JRadioButton();
        radioFuturistic = new javax.swing.JRadioButton();
        menuMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuitemExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RPG Building Generator");

        tabPane.setMinimumSize(new java.awt.Dimension(20, 20));
        tabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPaneStateChanged(evt);
            }
        });

        javax.swing.GroupLayout designerPanelLayout = new javax.swing.GroupLayout(designerPanel);
        designerPanel.setLayout(designerPanelLayout);
        designerPanelLayout.setHorizontalGroup(
            designerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        designerPanelLayout.setVerticalGroup(
            designerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tabPane.addTab("Designer", designerPanel);

        labelOptionsTitle.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        labelOptionsTitle.setText("Building criteria");

        labelType.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        labelType.setText("Type");

        buttongroupType.add(radioTavern);
        radioTavern.setSelected(true);
        radioTavern.setText("Tavern");
        radioTavern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTavernActionPerformed(evt);
            }
        });

        buttongroupType.add(radioChurch);
        radioChurch.setText("Church");
        radioChurch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioChurchActionPerformed(evt);
            }
        });

        buttongroupType.add(radioHouse);
        radioHouse.setText("House");
        radioHouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioHouseActionPerformed(evt);
            }
        });

        buttonGenerate.setText("Generate");
        buttonGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGenerateActionPerformed(evt);
            }
        });

        buttonClear.setText("Clear");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        sliderEcon.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderEconStateChanged(evt);
            }
        });

        labelEconimic.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        labelEconimic.setText("Economic");

        labelPoor.setText("Poor");

        labelWealthy.setText("Wealthy");

        buttonSave.setText("Save");
        buttonSave.setEnabled(false);
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        labelTheme.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        labelTheme.setText("Theme");

        buttongroupTheme.add(radioMedieval);
        radioMedieval.setSelected(true);
        radioMedieval.setText("Medieval");
        radioMedieval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioMedievalActionPerformed(evt);
            }
        });

        buttongroupTheme.add(radioModern);
        radioModern.setText("Modern");
        radioModern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioModernActionPerformed(evt);
            }
        });

        buttongroupTheme.add(radioFuturistic);
        radioFuturistic.setText("Futuristic");
        radioFuturistic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioFuturisticActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOptionsLayout = new javax.swing.GroupLayout(panelOptions);
        panelOptions.setLayout(panelOptionsLayout);
        panelOptionsLayout.setHorizontalGroup(
            panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOptionsLayout.createSequentialGroup()
                        .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelOptionsTitle)
                            .addComponent(labelType)
                            .addComponent(radioTavern)
                            .addComponent(radioChurch)
                            .addComponent(radioHouse))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelOptionsLayout.createSequentialGroup()
                        .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelEconimic)
                            .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(panelOptionsLayout.createSequentialGroup()
                                    .addComponent(labelPoor)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(labelWealthy))
                                .addComponent(buttonClear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonGenerate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                            .addComponent(sliderEcon, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTheme))
                        .addGap(0, 11, Short.MAX_VALUE))))
            .addGroup(panelOptionsLayout.createSequentialGroup()
                .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioMedieval)
                    .addComponent(radioModern)
                    .addComponent(radioFuturistic))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelOptionsLayout.setVerticalGroup(
            panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelOptionsTitle)
                .addGap(18, 18, 18)
                .addComponent(labelTheme)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioMedieval)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioModern)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioFuturistic)
                .addGap(18, 18, 18)
                .addComponent(labelType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioTavern)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioChurch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioHouse)
                .addGap(18, 18, 18)
                .addComponent(labelEconimic, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sliderEcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPoor)
                    .addComponent(labelWealthy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(buttonGenerate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSave)
                .addGap(16, 16, 16))
        );

        menuFile.setText("File");

        menuitemExit.setText("Exit");
        menuitemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemExitActionPerformed(evt);
            }
        });
        menuFile.add(menuitemExit);

        menuMain.add(menuFile);

        setJMenuBar(menuMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(panelOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(729, 569));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuitemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemExitActionPerformed

        System.exit(0);
    }//GEN-LAST:event_menuitemExitActionPerformed

    private void radioChurchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioChurchActionPerformed
        controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.CHURCH);
    }//GEN-LAST:event_radioChurchActionPerformed

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearActionPerformed
        designerPanel.Clear();
        tabPane.setSelectedComponent(designerPanel);
    }//GEN-LAST:event_buttonClearActionPerformed

    private void buttonGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGenerateActionPerformed
        
        // TODO: need to only allow generation if a building outline has been defined
        // maybe put up a message box
        
        if (controller.getBuildingOutLine().isComplete()) {
            controller.getFloorPlanner().generate(controller.getBuildingOutLine());
            designerPanel.Update();
        }
        
    }//GEN-LAST:event_buttonGenerateActionPerformed

    private void radioTavernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTavernActionPerformed
        controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.TAVERN);
    }//GEN-LAST:event_radioTavernActionPerformed

    private void radioHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioHouseActionPerformed
        controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.HOUSE);
    }//GEN-LAST:event_radioHouseActionPerformed

    private void tabPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPaneStateChanged
        if (tabPane.getSelectedComponent() == designerPanel) {
            buttonGenerate.setEnabled(true);
            buttonSave.setEnabled(false);
            radioMedieval.setEnabled(true);
            radioModern.setEnabled(true);
            radioFuturistic.setEnabled(true);
            radioChurch.setEnabled(true);
            radioTavern.setEnabled(true);
            radioHouse.setEnabled(true);
            sliderEcon.setEnabled(true);
            labelPoor.setEnabled(true);
            labelWealthy.setEnabled(true);
            
        } else {
            buttonGenerate.setEnabled(false);
            buttonSave.setEnabled(true);
            radioMedieval.setEnabled(false);
            radioModern.setEnabled(false);
            radioFuturistic.setEnabled(false);
            radioChurch.setEnabled(false);
            radioTavern.setEnabled(false);
            radioHouse.setEnabled(false);
            sliderEcon.setEnabled(false);
            labelPoor.setEnabled(false);
            labelWealthy.setEnabled(false);
            
            //Clicking on Viewer will start the 3D renderer
            progressBar = new ProgressMonitor(this, "Generating House", "", 0, 100);
            progressBar.setProgress(0);
            controller.setprogressBar(progressBar);
            
        }
    }//GEN-LAST:event_tabPaneStateChanged

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        // parent component of the dialog
        //JFrame parentFrame = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        
        FileNameExtensionFilter filt = new FileNameExtensionFilter("PNG file", "png");
        fileChooser.addChoosableFileFilter(filt);
        fileChooser.setFileFilter(filt);
        fileChooser.setDialogTitle("Specify file to save");   
        fileChooser.setDialogType(SAVE_DIALOG);

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            controller.setSaveImage(true);
            controller.setimageFilename(fileToSave.getAbsolutePath());
        }
       
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void radioModernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioModernActionPerformed
        controller.getFloorPlanner().setBuildingTheme(FloorPlanner.BuildingTheme.MODERN);
    }//GEN-LAST:event_radioModernActionPerformed

    private void radioMedievalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioMedievalActionPerformed
        controller.getFloorPlanner().setBuildingTheme(FloorPlanner.BuildingTheme.MEDIEVAL);                                          
    }//GEN-LAST:event_radioMedievalActionPerformed

    private void radioFuturisticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioFuturisticActionPerformed
        controller.getFloorPlanner().setBuildingTheme(FloorPlanner.BuildingTheme.FUTURISTIC);                                          
    }//GEN-LAST:event_radioFuturisticActionPerformed

    private void sliderEconStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderEconStateChanged
       controller.getFloorPlanner().setWealthIndicator(sliderEcon.getValue());
    }//GEN-LAST:event_sliderEconStateChanged

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
            java.util.logging.Logger.getLogger(ApplicationMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ApplicationMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ApplicationMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ApplicationMainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
                 
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ApplicationMainWindow().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonClear;
    private javax.swing.JButton buttonGenerate;
    private javax.swing.JButton buttonSave;
    private javax.swing.ButtonGroup buttongroupTheme;
    private javax.swing.ButtonGroup buttongroupType;
    private designer.DesignerPanel designerPanel;
    private javax.swing.JLabel labelEconimic;
    private javax.swing.JLabel labelOptionsTitle;
    private javax.swing.JLabel labelPoor;
    private javax.swing.JLabel labelTheme;
    private javax.swing.JLabel labelType;
    private javax.swing.JLabel labelWealthy;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuBar menuMain;
    private javax.swing.JMenuItem menuitemExit;
    private javax.swing.JPanel panelOptions;
    private javax.swing.JRadioButton radioChurch;
    private javax.swing.JRadioButton radioFuturistic;
    private javax.swing.JRadioButton radioHouse;
    private javax.swing.JRadioButton radioMedieval;
    private javax.swing.JRadioButton radioModern;
    private javax.swing.JRadioButton radioTavern;
    private javax.swing.JSlider sliderEcon;
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
