/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

public class zMainGameLoop {
    
    private static final int HEIGHT = 640;
    private static final int WIDTH = 480;
    private static final int FPS = 60;
    
    public static void main(String[] args) {
        Controller controller = new Controller();
        
        EventQueue.invokeLater(() -> {
            zApplicationMainWindow window = new zApplicationMainWindow("RPG-Toolkit Designer", HEIGHT, WIDTH, controller);
            window.setVisible(true);            
        });

    }
}
