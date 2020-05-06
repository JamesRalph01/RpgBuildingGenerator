package viewer.engine;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final String title;

    private int width;

    private int height;

    private long windowHandle;

    private boolean resized;

    private boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
    }


    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }
}


//package main;
//
//import com.jogamp.opengl.awt.GLCanvas;
//import com.jogamp.opengl.util.FPSAnimator;
//import floorplanner.FloorPlanner;
//import java.awt.BorderLayout;
//import java.awt.Component;
//import java.awt.Container;
//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
//import javax.swing.ButtonGroup;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JRadioButton;
//import shapes.BuildingOutline;
//import util.CoordSystemHelper;
//import util.Point;
//
///**
// *
// * @author James
// */
//public class Window extends JFrame implements ActionListener {
//    
//    private Renderer renderer;
//    private JPanel mainPanel;
//    private JPanel optionsPanel;
//    private ButtonGroup presets;
//    private ButtonGroup types;
//    private Controller controller;
//    RadioListener radioListener = new RadioListener();
//        
//    public Window(String title, FPSAnimator animator, int width, int height) {
//        
//        super(title);
//        setupWindow(animator, width, height);
//        CoordSystemHelper.initDevice(width, height);
//        mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
//        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
//    }
//    
//    private void setupWindow(FPSAnimator animator, int width,int height) {
//        this.setLayout(new BorderLayout());
//        this.setSize(new Dimension(width, height));
//        this.setLocationRelativeTo(null);
//        
//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                if (animator.isStarted()) {
//                    System.out.println("Stopping FPSAnimator...");
//                    animator.stop();
//                    System.out.println("FPSAnimator stopped...");
//                }
//                System.out.println("Closing Window...");
//                System.exit(0);
//            }
//        });
//    }
//    
//    public void setVisibility(boolean isVisible) {
//        this.setVisible(isVisible);
//    }
//    
//    public void setGLCanvas(GLCanvas canvas, String pos) {
//        mainPanel.add(canvas, pos, 0);
//    }
//    
//    public void setController(Controller controller) {
//        this.controller = controller;
//    }
//    
//    public void createOptionsJPanel(String pos) {
//        optionsPanel = new JPanel();
//                
//        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
//        
//        RadioListener myListener = new RadioListener();        
//        
//        addLabel("Presets", optionsPanel);
//        this.presets = new ButtonGroup();
//        addRadioButton("Medieval", optionsPanel, presets);
//        addRadioButton("Modern", optionsPanel, presets);
//        addRadioButton("Sci-Fi", optionsPanel, presets);
//
//        addLabel("Building Type", optionsPanel);
//        this.types = new ButtonGroup();
//
//        addRadioButton("Tavern", optionsPanel, types);
//        addRadioButton("Church", optionsPanel, types);
//        addRadioButton("House", optionsPanel, types);
//
//        
//        addButton("GENERATE", optionsPanel);
//        addButton("CLEAR", optionsPanel);
//        addButton("TEST", optionsPanel);
//        addButton("Toggle outline", optionsPanel);
//        
//        mainPanel.add(optionsPanel, pos);
//    }
//    
//    private void addButton(String text, Container container) {
//        JButton button = new JButton(text);
//        button.setAlignmentX(Component.LEFT_ALIGNMENT);
//        button.addActionListener(this);
//        container.add(button);
//        
//    }
//    
//    private void addRadioButton(String text, Container container, ButtonGroup group) {
//        JRadioButton radio = new JRadioButton(text);
//        radio.setAlignmentX(Component.LEFT_ALIGNMENT);
//        radio.addActionListener(radioListener);
//        container.add(radio);
//        group.add(radio);
//    }
//    
//    private void addLabel(String text, Container container) {
//        JLabel label = new JLabel(text);
//        label.setAlignmentX(Component.LEFT_ALIGNMENT);
//        container.add(label);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String action;
//        String type;
//        BuildingOutline buildingOutline;
//        int w, h;
//
//        w = this.mainPanel.getComponent(0).getWidth();
//        h = this.mainPanel.getComponent(0).getHeight();
//        buildingOutline = controller.getBuildingOutLine();
//        
//        action = e.getActionCommand();
//        
//        switch (action) {
//            case "GENERATE":
//                controller.getFloorPlanner().generate(buildingOutline, w, h);
//                break;
//            case "TEST":
//                controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.TEST);
//                controller.getBuildingOutLine().clear();
//                controller.getBuildingOutLine().addPoint(new Point(50,200));
//                controller.getBuildingOutLine().addPoint(new Point(150,100));
//                controller.getBuildingOutLine().addPoint(new Point(250,50));
//                controller.getBuildingOutLine().addPoint(new Point(350,50));
//                controller.getBuildingOutLine().addPoint(new Point(450,150));
//                controller.getBuildingOutLine().addPoint(new Point(450,350));
//                controller.getBuildingOutLine().addPoint(new Point(300,400));
//                controller.getBuildingOutLine().addPoint(new Point(130,400));
//       
//                // finish outline by finishing on same point
//                controller.getBuildingOutLine().addPoint(new Point(50,200));
//                controller.getFloorPlanner().generate(buildingOutline, w, h);
//                break;               
//            case "CLEAR":
//                controller.getBuildingOutLine().clear();
//                controller.getFloorPlanner().clear();
//                controller.showOutline = true; // reset
//                break;   
//            case "Toggle outline":
//                controller.showOutline = !controller.showOutline;
//                break; 
//        }   
//    }  
//    
//    class RadioListener implements ActionListener {
//
//        public RadioListener() {
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String factoryName = null;
//            System.out.println("ActionEvent received: " + e.getActionCommand());
//            switch (e.getActionCommand()) {
//                case "Tavern":
//                    controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.TAVERN);
//                    break;
//                case "Church":
//                    controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.CHURCH);
//                    break;
//                case "House":
//                    controller.getFloorPlanner().setBuildingType(FloorPlanner.BuildingType.HOUSE);
//                    break;
//            }
//        }
//    }
//}
