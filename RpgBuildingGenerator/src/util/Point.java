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
public class Point extends Vector2i {
    
    public enum Scope {
        NA, INTERNAL, EXTERNAL
    }
    public Point.Scope scope = Scope.NA;
    public int orginalIndex = 0; // for sorting adjustment points.  TODO move to a new subclass of point
    
    public Point(int x, int y) {
        super(x,y);
    }
    
    public Point(double x, double y) {
        super((int) x,(int) y);
    }
    
    public Point(Point p) {
        super(p);
        this.scope = p.scope;
    }
    
    public Point() {
        super();
    }  
    
}
