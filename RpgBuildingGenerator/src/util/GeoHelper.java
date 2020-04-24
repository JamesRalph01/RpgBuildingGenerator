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
public class GeoHelper {

    public static boolean isPointInsidePolygon(ArrayList<Vector2i> polygon, Vector2i pointToTest) {
        
        PolygonsIntersection ppi;
        
        //convert to float array
        float[] vertices = new float[polygon.size() * 2]; // for x and y values
        int[] nPolygons = {0};
        int nVertices = polygon.size();
        
        int i = 0;
        for (Vector2i point : polygon) {
            vertices[i++] = point.x;
            vertices[i++] = point.y;
        }
        ppi = new PolygonsIntersection(vertices, nPolygons, nVertices);
        return ppi.testPoint((float)pointToTest.x, (float)pointToTest.y);
    }
}
