/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector3f;
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
    private float width, height, depth;
    private Vector2f p0, p1, p2, p3;
    
    
    public Wall(Edge edge, float width, float height, Vector2f screenOrigin) {
        super();
        this.edge = edge;
        this.width = width;
        this.height = height;
        this.depth = (float) Math.sqrt(((edge.x2() - edge.x1()) * (edge.x2() - edge.x1())) + 
                                       ((edge.y2() - edge.y1()) * (edge.y2() - edge.y1())));
        calcWall2DPoints();
        generatePositions2();
        calcLocation(screenOrigin);

    }

    private void calcLocation(Vector2f screenOrigin) {
        this.setLocation(-screenOrigin.x, -screenOrigin.y, 0);        
    }
    
     private void generateWall3D(Vector2f screenOrigin) {

        // Calculate target location and rotation in 3D world space
        float locX = -screenOrigin.x;
        float locY = -screenOrigin.y;
        this.setLocation(locX, locY, 0);

        // translate Edge to Origin and calculate angle of rotation
        // Only need one point for this

        
        float angle = (float) Math.toDegrees(Math.atan2(edge.y2()-edge.y1(), edge.x2()- edge.x1()));

        if (angle < 0.0) {
            angle += 360.0;
        }
         
        this.setRotation(0, angle, 0);

        //Calculate faces
        this.generatePositions();
    }
    
    private void generatePositions() {
        float x = this.width / 2.0f;
        float y = this.height / 2.0f;
        float z = this.depth / 2.0f;
                
        positions = new float[] {

            // V0
            -x, y, -z,
            // V1
            -x, -y, -z,
            // V2
            x, -y, -z,
            // V3
            x, y, -z,
            // V4
            -x, y, z,
            // V5
            x, y, z,
            // V6
            -x, -y, z,
            // V7
            x, -y, z,

            // For text coords in top face
            // V8: V4 repeated
            -x, y, z,
            // V9: V5 repeated
            x, y, z,
            // V10: V0 repeated
            -x, y, -z,
            // V11: V3 repeated
            x, y, -z,

            // For text coords in right face
            // V12: V3 repeated
            x, y, -z,
            // V13: V2 repeated
            x, -y, -z,

            // For text coords in left face
            // V14: V0 repeated
            -x, y, -z,
            // V15: V1 repeated
            -x, -y, -z,

            // For text coords in bottom face
            // V16: V6 repeated
            -x, -y, z,
            // V17: V7 repeated
            x, -y, z,
            // V18: V1 repeated
            -x, -y, -z,
            // V19: V2 repeated
            x, -y, -z,
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
     
    private void generatePositions2() {
                
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
