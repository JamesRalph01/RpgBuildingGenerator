/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import viewer.Renderer;

public class Main {
    
    private static final int HEIGHT = 640;
    private static final int WIDTH = 480;
    private static final int FPS = 60;
    
    public static void main(String[] args) {
                
        final GLProfile profile = GLProfile.get(GLProfile.GL4);
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setDoubleBuffered(true);
        capabilities.setHardwareAccelerated(true);
        
        GLCanvas canvas = new GLCanvas(capabilities);
        FPSAnimator animator = new FPSAnimator(canvas, FPS);
        Controller controller = new Controller();
                
        ApplicationMainWindow window = new ApplicationMainWindow(animator);
        Renderer renderer = new Renderer();
        
        canvas.addGLEventListener(renderer);
        canvas.addMouseListener(renderer);
        canvas.addMouseMotionListener(renderer);
        canvas.addKeyListener(renderer);
        
        window.setGLCanvas(canvas, BorderLayout.CENTER);
        
        animator.start();
        window.setVisible(true);
        
    }
}