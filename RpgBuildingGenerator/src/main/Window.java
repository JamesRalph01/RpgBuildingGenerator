/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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

/**
 *
 * @author James
 */
public class Window extends JFrame {
    
    private JPanel mainPanel;
    private JPanel optionsPanel;
    private ButtonGroup presets;
    private ButtonGroup types;
    
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
        mainPanel.add(canvas, pos);
    }
    
    public void createOptionsJPanel(String pos) {
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        
        
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
        
        mainPanel.add(optionsPanel, pos);
    }
    
    private void addButton(String text, Container container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(button);
    }
    
    private void addRadioButton(String text, Container container, ButtonGroup group) {
        JRadioButton radio = new JRadioButton(text);
        radio.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(radio);
        group.add(radio);
    }
    
    private void addLabel(String text, Container container) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(label);
    }
         
    
}
