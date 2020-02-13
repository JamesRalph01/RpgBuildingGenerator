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

public class MainGameLoop {
    
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
        Renderer renderer = new Renderer();
        
        Window window = new Window("RPG-Toolkit", animator,  HEIGHT, WIDTH);
        
        canvas.addGLEventListener(renderer);
        canvas.addMouseListener(renderer);
        canvas.addMouseMotionListener(renderer);
        
        window.setGLCanvas(canvas, BorderLayout.CENTER);
        window.createOptionsJPanel(BorderLayout.LINE_END);
        animator.start();
        window.setVisibility(true);
        
    }
}
