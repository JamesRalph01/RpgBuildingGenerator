/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.delaunay;

/**
 *
 * @author chrisralph
 */
public class Edge2D {

    public Vector2D a;
    public Vector2D b;

    /**
     * Constructor of the 2D edge class used to create a new edge instance from
     * two 2D vectors describing the edge's vertices.
     * 
     * @param a
     *            The first vertex of the edge
     * @param b
     *            The second vertex of the edge
     */
    public Edge2D(Vector2D a, Vector2D b) {
        this.a = a;
        this.b = b;
    }
    
}
