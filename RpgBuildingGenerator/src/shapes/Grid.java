/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import org.joml.Vector2f;

/**
 *
 * @author chrisralph
 */
public class Grid extends Shape {
    
    static int MAX_GRID_ELEMENTS = 20 * 20 * 3;
    static int GRID_SPACING = 20;
    
    private float[] positionData;
    private float[] colourData;
    private final float normalised_point = 1.0F / (10.0F * GRID_SPACING);
    
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
    
    public Vector2f getNearestGridPoint(Vector2f point) {
        Vector2f nearestGridPoint = new Vector2f(99,99);
        for (int i=0; i<positionData.length; i+=3) {
            Vector2f gridPoint = new Vector2f();
            gridPoint.x = positionData[i];
            gridPoint.y = positionData[i+1];

            if (point.distance(gridPoint) < point.distance(nearestGridPoint)) {
                nearestGridPoint = gridPoint;
                /* System.out.printf("Point to check: %.2f, %.2f \n", point.x, point.y);
                System.out.printf("Grid point: %.2f, %.2f \n", gridPoint.x, gridPoint.y);
                System.out.printf("Nearest point: %.2f, %.2f \n", nearestGridPoint.x, nearestGridPoint.y);
                System.out.printf("distance: %.2f \n", point.distance(gridPoint)); */
            }
        }
        return nearestGridPoint;
    }
    
    public int numbervertices() {
        return positionData.length / 3;
    }
    
    private void initPositionData() {
        // Grid of points every 20 pixels
        positionData = new float[MAX_GRID_ELEMENTS];
        int i=0;
        
        for (int x=-10; x<10; x++) {
            for (int y=-10; y<10; y++){
                positionData[i++] = normalised_point * (x * GRID_SPACING); //x
                positionData[i++] = normalised_point * (y * GRID_SPACING); //y
                positionData[i++] = 1;
                System.out.println("Vertice: x:" + positionData[i-3] + " y:" + positionData[i-2] + " z:" + positionData[i-1]);
            }
        }
    }
   
    private void initColourData() {
        colourData = new float[MAX_GRID_ELEMENTS];
        for (int i=0; i<MAX_GRID_ELEMENTS; i++) {
            colourData[i] = 1;
        }
    }
    
}
