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
import util.Point;

/**
 *
 * @author chrisralph
 */
public class ApplicationMainWindow extends javax.swing.JFrame {

    /**
     * Creates new form NewApplicationMainWindow
     */
    
    private Controller controller;
    
    public ApplicationMainWindow() {
        controller = new Controller();
        initComponents();
        designerPanel.setController(controller);
        tabMain.setSelectedComponent(designerPanel);
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
        viewerPanel.add(canvas, pos, 0);
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
        tabMain = new javax.swing.JTabbedPane();
        designerPanel = new designer.DesignerPanel();
        viewerPanel = new viewer.engine.viewerPanel();
        panelOptions = new javax.swing.JPanel();
        labelOptionsTitle = new javax.swing.JLabel();
        labelType = new javax.swing.JLabel();
        radioTavern = new javax.swing.JRadioButton();
        radioChurch = new javax.swing.JRadioButton();
        radioHouse = new javax.swing.JRadioButton();
        buttonGenerate = new javax.swing.JButton();
        buttonClear = new javax.swing.JButton();
        buttonTest = new javax.swing.JButton();
        menuMain = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuitemExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RPG Building Generator");

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

        tabMain.addTab("Designer", designerPanel);

        javax.swing.GroupLayout viewerPanelLayout = new javax.swing.GroupLayout(viewerPanel);
        viewerPanel.setLayout(viewerPanelLayout);
        viewerPanelLayout.setHorizontalGroup(
            viewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        viewerPanelLayout.setVerticalGroup(
            viewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 378, Short.MAX_VALUE)
        );

        tabMain.addTab("Viewer", viewerPanel);

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

        buttonTest.setText("Test");
        buttonTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTestActionPerformed(evt);
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
                        .addContainerGap(34, Short.MAX_VALUE))
                    .addGroup(panelOptionsLayout.createSequentialGroup()
                        .addGroup(panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(buttonTest, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonClear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonGenerate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelOptionsLayout.setVerticalGroup(
            panelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelOptionsTitle)
                .addGap(32, 32, 32)
                .addComponent(labelType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioTavern)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioChurch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioHouse)
                .addGap(27, 27, 27)
                .addComponent(buttonGenerate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonClear)
                .addGap(51, 51, 51)
                .addComponent(buttonTest)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(tabMain, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabMain)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(640, 480));
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
        tabMain.setSelectedComponent(designerPanel);
    }//GEN-LAST:event_buttonClearActionPerformed

    private void buttonTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestActionPerformed
        designerPanel.Clear();
        controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.TEST);
        controller.getBuildingOutLine().addPoint(new Point(50,200));
        controller.getBuildingOutLine().addPoint(new Point(150,100));
        controller.getBuildingOutLine().addPoint(new Point(250,50));
        controller.getBuildingOutLine().addPoint(new Point(350,50));
        controller.getBuildingOutLine().addPoint(new Point(450,150));
        controller.getBuildingOutLine().addPoint(new Point(450,350));
        controller.getBuildingOutLine().addPoint(new Point(300,400));
        controller.getBuildingOutLine().addPoint(new Point(130,400));

        // finish outline by finishing on same point
        controller.getBuildingOutLine().addPoint(new Point(50,200));
        designerPanel.Generate();
    }//GEN-LAST:event_buttonTestActionPerformed

    private void buttonGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGenerateActionPerformed
        
        // TODO: need to only allow generation if a building outline has been defined
        // maybe put up a message box
        
        if (controller.getBuildingOutLine().isComplete()) {
            designerPanel.Generate();

            // Show 3D viewer <- not sure if this is desirable.  nice to show 2D view first?
            //tabMain.setSelectedComponent(viewerPanel);
        }
        
    }//GEN-LAST:event_buttonGenerateActionPerformed

    private void radioTavernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTavernActionPerformed
        controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.TAVERN);
    }//GEN-LAST:event_radioTavernActionPerformed

    private void radioHouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioHouseActionPerformed
        controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.HOUSE);
    }//GEN-LAST:event_radioHouseActionPerformed

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
    private javax.swing.JButton buttonTest;
    private javax.swing.ButtonGroup buttongroupType;
    private designer.DesignerPanel designerPanel;
    private javax.swing.JLabel labelOptionsTitle;
    private javax.swing.JLabel labelType;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuBar menuMain;
    private javax.swing.JMenuItem menuitemExit;
    private javax.swing.JPanel panelOptions;
    private javax.swing.JRadioButton radioChurch;
    private javax.swing.JRadioButton radioHouse;
    private javax.swing.JRadioButton radioTavern;
    private javax.swing.JTabbedPane tabMain;
    private viewer.engine.viewerPanel viewerPanel;
    // End of variables declaration//GEN-END:variables
}
