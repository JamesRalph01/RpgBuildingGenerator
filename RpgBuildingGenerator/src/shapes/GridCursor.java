/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.util.ArrayList;
import org.joml.Vector2i;
import util.CoordSystemHelper;

public class GridCursor extends Shape {
    
    ArrayList<Vector2i> points;   
    private Vector2i cursorPostion = new Vector2i(0,0);    
    private float[] colourData = {
        245f/255f, 250f/255f, 90f/255f,
        245f/255f, 250f/255f, 90f/255f,
        245f/255f, 250f/255f, 90f/255f,
        245f/255f, 250f/255f, 90f/255f
    };
    
    public GridCursor()
    {
        initPositionData();
    }
    
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
    
    public void cursorPosition(Vector2i point) {
        cursorPostion = point;
        initPositionData();
    }
    
    private void initPositionData() {

        points = new ArrayList<>();
        points.add(new Vector2i(cursorPostion.x-5, cursorPostion.y));
        points.add(new Vector2i(cursorPostion.x+5, cursorPostion.y));
        points.add(new Vector2i(cursorPostion.x, cursorPostion.y-5));
        points.add(new Vector2i(cursorPostion.x, cursorPostion.y+5)); 
    }
}

