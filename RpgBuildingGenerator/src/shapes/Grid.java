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
public class Grid extends Shape {
    
    static int MAX_GRID_ELEMENTS = 20;
    static int GRID_SPACING = 20;
    
    ArrayList<Vector2i> points = new ArrayList<>();
    
    private float[] positionData;
    private float[] colourData;
    
    public Grid()
    {
        initPositionData();
        initColourData();
    }
    
    @Override
    public float[] getPositionData() {
        return this.positionData;
    }

    @Override
    public float[] getColourData() {
        return this.colourData;    
    }
    
    public Vector2i getNearestGridPoint(Vector2i pointToTest) {
        Vector2i nearestGridPoint = new Vector2i(32000,32000);
        for (Vector2i point : points) {
            if (pointToTest.distance(point) < pointToTest.distance(nearestGridPoint)) {
                nearestGridPoint = point;
                /* System.out.printf("Point to check: %.2f, %.2f \n", point.x, point.y);
                System.out.printf("Grid point: %.2f, %.2f \n", gridPoint.x, gridPoint.y);
                System.out.printf("Nearest point: %.2f, %.2f \n", nearestGridPoint.x, nearestGridPoint.y);
                System.out.printf("distance: %.2f \n", point.distance(gridPoint)); */
            }            
        };
        return nearestGridPoint;
    }
    
    public int numbervertices() {
        return points.size();
    }
    
    private void initPositionData() {
        for (int x=20; x<MAX_GRID_ELEMENTS; x+=GRID_SPACING) {
            for (int y=20; y<MAX_GRID_ELEMENTS; y+=GRID_SPACING){
                points.add(new Vector2i(x,y));
            }
        }
        // Cache position data in OpenGL coord system as it won't change
        positionData = CoordSystemHelper.deviceToOpenGLf(points);
    }
   
    private void initColourData() {
         int i = 0;
         
         // Green 50, 168, 82
         colourData = new float[points.size() * 3];   
         for (Vector2i point : points) {
             colourData[i++] = 50f/255f; //R
             colourData[i++] = 168f/255f; //G
             colourData[i++] = 82f/255f; //B
         }
    }
    
}
