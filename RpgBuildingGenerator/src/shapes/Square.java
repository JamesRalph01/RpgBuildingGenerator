/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

/**
 *
 * @author James
 */
public class Square extends Shape {

    private float[] positionData = {
        -0.9f, 0.2f, 0.0f,
        -0.5f, 0.2f, 0.0f,
        -0.5f, 0.9f, 0.0f,
        -0.9f, 0.9f, 0.0f
    };
    
    private float[] colourData = {
        1.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 1.0f
    };
    
    @Override
    public float[] getPositionData() {
        return this.positionData;
    }

    @Override
    public float[] getColourData() {
        return this.colourData;    
    }
    
    
    
}
