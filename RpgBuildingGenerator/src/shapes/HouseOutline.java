/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;
import java.util.ArrayList;
import org.joml.Vector2f;

public class HouseOutline extends Shape {
    
    ArrayList<Vector2f> points = new ArrayList<Vector2f>();
    
    public void addPoint(Vector2f point) {
        points.add(point);
    }
    
    @Override
    public float[] getPositionData() {
        float positionData[] = new float[points.size()*2];
        int i=0;
        for (Vector2f point : points) {
            positionData[i++] = point.x;
            positionData[i++] = point.y;        
        }
        return positionData;
    }

    @Override
    public float[] getColourData() {
        float[] colourData = new float[] { 255.0f };  
        return colourData;
    }
   
}

