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
public class Circle extends Shape{

    private float x_pos;
    private float y_pos;
    private float radius;
    private int numSegments;
    private float[] positionData;
    private float[] colourData;      
    
    public Circle(float x_pos, float y_pos, float radius, int numSegments) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.radius = radius;
        this.numSegments = numSegments;
        this.positionData = new float[3*numSegments];
        generatePositionData();
        this.colourData = new float[3*numSegments];
        generateColourData();
    }
    
    private void generatePositionData() {
        for (int i = 0; i < this.numSegments; i++) {
            float theta = 2.0f * (float)Math.PI * (float)i / (float)this.numSegments;
            
            float x = this.radius * (float)Math.cos((double)theta); // calculate x vertex coord
            float y = this.radius * (float)Math.sin((double)theta); // calculate y vertex coord
            float z = 0.0f;
            
            this.positionData[i*3] = x + this.x_pos;
            this.positionData[(i*3)+1] = y + this.y_pos;
            this.positionData[(i*3)+2] = z;
        }
    }
    
    private void generateColourData() {
        for (int i = 0; i < this.numSegments; i++) {
            this.colourData[i*3] = 1.0f;
            this.colourData[(i*3)+1] = 0.0f;
            this.colourData[(i*3)+2] = 0.0f;
        }
    }
    
    @Override
    public float[] getPositionData() {
        return this.positionData;   
    }

    @Override
    public float[] getColourData() {
        return this.colourData;    
    }
    
}
