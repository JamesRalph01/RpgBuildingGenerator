/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import handler.ShaderHandler;
import handler.BufferHandler;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import shapes.*;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener {

    private int[] triangleVaoHandle = new int[1];
    private int[] squareVaoHandle = new int[1];
    private int[] circleVaoHandle = new int[1];
    private int[] gridVaoHandle = new int[1];
    private int[] gridCursorVaoHandle = new int[1];
    private Grid grid = new Grid();
    private GridCursor gridCursor = new GridCursor();
    
    public Renderer() {
        
    }
    
    @Override
    public void init(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();
        
         // Output OpenGL version
        System.out.println(" GL_VERSION: "+ gl.glGetString(GL4.GL_VERSION) );
        
        // Create Shader Objects
        int vertexShader = ShaderHandler.createShader("shaders/vertex_shader.glsl", GL4.GL_VERTEX_SHADER, gl);
        int fragmentShader = ShaderHandler.createShader("shaders/fragment_shader.glsl", GL4.GL_FRAGMENT_SHADER, gl);
        int shaderList[] = {vertexShader,fragmentShader};      
        int programHandle = ShaderHandler.createProgram(shaderList, gl);
        
        final int VERTEX_POSITION_INDEX = 0;
        final int VERTEX_COLOUR_INDEX = 1;
        
        BufferHandler.setupBuffers(gridVaoHandle, grid.getPositionData(), 
                grid.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
        
        BufferHandler.setupBuffers(gridCursorVaoHandle, gridCursor.getPositionData(), 
                gridCursor.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
        
        /*Triangle triangle = new Triangle();
        BufferHandler.setupBuffers(triangleVaoHandle, triangle.getPositionData(), 
                triangle.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);*/
       
        /*
        Circle circle = new Circle(-0.5f,0.5f,0.02f,40);
        BufferHandler.setupBuffers(circleVaoHandle, circle.getPositionData(), 
                circle.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl); */
        ShaderHandler.linkProgram(programHandle, gl);
        gl.glUseProgram(programHandle);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();
        
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT);
        
        gl.glPointSize(2.0F);
        gl.glBindVertexArray(gridVaoHandle[0]);
        gl.glDrawArrays(GL4.GL_POINTS, 0, grid.getPositionData().length);
        
        gl.glLineWidth(2.0F);
        gl.glBindVertexArray(gridCursorVaoHandle[0]);
        gl.glDrawArrays(GL4.GL_LINES, 0, gridCursor.getPositionData().length);
        
        /*
        gl.glBindVertexArray(triangleVaoHandle[0]);
        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 3); */
        
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        
        final GL4 gl = drawable.getGL().getGL4();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("mouseMoved x:" + e.getX() + " y:" + e.getY());
    }
    
}
