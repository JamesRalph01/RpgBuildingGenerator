/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import org.joml.PolygonsIntersection;
import org.joml.Vector2i;

/**
 *
 * @author chrisralph
 */
public class PolygonHelper {

    private PolygonsIntersection ppi;
    
    public PolygonHelper(ArrayList<Vector2i> points) {
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
}
