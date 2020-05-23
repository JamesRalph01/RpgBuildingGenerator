/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import java.util.ArrayList;
import org.joml.GeometryUtils;
import org.joml.Rectangled;
import org.joml.Vector3f;
import util.Point;
import util.Triangulate;

/**
 *
 * @author chrisralph
 */
public class Floor extends BuildingItem {
    
    public float[] positions;
    public float[] textCoords;
    public int[] indices;
    public float[] normals;
    
    private ArrayList<Point> points;
    private Rectangled boundingRect;
    
    public Floor() {
        super();
        this.rootPath = "floor/";
        this.textures = new String[1];
    }
    
    public void setExternalPoints(ArrayList<Point> points) {
        this.points = points;
    }
    
    public void setBoundingRect(Rectangled boundingRect) {
        this.boundingRect = boundingRect;
    }
    
    public void Generate3DPositionsInternal(Vector3f screenOrigin, int wealthInd) {
        generatePositions();
        calcLocation(screenOrigin);
        calcTextureCoords();
        calcNormals();
        chooseTexture(wealthInd);
    }
        
    private void calcLocation(Vector3f screenOrigin) {
        // This looks wrong - should be negative
        this.setLocation(-screenOrigin.x, -screenOrigin.y, screenOrigin.z);        
    }
    
    private void generatePositions() {
        ArrayList<Point> triangles;        
        triangles = Triangulate.computeTriangles(points);

        positions = new float[triangles.size() * 3]; // 3 entries per point (x,y,z)
        textCoords = new float[triangles.size() * 2]; // 2 entries per point (u,v)
        indices = new int[triangles.size()]; // 1 entry per point
       
        int p = 0;
        int ind = 0;
        for (Point point : triangles) {
            indices[ind++] = triangles.indexOf(point);
            positions[p++] = (float) point.x;
            positions[p++] = 0.0f;
            positions[p++] = (float) point.y; // Z!  
        }

    }
    
    private void chooseTexture(int wealthInd) {
        // this.textures[0] = "Parquet_flooring.png";
        this.textures[0] = "Light_wooden_parquet_flooring.png";
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
    
    private void calcTextureCoords() {

        int t = 0;
        
        float textureWidth = 11;
        float textureHeight = 11;
        
        float normalisedX = 1.0f / textureWidth;
        float normalisedY = 1.0f / textureHeight;

        for (int i=0; i < positions.length; i+=3) {            
            float vX = positions[i] - (float) this.boundingRect.minX;
            float vY = positions[i+2] - (float) this.boundingRect.minY; // Z
            float u = toU(vX);
            float v = toV(vY);
            
            this.textCoords[t++] = u;
            this.textCoords[t++] = v;   

            // Convert  coord to texture coord
            //this.textCoords[t++] = normalisedX * (vX - (Math.floorDiv((long)vX, (long)textureWidth) * textureWidth));     
            //this.textCoords[t++] = normalisedY * (vY - (Math.floorDiv((long)vY, (long)textureWidth) * textureWidth));
            
        }
    }
    
    private float toU(float deviceCoordX) {
        return (1.0f / (float) this.boundingRect.lengthX())  * deviceCoordX;
    }
    
    private float toV(float deviceCoordY) { 
        return 1.0f - (1.0f / (float) this.boundingRect.lengthY()  * deviceCoordY);
    }
}
