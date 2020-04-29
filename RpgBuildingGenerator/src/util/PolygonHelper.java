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
import org.joml.Vector3d;

/**
 *
 * @author chrisralph
 */
public class PolygonHelper {

    private PolygonsIntersection ppi;
    private ArrayList<Edge> edges;
    private final ArrayList<Point> polygon;
    
    public PolygonHelper(ArrayList<Point> points) {
        
        polygon = points;

        //convert to float array
        float[] vertices = new float[points.size() * 2]; // for x and y values
        int[] nPolygons = {0};
        int nVertices = points.size();
        
        int i = 0;
        for (Point point : points) {
            vertices[i++] = point.x;
            vertices[i++] = point.y;
        }
        
        calcEdges();
        ppi = new PolygonsIntersection(vertices, nPolygons, nVertices);
    }
    
    public ArrayList<Edge> edges() {
        return this.edges;
    }
    
    public ArrayList<Point> points() {
        return this.polygon;
    }
    
    public  boolean isPointInsidePolygon(Point pointToTest) {
        return ppi.testPoint((float)pointToTest.x, (float)pointToTest.y);
    }

    public boolean isRectInsidePolygon(Rectangled rectToTest) {
        int result;
        
        // Are all points inside Polygon?
        if (this.isPointInsidePolygon(new Point((int)rectToTest.minX, (int)rectToTest.minY)) == false) return false;
        if (this.isPointInsidePolygon(new Point((int)rectToTest.maxX, (int)rectToTest.minY)) == false) return false;
        if (this.isPointInsidePolygon(new Point((int)rectToTest.maxX, (int)rectToTest.maxY)) == false) return false;
        if (this.isPointInsidePolygon(new Point((int)rectToTest.minX, (int)rectToTest.maxY)) == false) return false;
        
        // Check if any sides of the rectangle intersect with the polygon        
        Vector2d intersectionPoint = new Vector2d();

        for (Edge edge: edges) {
            result = Intersectiond.intersectLineSegmentAar((double)edge.x1(), (double)edge.y1(),
                                                          (double)edge.x2(), (double)edge.y2(),                      
                                                          rectToTest.minX, rectToTest.minY,
                                                          rectToTest.maxX, rectToTest.maxY,
                                                          intersectionPoint);
            if (result != Intersectiond.OUTSIDE) {
                return false;
            }    
        }
        return true;
    }
    
    public Rect findLargestRect() {
        ArrayList<Point> pointsToCheck;
        ArrayList<Rectangled> rectangles = new ArrayList<>();
        Rectangled largestRect = new Rectangled(0,0,0,0);
        Rectangled bounds;
        Point pointToCheck;

        bounds = this.boundingRect();
        pointsToCheck = calcPointstoCheck(bounds);
        
        // find largest rect for each sample point
        for (Point point : pointsToCheck) {
            rectangles.add(calcLargestRect(point));
        }
        
        // Now pick best candidate based on size and aspect ratio

        for (Rectangled rect: rectangles) {
            double areaLargest, areaToCheck, aspectRatio;
            double w, h;
            
            w = Math.abs(rect.maxX - rect.minX);
            h = Math.abs(rect.maxY - rect.minY);
            areaToCheck =  w * h;
            aspectRatio = Math.max(w/h, h/w);
            
            w = largestRect.maxX - largestRect.minX;
            h = largestRect.maxY - largestRect.minY;
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

    public Rect findLargestRect(Point fromPoint) {

        Rect largestRect;
        Rectangled rect;
        
        rect = calcLargestRect(fromPoint);
        
        return new Rect(rect.minX, 
                        rect.minY, 
                        Math.abs(rect.maxX - rect.minX), 
                        Math.abs(rect.maxY - rect.minY));
    }
    
    private ArrayList<Point> calcPointstoCheck(Rectangled bounds) {
    
            ArrayList<Point> pointsToCheck = new ArrayList<>();
        
            // Create 100 sample points within the bounding rectangle
            for (int i=0; i<100; i++) {
                Point point = new Point();
                point.x = (int) ((Math.random() * (bounds.maxX-bounds.minX)) + bounds.minX);
                point.y = (int) ((Math.random() * (bounds.maxY-bounds.minY)) + bounds.minX);
                if (this.isPointInsidePolygon(point)) {
                    pointsToCheck.add(point);                   
                }
            }
            return pointsToCheck;
    }
    
    private Rectangled calcLargestRect(Point pointToCheck) {
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
          
        for (Point point : polygon) {
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
    
    public Edge closestEdge(Point point) {
        Edge nearestEdge = null;
        Point nearestPoint = new Point();
        boolean first = true;

        for (Edge edge: this.edges()) {

            Vector3d result = new Vector3d();
            Intersectiond.findClosestPointOnLineSegment((double)edge.x1(), (double)edge.y1(), 0, 
                                                        (double)edge.x2(), (double)edge.y2(), 0, 
                                                        (double)point.x, (double)point.y, 0, 
                                                        result);
            if (first) {
                nearestEdge = edge;
                nearestPoint = new Point((int)result.x, (int)result.y);
                first = false;
            } else {
                // found closer edge?
                if (point.distance(new Point((int)result.x, (int)result.y)) < 
                    point.distance(nearestPoint)) {
                    nearestEdge = edge;        
                    nearestPoint = new Point((int)result.x, (int)result.y);
                }
            }
        }
        return nearestEdge;
    }
    
    private void calcEdges() {
        Point edgeFrom = new Point();
        Point edgeTo = new Point();
        int i = 1;
        
        edges = new ArrayList<>();
        
        if (polygon.size() > 1) {
            do {
                edgeFrom.x = polygon.get(i-1).x;
                edgeFrom.y = polygon.get(i-1).y;               
                edgeTo.x = polygon.get(i).x;
                edgeTo.y = polygon.get(i).y;
                i++;
                edges.add(new Edge(edgeFrom, edgeTo));
                
            } while (i < polygon.size());
             
            // check closing edge (i.e. last point to fist)
            edgeFrom.x = polygon.get(i-1).x;
            edgeFrom.y = polygon.get(i-1).y;               
            edgeTo.x = polygon.get(0).x;
            edgeTo.y = polygon.get(0).y;
            edges.add(new Edge(edgeFrom, edgeTo));
        }  
    }
}
