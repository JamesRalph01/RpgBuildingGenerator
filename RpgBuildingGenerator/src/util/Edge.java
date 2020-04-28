/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.joml.Vector2i;

/**
 *
 * @author chrisralph
 */
public class Edge {

    private Vector2i[] points;
    private boolean isInside = false;
    
    public Edge(Vector2i point1, Vector2i point2) {
        points = new Vector2i[2];
        
        points[0] = new Vector2i(point1);
        points[1] = new Vector2i(point2);
    }
    
    public Edge(Vector2i point1, Vector2i point2, boolean isInside) {
       this(point1, point2);
       this.isInside = isInside;
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
    
    public boolean isInside() {
        return this.isInside;
    }
}
