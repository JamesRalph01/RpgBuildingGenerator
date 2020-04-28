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
import org.joml.Vector2i;

/**
 *
 * @author chrisralph
 */
public class Edge {

    private Vector2i[] points;
    private boolean isInternal = false;
    private ArrayList<Edge> connectedEdges;
    
    public Edge(Edge edgeToCopy) {
        this(edgeToCopy.point1(), 
             edgeToCopy.point2(), 
             edgeToCopy.isInternal);
    }
    
    public Edge(Vector2i point1, Vector2i point2) {
        points = new Vector2i[2];
        connectedEdges = new ArrayList<>();
        
        points[0] = new Vector2i(point1);
        points[1] = new Vector2i(point2);
    }
    
    public Edge(Vector2i point1, Vector2i point2, boolean isInternal) {
       this(point1, point2);
       this.isInternal = isInternal;
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
    
    public Vector2i point1() {
        return points[0];
    }
    
    public Vector2i point2() {
        return points[1];
    }
    
    public boolean isInternal() {
        return this.isInternal;
    }
    
    public void isInternal(boolean isInside) {
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
            } else {
                points[1].y -= delta;             
            }              
        } else {
            //shrink x
            if (Math.min(this.x1(), this.x2()) == this.x1()) {
                points[0].x += delta;
            } else {
                points[1].x -= delta;             
            }
        }
    }
    
}
