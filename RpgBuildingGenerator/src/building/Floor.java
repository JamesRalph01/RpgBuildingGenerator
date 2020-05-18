/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.GeometryUtils;
import org.joml.Rectangled;
import org.joml.Vector3f;
import util.Point;
import util.delaunay.DelaunayTriangulator;
import util.delaunay.NotEnoughPointsException;
import util.delaunay.Triangle2D;
import util.delaunay.Vector2D;

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
    private DelaunayTriangulator delaunayTriangulator;
    
    public Floor() {
        super();
    }
    
    public void setExternalPoints(ArrayList<Point> points) {
        this.points = points;
    }
    
    public void setBoundingRect(Rectangled boundingRect) {
        this.boundingRect = boundingRect;
    }
    
    public void Generate3DPositionsInternal(Vector3f screenOrigin, int wealthInd) {
        TessellateFloor();
        generatePositions();
        calcLocation(screenOrigin);
        calcTextureCoords();
        calcNormals();
        chooseTexture(wealthInd);
    }
    
    private void TessellateFloor() {
        ArrayList<Vector2D> pointSet = new ArrayList<>();
        for (Point point : this.points) {
            pointSet.add(new Vector2D(point.x, point.y));
        }
        
        delaunayTriangulator = new DelaunayTriangulator(pointSet);
        try {
            delaunayTriangulator.triangulate();
        } catch (NotEnoughPointsException ex) {
            Logger.getLogger(Floor.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
    
    private void calcLocation(Vector3f screenOrigin) {
        // This looks wrong - should be negative
        this.setLocation(-screenOrigin.x, -screenOrigin.y, screenOrigin.z);        
    }
    
    private void generatePositions() {
        
        positions = new float[delaunayTriangulator.getTriangles().size() * 3 * 3];
        indices = new int[delaunayTriangulator.getTriangles().size() * 3];
        
        
        int p = 0;
        int ind = 0;
        for (int i = 0; i < delaunayTriangulator.getTriangles().size(); i++) {
            Triangle2D triangle = delaunayTriangulator.getTriangles().get(i);
            // V1
            indices[ind++] = i * 3;
            positions[p++] = (float) triangle.a.x;
            positions[p++] = 0.0f;
            positions[p++] = (float) triangle.a.y; // Z!
            
            // V2
            indices[ind++] = i * 3 + 1;
            positions[p++] = (float) triangle.b.x;
            positions[p++] = 0.0f;
            positions[p++] = (float) triangle.b.y; // Z!

            // V3
            indices[ind++] = i * 3 + 2;
            positions[p++] = (float) triangle.c.x;
            positions[p++] = 0.0f;
            positions[p++] = (float) triangle.c.y; // Z!
        }  
        
    }
    
    private void chooseTexture(int wealthInd) {
        this.texture = "Parquet_flooring.png";
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

        this.textCoords = new float[positions.length * 2];
        
        for (int i=0; i < positions.length; i+=3) {            
            float vX = positions[i];
            float vY = positions[i+2]; // Z

            // Convert  coord to texture coord
            this.textCoords[t++] = normalisedX * (vX - (Math.floorDiv((long)vX, (long)textureWidth) * textureWidth));     
            this.textCoords[t++] = normalisedY * (vY - (Math.floorDiv((long)vY, (long)textureWidth) * textureWidth));
            
        }
    }
}
