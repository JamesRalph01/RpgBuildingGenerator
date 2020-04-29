/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;


import java.util.ArrayList;
import util.Point;
import util.CoordSystemHelper;

/**
 *
 * @author chrisralph
 */
public class Grid extends Shape {
    
    static int MAX_GRID_ELEMENTS = 20;
    static int GRID_SPACING = 20;
    
    ArrayList<Point> points = new ArrayList<>();
    private float[] colourData;
    
    public Grid()
    {
        initPositionData();
        initColourData();
    }
    
    @Override
    public float[] getPositionData() {
        return CoordSystemHelper.deviceToOpenGLf(points);
    }

    @Override
    public float[] getColourData() {
        return this.colourData;    
    }
    
    public Point getNearestGridPoint(Point pointToTest) {
        Point nearestGridPoint = new Point(32000,32000);
        for (Point point : points) {
            if (pointToTest.distance(point) < pointToTest.distance(nearestGridPoint)) {
                nearestGridPoint = point;

            }            
        };
        /*System.out.printf("Point to check: %d, %d \n", pointToTest.x, pointToTest.y);
        System.out.printf("Nearest point: %d, %d \n", nearestGridPoint.x, nearestGridPoint.y);
        System.out.printf("distance: %f \n", pointToTest.distance(nearestGridPoint)); */
        return nearestGridPoint;
    }
    
    public int numbervertices() {
        return points.size();
    }
    
    private void initPositionData() {
        for (int x=0; x<MAX_GRID_ELEMENTS; x++) {
            for (int y=0; y<MAX_GRID_ELEMENTS; y++){
                points.add(new Point(GRID_SPACING+x*GRID_SPACING,GRID_SPACING+y*GRID_SPACING));
            }
        }
    }
   
    private void initColourData() {
         int i = 0;
         
         // Green 50, 168, 82
         colourData = new float[points.size() * 3];   
         for (Point point : points) {
             colourData[i++] = 1.0f; //R
             colourData[i++] = 1.0f; //G
             colourData[i++] = 1.0f; //B
         }
    }
    
}
