/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

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
    public boolean isInternal = false;
    
    private Edge edge;
    private final float width;
    private final float height;
    private Vector2f p0, p1, p2, p3;
    private float depth;
    
    
    public Wall(Edge edge) {
        super();
        this.edge = edge;
        if (edge.isInternal()) {
            this.width = 4;
        } else {
            this.width = 6; // Chunky external walls            
        }

        this.height = 30;
    }

    public void Generate3DPositions(Vector3f screenOrigin) {
        calcWall2DPoints();
        generatePositions();
        calcLocation(screenOrigin);    
    }
    
    private void calcLocation(Vector3f screenOrigin) {
        this.setLocation(-screenOrigin.x, -screenOrigin.y, screenOrigin.z);        
    }
    

    private void generatePositions() {
                
        positions = new float[] {
            
            // FRONT FACE
            p0.x, p0.y, this.height, // V0
            p0.x, p0.y, 0,           // V1
            p1.x, p1.y, 0,           // V2
            p1.x, p1.y, this.height, // V3
            
            // TOP FACE
            p2.x, p2.y, this.height, // V4
            p0.x, p0.y, this.height, // V5
            p1.x, p1.y, this.height, // V6
            p3.x, p3.y, this.height, // V7
            
            // RIGHT FACE
            p1.x, p1.y, this.height, // V8
            p1.x, p1.y, 0,           // V9
            p3.x, p3.y, 0,           // V10
            p3.x, p3.y, this.height, // V11
            
            // BACK FACE
            p3.x, p3.y, this.height, // V12
            p3.x, p3.y, 0,           // V13
            p2.x, p2.y, 0,           // V14
            p2.x, p2.y, this.height, // V15
            
            // LEFT FACE
            p2.x, p2.y, this.height, // V16
            p2.x, p2.y, 0,           // V17
            p0.x, p0.y, 0,           // V18
            p0.x, p0.y, this.height, // V19
            
            // BOTTOM FACE
            p3.x, p3.y, 0,           // V20
            p1.x, p1.y, 0,           // V21
            p0.x, p0.y, 0,           // V22
            p2.x, p2.y, 0,           // V23
        };
        
        float ratio = depth/height;
        System.out.println("RATIO >>>>>>> " + ratio);
        textCoords = new float[]{
            // FRONT FACE
            0.0f, 0.0f,
            ratio, 0.0f,
            ratio, height,
            0.0f, height,
            
            // TOP FACE
            0.0f, 0.0f,
            ratio, 0.0f,
            ratio, height,
            0.0f, height,
            
            // RIGHT FACE
            0.0f, 0.0f,
            ratio, 0.0f,
            ratio, height,
            0.0f, height,
            
            // BACK FACE
            0.0f, 0.0f,
            ratio, 0.0f,
            ratio, height,
            0.0f, height,
            
            // LEFT FACE
            0.0f, 0.0f,
            ratio, 0.0f,
            ratio, height,
            0.0f, height,
            
            // BOTTOM FACE
            0.0f, 0.0f,
            ratio, 0.0f,
            ratio, height,
            0.0f, height,
            

        };
        
        indices = new int[] {
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            4, 5, 7, 7, 5, 6,
            // Right face
            8, 9, 11, 11, 9, 10,
            // Bottom face
            12, 13, 15, 15, 13, 14,
            // Left face
            16, 17, 19, 19, 17, 18,
            // Back face
            20, 21, 23, 23, 21, 22,
        };
    }
    
    
    private void calcWall2DPoints() {
        float Dx, Dy, D;
        float xMin, xMax, yMin, yMax, zMin, zMax;

        Dx = edge.x2() - edge.x1();
        Dy = edge.y2() - edge.y1();

        D = (float) Math.sqrt(Dx * Dx + Dy * Dy);
        this.depth = D;

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
