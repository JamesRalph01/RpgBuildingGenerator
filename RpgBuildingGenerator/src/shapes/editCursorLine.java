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
public class editCursorLine extends Shape {

    ArrayList<Point> points;
    // Gold 252, 186, 3
    private float[] colourData = {
        252f/255f, 186f/255f, 3f/255f,
        252f/255f, 186f/255f, 3f/255f
    };
    
    public editCursorLine()
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
    
    public void fromPoint(Point point) {
        points.get(0).x = point.x;
        points.get(0).y = point.y;
    }
    
    public void ToPoint(Point point) {
        points.get(1).x = point.x;
        points.get(1).y = point.y;
    }
    
    private void initPositionData() {

        points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 0));
    }
}
