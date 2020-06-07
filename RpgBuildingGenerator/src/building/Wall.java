/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import org.joml.GeometryUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Edge;
import java.util.ArrayList;

public class Wall extends BuildingItem {

    public float[] positions;
    public float[] textCoords;
    public int[] indices;
    public float[] normals;
    public boolean isInternal = false;
    
    private Edge edge;
    private final float width;
    private final float height;
    private Vector2f p0, p1, p2, p3;
    private float depth;
    
    
    public Wall(Edge edge) {
        super();
        this.edge = edge;
        this.isInternal = edge.isInternal();
        if (this.isInternal) {
            this.rootPath = "internal_walls/";
        } else {
            this.rootPath = "external_walls/";           
        }
            
        this.textures = new String[1];
        
               
        if (this.isInternal) {
            this.width = 4;
        } else {
            this.width = 6; // Chunky external walls            
        }

        this.height = 50;
    }

    public Edge getEdge() {
        return this.edge;
    }
    
    public void Generate3DPositionsInternal(Vector3f screenOrigin, int wealthInd, Room.RoomType roomType) {
        calcWall2DPoints();
        generatePositions();
        calcLocation(screenOrigin);
        chooseRoomWallTexture(wealthInd, roomType);
    }
    
     public void Generate3DPositionsExternal(Vector3f screenOrigin, String externalWallTexture) {
        calcWall2DPoints();
        generatePositions();
        calcLocation(screenOrigin);
    
        
        this.textures[0] = externalWallTexture; // Same texture set for all external walls
    }
    
    private void calcLocation(Vector3f screenOrigin) {
        this.setLocation(-screenOrigin.x, -screenOrigin.y, screenOrigin.z);        
    }
    

    private void generatePositions() {
                
        positions = new float[] {
            
            // FRONT FACE
            p0.x, this.height, p0.y, // V0
            p0.x, 0, p0.y,           // V1
            p1.x, 0, p1.y,           // V2
            p1.x, this.height, p1.y, // V3
            
            // TOP FACE
            p2.x, this.height, p2.y, // V4
            p0.x, this.height, p0.y, // V5
            p1.x, this.height, p1.y, // V6
            p3.x, this.height,p3.y,  // V7
            
            // RIGHT FACE
            p1.x, this.height, p1.y, // V8
            p1.x, 0, p1.y,           // V9
            p3.x, 0, p3.y,           // V10
            p3.x, this.height, p3.y, // V11
            
            // BACK FACE
            p3.x, this.height,p3.y,  // V12
            p3.x, 0, p3.y,           // V13
            p2.x, 0, p2.y,           // V14
            p2.x, this.height, p2.y, // V15
            
            // LEFT FACE
            p2.x, this.height,p2.y,  // V16
            p2.x, 0, p2.y,           // V17
            p0.x, 0, p0.y,           // V18
            p0.x, this.height, p0.y, // V19
            
            // BOTTOM FACE
            p3.x, 0, p3.y,           // V20
            p1.x, 0, p1.y,           // V21
            p0.x, 0, p0.y,           // V22
            p2.x, 0, p2.y,           // V23
        };
        
        float heightRatio;
        float depthRatio;
        
        if (isInternal) { //Internal Wall
            heightRatio = 4;
            depthRatio = depth/2/heightRatio;
        }
        else { // External Wall
            heightRatio = 1;
            depthRatio = depth/30/heightRatio;
        }
        //System.out.println("RATIO >>>>>>> " + heightRatio + "   " + depthRatio);
        textCoords = new float[]{
            // FRONT FACE
            0.0f, heightRatio,
            0.0f, 0.0f,
            depthRatio, 0.0f,
            depthRatio, heightRatio,
            
            // TOP FACE
            0.0f, heightRatio,
            0.0f, 0.0f,
            depthRatio, 0.0f,
            depthRatio, heightRatio,
            
            // RIGHT FACE
            0.0f, heightRatio,
            0.0f, 0.0f,
            depthRatio, 0.0f,
            depthRatio, heightRatio,
            
            // BACK FACE
            0.0f, heightRatio,
            0.0f, 0.0f,
            depthRatio, 0.0f,
            depthRatio, heightRatio,
            
            // LEFT FACE
            0.0f, heightRatio,
            0.0f, 0.0f,
            depthRatio, 0.0f,
            depthRatio, heightRatio,
            
            // BOTTOM FACE
            0.0f, heightRatio,
            0.0f, 0.0f,
            depthRatio, 0.0f,
            depthRatio, heightRatio,
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
        
        calcNormals();   
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
    
    
    private void chooseRoomWallTexture(int wealthInd, Room.RoomType roomType) {
        
        switch (roomType) {
            case MasterBedroom:
                this.textures[0] = "pattern.png"; 
                break;
            case LivingRoom:
                this.textures[0] = "Red_stone_wall.png"; 
                break;
            case Kitchen:
                this.textures[0] = "Marble_tiles.png"; 
                break;
            case Bathroom:
                this.textures[0] = "blue_tiles.png"; 
                break;
            case SpareRoom:
                this.textures[0] = "pattern.png"; 
                break;
            case Toilet:
                this.textures[0] = "blue_tiles.png"; 
                break;
            case Utility:
                this.textures[0] = "Bronze.png"; 
                break;
            case DiningRoom:
                this.textures[0] = "Red_stone_wall.png"; 
                break;
            default:
                this.textures[0] = "Grunge_wall.png";
                break;
        }
    }
    
    private void calcNormals() {
        ArrayList<Vector3f> vNormals = new ArrayList<>();
        
        for (int i=0; i<indices.length; i+=3) {
            int offsetV0 = indices[i] * 3;
            int offsetV1 = indices[i+1] * 3;
            int offsetV2 = indices[i+2] * 3;
            Vector3f v0 = new Vector3f(positions[offsetV0], positions[offsetV0+1], positions[offsetV0+2]);
            Vector3f v1 = new Vector3f(positions[offsetV1], positions[offsetV1+1], positions[offsetV1+2]);
            Vector3f v2 = new Vector3f(positions[offsetV2], positions[offsetV2+1], positions[offsetV2+2]);
            Vector3f normal = new Vector3f();
            GeometryUtils.normal(v0, v1, v2, normal);
            vNormals.add(normal);
        }
        
        this.normals = new float[vNormals.size() * 3];
        int i=0;
        for (Vector3f normal : vNormals) {
            this.normals[i++] = normal.x;
            this.normals[i++] = normal.y;
            this.normals[i++] = normal.z;
        }
    }

   
}
