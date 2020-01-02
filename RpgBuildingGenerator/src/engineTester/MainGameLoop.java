/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engineTester;

import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

/**
 *
 * @author James
 */
public class MainGameLoop {
    
    public static void main(String[] args) {
        
        DisplayManager.createDisplay();
        
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        
        // Counter Clockwise Vertices
        float[] vertices = {
           // LEFT BOTTOM TRIANGLE
           -0.5f, 0.5f, 0f,
           -0.5f, -0.5f, 0f,
           0.5f, -0.5f, 0f,
           // RIGHT TOP TRIANGLE
           0.5f, -0.5f, 0f,
           0.5f, 0.5f, 0f,
           -0.5f, 0.5f, 0f
         };
        
        RawModel model = loader.loadToVAO(vertices);
            
        while(!Display.isCloseRequested()){
            
            //game logic
            renderer.prepare();
            renderer.render(model);
            DisplayManager.updateDisplay();
        }
        
        //loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
