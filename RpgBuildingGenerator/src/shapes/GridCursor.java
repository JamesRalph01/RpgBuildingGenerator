/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

public class GridCursor extends Shape {
    
    private float[] positionData = {
        -0.025f, 0.0f, 0.0f,
        0.025f, 0.0f, 0.0f,
        0.0f, -0.025f, 0.0f,
        0.0f, 0.025f, 0.0f
    };
    
    private float[] colourData = {
        245f/255f, 250f/255f, 90f/255f,
        245f/255f, 250f/255f, 90f/255f,
        245f/255f, 250f/255f, 90f/255f,
        245f/255f, 250f/255f, 90f/255f
    };
    
    @Override
    public float[] getPositionData() {
        return this.positionData;
    }

    @Override
    public float[] getColourData() {
        return this.colourData;    
    }
   
    public int numbervertices() {
        return 4;
    }
}

