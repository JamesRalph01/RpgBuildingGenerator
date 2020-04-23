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

class GeomEdge{
    
    int xmin, xmax; /* horiz, +x is right */
    int ymin, ymax; /* vertical, +y is down */
    double m,b; /* y = mx + b */
    boolean isTop, isRight; /* position of edge w.r.t. hull */
    
    public GeomEdge(Vector2i p, Vector2i q){
        this.xmin = Math.min(p.x, q.x);
        this.xmax = Math.max(p.x, q.x);
        this.ymin = Math.min(p.y, q.y);
        this.ymax = Math.max(p.y, q.y);
        this.m = ((double)(q.y-p.y))/((double)(q.x-p.x));
        this.b = p.y - m*(p.x);
        this.isTop = p.x > q.x; //edge from right to left (ccw)
        this.isRight = p.y > q.y; //edge from bottom to top (ccw)
    }
}