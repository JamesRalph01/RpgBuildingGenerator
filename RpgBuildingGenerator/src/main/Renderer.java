/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import org.joml.Vector2f;
import handler.ShaderHandler;
import handler.BufferHandler;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import shapes.*;
import util.CoordSystemHelper;
import util.GeoHelper;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener {

    private int[] gridVaoHandle = new int[1];
    private int[] gridCursorVaoHandle = new int[1];
    private int programHandle;
    private Grid grid = new Grid();
    private GridCursor gridCursor = new GridCursor();
    private Vector2f vCursorPosition = new Vector2f(0,0); 
    private Vector2f vNearestGridPoint = new Vector2f(0,0);
    private int width, height;
    private Controller controller;
    
    private final int VERTEX_POSITION_INDEX = 0;
    private final int VERTEX_COLOUR_INDEX = 1;
    
    public Renderer(Controller controller) {
        this.controller = controller;
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
        
        programHandle = ShaderHandler.createProgram(shaderList, gl);
        
        BufferHandler.setupBuffers(gridVaoHandle, grid.getPositionData(), 
                grid.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
        
        BufferHandler.setupBuffers(gridCursorVaoHandle, gridCursor.getPositionData(), 
                gridCursor.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
        
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
     
        //obtain pointer to global variables from graphics pipeline (shaders)
        int loc = gl.glGetUniformLocation(programHandle, "Transform");
        
        //translation to zero and draw background grid
        if (loc != -1)
        {
            FloatBuffer fb2 = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().translate(0.0f,0.0f,0.0f).get(fb2);
            gl.glUniformMatrix4fv(loc, 1, false, fb2);
        }
        gl.glPointSize(2.0f);
        gl.glBindVertexArray(gridVaoHandle[0]);
        gl.glDrawArrays(GL4.GL_POINTS, 0, grid.numbervertices());

        //translate and draw cursor, snapping to nearest grid point 
        if (loc != -1)
        {
            FloatBuffer fb2 = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().translate(vNearestGridPoint.x,vNearestGridPoint.y,0.0f).get(fb2);
            gl.glUniformMatrix4fv(loc, 1, false, fb2);
        }
        gl.glBindVertexArray(gridCursorVaoHandle[0]);
        gl.glDrawArrays(GL4.GL_LINES, 0, gridCursor.numbervertices());
                
        //draw building outline
        int[] houseOutlineVaoHandle = new int[1];
        if (controller.getBuildingOutLine().size() >= 1) {

            BufferHandler.setupBuffers(houseOutlineVaoHandle, controller.getBuildingOutLine().getPositionData(), 
                    controller.getBuildingOutLine().getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
            if (loc != -1)
            {
                FloatBuffer fb2 = Buffers.newDirectFloatBuffer(16);
                new Matrix4f().translate(0.0f,0.0f,0.0f).get(fb2);
                gl.glUniformMatrix4fv(loc, 1, false, fb2);
            }
            gl.glLineWidth(4.0f);
            gl.glBindVertexArray(houseOutlineVaoHandle[0]);
            if (controller.getBuildingOutLine().isComplete()) {
                gl.glDrawArrays(GL4.GL_LINE_LOOP, 0, controller.getBuildingOutLine().numbervertices());  
            } else {
                gl.glDrawArrays(GL4.GL_LINE_STRIP, 0, controller.getBuildingOutLine().numbervertices());                  
            }

        }
        
        //draw floorplan
        if (controller.getFloorPlanner().activeFloorPlan()) {
            int[] floorplanVaoHandle = new int[1];
            BufferHandler.setupBuffers(floorplanVaoHandle, controller.getFloorPlanner().getPositionData(), 
                    controller.getFloorPlanner().getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
            if (loc != -1)
            {
                FloatBuffer fb2 = Buffers.newDirectFloatBuffer(16);
                new Matrix4f().translate(0.0f,0.0f,0.0f).get(fb2);
                gl.glUniformMatrix4fv(loc, 1, false, fb2);
            }
            gl.glLineWidth(1.0f);
            gl.glBindVertexArray(floorplanVaoHandle[0]);
            gl.glDrawArrays(GL4.GL_LINES, 0, controller.getFloorPlanner().numbervertices());  
        }
        
        gl.glFlush();
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        
        final GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(x, y, w, h);
        width = w;
        height = h;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.getBuildingOutLine().addPoint(vNearestGridPoint);
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
        vCursorPosition.x = (float) (e.getX() * 2.0f / (float) width - 1.0);
        vCursorPosition.y = (float) (e.getY() * 2.0f / (float) height - 1.0);
        vCursorPosition.y = -vCursorPosition.y;
        
        vNearestGridPoint = grid.getNearestGridPoint(vCursorPosition);
        controller.getBuildingOutLine().setCursorLocation(vNearestGridPoint);
        
        //is point inside polygon?
        if (controller.getBuildingOutLine().isComplete()) {
            ArrayList<Vector2i> polygon;
            
            polygon = CoordSystemHelper.openGLToDevice(width, height, controller.getBuildingOutLine().points()); 
            if (GeoHelper.isPointInsidePolygon(polygon, new Vector2i(e.getX(), e.getY()))) {
                System.out.printf("Cursor inside \n");
            } else {
                System.out.printf("Cursor outside \n");
            }
        }
    }
}
