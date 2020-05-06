/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import java.nio.FloatBuffer;

public class BufferHandler {
    
    public static void setupBuffers(int[] objectVaoHandle, float[] positionData,
            float[] colourData, final int vertexPositionIndex, 
            final int colourPositionIndex, GL4 gl) {
        
        // Create the buffer objects
        int vboHandles[] = new int [2];
        gl.glGenBuffers(2, vboHandles, 0);
        
        // Assign handles to descriptive variables
        int positionBufferHandle = vboHandles[0];
        int colourBufferHandle = vboHandles[1];
        
        // Populate the position buffer
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, positionBufferHandle);
        FloatBuffer positionBufferData = Buffers.newDirectFloatBuffer(positionData);
        int numBytes = positionData.length * 4;
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, numBytes, positionBufferData, GL4.GL_STATIC_DRAW);
        
        // Populate the colour buffer
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, colourBufferHandle);
        FloatBuffer colourBufferData = Buffers.newDirectFloatBuffer(colourData);
        numBytes = colourData.length * 4;
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, numBytes, colourBufferData, GL4.GL_STATIC_DRAW);
        
        // Create and set up the vertex array object
        gl.glGenVertexArrays(1, objectVaoHandle, 0);
        gl.glBindVertexArray(objectVaoHandle[0]);
        
        // Enable the vertex attribute arrays
        gl.glEnableVertexAttribArray(vertexPositionIndex); // Vertex pos
        gl.glEnableVertexAttribArray(colourPositionIndex); // Vertex colour
        
        // Map index 0 to the position buffer
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, positionBufferHandle);
        gl.glVertexAttribPointer(vertexPositionIndex, 3, GL4.GL_FLOAT, Boolean.FALSE, 0, 0);
        
        // Map index 1 to the colour buffer
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, colourBufferHandle);
        gl.glVertexAttribPointer(colourPositionIndex, 3, GL4.GL_FLOAT, Boolean.FALSE, 0, 0);
    }
    
}
