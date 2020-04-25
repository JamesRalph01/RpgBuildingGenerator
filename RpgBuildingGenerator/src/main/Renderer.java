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

    private int programHandle;
    private Grid grid = new Grid();
    private GridCursor gridCursor = new GridCursor();
    private editCursorLine editCursorLine = new editCursorLine();
    private Vector2i cursorPosition = new Vector2i(0,0); 
    private Vector2i nearestGridPoint = new Vector2i(0,0);
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
        int[] gridVaoHandle = new int[1];
        BufferHandler.setupBuffers(gridVaoHandle, grid.getPositionData(), 
                grid.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
        if (loc != -1)
        {
            FloatBuffer fb2 = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().translate(0.0f,0.0f,0.0f).get(fb2);
            gl.glUniformMatrix4fv(loc, 1, false, fb2);
        }
        gl.glPointSize(2.0f);
        gl.glBindVertexArray(gridVaoHandle[0]);
        gl.glDrawArrays(GL4.GL_POINTS, 0, grid.numbervertices());

        // line from last point picked to moving cursor
        if (controller.getBuildingOutLine().isComplete() == false && 
            controller.getBuildingOutLine().points().size() > 0) {
            int[] editLineVaoHandle = new int[1];
            BufferHandler.setupBuffers(editLineVaoHandle, editCursorLine.getPositionData(), 
                    editCursorLine.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
            if (loc != -1)
            {
                FloatBuffer fb2 = Buffers.newDirectFloatBuffer(16);
                new Matrix4f().translate(0.0f,0.0f,0.0f).get(fb2);
                gl.glUniformMatrix4fv(loc, 1, false, fb2);
            }
            gl.glLineWidth(1.0f);
            gl.glBindVertexArray(editLineVaoHandle[0]);
            gl.glDrawArrays(GL4.GL_LINES, 0, editCursorLine.numbervertices());        
        }
        
        // moving grid crosshair cursor
        int[] gridCursorVaoHandle = new int[1];
        BufferHandler.setupBuffers(gridCursorVaoHandle, gridCursor.getPositionData(), 
                gridCursor.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
        if (loc != -1)
        {
            FloatBuffer fb2 = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().translate(0.0f,0.0f,0.0f).get(fb2);
            gl.glUniformMatrix4fv(loc, 1, false, fb2);
        }
        gl.glLineWidth(1.0f);
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
        CoordSystemHelper.initDevice(w, h);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.getBuildingOutLine().addPoint(nearestGridPoint);
        editCursorLine.fromPoint(nearestGridPoint);
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

        System.out.printf("Cursor x %d, y %d \n", e.getX(), e.getY());
        System.out.printf("dimensions width %d, height %d \n", CoordSystemHelper.deviceWidth, CoordSystemHelper.deviceHeight);
        
        cursorPosition = new Vector2i(e.getX(), e.getY());
        nearestGridPoint = grid.getNearestGridPoint(cursorPosition);
        gridCursor.cursorPosition(nearestGridPoint);
        editCursorLine.ToPoint(nearestGridPoint);
        
        //is point inside polygon?
        if (controller.getBuildingOutLine().isComplete()) {
            ArrayList<Vector2i> polygon;
            Vector2i pointToCheck;
            
            polygon = controller.getBuildingOutLine().points(); 
            //pointToCheck = new Vector2i(e.getX(), e.getY());
            pointToCheck = nearestGridPoint;
            
            if (GeoHelper.isPointInsidePolygon(polygon, pointToCheck)) {
                System.out.printf("Cursor inside x %d, y %d \n", pointToCheck.x, pointToCheck.y );
            } else {
                System.out.printf("Cursor outside x %d, y %d \n", pointToCheck.x, pointToCheck.y );
            }
        }
    }
}
