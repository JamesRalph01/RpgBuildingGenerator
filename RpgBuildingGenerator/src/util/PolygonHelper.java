/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import org.joml.Intersectiond;
import org.joml.PolygonsIntersection;
import org.joml.Rectangled;
import org.joml.Vector2d;
import org.joml.Vector2i;

/**
 *
 * @author chrisralph
 */
public class PolygonHelper {

    private PolygonsIntersection ppi;
    private final ArrayList<Vector2i> polygon;
    
    public PolygonHelper(ArrayList<Vector2i> points) {
        
        polygon = points;

        //convert to float array
        float[] vertices = new float[points.size() * 2]; // for x and y values
        int[] nPolygons = {0};
        int nVertices = points.size();
        
        int i = 0;
        for (Vector2i point : points) {
            vertices[i++] = point.x;
            vertices[i++] = point.y;
        }
        ppi = new PolygonsIntersection(vertices, nPolygons, nVertices);
    }
    
    public  boolean isPointInsidePolygon(Vector2i pointToTest) {
        return ppi.testPoint((float)pointToTest.x, (float)pointToTest.y);
    }
    
    public boolean isRectInsidePolygon(Rectangled rectToTest) {
        // Are all points inside Polygon?
        if (this.isPointInsidePolygon(new Vector2i((int)rectToTest.minX, (int)rectToTest.minY)) == false) return false;
        if (this.isPointInsidePolygon(new Vector2i((int)rectToTest.maxX, (int)rectToTest.minY)) == false) return false;
        if (this.isPointInsidePolygon(new Vector2i((int)rectToTest.maxX, (int)rectToTest.maxY)) == false) return false;
        if (this.isPointInsidePolygon(new Vector2i((int)rectToTest.minX, (int)rectToTest.maxY)) == false) return false;
        
        // Check if any sides of the rectangle intersect with the polygon        
        int i = 1;
        Vector2d edgeFrom = new Vector2d();
        Vector2d edgeTo = new Vector2d();
        Vector2d intersectionPoint = new Vector2d();
                
        if (polygon.size() > 1) {
             do {
                edgeFrom.x = polygon.get(i-1).x;
                edgeFrom.y = polygon.get(i-1).y;               
                edgeTo.x = polygon.get(i).x;
                edgeTo.y = polygon.get(i).y;
                
                // Check rect top edge
                if (Intersectiond.intersectLineLine(rectToTest.minX, rectToTest.minY, 
                                                    rectToTest.maxX, rectToTest.minY,
                                                    edgeFrom.x, edgeFrom.y, 
                                                    edgeTo.x, edgeTo.y,
                                                    intersectionPoint)) {
                    return false;
                }
                // Check rect right edge
                if (Intersectiond.intersectLineLine(rectToTest.maxX, rectToTest.minY, 
                                                    rectToTest.maxX, rectToTest.maxY,
                                                    edgeFrom.x, edgeFrom.y, 
                                                    edgeTo.x, edgeTo.y,
                                                    intersectionPoint)) {
                    return false;
                }
                // rect bottom
                if (Intersectiond.intersectLineLine(rectToTest.minX, rectToTest.maxY, 
                                                    rectToTest.maxX, rectToTest.maxY,
                                                    edgeFrom.x, edgeFrom.y, 
                                                    edgeTo.x, edgeTo.y,
                                                    intersectionPoint)) {
                    return false;
                }
                // rect left
                if (Intersectiond.intersectLineLine(rectToTest.minX, rectToTest.minY, 
                                                    rectToTest.minX, rectToTest.maxY,
                                                    edgeFrom.x, edgeFrom.y, 
                                                    edgeTo.x, edgeTo.y,
                                                    intersectionPoint)) {
                    return false;
                }
                i++;
             } while (i < polygon.size());
             
             // check closing edge (i.e. last point to fist)
             edgeFrom.x = polygon.get(i-1).x;
             edgeFrom.y = polygon.get(i-1).y;               
             edgeTo.x = polygon.get(0).x;
             edgeTo.y = polygon.get(0).y;               
             if (Intersectiond.intersectLineLine(rectToTest.minX, rectToTest.minY, 
                                                 rectToTest.maxX, rectToTest.minY,
                                                 edgeFrom.x, edgeFrom.y, 
                                                 edgeTo.x, edgeTo.y,
                                                 intersectionPoint)) {
                 return false;
             }
             if (Intersectiond.intersectLineLine(rectToTest.maxX, rectToTest.minY, 
                                                 rectToTest.maxX, rectToTest.maxY,
                                                 edgeFrom.x, edgeFrom.y, 
                                                 edgeTo.x, edgeTo.y,
                                                 intersectionPoint)) {
                 return false;
             } 
             if (Intersectiond.intersectLineLine(rectToTest.minX, rectToTest.maxY, 
                                                 rectToTest.maxX, rectToTest.maxY,
                                                 edgeFrom.x, edgeFrom.y, 
                                                 edgeTo.x, edgeTo.y,
                                                 intersectionPoint)) {
                 return false;
             } 
             if (Intersectiond.intersectLineLine(rectToTest.minX, rectToTest.minY, 
                                                 rectToTest.minX, rectToTest.maxY,
                                                 edgeFrom.x, edgeFrom.y, 
                                                 edgeTo.x, edgeTo.y,
                                                 intersectionPoint)) {
                 return false;
             } 
        }
        return true;

    }
}
