/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import designer.BuildingOutline;
import floorplanner.FloorPlanner;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import util.CoordSystemHelper;
import util.Point;

/**
 *
 * @author James
 */
public class zApplicationMainWindow extends JFrame implements ActionListener {
    
    private Renderer renderer;
    private JPanel mainPanel;
    private JPanel optionsPanel;
    private ButtonGroup presets;
    private ButtonGroup types;
    private Controller controller;
    RadioListener radioListener = new RadioListener();
        
    public zApplicationMainWindow(String title, int width, int height, Controller controller) {        
        super(title);
        this.controller = controller;
        initUI(width, height);
    }
    
    private void initUI(int width, int height) {
        
        // Set window hight and layout
        this.setSize(new Dimension(width, height));
        this.setLocationRelativeTo(null); 
        this.setLayout(new BorderLayout());

        // Configure controls and layout
        createMenuBar();
        createDesignerPanel();
        createOptionsPanel();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void createDesignerPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);        
    }
    
    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        //var exitIcon = new ImageIcon("src/resources/exit.png");

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }
    
    private void createOptionsPanel() {
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
        addButton("TEST", optionsPanel);
        addButton("Toggle outline", optionsPanel);
        
        mainPanel.add(optionsPanel, BorderLayout.LINE_END);
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
        BuildingOutline buildingOutline;
        int w, h;

        w = this.mainPanel.getComponent(0).getWidth();
        h = this.mainPanel.getComponent(0).getHeight();
        buildingOutline = controller.getBuildingOutLine();
        
        action = e.getActionCommand();
        
        switch (action) {
            case "GENERATE":
                controller.getFloorPlanner().generate(buildingOutline, w, h);
                break;
            case "TEST":
                controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.TEST);
                controller.getBuildingOutLine().clear();
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
                controller.getFloorPlanner().generate(buildingOutline, w, h);
                break;               
            case "CLEAR":
                controller.getBuildingOutLine().clear();
                controller.getFloorPlanner().clear();
                controller.showOutline = true; // reset
                break;   
            case "Toggle outline":
                controller.showOutline = !controller.showOutline;
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


