/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
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
    private Color colour = new Color(116, 235, 52);
    
    public Point(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }
    
    public Point(double x, double y) {
        super();
        this.x = (int) x;
        this.y = (int) y;
    }
    
    public Point(Point p) {
        super();
        this.x = p.x;
        this.y = p.y;
        this.scope = p.scope;
        this.colour = p.colour;
    }
    
    public Point() {
        super();
    }
    
    public Color getColour() {
        return this.colour;
    }
    
    public void setColour(int R, int G, int B) {
        this.colour = new Color(R,G,B);
    }
    
    public void setColour(Color colour) {
        this.colour = colour;
    }
    
    public float getRedf() {
        return (float) colour.getRed() / 255f;
    }

    public float getGreenf() {
        return (float) colour.getGreen() / 255f;
    }
    
    public float getBluef() {
        return (float) colour.getBlue() / 255f;
    }
    
}
