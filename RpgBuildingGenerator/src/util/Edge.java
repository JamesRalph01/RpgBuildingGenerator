/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import org.joml.Intersectiond;
import org.joml.Rectangled;
import org.joml.Vector2d;

/**
 *
 * @author chrisralph
 */
public class Edge {

    public enum EdgeAlignment {
        HORIZONTAL, VERTICAL, SLANTED
    }
            
    private Point[] points;
    private boolean isInternal = false;
    private ArrayList<Edge> connectedEdges;
    
    public Edge(Edge edgeToCopy) {
        this(edgeToCopy.point1(), 
             edgeToCopy.point2(), 
             edgeToCopy.isInternal);
    }
    
    public Edge(Point point1, Point point2) {
        points = new Point[2];
        connectedEdges = new ArrayList<>();
        
        points[0] = new Point(point1);
        points[1] = new Point(point2);
    }
    
    public Edge(Point point1, Point point2, Rect bounds) {
        this(point1,point2);
        calcExternalPoints(bounds);
    }
        
    public Edge(Point point1, Point point2, boolean isInternal) {
       this(point1, point2);
       this.isInternal = isInternal;
    }
  
    public void calcExternalPoints(Rect bounds) {
        // check if edge lies on bounding rectangle

        for (int i = 0; i < 2; i++) {
            Point p = points[i];

            // does the point lie on the rect ?
            p.scope = Point.Scope.INTERNAL;
            
            if (this.alignment() == Edge.EdgeAlignment.HORIZONTAL) {
                if (p.x == (int)bounds.x || p.x == (int)bounds.x+bounds.w) {
                    if (p.y >= (int)bounds.y && p.y <= (int)bounds.y+bounds.h){
                        p.scope = Point.Scope.EXTERNAL;                        
                    }
                }
            } else {
                if (p.y == (int)bounds.y || p.y == (int)bounds.y+bounds.h) {
                    if (p.x >= (int)bounds.x && p.x <= (int)bounds.x+bounds.w){
                        p.scope = Point.Scope.EXTERNAL;                  
                    }
                }   
            }
            
        }
    }
    
    public EdgeAlignment alignment() {
        if (points[0].x == points[1].x) return EdgeAlignment.VERTICAL;
        if (points[0].y == points[1].y) return EdgeAlignment.HORIZONTAL;
        return EdgeAlignment.SLANTED;        
    }
    
    public int x1(){
        return points[0].x;
    }
    
    public int y1(){
        return points[0].y;
    }
    
    public int x2(){
        return points[1].x;
    }
    
    public int y2(){
        return points[1].y;
    }
    
    public Point point1() {
        return points[0];
    }
    
    public Point point2() {
        return points[1];
    }
    
    public boolean isInternal() {
        return this.isInternal;
    }
    
    public void isInternal(boolean isInternal) {
        this.isInternal = isInternal;
    }
    
    public boolean intersets(Rectangled rect) {
        int result;
        
        Vector2d intersectionPoint = new Vector2d();
        
        result = Intersectiond.intersectLineSegmentAar((double)this.x1(), (double)this.y1(),
                                                      (double)this.x2(), (double)this.y2(),                      
                                                      rect.minX, rect.minY,
                                                      rect.maxX, rect.maxY,
                                                      intersectionPoint);
        return result != Intersectiond.OUTSIDE;
    }
    
    public boolean intersets(Edge edgeToCheck) {
        Rectangled rect;
        int result;
        
        rect = new Rectangled();
        rect.minX = Math.min((double) edgeToCheck.x1(), (double) edgeToCheck.x2());
        rect.maxX = Math.max((double) edgeToCheck.x1(), (double) edgeToCheck.x2());
        rect.minY = Math.min((double) edgeToCheck.y1(), (double) edgeToCheck.y2());
        rect.maxY = Math.max((double) edgeToCheck.y1(), (double) edgeToCheck.y2());
        
        Vector2d intersectionPoint = new Vector2d();
        
        result = Intersectiond.intersectLineSegmentAar((double)this.x1(), (double)this.y1(),
                                                      (double)this.x2(), (double)this.y2(),                      
                                                      rect.minX, rect.minY,
                                                      rect.maxX, rect.maxY,
                                                      intersectionPoint);
        return result != Intersectiond.OUTSIDE;
    }
    
    public ArrayList<Edge> connectedEdges() {
        return this.connectedEdges;
    }
    
    public void shrink(int delta) {
        if (this.x1() == this.x2()) {
            //shrink y
            if (Math.min(this.y1(), this.y2()) == this.y1()) {
                points[0].y += delta;
                points[1].y -= delta;
            } else {
                points[0].y -= delta;
                points[1].y += delta;             
            }              
        } else {
            //shrink x
            if (Math.min(this.x1(), this.x2()) == this.x1()) {
                points[0].x += delta;
                points[1].x -= delta;
            } else {
                points[0].x -= delta;
                points[1].x += delta;             
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Edge edge = (Edge) o;
        // field comparison
        return this.point1().equals(edge.point1()) && this.point2().equals(edge.point2());
    }
    
}
