/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import floorplanner.FloorPlanner;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import shapes.BuildingOutline;

/**
 *
 * @author James
 */
public class Window extends JFrame implements ActionListener {
    
    private Renderer renderer;
    private JPanel mainPanel;
    private JPanel optionsPanel;
    private ButtonGroup presets;
    private ButtonGroup types;
    private Controller controller;
    RadioListener radioListener = new RadioListener();
        
    public Window(String title, FPSAnimator animator, int width, int height) {
        
        super(title);
        setupWindow(animator, width, height);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,0));
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupWindow(FPSAnimator animator, int width,int height) {
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(width, height));
        this.setLocationRelativeTo(null);
        
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
    
    public void setVisibility(boolean isVisible) {
        this.setVisible(isVisible);
    }
    
    public void setGLCanvas(GLCanvas canvas, String pos) {
        mainPanel.add(canvas, pos, 0);
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    public void createOptionsJPanel(String pos) {
        optionsPanel = new JPanel();
                
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        
        RadioListener myListener = new RadioListener();        
        
        addLabel("Presets", optionsPanel);
        this.presets = new ButtonGroup();
        addRadioButton("Medieval", optionsPanel, presets);
        addRadioButton("Modern", optionsPanel, presets);
        addRadioButton("Sci-Fi", optionsPanel, presets);

        addLabel("Building Type", optionsPanel);
        this.types = new ButtonGroup();

        addRadioButton("Tavern", optionsPanel, types);
        addRadioButton("Church", optionsPanel, types);
        addRadioButton("House", optionsPanel, types);

        
        addButton("GENERATE", optionsPanel);
        addButton("CLEAR", optionsPanel);
        
        mainPanel.add(optionsPanel, pos);
    }
    
    private void addButton(String text, Container container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(this);
        container.add(button);
        
    }
    
    private void addRadioButton(String text, Container container, ButtonGroup group) {
        JRadioButton radio = new JRadioButton(text);
        radio.setAlignmentX(Component.LEFT_ALIGNMENT);
        radio.addActionListener(radioListener);
        container.add(radio);
        group.add(radio);
    }
    
    private void addLabel(String text, Container container) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action;
        String type;

        action = e.getActionCommand();
        
        switch (action) {
            case "GENERATE":
                int w = this.mainPanel.getComponent(0).getWidth();
                int h = this.mainPanel.getComponent(0).getHeight();
                BuildingOutline buildingOutline = controller.getBuildingOutLine();
                controller.getFloorPlanner().generate(buildingOutline, w, h);
                break;
            case "CLEAR":
                controller.getBuildingOutLine().clear();
                controller.getFloorPlanner().clear();
                break;                
        }   
    }  
    
    class RadioListener implements ActionListener {

        public RadioListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String factoryName = null;
            System.out.println("ActionEvent received: " + e.getActionCommand());
            switch (e.getActionCommand()) {
                case "Tavern":
                    controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.TAVERN);
                    break;
                case "Church":
                    controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.CHURCH);
                    break;
                case "House":
                    controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.HOUSE);
                    break;
            }
        }
    }
}


