/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

/**
 *
 * @author chrisralph
 */
public class Grid extends Shape {
    
    static int MAX_GRID_ELEMENTS = 20 * 20 * 3;
    static int GRID_SPACING = 20;
    
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
    
    private void initPositionData() {
        // Grid of points every 20 pixels
        positionData = new float[MAX_GRID_ELEMENTS];
        float normalised_point = 1.0F / (10.0F * GRID_SPACING);
        int i=0;
        
        for (int x=-10; x<10; x++) {
            for (int y=-10; y<10; y++){
                positionData[i++] = normalised_point * (x * GRID_SPACING); //x
                positionData[i++] = normalised_point * (y * GRID_SPACING); //y
                positionData[i++] = 0; //z  
                System.out.println("Vertice: x:" + positionData[i-3] + " y:" + positionData[i-2] + " z:" + positionData[i-1]);
            }
        }
    }
   
    private void initColourData() {
        colourData = new float[MAX_GRID_ELEMENTS];
        for (int i=0; i<MAX_GRID_ELEMENTS; i++) {
            colourData[i] = 255;
        }
    }
    
}
