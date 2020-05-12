/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import org.joml.Vector2f;
import util.Edge;

/**
 *
 * @author chrisralph
 */
public class Wall extends BuildingItem {

    public float[] positions;
    public float[] textCoords;
    public int[] indices;
    
    private Edge edge;
    private final float width;
    private final float height;
    private Vector2f p0, p1, p2, p3;
    
    
    public Wall(Edge edge, Vector2f screenOrigin) {
        super();
        this.edge = edge;
        this.width = 5;
        this.height = 20;
        calcWall2DPoints();
        generatePositions();
        calcLocation(screenOrigin);

    }

    private void calcLocation(Vector2f screenOrigin) {
        this.setLocation(-screenOrigin.x, -screenOrigin.y, 0);        
    }
    

    private void generatePositions() {
                
        positions = new float[] {

            // V0
            p0.x, p0.y, this.height,
            // V1
            p0.x, p0.y, 0,
            // V2
            p1.x, p1.y, 0,
            // V3
            p1.x, p1.y, this.height,
            // V4
            p2.x, p2.y, this.height,
            // V5
            p3.x, p3.y, this.height,
            // V6
            p3.x, p3.y, 0,
            // V7
            p2.x, p2.y, 0,

            // For text coords in top face
            // V8: V4 repeated
            p2.x, p2.y, this.height,
            // V9: V5 repeated
            p3.x, p3.y, this.height,
            // V10: V0 repeated
            p0.x, p0.y, this.height,
            // V11: V3 repeated
            p1.x, p1.y, this.height,

            // For text coords in right face
            // V12: V3 repeated
            p1.x, p1.y, this.height,
            // V13: V2 repeated
            p1.x, p1.y, 0,

            // For text coords in left face
            // V14: V0 repeated
            p0.x, p0.y, this.height,
            // V15: V1 repeated
            p0.x, p0.y, 0,

            // For text coords in bottom face
            // V16: V6 repeated
            p3.x, p3.y, 0,
            // V17: V7 repeated
            p2.x, p2.y, 0,
            // V18: V1 repeated
            p0.x, p0.y, 0,
            // V19: V2 repeated
            p1.x, p1.y, 0,
        };
                
        textCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            
            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
        };
        
        indices = new int[] {
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,};
    }
    
    
    private void calcWall2DPoints() {
         float Dx, Dy, D;
         float xMin, xMax, yMin, yMax, zMin, zMax;

         Dx = edge.x2() - edge.x1();
         Dy = edge.y2() - edge.y1();

         D = (float) Math.sqrt(Dx * Dx + Dy * Dy);

         Dx = (float) 0.5 * width * Dx / D;
         Dy = (float) 0.5 * width * Dy / D;

         xMin = Math.min(edge.x1(), edge.x2()) - Math.abs(Dy);
         xMax = Math.max(edge.x1(), edge.x2()) + Math.abs(Dy);

         yMin = Math.min(edge.y1(), edge.y2()) - Math.abs(Dx);
         yMax = Math.max(edge.y1(), edge.y2()) + Math.abs(Dx);
         
         
         p0 = new Vector2f(edge.x1() - Dy, edge.y1() + Dx);
         p1 = new Vector2f(edge.x1() + Dy, edge.y1() - Dx);
         
         p2 = new Vector2f(edge.x2() - Dy, edge.y2() + Dx);
         p3 = new Vector2f(edge.x2() + Dy, edge.y2() - Dx);
         
        
         //this.setBounds(xMin, yMin, zMin, xMax, yMax, zMax);
           
    }
   
}
