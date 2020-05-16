package viewer;

import com.jogamp.opengl.GL4;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


public class Mesh {

    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    private Vector3f colour;
    
    private Material material;
    
    public Mesh(GL4 gl, float[] positions, float[] textCoords, float[] normals, int[] indices) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            colour = Mesh.DEFAULT_COLOUR;
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            int[] vaoIds = new int[1];
            int[] vboIds = new int[1];
            
            gl.glGenVertexArrays(1, IntBuffer.wrap(vaoIds));
            vaoId = vaoIds[0];
            
            gl.glBindVertexArray(vaoId);
            
            // Position VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            
            vboIdList.add(vboIds[0]);          
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();                  
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, positions.length * 4, posBuffer, GL4.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(0);
            gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);
            
            // Texture coordinates VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            vboIdList.add(vboIds[0]);          
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();                 
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, textCoords.length * 4, textCoordsBuffer, GL4.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(1);
            gl.glVertexAttribPointer(1, 2, GL4.GL_FLOAT, false, 0, 0);
            
            // Vertex normals VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            vboIdList.add(vboIds[0]);          
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();             
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, normals.length * 4, vecNormalsBuffer, GL4.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(2);
            gl.glVertexAttribPointer(2, 3, GL4.GL_FLOAT, false, 0, 0);
            
            // Index VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            vboIdList.add(vboIds[0]);          
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();         
            gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indicesBuffer, GL4.GL_STATIC_DRAW);
            
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
            gl.glBindVertexArray(0);
            
            
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public Mesh(GL4 gl, float[] positions, float[] textCoords, int[] indices) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            colour = Mesh.DEFAULT_COLOUR;
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            int[] vaoIds = new int[1];
            int[] vboIds = new int[1];
            
            gl.glGenVertexArrays(1, IntBuffer.wrap(vaoIds));
            vaoId = vaoIds[0];
            
            gl.glBindVertexArray(vaoId);
            
            // Position VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            
            vboIdList.add(vboIds[0]);          
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();                  
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, positions.length * 4, posBuffer, GL4.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(0);
            gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);
            
            // Texture coordinates VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            vboIdList.add(vboIds[0]);          
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();                 
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, textCoords.length * 4, textCoordsBuffer, GL4.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(1);
            gl.glVertexAttribPointer(1, 2, GL4.GL_FLOAT, false, 0, 0);
                        
            // Index VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            vboIdList.add(vboIds[0]);          
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();         
            gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indicesBuffer, GL4.GL_STATIC_DRAW);
            
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
            gl.glBindVertexArray(0);
            
            
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }
    
    public Mesh(GL4 gl, float[] positions, int[] indices) {
        FloatBuffer posBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            colour = Mesh.DEFAULT_COLOUR;
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            int[] vaoIds = new int[1];
            int[] vboIds = new int[1];
            
            gl.glGenVertexArrays(1, IntBuffer.wrap(vaoIds));
            vaoId = vaoIds[0];
            
            gl.glBindVertexArray(vaoId);
            
            // Position VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            
            vboIdList.add(vboIds[0]);          
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();                  
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, positions.length * 4, posBuffer, GL4.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(0);
            gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);
                        
            // Index VBO
            gl.glGenBuffers(1, IntBuffer.wrap(vboIds));
            vboIdList.add(vboIds[0]);          
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();         
            gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, vboIds[0]);
            gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indicesBuffer, GL4.GL_STATIC_DRAW);
            
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
            gl.glBindVertexArray(0);
            
            
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public Vector3f getColour() {
        return this.colour;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render(GL4 gl) {
        Texture texture = material.getTexture();
        if (texture != null) {
            // Activate first texture bank
            gl.glActiveTexture(GL4.GL_TEXTURE0);
            // Bind the texture
            gl.glBindTexture(GL4.GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        gl.glBindVertexArray(getVaoId());

        gl.glDrawElements(GL4.GL_TRIANGLES, getVertexCount(), GL4.GL_UNSIGNED_INT, 0);

        // Restore state
        gl.glBindVertexArray(0);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, 0);
    }

    public void cleanUp(GL4 gl) {
        gl.glDisableVertexAttribArray(0);

        // Delete the VBOs
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            int[] vboIds = new int[1];
            vboIds[0] = vboId;
            gl.glDeleteBuffers(1, IntBuffer.wrap(vboIds));
        }

        // Delete the texture
        Texture texture = material.getTexture();
        if (texture != null) {
            texture.cleanup(gl);
        }

        // Delete the VAO
        gl.glBindVertexArray(0);
        int[] vaoIds = new int[1];
        vaoIds[0] = vaoId;
        gl.glDeleteVertexArrays(1, IntBuffer.wrap(vaoIds));
    }
}
