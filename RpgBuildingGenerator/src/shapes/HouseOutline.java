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
        System.out.printf("House outline added: %.2f, %.2f \n", point.x, point.y);
    }
    
    public void clear() {
        points.clear();
    }
    
    public int size() {
        return points.size();
    }
    
    @Override
    public float[] getPositionData() {
        float positionData[] = new float[points.size()*3];
        int i=0;
        for (Vector2f point : points) {
            positionData[i++] = point.x;
            positionData[i++] = point.y;
            positionData[i++] = 1.0f;  
        }
        return positionData;
    }

    @Override
    public float[] getColourData() {
        float[] colourData = new float[points.size()*3];
        int i=0;
        for (Vector2f point : points) {
            colourData[i++] = 1.0f; //R
            colourData[i++] = 1.0f; //G
            colourData[i++] = 1.0f; //B
        }
        return colourData;
    }
    
    public int numbervertices() {
        return points.size();
    }
   
}

