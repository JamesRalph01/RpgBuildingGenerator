/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.util.ArrayList;
import org.joml.Vector2i;
import util.CoordSystemHelper;
/**
 *
 * @author chrisralph
 */
public class Room extends Shape {

    ArrayList<Vector2i> points;
    private float[] colourData;
    
    @Override
    public float[] getPositionData() {
        return CoordSystemHelper.deviceToOpenGLf(points);
    }

    @Override
    public float[] getColourData() {
        return this.colourData;   
    }
    
    public int numbervertices() {
        return points.size();
    }

    private void initColourData() {
        int i = 0;

        // Green 50, 168, 82
        colourData = new float[points.size() * 3];   
        for (Vector2i point : points) {
            colourData[i++] = 102f/255f;
            colourData[i++] = 224f/255f;
            colourData[i++] = 20f/255f;   
        }
    }
    
}
