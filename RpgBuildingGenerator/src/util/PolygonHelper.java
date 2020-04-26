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
        
        int result;
                
        if (polygon.size() > 1) {
            do {
                edgeFrom.x = polygon.get(i-1).x;
                edgeFrom.y = polygon.get(i-1).y;               
                edgeTo.x = polygon.get(i).x;
                edgeTo.y = polygon.get(i).y;
                
                // Check rect top edge

                result = Intersectiond.intersectLineSegmentAar(edgeFrom.x, edgeFrom.y,
                                                          edgeTo.x, edgeTo.y,                      
                                                          rectToTest.minX, rectToTest.minY,
                                                          rectToTest.maxX, rectToTest.minY,
                                                          intersectionPoint);
                if (result != Intersectiond.OUTSIDE) {
                    return false;
                }
                i++;
            } while (i < polygon.size());
             
            // check closing edge (i.e. last point to fist)
            edgeFrom.x = polygon.get(i-1).x;
            edgeFrom.y = polygon.get(i-1).y;               
            edgeTo.x = polygon.get(0).x;
            edgeTo.y = polygon.get(0).y;               
            result = Intersectiond.intersectLineSegmentAar(edgeFrom.x, edgeFrom.y, 
                                                            edgeTo.x, edgeTo.y,
                                                            rectToTest.minX, rectToTest.minY, 
                                                            rectToTest.maxX, rectToTest.minY,
                                                            intersectionPoint);
            if (result != Intersectiond.OUTSIDE) return false;
        }
        return true;
    }
    
    public Rect findLargestRect() {
        ArrayList<Vector2i> pointsToCheck = new ArrayList<>();
        ArrayList<Rectangled> rectangles = new ArrayList<>();
        Rectangled largestRect = new Rectangled(0,0,0,0);
        Rectangled bounds;
        Vector2i pointToCheck;

        bounds = this.boundingRect();
        pointsToCheck = calcPointstoCheck(bounds);
        
        // find largest rect for each sample point
        for (Vector2i point : pointsToCheck) {
            rectangles.add(calcLargestRect(point));
        }
        
        // Now pick best candidate based on size and aspect ratio

        for (Rectangled rect: rectangles) {
            double areaLargest, areaToCheck, aspectRatio;
            double w, h;
            
            w = rect.maxX - rect.minX;
            h = rect.maxY = rect.minX;
            areaToCheck =  w * h;
            aspectRatio = Math.max(w/h, h/w);
            
            w = largestRect.maxX - largestRect.minX;
            h = largestRect.maxY = largestRect.minY;
            areaLargest =  w * h;
            
            if (aspectRatio > 0.3 ) {
                if (areaToCheck > areaLargest) {
                    largestRect = rect;
                }
            }          
        }
        
        return new Rect(largestRect.minX, 
                        largestRect.minY, 
                        Math.abs(largestRect.maxX - largestRect.minX), 
                        Math.abs(largestRect.maxY - largestRect.minY));
    }

    private ArrayList<Vector2i> calcPointstoCheck(Rectangled bounds) {
    
            ArrayList<Vector2i> pointsToCheck = new ArrayList<>();
        
            // Create 100 sample points within the bounding rectangle
            for (int i=0; i<100; i++) {
                Vector2i point = new Vector2i();
                point.x = (int) ((Math.random() * (bounds.maxX-bounds.minX)) + bounds.minX);
                point.y = (int) ((Math.random() * (bounds.maxY-bounds.minY)) + bounds.minX);
                if (this.isPointInsidePolygon(point)) {
                    pointsToCheck.add(point);                   
                }
            }
            return pointsToCheck;
    }
    
    private Rectangled calcLargestRect(Vector2i pointToCheck) {
        Rectangled rect = new Rectangled(pointToCheck.x, pointToCheck.y, pointToCheck.x, pointToCheck.y);
        
        // expand rectange until it intersects with polygon edge on both width and height.
        boolean expandwidthL = true;
        boolean expandwidthR = true;
        boolean expandheightT = true;
        boolean expandheightB = true;
        
        do
        {   
            Rectangled rectToTest = new Rectangled(rect);

            // Check if height and width can be expanded
            rectToTest.minY -= 1.0;
            if (expandheightT && this.isRectInsidePolygon(rectToTest) == false) {
                expandheightT = false;
            }
            rectToTest = new Rectangled(rect);
            rectToTest.maxY += 1.0;
            if (expandheightB && this.isRectInsidePolygon(rectToTest) == false) {
                expandheightB = false;
            }
            rectToTest = new Rectangled(rect);
            rectToTest.minX -= 1.0;
            if (expandwidthL && this.isRectInsidePolygon(rectToTest) == false) {
                expandwidthL = false;
            }
            rectToTest = new Rectangled(rect);
            rectToTest.maxX += 1.0;
            if (expandwidthR && this.isRectInsidePolygon(rectToTest) == false) {
                expandwidthR = false;
            }

            if (expandwidthL) rect.minX -= 1.0;
            if (expandwidthR) rect.maxX += 1.0;
            if (expandheightT) rect.minY -= 1.0;
            if (expandheightB) rect.maxY += 1.0;

        } while (expandwidthL || expandwidthR || expandheightT || expandheightB);   
        
        System.out.printf("Found rect x1 %f, y1 %f, x2 %f, y2 %f\n", rect.minX, rect.minY, rect.maxX, rect.maxY);
        return rect;
    }
    
    public Rectangled boundingRect() {
        Rectangled bounds = new Rectangled();
        boolean firstPoint = true;
          
        for (Vector2i point : polygon) {
            if (firstPoint) {
                bounds.minX = point.x;
                bounds.maxX = point.x;
                bounds.minY = point.y;
                bounds.maxY = point.x;
                firstPoint = false;
            } else {
                bounds.minX = Math.min(bounds.minX, point.x);
                bounds.maxX = Math.max(bounds.maxX, point.x);
                bounds.minY = Math.min(bounds.minY, point.y);
                bounds.maxY = Math.max(bounds.maxY, point.y);
            }
        }
        System.out.printf("Bounding rect x1 %f, y1 %f, x2 %f, y2 %f\n", bounds.minX, bounds.minY, bounds.maxX, bounds.maxY);
 
        return bounds;
     }
}
